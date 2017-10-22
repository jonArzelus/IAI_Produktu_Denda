package model;

import java.time.LocalDateTime;

public class Saltzailea {
	
	String id, izena, mota, pasahitza, irudia; //mota aldagaia jarri da, aurrerago "administratzailea" mota sortzeko asmoz
	LocalDateTime azkenSarrera;
	
	public Saltzailea(String id, String izena, String mota, String pasahitza) {
		this.id=id;
		this.izena=izena;
		this.mota=mota;
		this.pasahitza=pasahitza;
		this.irudia=null;
		this.azkenSarrera=null;
	}
	
	public Saltzailea(String id, String izena, String mota, String pasahitza, String irudia) {
		this.id=id;
		this.izena=izena;
		this.mota=mota;
		this.pasahitza=pasahitza;
		this.irudia=irudia;
		this.azkenSarrera=null;
	}

	public String getId() {
		return id;
	}

	public String getIzena() {
		return izena;
	}
	
	public String getMota() {
		return mota;
	}
	
	public String getPasahitza() {
		return pasahitza;
	}

	public String getIrudia() {
		return irudia;
	}


	public LocalDateTime getAzkenSarrera() {
		return azkenSarrera;
	}
	
	

}
