package main.application.models;

import java.util.Objects;

public class Docente {
	private int idDocente;
	private String nome;
	private String cognome;
	private String stato;
	
	public Docente() {
		this.stato = "fresh";
	}
	
	public Docente(String nome, String cognome) {
		this.nome = nome;
		this.cognome = cognome;
	}
	
	public Docente(int idDocente, String nome, String cognome) {
		this.idDocente = idDocente;
		this.nome = nome;
		this.cognome = cognome;
	}
	
	public Docente(int idDocente, String nome, String cognome, String stato) {
		this.idDocente = idDocente;
		this.nome = nome;
		this.cognome = cognome;
		this.stato = stato;
	}

	public int getIdDocente() {
		return idDocente;
	}
	
	public void setIdDocente(int idDocente) {
		this.idDocente = idDocente;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	public String getNomeCognome() {
		return this.nome+" "+this.cognome;
	}
	
	public boolean equals(Docente d) {
		if (this == d) {
            return true;
        }
        if (d == null || getClass() != d.getClass()) {
            return false;
        }
        
        return Objects.equals(this.idDocente, d.idDocente)
        		&& Objects.equals(this.nome, d.nome)
                && Objects.equals(this.cognome, d.cognome);
	}
	
	@Override
	public String toString() {
		return "Docente [idDocente=" + idDocente + ", nome=" + nome + ", cognome=" + cognome + ", stato=" + stato + "]";
	}
}
