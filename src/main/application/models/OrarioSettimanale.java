package main.application.models;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OrarioSettimanale {
	private String nomeOrario;
	private LinkedHashMap<String, LinkedHashMap<String, String>> settimana;

	public OrarioSettimanale(String nomeOrario) {
		this.nomeOrario = nomeOrario;
		settimana = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		settimana.put("lunedi", initGiornoHashMap());
		settimana.put("martedi", initGiornoHashMap());
		settimana.put("mercoledi", initGiornoHashMap());
		settimana.put("giovedi", initGiornoHashMap());
		settimana.put("venerdi", initGiornoHashMap());
		settimana.put("sabato", initGiornoHashMap());
		settimana.put("domenica", initGiornoHashMap());
	}

	public LinkedHashMap<String, String> initGiornoHashMap() {
		LinkedHashMap<String, String> giorno = new LinkedHashMap<String, String>();
		giorno.put("1ora", null);
		giorno.put("2ora", null);
		giorno.put("3ora", null);
		giorno.put("4ora", null);
		giorno.put("5ora", null);
		giorno.put("6ora", null);
		giorno.put("7ora", null);
		giorno.put("8ora", null);
		giorno.put("9ora", null);
		giorno.put("10ora", null);
		giorno.put("11ora", null);
		return giorno;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getSettimana() {
		return settimana;
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

	public boolean addMateria(String ora, String giorno, String materia) {
		for (String key : settimana.keySet()) {
			if (key.equals(giorno)) {
				LinkedHashMap<String, String> g = settimana.get(key);
				for (String key2 : g.keySet()) {
					if (key2.equals(ora)) {
						g.put(key2, materia);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void toXML() {
		System.out.println("writing oraio "+ this.getNomeOrario() +" into .xml file");
		try {
			XMLEncoder encoder;
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nomeOrario + ".xml")));
			encoder.writeObject(settimana);
			encoder.close();
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

	public void removeMateria(String materia) {
		for(String giorno : settimana.keySet()) {
			for(String ora : settimana.get(giorno).keySet()) {
				if(settimana.get(giorno).get(ora) != null) {
					if(settimana.get(giorno).get(ora).equals(materia))
						settimana.get(giorno).put(ora, null);
				}
			}
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

	public String toString() {
		System.out.println("Orario nome: " + this.nomeOrario);
		for (String key : settimana.keySet()) {
			System.out.println(key + "  \t:\t" + settimana.get(key).toString());
		}
		return null;
	}
}
