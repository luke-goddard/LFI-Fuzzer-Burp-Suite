package burp;

import LfiFuzzer.PayloadGenerator;
import LfiFuzzer.PayloadGeneratorConfig;
import LfiFuzzer.UserInterface;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.Set;


/**
 * Local File Inclusion Fuzzer
 */
public class BurpExtender implements IBurpExtender, IIntruderPayloadGeneratorFactory
{
    private IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;
    private UserInterface userInterface;

    //
    // implement IBurpExtender
    //
    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        // obtain an extension helpers object
        helpers = callbacks.getHelpers();
        callbacks.setExtensionName("Local File Inclusion intruder payloads");
        callbacks.registerIntruderPayloadGeneratorFactory(this);

        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        stdout.println("Local File Inclusion Fuzzer plugin");
        stdout.println("---------------------------");

        userInterface = new UserInterface(callbacks);
        SwingUtilities.invokeLater(userInterface);
    }

    //
    // implement IIntruderPayloadGeneratorFactory
    //

    @Override
    public String getGeneratorName()
    {
        return "Local File Inclusion Fuzzer";
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack attack)
    {
        return new IntruderPayloadGenerator(userInterface.getPayloadConfiguration());
    }


    class IntruderPayloadGenerator implements IIntruderPayloadGenerator
    {
        int payloadIndex = 0;
        private byte[][] payloads;

        IntruderPayloadGenerator(PayloadGeneratorConfig config){
            generatePayloads(config);
        }

        void generatePayloads(PayloadGeneratorConfig config){
            PayloadGenerator generator = new PayloadGenerator(config, stdout, stderr, helpers);
            Set<byte[]> payloadsSet = generator.generatePayloads();
            int i = 0;
            payloads = new byte[payloadsSet.size()][];
            for(byte[] payload: payloadsSet) {
                payloads[i] = payload;
                i++;
            }
        }

        @Override
        public boolean hasMorePayloads()
        {
            return payloadIndex < payloads.length;
        }

        @Override
        public byte[] getNextPayload(byte[] baseValue)
        {
            byte[] payload = payloads[payloadIndex];
            payloadIndex++;
            return payload;
        }

        @Override
        public void reset()
        {
            payloadIndex = 0;
        }
    }
}