package advent.of.code.days;

import advent.of.code.Day;
import advent.of.code.IO;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

public class Day09 implements Day {
    @Override
    public void part1(IO io) {
        int[] disk = io.scanner().nextLine().chars().map(Character::getNumericValue).toArray();
        int hiPos = disk.length-1, hiId = hiPos/2, block = 0;
        long checksum = 0;
        loop: for (int pos = 0; pos <= hiPos; pos++) {
            int size = disk[pos], id = pos/2;
            if (pos%2 == 0) { // a file region
                for (int n = block+size; block < n; block++) {
                    checksum += (long) block * id;
                }
            } else { // a free region
                for (int n = block+size; block < n; block++) {
                    for (;;) {
                        if (disk[hiPos] > 0) {
                            disk[hiPos]--;
                            checksum += (long) block * hiId;
                            break;
                        } else if ((hiPos -= 2) < pos) {
                            break loop;
                        } else {
                            hiId = hiPos/2;
                        }
                    }
                }
            }
        }
        io.out().println(checksum);
    }
    
    @Override
    public void part2(IO io) {
        // Attempt to move each file ONCE, highest-id-first, to the leftmost free space that will hold it, if any.
        // Note that this implies we will never move higher ids into the gap created by moving lower ids.
        // Therefore, we can get the same effect by, at each free space, choosing the highest unused id that fits.
        // We can optimize "choosing the highest unused id that fits" by first grouping ids into ordered sets by size.
        // It helps that there are only 10 distinct sizes (digits 0-9) - a small-ish constant to loop over.
        
        int[] disk = io.scanner().nextLine().chars().map(Character::getNumericValue).toArray();
        @SuppressWarnings("unchecked")
        var buckets = (Deque<Integer>[]) IntStream.range(0, 10).mapToObj(_ -> new ArrayDeque<>()).toArray(Deque[]::new);
        for (int pos = 0; pos < disk.length; pos += 2) {
            int size = disk[pos], id = pos/2;
            buckets[size].push(id);
        }
        int block = 0, nbuckets = 9;
        long checksum = 0;
        for (int pos = 0; pos < disk.length && nbuckets > 0; pos++) {
            int size = disk[pos], id = pos/2;
            if (size < 0) { // a moved region
                block -= size;
            } else if (pos%2 == 0) { // a file region
                for (int n = block+size; block < n; block++) {
                    checksum += (long) block * id;
                }
            } else { // a free region
                for (;;) {
                    int hiId = -1, hiSize = -1;
                    for (int i = size; i > 0; i--) {
                        Integer tmp = buckets[i].peek();
                        if (tmp != null) {
                            if (tmp <= id) {
                                // Already passed this id (and the rest of the bucket, since highest id is first)
                                buckets[i].clear();
                                --nbuckets;
                            } else if (tmp > hiId) {
                                hiId = tmp;
                                hiSize = i;
                            }
                        }
                    }
                    if (hiSize == -1) {
                        block += size;
                        break;
                    }
                    buckets[hiSize].poll();
                    if (buckets[hiSize].isEmpty()) {
                        --nbuckets;
                    }
                    int hiPos = hiId*2;
                    disk[hiPos] = -hiSize; // Mark as moved
                    for (; hiSize > 0; hiSize--, size--) {
                        checksum += (long) block++ * hiId;
                    }
                }
            }
        }
        io.out().println(checksum);
    }
}
