package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.util.Set;
import java.util.TreeSet;

/**
 * Creates a set of byte arrays containing different directory transversals
 * ranging from config tranMin to config tranMax
 * for example /etc/passwd, ../etc/passwd, ../../etc/passwd
 */
public class TransversalPayloads extends PayloadType{
    TransversalPayloads(Set<byte[]> previousPayloads, PayloadGeneratorConfig config) {
        super(previousPayloads, config);
    }

    @Override
    public Set<byte []> generatePayload(){
        for(byte[] fileToInclude: this.previousPayloads){
            newPayloads.addAll(generatePayloadsForUniqFile(fileToInclude));
        }
        return newPayloads;
    }

    private Set<byte[]> generatePayloadsForUniqFile(byte[] fileToInclude){
        // Create a TreeSet so that byte arrays have a valid hashcode
        Set<byte []> payloadsForFile = new TreeSet<>((left, right) -> {
            if(left == null || right == null) return 0;
            for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                int a = (left[i] & 0xff);
                int b = (right[j] & 0xff);
                if (a != b) {
                    return a - b;
                }
            }
            return left.length - right.length;
        });
        byte[] transversal = getBytes("..");

        byte[] transversalWithSlash = concatByteArrays(transversal, getBytes(this.slashDirection));

        for(int x = config.tranMin; x <= config.tranMax; x++){
            byte[] currentPayload = getBytes("");
            for(int y = 0; y <=x ; y++){
                if (y == x && checkIfFirstByteIsSlash(fileToInclude)){
                    // Stop the first payload getting an extra slash
                    currentPayload = concatByteArrays(currentPayload, transversal);
                }
                else{
                    currentPayload = concatByteArrays(currentPayload, transversalWithSlash);
                }
            }

            //stdout.print("(Tran " + x + ")");
            //printPayloadBeforeAndAfter(fileToInclude, concatByteArrays(currentPayload, fileToInclude)); //TODO REMOVE

            payloadsForFile.add(concatByteArrays(currentPayload, fileToInclude));
        }
        return payloadsForFile;
    }

    private boolean checkIfFirstByteIsSlash(byte[] fileToInclude){
        return fileToInclude[0] == getBytes("//")[0] || fileToInclude[0] == getBytes("\\")[0];
    }
}
