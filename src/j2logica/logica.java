package j2logica;

/**
 *
 * @author Gustavo
 */
public class logica {

    private int a, b, c, d, res1, res2, varAleatorio;
    private String operador1, operador2;
    private String comparador;
    private boolean res1MenorQue;
    private boolean sumarPuntos;
    private int nivel, numeroDigitos;

    public logica(int nivel) {//Construimos

        this.nivel = nivel;
        sumarPuntos = false;
        res1MenorQue = true;

        //Genera el numero de dígitos según el nivel
        if (nivel == 1 || nivel == 3) {
            numeroDigitos = 10;
        } else if (nivel == 2 || nivel == 4) {
            numeroDigitos = 100;
        } else if (nivel == 5) {
            numeroDigitos = 1000;
        }

        a = (int) (Math.random() * numeroDigitos);
        b = (int) (Math.random() * numeroDigitos);
        
        //comparador aleatorio
        varAleatorio = (int) (Math.random() * 100);//Generamos otro número aleatorio, esta ves para decidir el comparador lógico
        if (varAleatorio < 50) {
            comparador = " menor que ";
        } else {
            comparador = " mayor que ";
        }

        if (nivel >= 3) {
            
            c = (int) (Math.random() * numeroDigitos);
            d = (int) (Math.random() * numeroDigitos);
            
            varAleatorio = (int) (Math.random() * 100);//Pare generar un signo aleatoriamente (+ o -) primera operacion izquierda 
            if (varAleatorio < 50) {
                operador1 = "+";
            } else {
                operador1 = "-";
            }

            varAleatorio = (int) (Math.random() * 100);//Pare generar un signo aleatoriamente (+ o -) segunda operacion derecha
            if (varAleatorio < 50) {
                operador2 = "+";
            } else {
                operador2 = "-";
            }
            //aqui el ocmparador
            
            if (operador1 == "+") {//Inicializamos res1
                res1 = a + b;
            } else {
                res1 = a - b;
            }
            if (operador2 == "+") {//Inicializamos res2
                res2 = c + d;
            } else {
                res2 = c - d;
            }
            // System.out.println("a = "+a+"\nb = "+b+"\nc = "+c+"\nd = "+d+"\nComparador = "+comparador+"\nOperador1 = "+operador1+"\nOperador2 = "+operador2+"\nRes1 = "+res1+"\nRes2 = "+res2);
        } else {
            res1 = a;
            res2 = b;
        }

    }

    protected String logicaGenerada() {
        //ADAPTAR PARA QUE SEA A PORCENTAJE DE NIVEL
        if (nivel >= 3) {
            return ("¿" + a + operador1 + b + " es " + comparador + c + operador2 + d + "?");
        }else{
            return "¿" + res1 + comparador + res2 + "?";
        }
    }

    protected boolean evaluaLogica() {
        
        if (res1 >= res2 && comparador.equalsIgnoreCase(" menor que ")) {// si fuera falsa la afirmacion
            res1MenorQue = false;
        } 
        if(res1 <= res2 && comparador.equalsIgnoreCase(" mayor que ")) { //// si fuera falsa la afirmacion
            res1MenorQue = false;
        }
        return res1MenorQue;
    }

    protected boolean puntos(boolean resJugador) { // valida o no aumentar puntos
        evaluaLogica();
        if (resJugador == res1MenorQue) {
            sumarPuntos = true;
        }
        
        return this.sumarPuntos;
    }
}
