package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.util.Set;

/**
 * Adds some extra characters to try and bypass filter sanitation.
 */
public class ExtraCharsPayloads extends PayloadType{

    private int charMin; 
    private int charMax; 
    private byte replaceChar;
    
    ExtraCharsPayloads(Set<byte[]> previousPayloads, PayloadGeneratorConfig config, String charType) {
        super(previousPayloads, config);
        if(charType.equals("slash")){
            charMin = config.slashMin;
            charMax = config.slashMax;
            replaceChar = getBytes(this.slashDirection)[0];
        }
        else{
            charMin = config.dotsMin;
            charMax = config.dotsMax;
            replaceChar = getBytes(".")[0];
        }
    }

    @Override
    public Set<byte[]> generatePayload(){
        for(byte [] currentPayload: previousPayloads) replaceCharactersForFile(currentPayload);
        return newPayloads;
    }

    private void replaceCharactersForFile(byte[] currentPayload){
        for(int x = charMin; x <= charMax; x++){
            newPayloads.add(replaceCharacters(x, currentPayload));
        }
    }

    private byte[] replaceCharacters(int numberOfCharacters, byte[] payload){
        if(numberOfCharacters < 2) return new byte[0];

        int currentSlashCount = getNumberOfReplaceableCharactersInPayload(payload);
        byte[] newPayload = new byte[payload.length + (currentSlashCount * numberOfCharacters)];
        int extra = 0;

        for(int x = 0; x < payload.length ; x++){
            if(payload[x] == replaceChar){
                for(int y=0; y <= numberOfCharacters -1; y++){
                    newPayload[x + extra] = replaceChar;
                    extra++;
                }
            }
            newPayload[x + extra] = payload[x];
        }
        concatByteArrays(payload, newPayload);
        return newPayload;
    }

    private int getNumberOfReplaceableCharactersInPayload(byte[] payload){
        int currentSlashCount = 0;
        byte characterByte = replaceChar;

        for (byte b : payload) if (characterByte == b) currentSlashCount++;
        return currentSlashCount;
    }
}
