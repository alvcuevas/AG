package com.ia.util;

import static com.ia.util.Constantes.MAX_GENERACIONES;
import static com.ia.util.Constantes.NUM_GEN_RESUMEN;
import static com.ia.util.Constantes.NUM_INDIVIDUOS;
import static com.ia.util.Constantes.PROB_MUTACION;
import static com.ia.util.Constantes.RUTA_ARCHIVO_CONFIGURACION;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.ia.model.Parametros;

public class Util {
	
	static Random r = new Random();
	
	// Lee la frase inicial por consola y lo almacena en un array
	public static String leeTarget(){
		
		String target = new String();
		Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la frase: ");
 
        if(scanner.hasNextLine())
        	target = scanner.nextLine().toString();
        
        System.out.println("(*)Sentencia target: " + target.toString() + " (longitud " + target.length() + ")");

        for(int i=0; i<target.length(); i++)
        
        scanner.close();	
        
        return target;
	}
	
	// Lectura del archivo desde una ruta especifica
	public static String leerArchivo(String ruta){
		
		try {
			return new String(Files.readAllBytes(Paths.get(ruta)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.getMessage();
		}
		
		return null;
	}
	
	// Obtencion de los parametros del archivo configuracion para almacenarlos en la clase Parametros
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
	
	// Genera poblacion de individuos aleatorios y los almacena
	public static List<String> generaPoblacion(int numIndividuos, String target){
		
		List<String> poblacion = new ArrayList<String>();
		
		for(int i=0; i<numIndividuos; i++){
			
			poblacion.add(new String());
			
			for(int j=0; j<target.length(); j++){
				int c = _obtenerRandom();
				poblacion.set(i, poblacion.get(i) + (char)c);
			}
		}
		
		System.out.printf("(*)Poblacion aleatoria inicial: \n");
		for(int i=0; i<poblacion.size(); i++)
			System.out.printf(poblacion.get(i).toString()+", \n");	
	    
		return poblacion;
	}
	
	public static int _obtenerRandom(){
		int c = r.nextInt(27) + (byte)'a';
		if(c==(byte)'a'+26) c = 32;
		return c;
	}
	
	public static int _obtenerRandomMax(int max){
		int c = r.nextInt(max);
		return c;
	}
	
	// Cálculo del número total de coincidencias entre los individuos y el target introducido
	public static String calculaCoincidencias(List<String> poblacion, String target){
		
		List<Integer> numCoincidencias = new ArrayList<Integer>();
		Integer numeroMaximo = 0;
		
		for(int i=0; i<poblacion.size(); i++){
			numCoincidencias.add(new Integer(0));
			for(int j=0; j<target.length(); j++){
			
				if(poblacion.get(i).charAt(j) == target.charAt(j)){
					numCoincidencias.set(i, numCoincidencias.get(i)+1);
				}
			}
			if(numeroMaximo==0 || numeroMaximo<numCoincidencias.get(i)) 
				numeroMaximo = numCoincidencias.get(i);
		}
		
		System.out.println("(*)Num coincidencias/individuo: " + numCoincidencias);
		return calculaFitness(poblacion, target, numeroMaximo, numCoincidencias);
		
	}
	
	// Cálculo del fitness de los individuos
	public static String calculaFitness(List<String> poblacion, String target, Integer numeroMaximo, List<Integer> numCoincidencias){
		
		List<Double> listaFitness = new ArrayList<Double>();
		List<Double> fitnessNormalizado = new ArrayList<Double>();
		Double maximoFitness = new Double(0);
		Integer indiceMejorFitness = 0;
		
		// Cálculo del fitness de cada individuo
		for(int i=0; i<poblacion.size(); i++){
			listaFitness.add(Math.pow(Math.E, (numCoincidencias.get(i)-target.length()))
					- Math.pow(Math.E, (-target.length())));
		}
		
		System.out.println("(*)Valores Fi/individuo: " + listaFitness);
		
		// Cálculo del fitness normalizado de cada individuo
		for(int i=0; i<poblacion.size(); i++){
			fitnessNormalizado.add(listaFitness.get(i) / 
					(Math.pow(Math.E, numeroMaximo-target.length() - Math.pow(Math.E, -target.length()))));
		}
		
		System.out.println("(*)Valores Fi Normalizados: " + fitnessNormalizado);
		
		// Cálculo del mejor fitness del conjunto de soluciones
		for(int i=0; i<poblacion.size(); i++){
			if(maximoFitness==new Double(0) || maximoFitness<fitnessNormalizado.get(i)){
				maximoFitness = fitnessNormalizado.get(i);
				indiceMejorFitness = i;
			}
		}
		
		System.out.println("(*)Individuo con mejor Fi: " + indiceMejorFitness);
		return mutaIndividuo(poblacion, indiceMejorFitness, target);
		
	}
	
	// Proceso de mutacion sobre individuo seleccionado
	public static String mutaIndividuo(List<String> poblacion, Integer indiceMejorFitness, String target){
		
		String mejorIndividuo = poblacion.get(indiceMejorFitness);
		String nuevoIndividuo = new String();
		
		for(int i=0; i<target.length(); i++){
			if(target.charAt(i)!=mejorIndividuo.charAt(i)){
				int c = _obtenerRandom();
				nuevoIndividuo += (char) c;
			} else {
				nuevoIndividuo += mejorIndividuo.charAt(i);
			}
		}
		
		System.out.println("(*)Nueva solución tras la mutación: " + nuevoIndividuo);
		
		int nuevaPosicion = _obtenerRandomMax(target.length());
		
		poblacion.set(nuevaPosicion, nuevoIndividuo);
		
		return nuevoIndividuo;
		
	}
}
