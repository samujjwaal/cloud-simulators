import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDependencyTest {

    @Test
    void main() {

        Config testConfig = ConfigFactory.load("test.conf");
        Boolean featureBFlag = testConfig.getBoolean("featureFlags.featureB");
        assertEquals(true,featureBFlag);
    }
}