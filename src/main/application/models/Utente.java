package main.application.models;

public class Utente {
	private int userid;
	private String username;
	private String nome;
	private String cognome;
	private String scuola;
	
	public Utente() {
		
	}
	
	public Utente(String username) {
		this.username = username;
	}
	
	public Utente(String username,String nome, String cognome, String scuola) {
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.scuola = scuola;
	}
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
	this.userid = userid;	
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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
	
	public String getScuola() {
		return scuola;
	}
	
	public void setScuola(String scuola) {
		this.scuola = scuola;
	}

	@Override
	public String toString() {
		return "Utente [userid=" + userid + ", username=" + username + ", nome=" + nome + ", cognome=" + cognome
				+ ", scuola=" + scuola + "]";
	}
	
	
}
