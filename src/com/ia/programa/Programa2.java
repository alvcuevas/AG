package com.ia.programa;

import java.util.ArrayList;
import java.util.List;

import com.ia.model.Parametros;
import com.ia.util.Util;
import com.ia.util.UtilAlgoritmos;

/**
 * ESTA CLASE NO ESTÁ ACTUALIZADA.
 * @author pab
 *
 */
public class Programa2 {
	
	/**
	 * Definición de variables. 
	 */
	private Parametros parametros;
	private String rutaResumen;
	private List<String> poblacion;
	private List<Integer> numCoincidencias;
	private List<Double> listaFitness;
	private String target;
	/**
	 * FIN definición de variables. 
	 */
	
	public Programa2(String target, Parametros parametros, String rutaResumen){
		
		this.parametros = Util.obtenerParametros(null);
		this.target = target;
		this.rutaResumen = rutaResumen;
		ejecutarPrograma();
	}
	
	// Para ir probando el programa
	public void ejecutarPrograma() {

		/*
		 * Inicializar variables.
		 */
		poblacion = new ArrayList<String>();
		numCoincidencias = new ArrayList<Integer>();
		listaFitness = new ArrayList<Double>();
//		poblacion = Util.generaPoblacion(parametros.getNumIndividuos(), target, rutaResumen);
		
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

//		Util.imprimirEnArchivo(rutaResumen, "Tiempo transcurrido: " + new Double(new Double(elapsedTime)/new Double(1000))+ "s\n\n");
//		Util.imprimirEnArchivo(rutaResumen, "(*)Poblacion final: \n" + poblacion.toString());
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
	public void algoritmoGenetico(){
		
		numCoincidencias = Util.calculaCoincidencias(poblacion, target);
		listaFitness = UtilAlgoritmos.calculaFitness(poblacion, target, numCoincidencias, parametros);
		poblacion = UtilAlgoritmos.mutaPoblacion(poblacion, target, numCoincidencias, listaFitness, parametros);
	}
	
	/**
	 * Éste método utiliza el ngen del configuracion.cfg. 
	 */
	private void _programaConGeneraciones(){
		for(int i=0; i<parametros.getNumMaxGeneraciones(); i++){
			algoritmoGenetico();
			if(i%parametros.getNumGenResumen()==0){}
//				Util.imprimirResumen(i, target, numCoincidencias, poblacion, rutaResumen);
		}
	}
	
	/**
	 * Éste método no utiliza el ngen del configuracion.cfg.
	 * 
	 * En lugar de esto, sigue mutando a la población hasta encontrar un cromosoma que sea
	 * igual que target.
	 *  
	 */
	public void _programaSinGeneraciones() {
		int i = 0;
//		while(!Util.hasEqual(numCoincidencias, target)){
//			i++;
//			algoritmoGenetico();
//		}
		System.out.println("Generaciones para generar: " + i);
	}
}
