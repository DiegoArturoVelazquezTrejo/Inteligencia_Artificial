package recocido; 
import java.io.File; 
import java.util.*; 
/**
 * Clase para ejecutar un proceso de optimización usando recocido simulado.
 * @author Benjamin Torres Saavedra
 * @author Verónica E. Arriola
 * @version 0.1
 */
public class Main{
	/**
	 * Recibe la dirección de un archivo .tsp y utiliza recocido simulado
	 * para encontrar una solución al problema del agente viajero en esa
	 * ciudad.
	 * El programa se podrá ejecutar como:
	 * java recocido.Main <archivo.tsp>
	 * @param args Nombre del archivo tsp.
	 */
	public static void main(String[] args){
		int interacciones = 1000;
		Solucion inicial; 
		float tempInicial = 10000; 
		float decaimiento = 0.0001f;
		// Definimos las estructuras de datos 
		ArrayList<Integer> solucion = new ArrayList<Integer>();  
		ArrayList<Double[]> coordenadas = new ArrayList<Double[]>();  
		
		// TODO: Generar una solución inicial.
		File arch = new File("./src/main/resources/dj38.tsp");
		DatosPAV datos = new DatosPAV(arch); 
		int num_ciudades = datos.numCiudades();
		for(int i = 0; i < num_ciudades; i++){
			coordenadas.add(datos.coordenadas(datos.codigo(i))); 
			solucion.add(datos.codigo(i));
		}
		Collections.shuffle(solucion); 
		inicial = new SolucionAViajero(solucion, coordenadas, num_ciudades); 
		RecocidoSimuladoAgenteViajero recocido = new RecocidoSimuladoAgenteViajero(inicial, tempInicial, decaimiento, interacciones);
		recocido.ejecutar(); 
	}
}	
