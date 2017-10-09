package model;

public class Produktua {
	
	String id, izena;
	double prezioa;
	int kopurua;
	
	public Produktua(String ident, String izen, double prez) {
		id=ident;
		izena=izen;
		prezioa=prez;
	}
	
	public Produktua(String ident, String izen, int kop, double prez) {
		id=ident;
		izena=izen;
		kopurua=kop;
		prezioa=prez;
	}
	
	public String getIzena() {
		return izena;
	}



	public void setIzena(String izena) {
		this.izena = izena;
	}



	public double getPrezioa() {
		return prezioa;
	}



	public void setPrezioa(double prezioa) {
		this.prezioa = prezioa;
	}



	public int getKopurua() {
		return kopurua;
	}



	public void setKopurua(int kopurua) {
		this.kopurua = kopurua;
	}



	public String getId() {
		return id;
	}



	@Override
	public String toString() {
		return ("ID: "+id+",Izena: "+izena+", Prezioa: "+prezioa+"€");
	}
}
