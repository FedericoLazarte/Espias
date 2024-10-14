package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import controlador.Controlador;
import controlador.Controlador.Algoritmo_AGM;
import logica.Espia;
import logica.EspiasArgentinos;

@SuppressWarnings("serial")
public class Main extends JFrame{

	private Controlador controlador;
	private JMapViewer mapa;
	private boolean agmEnPantalla;
	private static String direccionArchivo ="src/logica/espias.txt";
	
	//Frames y paneles
	private JFrame frameParaElegirRelacion;
	private JPanel panelDeMapa;
	private JPanel panelDeBotones;
	
	//Elementos del panel de botones
	private JButton btnAgregarRelacion;
	private JComboBox<Algoritmo_AGM> comboBoxAlgoritmo;
	private JButton btnGenerarAGM;
	private JButton compararAlgoritmos;
	private JButton cargarDatosEspiasArgentina;
	private JButton cargarCercanosAleatoriosArgentina;
	private JButton quitarPuntos;
	
	//Elementos del frame para elegir relacion
	private JComboBox<Espia> comboBox1;
	private JComboBox<Espia> comboBox2;
	private JButton exitButton;
	private JTextField valorPesoEntradaUser;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}

	public Main() {
		agmEnPantalla = false;
		controlador = new Controlador(this);
		interfazGrafica();
	}

	public JMapViewer getMapViewer() {
		return mapa;
	}

	public void mostrarAlerta(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}
	
	private void interfazGrafica() {
		//Frame principal
		setTitle("Diseño de Regiones de un País");
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//Paneles del frame principal
		panelDeMapa = new JPanel(new BorderLayout());
		panelDeBotones = new JPanel();

		crearMapa();
		crearBotones();
		crearFrameAgregarRelacion();
		
		exitButton = new JButton("Salir");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Cerrar la aplicación
				System.exit(0);
			}
		});

		//Configurar el diseño de la ventana
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panelDeBotones, BorderLayout.NORTH);
		getContentPane().add(panelDeMapa, BorderLayout.CENTER);
	}

	private void crearMapa() {
		mapa = new JMapViewer();
		mapa.setZoom(5);
		mapa.setTileSource(new org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.Mapnik());
		mapa.setDisplayPosition(new Coordinate(-40.6037, -65.3816), 4);
		
		eventoMouseAlMapa();
		
		panelDeMapa.add(mapa, BorderLayout.CENTER);
	}

	private void eventoMouseAlMapa() {
		mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 2) {
					String nombreEspia;
					nombreEspia = JOptionPane.showInputDialog("Ingrese nombre de espía:");
					if (nombreEspia == null) {
						return;
					}
					Point punto = e.getPoint();
					Coordinate c = (Coordinate) mapa.getPosition(punto);
					Espia espia = new Espia(nombreEspia, c);
					nuevoEspia(espia);
				}
			}
		});
	}

	private void nuevoEspia(Espia espia) {
		controlador.agregarNuevoEspia(espia);
		
		//Se actualizan los comboboxs
		comboBox1.addItem(espia);
		comboBox1.setSelectedItem(espia);
		actualizarComboBox2();
		if (agmEnPantalla) {
			controlador.mostrarMapaConGrafo();
		}
		agmEnPantalla = false;
	}
	
	private void crearBotones() {
		JLabel labelAyuda = new JLabel("Doble clic derecho para una nueva locación");
		panelDeBotones.add(labelAyuda);
		labelAyuda.setHorizontalAlignment(SwingConstants.LEFT);
		crearBtnAgregarRelacion();
		crearComboBoxAlgoritmo();
		crearBotonAGM();
		crearBotonCompararAlgm();
		crearBotonesEspiasArgentinos();
		crearBotonBorrarEspias();
	}

	private void crearBtnAgregarRelacion() {
		btnAgregarRelacion = new JButton("Agregar relación");
		panelDeBotones.add(btnAgregarRelacion);

		btnAgregarRelacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cantidadEspias() == 0) {
					mostrarAlerta("Aún no hay espías disponibles.");
				} else {
					frameParaElegirRelacion.setVisible(true);
				}
			}
		});
	}
	
	private void crearComboBoxAlgoritmo() {
		comboBoxAlgoritmo = new JComboBox<>();
		comboBoxAlgoritmo.addItem(Algoritmo_AGM.KRUSKAL);
		comboBoxAlgoritmo.addItem(Algoritmo_AGM.PRIM);
		comboBoxAlgoritmo.setBounds(10,10,80,20);
	    panelDeBotones.add(comboBoxAlgoritmo);
	}
	
	private void crearBotonAGM() {
		btnGenerarAGM = new JButton("AGM");
		
		btnGenerarAGM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Algoritmo_AGM seleccionAlgoritmo = (Algoritmo_AGM)comboBoxAlgoritmo.getSelectedItem();
				if (controlador.dibujarAGM(seleccionAlgoritmo)) {
				agmEnPantalla = true;
				}
			}
		});

		panelDeBotones.add(btnGenerarAGM);
	}
	
	private void crearBotonCompararAlgm() {
		compararAlgoritmos = new JButton("Comparar Algoritmos");
		panelDeBotones.add(compararAlgoritmos);
		compararAlgoritmos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					long resultados[] = controlador.compararAlgoritmos();
					JOptionPane.showMessageDialog(panelDeBotones, "Kruskal = " + resultados[0] + " | Prim = " + resultados[1]);							
				}catch(Exception ex) {
					System.out.println(ex.getStackTrace());
					JOptionPane.showMessageDialog(panelDeBotones,"debe cargar los espias y sus aristas primero");
				}
			}
		});
		
	}
	
	private void crearBotonesEspiasArgentinos() {
		crearBtnEspiasArgentinos();
		crearBtnRelacionesAleatorias();
		cargarCercanosAleatoriosArgentina.setVisible(false);
	}

	private void crearBtnEspiasArgentinos() {
		cargarDatosEspiasArgentina = new JButton("Espías Argentinos");
		cargarDatosEspiasArgentina.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarEspiasArgentinos();
				cargarCercanosAleatoriosArgentina.setVisible(true);
				panelDeBotones.remove(cargarDatosEspiasArgentina);
				panelDeBotones.repaint();
			}
		});
		panelDeBotones.add(cargarDatosEspiasArgentina);
	}

	private void crearBtnRelacionesAleatorias() {
		cargarCercanosAleatoriosArgentina = new JButton("Relaciones aleatorias espías Argentinos");
		cargarCercanosAleatoriosArgentina.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarPesosAleatorio();
				panelDeBotones.remove(cargarCercanosAleatoriosArgentina);
				panelDeBotones.repaint();
			}
		});
		panelDeBotones.add(cargarCercanosAleatoriosArgentina);
	}
	
	private void crearBotonBorrarEspias() {
		quitarPuntos = new JButton("Borrar todo");
		panelDeBotones.add(quitarPuntos);
		quitarPuntos .addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quitarTodosLosPuntos();
				panelDeBotones.add(cargarDatosEspiasArgentina);
				panelDeBotones.add(cargarCercanosAleatoriosArgentina);
				cargarCercanosAleatoriosArgentina.setVisible(false);
				repaint();
			}
		});
		
	}
	
	private void crearFrameAgregarRelacion() {
		frameParaElegirRelacion = new JFrame();
		JPanel panel = new JPanel();
		
		crearComboboxs();
		panel.add(comboBox1);
		panel.add(comboBox2);
		
		JLabel lblNewLabel_2 = new JLabel("       Indique peso:");
		panel.add(lblNewLabel_2);
		valorPesoEntradaUser = new JTextField();
		panel.add(valorPesoEntradaUser);
		valorPesoEntradaUser.setColumns(10);

		JButton cargarRelacion = new JButton("Cargar relación");
		panel.add(cargarRelacion);
		cargarRelacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarNuevaArista();
			}
		});
		frameParaElegirRelacion.setBounds(100,100,550,200);
		frameParaElegirRelacion.add(panel);
		frameParaElegirRelacion.setVisible(false);

	}
	
	private void crearComboboxs() {
		comboBox1 = new JComboBox<>();
		panelDeBotones.add(comboBox1);
		comboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarComboBox2();
			}
		});

		comboBox2 = new JComboBox<>();
		panelDeBotones.add(comboBox2);
	}
	
	private void cargarNuevaArista() {
		try {
			if (agmEnPantalla) {
				controlador.mostrarMapaConGrafo();
			}
			double peso = Double.valueOf(valorPesoEntradaUser.getText());
			Espia e1 = (Espia) comboBox1.getSelectedItem();
			Espia e2 = (Espia) comboBox2.getSelectedItem();
			controlador.nuevaArista(e1, e2, peso);
		} catch (NumberFormatException err) {
			mostrarAlerta("Error: no ingresó un valor numérico");
		} finally {
			valorPesoEntradaUser.setText(null);
		}
	}
	
	private void quitarTodosLosPuntos() {
		controlador.quitarTodosLosPuntos();
		mapa.removeAllMapMarkers();
		mapa.removeAllMapPolygons();
		comboBox1.removeAllItems();
		actualizarComboBox2();
	}
	
	private void agregarEspiasArgentinos() {
		for(Espia e : EspiasArgentinos.EspiasDeArgentina(direccionArchivo)) {
			nuevoEspia(e);
		}
	}

	private void agregarPesosAleatorio() {
		controlador.espiasConPesosAleatorios();
	}
	
	// previene que se pueda añadir arista entre mismo vertice
	private void actualizarComboBox2() {
		comboBox2.removeAllItems();
		for (int i = 0; i < comboBox1.getItemCount(); i++) {
			Espia e = comboBox1.getItemAt(i);
			//No se agrega el espia que se encuentra en el combobox1
			if (!e.equals(comboBox1.getSelectedItem())) {
				comboBox2.addItem(e);
			}
		}
	}

	private int cantidadEspias() {
		return comboBox1.getItemCount();
	}
}