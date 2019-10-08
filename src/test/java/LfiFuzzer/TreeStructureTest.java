package LfiFuzzer;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TreeStructureTest {

    @Test
    void testTreeComparator() {
        Set<byte []> testTree = TreeStructure.getTree();
        testTree.add("testing".getBytes());
        testTree.add("testing".getBytes());
        assertEquals(testTree.size(), 1, "Binary tree comparator does not handle bytes");
    }
}