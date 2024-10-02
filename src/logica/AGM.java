package logica;

import java.util.*;

import excepcion.GrafoNoConexoException;

public class AGM<T extends Comparable<T>>{

	private TreeMap<T, TreeSet<Arista<T>>> adjList;
	private TreeSet<Arista<T>> aristasConExtremoFuera;
	private TreeSet<Arista<T>> aristasDelAGM;
	private List<T> verticesConAristasPotenciales;
	private Grafo<T> g;

	public AGM(Grafo<T> g) {
		this.g = g;
		algoritmoAGM();
	}

	public TreeSet<Arista<T>> aristasDelAGM() {
		return aristasDelAGM;
	}

	public Grafo<T> AGMdelGrafo() {
		return new Grafo<>(verticesConAristasPotenciales,aristasDelAGM);
	}

	private void inicializarObjetosUtiles() {
		adjList = g.listaDeAdyacencias();
		aristasConExtremoFuera = new TreeSet<>(Arista.aristaComparator());
		aristasDelAGM = new TreeSet<>();
		verticesConAristasPotenciales = new ArrayList<>();
		verticesConAristasPotenciales.add(g.primerVertice());
	}

	// creo de sería de kruskal
	private void algoritmoAGM() {

		if (!g.esConexo())
			throw new GrafoNoConexoException();

		inicializarObjetosUtiles();

		while (verticesConAristasPotenciales.size() != g.tamanio()) {
			agregarAristasConExtremos();
			Arista<T> aristaMenorPeso = obtenerAristaDeMenorPesoEntreLasPosibles();
			verticesConAristasPotenciales.add(aristaMenorPeso.obtenerVerticeDestino());
			aristasDelAGM.add(aristaMenorPeso);
			descartarAristasQueGenerarianCiclos();
		}
	}

	private Arista<T> obtenerAristaDeMenorPesoEntreLasPosibles() {
		return aristasConExtremoFuera.pollFirst();
	}

	private void agregarAristasConExtremos() {
		for (T vertice: verticesConAristasPotenciales) {
			for (Arista<T> arista: adjList.get(vertice)) {
				if (!aristasDelAGM.contains(arista) &&
				!verticesConAristasPotenciales.contains(arista.obtenerVerticeDestino()))
				{
					aristasConExtremoFuera.add(arista);
				}
			}
		}
	}

	private void descartarAristasQueGenerarianCiclos() {
		Set<Arista<T>> aristasParaDescartar = new HashSet<>();
		for (Arista<T> arista: aristasConExtremoFuera) {
			if (verticesConAristasPotenciales.contains(arista.obtenerVerticeInicio())
			&& verticesConAristasPotenciales.contains(arista.obtenerVerticeDestino())) {
				aristasParaDescartar.add(arista);
			}
		}
		aristasConExtremoFuera.removeAll(aristasParaDescartar);
	}

}
