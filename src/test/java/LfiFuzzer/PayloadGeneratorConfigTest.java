package LfiFuzzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayloadGeneratorConfigTest {

    private PayloadGeneratorConfig config;
    
    @BeforeEach
    void setup(){
        config = new PayloadGeneratorConfig();
    }
    
    @Test
    void testPayloadCardinalityNegSlashSettings() {
        config.slashMax = -1;
        config.slashMin = -2;

        Throwable exception = assertThrows(PayloadConfigException.class, config::getPayloadCardinality);
        assertEquals("Invalid Configurations", exception.getMessage());
    }

    @Test
    void testPayloadCardinalityNegTranSettings() {
        config.tranMax = -1;
        config.tranMin = -2;
        Throwable exception = assertThrows(PayloadConfigException.class, config::getPayloadCardinality);
        assertEquals("Invalid Configurations", exception.getMessage());
    }

    @Test
    void testPayloadCardinalityNegDotSlashSettings() {
        config.dotsMax = -1;
        config.dotsMin = -2;
        Throwable exception = assertThrows(PayloadConfigException.class, config::getPayloadCardinality);
        assertEquals("Invalid Configurations", exception.getMessage());
    }

    @Test
    void testPayloadCardinalityNegSlashSettingsMaxMin() {
        config.slashMax = 1;
        config.slashMin = 2;
        Throwable exception = assertThrows(PayloadConfigException.class, config::getPayloadCardinality);
        assertEquals("Invalid Configurations", exception.getMessage());
    }

    @Test
    void testPayloadCardinalityNegTranSettingsMaxMin() {
        config.tranMax = 1;
        config.tranMin = 2;
        Throwable exception = assertThrows(PayloadConfigException.class, config::getPayloadCardinality);
        assertEquals("Invalid Configurations", exception.getMessage());
    }

    @Test
    void testPayloadCardinalityDotSettingsMaxMin() {
        config = new PayloadGeneratorConfig();
        config.dotsMax = -1;
        config.dotsMin = -2;
        Throwable exception = assertThrows(PayloadConfigException.class, config::getPayloadCardinality);
        assertEquals("Invalid Configurations", exception.getMessage());
    }
}