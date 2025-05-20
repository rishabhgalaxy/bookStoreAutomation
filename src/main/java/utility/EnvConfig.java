package utility;

import java.io.InputStream;
import java.util.Properties;

public final class EnvConfig {

    private static final Properties configProps = new Properties();
    private static final String DEFAULT_ENV_FILE = "application-UATB.properties";

    static {
        loadProperties(DEFAULT_ENV_FILE);
    }

    private EnvConfig() {
        // Prevent instantiation
    }

    private static void loadProperties(String fileName) {
        String path = "src/test/resources/" + fileName;
        try (InputStream input = EnvConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Config file not found: " + path);
            }
            configProps.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load configuration: " + path, e);
        }
    }

    public static String getValue(String key) {
        String value = configProps.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property not found: " + key);
        }
        return value;
    }
}
