package logica;

import java.util.ArrayList;
import java.util.TreeSet;

import excepcion.GrafoNoConexoException;

public class AGMKruskal<T extends Comparable<T>> extends AbstractAGM<T> {

	public AGMKruskal(Grafo<T> g) {
		super(g);
	}

	@Override
	public TreeSet<Arista<T>> aristasDelAGM() {
		algoritmoAGM(); 
		return aristasDelAGM;
	}

	// Algoritmo de Kruskal
	private void algoritmoAGM() {
		if (!grafo.esConexo()) {
			throw new GrafoNoConexoException();
		}
		inicializarObjetosUtiles();
		while (verticesConAristasPotenciales.size() != grafo.tamanio()) {
			agregarAristasConExtremos();
			Arista<T> aristaMenorPeso = obtenerAristaDeMenorPesoEntreLasPosibles();
			verticesConAristasPotenciales.add(aristaMenorPeso.obtenerVerticeDestino());
			aristasDelAGM.add(aristaMenorPeso);
			descartarAristasQueGenerarianCiclos();
		}
	}
	
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
	
	private Arista<T> obtenerAristaDeMenorPesoEntreLasPosibles() {
		return aristasConExtremoFuera.pollFirst(); // Extrae la arista de menor peso
	}
}