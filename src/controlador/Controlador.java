package controlador;

import java.awt.Color;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;

import excepcion.GrafoNoConexoException;
import interfaz.Main;
import logica.Arista;
import logica.Espia;
import logica.Grafo;
import logica.AGMKruskal;
import logica.AGMPrim;
import logica.AbstractAGM;

public class Controlador {
	private Main vista;
	private Grafo<Espia> grafo;
	private Set<Espia> espias;
	private Set<Arista<Espia>> aristasG;
	private TreeSet<Arista<Espia>> aristasAGM;

	public Controlador(Main vista) {
		this.vista = vista;
		inicializarObjetos();
	}

	public void mostrarMapaConGrafo() {
		mostrarMapaConGrafo(this.aristasG);
	}

	public boolean dibujarAGM(Algoritmo_AGM algoritmoAGM) {
		try {
			AbstractAGM<Espia> algoritmoSeleccionado;

			switch(algoritmoAGM) {
			case PRIM:
				algoritmoSeleccionado = new AGMPrim<>(grafo); break;
			case KRUSKAL:
				algoritmoSeleccionado = new AGMKruskal<>(grafo); break;
			default:
				throw new IllegalArgumentException("Algoritmo no cargado");
			}
			this.aristasAGM = grafo.aristasDelAGM(algoritmoSeleccionado);
			limpiarMapa();
			mostrarMapaConGrafo(this.aristasAGM);
			System.out.println("tiempo de ejecucion = " + algoritmoSeleccionado.tiempoEjecucionEnNanoSegundos());
			return true;
		} catch (GrafoNoConexoException e) {
			vista.mostrarAlerta("El grafo aún no es conexo, añada más aristas");
		} catch (NoSuchElementException e) {
			vista.mostrarAlerta("¡Aún no hay vértices!");
		}
		return false;
	}

	public void agregarNuevoEspia(Espia espia) {
		this.grafo.agregarVertice(espia);
		this.espias.add(espia);
		mostrarPunto(vista.getMapViewer(), espia.obtenerCoordenadaEspia(), espia.toString(), Color.YELLOW);
	}

	public void nuevaArista(Espia e1, Espia e2, double peso) {
		try {
			this.grafo.agregarAristaEntreVertices(e1, e2, peso);
			Arista<Espia> ar = new Arista<>(e1, e2, peso);
			if (agregarArista(ar)) {
				graficarArista(vista.getMapViewer(), ar);
			} else {
				vista.mostrarAlerta("¡No puede cambiar el peso de la arista!");
			}
		} catch (IllegalArgumentException e) {
			vista.mostrarAlerta("¡No puede añadir una relación entre un mismo vértice!");
		}
	}

	public void quitarTodosLosPuntos() {
		inicializarObjetos();
	}

	public void grafoCompletoAristasAleatorias() {
		inicializarAristas();
		for (Espia e : this.espias) {
			for (Espia e2 : this.espias) {
				if (e.equals(e2)) {
					continue;
				}
				double peso = Math.random();
				Arista<Espia> ar = new Arista<>(e, e2, peso);
				if (agregarArista(ar)) {
					this.grafo.agregarAristaEntreVertices(e2, e, peso);
				}
			}
		}
		grafo = new Grafo<>(this.espias, this.aristasG);
		mostrarMapaConGrafo();
	}

	public void espiasConPesosAleatorios() {
		for (Espia e : this.espias) {
			for (Espia e2 : e.obtenerEspiasCercanos()) {
				double peso = Math.random();
				Arista<Espia> ar = new Arista<>(e, e2, peso);
				if (agregarArista(ar)) {
					grafo.agregarAristaEntreVertices(e, e2, peso);
				}
			}
		}
		mostrarMapaConGrafo();
	}


	public long[] compararAlgoritmos() {
		AbstractAGM<Espia> algoritmoKruskal = new AGMKruskal<Espia>(grafo);
		AbstractAGM<Espia> algoritmoPrim = new AGMPrim<Espia>(grafo);
		
		long resultados[] = new long[2];
		resultados[0] = algoritmoKruskal.tiempoEjecucionEnNanoSegundos();
		resultados[1] = algoritmoPrim.tiempoEjecucionEnNanoSegundos();
		
		return resultados;
	}
	
	private void mostrarMapaConGrafo(Set<Arista<Espia>> aristas) {
		limpiarMapa();
		for (Espia e : this.espias) {
			mostrarPunto(vista.getMapViewer(), e.obtenerCoordenadaEspia(), e.toString(), Color.YELLOW);
		}
		for (Arista<Espia> ar : aristas) {
			graficarArista(vista.getMapViewer(), ar);
		}
	}

	private void limpiarMapa() {
		limpiarPuntos();
		limpiarAristas();
	}

	private void limpiarPuntos() {
		vista.getMapViewer().removeAllMapMarkers();
	}

	private void limpiarAristas() {
		vista.getMapViewer().removeAllMapPolygons();
	}

	private void graficarArista(JMapViewer map, Arista<Espia> ar) {
		Coordinate c1 = ar.obtenerVerticeInicio().obtenerCoordenadaEspia();
		Coordinate c2 = ar.obtenerVerticeDestino().obtenerCoordenadaEspia();

		mostrarPunto(map, c1, ar.obtenerVerticeInicio().toString(), Color.YELLOW);
		mostrarPunto(map, c2, ar.obtenerVerticeDestino().toString(), Color.YELLOW);

		dibujarLineaEntreCoordinates(map, c1, c2);
		dibujarPesoEnElMedio(map, c1, c2, ar.obtenerPeso());
	}

	private void dibujarLineaEntreCoordinates(JMapViewer map, Coordinate c1, Coordinate c2) {
		map.addMapPolygon(new MapPolygonImpl(c1, c2, c1));
	}

	private void dibujarPesoEnElMedio(JMapViewer map, Coordinate c1, Coordinate c2, double peso) {
		Coordinate coordPeso;
		double x = (c1.getLat() + c2.getLat()) / 2;
		double y = (c1.getLon() + c2.getLon()) / 2;
		coordPeso = new Coordinate(x, y);
		mostrarPunto(map, coordPeso, String.valueOf(peso), Color.RED);
	}

	private void mostrarPunto(JMapViewer map, Coordinate c, String texto, Color color) {
		MapMarkerDot punto = new MapMarkerDot(c);
		if (texto != null) {
			punto.setName(texto);
		}
		punto.setBackColor(color);
		map.addMapMarker(punto);
	}

	private boolean agregarArista(Arista<Espia> ar) {
		return aristasG.add(ar);
	}

	private void inicializarObjetos() {
		grafo = new Grafo<>();
		espias = new HashSet<>();
		inicializarAristas();
	}

	private void inicializarAristas() {
		aristasG = new HashSet<>();
		aristasAGM = new TreeSet<>();
	}

	public static enum Algoritmo_AGM {
		PRIM,
		KRUSKAL
	}
}