/*
 * TODO: Completar el código faltante.
 */
package termitas;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

/**
 * @autor rodrigo
 * @author blackzafiro
 */

// Para ejecutar, desde la carpeta superior ejecuta los siguientes comandos:
// $ javac -cp .:lib/core.jar termitas/Termitas.java
// $ java -cp .:lib/core.jar termitas/Termitas

public class Termitas extends PApplet {

    PFont fuente;  // Fuente para mostrar texto en pantalla

    // Propiedades del modelo de termitas.
    int alto = 100;         // Altura (en celdas) de la cuadricula.
    int ancho = 150;        // Anchura (en celdas) de la cuadricula.
    int celda = 4;          // Tamanio de cada celda cuadrada (en pixeles).
    int termitas = 200;      // Cantidad de termitas dentro del modelo.
    float densidad = 0.45f;   // Proporcion de astilla en el modelo (con probabilidad de 0 a 1).
    ModeloTermitas modelo;  // El objeto que representa el modelo de termitas.

    /**
     * Configuración inicial de la applet.
     */
    @Override
    public void setup() {
        background(50);
        fuente = createFont("Arial", 12, true);
        modelo = new ModeloTermitas(ancho, alto, celda, termitas, densidad);

        // Preprocesamiento
        //for(int i = 0; i < 5000; i++)
        //modelo.evolucion2();
    }

    /**
     * Asigna el tamaño de la ventana.
     */
    @Override
    public void settings() {
        size(ancho * celda, (alto * celda) + 32);
    }

