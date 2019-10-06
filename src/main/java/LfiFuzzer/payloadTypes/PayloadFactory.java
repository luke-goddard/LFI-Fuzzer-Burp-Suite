package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.util.Set;

public class PayloadFactory {
    public static PayloadType getPayloadType(String type, Set<byte[]> previousPayloads, PayloadGeneratorConfig config) throws PayloadNotFoundException{
        if(type.equals("Transversal")){
            return new TransversalPayloads(previousPayloads, config);
        }
        else if(type.equals("Extra Slashes")){
            return new ExtraCharsPayloads(previousPayloads, config, "slash");
        }
        else if(type.equals("Extra Dots")){
            return new ExtraCharsPayloads(previousPayloads, config, "dots");
        }
        else if(type.equals("Nullbytes")){
            return new NullBytePayloads(previousPayloads, config);
        }
        throw new PayloadNotFoundException("Failed to find payload type: " + type);
    }
}


