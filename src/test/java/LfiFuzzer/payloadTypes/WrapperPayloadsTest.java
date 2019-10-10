package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;
import static org.junit.jupiter.api.Assertions.*;

class WrapperPayloadsTest {

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
    void generatePayloadWithExpectTest() {
        failInfo = "failed to generate payloads with expect wrapper";
        addPreviousPayload("examplePayload");
        byte[] expected = decodeString("expect://examplePayload");
        config.expectWrapper = true;

        WrapperPayloads tester = new WrapperPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    @Test
    void generatePayloadWithFilterTest() {
        failInfo = "failed to generate payloads with expect wrapper";
        addPreviousPayload("examplePayload");
        byte[] expected = decodeString("filter://examplePayload");
        config.filterWrapper = true;

        WrapperPayloads tester = new WrapperPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    @Test
    void generatePayloadWithPharTest() {
        failInfo = "failed to generate payloads with phar wrapper";
        addPreviousPayload("examplePayload");
        byte[] expected = decodeString("phar://examplePayload");
        config.pharWrapper = true;

        WrapperPayloads tester = new WrapperPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    @Test
    void generatePayloadWithZipTest() {
        failInfo = "failed to generate payloads with zip wrapper";
        addPreviousPayload("examplePayload");
        byte[] expected = decodeString("zip://examplePayload");
        config.zipWrapper = true;

        WrapperPayloads tester = new WrapperPayloads(previousPayloads, config);
        assertTrue(tester.generatePayload().contains(expected), failInfo);
    }

    void addPreviousPayload(String p){
        previousPayloads.add(decodeString(p));
    }

    byte[] decodeString(String p){
        return p.getBytes(StandardCharsets.US_ASCII);
    }
}