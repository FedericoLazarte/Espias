package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import controlador.Controlador;
import logica.Espia;
import logica.EspiasHarcodeado;

@SuppressWarnings("serial")
public class Main extends JFrame{

	private Controlador controlador;
	private JButton exitButton;
	private JMapViewer mapViewer;
	private JTextField valorPesoEntradaUser;
	JComboBox<Espia> comboBox1;
	JComboBox<Espia> comboBox2;
	private boolean agmEnPantalla;
	private JFrame frameParaElegirRelacion;
	
	//--------------------nuevo----------------------------
	private static String direccionArchivo ="src/logica/espias.txt";
	//-----------------------------------------------------
	
	
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

	/**
	 * Create the application.
	 */
	public Main() {
		agmEnPantalla = false;
		mapViewer = new JMapViewer();
		controlador = new Controlador(this);
		initializeUI();
	}

	//cambio!! al metodo de EspiasHarcodeado "EspiasDeArgentina" le pasas el archivo
	private void agregarEspeciasArgentinos() {
		for(Espia e : EspiasHarcodeado.EspiasDeArgentina(direccionArchivo))
			nuevoEspia(e);
	}
	
	private void agregarPesosAleatorio() {
		controlador.espiasConPesosAleatorios();
	}
	
	private void initializeUI() {
		setTitle("Diseño de Regiones de un País");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		JPanel mapPanel = new JPanel(new BorderLayout());
		exitButton = new JButton("Salir");

		// Mapa
		mapViewer.setZoom(5);
		mapViewer.setTileSource(new org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.Mapnik());
		mapViewer.setDisplayPosition(new Coordinate(-40.6037, -65.3816), 4);
		mapViewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 2) {
					String nombreEspia;
					nombreEspia = JOptionPane.showInputDialog("Ingrese nombre de espía:");
					if (nombreEspia == null)
						return;
					Point punto = e.getPoint();
					Coordinate c = (Coordinate) mapViewer.getPosition(punto);
					Espia espia = new Espia(nombreEspia, c);
					nuevoEspia(espia);
				}
			}
		});
mapPanel.add(mapViewer, BorderLayout.CENTER);
		
		JLabel labelAyuda = new JLabel("Doble clic derecho para una nueva locación");
		buttonPanel.add(labelAyuda);
		labelAyuda.setHorizontalAlignment(SwingConstants.LEFT);

		comboBox1 = new JComboBox<>();
		buttonPanel.add(comboBox1);
		comboBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarComboBox2();
			}
		});
		
		comboBox2 = new JComboBox<>();
		buttonPanel.add(comboBox2);

		// AGREGO PESO A RELACION AL MAPA
		JButton btnAgregarRelacion = new JButton("Agregar relación");
		buttonPanel.add(btnAgregarRelacion);

		btnAgregarRelacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cantidadEspias() == 0)
					mostrarAlerta("Aún no hay espías disponibles.");
				else {
					frameParaElegirRelacion.setVisible(true);
				}
			}
		});
		

		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Cerrar la aplicación
				System.exit(0);
			}
		});
		
		// Configurar el diseño de la ventana
				getContentPane().setLayout(new BorderLayout());
				getContentPane().add(buttonPanel, BorderLayout.NORTH);

				
				JButton btnGenerarAGM = new JButton("AGM");
				btnGenerarAGM.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (controlador.dibujarAGM()) {
						agmEnPantalla = true;
					//	quitarAristasAGM.setVisible(true);
						}
					}
				});
				
				buttonPanel.add(btnGenerarAGM);

				getContentPane().add(mapPanel, BorderLayout.CENTER);

				crearFrameAgregarRelacion();
				
				JButton cargarDatosEspiasArgentina = new JButton("Espías Argentinos");
				JButton cargarCercanosAleatoriosArgentina = new JButton("Relaciones aleatorias espías Argentinos");
				buttonPanel.add(cargarCercanosAleatoriosArgentina);
				buttonPanel.add(cargarDatosEspiasArgentina);
				cargarCercanosAleatoriosArgentina.setVisible(false);
				
				cargarDatosEspiasArgentina.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						agregarEspeciasArgentinos();
						cargarCercanosAleatoriosArgentina.setVisible(true);
						buttonPanel.remove(cargarDatosEspiasArgentina);
						buttonPanel.repaint();
					}
				});
				
				cargarCercanosAleatoriosArgentina.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						agregarPesosAleatorio();
						buttonPanel.remove(cargarCercanosAleatoriosArgentina);
						buttonPanel.repaint();
					}
				});
				
				JButton quitarPuntos = new JButton("Borrar todo");
				buttonPanel.add(quitarPuntos);
				quitarPuntos .addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						quitarTodosLosPuntos();
						buttonPanel.add(cargarDatosEspiasArgentina);
						buttonPanel.add(cargarCercanosAleatoriosArgentina);
						cargarCercanosAleatoriosArgentina.setVisible(false);
						repaint();
					}
				});

	}
	
	// previene que se pueda añadir arista entre mismo vertice
		private void actualizarComboBox2() {
			comboBox2.removeAllItems();
			for (int i = 0; i < comboBox1.getItemCount(); i++) {
				Espia e = (Espia) comboBox1.getItemAt(i);
				if (!e.equals(comboBox1.getSelectedItem()))
					comboBox2.addItem(e);
			}
		}
		
		private void crearFrameAgregarRelacion() {
			frameParaElegirRelacion = new JFrame();
			JPanel panel = new JPanel();
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
				public void actionPerformed(ActionEvent e) {
					cargarNuevaArista();				
				}
			});
			frameParaElegirRelacion.setBounds(100,100,550,200);
			frameParaElegirRelacion.add(panel);
			frameParaElegirRelacion.setVisible(false);
			
		}
		
		private void quitarTodosLosPuntos() {
			controlador.quitarTodosLosPuntos();
			mapViewer.removeAllMapMarkers();
			mapViewer.removeAllMapPolygons();
			comboBox1.removeAllItems();
			actualizarComboBox2();
		}
		
		
		private int cantidadEspias() {
			return comboBox1.getItemCount();
		}
		
		
		public JMapViewer getMapViewer() {
			return mapViewer;
		}

		public void mostrarAlerta(String mensaje) {
			JOptionPane.showMessageDialog(null, mensaje);
		}
		
		private void nuevoEspia(Espia espia) {
			controlador.agrgarNuevoEspia(espia);
			comboBox1.addItem(espia);
			comboBox1.setSelectedItem(espia);
			actualizarComboBox2();
			if (agmEnPantalla)
				controlador.mostrarMapaConGrafo();
			agmEnPantalla = false;
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
}
