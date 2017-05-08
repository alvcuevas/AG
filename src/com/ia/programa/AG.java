package com.ia.programa;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;

public class AG {

	static SimpleDateFormat sdf ;
	static String nombreArchivoResumenGenerico ;
	static String rutaResumen;
	static String ultimaRutaResumen;
	static String rutaConfiguracion;
	static Parametros parametros;
	static Boolean cfgPersonalizada;
	static Boolean generado;
	static Thread hiloResumen;
	
	public static void main(String[] args) {
		
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		parametros = Util.obtenerParametros(null);
		nombreArchivoResumenGenerico = "resumen_a_genetico_" + sdf.format(new Date()) + ".txt";
		cfgPersonalizada = false;
		rutaResumen = Constantes.RUTA_PROYECTO;
		generado = false;

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

		inputFrase.setText(Constantes.FRASE_DEFECTO.toUpperCase());
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
		JButton btnConfiguracion = new JButton("Configuración");
		btnConfiguracion.setPreferredSize(new Dimension(100,20));
		btnConfiguracion.setToolTipText("Personalizar la configuración del programa.");

		/** Ver Ruta Exportacion*/
		JButton btnVerRuta = new JButton("Ver Ruta");
		btnVerRuta.setPreferredSize(new Dimension(100,20));
		btnVerRuta.setToolTipText("Personalizar la configuración del programa.");
		
		JButton btnNPob = new JButton("Defecto");
		btnNPob.setPreferredSize(new Dimension(100, 20));
		JButton btnNGen = new JButton("Defecto");
		btnNGen.setPreferredSize(new Dimension(100, 20));
		JButton btnNRes = new JButton("Defecto");
		btnNRes.setPreferredSize(new Dimension(100, 20));
		JButton btnQ = new JButton("Defecto");
		btnQ.setPreferredSize(new Dimension(100, 20));
		
		/** Resúmen*/
		JButton btnVerResumen = new JButton("Resúmen");
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
		JFrame pantalla = new JFrame("Algoritmo Genético (IAII)");
		pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel gui = new JPanel();
		pantalla.setContentPane(gui);
		JPanel linea1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel linea2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaRutaResumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel linea3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel lineaNPob = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaNGen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaNRes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaQ = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel lineaResumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        Border border = lineaRutaResumen.getBorder();
        Border margin = new EmptyBorder(0,80,0,0);
        lineaRutaResumen.setBorder(new CompoundBorder(border, margin));

        lineaNPob.setVisible(false);
		lineaNGen.setVisible(false);
		lineaNRes.setVisible(false);
		lineaQ.setVisible(false);
		lineaResumen.setVisible(false);
		lineaRutaResumen.setVisible(false);
		
		JTextArea resumenArea = new JTextArea(8, 40);
		resumenArea.setEditable(false);
		resumenArea.setFocusable(false);
		JScrollPane resumenScroll = new JScrollPane(resumenArea);
		JPanel resumenPanel = new JPanel(new BorderLayout());
		resumenPanel.add(new JLabel("Resumen:", SwingConstants.LEFT), BorderLayout.PAGE_START);
		resumenPanel.add(resumenScroll);
		resumenPanel.setVisible(false);
		
        gui.add(linea1);
        gui.add(linea2);
        gui.add(lineaRutaResumen);
        gui.add(linea3);
        gui.add(resumenPanel);
        
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
        JLabel labelArchivoResumen = new JLabel("Resúmen: "); 
        labelArchivoResumen.setPreferredSize(new Dimension(70, 20));
        JLabel labelRutaActual = new JLabel(rutaResumen);
        labelRutaActual.setPreferredSize(new Dimension(400, 20));
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
        
        lineaRutaResumen.add(labelRutaActual);

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

        JCheckBox pobButton = new JCheckBox("Imprimir población");
        pobButton.setToolTipText("Imprimir o no la población en cada generación");
        pobButton.setSelected(false);
        linea3.add(pobButton);
        linea3.add(btnConfiguracion);
        linea3.add(btnVerRuta);
        linea3.add(btnVerResumen);
        /**
         * Fin Añadir componentes a cada línea.
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
        	/** Mostrar/ocultar configuración*/
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
        
        btnVerRuta.addActionListener(new ActionListener()
		{
        	/** Mostrar/ocultar ruta de exportación*/
			public void actionPerformed(ActionEvent e)
			{
				lineaRutaResumen.setVisible(!lineaRutaResumen.isVisible());
				btnVerRuta.setText(lineaRutaResumen.isVisible() ? "Ocultar" : "Ver Ruta");
				
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
					labelRutaActual.setText(rutaResumen);
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
        			archivoResumenStr += Constantes.RUTA_PROYECTO;
        		
        		if(inputArchivoResumen.getText()!=null && !inputArchivoResumen.getText().isEmpty())
        			archivoResumenStr += (File.separator.toString() + inputArchivoResumen.getText()); 
        		else
        			archivoResumenStr += (File.separator.toString() + nombreArchivoResumenGenerico);
        		
		        final String archivoResumenStrFinal = archivoResumenStr;
		        
		        Thread hilo = new Thread() {
		            public void run() {
		            	generado = false;
		            	new Programa(inputFrase.getText().toLowerCase(), parametros, archivoResumenStrFinal, pobButton.isSelected());
		            	_actualizarResumen(pantalla, gui, resumenArea, resumenScroll, resumenPanel, 0);
		            	generado = true;
		            }  
		        };

		        hilo.start();

		        ultimaRutaResumen = archivoResumenStr;
		        inputArchivoResumen.setText("resumen_a_genetico_" + sdf.format(new Date()) + ".txt");
		        btnVerResumen.setEnabled(true);
		        
        	}
        });
		
		
		
		btnVerResumen.addActionListener(new ActionListener()
        {
        	/** Generar! */
        	public void actionPerformed(ActionEvent e)
        	{
        		
        		if(resumenPanel.isVisible()){
        			resumenPanel.setVisible(false);
        			if(hiloResumen!=null&&hiloResumen.isAlive())
        				hiloResumen.interrupt();
        			gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
    		        gui.revalidate();
    		        gui.validate();
    		        gui.repaint();
    		        pantalla.pack();
        			
        		} else {
	        		hiloResumen = new Thread() {
			            public void run() {
			            	double ultimoMax = 0;
			            	
			            	do{
			            		ultimoMax = _actualizarResumen(pantalla, gui, resumenArea, resumenScroll, resumenPanel, ultimoMax);
			            	}while(!generado);
			            }
	
	        		};
	        		
	        		hiloResumen.start();
	        		
        		}
        	}
        });
		
	}

