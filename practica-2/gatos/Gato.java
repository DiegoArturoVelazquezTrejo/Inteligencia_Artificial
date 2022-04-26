/*
 * No redistribuir.
 */
package gatos;

import java.util.LinkedList;

import javax.print.attribute.standard.JobHoldUntil;

/**
 * Clase para representar un estado del juego del gato. Cada estado sabe cómo
 * generar a sus sucesores.
 *
 * @author Vero
 */
public class Gato {

    public static final int MARCA1 = 1;             // Número usado en el tablero del gato para marcar al primer jugador.
    public static final int MARCA2 = 4;             // Se usan int en lugar de short porque coincide con el tamaÃ±o de la palabra, el código se ejecuta ligeramente más rápido.

    int[][] tablero = new int[3][3];     // Tablero del juego
    Gato padre;                          // Quién generó este estado.
    LinkedList<Gato> sucesores;          // Posibles jugadas desde este estado.
    boolean jugador1 = false;            // Jugador que tiró en este tablero.
    boolean hayGanador = false;          // Indica si la última tirada produjo un ganador.
    int tiradas = 0;                     // Número de casillas ocupadas.

    /**
     * Constructor del estado inicial.
     */
    Gato() {}

    /**
     * Constructor que copia el tablero de otro gato y el número de tiradas
     */
    Gato(int[][] g, int t, boolean jugador) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = g[i][j];
            }
        }
        tiradas = t;
        jugador1 = jugador; 
    }

    /**
     * Indica si este estado tiene sucesores expandidos.
     */
    int getNumHijos() {
        if (sucesores != null) {
            return sucesores.size();
        } else {
            return 0;
        }
    }

    /* Función auxiliar.
     * Dada la última posición en la que se tiró y la marca del jugador
     * calcula si esta jugada produjo un ganador y actualiza el atributo correspondiente.
     * 
     * Esta función debe ser lo más eficiente posible para que la generación del árbol no sea demasiado lenta.
     */
    private void hayGanador(int ren, int col, int marca) {
        // Horizontal
        if (tablero[ren][(col + 1) % 3] == marca && tablero[ren][(col + 2) % 3] == marca) {
            hayGanador = true;
            return;
        }
        // Vertical
        if (tablero[(ren + 1) % 3][col] == marca && tablero[(ren + 2) % 3][col] == marca) {
            hayGanador = true;
            return;
        }
        // Diagonal
        if ((ren != 1 && col == 1) || (ren == 1 && col != 1)) {
			hayGanador = false;  // No debiera ser necesaria.
            return; // No pueden hacer diagonal
        }      // Centro y esquinas
        if (col == 1 && ren == 1) {
            // Diagonal \
            if (tablero[0][0] == marca && tablero[2][2] == marca) {
                hayGanador = true;
                return;
            }
            if (tablero[2][0] == marca && tablero[0][2] == marca) {
                hayGanador = true;
                return;
            }
        } else if (ren == col) {
            // Diagonal \
            if (tablero[(ren + 1) % 3][(col + 1) % 3] == marca && tablero[(ren + 2) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        } else {
            // Diagonal /
            if (tablero[(ren + 2) % 3][(col + 1) % 3] == marca && tablero[(ren + 1) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        }
    }

    /* Función auxiliar.
     * Coloca la marca del jugador en turno para este estado en las coordenadas indicadas.
     * Asume que la casilla está libre.
     * Coloca la marca correspondiente, verifica y asigna la variable si hay un ganador.
     */
    private void tiraEn(int ren, int col) {
        tiradas++;
        int marca = (jugador1) ? MARCA1 : MARCA2;
        tablero[ren][col] = marca;
        hayGanador(ren, col, marca);
    }

    /**
     * ------- *** ------- *** -------
     * Este es el método que se deja como práctica.
     * ------- *** ------- *** -------
     * Crea la lista sucesores y 
     * agrega a todos los estados que sujen de tiradas válidas. Se consideran
     * tiradas válidas a aquellas en una casilla libre. Además, se optimiza el
     * proceso no agregando estados con jugadas simétricas. Los estados nuevos
     * tendrán una tirada más y el jugador en turno será el jugador
     * contrario.
     */
    LinkedList<Gato> generaSucesores() {
        if (hayGanador || tiradas == 9) {
            return null; // Es un estado meta.
        }
        sucesores = new LinkedList<>();
        // TODO: Tu código aquí.
        /**
         * Tenemos que colocar por cada casilla, una pieza y agregarlo a la lista. Una vez
         * que ya tengamos elementos en la lista, tenemos que ir agregando aquellos movimientos que no tengan ninguna 
         * simetría de las que ya se definieron al inicio de la práctica. 
         */
        boolean esta; 
        for(int i = 0 ; i < 3; i++){
            for(int j = 0; j < 3; j++){
                esta = false; 
                if( (tablero[i][j] != MARCA1) && (tablero[i][j] != MARCA2)){
                    Gato g = new Gato(tablero, tiradas, !jugador1); 
                    g.tiraEn(i, j);
                    for(Gato sucesor : sucesores){
                        if(g.equals(sucesor))
                            esta = true; 
                    }
                    if(esta == false)
                        sucesores.add(g);
                
                }
            }
        }
        return sucesores;
    }

    // ------- *** ------- *** -------
    // Serie de funciones que revisan la equivalencia de estados considerando las simetrías de un cuadrado.
    // ------- *** ------- *** -------
    // http://en.wikipedia.org/wiki/Examples_of_groups#The_symmetry_group_of_a_square_-_dihedral_group_of_order_8
    // ba es reflexion sobre / y ba3 reflexion sobre \.
    
    /**
     * Revisa si ambos gatos son exactamente el mismo.
     */
    boolean esIgual(Gato otro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] != otro.tablero[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Revisa si dos matrices son exactamente las mismas.
     */
    boolean mesIgual(int[][] matrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] != matrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Función que cambia elementos en una matrix 
     */
    void swap(int[][] matriz, int i, int j, int n, int m){
        int a = matriz[i][j]; 
        int b = matriz[n][m]; 
        matriz[i][j] = b; 
        matriz[n][m] = a; 
    }

    /**
     * Al reflejar el gato sobre la diagonal \ son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonalInvertida(Gato otro) {
        
        return esSimetrico180(otro);
    }

    /**
     * Al reflejar el gato sobre la diagonal / son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonal(Gato otro) {
        // Vamos a obtener la transpuesta de la matriz 
        int[][] matrix_aux = new int[3][3]; 
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                matrix_aux[i][j] = otro.tablero[j][i]; 
            }
        }
        return mesIgual(matrix_aux);
    }

    /**
     * Al reflejar el otro gato sobre la vertical son iguales
     */
    boolean esSimetricoVerticalmente(Gato otro) {
        int[][] matrix_aux = new int[3][3];  
        swap(matrix_aux, 0, 0, 0, 2);
        swap(matrix_aux, 1, 0, 1, 2);
        swap(matrix_aux, 2, 0, 2, 2);
        return mesIgual(matrix_aux);
    }

    /**
     * Al reflejar el otro gato sobre la horizontal son iguales
     */
    boolean esSimetricoHorizontalmente(Gato otro) {
        int[][] matrix_aux = otro.tablero; 
        swap(matrix_aux, 0, 0, 2, 0);
        swap(matrix_aux, 0, 1, 2, 1);
        swap(matrix_aux, 0, 2, 2, 2);
        return mesIgual(matrix_aux);
    }

    /**
     * Rota el otro tablero 90Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico90(Gato otro) { 
        int aux = 0;
        int[][] matrix_auxiliar = new int[3][3];  
        for(int i = 0; i < 3; i++){
            int aux_2 = 2; 
            for(int j = 0; j < 3; j++){
                matrix_auxiliar[aux_2][aux] = otro.tablero[i][j]; 
                aux_2 -= 1; 
            }
            aux += 1; 
        }
        return mesIgual(matrix_auxiliar);
    }

    /**
     * Rota el otro tablero 180Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico180(Gato otro) {
        int aux = 2;
        int[][] matrix_auxiliar = new int[3][3];  
        for(int i = 0; i < 3; i++){
            int aux_2 = 2; 
            for(int j = 0; j < 3; j++){
                matrix_auxiliar[aux][aux_2] = otro.tablero[i][j]; 
                aux_2 -= 1; 
            }
            aux -= 1; 
        }
        return mesIgual(matrix_auxiliar);
    }

    /**
     * Rota el otro tablero 270Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico270(Gato otro) {
        int aux = 2;
        int[][] matrix_auxiliar = new int[3][3];  
        for(int i = 0; i < 3; i++){
            int aux_2 = 0; 
            for(int j = 0; j < 3; j++){
                matrix_auxiliar[aux_2][aux] = otro.tablero[i][j]; 
                aux_2 += 1; 
            }
            aux -= 1; 
        }
        return mesIgual(matrix_auxiliar);
    }

    /**
     * Indica si dos estados del juego del gato son iguales, considerando
     * simetrías, de este modo el problema se vuelve manejable.
     */
    @Override
    public boolean equals(Object o) {
        Gato otro = (Gato) o;
        if (esIgual(otro)) {
            return true;
        }

        if (esSimetricoDiagonalInvertida(otro)) {
            return true;
        }
        if (esSimetricoDiagonal(otro)) {
            return true;
        }
        if (esSimetricoVerticalmente(otro)) {
            return true;
        }
        if (esSimetricoHorizontalmente(otro)) {
            return true;
        }
        if (esSimetrico90(otro)) {
            return true;
        }
        if (esSimetrico180(otro)) {
            return true;
        }
        if (esSimetrico270(otro)) {
            return true; // No redujo el diámetro máximo al agregarlo
        }
        return false;
    }

    /**
     * Devuelve una representación con caracteres de este estado. Se puede usar
     * como auxiliar al probar segmentos del código.
     */
    @Override
    public String toString() {
        char simbolo = jugador1 ? 'o' : 'x';
        String gs = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gs += tablero[i][j] + " ";
            }
            gs += '\n';
        }
        return gs;
    }
}
