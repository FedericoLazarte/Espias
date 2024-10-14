package logica;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@SuppressWarnings("unchecked")
public class Grafo<T extends Comparable<T>> {
    private TreeMap<T, TreeSet<Arista<T>>> listaDeVecinos;

    public Grafo() {
    	listaDeVecinos = new TreeMap<>();
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
        if (vertices.isEmpty() && !aristas.isEmpty()) {
            throw new IllegalArgumentException("Inconsistencia: sin vertices y con aristas");
        }

        for (T vertice : vertices) {
            agregarVertice(vertice);
        }

        if (aristas == null) {
            return; // Grafo sin aristas
        }

        loop:
        for (Arista<T> arista : aristas) {
            T vertInicio = arista.obtenerVerticeInicio();
            T vertDestino = arista.obtenerVerticeDestino();
            chequearQueSeanDistintosVertices(vertInicio, vertDestino);

            TreeSet<Arista<T>> aristasDelVertice = listaDeVecinos.get(vertInicio);
            for (Arista<T> a : aristasDelVertice) {
                if (a.equals(arista)) {
                    continue loop;
                }
            }

            aristasDelVertice.add(arista);
            Arista<T> aristaAux = new Arista<>(vertDestino, vertInicio, arista.obtenerPeso());
            aristasDelVertice = listaDeVecinos.get(vertDestino);
            aristasDelVertice.add(aristaAux);
        }
    }

    public void agregarVertices(T... vertices) {
        agregarVertices(Arrays.asList(vertices));
    }
    
    public void agregarVertices(Collection<T> vertices) {
    	for (T vertice : vertices) {
            agregarVertice(vertice);
        }
    }

    public void agregarVertice(T vertice) {
        chequearVerticeSiNoExiste(vertice);
        listaDeVecinos.put(vertice, new TreeSet<>(Arista.aristaComparator()));
    }

    public void agregarAristaEntreVertices(T v1, T v2, double p) {
        chequearValidezPosibleArista(v1, v2, p);
        listaDeVecinos.get(v1).add(new Arista<>(v1, v2, p));
        listaDeVecinos.get(v2).add(new Arista<>(v2, v1, p));
    }

    public void cambiarPesoDeArista(T v1, T v2, double nuevoPeso) {
        chequearVerticeSiExiste(v1);
        chequearVerticeSiExiste(v2);
        for (Arista<T> ar : listaDeVecinos.get(v1)) {
            if (ar.obtenerVerticeDestino().equals(v2)) {
                ar.cambiarPeso(nuevoPeso);
            }
        }
        for (Arista<T> ar : listaDeVecinos.get(v2)) {
            if (ar.obtenerVerticeDestino().equals(v1)) {
                ar.cambiarPeso(nuevoPeso);
            }
        }
    }

    public TreeMap<T, TreeSet<Arista<T>>> listaDeVecinos() {
        return listaDeVecinos;
    }

    public int tamanio() {
        return listaDeVecinos.size();
    }

    public boolean existeAristaEntreVertices(T v1, T v2) {
        for (Arista<T> arista : listaDeVecinos.get(v1)) {
            if (arista.obtenerVerticeDestino().equals(v2)) {
                return true;
            }
        }
        return false;
    }

    public double pesoDeLaAristaEntreVertices(T v1, T v2) {
        for (Arista<T> arista : listaDeVecinos.get(v1)) {
            if (arista.obtenerVerticeDestino().equals(v2)) {
                return arista.obtenerPeso();
            }
        }
        throw new IllegalArgumentException("No existe arista entre dichos vertices");
    }

    public Set<T> vecinosDeVertice(T v) {
        Set<T> vecinos = new HashSet<>();
        for (Arista<T> arista : listaDeVecinos.get(v)) {
            vecinos.add(arista.obtenerVerticeDestino());
        }
        return vecinos;
    }

    public T primerVertice() {
        return listaDeVecinos.firstKey();
    }

    public boolean esConexo() {
        return BFS.esConexo(this);
    }

    public TreeSet<Arista<T>> aristasDelAGM(AbstractAGM<T> agmGenerator) {
        return agmGenerator.aristasDelAGM();
    }

    public void printData() {
        for (T vertice : listaDeVecinos.keySet()) {
            System.out.print(vertice + " es vecino de: ");
            TreeSet<Arista<T>> aristas = listaDeVecinos.get(vertice);
            for (Arista<T> a : aristas) {
                System.out.print(a.obtenerVerticeDestino() + ", ");
            }
            System.out.println();
        }
    }

    // Métodos para verificar vertices
    private void chequearVerticeSiExiste(T vertice) {
        if (vertice == null) {
            throw new NullPointerException("el vertice es null");
        }
        if (!listaDeVecinos.containsKey(vertice)) {
            throw new IllegalArgumentException("Vértice no existe");
        }
    }

    private void chequearVerticeSiNoExiste(T vertice) {
        if (vertice == null) {
            throw new NullPointerException("el vertice es null");
        }
        if (listaDeVecinos.containsKey(vertice)) {
            throw new IllegalArgumentException("Vértice existente");
        }
    }

    private void chequearValidezPosibleArista(T v1, T v2, double p) {
        if (p < 0) {
            throw new IllegalArgumentException("peso negativo no válido");
        }
        chequearVerticeSiExiste(v1);
        chequearVerticeSiExiste(v2);
        chequearQueSeanDistintosVertices(v1, v2);
    }

    private void chequearQueSeanDistintosVertices(T v1, T v2) {
        if (v1.equals(v2)) {
            throw new IllegalArgumentException("Invalidado crear arista entre un mismo vértice");
        }
    }
}