/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j2logica;

import complementos.puntosJugador;
import dimensionesEscala.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ventanaPrincipal.*;
import complementos.*;
import java.io.*; // para serializar
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Esta clase es la ventana del juego de lógica (j2)
 * @Autores:  
 * Abasto Argote Gustavo, 
 * Laura Cuisara Erwin Harnaldo, 
 * Cayo Huaylla Ruddy, 
 * Ancari Iñiguez Abel
 * 
 */
public class ventanaLogica extends JInternalFrame implements ActionListener, Runnable, Serializable {

    private JLabel labelTitulo, labelLogica, labelPuntos, labelCaritas, labelVidas, labelNivel, labelIntentos, labelFondo, 
            labelCronometro, labelNombreJugador, labelJugadorTurno, labelPausa,
            labelAciertos, labelCronometroMultijugador;
    private JButton botonSi, botonNo, botonHome, botonPausa, botonGuardar;
    private DimensionesPantalla dimPan;
    private logica logico;
    private int puntos, nivel, intentos;
    private byte vidas;
    private puntosJugador sumaPuntos;
    private nivelYVidas nivelVidas;
    private int nivelMaximo;

    //Ventanas
    private ventanaCampeon ganador;
    private ventanaPerdedor perdedor;
    //Fondo de pantalla
    private ImageIcon fondo, propiedadesFondo;
    private audioEfectos sonido, sonidoFaltan5Minutos;
    private int segundos;
    private Thread hilo1;

    //Carga datos
    //private String ruta;
    private String nombreJugador;
    private gestionArchivos archivo;
    private java.util.List copiaLista;

    //Multijugador
    private int tiempoMultijugador, segundosRestantes, jugador, aciertosMultijugador;
    private String datosJugadores[][];
    private ventanaResultadosMultijugador resultadosMultijugador;
    //Pausado
    private boolean pausado;

    /**
     * 
     * @param listaJugadores Recibe la cantidad de jugadores
     * @param rutaArchivo Recibe la ruta de un archivo (En caso de que se cargue el archivo)
     * @param nivelMultijugador Recibe el nivel para el juego multijugador (desde el comboBox del la ventana Menu)
     */
    public ventanaLogica(java.util.List listaJugadores, String rutaArchivo, int nivelMultijugador) { // Accion es para la carga o no de datos
        
        pausado = false;
        copiaLista = new ArrayList();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }
        if (listaJugadores.size() <= 1) {

            nombreJugador = (String) listaJugadores.get(0);

        } else {
            nombreJugador = (String) copiaLista.get(0);//nombreJugador = "Multijugador";
        }

        archivo = new gestionArchivos();
        //ruta = "C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego2\\";

        setLayout(null);
        dimPan = new DimensionesPantalla();
        this.setIconifiable(true);
        getContentPane().setBackground(Color.BLACK);
        setTitle("Logica  Si - No");
        //FONDO DE PANTALLA
        labelFondo = new JLabel();
        labelFondo.setSize(new DimensionesPantalla().PenX(100), new DimensionesPantalla().PenY(100));
        fondo = new ImageIcon(getClass().getResource("images/animadoJuego1.gif"));
        propiedadesFondo = new ImageIcon(fondo.getImage().getScaledInstance(labelFondo.getWidth(), labelFondo.getHeight(), Image.SCALE_DEFAULT));
        labelFondo.setIcon(propiedadesFondo);
        setBounds(-10, 0, new DimensionesPantalla().PenX(100F), new DimensionesPantalla().PenY(100F));
        add(labelFondo);
        
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(0, 0, dimPan.PenX(5), dimPan.PenY(2));
        botonGuardar.addActionListener(this);
        botonGuardar.setContentAreaFilled(false);
        botonGuardar.setForeground(Color.WHITE);
        labelFondo.add(botonGuardar);

