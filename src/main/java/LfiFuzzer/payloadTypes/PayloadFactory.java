package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.util.Set;

public class PayloadFactory {
    public static PayloadType getPayloadType(String type, Set<byte[]> previousPayloads, PayloadGeneratorConfig config) throws PayloadNotFoundException{
        switch (type) {
            case "Transversal":
                return new TransversalPayloads(previousPayloads, config);
            case "Extra Slashes":
                return new ExtraCharsPayloads(previousPayloads, config, "slash");
            case "Extra Dots":
                return new ExtraCharsPayloads(previousPayloads, config, "dots");
            case "Nullbytes":
                return new NullBytePayloads(previousPayloads, config);
            case "Wrappers":
                return new WrapperPayloads(previousPayloads, config);
        }
        throw new PayloadNotFoundException("Failed to find payload type: " + type);
    }
}


