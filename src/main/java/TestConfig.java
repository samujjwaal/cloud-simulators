import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TestConfig {
    public static void main(String[] args) {

        Config paramsConfig = ConfigFactory.load("default.conf");
        Config[] iterations = paramsConfig.getConfigList("iterations").toArray(new Config[0]);
        System.out.println(iterations[0].getInt("designPatternChoice"));


        Config testConfig = ConfigFactory.load("test.conf");
        System.out.println(testConfig.getString("conf.combined"));
        System.out.println(testConfig.getString("featureFlags.featureA"));

    }
}
