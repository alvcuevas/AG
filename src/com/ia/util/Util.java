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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
        
//        System.out.println("(*)Sentencia target: " + target.toString() + " (longitud " + target.length() + ")");

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
		
//		System.out.printf("(*)Poblacion aleatoria inicial: \n");
//		System.out.println(poblacion);
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
	
	public static double _obtenerRandomMax(double max){
		double c = ThreadLocalRandom.current().nextDouble(0, max);
		return c;
	}
	
	// Cálculo del número total de coincidencias entre los individuos y el target introducido
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
		
//		System.out.println("(*)Num coincidencias/individuo: " + numCoincidencias);
		return numCoincidencias;
		
	}
	
	// Cálculo del fitness de los individuos
	public static List<Double> calculaFitness(List<String> poblacion, String target, List<Integer> numCoincidencias, 
			Parametros parametros){
		
		List<Double> listaFitness = new ArrayList<Double>();
		
		Double maximoFitness = new Double(0);
		
		// Cálculo del fitness de cada individuo
		for(int i=0; i<poblacion.size(); i++){
			listaFitness.add(Math.pow(Math.E, numCoincidencias.get(i)-target.length())
					- Math.pow(Math.E, (-target.length())));
		}
		
//		System.out.println("(*)Valores Fi/individuo: " + listaFitness);
		
		return listaFitness;
		
	}

	public static List<String> calculaFitnessNormalizado(List<String> poblacion, String target, List<Integer> numCoincidencias,
			List<Double> listaFitness, Parametros parametros) {
		
		List<Double> fitnessNormalizado = Arrays.asList(new Double[poblacion.size()]);
		
		Integer indice = _obtenerRandomMax(poblacion.size());
		Integer nuevoIndice = _obtenerRandomMax(poblacion.size());
		Integer primerIndice = indice;
		
		Integer maxCoincidencias = 0;
		
		for(int i=0; i<numCoincidencias.size();i++) 
			if(maxCoincidencias<numCoincidencias.get(i)) 
				maxCoincidencias = numCoincidencias.get(i);
		
		Boolean primera = true;
		
		do{
			if(!primera){
				indice = _actualizaIndice(indice, poblacion.size());
			}
			primera=false;

			fitnessNormalizado.set(indice, listaFitness.get(indice) / 
					(Math.pow(Math.E, (maxCoincidencias-target.length()))) - Math.pow(Math.E, (-target.length()))) ;
		
			double randomMonteCarlo = _obtenerRandomMax(1.0);

//			System.out.println("(*)Valor Fi: " + listaFitness.get(indice));
//			System.out.println("(*)Valor Fi Normalizado: " + fitnessNormalizado.get(indice));
//			System.out.println("(*)Valor random Monte Carlo: " + randomMonteCarlo);
			
			if(randomMonteCarlo<fitnessNormalizado.get(indice)){
				poblacion.set(nuevoIndice, mutaIndividuo(poblacion.get(indice), indice, parametros));
				numCoincidencias = Util.calculaCoincidencias(poblacion, target);
				listaFitness = Util.calculaFitness(poblacion, target, numCoincidencias, parametros);
			}
			
		}while(indice!=primerIndice);
		
		return poblacion;
	}
	
	private static Integer _actualizaIndice(Integer indice, Integer max){
		if(indice+1<max) return indice+1;
		else return 0;
	}
	
	
	// Proceso de mutacion sobre individuo seleccionado
	public static String mutaIndividuo(String individuo, Integer indice, Parametros parametros){
		
//		System.out.println("(*)Antes de mutar: " + individuo);
		
		String nuevoIndividuo = new String();
		
		for(int i=0; i<individuo.length(); i++){
			
			double randomMonteCarlo = _obtenerRandomMax(1.0);
			
			if(randomMonteCarlo<parametros.getProbMutacion()){
				nuevoIndividuo += individuo.charAt(i);
			} else {
				int c = _obtenerRandom();
				nuevoIndividuo += (char) c;
			}
		}
		
//		System.out.println("(*)Nueva solución tras la mutación: " + nuevoIndividuo);
		
		return nuevoIndividuo;
		
	}
}
