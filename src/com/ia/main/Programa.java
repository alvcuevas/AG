package com.ia.main;

import java.util.ArrayList;
import java.util.List;

import com.ia.model.Parametros;
import com.ia.util.Util;
import com.ia.util.UtilAlgoritmos;

public class Programa {
	
	/**
	 * Definición de variables. 
	 */
	private static Parametros parametros;
	private static List<String> poblacion;
	private static List<Integer> numCoincidencias;
	private static List<Double> listaFitness;
	private static String target;
	/**
	 * FIN definición de variables. 
	 */
	
	
	// Para ir probando el programa
	public static void main(String[] args) {

		/*
		 * Inicializar variables.
		 */
		poblacion = new ArrayList<String>();
		numCoincidencias = new ArrayList<Integer>();
		listaFitness = new ArrayList<Double>();
		parametros = Util.obtenerParametros(null);
		target = Util.leeTarget();
		poblacion = Util.generaPoblacion(parametros.getNumIndividuos(), target);
		
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
		System.out.println(poblacion);
		System.out.println(elapsedTime);
	}
	
	/**
	 * Éste método utiliza el ngen del configuracion.cfg. 
	 */
	private static void _programaConGeneraciones(){
		for(int i=0; i<parametros.getNumMaxGeneraciones(); i++){
			UtilAlgoritmos.algoritmoGenetico(target, poblacion, numCoincidencias,
					parametros, listaFitness);
		}
	}
	
	/**
	 * Éste método no utiliza el ngen del configuracion.cfg.
	 * 
	 * En lugar de esto, sigue mutando a la población hasta encontrar un cromosoma que sea
	 * igual que target.
	 *  
	 */
	public static void _programaSinGeneraciones() {
		int i = 0;
		while(!Util.hasEqual(numCoincidencias, target)){
			i++;
			UtilAlgoritmos.algoritmoGenetico(target, poblacion, numCoincidencias, 
					parametros, listaFitness);
		}
		System.out.println("Generaciones para generar: " + i);
	}
}
