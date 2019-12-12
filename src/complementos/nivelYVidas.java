/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complementos;

/**
 *
 * @author Gustavo
 */
public class nivelYVidas {

    private int nivel, puntos, intentos, tamanioMatriz;

    private byte vidas;

    private boolean resultado;

    public nivelYVidas(int puntos, byte vidas, boolean resultado, int intentos) {

        this.puntos = puntos;

        this.vidas = vidas;

        this.nivel = 1;

        this.resultado = resultado;

        this.intentos = intentos;

    }

    public nivelYVidas(int puntos, byte vidas, boolean resultado, int intentos, int tamanioMatriz) { // Sobre carga de metodos para ordenar numeros

        this.puntos = puntos;

        this.vidas = vidas;

        this.nivel = 1;

        this.resultado = resultado;

        this.intentos = intentos;

        this.tamanioMatriz = tamanioMatriz;

    }

    public int nivel() {

        while (puntos > 0) {//while(puntos > 0 && resultado){ //alternativa para facilitar despues de una falla

            puntos = puntos - 800;
            
            if (puntos >= 0) {
                
                nivel++;
                
            }
        }

        return nivel;

    }

    public byte vidas() {

        if (intentos > 0) {
            if (!resultado && vidas > 0) {
                vidas--;
            }
        }

        return vidas;

    }

    public int segundos() { // Juego de ordenar

        return this.tamanioMatriz * 5;

    }

    public int segundosDeTiempo() { //juegos 2 y 3

        nivel();
        return 10 + (nivel * 2);

    }
    
    public int segundosDeTiempoJ3() { //juegos 3

        nivel();
        return 40 + (nivel * 4);

    }
    
    public int segundosMultijugador(int segundos, int numeroJugadores){ //Juego 4
        
        return segundos/numeroJugadores;
    }

}
