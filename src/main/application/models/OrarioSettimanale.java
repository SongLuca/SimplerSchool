package main.application.models;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;

public class OrarioSettimanale {
	private String nomeOrario;
	private LinkedHashMap<String, LinkedHashMap<String, String>> settimana;
	
	public OrarioSettimanale(String nomeOrario){
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
		for(String key : settimana.keySet()) {
			if(key.equals(giorno)) {
				LinkedHashMap<String, String> g = settimana.get(key);
				for(String key2 : g.keySet()) {
					if(key2.equals(ora)) {
						g.put(key2, materia);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void toXML(OrarioSettimanale os, String path) {
		try {
			XMLEncoder encoder;
			if(path == null)
				encoder = new XMLEncoder(new BufferedOutputStream(
			          new FileOutputStream(os.getNomeOrario()+".xml")));
			else {
				encoder = new XMLEncoder(new BufferedOutputStream(
				          new FileOutputStream(path+"/"+os.getNomeOrario()+".xml")));
			}
			encoder.writeObject(os.getSettimana());
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public String toString() {
		for(String key : settimana.keySet()) {
			System.out.println(key+": "+settimana.get(key).toString());
		}
		return null;
	}
}
