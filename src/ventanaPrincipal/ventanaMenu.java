package ventanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dimensionesEscala.*;
import java.util.*;

import j1ordenamiento.ventanaOrdenarNumeros;
import j2logica.ventanaLogica;
import j3completaoperacion.ventanaCompletaOperaciones;
import j4cartaspares.ventanaCartasPares;
import complementos.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gustavo
 */
public class ventanaMenu extends JFrame implements ActionListener {

    private JButton botonJuego1, botonJuego2, botonJuego3, botonJuego4, botonSalir, botonVolver, botonCargarJ1, botonCargarJ2, botonCargarJ3, botonCargarJ4;
    private JLabel labelTitulo, labelNombreJugador, labelNivel, labelFondo;

    private ventanaSesion sesion;
    private ventanaOrdenarNumeros ordenar;
    private ventanaLogica logica;
    private ventanaCompletaOperaciones completaOperaciones;
    private ventanaCartasPares pares;

    private int nivel;

    private JComboBox comboNivel;

    private ImageIcon imagen;
    private Icon escala;

    private ImageIcon imagenEscalado;
    private DimensionesPantalla dimPan;

    private audioEfectos audio;
    //Cargar datos
    private String nombreJugadorEs;
    private java.util.List copiaLista;
    private gestionArchivos archivo; // Ojo usaremos para comprobar que ventana se abrira juegoN - campeon - perdedor
    //private JPanel panelMinimizados;
    
    private JDesktopPane escritorio;
    
    public ventanaMenu(java.util.List listaJugadores) {
        copiaLista = new ArrayList();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }

        System.out.println(listaJugadores.size());
        nombreJugadorEs = "";
        for (int i = 0; i < listaJugadores.size(); i++) {
            if (i <= 0) {
                nombreJugadorEs = (String) listaJugadores.get(i);
            } else {
                nombreJugadorEs = nombreJugadorEs + " vs " + listaJugadores.get(i);
            }
        }
        //this.nombreJugadorEs = nombreJugador;
        setUndecorated(true);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setTitle("HOME");
        getContentPane().setBackground(new Color(71, 57, 8));

        setBounds(0, 0, new DimensionesPantalla().PenX(100), new DimensionesPantalla().PenY(100));

        dimPan = new DimensionesPantalla();
        
        escritorio = new JDesktopPane();
        escritorio.setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        this.add(escritorio);
                

        //TITULO DEL MENU
        labelTitulo = new JLabel();
        labelTitulo.setBounds(dimPan.PenX(15), dimPan.PenY(1), dimPan.PenX(70), dimPan.PenY(15));
        imagen = new ImageIcon(getClass().getResource("images/tituloMenuPrincipal.png"));
        imagenEscalado = new ImageIcon(imagen.getImage().getScaledInstance(labelTitulo.getWidth(), labelTitulo.getHeight(), Image.SCALE_DEFAULT));
        labelTitulo.setIcon(imagenEscalado);
        escritorio.add(labelTitulo);

