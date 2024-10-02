package logica;

import java.util.*;

public class BFS <T extends Comparable<T>>{

	private List<T> verticesNoVisitados;
	private Set<T> visitados;
	private Grafo<T> grafo;

	public static <T extends Comparable<T>> boolean esConexo(Grafo<T> g) {
		return new BFS<T>(g).esConexo();
	}

	private BFS(Grafo<T> grafo) {
		this.grafo = grafo;
	}

	private boolean esConexo() {
		return grafo.tamanio() == 0 ||
		alcanzablesDesdeVertice(grafo.primerVertice()).size() == grafo.tamanio();
	}

	private void inicializarObjetosUtiles(T verticeOrigen) {
		verticesNoVisitados = new ArrayList<>();
		verticesNoVisitados.add(verticeOrigen);
		visitados = new HashSet<>();
	}

	private Set<T> alcanzablesDesdeVertice(T verticeOrigen){
		inicializarObjetosUtiles(verticeOrigen);
		while (!verticesNoVisitados.isEmpty()) {
			T v = verticesNoVisitados.get(0);
			visitados.add(v);
			agregarVecinosPendientes(grafo, v);
			verticesNoVisitados.remove(0);
		}
		return visitados;
	}

	private void agregarVecinosPendientes(Grafo<T> g, T v) {
		for (T vecino: g.vecinosDeVertice(v)) {
			if (!visitados.contains(vecino) &&
				!verticesNoVisitados.contains(vecino)) {

				verticesNoVisitados.add(vecino);
			}
		}
	}
}
