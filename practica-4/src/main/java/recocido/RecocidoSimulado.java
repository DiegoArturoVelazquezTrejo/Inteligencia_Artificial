package recocido; 
 import java.util.Random;

/**
 * Clase con los métodos necesarios para implementar el algoritmo
 * de recocido simulado junto con la solución a un problema particular.
 * 
 * @author Benjamin Torres
 * @author Verónica E. Arriola
 * @version 0.1
 */
public abstract class RecocidoSimulado{

	/** Es la calificación que otorga la heurística a la solución actual. */
	private double valor;

	/** Parámetros del recocido. */
	protected float temperatura;
	protected float decaimiento;
	protected int ejecuciones; 
	protected Random rand; 

	/** Solución actual. */
	protected Solucion sol;

	/**
	 * Inicializa los valores necesarios para realizar el
	 * recocido simulado durante un número determinado de iteraciones.
	 * @param inicial Instancia de la clase para el problema particular que
	 * 	  se quiere resolver.  Contine la propuesta de solución inicial.
	 * @param temperatura <code>float</code> con el valor actual .
	 * @param decaimiento <code>float</code> que será usado para hacer decaer el valor de temperatura.
	 */
	public RecocidoSimulado(Solucion inicial,float temperaturaInicial,float decaimiento, int ejecuciones){ //escoge los parametros necesarios para inicializar el algoritmo
		sol = inicial;
		temperatura = temperaturaInicial;
		this.decaimiento = decaimiento;
		this.ejecuciones = ejecuciones; 
		rand = new Random(); 
	}
	/**
	 * Método para obtener la evaluación del elemento actual de la clase 
	 * @return elemento.evalua 
	 */
	public double obtieneEvaluacion(){
		this.valor = this.sol.evaluar(); 
		return this.valor; 
	}

	/**
	 * Función que calcula una nueva temperatura en base a
	 * la anterior y el decaemiento usado.
	 * @return nueva temperatura
	 */
	public float nuevaTemperatura(){
		return temperatura - this.decaimiento * temperatura; 
	}

	/**
	 * Genera y devuelve la solución siguiente a partir de la solución
	 * actual. Dependiendo de su valor,
	 * si es mejor o peor que la que ya se tenía,
	 * y de la probabilidad actual de elegir una solución peor, puede devolver
	 * una solución nueva o quedarse con la que ya se tenía.
	 * @return Solución nueva
	 */
	public abstract Solucion seleccionarSiguienteSolucion(Solucion actual); 

	/**
	 * Ejecuta el algoritmo con los parámetros con los que fue inicializado y
	 * devuelve una solución.
	 * @param
	 * @return Solución al problema
	 */
	public Solucion ejecutar(){
		Double ev1 = sol.evaluar(); 
		System.out.print("La solución inicial: ");
		System.out.println(sol.toString());
		double u, delta; 
		int ejecuciones = 0; 
		Solucion solucion_nueva; 
		Solucion minima_global = sol; 
		while(temperatura > 1){
			solucion_nueva = this.seleccionarSiguienteSolucion(sol); 
			delta  =  sol.evaluar() - solucion_nueva.evaluar(); 
			if(delta > 0){
				sol = solucion_nueva; 
				minima_global = sol; 
			}
			else{
				// Vamos a generar una probabilidad y evaluarla para ver el estado de aceptación de la solución 
				u = this.rand.nextDouble(); 
				if(Math.exp(delta/temperatura) < u)
					sol = solucion_nueva; 
			}
			// Actualizamos la temperatura 
			temperatura = nuevaTemperatura(); 
			ejecuciones+=1; 
		}
		System.out.println("\n-----------------------------------------");
		System.out.println("Total de ejecuciones realizadas: "+String.valueOf(ejecuciones));
		System.out.println("\n-----------------------------------------");
		System.out.print("La solución encontrada: ");
		System.out.println(sol.toString());
		System.out.println("\n-----------------------------------------");
		System.out.print("Estado: ");
		if(sol.evaluar()<ev1)
			System.out.println("se logró minimizar el costo por "+String.valueOf(Math.abs(ev1 - sol.getValor()))); 
		else
			System.out.println("no se logró minimizar el costo");
		System.out.println("\n-----------------------------------------");
		System.out.println("Solución global mínima: "+minima_global.toString());
		return sol;
	}
}
