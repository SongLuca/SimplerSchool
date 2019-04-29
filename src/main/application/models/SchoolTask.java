package main.application.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import main.database.DataBaseHandler;

public class SchoolTask {
	private int idTask;
	private int idMateria;
	private LocalDate data ;
	private String tipo;
	private String comment;
	private LinkedHashMap<String,Allegato> allegati;
	
	public SchoolTask(){
		
	}
	
	public SchoolTask(int idTask, int idMateria, LocalDate data, String tipo, String comment) {
		this.idTask = idTask;
		this.idMateria = idMateria;
		this.data = data;
		this.tipo = tipo;
		this.comment = comment;
		this.allegati = new LinkedHashMap<String,Allegato>();
	}
	
	public SchoolTask(int idMateria, LocalDate data, String tipo, String comment, LinkedHashMap<String,Allegato> allegati) {
		this.idMateria = idMateria;
		this.data = data;
		this.tipo = tipo;
		this.comment = comment;
		this.allegati = allegati;
	}

	public SchoolTask(int idMateria, LocalDate data, String tipo, String comment) {
		this.idMateria = idMateria;
		this.data = data;
		this.tipo = tipo;
		this.comment = comment;
		this.allegati = new LinkedHashMap<String,Allegato>();
	}
	
	public void addFile(Allegato a) {
		if(allegati == null)
			allegati = new LinkedHashMap<String,Allegato>();
		allegati.put(a.getFile().getName(),a);
	}
	
	public int getIdMateria() {
		return idMateria;
	}

	public void setIdMateria(int idMateria) {
		this.idMateria = idMateria;
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
	
	public String getMateriaNome() {
		ArrayList<Materia> materie = DataBaseHandler.getInstance().getMaterie();
		for(Materia m : materie) {
			if(m.getId() == this.idMateria)
				return m.getNome();
		}
		return "";
	}
	
	public boolean hasAllegato() {
		if(allegati == null)
			return false;
		else
			return !allegati.isEmpty();
	}
	
	public LinkedHashMap<String,Allegato> getAllegati() {
		return allegati;
	}
	
	public void setAllegati(LinkedHashMap<String,Allegato> allegati) {
		this.allegati = allegati;
	}
	
	public boolean equals(SchoolTask task) {
		if (this == task) {
            return true;
        }
        if (task == null || getClass() != task.getClass()) {
            return false;
        }
        
        return Objects.equals(this.data, task.data)
                && Objects.equals(this.tipo, task.tipo)
                && Objects.equals(this.idMateria, task.idMateria)
                && Objects.equals(this.comment, task.comment)
                && Objects.equals(this.allegati.keySet(), task.allegati.keySet());
	}
	
	@Override
	public String toString() {
		if(allegati != null)
			return "Task id: " + idTask +" data: " + data + " tipo: " 
					+ tipo + " materia: " + getMateriaNome() + "(" + idMateria + ") commento: " + comment + " allegati: " + allegati.toString();
		else
			return "Task id: " + idTask +" data: " + data + " tipo: " 
					+ tipo + " materia: " + getMateriaNome() + "(" + idMateria + ") commento: " + comment + " nessun file allegato";
	}
}
