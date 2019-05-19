package main.application.models;

import java.util.Properties;

public class Config {
	public static Properties config; 
	public static Properties userConfig;
	public static Properties databaseinfo;
	
	public static double getDouble(String configName, String key) {
		switch(configName.toLowerCase()) {
			case "config":
				return Double.parseDouble(config.getProperty(key));
			case "userconfig":
				return Double.parseDouble(userConfig.getProperty(key));
			case "databaseinfo":
				return Double.parseDouble(databaseinfo.getProperty(key));
		}
		return 0;
	}
	
	public static int getInt(String configName, String key) {
		switch(configName.toLowerCase()) {
			case "config":
				return Integer.parseInt(config.getProperty(key));
			case "userconfig":
				return Integer.parseInt(userConfig.getProperty(key));
			case "databaseinfo":
				return Integer.parseInt(databaseinfo.getProperty(key));
		}
		return 0;
	}
	
	public static String getString(String configName, String key) {
		switch(configName.toLowerCase()) {
			case "config":
				return config.getProperty(key);
			case "userconfig":
				return userConfig.getProperty(key);
			case "databaseinfo":
				return databaseinfo.getProperty(key);
		}
		return "error";
	}
	
	public static boolean getBoolean(String configName, String key) {
		switch(configName.toLowerCase()) {
			case "config":
				return Boolean.parseBoolean(config.getProperty(key));
			case "userconfig":
				return Boolean.parseBoolean(userConfig.getProperty(key));
			case "databaseinfo":
				return Boolean.parseBoolean(databaseinfo.getProperty(key));
		}
		return false;
	}
}
