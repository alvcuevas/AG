package com.ia.main;

import java.util.ArrayList;
import java.util.List;

import com.ia.model.Parametros;
import com.ia.util.Util;

public class Programa {
	
	static Parametros parametros ;
	static List<String> poblacion = new ArrayList<String>();
	static String target ;
	static String solucionActual;
	
	// Para ir probando el programa
	public static void main(String[] args) {
		parametros = Util.obtenerParametros(null);
		target = Util.leeTarget();
		poblacion = Util.generaPoblacion(target.length(), target);
		for(int i=0; i<100; i++){
			solucionActual = Util.evaluaFitness(poblacion, target);
			if(solucionActual.equals(target)) break;
		}
//		Util.codificaIndividuos(target);
	}

}
