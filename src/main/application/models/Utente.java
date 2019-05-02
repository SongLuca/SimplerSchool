package main.application.models;

import java.util.Objects;

public class Utente {
	private int userid;
	private String username;
	private String nome;
	private String cognome;
	private String scuola;
	private String avatar_path;

	public Utente() {

	}

	public Utente(String username) {
		this.username = username;
	}

	public Utente(int userid,String username) {
		this.userid = userid;
		this.username = username;
	}
	
	public Utente(String username, String nome, String cognome, String scuola) {
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

	public String getAvatar_path() {
		return avatar_path;
	}

	public void setAvatar_path(String avatar_path) {
		this.avatar_path = avatar_path;
	}
	
	public boolean equals(Utente u) {
		if (this == u) {
            return true;
        }
        if (u == null || getClass() != u.getClass()) {
            return false;
        }
        
        return Objects.equals(this.userid, u.userid)
        		&& Objects.equals(this.username, u.username)
                && Objects.equals(this.nome, u.nome)
                && Objects.equals(this.cognome, u.cognome)
                && Objects.equals(this.scuola, u.scuola)
                && Objects.equals(this.avatar_path, u.avatar_path);
	}
	
	@Override
	public String toString() {
		return "Utente [userid=" + userid + ", username=" + username + ", nome=" + nome + ", cognome=" + cognome
				+ ", scuola=" + scuola + ", avatar_path=" + avatar_path + "]";
	}

}
