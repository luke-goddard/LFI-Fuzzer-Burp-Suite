package LfiFuzzer;

import java.util.HashSet;
import java.util.Set;

public class PayloadGeneratorConfig {
    int fileCount = 1;

    public int tranMin = 1;
    public int tranMax = 10;

    public int slashMin = 1;
    public int slashMax = 3;

    public int dotsMin = 1;
    public int dotsMax = 3;

    public boolean nullByteYes = false;
    public boolean nullByteNo = true;

    public boolean urlEncodeYes = false;
    public boolean urlEncodeNo = true;

    public boolean doubleUrlEncodeYes = false;
    public boolean doubleUrlEncodeNo = true;

    public boolean utf8EncodeYes= false;
    public boolean utf8EncodeNo = true;

    public boolean expectWrapper = false;
    public boolean filterWrapper = false;
    public boolean pharWrapper = false;
    public boolean zipWrapper = false;

    public boolean forwardSlash = true;
    public boolean backwardsSlash = false;

    Set<String> filesToInclude = new HashSet<>();

    int getPayloadCardinality() throws PayloadConfigException {
        int cardinality = 1;
        int tranTot = tranMax - tranMin;
        int slashesTot = slashMax - slashMin + 1;
        int dotsTot = dotsMax - dotsMin + 1;
        int nullBytesTot = convertBooleanToInt(nullByteNo) * convertBooleanToInt(nullByteYes);
        int urlEncodeTot = convertBooleanToInt(doubleUrlEncodeNo) * convertBooleanToInt(doubleUrlEncodeYes);
        int doubleURLEncodeTot = convertBooleanToInt(doubleUrlEncodeNo) * convertBooleanToInt(doubleUrlEncodeYes);
        int utfEncodeTot = convertBooleanToInt(utf8EncodeNo) * convertBooleanToInt(utf8EncodeYes);
        int slashDirectionTot = convertBooleanToInt(forwardSlash) * convertBooleanToInt(backwardsSlash);

        cardinality *= fileCount;
        cardinality *= tranTot;
        cardinality *= slashesTot;
        cardinality *= dotsTot;
        cardinality *= nullBytesTot;
        cardinality *= urlEncodeTot;
        cardinality *= doubleURLEncodeTot;
        cardinality *= utfEncodeTot;
        cardinality *= slashDirectionTot;

        if (cardinality < 1){
            throw new PayloadConfigException("Invalid Configurations");
        }

        cardinality += convertBooleanToInt(expectWrapper) - 1;
        cardinality += convertBooleanToInt(filterWrapper) - 1;
        cardinality += convertBooleanToInt(pharWrapper) - 1;
        cardinality += convertBooleanToInt(zipWrapper) - 1;

        return cardinality;
    }

    private int convertBooleanToInt(Boolean b){
        if (b) return 2;
        return 1;
    }
}
