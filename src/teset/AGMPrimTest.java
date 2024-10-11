package teset;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Test;

import excepcion.GrafoNoConexoException;
import logica.AGMPrim;
import logica.Arista;
import logica.Grafo;

public class AGMPrimTest {

	 @Test(expected = GrafoNoConexoException.class)
	    public void testGrafoNoConexoLanzaExcepcion() {
	        Grafo<Integer> grafoNoConexo = new Grafo<>(1, 2, 3, 4);
	        grafoNoConexo.agregarAristaEntreVertices(1, 2, 1);
	        grafoNoConexo.agregarAristaEntreVertices(3, 4, 1);
	        AGMPrim agm = new AGMPrim(grafoNoConexo);
	        agm.aristasDelAGM();
	    }

	    @Test
	    public void testGrafoUnVertice() {
	        Grafo<Integer> grafoUnVertice = new Grafo<>(1);
	        AGMPrim<Integer> agm = new AGMPrim<>(grafoUnVertice);

	        assertTrue(agm.aristasDelAGM().isEmpty());
	    }

	    @Test
	    public void testAGMGrafoPequenoConexo() {
	        Grafo<Integer> grafoConexo = new Grafo<>(1, 2, 3);
	        grafoConexo.agregarAristaEntreVertices(1, 2, 0.5);
	        grafoConexo.agregarAristaEntreVertices(2, 3, 0.5);
	        grafoConexo.agregarAristaEntreVertices(1, 3, 0.5);

	        AGMPrim<Integer> agm = new AGMPrim<>(grafoConexo);
	        TreeSet<Arista<Integer>> aristasAGM = agm.aristasDelAGM();

	        assertEquals(2, aristasAGM.size());
	    }

	    @Test
	    public void testAGMGrafoConAristasIgualPeso() {
	        Grafo<Integer> grafoConPesoIgual = new Grafo<>(1, 2, 3);
	        grafoConPesoIgual.agregarAristaEntreVertices(1, 2, 1);
	        grafoConPesoIgual.agregarAristaEntreVertices(2, 3, 1);
	        grafoConPesoIgual.agregarAristaEntreVertices(1, 3, 1);

	        AGMPrim<Integer> agm = new AGMPrim<>(grafoConPesoIgual);
	        TreeSet<Arista<Integer>> aristasAGM = agm.aristasDelAGM();

	        assertEquals(2, aristasAGM.size());
	    }

}
