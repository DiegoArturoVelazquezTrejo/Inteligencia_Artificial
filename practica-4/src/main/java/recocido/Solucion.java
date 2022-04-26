package recocido; 

/**
 * Clase base para representar soluciones que pueden ser usadas
 * en el método de recocido simulado.
 * 
 * @author Benjamin Torres
 * @author Verónica E. Arriola
 * @version 0.1
 */
public abstract class Solucion {
	protected Double valor;    //calificación de la solución actual

	/**
	* Método constructor de una solución a un problema.
	*/
	public Solucion(){
		this.valor=0.0;
	}

	/**
	* Genera, a partir de una solución aproximada, una
	* nueva dentro de la vecindad actual de forma aleatoria.
	* @return una solución nueva generada al modificar la que llama
	* al método.
	*/
	public abstract Solucion siguienteSolucion();

	/**
	* Asigna una calificación (valor) a la solución que invoca el método
	* @return evaluación de la solución
	*/
	public abstract double evaluar();
	/**
	 * Método para obtener el valor de la evaluación de la solución 
	 */
	public Double getValor(){
		return this.valor; 
	}

	/**
	* Método requerido para imprimir una solución.
	* @return Representación de cadena para la solución.
	*/
	public abstract String toString();

}
