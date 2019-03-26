package main.application.models;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import main.application.Main;
import main.utils.Console;

public class OrarioSettimanale {
	private int id;
	private String nomeOrario;
	private String storedPath;
	private int user_id ;
	private String stato;
	private LinkedHashMap<String, LinkedHashMap<String, String>> settimana;
	
	public OrarioSettimanale() {
		this.stato = "fresh";
		settimana = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		settimana.put("lunedi", initGiornoHashMap());
		settimana.put("martedi", initGiornoHashMap());
		settimana.put("mercoledi", initGiornoHashMap());
		settimana.put("giovedi", initGiornoHashMap());
		settimana.put("venerdi", initGiornoHashMap());
		settimana.put("sabato", initGiornoHashMap());
	}
	
	public OrarioSettimanale(String nomeOrario) {
		this.stato = "fresh";
		this.nomeOrario = nomeOrario;
		settimana = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		settimana.put("lunedi", initGiornoHashMap());
		settimana.put("martedi", initGiornoHashMap());
		settimana.put("mercoledi", initGiornoHashMap());
		settimana.put("giovedi", initGiornoHashMap());
		settimana.put("venerdi", initGiornoHashMap());
		settimana.put("sabato", initGiornoHashMap());
	}
	
	public LinkedHashMap<String, String> initGiornoHashMap() {
		LinkedHashMap<String, String> giorno = new LinkedHashMap<String, String>();
		giorno.put("1ora", "null");
		giorno.put("2ora", "null");
		giorno.put("3ora", "null");
		giorno.put("4ora", "null");
		giorno.put("5ora", "null");
		giorno.put("6ora", "null");
		giorno.put("7ora", "null");
		giorno.put("8ora", "null");
		giorno.put("9ora", "null");
		giorno.put("10ora", "null");
		return giorno;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getSettimana() {
		return settimana;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSettimana(LinkedHashMap<String, LinkedHashMap<String, String>> settimana) {
		this.settimana = settimana;
	}

	public String getNomeOrario() {
		return nomeOrario;
	}

	public void setNomeOrario(String nomeOrario) {
		this.nomeOrario = nomeOrario;
	}
	
	public String getStoredPath() {
		return storedPath;
	}

	public void setStoredPath(String stoledPath) {
		this.storedPath = stoledPath;
	}
	
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public boolean addMateria(String ora, String giorno, String materia) {
		for (String key : settimana.keySet()) {
			if (key.equals(giorno)) {
				LinkedHashMap<String, String> g = settimana.get(key);
				for (String key2 : g.keySet()) {
					if (key2.equals(ora)) {
						g.put(key2, materia);
						if(!stato.equals("insert"))
							stato = "update";
						return true;
					}
				}
			}
		}
		return false;
	}

	public void toXML() {
		Console.print("Writing orario "+ this.getNomeOrario() +" into .xml file", "fileio");
		try {
			XMLEncoder encoder;
			File filePath = new File(Config.getString("config", "databaseFolder") + "/users/" + Main.utente.getUserid() + "/orariosettimanale/");
			if(!filePath.exists())
				filePath.mkdirs();
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath.getAbsolutePath() + "/" + this.id + ".xml")));
			storedPath = "users/" + Main.utente.getUserid()+"/orariosettimanale/"+ this.id+".xml";
			encoder.writeObject(settimana);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	public void loadXML() {
		Console.print("Reading orario "+ this.getNomeOrario() +" from .xml file", "fileio");
		try {
			XMLDecoder decoder;
			String path = Config.getString("config", "databaseFolder") + "/" + storedPath;
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
			settimana = (LinkedHashMap<String, LinkedHashMap<String, String>>) decoder.readObject();
			decoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Materia findMateriaByNome(HashMap<Integer, Materia> materie, String nome) {
		for (int key : materie.keySet()) {
			if (materie.get(key).getNome().equals(nome))
				return materie.get(key);
		}
		return null;
	}
	
	public String getMateriaByPos(int row, int col) {
		return settimana.get(getGiornoByCol(col)).get(getOraByRow(row));
	}
	
	public void removeMateria(String materia) {
		for(String giorno : settimana.keySet()) {
			for(String ora : settimana.get(giorno).keySet()) {
				if(settimana.get(giorno).get(ora).equals(materia)) {
					settimana.get(giorno).put(ora, "null");
					if(!stato.equals("insert"));
					stato = "update";
				}
			}
		}
	}
	
	public LinkedHashMap<String, String> getOrarioGiorno(int day) {
		String giorno = this.getGiornoByCol(day);
		return settimana.get(giorno);
	}
	
	public String getGiornoByCol(int col) {
		switch (col) {
		case 0:
			return "lunedi";
		case 1:
			return "martedi";
		case 2:
			return "mercoledi";
		case 3:
			return "giovedi";
		case 4:
			return "venerdi";
		case 5:
			return "sabato";
		case 6:
			return "domenica";
		default:
			return "errore";
		}
	}
	
	public String getOraByRow(int row) {
		switch (row) {
		case 0:
			return "1ora";
		case 1:
			return "2ora";
		case 2:
			return "3ora";
		case 3:
			return "4ora";
		case 4:
			return "5ora";
		case 5:
			return "6ora";
		case 6:
			return "7ora";
		case 7:
			return "8ora";
		case 8:
			return "9ora";
		case 9:
			return "10ora";
		case 10:
			return "11ora";
		default:
			return "errore";
		}
	}
	
	public int getColByGiorno(String giorno) {
		switch (giorno) {
		case "lunedi":
			return 0;
		case "martedi":
			return 1;
		case "mercoledi":
			return 2;
		case "giovedi":
			return 3;
		case "venerdi":
			return 4;
		case "sabato":
			return 5;
		case "domenica":
			return 6;
		default:
			return 7;
		}
	}

	public int getRowByOra(String ora) {
		switch (ora) {
			case "1ora":
				return 0;
			case "2ora":
				return 1;
			case "3ora":
				return 2;
			case "4ora":
				return 3;
			case "5ora":
				return 4;
			case "6ora":
				return 5;
			case "7ora":
				return 6;
			case "8ora":
				return 7;
			case "9ora":
				return 8;
			case "10ora":
				return 9;
			case "11ora":
				return 10;
			default:
				return 11;
		}
	}

	public String findMateriaByPos(int row, int col) {
		int countRow = 0;
		int countCol = 0;
		for (String keyDay: settimana.keySet()) {
			if (row == countRow) {
				for (String keyHour: settimana.get(keyDay).keySet()) {
					if (col == countCol) {
						return settimana.get(keyDay).get(keyHour);
					}
					countCol++;
				}
			}
			countRow++;
		}
		return null;
	}
	
	@Override
	public String toString() {
		System.out.println("userid: " + user_id +" Orario nome: " + this.nomeOrario + " stato: " + stato + " path: " + storedPath);
		for (String key : settimana.keySet()) {
			System.out.println(key + "  \t:\t" + settimana.get(key).toString());
		}
		return null;
	}
	
	
}
