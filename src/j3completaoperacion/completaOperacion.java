package j3completaoperacion;

import java.util.Random;

public class completaOperacion {

    private int tamanioArreglo, indice;
    private int respuesta, respuestaJugador;
    private int numDigitos;
    private boolean respuestaAcertada;
    private int nivel;
    private String[] arregloPropuesto, a; // En este arreglo se almacenara una sucecion de numeros y operadores
    private String[] posiblesRespuestas, posiblesRespuestas2;
    private int indiceEscondido;

    public completaOperacion(int nivel) {

        indice = 0;
        this.nivel = nivel;
        //"tamanioArreglo" Siempre debe ser impar
        switch (nivel) { // Se decide numero de digitos y tamanio de arreglo 
            case 1:
            case 4:
                numDigitos = 10;
                if (nivel == 1) {

                    tamanioArreglo = 3;

                } else {
                    tamanioArreglo = 5;
                }
                break;
            case 2:
            case 5:
                numDigitos = 100;
                if (nivel == 2) {

                    tamanioArreglo = 3;

                } else {
                    tamanioArreglo = 5;
                }
                break;
            case 3:
                numDigitos = 1000;
                tamanioArreglo = 3;
                break;
            default:
                numDigitos = 100000;
                tamanioArreglo = 5;
                break;
        }

        respuestaAcertada = false;

        arregloPropuesto = new String[tamanioArreglo];

        while (indice < tamanioArreglo) { // Genera numeros y signos aleatorios en un arreglo

            if ((indice % 2) != 0) {// Signo
                if (((int) (Math.random() * 100)) <25) {
                    arregloPropuesto[indice] = "+";
                } else
                    if (((int) (Math.random() * 100)) <25){
                        arregloPropuesto[indice] = "*";
                    } else
                        if (((int) (Math.random() * 100)) <25){
                        arregloPropuesto[indice] = "*";
                        }
                        else
                        {
                    arregloPropuesto[indice] = "-";
                }
            } else {
                arregloPropuesto[indice] = "" + (int) ((Math.random() * numDigitos))*-(1); // Genera numeros aleatorios
            }
            indice++;
        }
        a = arregloPropuesto;

        //Se escoge un numero para esconder
        indiceEscondido = escogerPosicionAEsconder();
    }

    private int escogerPosicionAEsconder() {

        int ocultarNumero;

        if (nivel <= 3) { // No se puede directamente porque el orden va mesclado con los signos

            ocultarNumero = 1 + new Random().nextInt(2) ;
            if (ocultarNumero == 1) {
                ocultarNumero = 0;// le pasamos la ubicacion del primer numero
            } else if (ocultarNumero == 2) {
                ocultarNumero = 2;// le pasamos la ubicacion del segundo numero
            }

        } else {//Si nivel es mayor a 3
            ocultarNumero = 1 + new Random().nextInt(3);
            if (ocultarNumero == 1) {
                ocultarNumero = 0;// le pasamos la ubicacion del primer numero
            } else if (ocultarNumero == 2) {
                ocultarNumero = 2;// le pasamos la ubicacion del segundo numero
            } else {
                ocultarNumero = 4;
            }
        }

        return ocultarNumero;
    }

    protected String[] numerosPropuestos() {// ESTE METODO DEVOLVERA UN ARREGLO CON NUMEROS ALEATORIOS, EL EL CUAL ESTARA EL NUMERO RESPUESTA

        int ocultarNumero = this.indiceEscondido; // posicion en el que se encuentra el numero que se escondera
        //Mesclamos el numero respuesta entre numeros aleatorios
        byte indice = 0;
        posiblesRespuestas = new String[8]; // En la posicion adicional se almacenara el numero del indice donde se copiará la respuesta
        int posicionAleatoria = (new Random().nextInt(posiblesRespuestas.length - 1 )); // en la que se acumulara el numero (respuesta oculto)

        while (indice < posiblesRespuestas.length) { // Llena el arreglo de numeros aleatorios
            posiblesRespuestas[indice] = ((int) (Math.random() * numDigitos)) + "";
            indice++;
        }
        posiblesRespuestas[posiblesRespuestas.length - 1] = ocultarNumero + ""; // en la última casilla mandamos la posicion en la que se encuentra la respuesta
        posiblesRespuestas[posicionAleatoria] = a[ocultarNumero]; // Menos al ultimo porque no queremos reemplazar la posicion que mandamos

        System.out.println("Tamanio del arreglo: " + tamanioArreglo);
        System.out.println("La posicion aleatoria escondida: " + ocultarNumero);
        System.out.println("La posicion aleatoria de la respuesta: " + posicionAleatoria);
        System.out.println("El numero escondido es: " + posiblesRespuestas[posicionAleatoria]);

        return posiblesRespuestas;
    }

    protected String[] numerosPropuestos2() {//Entre estos numeros estarán los numeros mostrados.

        posiblesRespuestas2 = new String[6]; //El arreglo denumeros de la segunda fila
        int i = 0;
        while (i < posiblesRespuestas2.length) {//Llena de numeros aleatorios
            posiblesRespuestas2[i] = posiblesRespuestas2[i] = ((int) (Math.random() * numDigitos)) + ""; //"?";
            i++;
        }
        //Mostrar los numeros mostrados (para no perder respeusta)
        i = 0;
        int posicionOculta = this.indiceEscondido;
        boolean PrimerNumeroIngresado = false;
        int mitadTamanioPosiblesRespuestas2 = (posiblesRespuestas2.length / 2);

        while (i < a.length) {
            if (nivel <= 3 && (posicionOculta != i)) {
                posiblesRespuestas2[new Random().nextInt(posiblesRespuestas2.length) ] = a[i];
            } else {
                if (posicionOculta != i && !PrimerNumeroIngresado) {
                    posiblesRespuestas2[new Random().nextInt(mitadTamanioPosiblesRespuestas2)] = a[i];
                    PrimerNumeroIngresado = true;
                } else if (posicionOculta != i && PrimerNumeroIngresado) {
                    posiblesRespuestas2[(mitadTamanioPosiblesRespuestas2 + new Random().nextInt(mitadTamanioPosiblesRespuestas2))] = a[i];//  OJO VERIFICAR ERROR.....................................................................
                }

            }

            i = i += 2;

        }

        return posiblesRespuestas2;

    }

