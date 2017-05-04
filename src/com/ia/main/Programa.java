package com.ia.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ia.model.Parametros;
import com.ia.util.Util;

public class Programa {
	
	static Parametros parametros ;
	
	static List<String> poblacion = new ArrayList<String>();
	static List<Integer> numCoincidencias = new ArrayList<Integer>();
	
	static List<Double> listaFitness = new ArrayList<Double>();
	static List<Double> fitnessNormalizado = new ArrayList<Double>();
	
	static String target ;
	static String solucionActual;
	
	// Para ir probando el programa
	public static void main(String[] args) {
		parametros = Util.obtenerParametros(null);
		target = Util.leeTarget();
		poblacion = Util.generaPoblacion(parametros.getNumIndividuos(), target);
		int i = 0;
		
		long start = System.currentTimeMillis();    
		
		while(!hasEqual()){
			numCoincidencias = Util.calculaCoincidencias(poblacion, target);
			listaFitness = Util.calculaFitness(poblacion, target, numCoincidencias, parametros);
			poblacion = Util.calculaFitnessNormalizado(poblacion, target, numCoincidencias, listaFitness, parametros);
//			System.out.println(i + " -- " + poblacion);
		}
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println(poblacion);
		System.out.println(elapsedTime);
	}
	
	static boolean hasEqual(){
		for(Integer numCoin : numCoincidencias){
			if(numCoin==target.length())
				return true;
		}
		return false;
	}
}
