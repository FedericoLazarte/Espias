package logica;

import java.util.*;

@SuppressWarnings("unchecked")
public class Grafo<T extends Comparable<T>>{

	private TreeMap<T, TreeSet<Arista<T>>> adjList;

	public Grafo() {
		crearMapa();
	}

	public Grafo(T... vertices) {
		this(Arrays.asList(vertices));
	}

	public Grafo(Collection<T> vertices) {
		this();
		agregarVertices(vertices);
	}

	public Grafo(Collection<T> vertices, Collection<Arista<T>> aristas) {

		this();

		if (vertices.isEmpty() && !aristas.isEmpty())
			throw new IllegalArgumentException("Inconsistencia: sin vertices y con aristas");

		for (T vertice: vertices)
			agregarVerticeIgnorarChequeo(vertice);

		if (aristas == null) return; // Grafo sin aristas

		loop:
		for (Arista<T> arista: aristas) {

			T vertInicio = arista.obtenerVerticeInicio();
			T vertDestino = arista.obtenerVerticeDestino();
			chequearQueSeanDistintosVertices(vertInicio, vertDestino);

			TreeSet<Arista<T>> aristasDelVertice = adjList.get(vertInicio);

			for (Arista<T> a: aristasDelVertice) {
				if (a.equals(arista)) {
					continue loop;
				}
			}

			aristasDelVertice.add(arista);

			Arista<T> aristaAux = new Arista<>(vertDestino, vertInicio, arista.obtenerPeso());
			aristasDelVertice = adjList.get(vertDestino);
			aristasDelVertice.add(aristaAux);
		}
	}

	private void crearMapa() {
		adjList = new TreeMap<>();
	}

	protected TreeMap<T, TreeSet<Arista<T>>> listaDeAdyacencias() {
		return adjList;
	}

	public int tamanio() {
		return adjList.size();
	}

	private void chequearVerticeSiExiste(T v) {
		if (v == null)
			throw new NullPointerException("v es null");
		if (!adjList.containsKey(v))
			throw new IllegalArgumentException("Vértice no existe");
	}

	private void chequearVerticeSiNoExiste(T v) {
		if (v == null)
			throw new NullPointerException("v es null");
		if (adjList.containsKey(v))
			throw new IllegalArgumentException("Vértice existente");
	}

	private void chequearValidezPosibleArista(T v1, T v2, double p) {
		if (p < 0)
			throw new IllegalArgumentException("peso negativo no válido");
		chequearVerticeSiExiste(v1);
		chequearVerticeSiExiste(v2);
		chequearQueSeanDistintosVertices(v1, v2);
	}

	private void chequearQueSeanDistintosVertices(T v1, T v2) {
		if (v1.equals(v2)) throw new IllegalArgumentException
		("Invalidado crear arista entre un mismo vértice");
	}

	public boolean existeAristaEntreVertices(T v1, T v2) {
		for (Arista<T> arista: adjList.get(v1)) {
			if (arista.obtenerVerticeDestino().equals(v2))
				return true;
		}
		return false;
	}

	public double pesoDeLaAristaEntreVertices(T v1, T v2) {
		for (Arista<T> arista: adjList.get(v1)) {
			if (arista.obtenerVerticeDestino().equals(v2))
				return arista.obtenerPeso();
		}
		throw new IllegalArgumentException("No existe arista entre dichos vertices");
	}

	public void cambiarPesoDeArista(T v1, T v2, double nuevoPeso) {
		chequearVerticeSiExiste(v1);
		chequearVerticeSiExiste(v2);
		for (Arista<T> ar: adjList.get(v1)) {
			if (ar.obtenerVerticeDestino().equals(v2))
				ar.cambiarPeso(nuevoPeso);
		}
		for (Arista<T> ar: adjList.get(v2)) {
			if (ar.obtenerVerticeDestino().equals(v1))
				ar.cambiarPeso(nuevoPeso);
		}
	}

	public void agregarVertice(T n) {
		chequearVerticeSiNoExiste(n);
		agregarVerticeIgnorarChequeo(n);
	}

	public void agregarVertices(Collection<T> vertices) {
		if (tamanio() != 0) {
			for (T vertice: vertices)
				agregarVertice(vertice);
		} else {
			for (T vertice: vertices)
				agregarVerticeIgnorarChequeo(vertice);
		}
	}

	public void agregarVertices(T... vertices) {
		agregarVertices(Arrays.asList(vertices));
	}

	private void agregarVerticeIgnorarChequeo(T v) {
		adjList.put(v, new TreeSet<>(Arista.aristaComparator()));
	}
	
	public void agregarAristaEntreVertices(T v1, T v2, double p) {
		chequearValidezPosibleArista(v1,v2,p);
		adjList.get(v1).add(new Arista<>(v1,v2,p));
		adjList.get(v2).add(new Arista<>(v2,v1,p));
	}

	public Set<T> vecinosDeVertice(T v) {
		Set<T> vecinos = new HashSet<>();
		
		for (Arista<T> arista: adjList.get(v))
			vecinos.add(arista.obtenerVerticeDestino());
		
		return vecinos;
	}

	protected T primerVertice() {
		return adjList.firstKey();
	}

	// para testear bfs
	public void printData() {
		for (T vertice: adjList.keySet()) {
			System.out.print(vertice + " es vecino de: ");
			TreeSet<Arista<T>> aristas = adjList.get(vertice);
			for (Arista<T> a: aristas) {
				System.out.print(a.obtenerVerticeDestino()+", ");
			}
			System.out.println();
		}
	}
	
	public boolean esConexo() {
		return BFS.esConexo(this);
	}

	public TreeSet<Arista<T>> aristasDelAGM(){
		return new AGM<T>(this).aristasDelAGM();
	}

}
