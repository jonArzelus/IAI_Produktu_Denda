package phidgetDenda;

public class Produktua {
	
	String id, izena;
	double prezioa;
	
	public Produktua(String ident, String izen, double prez) {
		id=ident;
		izena=izen;
		prezioa=prez;
	}
	
	@Override
	public String toString() {
		return ("ID: "+id+",Izena: "+izena+", Prezioa: "+prezioa+"€");
	}
}
