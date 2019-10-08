package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;
import static org.junit.jupiter.api.Assertions.*;

class TransversalPayloadsTest {

    private Set<byte []> previousPayloads;
    private PayloadGeneratorConfig config;
    private String failInfo;

    @BeforeEach
    void setup(){
        previousPayloads = getTree();
        config = new PayloadGeneratorConfig();
    }

    @Test
    void generatePayloadEmptyTest() {
        failInfo = "No additional payloads should be generated when the original set is of size 0";
        TransversalPayloads tester = new TransversalPayloads(previousPayloads, config);
        assertEquals(0, tester.generatePayload().size(), failInfo);
    }

    @Test
    void generatePayloadForwardTest() {
        failInfo = "forward directory transversal failed";
        addPreviousPayload("/etc/passwd");
        byte[] expected = decodeString("../etc/passwd");
        config.forwardSlash = true;
        config.slashMin = 1;
        config.backwardsSlash = false;

        TransversalPayloads tester = new TransversalPayloads(previousPayloads, config);

        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    @Test
    void generatePayloadForwardMissingSlashTest(){
        failInfo = "forward directory transversal failed on partial file path";
        addPreviousPayload("etc/passwd");
        byte[] expected = decodeString("../etc/passwd");
        config.forwardSlash = true;
        config.backwardsSlash = false;

        TransversalPayloads tester = new TransversalPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    @Test
    void generatePayloadBackwardsTest(){
        failInfo = "backwards directory transversal failed on file path";
        addPreviousPayload("\\etc\\passwd");
        byte[] expected = decodeString("..\\etc\\passwd");
        config.forwardSlash = true;
        config.backwardsSlash = false;

        TransversalPayloads tester = new TransversalPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    @Test
    void generatePayloadBackwardsMissingSlashTest(){
        failInfo = "backwards directory transversal failed on partial file path";
        addPreviousPayload("etc\\passwd");
        byte[] expected = decodeString("..\\etc\\passwd");
        config.forwardSlash = false;
        config.backwardsSlash = true;

        TransversalPayloads tester = new TransversalPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    void addPreviousPayload(String p){
        previousPayloads.add(decodeString(p));
    }

    byte[] decodeString(String p){
        return p.getBytes(StandardCharsets.US_ASCII);
    }
}