    protected String[] getArreglo() {//retorna el arreglo generado

        return a;
    }

    protected int tamanioArreglo() {
        return this.tamanioArreglo;
    }

    protected int calculoRespuesta() { // Respuesta maquina

        int indice1 = 0;
        String copiaArregloPropuesto[] = new String[tamanioArreglo];
        while (indice1 < copiaArregloPropuesto.length) {//Copia los datos en un arreglo distinto

            copiaArregloPropuesto[indice1] = arregloPropuesto[indice1];

            indice1++;
        }
        indice1 = 0;

        while (indice1 < copiaArregloPropuesto.length && indice1 != copiaArregloPropuesto.length - 1) {

            if ("+".equals(copiaArregloPropuesto[indice1 + 1])) {

                respuesta = Integer.parseInt(copiaArregloPropuesto[indice1]) + Integer.parseInt(copiaArregloPropuesto[indice1 + 2]);
                copiaArregloPropuesto[indice1 + 2] = "" + respuesta;
            } else {
                
                
            //    /*
                    if ("-".equals(copiaArregloPropuesto[indice1 + 1])) {

                    respuesta = Integer.parseInt(copiaArregloPropuesto[indice1]) - Integer.parseInt(copiaArregloPropuesto[indice1 + 2]);
                    copiaArregloPropuesto[indice1 + 2] = "" + respuesta;
                    }
                    else
                    {
                        if ("*".equals(copiaArregloPropuesto[indice1 + 1])) {

                        respuesta = Integer.parseInt(copiaArregloPropuesto[indice1]) * Integer.parseInt(copiaArregloPropuesto[indice1 + 2]);
                        copiaArregloPropuesto[indice1 + 2] = "" + respuesta;
                        }
                        else
                        {
                            if ("/".equals(copiaArregloPropuesto[indice1 + 1])) {
            //    */          
                
                
                            respuesta = Integer.parseInt(copiaArregloPropuesto[indice1]) / Integer.parseInt(copiaArregloPropuesto[indice1 + 2]);
                            copiaArregloPropuesto[indice1 + 2] = "" + respuesta;
            //    /*
                            }
                        }
                    }            
            //    */
                
            }
            indice1 += 2;
        }
        System.out.println("La respuesta es: " + respuesta);
        return respuesta;
    }

    private void calculoRespuestaJugador(String[] arregloJugador) { // Respuesta Jugador

        try {
            String copiaArregloPrpuesto[] = arregloJugador;
            int indice2 = 0;

            while (indice2 < copiaArregloPrpuesto.length && indice2 != copiaArregloPrpuesto.length - 1) {// Realiza el calculo de lo ingresado por el usuario

                if ("+".equals(copiaArregloPrpuesto[indice2 + 1])) {
                    respuestaJugador = Integer.parseInt(copiaArregloPrpuesto[indice2]) + Integer.parseInt(copiaArregloPrpuesto[indice2 + 2]);
                    copiaArregloPrpuesto[indice2 + 2] = "" + respuestaJugador;
                }
                else
                {
                    if ("-".equals(copiaArregloPrpuesto[indice2 + 1])) {
                    respuestaJugador = Integer.parseInt(copiaArregloPrpuesto[indice2]) - Integer.parseInt(copiaArregloPrpuesto[indice2 + 2]);
                    copiaArregloPrpuesto[indice2 + 2] = "" + respuestaJugador;
                    }
                    
                
  //  /*
                    else {
                        if ("*".equals(copiaArregloPrpuesto[indice2 + 1])) {
                        respuestaJugador = Integer.parseInt(copiaArregloPrpuesto[indice2]) * Integer.parseInt(copiaArregloPrpuesto[indice2 + 2]);
                        copiaArregloPrpuesto[indice2 + 2] = "" + respuestaJugador;
                        }
                        else {
                            if ("/".equals(copiaArregloPrpuesto[indice2 + 1])) {
                            respuestaJugador = Integer.parseInt(copiaArregloPrpuesto[indice2]) / Integer.parseInt(copiaArregloPrpuesto[indice2 + 2]);
                            copiaArregloPrpuesto[indice2 + 2] = "" + respuestaJugador;
                            }
   // */
                            else {
                            respuestaJugador = respuesta + 1;
                            }
  //  /*
                        }
                    }
                }
   // */
                indice2 += 2;
            
                
                
            }
        } catch (Exception e) {
            respuestaJugador = 1000000000; // numero inalcanzable en caso de error
        }

        System.out.println("La respuesta del jugador es: " + respuestaJugador);
//
    }

    protected boolean acertoRespuesta(String[] arregleJugador) { // devuelve la autorización para incrementar o no el puntaje

        calculoRespuestaJugador(arregleJugador); // con esto envio a calculo, lo generador por el jugador jugador una sugerencia
        calculoRespuesta();

        if (respuesta == respuestaJugador) {
            respuestaAcertada = true;
        }

        return respuestaAcertada;

    }

    protected void mostrarArreglo() {

        int recorredor = 0;

        while (recorredor < arregloPropuesto.length) {
            System.out.print(arregloPropuesto[recorredor] + " ");
            recorredor++;
        }
        System.out.println();
    }
}
