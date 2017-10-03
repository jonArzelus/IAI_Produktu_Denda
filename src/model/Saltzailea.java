package model;

import java.time.LocalDateTime;

public class Saltzailea {
	
	String id, izena, mota, pasahitza;
	LocalDateTime azkenSarrera;
	
	public Saltzailea(String id, String izena, String mota, String pasahitza) {
		this.id=id;
		this.izena=izena;
		this.mota=mota;
		this.pasahitza=pasahitza;
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


	public LocalDateTime getAzkenSarrera() {
		return azkenSarrera;
	}
	
	

}
