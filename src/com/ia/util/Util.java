package com.ia.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.ia.util.Constantes.RUTA_ARCHIVO_CONFIGURACION;
import static com.ia.util.Constantes.MAX_GENERACIONES;
import static com.ia.util.Constantes.NUM_GEN_MUESTREO;
import static com.ia.util.Constantes.NUM_GEN_RESUMEN;
import static com.ia.util.Constantes.NUM_INDIVIDUOS;
import static com.ia.util.Constantes.PROB_MUTACION;


import com.ia.model.Parametros;

public class Util {
	
	static Parametros parametros = new Parametros();
	static Random r = new Random();
	static List<String> frase = new ArrayList<String>();
	static List<Character> poblacion = new ArrayList<Character>();
	
	public static void main(String[] args) {
		obtenerParametros(null);
		leeTarget();
		generaPoblacion();
		evaluaFitness();
		codificaIndividuos();
    }
	
	// Lee la frase inicial por consola y lo almacena en un array
	public static void leeTarget(){
		
		Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la frase: ");
 
        if(scanner.hasNextLine())
        	frase.add(scanner.nextLine().toString());
        
        for(int i=0; i<frase.size(); i++)
        	System.out.println("(*)Sentencia target: " + frase.get(i).toString() + " (longitud " + frase.get(i).length() + ")");
        
        scanner.close();		
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
		
		String archivo = leerArchivo(ruta==null||ruta.isEmpty()?RUTA_ARCHIVO_CONFIGURACION:ruta);
		String[] lineas = archivo.split("\n");
		
		try {
			
			for(String linea : lineas){
				
				if(linea.contains("\r"))
					linea = linea.replace("\r", "");
				
				String valor = linea.split("=")[1];
				
				if(linea.startsWith(NUM_INDIVIDUOS)){
					parametros.setNumIndividuos(Integer.valueOf(valor));
					//System.out.println(parametros.getNumIndividuos());
				}
				if(linea.startsWith(MAX_GENERACIONES)){
					parametros.setNumMaxGeneraciones(Integer.valueOf(valor));
					//System.out.println(parametros.getNumMaxGeneraciones());
				}
				if(linea.startsWith(NUM_GEN_RESUMEN)){
					parametros.setNumGenResumen(Integer.valueOf(valor));
					//System.out.println(parametros.getNumGenResumen());
				}
				if(linea.startsWith(NUM_GEN_MUESTREO)){
					parametros.setNumGenMuestreo(Integer.valueOf(valor));
					//System.out.println(parametros.getNumGenMuestreo());
				}
				if(linea.startsWith(PROB_MUTACION)){
					parametros.setProbMutacion(Double.valueOf(valor));
					//System.out.println(parametros.getProbMutacion());
				}
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		
		return parametros;
	}
	
	// Genera poblacion de individuos aleatorios y los almacena
	public static void generaPoblacion(){
		
		int individuos = parametros.getNumIndividuos();
		
		for(int i=0; i<individuos; i++){
			int c = r.nextInt(26) + (byte)'a';
			poblacion.add((char)c);
		}
		
		System.out.printf("(*)Poblacion aleatoria inicial: ");
		for(int i=0; i<poblacion.size(); i++)
			System.out.printf(poblacion.get(i).toString()+", ");	
	    
	}
	
	
	// Codifica los individuos de la poblacion para obtener sus cromosomas equivalentes
	public static void codificaIndividuos(){

		System.out.printf("\n(*)Cromosomas de cada individuo: ");
		for(int i=0; i<poblacion.size(); i++){
			int y = (int)poblacion.get(i);
			System.out.printf(Integer.toBinaryString(y)+", ");
		}
			
	}
	
	
	// Evalua el fitness => número de coincidencias de la poblacion con la frase introducida
	public static void evaluaFitness(){
		
		List<Character> coincidencias = new ArrayList<Character>();
		char[] letrasFrase = frase.toString().toCharArray();
		
		for(int i=0; i<letrasFrase.length; i++){
			for(int j=0; j<poblacion.size(); j++){
				if(letrasFrase[i] == poblacion.get(j).charValue()){
					coincidencias.add(poblacion.get(j).charValue());
				}
			}
		}
		
		System.out.println("\n(*)Coincidencias target/individuos: " + coincidencias.size());
		
		System.out.printf("(*)Elementos coincidentes: ");
		for(int i=0; i<coincidencias.size(); i++)
			System.out.printf(coincidencias.get(i).toString() + ", ");

		
	}
	

	
	
}

