package com.ia.ui;

import static com.ia.util.Constantes.FRASE_DEFECTO;
import static com.ia.util.Constantes.RUTA_PROYECTO;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.ia.model.Parametros;
import com.ia.programa.Programa2;
import com.ia.util.Util;

public class Pantalla {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
	private static String nombreArchivoResumenGenerico = "resumen_a_genetico_" + sdf.format(new Date()) + ".txt";
	private static String rutaResumen = null;
	private static String rutaConfiguracion;
	private static Parametros parametros;
	private static Boolean cfgPersonalizada = false;
	
	public static void asd(String[] args) {
		
		parametros = Util.obtenerParametros(null);

		/**
		 * Inputs.
		 */
		JTextField inputFrase = new JTextField();
		inputFrase.setPreferredSize(new Dimension(300, 20));
		JTextField inputNPob = new JTextField();
		inputNPob.setPreferredSize(new Dimension(300, 20));
		JTextField inputNGen = new JTextField();
		inputNGen.setPreferredSize(new Dimension(300, 20)); 
		JTextField inputNRes = new JTextField();
		inputNRes.setPreferredSize(new Dimension(300, 20)); 
		JTextField inputQ = new JTextField();
		inputQ.setPreferredSize(new Dimension(300, 20)); 
		JTextField inputArchivoResumen = new JTextField();
		inputArchivoResumen.setPreferredSize(new Dimension(300, 20)); 

		inputFrase.setText(FRASE_DEFECTO.toUpperCase());
		inputNPob.setText(parametros.getNumIndividuos().toString());
		inputNGen.setText(parametros.getNumMaxGeneraciones().toString());
		inputNRes.setText(parametros.getNumGenResumen().toString());
		inputQ.setText(parametros.getProbMutacion().toString());
		inputArchivoResumen.setText(nombreArchivoResumenGenerico);
		/**
		 * Fin Inputs.
		 */
		
		/**
		 * Botones
		 */
		/** Generar*/
		JButton btnGenerar = new JButton("Generar!");
		btnGenerar.setPreferredSize(new Dimension(100, 20));

		/** Configuración*/
		JButton btnConfiguracion = new JButton("Configuracion");
		btnConfiguracion.setPreferredSize(new Dimension(100,20));
		btnConfiguracion.setToolTipText("Personalizar la configuración del programa.");
		
		JButton btnNPob = new JButton("Defecto");
		btnNPob.setPreferredSize(new Dimension(100, 20));
		JButton btnNGen = new JButton("Defecto");
		btnNGen.setPreferredSize(new Dimension(100, 20));
		JButton btnNRes = new JButton("Defecto");
		btnNRes.setPreferredSize(new Dimension(100, 20));
		JButton btnQ = new JButton("Defecto");
		btnQ.setPreferredSize(new Dimension(100, 20));
		
		/** Resúmen*/
		JButton btnVerResumen = new JButton("Resumen");
		btnVerResumen.setPreferredSize(new Dimension(100,20));
		btnVerResumen.setEnabled(false);
		btnVerResumen.setToolTipText("Ver resultados generados en esta ventana." 
							+ (!btnVerResumen.isEnabled()?" Deshabilitado: Aún no se ha generado nada":""));
		JButton btnExportar = new JButton("Carpeta");
		btnExportar.setPreferredSize(new Dimension(100, 20));
		/**
		 * Fin Botones
		 */
		
		/**
		 * Ventana, JPanel y líneas.
		 */
		JFrame pantalla = new JFrame("Algoritmo Genetico (IAII)");
		pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel gui = new JPanel();
		pantalla.setContentPane(gui);
		JPanel linea1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel linea2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel linea3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel lineaNPob = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaNGen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaNRes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaQ = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaResumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaBotonesConfiguracion = new JPanel(new FlowLayout(FlowLayout.LEFT));

        lineaNPob.setVisible(false);
		lineaNGen.setVisible(false);
		lineaNRes.setVisible(false);
		lineaQ.setVisible(false);
		lineaResumen.setVisible(false);
		
        gui.add(linea1);
        gui.add(linea2);
        gui.add(linea3);
        
        gui.add(lineaNGen);
        gui.add(lineaNPob);
        gui.add(lineaNRes);
        gui.add(lineaQ);
        gui.add(lineaResumen);
        /**
         * Fin Ventana, JPanel y líneas.
         */
		
        
        /**
         * Labels.
         */
        JLabel jlabelFrase = new JLabel("Frase: "); 
        jlabelFrase.setPreferredSize(new Dimension(70, 20));
        JLabel labelNPob = new JLabel("NPob: "); 
        labelNPob.setPreferredSize(new Dimension(70, 20));
        JLabel labelNGen = new JLabel("NGen: "); 
        labelNGen.setPreferredSize(new Dimension(70, 20));
        JLabel labelNRes = new JLabel("NRes: "); 
        labelNRes.setPreferredSize(new Dimension(70, 20));
        JLabel labelQ = new JLabel("Q: "); 
        labelQ.setPreferredSize(new Dimension(70, 20));
        JLabel labelArchivoResumen = new JLabel("Resumen: "); 
        labelArchivoResumen.setPreferredSize(new Dimension(70, 20));
        /**
         * Fin Labels.
         */
        
        /**
         * Añadir componentes a cada línea.
         */
        linea1.add(jlabelFrase);
        linea1.add(inputFrase);
        linea1.add(btnGenerar);
        
        linea2.add(labelArchivoResumen);
        linea2.add(inputArchivoResumen);
        linea2.add(btnExportar);

        lineaNPob.add(labelNPob);
        lineaNPob.add(inputNPob);
        lineaNPob.add(btnNPob);
        lineaNGen.add(labelNGen);
        lineaNGen.add(inputNGen);
        lineaNGen.add(btnNGen);
        lineaNRes.add(labelNRes);
        lineaNRes.add(inputNRes);
        lineaNRes.add(btnNRes);
        lineaQ.add(labelQ);
        lineaQ.add(inputQ);
        lineaQ.add(btnQ);

        linea3.add(btnConfiguracion);
        linea3.add(btnVerResumen);
        /**
         * Fin A�adir componentes a cada linea.
         */
		
        /**
         * Inicializar la pantalla.
         */
        gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
        pantalla.pack();
        pantalla.setVisible(true);
        /**
         * Fin Inicializar la pantalla.
         */
		
        
        /**
         * Action listeners.
         */
        btnConfiguracion.addActionListener(new ActionListener()
		{
        	/** Mostrar/ocultar configuracion*/
			public void actionPerformed(ActionEvent e)
			{
				cfgPersonalizada = !cfgPersonalizada;
				
				lineaNPob.setVisible(!lineaNPob.isVisible());
				lineaNGen.setVisible(!lineaNGen.isVisible());
				lineaNRes.setVisible(!lineaNRes.isVisible());
				lineaQ.setVisible(!lineaQ.isVisible());
				
		        gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		        gui.revalidate();
		        gui.validate();
		        gui.repaint();
		        pantalla.pack();
			}
		});
        
        btnNPob.addActionListener(new ActionListener()
		{
        	/** NPob por defecto*/
			public void actionPerformed(ActionEvent e)
			{
				inputNPob.setText(parametros.getNumIndividuos().toString());
			}
		});
        
        btnNGen.addActionListener(new ActionListener()
        {
        	/** NGen por defecto*/
        	public void actionPerformed(ActionEvent e)
        	{
        		inputNGen.setText(parametros.getNumMaxGeneraciones().toString());
        	}
        });
        
        btnNRes.addActionListener(new ActionListener()
        {
        	/** NRes por defecto*/
        	public void actionPerformed(ActionEvent e)
        	{
        		inputNRes.setText(parametros.getNumGenResumen().toString());
        	}
        });
        
        btnQ.addActionListener(new ActionListener()
        {
        	/** Q por defecto*/
        	public void actionPerformed(ActionEvent e)
        	{
        		inputQ.setText(parametros.getProbMutacion().toString());
        	}
        });
        
		btnExportar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser(); 
				chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				chooser.setDialogTitle("Exportación resultados");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
				//    
				if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) { 
					 rutaResumen = chooser.getSelectedFile().toString();
				} else {
					System.out.println("No Selection ");
				}
			
			}
		});
		
		btnGenerar.addActionListener(new ActionListener()
        {
        	/** Generar! */
        	public void actionPerformed(ActionEvent e)
        	{
        		String archivoResumenStr = "";
        		
        		if(cfgPersonalizada)
        			parametros = new Parametros(Integer.valueOf(inputNPob.getText()), 
        										Integer.valueOf(inputNGen.getText()), 
        										Integer.valueOf(inputNRes.getText()), 
        										Double.valueOf(inputQ.getText()));
        		
        		if(rutaResumen!=null && !rutaResumen.isEmpty()) 
        			archivoResumenStr += rutaResumen;
        		else
        			archivoResumenStr += RUTA_PROYECTO;
        		
        		if(inputArchivoResumen.getText()!=null && !inputArchivoResumen.getText().isEmpty())
        			archivoResumenStr += (File.separator.toString() + inputArchivoResumen.getText()); 
        		else
        			archivoResumenStr += (File.separator.toString() + nombreArchivoResumenGenerico);
        		
        		btnGenerar.setText("Generando...");
        		new Programa2(inputFrase.getText().toLowerCase(), parametros, archivoResumenStr);
        		btnGenerar.setText("Generar!");
        	}
        });
		
	}
}
