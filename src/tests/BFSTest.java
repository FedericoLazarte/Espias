package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import logica.BFS;
import logica.Grafo;

public class BFSTest {

	@Test
    public void testEsConexoGrafoVacio() {
        Grafo<Integer> grafoVacio = new Grafo<>();
        assertTrue(BFS.esConexo(grafoVacio));
    }

    @Test
    public void testEsConexoUnVertice() {
        Grafo<Integer> grafoUnVertice = new Grafo<>(1);
        assertTrue(BFS.esConexo(grafoUnVertice));
    }

    @Test
    public void testEsConexoGrafoConexo() {
        Grafo<Integer> grafoConexo = new Grafo<>(1, 2, 3);
        grafoConexo.agregarAristaEntreVertices(1, 2, 1);
        grafoConexo.agregarAristaEntreVertices(2, 3, 1);
        assertTrue(BFS.esConexo(grafoConexo));
    }

    @Test
    public void testEsConexoGrafoNoConexo() {
        Grafo<Integer> grafoNoConexo = new Grafo<>(1, 2, 3, 4);
        grafoNoConexo.agregarAristaEntreVertices(1, 2, 1);
        grafoNoConexo.agregarAristaEntreVertices(3, 4, 1);
        assertFalse(BFS.esConexo(grafoNoConexo));
    }

}