	private static double _actualizarResumen(JFrame pantalla, JPanel gui, JTextArea resumenArea,
			JScrollPane resumenScroll, JPanel resumenPanel, double ultimoMax) {
		
		if((ultimoMax) == (double)resumenScroll.getVerticalScrollBar().getMaximum())
			resumenScroll.getVerticalScrollBar().setValue(resumenScroll.getVerticalScrollBar().getMaximum());
		resumenArea.setText(Util.leerArchivo(ultimaRutaResumen));
		resumenPanel.setVisible(true);
		
		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		gui.revalidate();
		gui.validate();
		gui.repaint();
		pantalla.pack();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			String message = "run: InterruptedException" + 
					e.getMessage() + 
					e.getCause() + 
					e.getStackTrace();
			System.out.println(message);
		}
		ultimoMax = (double)resumenScroll.getVerticalScrollBar().getMaximum();
		return ultimoMax;
	}
}

class Programa {
	
	/**
	 * Definición de variables. 
	 */
	private Parametros parametros;
	private String rutaResumen;
	private List<String> poblacion;
	private List<Integer> numCoincidencias;
	private List<Double> listaFitness;
	private String target;
	private Boolean imprimirPoblacion;
	
	public Programa(String target, Parametros parametros, String rutaResumen, Boolean imprimirPoblacion){
		
		this.target = target;
		this.parametros = parametros;
		this.rutaResumen = rutaResumen;
		this.imprimirPoblacion = imprimirPoblacion;
		ejecutarPrograma();
	}
	
