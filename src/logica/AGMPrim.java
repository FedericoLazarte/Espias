package logica;

import java.util.ArrayList;
import java.util.TreeSet;

import logica.Arista;
import logica.Grafo;

public class AGMPrim<T extends Comparable<T>> extends AbstractAGM<T> {

    public AGMPrim(Grafo<T> g) {
        super(g);
    }

    @Override
    public TreeSet<Arista<T>> aristasDelAGM() {
        algoritmoAGM(); // Ejecuta el algoritmo para generar el AGM
        return aristasDelAGM;
    }

    // Algoritmo de Prim
    private void algoritmoAGM() {
        // Inicializamos las estructuras de datos
        inicializarObjetosUtiles();

        // Mientras no tengamos todos los vértices en el AGM
        while (verticesConAristasPotenciales.size() != g.tamanio()) {
            agregarAristasConExtremos();
            Arista<T> aristaMenorPeso = obtenerAristaDeMenorPesoEntreLasPosibles();

            // Si ya no hay aristas disponibles, salimos del bucle
            if (aristaMenorPeso == null) {
                break;
            }

            verticesConAristasPotenciales.add(aristaMenorPeso.obtenerVerticeDestino());
            aristasDelAGM.add(aristaMenorPeso);
            descartarAristasQueGenerarianCiclos();
        }
    }

    // Inicializa los objetos necesarios para ejecutar el algoritmo
    private void inicializarObjetosUtiles() {
        adjList = g.listaDeAdyacencias();
        aristasConExtremoFuera = new TreeSet<>(Arista.aristaComparator());
        aristasDelAGM = new TreeSet<>();
        verticesConAristasPotenciales = new ArrayList<>();
        verticesConAristasPotenciales.add(g.primerVertice()); // Comenzamos con el primer vértice
    }

    // Agrega las aristas cuyo extremo está fuera del conjunto de vértices visitados
    private void agregarAristasConExtremos() {
        for (T vertice : verticesConAristasPotenciales) {
            for (Arista<T> arista : adjList.get(vertice)) {
                if (!aristasDelAGM.contains(arista) &&
                    !verticesConAristasPotenciales.contains(arista.obtenerVerticeDestino())) {
                    aristasConExtremoFuera.add(arista); // Agregamos aristas con un vértice no visitado
                }
            }
        }
    }

    // Obtiene la arista con el menor peso entre las posibles
    private Arista<T> obtenerAristaDeMenorPesoEntreLasPosibles() {
        return aristasConExtremoFuera.pollFirst(); // Extrae la arista de menor peso
    }
}
