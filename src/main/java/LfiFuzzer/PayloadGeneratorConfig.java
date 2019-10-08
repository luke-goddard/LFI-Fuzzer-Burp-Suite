package LfiFuzzer;

import java.util.HashSet;
import java.util.Set;

/**
 * Class as a data structure to hold the form information that the user
 * entered.
 */
public class PayloadGeneratorConfig {
    int fileCount = 1;

    public int tranMin = 1;
    public int tranMax = 10;

    public int slashMin = 1;
    public int slashMax = 3;

    public int dotsMin = 1;
    public int dotsMax = 3;

    public boolean nullByteYes = false;
    boolean nullByteNo = true;

    boolean urlEncodeYes = false;
    boolean urlEncodeNo = true;

    boolean doubleUrlEncodeYes = false;
    boolean doubleUrlEncodeNo = true;

    boolean utf8EncodeYes= false;
    boolean utf8EncodeNo = true;

    boolean expectWrapper = false;
    boolean filterWrapper = false;
    boolean pharWrapper = false;
    boolean zipWrapper = false;

    public boolean forwardSlash = true;
    public boolean backwardsSlash = false;

    Set<String> filesToInclude = new HashSet<>();

    public int getPayloadCardinality() throws PayloadConfigException {
        int cardinality = 1;
        int tranTot = tranMax - tranMin + 1;
        int slashesTot = slashMax - slashMin + 1;
        int dotsTot = dotsMax - dotsMin + 1;
        int nullBytesTot = convertNoOrYesOrBoth(nullByteNo, nullByteYes);
        int urlEncodeTot = convertNoOrYesOrBoth(doubleUrlEncodeNo, doubleUrlEncodeYes);
        int doubleURLEncodeTot = convertNoOrYesOrBoth(doubleUrlEncodeNo, doubleUrlEncodeYes);
        int utfEncodeTot = convertNoOrYesOrBoth(utf8EncodeNo, utf8EncodeYes);
        int slashDirectionTot = convertNoOrYesOrBoth(forwardSlash, backwardsSlash);

        if(!validateSettings())throw new PayloadConfigException("Invalid Configurations");

        cardinality *= fileCount;
        cardinality *= tranTot;
        cardinality *= slashesTot;
        cardinality *= dotsTot;
        cardinality *= nullBytesTot;
        cardinality *= urlEncodeTot;
        cardinality *= doubleURLEncodeTot;
        cardinality *= utfEncodeTot;
        cardinality *= slashDirectionTot;
        cardinality *= convertBooleanToInt(expectWrapper);
        cardinality *= convertBooleanToInt(filterWrapper);
        cardinality *= convertBooleanToInt(pharWrapper);
        cardinality *= convertBooleanToInt(zipWrapper);

        return cardinality;
    }

    private boolean validateSettings(){
        return testMinValues() && testPairs();
    }

    private boolean testMinValues(){
        return tranMin >= 0 && tranMax >= 0 && slashMax >= 0 && slashMin >= 0 && dotsMax >= 0 && dotsMin >= 0;
    }

    private boolean testPairs(){
        return dotsMax >= dotsMin && slashMax >= slashMin && tranMax >= tranMin;
    }

    private int convertNoOrYesOrBoth(boolean a, boolean b){
        if(a && b) return 2;
        return 1;
    }

    private int convertBooleanToInt(boolean b){
        if (b) return 2;
        return 1;
    }
}
