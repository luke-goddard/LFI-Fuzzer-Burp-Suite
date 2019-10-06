package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;

public abstract class PayloadType {
    Set<byte []> previousPayloads;

    // For reason Java's HashSet does not generate a hashcode for byte arrays
    // So we can use a Tree Set and set a Comparator
    Set<byte []> newPayloads = new TreeSet<>((left, right) -> {
        for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
            int a = (left[i] & 0xff);
            int b = (right[j] & 0xff);
            if (a != b) {
                return a - b;
            }
        }
        return left.length - right.length;
    });

    PayloadGeneratorConfig config;
    String slashDirection;
    PrintWriter stdout;
    private PrintWriter stderr;

    PayloadType(Set<byte[]> previousPayloads, PayloadGeneratorConfig config){
        this.previousPayloads = previousPayloads;
        this.config = config;
        this.slashDirection = findSlashDirection();
    }

    public Set<byte []> generatePayload(){
        return newPayloads;
    }

    public void setStd(PrintWriter stdout, PrintWriter stderr){
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public byte[] concatByteArrays(byte[] a, byte[] b){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write( a );
            outputStream.write( b );
            return outputStream.toByteArray( );
        } catch (IOException e) {
            stderr.println(e.toString());
        }
        return new byte[0];

    }

    public void printPayloadBeforeAndAfter(byte[] before, byte[] after){
        String b = new String(before, StandardCharsets.UTF_8);
        String a = new String(after, StandardCharsets.UTF_8);
        stdout.println(b + " -> " + a);

    }

    public void printBytes(byte[] b){
        stdout.println(new String(b, StandardCharsets.UTF_8));
    }

    private String findSlashDirection(){
        if(config.forwardSlash){
            return "/";
        }
        return "\\";
    }

    /**
     * Make sure we can decode a string in a consistent way
     */
    public static byte[] getBytes(String str){
        return str.getBytes(StandardCharsets.US_ASCII);
    }

}
