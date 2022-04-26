package laberintos;
import java.util.Stack;
import processing.core.PApplet;
import java.util.Random;

/**
 * Clase que crea un laberinto con Processing.
 * @author Sara
 * @author Baruch
 */
public class Laberinto extends PApplet {
    int alto = 20;            // Altura (en celdas) de la cuadricula.
    int ancho = 20;           // Anchura (en celdas) de la cuadricula.
    int celda = 40;           // Tamanio de cada celda cuadrada (en pixeles).
    ModeloLaberinto modelo;   // El objeto que representa el modelo del laberinto.

    @Override
    public void setup() {
        frameRate(80);
        background(50);
        modelo = new ModeloLaberinto(ancho, alto, celda);
    }
    @Override
    public void settings() {
        size(ancho * celda, (alto * celda));
    }
    
    /**
     * Pintar el mundo del modelo.
     */
    @Override
    public void draw() {
      for (int i = 0; i < alto; i++){
        for (int j = 0; j < ancho; j++){
                  if(modelo.mundo[i][j].estado == false)
                    fill(204, 204, 204);
                  else
                    fill(25, 25, 25);
                  stroke(25,25,25);
                  rect(j * modelo.tamanio, i * modelo.tamanio, modelo.tamanio, modelo.tamanio);
                  // En caso de que las paredes de las celdas ya no se encuentren activas, estás se
                  // pintarán del color del fondo.
                  if(!modelo.mundo[i][j].pared_1){
                      stroke(204, 204, 204);
                      line(j * modelo.tamanio, i * modelo.tamanio, ((j + 1) * modelo.tamanio), i * modelo.tamanio);                    
                  }
                  if(!modelo.mundo[i][j].pared_2){
                      stroke(204, 204, 204);
                      line((j * modelo.tamanio) + modelo.tamanio, i * modelo.tamanio, (j + 1) * modelo.tamanio, (((i + 1) * modelo.tamanio)));
                  }
                  if(!modelo.mundo[i][j].pared_3){
                      stroke(204, 204, 204);
                      line(j * modelo.tamanio, (i * modelo.tamanio) + modelo.tamanio, ((j + 1) * modelo.tamanio), ((i + 1) * modelo.tamanio));                    
                  }
                  if(!modelo.mundo[i][j].pared_4){
                      stroke(204, 204, 204);
                      line(j * modelo.tamanio, i * modelo.tamanio, j * modelo.tamanio, ((i + 1) * modelo.tamanio));               
                  }
          }
      }
      modelo.generaLaberinto();
    }

    /**
     * Clase que representa cada celda de la cuadricula.
     */
    class Celda{
        int celdaX; 
        int celdaY;
        boolean pared_1;
        boolean pared_2;
        boolean pared_3;
        boolean pared_4;
        boolean estado;
        
        /** Constructor de una celda.
          *@param celdaX Coordenada en x
          *@param celdaY Coordenada en y
          *@param estado Estado de la celda. true si no ha sido visitada, false en otro caso.
        */
        Celda(int celdaX, int celdaY, boolean estado){
          this.celdaX = celdaX;
          this.celdaY = celdaY;
          this.estado = estado;
          this.pared_1 = true; // Booleano que representa la pared de arriba
          this.pared_2 = true; // Booleano que representa la pared de la derecha
          this.pared_3 = true; // Booleano que representa la pared de abajo
          this.pared_4 = true; // Booleano que representa la pared de la izquierda
        }
    }  

    /**
     * Clase que modela el laberinto, es decir, crea el mundo del laberinto.
     */
    class ModeloLaberinto{
        int ancho, alto;  // Tamaño de celdas a lo largo y ancho de la cuadrícula.
        int tamanio;  // Tamaño en pixeles de cada celda.
        Celda[][] mundo;  // Mundo de celdas
        int direccion;
        Stack<Celda> pila;  // Pila que contendrá las celdas.  
        int pos_X; 
        int pos_Y; 
        int cantidad_casillas_visitadas; 
        Random rand; 
      /** Constructor del modelo
        @param ancho Cantidad de celdas a lo ancho en la cuadricula.
        @param ancho Cantidad de celdas a lo largo en la cuadricula.
        @param tamanio Tamaño (en pixeles) de cada celda cuadrada que compone la cuadricula.
      */
      ModeloLaberinto(int ancho, int alto, int tamanio){
        this.rand = new Random(); 
        this.cantidad_casillas_visitadas = 0; 
        this.ancho = ancho;
        this.alto = alto;
        this.tamanio = tamanio;
        mundo = new Celda[alto][ancho];
        // Inicialización de la pila de celdas 
        this.pila = new Stack<>();  
        // Inicialización aleatoria de la posición inicial 
        this.pos_X = rand.nextInt(alto); // Por simplicidad, vamos a obtener una casilla aleatoria que no colinde con los márgenes.  
        this.pos_Y = rand.nextInt(ancho);  // Por simplicidad, vamos a obtener una casilla aleatoria que no colinde con los márgenes.  
        for(int i = 0; i < alto; i++)
          for(int j = 0; j < ancho; j++)
            mundo[i][j] = new Celda(j,i, false);
        // A. Método para escoger una dirección aleatoria para moverse, esta casilla
        this.pila.add(this.mundo[pos_X][pos_Y]);
        this.mundo[pos_X][pos_Y].estado = true;
      }