	// Para ir probando el programa
	public void ejecutarPrograma() {

		/*
		 * Inicializar variables.
		 */
		poblacion = new ArrayList<String>();
		numCoincidencias = new ArrayList<Integer>();
		listaFitness = new ArrayList<Double>();
		
		
		Util.imprimirEnArchivo(rutaResumen, "Configuración:" + System.getProperty("line.separator") 
			+ parametros.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
		poblacion = Util.generaPoblacion(parametros.getNumIndividuos(), target, rutaResumen);
		
		/*
		 *  Para contar el tiempo que tarda en correr el algoritmo
		 */
		long start = System.currentTimeMillis();    
		
		_programaConGeneraciones();
		
		
//		_programaSinGeneraciones();

		/*
		 * Contar el tiempo e imprimir este y la población en la última generación. 
		 */
		long elapsedTime = System.currentTimeMillis() - start;

		Util.imprimirEnArchivo(rutaResumen, "Tiempo transcurrido: " + new Double(new Double(elapsedTime)/new Double(1000))+ "s"+System.getProperty("line.separator")+System.getProperty("line.separator"));
		Util.imprimirEnArchivo(rutaResumen, "(*)Poblacion final: " +System.getProperty("line.separator") + poblacion.toString());
//		System.out.println(elapsedTime);
	}
	

	/**
	 * Una generación del algoritmo genético. Se comprueba toda la población, y se muta a los
	 * cromosomas que correspondan dependiendo de su fitness normalizado.
	 * 
	 * @param target: la frase target que se quiere alcanzar.
	 * @param poblacion: la población de individuos actual.
	 * @param numCoincidencias: una lista con el numero de coincidencias de cada cromosoma de la población 
	 * 		  con respecto al target.
	 * @param parametros: el objeto Parametros cargado desde configuracion.cfg.
	 * @param listaFitness: una lista con el fitness de cada individuo de la población.
	 *  
	 */
	public void algoritmoGenetico(){
		
		numCoincidencias = Util.calculaCoincidencias(poblacion, target);
		listaFitness = UtilAlgoritmos.calculaFitness(poblacion, target, numCoincidencias, parametros);
		poblacion = UtilAlgoritmos.mutaPoblacion(poblacion, target, numCoincidencias, listaFitness, parametros);
	}
	
	/**
	 * Éste método utiliza el ngen del configuracion.cfg. 
	 */
	private void _programaConGeneraciones(){
		for(int i=0; i<parametros.getNumMaxGeneraciones(); i++){
			algoritmoGenetico();
			if(i%parametros.getNumGenResumen()==0){
				Util.imprimirResumen(i, target, numCoincidencias, poblacion, rutaResumen);
				if(imprimirPoblacion)
					Util.imprimirEnArchivo(rutaResumen, "Población:" + System.getProperty("line.separator") + 
							poblacion.toString() +System.getProperty("line.separator")+System.getProperty("line.separator"));
			}
		}
	}
	
	/**
	 * Éste método no utiliza el ngen del configuracion.cfg.
	 * 
	 * En lugar de esto, sigue mutando a la población hasta encontrar un cromosoma que sea
	 * igual que target.
	 *  
	 */
	public void _programaSinGeneraciones(List<String> poblacion, String target, List<Integer> numCoincidencias, 
			List<Double> listaFitness, Parametros parametros, String rutaResumen) {
		int i = 0;
		while(!Util.hasEqual(numCoincidencias, target)){
			i++;
			algoritmoGenetico();
		}
		System.out.println("Generaciones para generar: " + i);
	}
}

class Constantes {

	public static final Boolean PRODUCCION = true;
	public static final String FRASE_DEFECTO = "animula vagula blandula";
	public static final String RUTA_PROYECTO = System.getProperty("user.dir");
	public static final String RUTA_ARCHIVO_CONFIGURACION = "configuracion.cfg"; 
			//(PRODUCCION ? "resources/configuracion.cfg" : "/resources/configuracion.cfg");
	public static final String RUTA_IMG_CHECK = RUTA_PROYECTO + "/resources/haken.png";

	public static final String NUM_INDIVIDUOS = "npob";
	public static final String MAX_GENERACIONES = "ngen";
	public static final String NUM_GEN_RESUMEN = "nres";
	public static final String NUM_GEN_MUESTREO = "ntotal";
	public static final String PROB_MUTACION = "q";
    
}

/**
 * En esta clase están únicamente los métodos de apoyo, como generar letras o números
 * random, cargar la configuración, leer o escribir ficheros de texto, etc...
 */
class Util {
	
	/**
	 * Un generador de randoms para usar desde varios métodos.
	 */
	static Random r ;
	
