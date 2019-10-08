package LfiFuzzer;

import burp.IBurpExtenderCallbacks;
import burp.ITab;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * User interface for the LFI Fuzzer tab
 * This user interface allows the user to configure the
 * payloads that will be generated.
 */
public class UserInterface implements Runnable, ITab {

    private PayloadGeneratorConfig payloadGeneratorConfig = new PayloadGeneratorConfig();
    private GridBagConstraints gbc = new GridBagConstraints();
    private IBurpExtenderCallbacks callbacks;

    private JPanel frame;
    private JPanel tranversalPanel;
    private JPanel slashPanel;
    private JPanel dotPanel;
    private JPanel nullRadioPanel;
    private JPanel urlEncodeRadioPanel;
    private JPanel doubleUrlEncodeRadioPanel;
    private JPanel filterCheckBoxPanel;
    private JPanel utf8EncodeRadioPanel;

    private JLabel fileToIncludeLabel;
    private JLabel dirTransversalCountLabel;
    private JLabel nullByteInjectionLabel;
    private JLabel extraSlashesLabel;
    private JLabel extraDotsLabel;
    private JLabel urlEncodeLabel;
    private JLabel doubleUrlEncodeLabel;
    private JLabel utf8EncodeLabel;
    private JLabel WrapperLabel;
    private JLabel payloadCounterLabel;
    private JLabel slashLabel;

    private JTextField includeFileTF;

    private JSlider tranMinSlider;
    private JSlider tranMaxSlider;
    private JSlider slashMinSlider;
    private JSlider slashMaxSlider;
    private JSlider dotsMinSlider;
    private JSlider dotsMaxSlider;

    private JCheckBox filterWrapperCheckBox;
    private JCheckBox expectWrapperCheckBox;
    private JCheckBox pharWrapperCheckBox;
    private JCheckBox zipWrapperCheckBox;

    private JComboBox<String> slashDirectionComboBox;

    private JButton generateBtn;


    public UserInterface(IBurpExtenderCallbacks callbacks){
        this.callbacks = callbacks;
    }

    @Override
    public void run() {
        frame = new JPanel();
        frame.setBorder(BorderFactory.createTitledBorder("LFI Payload Configurations."));

        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        frame.setLayout(new GridBagLayout());
        createJComponents();
        addComponents();

        callbacks.customizeUiComponent(frame);
        callbacks.customizeUiComponent(dirTransversalCountLabel);
        callbacks.addSuiteTab(UserInterface.this);
    }

    //
    // implement ITab
    //
    @Override
    public String getTabCaption() {
        return "LFI Fuzzer";
    }

    @Override
    public Component getUiComponent() {
        return frame;
    }

    public PayloadGeneratorConfig getPayloadConfiguration() {
        return payloadGeneratorConfig;
    }

    private void createJComponents(){
        createComponentsFileToInclude();
        createComponentsTranversal();
        createComponentsSlash();
        createComponentsDot();
        createComponentsNullByte();
        createComponentsSingleUrl();
        createComponentsDoubleUrl();
        createComponentsUtf();
        createComponentsWrapper();
        createComponentsSlashDirection();
        createComponentsPayload();
        createComponentsGenerate();
    }

    private void createComponentsFileToInclude(){
        fileToIncludeLabel = new JLabel("Files To Include (Separated With Commas):");
        includeFileTF = new JTextField("/etc/passwd");
        includeFileTF.addActionListener(actionEvent -> updateConfig());
    }

