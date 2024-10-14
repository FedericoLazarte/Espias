package logica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class EspiasArgentinos {
	
	private static boolean seAgregaronEspiasCercanos = false;
    private static List<Espia> espias = new ArrayList<>();
	
	// Método para cargar los espías desde el archivo de texto
    private static void cargarEspiasDesdeArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);

        try (FileInputStream fis = new FileInputStream(archivo);
             Scanner scanner = new Scanner(fis)) {

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();

                // Separar la línea en partes usando el espacio como delimitador
                String[] partes = linea.split(" ");

                if (partes.length >= 3) {
                    try {
                        // Las últimas dos partes son las coordenadas
                        double latitud = Double.parseDouble(partes[partes.length - 2].trim());
                        double longitud = Double.parseDouble(partes[partes.length - 1].trim());

                        // El nombre es todo lo que está antes de las coordenadas
                        StringBuilder nombreBuilder = new StringBuilder();
                        for (int i = 0; i < partes.length - 2; i++) {
                            nombreBuilder.append(partes[i]).append(" ");
                        }
                        String nombre = nombreBuilder.toString().trim();  // Eliminar el último espacio extra

                        // Crear el espia y agregarlo a la lista
                        Coordinate coordenada = new Coordinate(latitud, longitud);
                        Espia espia = new Espia(nombre, coordenada);
                        espias.add(espia);

                    } catch (NumberFormatException e) {
                        // Si ocurre un error al convertir las coordenadas
                        System.err.println("Error en el formato de coordenadas en la línea: " + linea);
                    }
                } else {
                    System.out.println("Línea mal formada: " + linea);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Espia [] EspiasDeArgentina(String archivo) {
    	if(!seAgregaronEspiasCercanos) {
    		cargarEspiasDesdeArchivo(archivo);
    		agregarEspiasCercanos();
    		
    	}
    	return espias.toArray(new Espia[0]);
    }
    
    
    private static void agregarEspiasCercanos() {
        // Suponiendo que 'espias' tiene al menos 24 elementos
        espias.get(0).agregarEspiasCercanos(espias.get(1), espias.get(5), espias.get(7), espias.get(11), espias.get(15), espias.get(20));
        espias.get(1).agregarEspiasCercanos(espias.get(0));
        espias.get(2).agregarEspiasCercanos(espias.get(5), espias.get(10), espias.get(16), espias.get(21), espias.get(23));
        espias.get(3).agregarEspiasCercanos(espias.get(6), espias.get(8), espias.get(16), espias.get(20), espias.get(21));
        espias.get(4).agregarEspiasCercanos(espias.get(15), espias.get(19));
        espias.get(5).agregarEspiasCercanos(espias.get(0), espias.get(2), espias.get(11), espias.get(10), espias.get(18), espias.get(20), espias.get(21));
        espias.get(6).agregarEspiasCercanos(espias.get(3), espias.get(7), espias.get(13), espias.get(20));
        espias.get(7).agregarEspiasCercanos(espias.get(0), espias.get(6), espias.get(20));
        espias.get(8).agregarEspiasCercanos(espias.get(3), espias.get(16));
        espias.get(9).agregarEspiasCercanos(espias.get(16));
        espias.get(10).agregarEspiasCercanos(espias.get(2), espias.get(5), espias.get(17), espias.get(18));
        espias.get(11).agregarEspiasCercanos(espias.get(0), espias.get(5), espias.get(12), espias.get(15), espias.get(18));
        espias.get(12).agregarEspiasCercanos(espias.get(11), espias.get(14), espias.get(17), espias.get(18));
        espias.get(13).agregarEspiasCercanos(espias.get(6));
        espias.get(14).agregarEspiasCercanos(espias.get(12), espias.get(15));
        espias.get(15).agregarEspiasCercanos(espias.get(0), espias.get(4), espias.get(11), espias.get(14));
        espias.get(16).agregarEspiasCercanos(espias.get(2), espias.get(3), espias.get(8), espias.get(9), espias.get(21), espias.get(23));
        espias.get(17).agregarEspiasCercanos(espias.get(10), espias.get(12), espias.get(18));
        espias.get(18).agregarEspiasCercanos(espias.get(5), espias.get(11), espias.get(12), espias.get(13), espias.get(17));
        espias.get(19).agregarEspiasCercanos(espias.get(4), espias.get(22));
        espias.get(20).agregarEspiasCercanos(espias.get(0), espias.get(3), espias.get(5), espias.get(6), espias.get(7), espias.get(21));
        espias.get(21).agregarEspiasCercanos(espias.get(2), espias.get(3), espias.get(5), espias.get(17), espias.get(20), espias.get(23));
        espias.get(22).agregarEspiasCercanos(espias.get(19));
        espias.get(23).agregarEspiasCercanos(espias.get(2), espias.get(16), espias.get(21));

        seAgregaronEspiasCercanos = true;
    }
}