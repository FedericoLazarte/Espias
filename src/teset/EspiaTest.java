package teset;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import logica.Espia;

public class EspiaTest {
    private Espia espia;

    @Before
    public void setUp() {
        espia = new Espia("Agente 007", new Coordinate(51.5074, 0.1278));
    }

    @Test
    public void crearEspiaOk() {
        assertEquals("Agente 007", espia.obtenerNombreEspia());
        assertEquals(new Coordinate(51.5074, 0.1278), espia.obtenerCoordenadaEspia());
    }

    @Test
    public void agregarEspiasCercanos() {
        Espia espiaCercano = new Espia("Agente 009", new Coordinate(34.0522, -118.2437));
        espia.agregarEspiasCeranos(espiaCercano);
        assertEquals(1, espia.obtenerEspiasCercanos().size());
        assertEquals("Agente 009", espia.obtenerEspiasCercanos().get(0).obtenerNombreEspia());
    }


    @Test
    public void toStringTest() {
        String expected = "Nombre: Agente 007 Coordinate[51.5074, 0.1278]";
        assertEquals(expected, espia.toString());
    }
}
