package logica;

import java.util.ArrayList;
import java.util.Arrays;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Espia implements Comparable<Espia>{
	private String nombre;
	private Coordinate coordenada;
	private ArrayList<Espia> espiasCercanos;
	
	public Espia(String nombre, Coordinate coordenada) {
		this.nombre = nombre;
		this.coordenada = coordenada;
		this.espiasCercanos = new ArrayList<>();
	}
	public void agregarEspiasCeranos(Espia...espias) {
		this.espiasCercanos.addAll(Arrays.asList(espias));
	}
	
	public String obtenerNombreEspia() {
		return this.nombre;
	}
	
	public Coordinate obtenerCoordenadaEspia() {
		return this.coordenada;
	}
	
	@Override
	public String toString() {
		StringBuilder infoEspia = new StringBuilder();
		infoEspia.append("Nombre: ")
		.append(this.obtenerNombreEspia())
		.append(" ")
		.append(this.obtenerCoordenadaEspia().toString());
		return infoEspia.toString();
	}

	@Override
	public int compareTo(Espia o) {
		Coordinate coordenadaEspia = o.obtenerCoordenadaEspia();
		int a;
		a = Double.compare(this.coordenada.getLat(), coordenadaEspia.getLat());
		if(a == 0) {
			a = Double.compare(this.coordenada.getLon(), coordenadaEspia.getLon());
			if(a == 0) {
				a = this.nombre.compareTo(o.nombre);
			}
		}
		return a;
	}
	
	public ArrayList<Espia> obtenerEspiasCercanos() {
		return this.espiasCercanos;
	}
	
}
