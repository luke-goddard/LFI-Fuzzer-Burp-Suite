package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadConfigException;
import LfiFuzzer.PayloadGeneratorConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PayloadFactoryTest {

    private Set<byte []> previousPayloads = getTree();
    private PayloadGeneratorConfig config = new PayloadGeneratorConfig();

    @Test
    void getPayloadType() {
        //TODO
    }
}