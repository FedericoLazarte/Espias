package logica;

import java.util.ArrayList;
import java.util.TreeSet;

import excepcion.GrafoNoConexoException;
import logica.Arista;
import logica.Grafo;

public class AGMKruskal<T extends Comparable<T>> extends AbstractAGM<T> {

	// Constructor 
	public AGMKruskal(Grafo<T> g) {
		super(g);
	}

	// Métodos públicos
	@Override
	public TreeSet<Arista<T>> aristasDelAGM() {
		algoritmoAGM(); // Ejecuta el algoritmo para generar el AGM
		return aristasDelAGM;
	}

	// Métodos privados
	
	// Algoritmo de Kruskal
	private void algoritmoAGM() {
		// Primero verificamos que el grafo sea conexo
		if (!grafo.esConexo()) {
			throw new GrafoNoConexoException();
		}

		// Inicializamos las estructuras de datos
		inicializarObjetosUtiles();

		// Mientras no tengamos todos los vértices en el AGM
		while (verticesConAristasPotenciales.size() != grafo.tamanio()) {
			agregarAristasConExtremos();
			Arista<T> aristaMenorPeso = obtenerAristaDeMenorPesoEntreLasPosibles();
			verticesConAristasPotenciales.add(aristaMenorPeso.obtenerVerticeDestino());
			aristasDelAGM.add(aristaMenorPeso);
			descartarAristasQueGenerarianCiclos();
		}
	}
	
	// Inicializa los objetos necesarios para ejecutar el algoritmo
	private void inicializarObjetosUtiles() {
		listaDeVecinos = grafo.listaDeVecinos();
		aristasConExtremoFuera = new TreeSet<>(Arista.aristaComparator());
		aristasDelAGM = new TreeSet<>();
		verticesConAristasPotenciales = new ArrayList<>();
		verticesConAristasPotenciales.add(grafo.primerVertice()); // Comenzamos con el primer vértice
	}

	// Agrega las aristas cuyo extremo está fuera del conjunto de vértices visitados
	private void agregarAristasConExtremos() {
		for (T vertice: verticesConAristasPotenciales) {
			for (Arista<T> arista: listaDeVecinos.get(vertice)) {
				if (!aristasDelAGM.contains(arista) &&
				!verticesConAristasPotenciales.contains(arista.obtenerVerticeDestino()))
				{
					aristasConExtremoFuera.add(arista);
				}
			}
		}
	}
	
	// Obtiene la arista con el menor peso entre las posibles
	private Arista<T> obtenerAristaDeMenorPesoEntreLasPosibles() {
		return aristasConExtremoFuera.pollFirst(); // Extrae la arista de menor peso
	}
}