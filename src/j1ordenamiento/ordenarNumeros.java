/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j1ordenamiento;

/**
 *
 * @author Gustavo
 */
public class ordenarNumeros {

    private int cantidad;
    private int[] numsAleatorios;
    private boolean bienHecho;
    private int x, negPos, probOrden; // negPos
    private String numeroEnCadena, tipoDeOrden;
    private int indice, cantidadDigitosMaximo;
    private float probNegar;

    public ordenarNumeros(int cantidadNumeros, int nivel, int nivelMaximo) { // el constructor recibe como dato  el tamaño del arreglo (cantidad de numeros a generar)    

        //{ AUMATIZAR PARA N NIVELES
        float nivelMaximox = nivelMaximo;
        probNegar = 0;

        int digitos = 1;
        int contador = 1;
        cantidadDigitosMaximo = (int) (Math.ceil(nivelMaximox / 2));
        if (nivel <= cantidadDigitosMaximo) { // Cantidad de dígitos
            while (contador <= cantidadDigitosMaximo && contador <= nivel) {
                digitos *= 10;
                contador++;
            }
        } else {
            while (contador <= cantidadDigitosMaximo) {
                digitos *= 10;
                contador++;
            }
            //Probabilidad de negar un numero
            int nivelesNegativos = nivelMaximo - cantidadDigitosMaximo;
            float incrementoNegacionPorNivel = (float) (50 / nivelesNegativos);
            contador = 1;
            while (contador<= (nivel - cantidadDigitosMaximo)) {
                probNegar += incrementoNegacionPorNivel;
                contador ++;
            }
        }

        tipoDeOrden = "ordena de menor a mayor...";
        bienHecho = true; // Servirá para saber si el jugador ordeno bien o no los numeros
        indice = 0; // Sirve pararecorrer el arreglo

        cantidad = cantidadNumeros; // Sirve para definir el tamaño del arreglo
        numsAleatorios = new int[cantidad]; //Es la instanciación del arreglo al cual le pasamos cantidad como tamaño

        while (indice < cantidad) { // Este ciclo ciclo genera los números aleatorios y asigna el valor a los indices del arreglo

            x = (int) (Math.random() * digitos);  //Genera un numero aleatorio cada ves que entra al ciclo

            //Proceso de negar un numero........................
            negPos = (int) (Math.random() * 100);
            if (negPos < probNegar) {
                numeroEnCadena = "-" + x;
                x = Integer.parseInt(numeroEnCadena);
            }

            numsAleatorios[indice] = x; // Copia el numero aleatorio en el indice del arreglo

            indice++;  // Incrementa indice en una unidad
        }

        probOrden = (int) (Math.random() * 100); // Este numero aleatorio decide el tipo de orden que se debe hacermayor a menor o menor a mayor

    }

    public int[] numerosAleatorios() { // Envia el arreglo con los números aleatorios generados por la computadora (desordenados)
        return numsAleatorios;
    }

    private void OrdenarNumeros() { //Este método no devuelve nada, su función es ordenar los numeros de MENOR A MAYOR

        int i = 0, ii = 1, aux; // estas dos variables (i, ii) nos permitiran comparar los valores del arreglo (son los indices para rrecorrer distintas posiciones) y aux nos sirve para guardar valores temporalmente

        while (i < numsAleatorios.length - 1) { // i es el numero anterior

            while (ii < numsAleatorios.length) { // ii es el numero posterior

                if (numsAleatorios[i] > numsAleatorios[ii]) { // Si el numero anterior es mayor al numero posterior ingresa al cuerpo del if
                    //A continuacion se realiza el proceso de intercambio
                    aux = numsAleatorios[i]; // guardamos el valor de la posicion i en aux
                    numsAleatorios[i] = numsAleatorios[ii];// copiamos el valor de la posicion ii en la posicion i
                    numsAleatorios[ii] = aux; // copiamos el valor guardado de i en ii
                }

                if (ii == 0) { //Rompe el ciclo si es la última comparación
                    break; // Rompe el ciclo del while anidado si ii es igual a 0 ya que es la última comparacion que se debe hacer
                }

                ii++;
            }
            i++;

            if (ii == 0) { //Rompe el ciclo si se ejecutó la última comparación
                break; //Rompe el ciclo del while principal y sale del último ciclo
            }

            if (i < (numsAleatorios.length - 1)) { //Si i no llega al ultimo elemento del arreglo
                ii = i + 1;
            } else {//i llega al último elemento del arreglo, este se debe comparar con  el primer elemento del arreglo (caso especial)
                ii = 0;
            }

        }

    }

