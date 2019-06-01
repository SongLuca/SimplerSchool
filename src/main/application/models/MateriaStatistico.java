package main.application.models;

public class MateriaStatistico {
	private String materia;
	private int verificheCount;
	private int interrCount;
	
	public MateriaStatistico(String nomeM){
		this.materia = nomeM;
		verificheCount = 0;
		interrCount = 0;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public int getVerificheCount() {
		return verificheCount;
	}

	public void setVerificheCount(int verificheCount) {
		this.verificheCount = verificheCount;
	}

	public int getInterrCount() {
		return interrCount;
	}

	public void setInterrCount(int interrCount) {
		this.interrCount = interrCount;
	}
	
	public void incrementV() {
		verificheCount++;
	}
	
	public void incrementI() {
		interrCount++;
	}

	public boolean isEmpty() {
		return verificheCount == 0 && interrCount == 0;
	}
	
	@Override
	public String toString() {
		return "MateriaStatistico [materia=" + materia + ", verificheCount=" + verificheCount + ", interrCount="
				+ interrCount + "]";
	}
	
}
