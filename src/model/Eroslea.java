package model;

public class Eroslea {

	String id, izena, pasahitza;
	double dirua;
	
	public Eroslea(String i, String iz, String pwd, double dir) {
		id = i;
		izena = iz;
		pasahitza = pwd;
		dirua = dir;
	}

	public String getId() {
		return id;
	}

	public String getIzena() {
		return izena;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public double getDirua() {
		return dirua;
	}

	public void setDirua(double dirua) {
		this.dirua = dirua;
	}
	
}
