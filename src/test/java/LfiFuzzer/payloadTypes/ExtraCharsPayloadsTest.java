package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;
import static org.junit.jupiter.api.Assertions.*;

class ExtraCharsPayloadsTest {

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
    void generatePayloadEmptySetTest() {
        failInfo = "empty payload set should not increase in size";
        ExtraCharsPayloads tester = new ExtraCharsPayloads(previousPayloads, config, "slash");
        assertTrue(tester.generatePayload().isEmpty(), failInfo);
    }

    @Test
    void generatePayloadForwardSlashesTest() {
        failInfo = "forward slashes should be duplicated";
        addPreviousPayload("/etc/passwd");
        ExtraCharsPayloads tester = new ExtraCharsPayloads(previousPayloads, config, "slash");
        config.slashMin = 1;
        config.slashMax = 3;
        config.forwardSlash = true;
        config.backwardsSlash = false;
        assertTrue(tester.generatePayload().contains(decodeString("///etc///passwd")), failInfo);
    }

    @Test
    void generatePayloadDots() {
        failInfo = "Dots should be duplicated";
        addPreviousPayload("../../etc/passwd");
        ExtraCharsPayloads tester = new ExtraCharsPayloads(previousPayloads, config, "dots");
        config.dotsMin = 1;
        config.dotsMax = 3;
        for(byte[] example : tester.generatePayload()) System.out.println(new String(example));
        assertTrue(tester.generatePayload().contains(decodeString("....../....../etc/passwd")), failInfo);
    }

    void addPreviousPayload(String p){
        previousPayloads.add(decodeString(p));
    }

    byte[] decodeString(String p){
        return p.getBytes(StandardCharsets.US_ASCII);
    }
}