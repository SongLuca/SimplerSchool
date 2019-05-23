package main.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import main.application.Main;
import main.application.models.Config;

public final class Preferences {
    public static String defaultLang;
    public static String votoMin;
    public static String votoMax;
    
    static {
    	defaultLang = Config.getString(Main.USERCONFIG, "selectedLanguage") == null ? "Italiano": Config.getString(Main.USERCONFIG, "selectedLanguage");
    }
    
    public static void loadPreferences() {
    	String app = Config.getString(Main.USERCONFIG, Main.utente.getUsername()+"-votoMin");
    	if(app == null) {
    		votoMin = "0";
    		Config.userConfig.setProperty(Main.utente.getUsername()+"-votoMin", votoMin);
    		Utils.saveProperties(Main.USERCONFIG, true);
    	}
    	else {
    		votoMin = app; 
    	}
    	app = Config.getString(Main.USERCONFIG, Main.utente.getUsername()+"-votoMax");
    	if(app == null) {
    		votoMax = "10";
    		Config.userConfig.setProperty(Main.utente.getUsername()+"-votoMax", votoMax);
       		Utils.saveProperties(Main.USERCONFIG, true);
    	}
    	else {
    		votoMax = app;
    	}
    }
 
    public static String showPreferences() {
    	return "Preferences: linguaggio di default = "+defaultLang+" votoMin = "+votoMin+" votoMax = "+votoMax;
    }
}
