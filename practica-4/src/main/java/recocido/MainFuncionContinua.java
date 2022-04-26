package recocido; 
import java.util.*; 
/**
 * Clase para ejecutar un proceso de optimización usando recocido simulado.
 * @author Benjamin Torres Saavedra
 * @author Verónica E. Arriola
 * @version 0.1
 */
public class MainFuncionContinua{
	/**
	 * Recibe la dirección de un archivo .tsp y utiliza recocido simulado
	 * para encontrar una solución al problema del agente viajero en esa
	 * ciudad.
	 * El programa se podrá ejecutar como:
	 * java recocido.Main <archivo.tsp>
	 * @param args Nombre del archivo tsp.
	 */
	public static void main(String[] args){
		Solucion inicial; 
        // -5 <= x 
        //  y <= 5 
        Double infX = 10.0;
        Double infY = 10.0; 
        Random rand = new Random(); 
        int interacciones = 1000;
		float tempInicial = 10000; 
		float decaimiento = 0.0001f;
        // Generamos una soluciones iniciales que satisfagan la condición de dominio 
        Double[] solucion = {infX * rand.nextDouble()-5, infY*rand.nextDouble()-5}; 

		// TODO: Generar una solución inicial.
		
		inicial = new SolucionFuncionContinua(solucion, infX, infY); 
		RecocidoSimuladoAgenteViajero recocido = new RecocidoSimuladoAgenteViajero(inicial, tempInicial, decaimiento, interacciones);
		recocido.ejecutar(); 
	}
}	