	public static Double porcentajeAcierto(String target, List<String> poblacion){
		
		Double porcentajeAcierto = 0.0;
		Integer numTotalCaracteresEnPoblacion = poblacion.size()*target.length();
		
		for(int i=0; i<poblacion.size(); i++){
			for(int j=0; j<target.length(); j++){
				if(poblacion.get(i).charAt(j) == target.charAt(j))
					porcentajeAcierto += (new Double(1)/numTotalCaracteresEnPoblacion)*100;
			}
		}
		
		return porcentajeAcierto;
	}
	
	/**
	 * Devuelve el porcentaje de frases iguales al target en la población actual.
	 * 
	 * @param largoTarget: el largo de la frase target.
	 * @param numCoincidencias: una lista con el numero de coincidencias de cada cromosoma
	 * 		  de la población. Esta lista tiene que cargarse antes de pasarla a este método.
	 * 
	 * @return el porcentaje de frases iguales al target en la población.
	 */
	public static Double porcentajeNTar(Integer largoTarget, List<Integer> numCoincidencias){
		
		Double porcentaje = 0.0;
		
		for(int i=0; i<numCoincidencias.size(); i++){
			if(numCoincidencias.get(i) == largoTarget)
				porcentaje += (new Double(1)/numCoincidencias.size())*100;
		}
		return porcentaje;
	}
	
	/**
	 * Devuelve el número de cromosomas iguales a la frase target en la población.
	 * 
	 * @param largoTarget: el largo de la frase target.
	 * @param numCoincidencias: una lista con el numero de coincidencias de cada cromosoma
	 * 		  de la población. Esta lista tiene que cargarse antes de pasarla a este método.
	 * 
	 * @return el número de cromosomas iguales a la frase target en la población.
	 */
	public static Integer numeroNTar(Integer largoTarget, List<Integer> numCoincidencias){
		
		Integer numero = 0;
		
		for(int i=0; i<numCoincidencias.size(); i++){
			if(numCoincidencias.get(i) == largoTarget)
				numero++;
		}
		return numero;
	}
	
	/**
	 * Lee la frase inicial por consola y lo almacena en un array
	 * @return la frase "target"
	 */
	public static String leeTarget(){
		
		String target = new String();
		Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la frase: ");
 
        if(scanner.hasNextLine())
        	target = scanner.nextLine().toString();
        
        System.out.println("(*)Sentencia target: " + target.toString() + " (longitud " + target.length() + ")");

        scanner.close();	
        
        return target;
	}
	
