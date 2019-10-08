package LfiFuzzer.payloadTypes;

import LfiFuzzer.PayloadGeneratorConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static LfiFuzzer.TreeStructure.getTree;

/**
 * Generic class that holds newly generated payloads for
 * a specific type.
 */
public abstract class PayloadType {
    Set<byte []> previousPayloads;
    Set<byte []> newPayloads = getTree();
    PayloadGeneratorConfig config;
    String slashDirection;

    private PrintWriter stdout;
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

    byte[] concatByteArrays(byte[] a, byte[] b){
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
        if(config.forwardSlash) return "/";
        return "\\";
    }

    /**
     * Make sure we can decode a string in a consistent way
     */
    public static byte[] getBytes(String str){
        return str.getBytes(StandardCharsets.US_ASCII);
    }

}
