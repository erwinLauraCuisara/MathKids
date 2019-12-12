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
public class puntosJugador {

    private int puntos;

    public puntosJugador(int puntosActuales) {
        
        //Implementar vaariaciones en aumento de puntos dependiendo del juego
        this.puntos = puntosActuales;

    }

    public int puntos(boolean sumarPuntos) {

        if (sumarPuntos) {

            puntos = puntos + 100;

        }

        return this.puntos;

    }
    
}