        labelNombreJugador = new JLabel(this.nombreJugadorEs);
        labelNombreJugador.setBounds(0, dimPan.PenY(16), dimPan.horizontal(), dimPan.PenY(7F));
        labelNombreJugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(50)));
        labelNombreJugador.setForeground(Color.darkGray);
        labelNombreJugador.setHorizontalAlignment(SwingConstants.CENTER);
        escritorio.add(labelNombreJugador);

        nivel = 1;
        if (copiaLista.size() > 1) {

            labelNivel = new JLabel("Nivel:");
            labelNivel.setBounds(dimPan.PenX(1), dimPan.PenY(8), dimPan.PenX(8), dimPan.PenY(3));
            labelNivel.setForeground(Color.WHITE);
            labelNivel.setFont(new Font("Andale Mono", 2, dimPan.tamanioLetra(20)));
            labelNivel.setHorizontalAlignment(SwingConstants.RIGHT);
            escritorio.add(labelNivel);

            comboNivel = new JComboBox();
            comboNivel.setBounds(dimPan.PenX(10), dimPan.PenY(8), dimPan.PenX(3), dimPan.PenY(3));
            comboNivel.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(18)));
            comboNivel.setBackground(Color.BLACK);
            comboNivel.setForeground(Color.WHITE);
            escritorio.add(comboNivel);
            for (int i = 1; i <= 5; i++) {
                comboNivel.addItem(i);
            }
        }
        
        botonJuego1 = new JButton();
        botonJuego1.setLocation(dimPan.PenX(8), dimPan.PenY(24));
        botonJuego1.setSize(dimPan.PenX(40), dimPan.PenY(35));
        imagen = new ImageIcon("src/ventanaPrincipal/images/juego1on.png");
        escala = new ImageIcon(imagen.getImage().getScaledInstance(botonJuego1.getWidth(), botonJuego1.getHeight(), Image.SCALE_DEFAULT));
        botonJuego1.setIcon(escala);
        botonJuego1.setBackground(null);
        botonJuego1.addActionListener(this);
        escritorio.add(botonJuego1);
        
        botonJuego2 = new JButton();
        botonJuego2.setBounds(dimPan.PenX(50), dimPan.PenY(24), dimPan.PenX(40), dimPan.PenY(35));
        imagen = new ImageIcon("src/ventanaPrincipal/images/juego2on.png");
        escala = new ImageIcon(imagen.getImage().getScaledInstance(botonJuego2.getWidth(), botonJuego2.getHeight(), Image.SCALE_DEFAULT));
        botonJuego2.setIcon(escala);
        botonJuego2.setBackground(null);
        botonJuego2.addActionListener(this);
        escritorio.add(botonJuego2);

        botonJuego3 = new JButton();
        botonJuego3.setBounds(dimPan.PenX(8), dimPan.PenY(61), dimPan.PenX(40), dimPan.PenY(35));
        imagen = new ImageIcon("src/ventanaPrincipal/images/juego3on.png");
        escala = new ImageIcon(imagen.getImage().getScaledInstance(botonJuego3.getWidth(), botonJuego3.getHeight(), Image.SCALE_DEFAULT));
        botonJuego3.setIcon(escala);
        botonJuego3.setBackground(null);
        botonJuego3.addActionListener(this);
        escritorio.add(botonJuego3);

        botonJuego4 = new JButton();
        botonJuego4.setBounds(dimPan.PenX(50), dimPan.PenY(61), dimPan.PenX(40), dimPan.PenY(35));
        imagen = new ImageIcon("src/ventanaPrincipal/images/juego4on.png");
        escala = new ImageIcon(imagen.getImage().getScaledInstance(botonJuego4.getWidth(), botonJuego4.getHeight(), Image.SCALE_DEFAULT));
        botonJuego4.setIcon(escala);
        botonJuego4.setBackground(null);
        botonJuego4.addActionListener(this);
        escritorio.add(botonJuego4);

        botonSalir = new JButton();
        botonSalir.setBounds(dimPan.PenX(85F), dimPan.PenY(5F), dimPan.PenX(8F), dimPan.PenY(12F));
        imagen = new ImageIcon("src/ventanaPrincipal/images/salir.png");
        escala = new ImageIcon(imagen.getImage().getScaledInstance(botonSalir.getWidth(), botonSalir.getHeight(), Image.SCALE_DEFAULT));
        botonSalir.setIcon(escala);
        botonSalir.setBorder(null);
        botonSalir.setSelectedIcon(null);
        botonSalir.addActionListener(this);
        botonSalir.setToolTipText("Sale del juego");
        botonSalir.setOpaque(false);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setBorderPainted(false);
        escritorio.add(botonSalir);

        botonVolver = new JButton("Volver");
        botonVolver.setBounds(dimPan.PenX(85), dimPan.PenY(17), dimPan.PenX(8), dimPan.PenY(4));
        botonVolver.addActionListener(this);
        botonVolver.setBackground(Color.red);
        botonVolver.setForeground(Color.YELLOW);
        escritorio.add(botonVolver);
        
        botonJuego1.setLayout(null);
        botonCargarJ1 = new JButton("CARGAR JUEGO");
        botonCargarJ1.setBounds(0, 0, dimPan.PenX(10), dimPan.PenY(5));
        botonCargarJ1.addActionListener(this);
        botonCargarJ1.setBackground(Color.red);
        botonCargarJ1.setForeground(Color.WHITE);
        botonJuego1.add(botonCargarJ1);
        
        botonJuego2.setLayout(null);
        botonCargarJ2 = new JButton("CARGAR JUEGO");
        botonCargarJ2.setBounds(0, 0, dimPan.PenX(10), dimPan.PenY(5));
        botonCargarJ2.addActionListener(this);
        botonCargarJ2.setBackground(Color.red);
        botonCargarJ2.setForeground(Color.WHITE);
        botonJuego2.add(botonCargarJ2);
        
        botonJuego3.setLayout(null);
        botonCargarJ3 = new JButton("CARGAR JUEGO");
        botonCargarJ3.setBounds(0, 0, dimPan.PenX(10), dimPan.PenY(5));
        botonCargarJ3.addActionListener(this);
        botonCargarJ3.setBackground(Color.red);
        botonCargarJ3.setForeground(Color.WHITE);
        botonJuego3.add(botonCargarJ3);
        
        botonJuego4.setLayout(null);
        botonCargarJ4 = new JButton("CARGAR JUEGO");
        botonCargarJ4.setBounds(0, 0, dimPan.PenX(10), dimPan.PenY(5));
        botonCargarJ4.addActionListener(this);
        botonCargarJ4.setBackground(Color.red);
        botonCargarJ4.setForeground(Color.WHITE);
        botonJuego4.add(botonCargarJ4);
        
        //REPRODUCIR AUDIO
        audio = new audioEfectos();
        audio.reproduceFondo("fondoPrincipal");
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == botonJuego1) {

            //botonJuego1.setEnabled(false);
            if (copiaLista.size() > 1) {
                nivel = Integer.parseInt(comboNivel.getSelectedItem().toString());
                ordenar = new ventanaOrdenarNumeros(this.copiaLista, "", nivel); // En el String vacio existia cargar
            } else {
                ordenar = new ventanaOrdenarNumeros(this.copiaLista, "", 1); // 1 es cualquier numero porque no interesa en modo individual
            }
            escritorio.add(ordenar);
            ordenar.setVisible(true);

            audio.efectoDeSonido("water-drip2-med");
            audio.detenerAudio();

        }

        if (e.getSource() == botonJuego2) {
            //botonJuego2.setEnabled(false);
            audio.efectoDeSonido("water-drip2-med");
            if (copiaLista.size() > 1) {
                nivel = Integer.parseInt(comboNivel.getSelectedItem().toString());
                logica = new ventanaLogica(this.copiaLista, "", nivel);
            } else {
                logica = new ventanaLogica(this.copiaLista, "", 1); // 1 es cualquier numero porque no interesa en modo individual
                
            }
            escritorio.add(logica);
            logica.setVisible(true);
            //this.dispose();
            audio.detenerAudio();
        }

        if (e.getSource() == botonJuego3) {
            //botonJuego3.setEnabled(false);
            audio.efectoDeSonido("water-drip2-med");
            if (copiaLista.size() > 1) {
                nivel = Integer.parseInt(comboNivel.getSelectedItem().toString());
                completaOperaciones = new ventanaCompletaOperaciones(copiaLista, "", nivel);
            } else {
                completaOperaciones = new ventanaCompletaOperaciones(this.copiaLista, "", 1); // 1 es cualquier numero porque no interesa en modo individual
            }
            escritorio.add(completaOperaciones);
            completaOperaciones.setVisible(true);
            audio.detenerAudio();
        }
        if (e.getSource() == botonJuego4) {
            //botonJuego4.setEnabled(false);
            audio.efectoDeSonido("water-drip2-med");
            if (copiaLista.size() > 1) {
                nivel = Integer.parseInt(comboNivel.getSelectedItem().toString());
                pares = new ventanaCartasPares(copiaLista, "", nivel);
            } else {
                pares = new ventanaCartasPares(copiaLista, "", 1);
            }
            escritorio.add(pares);
            pares.setVisible(true);
            audio.detenerAudio();
        }

        if (e.getSource() == botonVolver) {

            sesion = new ventanaSesion();
            sesion.setVisible(true);
            audio.detenerAudio();
            this.setVisible(false);
        }
        if (e.getSource() == botonSalir) {
            System.exit(0);
        }
        
        //botones cargar juegos guardados en directorios
        if(e.getSource()==botonCargarJ1){
            abrirJuego("j1");
        }
        if(e.getSource()==botonCargarJ2){
            abrirJuego("j2");
        }
        if(e.getSource()==botonCargarJ3){
            abrirJuego("j3");
        }
        if(e.getSource()==botonCargarJ4){
            abrirJuego("j4");
        }
    }
    
    public void abrirJuego(String extensionJuego){
        //javax.swing.JFileChooser jF1 = new javax.swing.JFileChooser();
        JFileChooser jF1 = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(extensionJuego, extensionJuego);
        jF1.setFileFilter(filtro);
        int opcion = jF1.showOpenDialog(this);
        String ruta = "";
        try {
            if (opcion == jF1.APPROVE_OPTION) {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                System.out.println("Esperando");
                System.out.println("La rura es: "+ruta);
                
                if(extensionJuego.equalsIgnoreCase("j1")){
                    ordenar = new ventanaOrdenarNumeros(this.copiaLista, ruta, 1); // 1 es cualquier numero porque no interesa en modo individual
                    escritorio.add(ordenar);
                    ordenar.setVisible(true);
                } else if(extensionJuego.equalsIgnoreCase("j2")){
                    logica = new ventanaLogica(this.copiaLista, ruta, 1);
                    escritorio.add(logica);
                    logica.setVisible(true);
                } else if(extensionJuego.equalsIgnoreCase("j3")){
                    completaOperaciones = new ventanaCompletaOperaciones(this.copiaLista, ruta, 1);
                    escritorio.add(completaOperaciones);
                    completaOperaciones.setVisible(true);
                } else { // Si es j4
                    pares = new ventanaCartasPares(copiaLista, ruta, 1);
                    escritorio.add(pares);
                    pares.setVisible(true);
                }
            audio.efectoDeSonido("water-drip2-med");
            audio.detenerAudio();
                //System.out.println("El nombre del archivo es: "+jF1.getSelectedFile().getName());
                //Aqui ya tiens la ruta,,,ahora puedes crear un fichero n esa ruta y escribir lo k kieras... 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "El directorio no es válido");
        }
        
        System.out.println("Se guardo el archivo");
    }
}
