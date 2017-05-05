package com.ia.main;

import java.text.DecimalFormat;
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
	public static void algoritmoGenetico(){
		
		numCoincidencias = Util.calculaCoincidencias(poblacion, target);
		listaFitness = UtilAlgoritmos.calculaFitness(poblacion, target, numCoincidencias, parametros);
		poblacion = UtilAlgoritmos.mutaPoblacion(poblacion, target, numCoincidencias, listaFitness, parametros);
	}
	
	/**
	 * Éste método utiliza el ngen del configuracion.cfg. 
	 */
	private static void _programaConGeneraciones(){
		for(int i=0; i<parametros.getNumMaxGeneraciones(); i++){
			algoritmoGenetico();
			if(i%parametros.getNumGenResumen()==0)
				Util.imprimirResumen(i, target, numCoincidencias, poblacion);
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
			algoritmoGenetico();
		}
		System.out.println("Generaciones para generar: " + i);
	}
}
