package logica;

import java.util.ArrayList;
import java.util.TreeSet;

import excepcion.GrafoNoConexoException;
import logica.Arista;
import logica.Grafo;

public class AGMKruskal<T extends Comparable<T>> extends AbstractAGM<T> {

	public AGMKruskal(Grafo<T> g) {
		super(g);
	}

	@Override
	public TreeSet<Arista<T>> aristasDelAGM() {
		algoritmoAGM(); // Ejecuta el algoritmo para generar el AGM
		return aristasDelAGM;
	}

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
	
	private void inicializarObjetosUtiles() {
		listaDeAdj = grafo.listaDeAdyacencias();
		aristasConExtremoFuera = new TreeSet<>(Arista.aristaComparator());
		aristasDelAGM = new TreeSet<>();
		verticesConAristasPotenciales = new ArrayList<>();
		verticesConAristasPotenciales.add(grafo.primerVertice());
	}

	private Arista<T> obtenerAristaDeMenorPesoEntreLasPosibles() {
		return aristasConExtremoFuera.pollFirst();
	}

	private void agregarAristasConExtremos() {
		for (T vertice: verticesConAristasPotenciales) {
			for (Arista<T> arista: listaDeAdj.get(vertice)) {
				if (!aristasDelAGM.contains(arista) &&
				!verticesConAristasPotenciales.contains(arista.obtenerVerticeDestino()))
				{
					aristasConExtremoFuera.add(arista);
				}
			}
		}
	}
}