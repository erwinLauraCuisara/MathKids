/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j4cartaspares;

/**
 *
 * @author wi 10
 */
public class cartasPares {

    private int numeros[];

    public cartasPares() {
    }

    public void GenerarNumerosAleatorios(int nCartas, int cantidad) {
        numeros = new int[nCartas];
        int lista[] = new int[nCartas];
        int i = 0;
        int numeroA;
        int x;
        while (i < nCartas / 2) {
            numeroA = (int) (Math.random() * cantidad) + 1;
            x = (int) (Math.random() * nCartas - 1);
            if (numeros[x] == 0) {
                numeros[x] = numeroA;
                lista[i] = x;
                i++;
            }
        }
        i = 0;
        int n;
        for (int k = 0; k < nCartas; k++) {
            if (numeros[k] == 0) {
                n = lista[i];
                numeros[k] = numeros[n];
                i++;
            }

        }
    }

    public int[] getLista() {
        return numeros;
    }

}
