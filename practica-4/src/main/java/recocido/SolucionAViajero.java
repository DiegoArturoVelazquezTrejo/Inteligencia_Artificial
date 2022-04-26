package recocido; 
import java.util.*;
/**
 * Clase base para representar soluciones que pueden ser usadas
 * en el método de recocido simulado.
 * 
 * @author Benjamin Torres
 * @author Verónica E. Arriola
 * @version 0.1
 */

import java.util.ArrayList;

public class SolucionAViajero extends Solucion{

	protected Double valor;    //calificación de la solución actual
	protected ArrayList<Integer>  solucion;
	protected ArrayList<Double[]> coordenadas = new ArrayList<Double[]>(); 
	protected int num_ciudades; 
	protected Random rand; 
	/**
	* Método constructor de una solución a un problema.
	*/
	public SolucionAViajero(ArrayList<Integer> solucion,ArrayList<Double[]> coordenadas ,int num_ciudades){
		this.solucion = solucion; 
		this.num_ciudades = num_ciudades; 
		this.coordenadas = coordenadas; 
		this.valor=0.0;
		this.rand = new Random(); 
	}
	/**
	 * Método para swipear dos elementos de la solución 
	 */
	public void swipea(){
		int index1 = rand.nextInt(num_ciudades); 
		int index2 = rand.nextInt(num_ciudades); 
		int id_ciudad1 = solucion.get(index1); 
		int id_ciudad2 = solucion.get(index2); 
		solucion.set(index1, id_ciudad2);
		solucion.set(index2, id_ciudad1);
	}

	/**
	* Genera, a partir de una solución aproximada, una
	* nueva dentro de la vecindad actual de forma aleatoria.
	* @return una solución nueva generada al modificar la que llama
	* al método.
	*/
	public  Solucion siguienteSolucion(){
		// Hay que pasarle el array list de la solución 
        SolucionAViajero s = new SolucionAViajero(solucion, coordenadas, num_ciudades); 
		s.swipea();
        // Mandamos a llamar el método de swipe con dos índices aleatorios 
		return  s; 
    }
	// Métrica para calcular la distancia entre dos ciudades 
	public double distancia(Double[] coordA, Double[] coordB){
		// El tamaño del arreglo es 2 
		return Math.pow(coordA[0] - coordB[0], 2) + Math.pow(coordA[1] - coordB[1], 2); 
	}
	/**
	 * Método para obtener el número de ciudades
	 */
	public int get_ciudades(){
		return this.num_ciudades; 
	}
	/**
	* Asigna una calificación (valor) a la solución que invoca el método
	* @return evaluación de la solución
	*/
	public double evaluar(){
		Double[] coordA, coordB; 
		double suma = 0; 
		for(int i = 0; i < num_ciudades-1; i++){
			coordA = coordenadas.get(solucion.get(i)-1);
			coordB = coordenadas.get(solucion.get(i+1)-1); 
			suma += distancia(coordA, coordB);
		} 
		this.valor = suma; 
        return suma;
    }

	/**
	* Método requerido para imprimir una solución.
	* @return Representación de cadena para la solución.
	*/
	public  String toString(){
		String s = "(";
		for(int i = 0 ; i < num_ciudades; i++)
			s+= String.valueOf(solucion.get(i))+"_"; 
        return s+")"; 
    }
	/**
	 * Método para verificar que dos soluciones sean distintas
	 */
	public boolean equals(SolucionAViajero sol){
		if(sol.get_ciudades() != this.num_ciudades)
			return false; 
		for(int i = 0 ; i < num_ciudades; i++)
			if(solucion.get(i) != sol.solucion.get(i))
				return false; 
		return true; 
	}

}
