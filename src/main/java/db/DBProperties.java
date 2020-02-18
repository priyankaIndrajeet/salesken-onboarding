package db;
/**
 * 
 */

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Vaibhav Verma
 *
 */
public class DBProperties {
	private static final Logger logger = LogManager.getLogger(DBProperties.class);
	public static Properties configProperties;
	static {
		try {
			InputStream inputStream = DBProperties.class.getClassLoader().getResourceAsStream("db.properties");
			// InputStream inputStream = new FileInputStream(new
			// File("/root/vaibhav/appie/appie/db.properties"));
			configProperties = new Properties();
			configProperties.load(inputStream);
		} catch (Exception e) {
			logger.error("Could not load the file");
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return (String) configProperties.get(key);
	}
}