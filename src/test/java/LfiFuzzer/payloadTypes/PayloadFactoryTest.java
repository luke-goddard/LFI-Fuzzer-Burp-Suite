package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PayloadFactoryTest {

    private Set<byte []> payloads;
    private PayloadGeneratorConfig config;

    @BeforeEach
    void setup(){
        payloads = getTree();
        config = new PayloadGeneratorConfig();
    }

    @Test
    void getInvalidPayloadTypeTest(){
        String failInfo = "failed to raise correct exception with invalid payload generator type";
        Throwable exception = assertThrows(PayloadNotFoundException.class, () -> {
            PayloadFactory.getPayloadType("fake", payloads, config);
        });
        assertEquals("Failed to find payload type: fake", exception.getMessage(), failInfo);
    }
}