    private void createComponentsTranversal(){
        tranversalPanel = new JPanel();
        dirTransversalCountLabel = new JLabel("Directories To Transverse (Min, Max):");

        tranMinSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 1);
        tranMaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 1);

        tranMinSlider.setMajorTickSpacing(10);
        tranMinSlider.setMinorTickSpacing(1);
        tranMinSlider.setPaintTicks(true);
        tranMinSlider.setPaintLabels(true);
        tranMinSlider.setValue(1);

        tranMaxSlider.setMajorTickSpacing(10);
        tranMaxSlider.setMinorTickSpacing(1);
        tranMaxSlider.setPaintTicks(true);
        tranMaxSlider.setPaintLabels(true);
        tranMaxSlider.setValue(10);

        tranMaxSlider.addChangeListener(changeEvent -> updateConfig());
        tranMinSlider.addChangeListener(changeEvent -> updateConfig());

        tranversalPanel.add(tranMinSlider);
        tranversalPanel.add(tranMaxSlider);
    }

    private void createComponentsSlash(){
        slashPanel = new JPanel();
        extraSlashesLabel = new JLabel("Number Of Slashes (Min, Max):");

        slashMinSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 1);
        slashMaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 1);

        slashMinSlider.setMajorTickSpacing(10);
        slashMinSlider.setMinorTickSpacing(1);
        slashMinSlider.setPaintTicks(true);
        slashMinSlider.setPaintLabels(true);
        slashMinSlider.setValue(1);

        slashMaxSlider.setMajorTickSpacing(10);
        slashMaxSlider.setMinorTickSpacing(1);
        slashMaxSlider.setPaintTicks(true);
        slashMaxSlider.setPaintLabels(true);
        slashMaxSlider.setValue(3);

        slashMinSlider.addChangeListener(changeEvent -> updateConfig());
        slashMaxSlider.addChangeListener(changeEvent -> updateConfig());

        slashPanel.add(slashMinSlider);
        slashPanel.add(slashMaxSlider);
    }

    private void createComponentsDot(){
        dotPanel = new JPanel();
        extraDotsLabel = new JLabel("Number Of Dots (Min, Max):");

        dotsMinSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 1);
        dotsMaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 1);

        dotsMinSlider.setMajorTickSpacing(10);
        dotsMinSlider.setMinorTickSpacing(1);
        dotsMinSlider.setPaintTicks(true);
        dotsMinSlider.setPaintLabels(true);
        dotsMinSlider.setValue(1);

        dotsMaxSlider.setMajorTickSpacing(10);
        dotsMaxSlider.setMinorTickSpacing(1);
        dotsMaxSlider.setPaintTicks(true);
        dotsMaxSlider.setPaintLabels(true);
        dotsMinSlider.setValue(3);

        dotsMinSlider.addChangeListener(changeEvent -> updateConfig());
        dotsMaxSlider.addChangeListener(changeEvent -> updateConfig());

        dotPanel.add(dotsMinSlider);
        dotPanel.add(dotsMaxSlider);
    }

    private void createComponentsNullByte(){
        nullRadioPanel = new JPanel();
        nullByteInjectionLabel = new JLabel("Null Byte:");

        JRadioButton nullByteRadioNo = new JRadioButton("No");
        JRadioButton nullByteRadioYes = new JRadioButton("Yes");
        JRadioButton nullByteRadioBoth = new JRadioButton("Try Both");

        ButtonGroup nullGroup = new ButtonGroup();
        nullByteRadioNo.setSelected(true);

        nullByteRadioNo.addActionListener(actionEvent -> {
            payloadGeneratorConfig.nullByteNo = true;
            payloadGeneratorConfig.nullByteYes= false;
            updatePayloadCardinality();
        });

        nullByteRadioYes.addActionListener(actionEvent -> {
            payloadGeneratorConfig.nullByteNo = false;
            payloadGeneratorConfig.nullByteYes = true;
            updatePayloadCardinality();
        });

        nullByteRadioBoth.addActionListener(actionEvent -> {
            payloadGeneratorConfig.nullByteNo = true;
            payloadGeneratorConfig.nullByteYes = true;
            updatePayloadCardinality();
        });

        nullGroup.add(nullByteRadioNo);
        nullGroup.add(nullByteRadioYes);
        nullGroup.add(nullByteRadioBoth);

        nullRadioPanel.setLayout(new GridLayout(1, 2));
        nullRadioPanel.add(nullByteRadioNo);
        nullRadioPanel.add(nullByteRadioYes);
        nullRadioPanel.add(nullByteRadioBoth);
    }

    private void createComponentsSingleUrl(){
        urlEncodeLabel = new JLabel("URL encode:");

        JRadioButton urlEncodeRadioNo = new JRadioButton("No");
        JRadioButton urlEncodeRadioYes = new JRadioButton("Yes");
        JRadioButton urlEncodeRadioBoth = new JRadioButton("Try Both");

        urlEncodeRadioNo.addActionListener(actionEvent -> {
            payloadGeneratorConfig.nullByteNo = true;
            payloadGeneratorConfig.nullByteYes = false;
            updatePayloadCardinality();
        });

        urlEncodeRadioYes.addActionListener(actionEvent -> {
            payloadGeneratorConfig.urlEncodeNo = false;
            payloadGeneratorConfig.urlEncodeYes = true;
            updatePayloadCardinality();
        });

        urlEncodeRadioBoth.addActionListener(actionEvent -> {
            payloadGeneratorConfig.nullByteNo = true;
            payloadGeneratorConfig.nullByteYes = true;
            updatePayloadCardinality();
        });

        urlEncodeRadioNo.setSelected(true);

        ButtonGroup urlEncodeGroup = new ButtonGroup();
        urlEncodeGroup.add(urlEncodeRadioNo);
        urlEncodeGroup.add(urlEncodeRadioYes);
        urlEncodeGroup.add(urlEncodeRadioBoth);

        urlEncodeRadioPanel = new JPanel();
        urlEncodeRadioPanel.setLayout(new GridLayout(1, 2));

        urlEncodeRadioPanel.add(urlEncodeRadioNo);
        urlEncodeRadioPanel.add(urlEncodeRadioYes);
        urlEncodeRadioPanel.add(urlEncodeRadioBoth);
    }

    private void createComponentsDoubleUrl(){
        doubleUrlEncodeLabel = new JLabel("Double URL encode:");
        JRadioButton doubleUrlEncodeRadioNo = new JRadioButton("No");
        JRadioButton doubleUrlEncodeRadioYes = new JRadioButton("Yes");
        JRadioButton doubleUrlEncodeRadioBoth = new JRadioButton("Try Both");

        doubleUrlEncodeRadioNo.addActionListener(actionEvent -> {
            payloadGeneratorConfig.doubleUrlEncodeNo = true;
            payloadGeneratorConfig.doubleUrlEncodeYes = false;
            updatePayloadCardinality();
        });

        doubleUrlEncodeRadioYes.addActionListener(actionEvent -> {
            payloadGeneratorConfig.doubleUrlEncodeNo = false;
            payloadGeneratorConfig.doubleUrlEncodeYes = true;
            updatePayloadCardinality();
        });

        doubleUrlEncodeRadioBoth.addActionListener(actionEvent -> {
            payloadGeneratorConfig.doubleUrlEncodeNo = true;
            payloadGeneratorConfig.doubleUrlEncodeYes = true;
            updatePayloadCardinality();
        });

        doubleUrlEncodeRadioNo.setSelected(true);
        ButtonGroup doubleUrlEncodeGroup = new ButtonGroup();

        doubleUrlEncodeGroup.add(doubleUrlEncodeRadioNo);
        doubleUrlEncodeGroup.add(doubleUrlEncodeRadioYes);
        doubleUrlEncodeGroup.add(doubleUrlEncodeRadioBoth);

        doubleUrlEncodeRadioPanel = new JPanel();
        doubleUrlEncodeRadioPanel.setLayout(new GridLayout(1, 2));
        doubleUrlEncodeRadioPanel.add(doubleUrlEncodeRadioNo);
        doubleUrlEncodeRadioPanel.add(doubleUrlEncodeRadioYes);
        doubleUrlEncodeRadioPanel.add(doubleUrlEncodeRadioBoth);
    }

    private void createComponentsUtf(){
        utf8EncodeLabel = new JLabel("UTF-8 URL encode:");
        JRadioButton utf8EncodeRadioNo = new JRadioButton("No");
        JRadioButton utf8EncodeRadioYes = new JRadioButton("Yes");
        JRadioButton utf8EncodeRadioBoth = new JRadioButton("Try Both");

        utf8EncodeRadioNo.addActionListener(actionEvent -> {
            payloadGeneratorConfig.utf8EncodeNo = true;
            payloadGeneratorConfig.utf8EncodeYes = false;
            updatePayloadCardinality();
        });

        utf8EncodeRadioYes.addActionListener(actionEvent -> {
            payloadGeneratorConfig.utf8EncodeNo = false;
            payloadGeneratorConfig.utf8EncodeYes = true;
            updatePayloadCardinality();
        });

        utf8EncodeRadioBoth.addActionListener(actionEvent -> {
            payloadGeneratorConfig.utf8EncodeNo = true;
            payloadGeneratorConfig.utf8EncodeYes = true;
            updatePayloadCardinality();
        });

        utf8EncodeRadioNo.setSelected(true);
        ButtonGroup utf8EncodeRadioGroup = new ButtonGroup();

        utf8EncodeRadioGroup.add(utf8EncodeRadioNo);
        utf8EncodeRadioGroup.add(utf8EncodeRadioYes);
        utf8EncodeRadioGroup.add(utf8EncodeRadioBoth);

        utf8EncodeRadioPanel = new JPanel();
        utf8EncodeRadioPanel.setLayout(new GridLayout(1, 2));
        utf8EncodeRadioPanel.add(utf8EncodeRadioNo);
        utf8EncodeRadioPanel.add(utf8EncodeRadioYes);
        utf8EncodeRadioPanel.add(utf8EncodeRadioBoth);
    }

    private void createComponentsWrapper(){
        WrapperLabel = new JLabel("Wrappers:");
        filterCheckBoxPanel = new JPanel();

        expectWrapperCheckBox = new JCheckBox("Expect");
        filterWrapperCheckBox = new JCheckBox("Filter");
        pharWrapperCheckBox = new JCheckBox("Phar");
        zipWrapperCheckBox = new JCheckBox("Zip");

        expectWrapperCheckBox.addChangeListener(changeEvent -> updateConfig());
        filterWrapperCheckBox.addChangeListener(changeEvent -> updateConfig());
        pharWrapperCheckBox.addChangeListener(changeEvent -> updateConfig());
        zipWrapperCheckBox.addChangeListener(changeEvent -> updateConfig());


        filterCheckBoxPanel.add(expectWrapperCheckBox);
        filterCheckBoxPanel.add(filterWrapperCheckBox);
        filterCheckBoxPanel.add(pharWrapperCheckBox);
        filterCheckBoxPanel.add(zipWrapperCheckBox);
    }

    private void createComponentsSlashDirection(){
        slashLabel = new JLabel("Slash Directions:");
        String[] slashOptions = { "Forwards", "Backwards", "Both"};
        slashDirectionComboBox = new JComboBox<>(slashOptions);
        slashDirectionComboBox.addActionListener(actionEvent -> {
            String direction = (String) slashDirectionComboBox.getSelectedItem();
            if (direction != null && direction.equals("Forwards")) {
                payloadGeneratorConfig.forwardSlash = true;
                payloadGeneratorConfig.backwardsSlash = false;
            }
            else if (direction != null && direction.equals("Backwards")){
                payloadGeneratorConfig.forwardSlash = false;
                payloadGeneratorConfig.backwardsSlash = true;
            }
            else if (direction != null && direction.equals("Both")){
                payloadGeneratorConfig.forwardSlash = true;
                payloadGeneratorConfig.backwardsSlash = true;
            }
            updatePayloadCardinality();
        });
    }

    private void createComponentsPayload(){
        payloadCounterLabel = new JLabel();
        updatePayloadCardinality();
    }

    private void createComponentsGenerate(){
        generateBtn = new JButton("Generate Payloads");
        generateBtn.addActionListener(actionEvent -> updateConfig());
    }

    private void updateConfig(){
        // Files to include
        String[] files = includeFileTF.getText().split(",");
        Set<String> filesSet = new HashSet<>(Arrays.asList(files));
        Set<String> encodeSet = new HashSet<>();
        for(String f: filesSet){
            // Make sure that encoding is consistent
            encodeSet.add(new String(f.getBytes(), US_ASCII));
        }
        payloadGeneratorConfig.fileCount = encodeSet.size();
        payloadGeneratorConfig.filesToInclude = encodeSet;
        payloadGeneratorConfig.tranMax = tranMaxSlider.getValue();
        payloadGeneratorConfig.tranMin = tranMinSlider.getValue();
        payloadGeneratorConfig.slashMax = slashMaxSlider.getValue();
        payloadGeneratorConfig.slashMin = slashMinSlider.getValue();
        payloadGeneratorConfig.dotsMax = dotsMaxSlider.getValue();
        payloadGeneratorConfig.dotsMin = dotsMinSlider.getValue();
        payloadGeneratorConfig.expectWrapper = expectWrapperCheckBox.isSelected();
        payloadGeneratorConfig.filterWrapper = filterWrapperCheckBox.isSelected();
        payloadGeneratorConfig.pharWrapper = pharWrapperCheckBox.isSelected();
        payloadGeneratorConfig.zipWrapper = zipWrapperCheckBox.isSelected();

        updatePayloadCardinality();
    }

    private void updatePayloadCardinality(){
        try {
            int payloadCardinality = payloadGeneratorConfig.getPayloadCardinality();
            String formattedCardinality = NumberFormat.getNumberInstance(Locale.US).format(payloadCardinality);
            payloadCounterLabel.setText("Total Number Of Payloads per parameter: " + formattedCardinality);
        }catch (PayloadConfigException e){
            payloadCounterLabel.setText("Total Number Of Payloads per parameter: N/a");
        }
    }

    private void addComponents(){

        addJLabel(fileToIncludeLabel);

        int defaultValue = gbc.fill;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addInput(includeFileTF);
        gbc.fill = defaultValue;

        addJLabel(dirTransversalCountLabel);
        addInput(tranversalPanel);

        addJLabel(extraSlashesLabel);
        addInput(slashPanel);

        addJLabel(extraDotsLabel);
        addInput(dotPanel);

        addJLabel(nullByteInjectionLabel);
        addInput(nullRadioPanel);

        addJLabel(urlEncodeLabel);
        addInput(urlEncodeRadioPanel);

        addJLabel(doubleUrlEncodeLabel);
        addInput(doubleUrlEncodeRadioPanel);

        addJLabel(utf8EncodeLabel);
        addInput(utf8EncodeRadioPanel);

        addJLabel(WrapperLabel);
        addInput(filterCheckBoxPanel);

        addJLabel(slashLabel);
        addInput(slashDirectionComboBox);

        addJLabel(payloadCounterLabel);
        addInput(generateBtn);
    }

    private void addJLabel(JLabel label){
        gbc.gridx = 0;
        gbc.gridy ++;
        frame.add(label, gbc);
    }

    private void addInput(JComponent comp){
        gbc.gridx ++;
        frame.add(comp, gbc);
    }
}