	/**
	 * Lectura del archivo desde una ruta especifica
	 * 
	 * @param ruta: la ruta en la que está el archivo a leer.
	 * 
	 * @return el contenido del archivo en un String.
	 */
	public static String leerArchivo(String ruta){
		
		try {
			return new String(Files.readAllBytes(Paths.get(ruta)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.getMessage();
		}
		
		return null;
	}
	
	/**
	 * Obtencion de los parametros del archivo configuracion para almacenarlos en la clase Parametros.
	 * 
	 * @param ruta: si se quiere, se le puede pasar una ruta a un archivo de configuración alternativo. Si
	 * se pasa "ruta" en null, se utilizará el archivo de configuración por defecto.
	 * 
	 * @return el objeto Parametros cargado.
	 */
	public static Parametros obtenerParametros(String ruta){
		
		Parametros parametros = new Parametros();
		String archivo = leerResourceString(Constantes.RUTA_ARCHIVO_CONFIGURACION);
		String[] lineas = archivo.split(System.getProperty("line.separator"));
		
		try {
			
			for(String linea : lineas){
				
				if(linea.contains("\r"))
					linea = linea.replace("\r", "");
				
				String valor = linea.split("=")[1];
				
				if(linea.startsWith(Constantes.NUM_INDIVIDUOS)){
					parametros.setNumIndividuos(Integer.valueOf(valor));
				}
				if(linea.startsWith(Constantes.MAX_GENERACIONES)){
					parametros.setNumMaxGeneraciones(Integer.valueOf(valor));
				}
				if(linea.startsWith(Constantes.NUM_GEN_RESUMEN)){
					parametros.setNumGenResumen(Integer.valueOf(valor));
				}
				if(linea.startsWith(Constantes.PROB_MUTACION)){
					parametros.setProbMutacion(Double.valueOf(valor));
				}
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		return parametros;
	}
	
	/**
	 * Genera poblacion de individuos aleatorios y los almacena
	 * 
	 * @param numIndividuos: el numero de individuos que se quiere generar. Esto se corresponde con
	 * 		  el parámetro npob del configuracion.cfg.
	 * @param target: la frase target que se quiere generar.
	 * 
	 * @return una lista de frases con "numIndividuos" elementos del mismo largo que la frase target.
	 *  
	 */
	public static List<String> generaPoblacion(int numIndividuos, String target, String rutaResumen){
		
		List<String> poblacion = new ArrayList<String>();
		
		for(int i=0; i<numIndividuos; i++){
			
			poblacion.add(new String());
			
			for(int j=0; j<target.length(); j++){
				int c = obtenerCharRandom();
				poblacion.set(i, poblacion.get(i) + (char)c);
			}
		}
		
		imprimirEnArchivo(rutaResumen, "(*)Poblacion aleatoria inicial:"+System.getProperty("line.separator") + poblacion+System.getProperty("line.separator")+System.getProperty("line.separator"));
		return poblacion;
	}
	
	/**
	 * Genera un caracter random dentro del rango de varavteres válidos.
	 * 
	 * @return un caracter random dentro del rango de caracteres válidos.
	 */
	public static char obtenerCharRandom(){

		r = new Random();
		/*
		 * El rango de generación incluye todas las letras desde la A, mas una que corresponde al espacio.
		 */
		int c = r.nextInt(27) + (byte)'a';
		
		/*
		 * Si el random obtenido esta fuera del rango de letras, devolvemos el caracter espacio (" ") que
		 * corresponde con el int con valor 32.
		 */
		if(c==(byte)'a'+26) c = 32;
		
		return (char) c;
	}
	
	/**
	 * Devuelve un numero random entre 0 (incluído) y max (no incluído).
	 * 
	 * @param max: el primer número que NO está incluído en el rango que se quiere.
	 * 
	 * @return el numero random generado.
	 */
	public static int obtenerRandomMax(int max){
		r = new Random();
		int c = r.nextInt(max);
		return c;
	}
	
	/**
	 * Devuelve un double random entre 0 y 1.
	 *  
	 * @return un double random entre 0 y 1.
	 */
	public static double obtenerRandomDouble(){
		double c = ThreadLocalRandom.current().nextDouble(0, 1);
		return c;
	}
	
	/**
	 * Cálculo del número total de coincidencias entre los individuos y el target introducido
	 * 
	 * @param poblacion: la población de individuos que se quiere comprobar.
	 * @param target: la frase target.
	 * 
	 * @return una lista con 
	 */
	public static List<Integer> calculaCoincidencias(List<String> poblacion, String target){
		
		List<Integer> numCoincidencias = Arrays.asList(new Integer[poblacion.size()]);
		Integer numeroMaximo = 0;
		
		for(int i=0; i<poblacion.size(); i++){
			numCoincidencias.set(i, new Integer(0));
			for(int j=0; j<target.length(); j++){
			
				if(poblacion.get(i).charAt(j) == target.charAt(j)){
					numCoincidencias.set(i, numCoincidencias.get(i)+1);
				}
			}
			if(numeroMaximo==0 || numeroMaximo<numCoincidencias.get(i)) 
				numeroMaximo = numCoincidencias.get(i);
		}
		
		return numCoincidencias;
		
	}
	
	/**
	 * Si indice+1 es el máximo, se devuelve 0. Si no, se devuelve indice+1.
	 *  
	 * @param indice: el índice actual.
	 * @param max: el máximo (al que no se puede llegar).
	 * 
	 * @return el nuevo índice.
	 */
	public static Integer actualizaIndice(Integer indice, Integer max){
		if(indice+1<max) return indice+1;
		else return 0;
	}
	
	/** 
	 * Este método comprueba si existe un cromosoma igual que el target en la población.
	 * 
	 * En lugar de comparar strings, compara el largo del target con los numeros de coincidencias
	 * guardados en una lista de integers.
	 * 
	 */
	public static boolean hasEqual(List<Integer> numCoincidencias, String target){
		for(Integer numCoin : numCoincidencias){
			if(numCoin==target.length())
				return true;
		}
		return false;
	}

	public static void imprimirResumen(Integer generacion, String target, List<Integer> numCoincidencias, 
			List<String> poblacion, String rutaResumen){

		DecimalFormat dFormat = new DecimalFormat("0.00");

		String resumen = "Generacion: " + generacion +System.getProperty("line.separator");
		resumen += "Porcentaje de NTar: " + dFormat.format(Util.porcentajeNTar(target.length(), numCoincidencias)) + "%" +System.getProperty("line.separator");
		resumen += "Número de NTar: " + Util.numeroNTar(target.length(), numCoincidencias) +System.getProperty("line.separator");
		resumen += "Porcentaje de acierto: " + dFormat.format(Util.porcentajeAcierto(target, poblacion)) + "%"+System.getProperty("line.separator")+System.getProperty("line.separator");
		imprimirEnArchivo(rutaResumen, resumen);
	}
	
	public static void imprimirEnArchivo(String archivo, String contenido){
		Thread hilo = new Thread() {
            public void run() {
        		BufferedWriter writer = null;
                try {
                    //
                    writer = new BufferedWriter(new FileWriter(archivo, true));
                    writer.write(contenido);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // Close the writer regardless of what happens...
                        writer.close();
                    } catch (Exception e) {
                    }
                }
            }
		};
		hilo.start();
	}
	
	public static String leerResourceString(String ruta){
		InputStream is = Util.class.getClassLoader().getResourceAsStream(ruta);
		String theString;
		try {
			theString = IOUtils.toString(is, StandardCharsets.UTF_8);
			return theString;
		} catch (IOException e) {
			String message = "leerResourceString: IOException" + 
				e.getMessage() + 
				e.getCause() + 
				e.getStackTrace();
			System.out.println(message);
			return null;
		}
	}
}

/**
 * En esta clase están los métodos relacionados únicamente con el algoritmo genético.
 */
class UtilAlgoritmos {
	
	/**
	 * Cálculo del fitness de los individuos
	 * 
	 * @param poblacion: la población de individuos que se quiere comprobar. 
	 * @param target: la frase target.
	 * @param numCoincidencias: una lista que contiene el numero de coincidencias que cada cromosoma
	 * 		  tiene con la frase target.
	 * @param parametros: el objeto parámetros cargado desde el configuracion.cfg.
	 * 
	 * @return una lista con el fitness de cada individuo de la población.
	 */
	public static List<Double> calculaFitness(List<String> poblacion, String target, List<Integer> numCoincidencias, 
			Parametros parametros){
		
		List<Double> listaFitness = new ArrayList<Double>();
		
		/*
		 * Cálculo del fitness de cada individuo
		 */
		for(int i=0; i<poblacion.size(); i++){
			listaFitness.add(Math.pow(Math.E, numCoincidencias.get(i)-target.length())
					- Math.pow(Math.E, (-target.length())));
		}
		
//		System.out.println("(*)Valores Fi/individuo: " + listaFitness);
		
		return listaFitness;
		
	}
	
	public static List<String> mutaPoblacion(List<String> poblacion, String target, List<Integer> numCoincidencias,
			List<Double> listaFitness, Parametros parametros) {
		
		List<Double> fitnessNormalizado = Arrays.asList(new Double[poblacion.size()]);
		
		Integer indice = Util.obtenerRandomMax(poblacion.size());
		Integer nuevoIndice = Util.obtenerRandomMax(poblacion.size());
		Integer primerIndice = indice;
		
		Integer maxCoincidencias = 0;
		
		for(int i=0; i<numCoincidencias.size();i++) 
			if(maxCoincidencias<numCoincidencias.get(i)) 
				maxCoincidencias = numCoincidencias.get(i);
		
		Boolean primera = true;
		
		do{
			if(!primera){
				indice = Util.actualizaIndice(indice, poblacion.size());
			}
			primera=false;

			fitnessNormalizado.set(indice, listaFitness.get(indice) / 
					(Math.pow(Math.E, (maxCoincidencias-target.length()))) - Math.pow(Math.E, (-target.length()))) ;
		
			double randomMonteCarlo = Util.obtenerRandomDouble();

//			System.out.println("(*)Valor Fi: " + listaFitness.get(indice));
//			System.out.println("(*)Valor Fi Normalizado: " + fitnessNormalizado.get(indice));
//			System.out.println("(*)Valor random Monte Carlo: " + randomMonteCarlo);
			
			if(randomMonteCarlo<fitnessNormalizado.get(indice)){
				poblacion.set(nuevoIndice, mutaIndividuo(poblacion.get(indice), parametros));
				numCoincidencias = Util.calculaCoincidencias(poblacion, target);
				listaFitness = calculaFitness(poblacion, target, numCoincidencias, parametros);
			}
			
		}while(indice!=primerIndice);
		
		return poblacion;
	}
	
	/**
	 * Proceso de mutacion sobre individuo seleccionado. 
	 * 
	 * Para cada letra (gen) del individuo (cromosoma):
	 * 
	 * - se genera un random de 0 a 1. 
	 * 		- si el random es menor que la Q, el gen NO muta.
	 * 		- si es mayor que Q, el gen muta.
	 * 
	 * @param individuo: el individuo que se va a mutar.
	 * @param parametros: el objeto parametros cargado desde la configuracion.
	 * 
	 * @return devuelve el individuo con los genes que sean mutados (puede ser igual).
	 */
	public static String mutaIndividuo(String individuo, Parametros parametros){
		
		String nuevoIndividuo = new String();
		
		for(int i=0; i<individuo.length(); i++){
			
			double randomMonteCarlo = Util.obtenerRandomDouble();
			
			if(randomMonteCarlo<parametros.getProbMutacion()){
				nuevoIndividuo += individuo.charAt(i);
			} else {
				int c = Util.obtenerCharRandom();
				nuevoIndividuo += (char) c;
			}
		}
		
		return nuevoIndividuo;
		
	}
}

class Parametros {

	private Integer numIndividuos;
	private Integer numMaxGeneraciones;
	private Integer numGenResumen;
	private Double probMutacion;
	public Parametros() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Parametros(Integer numIndividuos, Integer numMaxGeneraciones, Integer numGenResumen, Double probMutacion) {
		super();
		this.numIndividuos = numIndividuos;
		this.numMaxGeneraciones = numMaxGeneraciones;
		this.numGenResumen = numGenResumen;
		this.probMutacion = probMutacion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numGenResumen == null) ? 0 : numGenResumen.hashCode());
		result = prime * result + ((numIndividuos == null) ? 0 : numIndividuos.hashCode());
		result = prime * result + ((numMaxGeneraciones == null) ? 0 : numMaxGeneraciones.hashCode());
		result = prime * result + ((probMutacion == null) ? 0 : probMutacion.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parametros other = (Parametros) obj;
		if (numGenResumen == null) {
			if (other.numGenResumen != null)
				return false;
		} else if (!numGenResumen.equals(other.numGenResumen))
			return false;
		if (numIndividuos == null) {
			if (other.numIndividuos != null)
				return false;
		} else if (!numIndividuos.equals(other.numIndividuos))
			return false;
		if (numMaxGeneraciones == null) {
			if (other.numMaxGeneraciones != null)
				return false;
		} else if (!numMaxGeneraciones.equals(other.numMaxGeneraciones))
			return false;
		if (probMutacion == null) {
			if (other.probMutacion != null)
				return false;
		} else if (!probMutacion.equals(other.probMutacion))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Parametros [numIndividuos=" + numIndividuos + ", numMaxGeneraciones=" + numMaxGeneraciones
				+ ", numGenResumen=" + numGenResumen + ", probMutacion=" + probMutacion + "]";
	}
	public Integer getNumIndividuos() {
		return numIndividuos;
	}
	public void setNumIndividuos(Integer numIndividuos) {
		this.numIndividuos = numIndividuos;
	}
	public Integer getNumMaxGeneraciones() {
		return numMaxGeneraciones;
	}
	public void setNumMaxGeneraciones(Integer numMaxGeneraciones) {
		this.numMaxGeneraciones = numMaxGeneraciones;
	}
	public Integer getNumGenResumen() {
		return numGenResumen;
	}
	public void setNumGenResumen(Integer numGenResumen) {
		this.numGenResumen = numGenResumen;
	}
	public Double getProbMutacion() {
		return probMutacion;
	}
	public void setProbMutacion(Double probMutacion) {
		this.probMutacion = probMutacion;
	}
}
