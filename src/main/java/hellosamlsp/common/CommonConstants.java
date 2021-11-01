package hellosamlsp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonConstants {

    private static final Logger logger = LoggerFactory.getLogger(CommonConstants.class);

    public static final String IDP_METADATA_FILEPATH;

    public static final String CONFIG_FILE_NAME = "/config/hellosamlsp.properties";

    static {
        Properties properties = loadProperties();
        IDP_METADATA_FILEPATH = properties.getProperty("idp_metadata_filepath");
        logger.info("============================CONFIG=========================");
        logger.info("IDP_METADATA_FILEPATH:" + IDP_METADATA_FILEPATH);
        logger.info("============================================================");
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            File file = new File(System.getProperty("user.dir") + CONFIG_FILE_NAME);
            if (!file.exists()) {
                file = new File(System.getProperty("user.home") + CONFIG_FILE_NAME);
            }
            if (!file.exists()) {
                throw new RuntimeException("can not find config file!");
            }
            InputStream ins = new FileInputStream(file);
            properties.load(ins);
            ins.close();
            logger.info("load config file:" + file.getAbsolutePath());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(0);
        }
        return properties;
    }

}
