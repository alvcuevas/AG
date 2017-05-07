package com.ia.util;

import static com.ia.util.Constantes.MAX_GENERACIONES;
import static com.ia.util.Constantes.NUM_GEN_RESUMEN;
import static com.ia.util.Constantes.NUM_INDIVIDUOS;
import static com.ia.util.Constantes.PROB_MUTACION;
import static com.ia.util.Constantes.RUTA_ARCHIVO_CONFIGURACION;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.ia.model.Parametros;

/**
 * En esta clase están únicamente los métodos de apoyo, como generar letras o números
 * random, cargar la configuración, leer o escribir ficheros de texto, etc...
 */
public class Util {
	
	
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
	 * Un generador de randoms para usar desde varios métodos.
	 */
	static Random r = new Random();
	
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
		String archivo = leerArchivo(ruta==null||ruta.isEmpty()?RUTA_ARCHIVO_CONFIGURACION:ruta);
		String[] lineas = archivo.split("\n");
		
		try {
			
			for(String linea : lineas){
				
				if(linea.contains("\r"))
					linea = linea.replace("\r", "");
				
				String valor = linea.split("=")[1];
				
				if(linea.startsWith(NUM_INDIVIDUOS)){
					parametros.setNumIndividuos(Integer.valueOf(valor));
				}
				if(linea.startsWith(MAX_GENERACIONES)){
					parametros.setNumMaxGeneraciones(Integer.valueOf(valor));
				}
				if(linea.startsWith(NUM_GEN_RESUMEN)){
					parametros.setNumGenResumen(Integer.valueOf(valor));
				}
				if(linea.startsWith(PROB_MUTACION)){
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
		
		imprimirEnArchivo(rutaResumen, "(*)Poblacion aleatoria inicial: \n" + poblacion + "\n\n");
		System.out.printf("(*)Poblacion aleatoria inicial: \n");
		System.out.println(poblacion);
		return poblacion;
	}
	
	/**
	 * Genera un caracter random dentro del rango de varavteres válidos.
	 * 
	 * @return un caracter random dentro del rango de caracteres válidos.
	 */
	public static char obtenerCharRandom(){

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

		String resumen = "Generacion: " + generacion +"\n";
		resumen += "Porcentaje de NTar: " + dFormat.format(Util.porcentajeNTar(target.length(), numCoincidencias)) + "%" +"\n";
		resumen += "Número de NTar: " + Util.numeroNTar(target.length(), numCoincidencias) +"\n";
		resumen += "Porcentaje de acierto: " + dFormat.format(Util.porcentajeAcierto(target, poblacion)) + "%\n\n";
		imprimirEnArchivo(rutaResumen, resumen);
	}
	
	public static void imprimirEnArchivo(String archivo, String contenido){
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
}
