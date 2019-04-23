package main.application.models;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class SchoolTask {
	private int idTask;
	private LocalDate data ;
	private String tipo;
	private String comment;
	private String materia;
	private List<File> allegati;
	
	public SchoolTask(){
		
	}
	
	public SchoolTask(LocalDate data, String tipo, String materia, String comment, List<File> allegati) {
		this.data = data;
		this.tipo = tipo;
		this.comment = comment;
		this.materia = materia;
		this.allegati = allegati;
	}

	public SchoolTask(LocalDate data, String tipo, String materia, String comment) {
		this.data = data;
		this.tipo = tipo;
		this.comment = comment;
		this.materia = materia;
		this.allegati = null;
	}
	
	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public int getIdTask() {
		return idTask;
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}

	public LocalDate getData() {
		return data;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean hasAllegato() {
		if(allegati == null)
			return false;
		else
			return allegati.isEmpty();
	}
	
	public List<File> getAllegati() {
		return allegati;
	}
	
	public void setAllegati(List<File> allegati) {
		this.allegati = allegati;
	}
	
	@Override
	public String toString() {
		return "Task id: " + idTask +" data: " + data + " tipo: " 
					+ tipo + " materia: " + materia + " commento: " + comment + " allegati: " + allegati.toString();
	}
}