    //metodo de mayor a menor
    private void OrdenarNumerosReverso() { //Este método no devuelve nada, su función es ordenar los numeros de MAYOR A MENOR

        int i = 0, ii = 1, aux; // estas dos variables (i, ii) nos permitiran comparar los valores del arreglo (son los indices para rrecorrer distintas posiciones) y aux nos sirve para guardar valores temporalmente

        while (i < numsAleatorios.length - 1) { // i es el numero anterior

            while (ii < numsAleatorios.length) { // ii es el numero posterior

                if (numsAleatorios[i] < numsAleatorios[ii]) { // Si el numero anterior es menor al numero posterior ingresa al cuerpo del if
                    //A continuacion se realiza el proceso de intercambio
                    aux = numsAleatorios[i]; // guardamos el valor de la posicion i en aux
                    numsAleatorios[i] = numsAleatorios[ii];// copiamos el valor de la posicion ii en la posicion i
                    numsAleatorios[ii] = aux; // copiamos el valor guardado de i en ii
                }

                if (ii == 0) { //Rompe el ciclo si es la última comparación
                    break; // Rompe el ciclo del while anidado si ii es igual a 0 ya que es la última comparacion que se debe hacer
                }

                ii++;
            }
            i++;

            if (ii == 0) { //Rompe el ciclo si se ejecutó la última comparación
                break; //Rompe el ciclo del while principal y sale del último ciclo
            }

            if (i < (numsAleatorios.length - 1)) { //Si i no llega al ultimo elemento del arreglo
                ii = i + 1;
            } else {//i llega al último elemento del arreglo, este se debe comparar con  el primer elemento del arreglo (caso especial)
                ii = 0;
            }

        }

    }

    public String TipoDeOrden() {//Este método elige que tipo de orden se debe realizar

        if (probOrden < 50) {
            return tipoDeOrden = "ordena de mayor a menor...";
        } else {
            return tipoDeOrden;
        }

    }

    public int[] numerosOrdenados() { // Este metodo devuelve el arreglo con los numeros ordenados (con return)
        if (probOrden < 50) {
            OrdenarNumerosReverso();
        } else {
            OrdenarNumeros(); // Para ello llama al método "ordenarNumeros()" 
        }

        return numsAleatorios; // y por último devuelve el arreglo ordenado
    }

    public boolean esCorrectoElOrden(int[] obtenido) { // Este metodo se encarga de comparar el arreglo ordenado por la computadora y el arreglo ordenado por el jugador, por eso recibe un arreglo "int[] obtenido" ordenado por el jugador

        if (probOrden < 50) {
            OrdenarNumerosReverso(); // Llama al método "OrdenarNumerosResverso()" para saber el orden correcto
        } else {
            OrdenarNumeros(); // llama al método "OrdenarNumeros()" para saber el orden correcto
        }

        int i = 0; // i será el indice para recorrer ambos arreglos

        //COMPARAR NUMEROS
        while (i < numsAleatorios.length) { // Este ciclo se encargará de recorrer ambos arreglos (el ordenado por el jugador y el ordenado por la computadora)
            if (numsAleatorios[i] != obtenido[i]) {// esta condicional servidá para cambiar el booleano "bienHecho", que por defecto viene en true por false
                //En la primera que encuentre que los numeros no coinciden entra en esta condicional
                bienHecho = false; // cambia el estado de bienHecho a false
                break; //rompe el ciclo, porque no necesita seguir comparando, ya es suficiente para saber que el arreglo enviado por el jugador está mal ordenado
            }
            i++;// increenta mientras los numeros coinsidan
        }

        return bienHecho;// retorna el booleano "bienHecho", con false si el jugador ordeno mal, y con true si el jugador ordeno correctamente 
    }
}
