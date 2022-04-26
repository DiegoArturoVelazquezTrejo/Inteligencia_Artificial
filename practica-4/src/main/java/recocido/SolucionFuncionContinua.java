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

public class SolucionFuncionContinua extends Solucion{

	protected Double[] valorC;    //Coordenadas 
    protected Double valor; 
    protected Double infX; 
    protected Double infY; 
    protected Random rand = new Random(); 

	/**
	* Método constructor de una solución a un problema.
	*/
	public SolucionFuncionContinua(Double[] inicial, Double infX, Double infY){
		this.valorC= inicial;
        this.infX = infX; 
        this.infY = infY; 
	}
	/**
	* Genera, a partir de una solución aproximada, una
	* nueva dentro de la vecindad actual de forma aleatoria.
	* @return una solución nueva generada al modificar la que llama
	* al método.
	*/
	public  Solucion siguienteSolucion(){
		// Tenemos que mutar el valor real y pasarlo 
        Double nuevo_valor_x = valorC[0]; 
        Double nuevo_valor_y = valorC[1]; 
        // Vamos a perturbar los valores por un valor dado entre -5 y 5 
        nuevo_valor_x += 2*rand.nextDouble(); 
        nuevo_valor_y -= 2*rand.nextDouble(); 

        Double[] valor_n = {nuevo_valor_x, nuevo_valor_y}; 
        SolucionFuncionContinua s = new SolucionFuncionContinua(valor_n, infX, infY); 
		return  s; 
    }
	/**
	* Asigna una calificación (valor) a la solución que invoca el método
	* @return evaluación de la solución
	*/
	public double evaluar(){
        Double eval; 
        eval = -20*Math.exp(-0.2)*Math.sqrt(0.5*(valorC[0]*valorC[0]+valorC[1]*valorC[1]))-Math.exp(0.5*(Math.cos(2*3.1416*valorC[0] + Math.cos(2*3.1416*valorC[1]))))+Math.exp(1)+20; 
		// Implementación de la función objetivo 
        this.valor = eval; 
        return eval; 
    }

	/**
	* Método requerido para imprimir una solución.
	* @return Representación de cadena para la solución.
	*/
	public  String toString(){
		String s = "("+String.valueOf(valorC[0])+" , "+String.valueOf(valorC[1])+")"; 
        return s;  
    }
	/**
	 * Método para verificar que dos soluciones sean distintas
	 */
	public boolean equals(SolucionFuncionContinua sol){
		return sol.valorC[0] == valorC[0] && sol.valorC[1] == valorC[1] ; 
	}

}
