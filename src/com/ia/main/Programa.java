package com.ia.main;

import com.ia.util.Util;

public class Programa {
	
	// Para ir probando el programa
	public static void main(String[] args) {
		Util.obtenerParametros(null);
		Util.leeTarget();
		Util.generaPoblacion();
		Util.evaluaFitness();
		Util.codificaIndividuos();
	}

}
