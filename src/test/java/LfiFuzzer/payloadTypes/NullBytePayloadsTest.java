package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;
import static org.junit.jupiter.api.Assertions.*;

class NullBytePayloadsTest {

    private Set<byte []> previousPayloads;
    private PayloadGeneratorConfig config;
    private String failInfo;

    @BeforeEach
    void setup(){
        previousPayloads = getTree();
        config = new PayloadGeneratorConfig();
        config.nullByteYes = true;
    }

    @Test
    void generatePayloadNullBytes() {
        failInfo = "null byte should be created on empty set";

        NullBytePayloads tester = new NullBytePayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(decodeString("%00")), failInfo);
    }

    @Test
    void generatePayloadNullBytesOnFile() {
        failInfo = "null byte should the prefix to the file";
        addPreviousPayload("/etc/passwd");

        NullBytePayloads tester = new NullBytePayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(decodeString("/etc/passwd%00")), failInfo);
    }

    @Test
    void doNotGeneratePayloadNullBytes() {
        failInfo = "null byte should not be created when nullBytesYes is false";
        addPreviousPayload("/etc/passwd");
        config.nullByteYes = false;

        NullBytePayloads tester = new NullBytePayloads(previousPayloads, config);
        assertEquals(0, tester.generatePayload().size(),  failInfo);
    }

    void addPreviousPayload(String p){
        previousPayloads.add(decodeString(p));
    }

    byte[] decodeString(String p){
        return p.getBytes(StandardCharsets.US_ASCII);
    }
}