package main.utils;

import main.application.Main;
import main.application.models.Config;

public final class Preferences {
    public static String defaultLang;
    public static String votoMin;
    public static String votoMax;
    public static String theme;
    public static boolean notificaCompiti;
    public static boolean notificaVerifiche;
    public static boolean notificaInterrogazioni;
    
    static {
    	defaultLang = Config.getString(Main.APPCONFIG, "selectedLanguage") == null ? "Italiano": Config.getString(Main.APPCONFIG, "selectedLanguage");
    }
    
    public static void loadPreferences() {
    	String app = Config.getString(Main.USERCONFIG, "votoMin");
    	if(app == null) {
    		votoMin = "0";
    		Config.userConfig.setProperty("votoMin", votoMin);
    	}
    	else {
    		votoMin = app; 
    	}
    	app = Config.getString(Main.USERCONFIG, "votoMax");
    	if(app == null) {
    		votoMax = "10";
    		Config.userConfig.setProperty("votoMax", votoMax);
    	}
    	else {
    		votoMax = app;
    	}
    	app = Config.getString(Main.USERCONFIG, "theme");
    	if(app == null) {
    		theme = "Theme1";
    		Config.userConfig.setProperty("theme", theme);
    	}
    	else {
    		theme = app;
    	}
    	notificaCompiti = Config.getBoolean(Main.USERCONFIG, "compitiPerCasaNotifica");
    	notificaVerifiche = Config.getBoolean(Main.USERCONFIG, "verificaNotifica");
    	notificaInterrogazioni = Config.getBoolean(Main.USERCONFIG, "interrogazioneNotifica");
    }
 
    public static String showPreferences() {
    	return "Preferences: linguaggio di default = "+defaultLang+" votoMin = "+votoMin+" votoMax = "+votoMax;
    }
    
}
