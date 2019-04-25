package main.application.models;

import javafx.scene.layout.GridPane;
import main.controllers.ControllerMain;

public class MetaData {
	
	// qui contengono i puntatori degli oggetti per uso inter-classe (quando non ci sono modi per accedere una variabile di una classe dall'altra)
	public static int sub_row;  
	public static int sub_col;	
	public static OrarioSettimanale os;
	public static GridPane OrarioSGrid;
	public static Object controller;
	public static ControllerMain cm;
	public static String materiaSelected;
}
