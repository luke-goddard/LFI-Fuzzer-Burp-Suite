package LfiFuzzer;

import java.util.Set;
import java.util.TreeSet;

/**
 * For some reason Java's HashSet does not generate a hashcode for byte arrays
 * This means that the Set will contain duplicate elements, making it not
 * a set. We can get past this by creating a TreeSet, and creating our own
 * comparator.
 */
public class TreeStructure {

    public static Set<byte []> getTree() {
        return new TreeSet<>((left, right) -> {
            if (left == null || right == null) return 0;
            for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                int a = (left[i] & 0xff);
                int b = (right[j] & 0xff);
                if (a != b) {
                    return b - a;
                }
            }
            return right.length - left.length;
        });
    }
}