    // Método para cambiar de celda 
    public void cambiaPosicion(int posx, int posy){
      this.pos_X = posx; 
      this.pos_Y = posy; 
      return; 
    }
    // Método para escoger una dirección aleatoria
    /**
     *     _| 0 |_
     *     3      1
     *     -| 2 |-
     * 
     */ 
    public int obtieneDireccion(int posx, int posy){
      int direccion = 0; 
      // Estamos en  la esquina superior izquierda  
      if(posx == 0 && posy == 0)
        direccion = this.rand.nextInt(2)+1; 
      
      // Estamos en la esquina superior derecha 
      else if(posx == ancho -1 && posy == 0)
        direccion = this.rand.nextInt(2) + 2;
      
      // Estamos en la esquina inferior izquierda 
      else if(posx == 0 && posy == alto - 1)
        direccion = this.rand.nextInt(2); 
      
      // Estamos en la esquina inferior derecha 
      else if(posy == alto -1 && posx == ancho -1)
        direccion = this.rand.nextInt(2); 
        if(direccion == 1)
          direccion = 3; 
      
      // Estamos en el marco de la izquierda 
      else if(posx == 0 )
        direccion = this.rand.nextInt(3); 
      
      // Estamos en el marco de la derecha 
      else if(posx == ancho -1){ 
        direccion = this.rand.nextInt(3); 
        if(direccion == 1)
          direccion = 3; 
      }
      // Estamos en el marco superior 
      else if(posy == 0)
        direccion = this.rand.nextInt(3) +1; // 0, 1, 2 => 1, 2, 3 
      
      // Estamos en el marco inferior 
      else if(posy == alto -1){  
        direccion = this.rand.nextInt(3); 
        if(direccion == 2)
          direccion = 3; 
      }
      // Estamos adentro del tablero 
      else{
        if(this.mundo[posy-1][posx].estado && this.mundo[posy+1][posx].estado && this.mundo[posy][posx-1].estado && this.mundo[posy][posx+1].estado)
          return -1; 
        direccion = this.rand.nextInt(4); 
      }
      // Aquí ya tenemos una dirección válida 
      return direccion; 
    }
    // Método para verificar que las coordenadas sean válidas 
    public boolean coordenadasValidas(int direccion, int pos_x, int pos_y){
      int posx = pos_x; 
      int posy = pos_y; 
      switch(direccion){
        case 0: 
          posy -= 1; 
          break; 
        case 1: 
          posx += 1; 
          break; 
        case 2: 
          posy += 1; 
          break; 
        case 3:
          posx -= 1; 
          break; 
      }
      return posy >= 0 && posy < alto && posx >= 0 && posx < ancho;  
    }
    // Método para eliminar la pared de la dirección aleatoria que se escogió 
    public void elimina_pared(int direccion, int posx, int posy){
      switch(direccion){
        case 0: 
          // Se elimina la pared de arriba
          this.mundo[posy][posx].pared_1 = false; 
          this.mundo[posy-1][posx].pared_3 = false;  
          break; 
        case 1: 
          // Se elimina la pared de la derecha 
          this.mundo[posy][posx].pared_2 = false;
          this.mundo[posy][posx+1].pared_4 = false; 
          break; 
        case 2: 
          // Se elimina la pared de abajo 
          this.mundo[posy][posx].pared_3 = false;
          this.mundo[posy+1][posx].pared_1 = false; 
          break; 
        case 3: 
          // Se elimina la pared de la izquierda
          this.mundo[posy][posx].pared_4 = false; 
          this.mundo[posy][posx -1].pared_2 = false; 
          break; 
      }
      return; 
    }
    // Método para verificar si hay vecinos disponibles. 
    // Idea de David Hernández Urióstegui, quien me sugirió como llevarlo a cabo. 
    boolean hayVecinosDisponibles(int x, int y){
        int[][] casillas = {{1, 0}, {0, -1},{-1, 0}, {0, 1}};
        for(int[] cas : casillas){
          int x_1 = x + cas[1];
          int y_1 = y + cas[0];
          if (x_1 >= 0 && y_1 < this.alto && y_1 >= 0 && x_1 < this.ancho && !this.mundo[y_1][x_1].estado)
              return true;
        }
        return false;
      }

    void generaLaberinto(){
      if (!this.pila.empty()){
        Celda actual = this.pila.peek();
        int x = actual.celdaX;
        int y = actual.celdaY;
        fill(200, 0, 0);
        rect(x * modelo.tamanio, y * modelo.tamanio, modelo.tamanio, modelo.tamanio);
        if (this.hayVecinosDisponibles(x, y)){ 
          boolean vecinoEncontrado = false;
          Celda vecino = null;
          int dir = this.obtieneDireccion(x, y);
          while(!vecinoEncontrado){
            switch(dir){
              case 0:
                if (y-1 >= 0 && !this.mundo[y-1][x].estado){
                  vecino = this.mundo[y-1][x];
                  vecinoEncontrado = true;
                }
                break; 
              case 1:
                if (x+1 < ancho && !this.mundo[y][x+1].estado){
                  vecino = this.mundo[y][x+1  ];
                  vecinoEncontrado = true;
                }
                break;
              case 2:
                if (y+1 <alto && !this.mundo[y+1][x].estado){
                  vecino = this.mundo[y+1][x];
                  vecinoEncontrado = true;
                }
                break;
              case 3:
                if (x-1 >= 0 && !this.mundo[y][x-1].estado){
                  vecino = this.mundo[y][x-1];
                  vecinoEncontrado = true;
                }
                break;
            }
            dir = this.obtieneDireccion(x, y);

          }
          this.elimina_pared(dir, x, y);
          this.mundo[vecino.celdaY][vecino.celdaX].estado = true;
          this.pila.add(this.mundo[vecino.celdaY][vecino.celdaX]);          
        }else
          this.pila.pop();
      }else{
        noLoop();
      }
    }
  }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         PApplet.main(new String[] { "laberintos.Laberinto" });
    } 
}