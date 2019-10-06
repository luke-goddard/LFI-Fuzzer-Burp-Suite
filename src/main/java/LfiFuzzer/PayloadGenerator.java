package LfiFuzzer;

import LfiFuzzer.payloadTypes.PayloadFactory;
import LfiFuzzer.payloadTypes.PayloadNotFoundException;
import LfiFuzzer.payloadTypes.PayloadType;
import burp.IExtensionHelpers;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PayloadGenerator {
    private PayloadGeneratorConfig config;

    // For reason Java's HashSet does not generate a hashcode for byte arrays
    // So we can use a Tree Set and set a Comparator
    private Set<byte []> payloads = new TreeSet<>((left, right) -> {
        if(left == null || right == null) return 0;
        for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
            int a = (left[i] & 0xff);
            int b = (right[j] & 0xff);
            if (a != b) {
                return b - a;
            }
        }
        return  right.length - left.length;
    });

    private PrintWriter stdout;
    private PrintWriter stderr;
    private IExtensionHelpers helpers;


    public PayloadGenerator(PayloadGeneratorConfig config, PrintWriter stdout, PrintWriter stderr, IExtensionHelpers helpers){
        this.config = config;
        this.stdout = stdout;
        this.stderr = stderr;
        this.helpers = helpers;
    }

    public Set<byte []> generatePayloads(){
        showPayloadSettings();
        stdout.println("Generating Payloads");
        payloads.addAll(generateFilePayloads());
        payloads.addAll(generateTranPayloads());
        payloads.addAll(generateSwapSlash());
        payloads.addAll(generateExtraSlashes());
        payloads.addAll(generateExtraDots());
        payloads.addAll(generateNullbyteSuffix());
        payloads.addAll(generateSingleUrlEncode());
        payloads.addAll(generateDoubleUrlEncode());
        payloads.addAll(generateUtf8Encode());
        payloads.remove(null);
        payloads.remove("".getBytes());
        stdout.println("Finished Generating Payloads");
        return payloads;
    }

    private Set<byte []> generateFilePayloads(){
        stdout.println("Generating Standard File Payloads");
        Set<byte []> filePayloads = new HashSet<>();
        for (String fileToInclude : config.filesToInclude){
            filePayloads.add(fileToInclude.getBytes());
        }
        return filePayloads;
    }

    private Set<byte []> generateTranPayloads() throws PayloadNotFoundException {
        stdout.println("Generating Directory Transversal:");
        try {
            PayloadType tranGen = PayloadFactory.getPayloadType("Transversal", payloads, config);
            tranGen.setStd(stdout, stderr);
            return tranGen.generatePayload();
        }catch (PayloadNotFoundException e){
            stderr.println(e.toString());
            return new HashSet<>();
        }
    }

    private Set<byte []> generateSwapSlash(){
        stdout.println("Generating Slash Swap:");
        Set<byte []> filePayloads = new HashSet<>();
        if(config.forwardSlash) filePayloads.addAll(swapSlashes("Forward"));
        if(config.forwardSlash) filePayloads.addAll(swapSlashes("Backward"));
        return filePayloads;
    }

    private Set<byte[]> generateExtraSlashes() throws PayloadNotFoundException{
        stdout.println("Generating Extra Slash For Filter Sanitization Bypass:");
        try{
            PayloadType extraSlashGen = PayloadFactory.getPayloadType("Extra Slashes", payloads, config);
            extraSlashGen.setStd(stdout, stderr);
            Set<byte []> newPayloads = extraSlashGen.generatePayload();
            stdout.println("Generated " + newPayloads.size()+ " new payloads");
            return newPayloads;
        }catch (PayloadNotFoundException e){
            stderr.println(e.toString());
            return new HashSet<>();
        }
    }

    private Set<byte[]> generateExtraDots() throws PayloadNotFoundException{
        stdout.println("Generating Extra Dots For Filter Sanitization Bypass:");
        try{
            PayloadType extraDotGen = PayloadFactory.getPayloadType("Extra Dots", payloads, config);
            extraDotGen.setStd(stdout, stderr);
            Set<byte []> newPayloads = extraDotGen.generatePayload();
            stdout.println("Generated " + newPayloads.size()+ " new payloads");
            return newPayloads;
        }catch (PayloadNotFoundException e){
            stderr.println(e.toString());
            return new HashSet<>();
        }
    }

    private Set<byte[]> generateNullbyteSuffix(){
        stdout.println("Generating NullBytes For Filter Sanitization Bypass:");
        try{
            PayloadType nullByteSuffix = PayloadFactory.getPayloadType("Nullbytes", payloads, config);
            nullByteSuffix.setStd(stdout, stderr);
            Set<byte []> newPayloads = nullByteSuffix.generatePayload();
            stdout.println("Generated " + newPayloads.size()+ " new payloads");
            return nullByteSuffix.generatePayload();
        }catch (PayloadNotFoundException e){
            stderr.println(e.toString());
            return new HashSet<>();
        }
    }

    private Set<byte[]> generateSingleUrlEncode(){
        Set<byte []> newPayloads = new HashSet<>();
        if(!config.urlEncodeYes) return newPayloads;
        stdout.println("Single URL Encode Filter Sanitization Bypass:");
        int i = 0;
        for(byte[] oldPayload: payloads){
            i++;
            newPayloads.add(helpers.urlEncode(oldPayload));
        }
        stdout.println("Generated " + i + " new payloads");
        return newPayloads;
    }

    private Set<byte[]> generateDoubleUrlEncode(){
        Set<byte []> newPayloads = new HashSet<>();
        if(!config.doubleUrlEncodeYes) return newPayloads;
        stdout.println("Double URL Encode Filter Sanitization Bypass:");
        int i = 0;
        for(byte[] oldPayload: payloads){
            i++;
            newPayloads.add(helpers.urlEncode(oldPayload));
        }
        stdout.println("Generated " + i + " new payloads");
        return newPayloads;
    }

    private Set<byte[]> generateUtf8Encode(){
        Set<byte []> newPayloads = new HashSet<>();
        if(!config.urlEncodeYes) return newPayloads;
        stdout.println("UTF-8 Encode For Filter Sanitization Bypass:");
        for(byte[] oldPayload: payloads){
            newPayloads.add(new String(oldPayload, StandardCharsets.UTF_8).getBytes());
        }
        stdout.println("Generated " + newPayloads.size()+ " new payloads");
        return newPayloads;
    }

    private Set<byte[]> swapSlashes(String direction){
        Set<byte []> newPayloads = new HashSet<>();
        byte before;
        byte after;
        if(direction.equals("Forward")){
            before = PayloadType.getBytes("\\")[0];
            after = PayloadType.getBytes("/")[0];
        }
        else{
            after = PayloadType.getBytes("\\")[0];
            before = PayloadType.getBytes("/")[0];
        }
        int i = 0;
        for(byte[] oldPayload: payloads){
            byte[] newPayload = new byte[oldPayload.length];
            for(int x = 0 ; x <= oldPayload.length - 1; x++){
                i++;
                if(oldPayload[x] == before){
                    newPayload[x] = after;
                    continue;
                }
                newPayload[x] = oldPayload[x];
            }
            newPayloads.add(newPayload);
        }
        stdout.println("Generated " + i + " new payloads");
        return newPayloads;
    }

    private void showPayloadSettings(){
        stdout.println("Payload Settings");
        stdout.println("tranMin: " + config.tranMin);
        stdout.println("tranMax: " + config.tranMax);
        stdout.println("slashMin: " + config.slashMin);
        stdout.println("slashMax: " + config.slashMax);
        stdout.println("dotsMin: " + config.dotsMin);
        stdout.println("dotsMax: " + config.dotsMax);
        stdout.println("nullByteYes: " + config.nullByteYes);
        stdout.println("nullByteNo: " + config.nullByteNo);
        stdout.println("urlEncodeYes: " + config.urlEncodeYes);
        stdout.println("urlEncodeNo: " + config.urlEncodeNo);
        stdout.println("doubleUrlEncodeYes: " + config.doubleUrlEncodeYes);
        stdout.println("doubleUrlEncodeNo: " + config.doubleUrlEncodeNo);
        stdout.println("utf8EncodeYe: " + config.utf8EncodeYes);
        stdout.println("utf8EncodeNo: " + config.utf8EncodeNo);
        stdout.println("expectWrapper: " + config.expectWrapper);
        stdout.println("filterWrapper: " + config.filterWrapper);
        stdout.println("pharWrapper: " + config.pharWrapper);
        stdout.println("zipWrapper: " + config.zipWrapper);
        stdout.println("forwardSlash: " + config.forwardSlash);
        stdout.println("backwardsSlash: " + config.backwardsSlash);

        stdout.println("list of files to include:");
        int i = 0;
        for(String f : config.filesToInclude){
            i++;
            stdout.println("File " + i + ": " + f);
            stdout.println("File " + i + ": " + Arrays.toString(f.getBytes(StandardCharsets.US_ASCII)));
        }
        stdout.println("");
    }

    private void showPayloadList(){
        for(byte[] payload : payloads){
            String toPrint = new String(payload, StandardCharsets.UTF_8);
            stdout.println(toPrint);
        }
    }
}
