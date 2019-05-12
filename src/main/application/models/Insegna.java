package main.application.models;

import java.util.Objects;

public class Insegna {
	private int materiaId;
	private int profId;
	private String stato;

	public Insegna(int materiaId, int profId) {
		this.materiaId = materiaId;
		this.profId = profId;
	}
	
	public Insegna(int materiaId, int profId, String stato) {
		this.materiaId = materiaId;
		this.profId = profId;
		this.stato = stato;
	}
	
	public int getMateriaId() {
		return materiaId;
	}
	
	public void setMateriaId(int materiaId) {
		this.materiaId = materiaId;
	}
	
	public int getProfId() {
		return profId;
	}
	
	public void setProfId(int profId) {
		this.profId = profId;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Override
	public boolean equals(Object i) {
		if (this == i) {
            return true;
        }
        if (i == null || getClass() != i.getClass()) {
            return false;
        }
        Insegna other = (Insegna) i ;
        return Objects.equals(this.materiaId, other.materiaId)
        		&& Objects.equals(this.profId, other.profId);
	}

	@Override
	public String toString() {
		return "Insegna [materiaId=" + materiaId + ", profId=" + profId + ", stato=" + stato + "]\n";
	}
	
	
}
