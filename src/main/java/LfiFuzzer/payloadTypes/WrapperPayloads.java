package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.util.Set;

public class WrapperPayloads extends PayloadType{

    private boolean enableExpect;
    private boolean enableFilter;
    private boolean enablePhar;
    private boolean enableZip;

    WrapperPayloads(Set<byte[]> previousPayloads, PayloadGeneratorConfig config) {
        super(previousPayloads, config);

        enableExpect = config.expectWrapper;
        enableFilter = config.filterWrapper;
        enablePhar = config.pharWrapper;
        enableZip =  config.zipWrapper;
    }

    @Override
    public Set<byte []> generatePayload(){
        if(enableExpect) generateExpectPayloads();
        if(enableFilter) generateFilterPayloads();
        if(enablePhar) generatePharPayloads();
        if(enableZip) generateZipPayloads();
        return newPayloads;
    }

    private void generateExpectPayloads(){
        /*
         Although these payloads might not execute a real command
         they could notify pentesters of strange behaviour that
         can be examined further.
        */
        for(byte[] currentPayload : previousPayloads){
            newPayloads.add(concatByteArrays(getBytes("expect://"), currentPayload));
        }
    }

    private void generateFilterPayloads(){
        for(byte[] currentPayload : previousPayloads){
            newPayloads.add(concatByteArrays(getBytes("filter://"), currentPayload));
        }
    }

    private void generatePharPayloads(){
        for(byte[] currentPayload : previousPayloads){
            newPayloads.add(concatByteArrays(getBytes("phar://"), currentPayload));
        }
    }

    private void generateZipPayloads(){
        for(byte[] currentPayload : previousPayloads){
            newPayloads.add(concatByteArrays(getBytes("zip://"), currentPayload));
        }
    }

}