        nivelMaximo = 5;
        if (copiaLista.size() <= 1) { // Si no es multijugador
                archivo = new gestionArchivos();
                if (!rutaArchivo.equals("")) {//Vacio es que no contiene ninguna ruta
                    
                    try {
                        nivel = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[0]); // int
                        puntos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[1]); // int
                        vidas = Byte.parseByte(archivo.recuperaDatos(rutaArchivo)[2]); //Byte
                        intentos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[3]);// si es un juego guardado debe ser mayor a 0 (int)
                    } catch (Exception e) {
                       JOptionPane.showMessageDialog(null, "El archivo no es compatible");
                        nivel = 1;
                        puntos = 0; // int
                        vidas = 3; //Byte
                        intentos = 0;// si es un juego guardado debe ser mayor a 0 (int)
                    }
                } else {
                    nivel = 1;
                    puntos = 0; // int
                    vidas = 3; //Byte
                    intentos = 0;// si es un juego guardado debe ser mayor a 0 (int)
                }
        } else { // Si es multijugador

            tiempoMultijugador = 10; //(Nunca cambia) Tiempo total en el que se ejecutará el juego
            segundosRestantes = tiempoMultijugador; //Cronometro cambiante
            jugador = 0; //Posicion del jugador
            this.nivel = nivelMultijugador;
            puntos = (nivelMultijugador - 1) * 800;
            vidas = 1;
            //jugadores y resultados (inserta a datos de los jugadores)
            datosJugadores = new String[listaJugadores.size()][3];// cada fila tiene nombreJugador - puntos - tiempo
            for (int i = 0; i < copiaLista.size(); i++) {
                datosJugadores[i][0] = (String) copiaLista.get(i);
                datosJugadores[i][1] = "" + 0;
                datosJugadores[i][2] = "" + tiempoMultijugador;
            }

            labelJugadorTurno = new JLabel("Turno de: " + datosJugadores[0][0]); // Se carga al label el nombre del primer jugador
            labelJugadorTurno.setBounds(0, dimPan.PenY(1), dimPan.PenX(33), dimPan.PenY(5));
            labelJugadorTurno.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(30)));
            labelJugadorTurno.setForeground(Color.ORANGE);
            labelJugadorTurno.setHorizontalAlignment(SwingConstants.CENTER);
            labelFondo.add(labelJugadorTurno);

            labelAciertos = new JLabel("Aciertos: " + datosJugadores[0][1]); // Se carga al label el nombre del primer jugador
            labelAciertos.setBounds(dimPan.PenX(66), dimPan.PenY(1), dimPan.PenX(33), dimPan.PenY(5));
            labelAciertos.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(30)));
            labelAciertos.setForeground(Color.ORANGE);
            labelAciertos.setHorizontalAlignment(SwingConstants.CENTER);
            labelFondo.add(labelAciertos);

            labelCronometroMultijugador = new JLabel("Atento...");
            labelCronometroMultijugador.setBounds(dimPan.PenX(0), dimPan.PenY(82), dimPan.PenX(100), dimPan.PenY(10));
            labelCronometroMultijugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(110)));
            labelCronometroMultijugador.setHorizontalAlignment(SwingConstants.CENTER);
            labelCronometroMultijugador.setForeground(Color.WHITE);
            labelFondo.add(labelCronometroMultijugador);

            aciertosMultijugador = 0;
        }

        sumaPuntos = new puntosJugador(puntos);

        refrescarDatosNuevos();

        sonidoFaltan5Minutos = new audioEfectos();
        sonido = new audioEfectos();

        setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        labelTitulo = new JLabel("JUEGO DE LOGICA");
        labelTitulo.setBounds(0, dimPan.PenY(5), dimPan.PenX(100), dimPan.PenY(20));
        labelTitulo.setFont(new Font("Algerian", 1, dimPan.tamanioLetra(160)));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setForeground(new Color(90, 51, 6));
        labelFondo.add(labelTitulo);

        labelNombreJugador = new JLabel(this.nombreJugador);
        labelNombreJugador.setBounds(dimPan.PenX(70), dimPan.PenY(82), dimPan.PenX(30), dimPan.PenY(10));
        labelNombreJugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(50)));
        labelNombreJugador.setForeground(Color.WHITE);
        labelNombreJugador.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelNombreJugador);

        labelVidas = new JLabel("Vidas: " + vidas);
        labelVidas.setBounds(0, dimPan.PenY(1), dimPan.PenX(33), dimPan.PenY(5));
        labelVidas.setFont(new Font("Andale Mono", 1, 30));
        labelVidas.setForeground(Color.ORANGE);
        labelVidas.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelVidas);

        labelNivel = new JLabel("Nivel: " + nivel);
        labelNivel.setBounds(dimPan.PenX(33), dimPan.PenY(1), dimPan.PenX(33), dimPan.PenY(5));
        labelNivel.setFont(new Font("Andale Mono", 1, 30));
        labelNivel.setForeground(Color.ORANGE);
        labelNivel.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelNivel);

        labelPuntos = new JLabel("Puntaje: " + puntos);
        labelPuntos.setBounds(dimPan.PenX(66), dimPan.PenY(1), dimPan.PenX(33), dimPan.PenY(5));
        labelPuntos.setFont(new Font("Arial", 1, 30));
        labelPuntos.setForeground(Color.ORANGE);
        labelPuntos.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelPuntos);

        labelCaritas = new JLabel("DALE!");
        labelCaritas.setBounds(0, dimPan.PenY(25F), dimPan.PenX(100), dimPan.PenY(10));
        labelCaritas.setFont(new Font("Andale Mono", 1, 75));
        labelCaritas.setForeground(Color.YELLOW);
        labelCaritas.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelCaritas);

        labelLogica = new JLabel(logico.logicaGenerada());
        labelLogica.setBounds(0, dimPan.PenY(36), dimPan.PenX(100), dimPan.PenY(20));
        labelLogica.setHorizontalAlignment(SwingConstants.CENTER);
        labelLogica.setFont(new Font("Arial", 1, 70));
        labelLogica.setForeground(Color.cyan);
        labelFondo.add(labelLogica);

        botonSi = new JButton("SI");
        botonSi.setBounds(dimPan.PenX(35), dimPan.PenY(65), dimPan.PenX(10), dimPan.PenY(10));
        botonSi.setFont(new Font("Arial", 1, 70));
        botonSi.setOpaque(false);
        botonSi.setContentAreaFilled(false);
        botonSi.setForeground(Color.GREEN);
        labelFondo.add(botonSi);
        botonSi.addActionListener(this);

        botonNo = new JButton("NO");
        botonNo.setBounds(dimPan.PenX(55), dimPan.PenY(65), dimPan.PenX(10), dimPan.PenY(10));
        botonNo.setFont(new Font("Arial", 1, 70));
        labelFondo.add(botonNo);
        botonNo.setOpaque(false);
        botonNo.setContentAreaFilled(false);
        botonNo.setForeground(Color.RED);
        botonNo.addActionListener(this);

        botonHome = new JButton("HOME");
        botonHome.setBounds(dimPan.PenX(5), dimPan.PenY(82), dimPan.PenX(10), dimPan.PenY(10));
        labelFondo.add(botonHome);
        botonHome.setForeground(Color.YELLOW);
        botonHome.setFont(new Font("", 1, 30));
        botonHome.setBackground(new Color(86, 73, 27));
        botonHome.addActionListener(this);
        botonHome.setOpaque(false); // Vuelve transparente al boton
        botonHome.setContentAreaFilled(false);//Quita el fondo al pulsar

        //Desactivado por el momento por no haber utilidad
        labelIntentos = new JLabel("" + intentos);
        labelIntentos.setBounds(dimPan.PenX(90), dimPan.PenY(80), dimPan.PenX(20), dimPan.PenY(20));
        labelIntentos.setForeground(new Color(87, 69, 8));
        labelIntentos.setFont(new Font("Andale Mono", 1, 100));
        labelFondo.add(labelIntentos);
        labelIntentos.setVisible(false); // Oculto por el momento

        labelCronometro = new JLabel(segundos + "");
        labelCronometro.setBounds(dimPan.PenX(0), dimPan.PenY(82), dimPan.PenX(100), dimPan.PenY(10));
        labelCronometro.setForeground(new Color(200, 209, 8));
        labelCronometro.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(110)));
        labelFondo.add(labelCronometro);
        labelCronometro.setHorizontalAlignment(SwingConstants.CENTER);
        
        botonPausa = new JButton("PAUSAR");
        botonPausa.setBounds(dimPan.PenX(5), dimPan.PenY(77), dimPan.PenX(10), dimPan.PenY(5));
        botonPausa.setOpaque(false);
        botonPausa.addActionListener(this);
        botonPausa.setContentAreaFilled(false);
        botonPausa.setForeground(Color.WHITE);
        botonPausa.setFont(new Font("Andale Mono", 2, dimPan.tamanioLetra(20)));
        
        labelPausa = new JLabel("PAUSA");
        labelPausa.setBounds(0, dimPan.PenY(40), dimPan.horizontal(), dimPan.PenY(25));
        labelPausa.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(250)));
        labelPausa.setForeground(Color.WHITE);
        labelFondo.add(labelPausa);
        labelPausa.setHorizontalAlignment(SwingConstants.CENTER);
        labelPausa.setVisible(false);
        labelFondo.add(botonPausa);
        
        
        if(copiaLista.size()>1){
        
            labelVidas.setVisible(false);
            labelPuntos.setVisible(false);
            labelCronometro.setVisible(false);
        
        }
        
        hilo1 = new Thread(this);
        hilo1.start();

    }

    private void refrescarDatosNuevos() {

        logico = new logica(nivel);
        nivelVidas = new nivelYVidas(puntos, vidas, false, intentos);
        segundos = nivelVidas.segundosDeTiempo();// ojo boton segundos
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //inicio pausar
        if(e.getSource()==botonPausa && pausado == false){
            sonidoFaltan5Minutos.detenerAudio();
            hilo1.stop();
            labelLogica.setVisible(false);
            botonSi.setEnabled(false);
            botonNo.setEnabled(false);
            pausado = true;
            botonPausa.setText("Reanudar");
            labelPausa.setVisible(true);
            labelCaritas.setVisible(false);
            
        }else if(e.getSource()==botonPausa && pausado == true){
            if(segundos<=5){
                sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
            }
            hilo1 = new Thread(this);
            hilo1.start();
            labelLogica.setVisible(true);
            botonSi.setEnabled(true);
            botonNo.setEnabled(true);
            pausado = false;
            botonPausa.setText("Pusar");
            labelPausa.setVisible(false);
            labelCaritas.setVisible(true);
        } // fin boton pausar
        
        if (e.getSource() == botonSi) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            sonido.efectoDeSonido("botonSi");
            if (copiaLista.size() <= 1) {
                puntos = sumaPuntos.puntos(logico.puntos(true));//Con true el jugador dice que la lógica es verdadera
                labelPuntos.setText("Puntaje: " + puntos);
            }

            if (logico.puntos(true)) {
                labelCaritas.setForeground(Color.GREEN);
                labelCaritas.setText("CORRECTO :)");
                if (copiaLista.size() > 1) {
                    aciertosMultijugador++;
                    labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                }

            } else {
                labelCaritas.setForeground(Color.red);
                labelCaritas.setText("INCORRECTO :(");
                //Una incorretca quita una buena
                if (copiaLista.size() > 1) {
                    if (aciertosMultijugador > 0) {
                        aciertosMultijugador--;
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                    }
                }

            }
            //CONTROL DE LAS VIDAS Y EL NIVEL
            intentos++;
            if (copiaLista.size() <= 1) {
                nivelVidas = new nivelYVidas(puntos, vidas, logico.puntos(true), intentos);
                vidas = nivelVidas.vidas();
                nivel = nivelVidas.nivel();
                labelNivel.setText("Nivel: " + nivel);
                labelVidas.setText("Vidas: " + vidas);
                if (vidas <= 0) {
                    perdedor = new ventanaPerdedor('2', copiaLista, nivel);
                    perdedor.setVisible(true);
                    this.setVisible(false);

                    labelLogica.setForeground(Color.red);
                    labelLogica.setText("Perdiste el juego...");
                    botonSi.setEnabled(false);
                    botonNo.setEnabled(false);
                    hilo1.stop();
                } else {
                    refrescarDatosNuevos();
                    labelCronometro.setText(segundos + ""); // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii?
                    labelLogica.setText(logico.logicaGenerada());
                }
                if (nivel >= nivelMaximo + 1) {
                    ganador = new ventanaCampeon('2', copiaLista, nivel);
                    ganador.setVisible(true);
                    this.setVisible(false);
                    hilo1.stop();
                }
                labelIntentos.setText("" + intentos);
            } else {
                refrescarDatosNuevos();
                labelLogica.setText(logico.logicaGenerada());
            }
        } // Fin boton SI
        if (e.getSource() == botonNo) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            sonido.efectoDeSonido("botonNo");
            if (copiaLista.size() <= 1) {
                puntos = sumaPuntos.puntos(logico.puntos(false));//Con false el jugador dice que la lógica no es verdadera
                labelPuntos.setText("Puntaje: " + puntos);
            }

            if (logico.puntos(false)) {
                labelCaritas.setForeground(Color.GREEN);
                labelCaritas.setText("CORRECTO :)");
                if (copiaLista.size() > 1) {
                    aciertosMultijugador++;
                    labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                }
            } else {
                labelCaritas.setForeground(Color.red);
                labelCaritas.setText("INCORRECTO :(");
                //Una incorrecta quita una buena
                if (copiaLista.size() > 1) {
                    if (aciertosMultijugador > 0) {
                        aciertosMultijugador--;
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                    }
                }
            }

            labelLogica.setText(logico.logicaGenerada());

            //CONTROL DE LAS VIDAS Y EL NIVEL
            intentos++;
            if (copiaLista.size() <= 1) {
                nivelVidas = new nivelYVidas(puntos, vidas, logico.puntos(false), intentos);
                vidas = nivelVidas.vidas();
                nivel = nivelVidas.nivel();
                labelNivel.setText("Nivel: " + nivel);
                labelVidas.setText("Vidas: " + vidas);

                if (vidas == 0) {

                    perdedor = new ventanaPerdedor('2', copiaLista, nivel);
                    perdedor.setVisible(true);
                    this.setVisible(false);

                    labelLogica.setForeground(Color.red);
                    labelLogica.setText("Perdiste el juego...");
                    botonSi.setEnabled(false);
                    botonNo.setEnabled(false);
                    hilo1.stop();

                } else {
                    refrescarDatosNuevos();
                    labelCronometro.setText(segundos + ""); //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii?
                    labelLogica.setText(logico.logicaGenerada());
                }
                if (nivel >= nivelMaximo + 1) {
                    ganador = new ventanaCampeon('2', copiaLista, nivel);
                    ganador.setVisible(true);
                    this.setVisible(false);
                    hilo1.stop();
                }

                labelIntentos.setText("" + intentos);
            } else {
                refrescarDatosNuevos();
                labelLogica.setText(logico.logicaGenerada());
            }
        } // Fin boton NO

        if (e.getSource() == botonHome) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            //home = new ventanaMenu(copiaLista);
            //home.setVisible(true);
            setVisible(false);
            hilo1.stop();
            sonido.efectoDeSonido("risa1");

            //Guardar datos
            /*
            if (!nombreJugador.equalsIgnoreCase("Entrenamiento") && copiaLista.size() <= 1) {
                archivo.serializarDatos(ruta + nombreJugador, (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos}));
            }*/
        }
        
        if(e.getSource()==botonGuardar){
             if(!botonPausa.getText().equalsIgnoreCase("Reanudar")){
                botonPausa.doClick();
            }
            salvarArchivo("j2");
        }
    }

    @Override
    public void run() {
        if (copiaLista.size() <= 1) { // INDIVIDUAL
            while (segundos > 0) {

                try {
                    if (segundos < 6) {
                        if (segundos % 2 == 0) {
                            labelCronometro.setForeground(new Color(200, 209, 8));
                        } else {
                            labelCronometro.setForeground(Color.red);
                        }
                    } else {
                        labelCronometro.setForeground(new Color(200, 209, 8));
                    }

                    Thread.sleep(1000);

                } catch (InterruptedException ex) {
                    System.out.println("Se produjo un error en el proceso ''segundos''");
                }
                segundos--;
                if (segundos > 0) {
                    labelCronometro.setText(segundos + "");
                }

                if (segundos <= 0) {
                    if (logico.evaluaLogica()) {
                        botonNo.doClick();
                    } else {
                        botonSi.doClick();
                    }
                }
                if (segundos == 5) {
                    sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
                }
            }
        } else { // MULTIJUGADOR
            try {
                //segundosRestantes = tiempoMultijugador;
                //Aquí inicializamos al primer jugador
                while (segundosRestantes > 0 && jugador < copiaLista.size()) {
                    if (segundosRestantes == tiempoMultijugador) {
                        botonSi.setEnabled(false);
                        botonNo.setEnabled(false);
                        labelCronometroMultijugador.setText("Atento...!!!");
                        Thread.sleep(3000);
                        botonSi.setEnabled(true);
                        botonNo.setEnabled(true);

                        labelCronometroMultijugador.setText("Ya!");
                    }

                    Thread.sleep(1000);
                    labelCronometroMultijugador.setText("" + segundosRestantes);
                    segundosRestantes--;
                    if (segundosRestantes <= 0 && jugador < copiaLista.size() - 1) {
                        //Guardamos los datos del jugador (Temporal)
                        jugador++;
                        //botonYa.doClick();
                        refrescarDatosNuevos();
                        labelLogica.setText(logico.logicaGenerada());

                        datosJugadores[jugador - 1][1] = "" + aciertosMultijugador;
                        datosJugadores[jugador - 1][2] = "" + segundosRestantes;
                        aciertosMultijugador = 0;
                        segundosRestantes = tiempoMultijugador;
                        
                        nombreJugador = datosJugadores[jugador][0];
                        labelJugadorTurno.setText("Turno de: " + nombreJugador);
                        labelCronometroMultijugador.setText("" + segundosRestantes);
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                        
                        labelNombreJugador.setText(nombreJugador);
                        segundosRestantes = tiempoMultijugador;
                    }
                }
                datosJugadores[jugador][1] = "" + aciertosMultijugador;
                datosJugadores[jugador][2] = "" + segundosRestantes;

                resultadosMultijugador = new ventanaResultadosMultijugador(datosJugadores);
                resultadosMultijugador.setVisible(true);
                this.setVisible(false);
                labelCronometroMultijugador.setText("FIN");

            } catch (Exception e) {
                System.out.println("Se produjo un error " + e);
            }
        }
    }
    
    private void salvarArchivo(String extension){
        archivo = new gestionArchivos();
        javax.swing.JFileChooser jF1 = new javax.swing.JFileChooser();
        String ruta = "";
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(extension, extension);
        jF1.setFileFilter(filtro);
        try {
            if (jF1.showSaveDialog(null) == jF1.APPROVE_OPTION) {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                System.out.println("Se guardó en: "+ruta);
                //archivo.serializarDatos(ruta+"."+extension, (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos}));
                archivo.serializarDatos(ruta +".j2", (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos}));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        botonPausa.doClick();
    }
}
