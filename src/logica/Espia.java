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
	
	public void agregarEspiasCercanos(Espia...espias) {
		this.espiasCercanos.addAll(Arrays.asList(espias));
	}

	public String obtenerNombreEspia() {
		return this.nombre;
	}

	public Coordinate obtenerCoordenadaEspia() {
		return this.coordenada;
	}

	public ArrayList<Espia> obtenerEspiasCercanos() {
		return this.espiasCercanos;
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
	public int compareTo(Espia otroEspia) {
		Coordinate coordenadaEspia = otroEspia.obtenerCoordenadaEspia();
		int comparacion;
		comparacion = Double.compare(this.coordenada.getLat(), coordenadaEspia.getLat());
		if(comparacion == 0) {
			comparacion = Double.compare(this.coordenada.getLon(), coordenadaEspia.getLon());
			if(comparacion == 0) {
				comparacion = this.nombre.compareTo(otroEspia.nombre);
			}
		}
		return comparacion;
	}
}