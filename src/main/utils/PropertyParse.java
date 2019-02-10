package main.utils;

import main.application.Main;

public class PropertyParse {
	
	public static int getInt(String integerS) {
		return Integer.parseInt(Main.prop.getProperty(integerS));
	}
	
	public static double getDouble(String doubleS) {
		return Double.parseDouble(Main.prop.getProperty(doubleS));
	}
	
	public static String getString(String string) {
		return Main.prop.getProperty(string);
	}
	
	public static Boolean getBoolean(String booleanS) {
		return Boolean.parseBoolean(Main.prop.getProperty(booleanS));
	}
}
