package teset;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import logica.Grafo;

public class GrafoTest {

    private Grafo<String> grafo;

    @Before
    public void setUp() {
        grafo = new Grafo<>();
    }

    @Test
    public void agregarVerticeOk() {
        int cantVertices = grafo.tamanio();
        grafo.agregarVertice("A");
        assertEquals(cantVertices + 1, grafo.tamanio());
    }

    @Test
    public void agregarAristaOk() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarAristaEntreVertices("A", "B", 0.1);

        assertTrue(grafo.existeAristaEntreVertices("A", "B"));
        assertTrue(grafo.existeAristaEntreVertices("B", "A"));
        assertEquals(0.1, grafo.pesoDeLaAristaEntreVertices("A", "B"), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void agregarAristaEntreVerticeInexistenteDaExcepcion() {
        grafo.agregarAristaEntreVertices("A", "B", 5.0);
    }

    @Test
    public void obtenerVecinosDeVertice() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarAristaEntreVertices("A", "B", 1.0);
        grafo.agregarAristaEntreVertices("A", "C", 0.5);

        Set<String> vecinos = grafo.vecinosDeVertice("A");
        assertTrue(vecinos.contains("B"));
        assertTrue(vecinos.contains("C"));
        assertEquals(2, vecinos.size());
    }

    @Test(expected = NullPointerException.class)
    public void agregarVerticeNulo() {
        grafo.agregarVertice(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pesoDeAristaIlegalDaExcepcion() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarAristaEntreVertices("A", "B", 10.0);
    }

    
    @Test
    public void cambiarPesoDeAristaOk() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarAristaEntreVertices("A", "B", 1.0);

        grafo.cambiarPesoDeArista("A", "B", 0.3);
        assertEquals(0.3, grafo.pesoDeLaAristaEntreVertices("A", "B"), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cambiarPesoAristaInexistente() {
        grafo.agregarVertice("A");
        grafo.cambiarPesoDeArista("A", "B", 10.0);
    }

    @Test
    public void testConexo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarAristaEntreVertices("A", "B", 1.0);

        assertTrue(grafo.esConexo());
    }

    @Test
    public void testNoConexo() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");

        assertFalse(grafo.esConexo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void agregarAristaMismoVertice() {
        grafo.agregarVertice("A");
        grafo.agregarAristaEntreVertices("A", "A", 1.0);
    }

    @Test
    public void agregarMultiplesVertices() {
        grafo.agregarVertices("A", "B", "C", "D");
        assertEquals(4, grafo.tamanio());
    }
}
