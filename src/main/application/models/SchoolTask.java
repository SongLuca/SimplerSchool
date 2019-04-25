package main.application.models;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Objects;

public class SchoolTask {
	private int idTask;
	private LocalDate data ;
	private String tipo;
	private String comment;
	private String materia;
	private LinkedHashMap<String,Allegato> allegati;
	
	public SchoolTask(){
		
	}
	
	public SchoolTask(int idTask,LocalDate data, String tipo, String materia, String comment) {
		this.idTask = idTask;
		this.data = data;
		this.tipo = tipo;
		this.comment = comment;
		this.materia = materia;
		this.allegati = null;
	}
	
	public SchoolTask(LocalDate data, String tipo, String materia, String comment, LinkedHashMap<String,Allegato> allegati) {
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
	
	public void addFile(Allegato a) {
		if(allegati == null)
			allegati = new LinkedHashMap<String,Allegato>();
		allegati.put(a.getFile().getName(),a);
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
                && Objects.equals(this.materia, task.materia)
                && Objects.equals(this.comment, task.comment)
                && Objects.equals(this.allegati.keySet(), task.allegati.keySet());
	}
	
	@Override
	public String toString() {
		if(allegati != null)
			return "Task id: " + idTask +" data: " + data + " tipo: " 
					+ tipo + " materia: " + materia + " commento: " + comment + " allegati: " + allegati.toString();
		else
			return "Task id: " + idTask +" data: " + data + " tipo: " 
					+ tipo + " materia: " + materia + " commento: " + comment + " nessun file allegato";
	}
}
