import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDependency {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(TestDependency.class);

    public static void main(String[] args) {

        logger.info("Parsing input from default config file ");

        Config paramsConfig = ConfigFactory.load("default.conf");
        Config[] iterations = paramsConfig.getConfigList("iterations").toArray(new Config[0]);
        System.out.println(iterations[0].getInt("designPatternChoice"));


        Config testConfig = ConfigFactory.load("test.conf");
        System.out.println(testConfig.getString("conf.combined"));
        System.out.println(testConfig.getString("featureFlags.featureA"));

        logger.info("End of execution");
    }
}
