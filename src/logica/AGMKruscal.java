package logica;

import java.util.ArrayList;
import java.util.TreeSet;

import excepcion.GrafoNoConexoException;
import logica.Arista;
import logica.Grafo;

public class AGMKruscal<T extends Comparable<T>> extends AbstractAGM<T> {


	public AGMKruscal(Grafo<T> g) {
		super(g);
	}

	@Override
	public TreeSet<Arista<T>> aristasDelAGM() {
		algoritmoAGM();
		return aristasDelAGM;
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

		if (!g.esConexo()) {
			throw new GrafoNoConexoException();
		}

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
}