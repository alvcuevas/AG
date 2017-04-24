package com.ia.util;

import static com.ia.util.Constantes.MAX_GENERACIONES;
import static com.ia.util.Constantes.NUM_GEN_MUESTREO;
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
	
//	public static void main(String[] args) {
//		obtenerParametros(null);
//		leeTarget();
//		generaPoblacion();
//		evaluaFitness();
//		codificaIndividuos();
//    }
	
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
				if(linea.startsWith(NUM_GEN_MUESTREO)){
					parametros.setNumGenMuestreo(Integer.valueOf(valor));
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
	
	
	// Codifica los individuos de la poblacion para obtener sus cromosomas equivalentes
	public static void codificaIndividuos(List<Character> target){

		System.out.printf("\n(*)Cromosomas de cada individuo: ");
		for(int i=0; i<target.size(); i++){
			int y = (int)target.get(i);
			System.out.printf(Integer.toBinaryString(y)+", ");
		}
			
	}
	
	
	// Evalua el fitness => n�mero de coincidencias de la poblacion con la frase introducida
	public static String evaluaFitness(List<String> poblacion, String target){
		
		List<Integer> numCoincidenciasI = new ArrayList<Integer>();
		List<Double> listaFitness = new ArrayList<Double>();
		List<Double> fitnessNormalizado = new ArrayList<Double>();
		Integer numeroMaximo = 0;
		Double maximoFitness = new Double(0);
		Integer indiceMejorFitness = 0;
		
		for(int i=0; i<poblacion.size(); i++){
			numCoincidenciasI.add(new Integer(0));
			for(int j=0; j<target.length(); j++){
			
				if(poblacion.get(i).charAt(j) == target.charAt(j)){
					numCoincidenciasI.set(i, numCoincidenciasI.get(i)+1);
				}
			}
			if(numeroMaximo==0 || numeroMaximo<numCoincidenciasI.get(i)) 
				numeroMaximo = numCoincidenciasI.get(i);
		}
		
		System.out.println(numCoincidenciasI);

		for(int i=0; i<poblacion.size(); i++){
			listaFitness.add(Math.pow(Math.E, (numCoincidenciasI.get(i)-target.length()))
					- Math.pow(Math.E, (-target.length())));
		}
		
		System.out.println(listaFitness);

		for(int i=0; i<poblacion.size(); i++){
			fitnessNormalizado.add(listaFitness.get(i) / 
					(Math.pow(Math.E, numeroMaximo-target.length() - Math.pow(Math.E, -target.length()))));
		}
		
		System.out.println(fitnessNormalizado);
		
		for(int i=0; i<poblacion.size(); i++){
			if(maximoFitness==new Double(0) || maximoFitness<fitnessNormalizado.get(i)){
				maximoFitness = fitnessNormalizado.get(i);
				indiceMejorFitness = i;
			}
		}
		
		System.out.println(indiceMejorFitness);
		
		String mejorSolucion = poblacion.get(indiceMejorFitness);
		String nuevaSolucion = new String();
		
		for(int i=0; i<target.length(); i++){
			if(target.charAt(i)!=mejorSolucion.charAt(i)){
				int c = _obtenerRandom();
				nuevaSolucion += (char) c;
			} else {
				nuevaSolucion += mejorSolucion.charAt(i);
			}
		}
		
		System.out.println(nuevaSolucion);
		
		int nuevaPosicion = _obtenerRandomMax(target.length());
		
		poblacion.set(nuevaPosicion, nuevaSolucion);
		
		return nuevaSolucion;
		
	}
	
}

