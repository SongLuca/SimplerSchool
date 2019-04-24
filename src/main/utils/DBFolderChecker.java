package main.utils;

import java.io.File;

import main.application.models.Config;

public class DBFolderChecker {
	public static boolean doCheck() {
		Utils.confirmMsg("Cartella database corrente: " + Config.getString("config", "databaseFolder") + 
				"\n\nAssicurati che stai usando la cartella database del tuo computer (modifica 'databaseFolder' nel config.properties)");
		String dbPath = Config.getString("config", "databaseFolder");
		File dbFolder = new File(dbPath);
		if(dbFolder.exists()) {
			Console.print("Database folder check", "fileio");
			return true;
		}
		else {
			Utils.confirmMsg("DBfolder inesistente. Configura ed esegue il file createDBFolder.bat");
			return false;
		}
	}
}
