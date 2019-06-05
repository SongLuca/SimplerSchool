package main.utils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

public final class LanguageBundle {
    private static final ObjectProperty<Locale> locale;
    
    static {
        locale = new SimpleObjectProperty<>(getDefaultLocale());
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }
    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    }

    public static Locale getDefaultLocale() {
    	String lang = Preferences.defaultLang.substring(0,2);
    	Locale l = new Locale(lang);
    	Locale.setDefault(l);
        return l;
    }

    public static Locale getLocale() {
        return locale.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("main.resources.languages.lang", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }
    
    public static String get(final String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("main.resources.languages.lang", getLocale());
        return MessageFormat.format(bundle.getString(key), locale);
    }
    
    public static StringBinding createStringBinding(final String key, Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }

    public static StringBinding createStringBinding(Callable<String> func) {
        return Bindings.createStringBinding(func, locale);
    }

    public static void labelForValue(Label label, Callable<String> func) {
        label.textProperty().bind(createStringBinding(func));
    }
    
    public static Label newLabelForValue(Callable<String> func) {
    	Label label = new Label();
        label.textProperty().bind(createStringBinding(func));
        return label;
    }
    
    public static void buttonForValue(JFXButton btn, Callable<String> func) {
    	btn.textProperty().bind(createStringBinding(func));
    }
    
    public static void textFieldForValue(JFXTextField textF, Callable<String> func) {
    	textF.promptTextProperty().bind(createStringBinding(func));
    }
    
    public static void passFieldForValue(JFXPasswordField passF, Callable<String> func) {
    	passF.promptTextProperty().bind(createStringBinding(func));
    }
    
    public static void checkBoxForValue(JFXCheckBox checkB, Callable<String> func) {
    	checkB.textProperty().bind(createStringBinding(func));
    }
    
    public static void radioButtonForValue(JFXRadioButton radioBtn, Callable<String> func) {
    	radioBtn.textProperty().bind(createStringBinding(func));
    }
    
    public static void buttonToolTipForValue(JFXButton btn, Callable<String> func) {
    	Tooltip tt = new Tooltip();
    	tt.textProperty().bind(createStringBinding(func));
    	btn.setTooltip(tt);
    }
    
    public static void tabForValue(Tab tab, Callable<String> func) {
    	tab.textProperty().bind(createStringBinding(func));
    }
    
    public static Button buttonForKey(final String key, final Object... args) {
        Button button = new Button();
        button.textProperty().bind(createStringBinding(key, args));
        return button;
    }
}
