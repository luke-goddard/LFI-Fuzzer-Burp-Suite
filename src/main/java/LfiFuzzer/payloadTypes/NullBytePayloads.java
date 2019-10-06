package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.util.Set;

public class NullBytePayloads extends PayloadType{
    NullBytePayloads(Set<byte[]> previousPayloads, PayloadGeneratorConfig config) {
        super(previousPayloads, config);
    }

    @Override
    public Set<byte[]> generatePayload(){
        if(!config.nullByteYes) return newPayloads;
        for(byte[] oldPayload : previousPayloads){
            byte[] newPayload = concatByteArrays(oldPayload, getBytes("%00"));
            newPayloads.add(newPayload);
            //printPayloadBeforeAndAfter(oldPayload, newPayload);
        }
        return newPayloads;
    }
}
