package main.application.models;
import main.controllers.ControllerCustomStage;
import main.controllers.ControllerDocenti;
import main.controllers.ControllerMain;
import main.controllers.ControllerMaterie;
import main.controllers.ControllerOreDetails;

public class MetaData {
	
	// qui contengono i puntatori degli oggetti per uso inter-classe (quando non ci sono modi per accedere una variabile di una classe dall'altra)
	public static ControllerMain cm;
	public static String materiaSelected;
	public static ControllerOreDetails cod;
	public static ControllerMaterie cmat;
	public static ControllerDocenti cd;
	public static ControllerCustomStage ccs;
}
