package tests;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Test;

import excepcion.GrafoNoConexoException;
import logica.AGMKruskal;
import logica.Arista;
import logica.Grafo;

public class AGMKruskalTest {

	 @Test(expected = GrafoNoConexoException.class)
	    public void testGrafoNoConexoLanzaExcepcion() {
	        Grafo<Integer> grafoNoConexo = new Grafo<>(1, 2, 3, 4);
	        grafoNoConexo.agregarAristaEntreVertices(1, 2, 1);
	        grafoNoConexo.agregarAristaEntreVertices(3, 4, 1);
	        AGMKruskal agm = new AGMKruskal(grafoNoConexo);
	        agm.aristasDelAGM();
	    }

	    @Test
	    public void testGrafoUnVertice() {
	        Grafo<Integer> grafoUnVertice = new Grafo<>(1);
	        AGMKruskal<Integer> agm = new AGMKruskal<>(grafoUnVertice);

	        assertTrue(agm.aristasDelAGM().isEmpty());
	    }

	    @Test 
	    public void testAGMGrafoPequenoConexoSinCircuito() {
	    	Grafo<Integer> grafoConexo = new Grafo<>(1, 2, 3);
	        grafoConexo.agregarAristaEntreVertices(1, 2, 0.5);
	        grafoConexo.agregarAristaEntreVertices(2, 3, 0.5);
	        
	        AGMKruskal<Integer> agm = new AGMKruskal<>(grafoConexo);
	        TreeSet<Arista<Integer>> aristasAGM = agm.aristasDelAGM();
	        assertEquals(2, aristasAGM.size());
	    }
	    
	    @Test
	    public void testAGMGrafoPequenoConexoConCircuito() {
	        Grafo<Integer> grafoConexo = new Grafo<>(1, 2, 3);
	        grafoConexo.agregarAristaEntreVertices(1, 2, 0.5);
	        grafoConexo.agregarAristaEntreVertices(2, 3, 0.5);
	        grafoConexo.agregarAristaEntreVertices(1, 3, 0.5);

	        AGMKruskal<Integer> agm = new AGMKruskal<>(grafoConexo);
	        TreeSet<Arista<Integer>> aristasAGM = agm.aristasDelAGM();

	        assertEquals(2, aristasAGM.size());
	    }

	    @Test
	    public void testAGMGrafoConAristasIgualPeso() {
	        Grafo<Integer> grafoConPesoIgual = new Grafo<>(1, 2, 3);
	        grafoConPesoIgual.agregarAristaEntreVertices(1, 2, 1);
	        grafoConPesoIgual.agregarAristaEntreVertices(2, 3, 1);
	        grafoConPesoIgual.agregarAristaEntreVertices(1, 3, 1);

	        AGMKruskal<Integer> agm = new AGMKruskal<>(grafoConPesoIgual);
	        TreeSet<Arista<Integer>> aristasAGM = agm.aristasDelAGM();

	        assertEquals(2, aristasAGM.size());
	    }

}
