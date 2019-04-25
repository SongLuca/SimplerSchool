package main.application.models;

import java.io.File;

public class Allegato {
	private int idAllegato;
	private int idTask;
	private File file;
	
	public Allegato(int idAllegato, int idTask, File file) {
		this.idAllegato = idAllegato;
		this.idTask = idTask;
		this.file = file;
	}
	
	public Allegato(int idTask, File file) {
		this.idTask = idTask;
		this.file = file;
	}
	
	public int getIdAllegato() {
		return idAllegato;
	}
	
	public void setIdAllegato(int idAllegato) {
		this.idAllegato = idAllegato;
	}
	
	public int getIdTask() {
		return idTask;
	}
	
	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "Allegato [idAllegato=" + idAllegato + ", idTask=" + idTask + ", file=" + file.toString() + "]";
	}
	
}
