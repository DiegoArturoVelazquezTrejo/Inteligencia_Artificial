package recocido; 

public class RecodicoSimuladoFContinua extends RecocidoSimulado{
    /**
	 * Inicializa los valores necesarios para realizar el
	 * recocido simulado durante un número determinado de iteraciones.
	 * @param inicial Instancia de la clase para el problema particular que
	 * 	  se quiere resolver.  Contine la propuesta de solución inicial.
	 * @param temperatura <code>float</code> con el valor actual .
	 * @param decaimiento <code>float</code> que será usado para hacer decaer el valor de temperatura.
	 */
	public RecodicoSimuladoFContinua(Solucion inicial,float temperaturaInicial,float decaimiento, int ejecuciones){ //escoge los parametros necesarios para inicializar el algoritmo
		super(inicial, temperaturaInicial, decaimiento, ejecuciones); 
	}
    /**
	 * Genera y devuelve la solución siguiente a partir de la solución
	 * actual. Dependiendo de su valor,
	 * si es mejor o peor que la que ya se tenía,
	 * y de la probabilidad actual de elegir una solución peor, puede devolver
	 * una solución nueva o quedarse con la que ya se tenía.
	 * @return Solución nueva
	 */
	public Solucion seleccionarSiguienteSolucion(Solucion s){
        return s.siguienteSolucion(); 
    }
}
