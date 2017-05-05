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
		
		long start = System.currentTimeMillis();    
		
		_programaConGeneraciones();
//		_programaSinGeneraciones();

		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println(poblacion);
		System.out.println(elapsedTime);
	}
	
	private static void _programaConGeneraciones(){
		for(int i=0; i<parametros.getNumMaxGeneraciones(); i++){
			numCoincidencias = Util.calculaCoincidencias(poblacion, target);
			listaFitness = Util.calculaFitness(poblacion, target, numCoincidencias, parametros);
			poblacion = Util.calculaFitnessNormalizado(poblacion, target, numCoincidencias, listaFitness, parametros);
		}
	}
	
	public static void _programaSinGeneraciones() {
		int i = 0;
		while(!hasEqual()){
			i++;
			numCoincidencias = Util.calculaCoincidencias(poblacion, target);
			listaFitness = Util.calculaFitness(poblacion, target, numCoincidencias, parametros);
			poblacion = Util.calculaFitnessNormalizado(poblacion, target, numCoincidencias, listaFitness, parametros);
		}
		System.out.println("Generaciones para generar: " + i);
	}
	
	static boolean hasEqual(){
		for(Integer numCoin : numCoincidencias){
			if(numCoin==target.length())
				return true;
		}
		return false;
	}
}