    /**
     * Pintar el mundo del modelo (la cuadricula y las astillas) en cada cuadro
     * de la animación.
     */
    @Override
    public void draw() {
        // Las astillas se representan por el valor true del estado de cada Celda.
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (modelo.mundo[i][j].estado) {
                    fill(255, 210, 0);
                } else {
                    fill(50);
                }
                rect(j * modelo.tamanio, i * modelo.tamanio, modelo.tamanio, modelo.tamanio);
            }
        }

        // Dibujar las termitas.
        // Cada termita puede ser de color verde, si no carga astilla o roja en caso contrario.
        for (Termita t : modelo.termitas) {
            if (t.cargando) {
                fill(255, 0, 0);
                rect(t.posX * modelo.tamanio, t.posY * modelo.tamanio, modelo.tamanio, modelo.tamanio);
            } else {
                fill(0, 255, 0);
                rect(t.posX * modelo.tamanio, t.posY * modelo.tamanio, modelo.tamanio, modelo.tamanio);
            }
        }

        // Pintar informacion del modelo en la parte inferior de la ventana.
        fill(50);
        rect(0, alto * celda, (ancho * celda), 32);
        fill(255);
        textFont(fuente, 10);
        text("Cuadricula: " + modelo.ancho + " x " + modelo.alto, 5, (alto * celda) + 12);
        text("Generacion " + modelo.generacion, 128, (alto * celda) + 12);
        text("Termitas: " + modelo.termitas.size(), 5, (alto * celda) + 24);
        text("Proporcion de astillas: " + densidad, 128, (alto * celda) + 24);

        // Actualizar el modelo a la siguiente generacion.
        //modelo.evolucion1();
        //modelo.evolucion2();
        modelo.evolucion3();
    }

    // --- Clase Celda ---
    /**
     * Representación de cada celda de la cuadrícula.
     */
    class Celda {

        int celdaX, celdaY;
        boolean estado;

        /**
         * Constructor de una celda
         *
         * @param celdaX Coordenada en x
         * @param celdaY Coordenada en y
         * @param estado True para activada (espacio con astilla), false en otro
         * caso.
         */
        Celda(int celdaX, int celdaY, boolean estado) {
            this.celdaX = celdaX;
            this.celdaY = celdaY;
            this.estado = estado;
        }
    }

    // --- Clase Termita ---
    /**
     * Representa cada una de las termitas del modelo.
     */
    class Termita {

        int posX, posY;  // Coordenadas de la posicion de la termita
        int direccion;   // Valor entre 0 y 7 para indicar dirección de movimiento
        boolean cargando;  // True si está cargando una astilla, false en caso contrario.

        /**
         * Constructor de una termita
         *
         * @param posX Indica su posicion en el eje X
         * @param posX Indica su posicion en el eje Y
         * @param direccion Indica la dirección en la que mira.
         *    -----------
         *   | 0 | 1 | 2 |
         *   |-----------|
         *   | 7 |   | 3 |
         *   |-----------|
         *   | 6 | 5 | 4 |
         *    -----------
         */
        Termita(int posX, int posY, int direccion) {
            this.posX = posX;
            this.posY = posY;
            this.direccion = direccion;
            this.cargando = false;
        }
    }

    // --- Clase ModeloTermitas ---
    /**
     * Representa el automata celular que genera autorganizacion en una colonia
     * de termitas.
     */
    class ModeloTermitas {

        int ancho, alto;  // Tamaño de celdas a lo largo y ancho de la cuadrícula.
        int tamanio;      // Tamaño en pixeles de cada celda.
        int generacion;   // Conteo de generaciones (cantidad de iteraciones) del modelo.
        Celda[][] mundo;  // Mundo de celdas donde habitan las astillas.
        ArrayList<Termita> termitas;  // Todas las termitas del modelo.
        Random rnd = new Random();    // Auxiliar para decisiones aleatorias.

        /**
         * Constructor del modelo
         *
         * @param ancho Cantidad de celdas a lo ancho en la cuadricula.
         * @param ancho Cantidad de celdas a lo largo en la cuadricula.
         * @param tamanio Tamaño (en pixeles) de cada celda cuadrada que compone
         * la cuadricula.
         * @param cantidad Numero de termitas dentro de la cuadricula.
         * @param densidad Probabilidad para que una celda (en el estado
         * inicial) contenga una astilla. El valor esta dado entre 0 y 1.
         */
        ModeloTermitas(int ancho, int alto, int tamanio, int cantidad, float densidad) {
            this.ancho = ancho;
            this.alto = alto;
            this.tamanio = tamanio;
            this.generacion = 0;
            //Inicializar mundo (usar densidad)
            mundo = new Celda[alto][ancho];
            int astillas = 0;
            boolean crea;
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    crea = rnd.nextFloat() < densidad ? true : false;
                    mundo[i][j] = new Celda(i, j, crea);
                    if (crea) {
                        astillas++;
                    }
                }
            }
            System.out.println(astillas); 
            //Inicializar termitas (usar cantidad)
            termitas = new ArrayList<Termita>();
            int x, y;
            if (cantidad >= (ancho * alto) - astillas) {
                throw new IllegalArgumentException("No caben tantas termitas");
            }
            for (int i = 0; i < cantidad; i++) {
                do {
                    x = rnd.nextInt(ancho);
                    y = rnd.nextInt(alto);
                } while (mundo[y][x].estado == true);
                termitas.add(new Termita(x, y, rnd.nextInt(8)));
            }
        }

        /**
         * Mueve una termita según la dirección dada. Considerando que las
         * fronteras son periódicas.
         *
         * @param t La termita a mover en el modelo.
         * @param dirección La dirección en la que se desea mover la termita
         * (con valor entre 0 y 7).
         */
        void moverTermita(Termita t, int direccion) {
            switch (direccion) {
                case 0:
                    t.posX = (t.posX - 1) % ancho;
                    if (t.posX < 0) {
                        t.posX += ancho;
                    }
                    t.posY = (t.posY - 1) % alto;
                    if (t.posY < 0) {
                        t.posY += alto;
                    }
                    t.direccion = direccion;
                    break;
                case 1:
                    t.posY = (t.posY - 1) % alto;
                    if (t.posY < 0) {
                        t.posY += alto;
                    }
                    t.direccion = direccion;
                    break;
                case 2:
                    t.posX = (t.posX + 1) % ancho;
                    if (t.posX < 0) {
                        t.posX += ancho;
                    }
                    t.posY = (t.posY - 1) % alto;
                    if (t.posY < 0) {
                        t.posY += alto;
                    }
                    t.direccion = direccion;
                    break;
                case 3:
                    t.posX = (t.posX + 1) % ancho;
                    if (t.posX < 0) {
                        t.posX += ancho;
                    }
                    t.direccion = direccion;
                    break;
                case 4:
                    t.posX = (t.posX + 1) % ancho;
                    if (t.posX < 0) {
                        t.posX += ancho;
                    }
                    t.posY = (t.posY + 1) % alto;
                    if (t.posY < 0) {
                        t.posY += alto;
                    }
                    t.direccion = direccion;
                    break;
                case 5:
                    t.posY = (t.posY + 1) % alto;
                    if (t.posY < 0) {
                        t.posY += alto;
                    }
                    t.direccion = direccion;
                    break;
                case 6:
                    t.posX = (t.posX - 1) % ancho;
                    if (t.posX < 0) {
                        t.posX += ancho;
                    }
                    t.posY = (t.posY + 1) % alto;
                    if (t.posY < 0) {
                        t.posY += alto;
                    }
                    t.direccion = direccion;
                    break;
                case 7:
                    t.posX = (t.posX - 1) % ancho;
                    if (t.posX < 0) {
                        t.posX += ancho;
                    }
                    t.direccion = direccion;
                    break;
            }
        }

        /**
         * Auxiliar que genera un número que corresponde a la dirección "al
         * frente", "izquierda" y "derecha", relativa a la dirección dada como
         * parámetro.
         *
         * @param direccion La dirección que se toma como base para determinar
         * los valores "al frente", "izquierda" y "derecha".
         * @return Entero que representa la dirección aleatoria escogida.
         */
        int direccionAleatoriaFrente(int direccion) {
            // Ejemplo: Si recibes de parametro el valor '3' significa que la
            // termita está mirando hacia la derecha por lo que puede caminar
            // aleatoriamente hacia los valores 2, 3 ó 4 (representando
            // izquierda, adelante y derecha).
            int dir_res = rnd.nextInt(8); 
            switch(direccion){
                case 0: 
                    // número entre {7 = 2, 0, 1}
                    dir_res = rnd.nextInt(3); 
                    if(dir_res == 2)
                        dir_res = 7; 
                    break; 
                case 1: 
                    // número entre {0, 1, 2}
                    dir_res = rnd.nextInt(3); 
                    break;
                case 2:
                    // número entre {1, 2, 3}
                    //   1 + [0,1,2] = [1, 2, 3]
                    dir_res = rnd.nextInt(3) + 1; 
                    break; 
                case 3:
                    // número entre {2, 3, 4}
                    // 2 + [0,1,2] = [2,3,4]
                    dir_res = rnd.nextInt(3) + 2; 
                    break;  
                case 4: 
                    // número entre {3, 4, 5}
                    // 3 + [0,1,2] = [3, 4, 5]
                    dir_res = rnd.nextInt(3) + 3; 
                    break; 
                case 5: 
                    // número entre {4, 5, 6}
                    // 4 + [0,1,2] = [4,5,6]
                    dir_res = rnd.nextInt(3) + 4; 
                    break; 
                case 6: 
                    // número entre {5,6,7}
                    // 5 + [0,1,2] = [5, 6, 7]
                    dir_res = rnd.nextInt(3) + 5; 
                    break; 
                case 7: 
                    // número entre {6,7,8 = 0}
                    // 6 + [0,1,2] = [6, 7, 8]
                    dir_res = rnd.nextInt(3) + 6;
                    if(dir_res == 8)
                        dir_res = 0;  
                    break; 
            }
            return dir_res; 
        }

        /**
         * Determina si la casilla en la dirección en la que se mueve la termita
         * contiene una astilla.
         *
         * @param t La termita cuya posición se utiliza para ubicar la celda de
         * la cuadrícula.
         * @param dir La dirección en la que deseamos observar si hay una
         * astilla (con valor entre 0 y 7).
         * @return True si hay una astilla en la dirección de la termita, falso
         * en otro caso.
         */
        boolean hayAstilla(Termita t, int direccion) {
            // Hint: El parametro direccion solo puede ser un valor entre 0-7.
            // Hint: mundo[t.posY][t.posX].estado Nos indica si hay una astilla en la misma posicion que la termita.
            int x = t.posX; 
            int y = t.posY; 
            switch (direccion) {
                case 0:
                    x = (x - 1) % ancho;
                    if (x < 0) {
                        x += ancho;
                    }
                    y = (y - 1) % alto;
                    if (y < 0) {
                        y += alto;
                    }
                    break;
                case 1:
                    y = (y - 1) % alto;
                    if (y < 0) {
                        y += alto;
                    }
                    break;
                case 2:
                    x = (x + 1) % ancho;
                    if (x < 0) {
                        x += ancho;
                    }
                    y = (y - 1) % alto;
                    if (y < 0) {
                        y += alto;
                    }
                    break;
                case 3:
                    x = (x + 1) % ancho;
                    if (x < 0) {
                        x += ancho;
                    }
                    break;
                case 4:
                    x = (x + 1) % ancho;
                    if (x < 0) {
                        x += ancho;
                    }
                    y = (y + 1) % alto;
                    if (y < 0) {
                        y+= alto;
                    }
                    break;
                case 5:
                    y = (y + 1) % alto;
                    if (y < 0) {
                        y += alto;
                    }
                    break;
                case 6:
                    x = (x - 1) % ancho;
                    if (x < 0) {
                        x += ancho;
                    }
                    y = (y + 1) % alto;
                    if (y < 0) {
                        y += alto;
                    }
                    break;
                case 7:
                    x = (x - 1) % ancho;
                    if (x < 0) {
                        x += ancho;
                    }
                    break;
            }
            return mundo[y][x].estado; 
        }

        /**
         * Simula el comportamiento de soltar una astilla y mover a la hormiga
         * en la dirección dada.
         *
         * @param t La termita que suelta la astilla y se mueve en la
         * cuadricula.
         * @param dir La dirección en la que se desea mover la termita. La
         * direccion real en la que se mueve tras dejar la astilla es la
         * direccion contraria a la dirección del parametro. Esto para intentar
         * mejorar el comportamiento de las termitas para que logren
         * autorganizacion mas rapido.
         */
        void dejarAstilla(Termita t, int dir) {
            // ##### IMPLEMENTACION #####
            // Hint: Indicar en el mundo que hay una astilla, indicar a la termita que 
            //       está cargando una astilla y mover a la termita
            //       en la dirección opuesta a la que está mirando (variable 'dir')
            mundo[t.posY][t.posX].estado = true; 
            t.cargando = false; 
            
            int opuesta = rnd.nextInt(8); 
            switch(dir){
                case 0: 
                    opuesta = 4; 
                    break; 
                case 1: 
                    opuesta = 5; 
                    break; 
                case 2: 
                    opuesta = 6; 
                    break; 
                case 3: 
                    opuesta = 7; 
                    break; 
                case 4: 
                    opuesta = 0; 
                    break; 
                case 5: 
                    opuesta = 1; 
                    break; 
                case 6: 
                    opuesta = 2; 
                    break; 
                case 7: 
                    opuesta = 3; 
                    break; 
            }
            this.moverTermita(t, opuesta);
        }

        /**
         * Simula el comportamiento de soltar una astilla y mover a la hormiga
         * aleatoriamente.
         *
         * @param t La termita que suelta la astilla y se mueve en la
         * cuadricula.
         */
        void dejarAstilla(Termita t) {
            // ##### IMPLEMENTACION ######
            mundo[t.posY][t.posX].estado = true; 
            t.cargando = false; 
            int dir = direccionAleatoriaFrente(rnd.nextInt(8)); 
            this.moverTermita(t, dir);
            // Hint: Marcar casilla para indicar la astilla, indicar que la termita carga una astilla y moverTermita aleatoriamente.
        }

        /**
         * Variacion del comportamiento cuando una termita suelta una astilla.
         * En este caso la termita suelta la astilla y es colocada en una celda
         * aleatoria de la cuadricula que no tiene astilla ("salta" a una celda
         * vacia).
         *
         * @param t La termita que va a soltar astilla en el modelo.
         */
        void dejarAstillaConSalto(Termita t) {
            // ##### IMPLEMENTACION #####
            // Hint: MArcar casilla con astilla, indicar que la termina ya no carga una astilla y asignar una nueva posicion a la termita.
            mundo[t.posY][t.posX].estado = true; 
            t.cargando = false; 
            int x = t.posX; 
            int y = t.posY; 
            // Vamos a buscar una casilla que no contenga una astilla 
            int dir = rnd.nextInt(8); 
            while(mundo[y][x].estado){
                x = rnd.nextInt(ancho); 
                y = rnd.nextInt(alto);  
            }
            t.posX = x; 
            t.posY = y; 
            t.direccion = dir; 
        }

        /**
         * Simula el proceso en el que una termita recoge una astilla.
         *
         * @param t La termita que recoge la astilla.
         * @param La dirección en la que se movera. Recoge la astilla despues de
         * moverse en la dirección indicada.
         */
        void tomarAstilla(Termita t, int dir) {
            // ##### IMPLEMENTACION #####
            // Hint: Mover a la termita, quitar la astilla del mundo e indicar que la termita carga la astilla.
            this.moverTermita(t, dir);
            mundo[t.posY][t.posX].estado = false; 
            t.cargando = true; 
        }

        /**
         * Reglas de evolucion mas simples: Caminata aleatoria en las 8
         * direcciones de la vecindad de Moore. Al dejar una astilla continua
         * moviendose aleatoriamente.
         */
        void evolucion1() {
            for (Termita t : termitas) {
                int dir = rnd.nextInt(8);
                if (this.hayAstilla(t, dir)) {
                    if (t.cargando) {
                        this.dejarAstilla(t);
                    } else {
                        this.tomarAstilla(t, dir);
                    }
                } else {
                    this.moverTermita(t, dir);
                }
            }

            generacion += 1;
        }

        /**
         * Reglas de evolucion mejoradas: Con caminata hacia el frente, es
         * decir, aleatoriamente solo se considera moverse al frente, a la
         * izquierda o la derecha. Al soltar una astilla, la termita se da la
         * vuelta y continua moviendose al frente (como se acaba de describir).
         */
        void evolucion2() {
            for (Termita t : termitas) {
                int dir = direccionAleatoriaFrente(t.direccion);
                if (this.hayAstilla(t, dir)) {
                    if (t.cargando) {
                        this.dejarAstilla(t, dir);
                    } else {
                        this.tomarAstilla(t, dir);
                    }
                } else {
                    this.moverTermita(t, dir);
                }
            }

            generacion += 1;
        }

        /**
         * Reglas de evolución mejoradas y alteradas: Con caminata hacia el
         * frente, es decir, aleatoriamente sólo se considera moverse al frente,
         * a la izquierda o la derecha. Al soltar una astilla, la termita
         * "salta" a una celda desocupada, es decir, aleatoriamente se coloca a
         * la termita en una celda sin astilla.
         */
        void evolucion3() {
            for (Termita t : termitas) {
                int dir = direccionAleatoriaFrente(t.direccion);
                if (this.hayAstilla(t, dir)) {
                    if (t.cargando) {
                        this.dejarAstillaConSalto(t);
                    } else {
                        this.tomarAstilla(t, dir);
                    }
                } else {
                    this.moverTermita(t, dir);
                }
            }

            generacion += 1;
        }

    }

    /** Lanza el hilo donde se ejecutará el applet. */
    static public void main(String args[]) {
        PApplet.main(new String[]{"termitas.Termitas"});
    }
}
