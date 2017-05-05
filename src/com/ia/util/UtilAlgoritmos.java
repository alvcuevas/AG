package com.ia.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ia.model.Parametros;

/**
 * En esta clase están los métodos relacionados únicamente con el algoritmo genético.
 */
public class UtilAlgoritmos {
	
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
		
//		System.out.println("(*)Antes de mutar: " + individuo);
		
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
		
//		System.out.println("(*)Nueva solución tras la mutación: " + nuevoIndividuo);
		
		return nuevoIndividuo;
		
	}
}
