package main.application.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Materia extends RecursiveTreeObject<Materia> {
	private int id;
	private String nome;
	private String colore;
	private String stato; // insert , update , delete , fresh

	public Materia() {

	}

	public Materia(int id) {
		this.id = id;
		this.nome = "";
		this.colore = "#FFFFFF";
		this.stato = "insert";
	}

	public Materia(int id, String nome, String colore, String stato) {
		this.id = id;
		this.nome = nome;
		this.colore = colore;
		this.stato = stato;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	@Override
	public String toString() {
		return "Materia [id=" + id + ", nome=" + nome + ", colore=" + colore + ", stato=" + stato + "]";
	}

}
