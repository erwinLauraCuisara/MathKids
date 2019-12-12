/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanaPrincipal;
/**
 *
 * @author Gustavo
 */
import complementos.gestionArchivos;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionListener;
import java.awt.*;
import dimensionesEscala.DimensionesPantalla;
import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ventanaSesion extends JFrame implements ActionListener, ChangeListener, Serializable {

    private final JButton botonSalir, botonContinuar, botonNext, botonAgregar, botonPrev, botonRegistrar, botonEliminar;
    private JCheckBox checkMultijugador;
    private JComboBox comboTipoJuego, comboNombreJugador;
    private final JPanel panelLamina1, panelLamina2, panelLamina3;
    private JPanel[] paneles;
    private byte indice;
    private JTextField fieldJugador, fieldNombreRegistro;
    private JPasswordField fieldContrasenia, fieldContraseniaRegistro, fieldContraseniaRegistro2, fieldContraseniaEliminar;
    private JLabel labelFondo, labelTitulo, labelSubTitulo1, labelSubTitulo2, labelSubTitulo3, labelListaJugadores, labelSugerencia, labelDerechosReservados, labelNombre, labelContraseña, labelNombreRegistro, labelcontraseñaRegistro, labelConfirmaContraseñaRegistro;
    private final DimensionesPantalla dimPan;
    private JScrollBar scroll1;
    private ventanaMenu menu;

    private File directorio;
    private String ruta, passowrd;
    private String nombreJugador;
    //Cargar Datos
    private gestionArchivos archivo;
    private List listaJugador, listaJugadores;

    protected ventanaSesion() {

        listaJugador = new ArrayList(); // Se usará cuando solo hay un jugador
        listaJugadores = new ArrayList(); // Se usará cuando hay n jugadores

        archivo = new gestionArchivos();
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        dimPan = new DimensionesPantalla();
        setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        getContentPane().setBackground(Color.darkGray);
        
        labelFondo = new JLabel();
        labelFondo.setSize(new DimensionesPantalla().PenX(100), new DimensionesPantalla().PenY(100));
        ImageIcon fondo = new ImageIcon(getClass().getResource("images/sesion.gif"));
        ImageIcon propiedadesFondo = new ImageIcon(fondo.getImage().getScaledInstance(labelFondo.getWidth(), labelFondo.getHeight(), Image.SCALE_DEFAULT));
        labelFondo.setIcon(propiedadesFondo);
        setBounds(-10, 0, new DimensionesPantalla().PenX(100F), new DimensionesPantalla().PenY(100F));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        add(labelFondo);
        

        ruta = "C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\datosGenerales\\";
        directorio = new File(ruta);
        passowrd = "holamundo";

        labelTitulo = new JLabel("<html>MATH KIDS</html>");
        labelTitulo.setBounds(dimPan.PenX(0), dimPan.PenY(9), dimPan.horizontal(), dimPan.PenY(10));
        labelTitulo.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(120)));
        labelTitulo.setForeground(Color.ORANGE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelTitulo);

        panelLamina1 = new JPanel();
        panelLamina1.setBounds(dimPan.PenX(25), dimPan.PenY(30), dimPan.PenX(50), dimPan.PenY(50));
        panelLamina1.setLayout(null);
        panelLamina1.setVisible(false);
        panelLamina1.setBackground(Color.GRAY);
        labelFondo.add(panelLamina1);

        panelLamina2 = new JPanel();
        panelLamina2.setBounds(dimPan.PenX(25), dimPan.PenY(30), dimPan.PenX(50), dimPan.PenY(50));
        panelLamina2.setLayout(null);
        panelLamina2.setVisible(false);
        panelLamina2.setBackground(Color.GRAY);
        labelFondo.add(panelLamina2);

        panelLamina3 = new JPanel();
        panelLamina3.setBounds(dimPan.PenX(25), dimPan.PenY(30), dimPan.PenX(50), dimPan.PenY(50));
        panelLamina3.setLayout(null);
        panelLamina3.setVisible(false);
        panelLamina3.setBackground(Color.RED);
        labelFondo.add(panelLamina3);

        indice = 0;
        paneles = new JPanel[3];
        paneles[0] = panelLamina1;
        paneles[1] = panelLamina2;
        paneles[2] = panelLamina3;
        paneles[0].setVisible(true);

        botonNext = new JButton("►");
        botonNext.setBounds(dimPan.PenX(50), dimPan.PenY(80), dimPan.PenX(5), dimPan.PenY(5));
        botonNext.addActionListener(this);
        botonNext.setContentAreaFilled(false);
        labelFondo.add(botonNext);

        botonPrev = new JButton("◄");
        botonPrev.setBounds(dimPan.PenX(45), dimPan.PenY(80), dimPan.PenX(5), dimPan.PenY(5));
        botonPrev.addActionListener(this);
        botonPrev.setContentAreaFilled(false);
        labelFondo.add(botonPrev);

        labelSubTitulo1 = new JLabel("INICIAR");
        labelSubTitulo1.setBounds(0, dimPan.PenY(3), dimPan.PenX(50), dimPan.PenY(8));
        labelSubTitulo1.setForeground(Color.white);
        labelSubTitulo1.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(60)));
        labelSubTitulo1.setHorizontalAlignment(SwingConstants.CENTER);
        panelLamina1.add(labelSubTitulo1);

        labelSubTitulo2 = new JLabel("<html>NUEVO JUGADOR</html>");
        labelSubTitulo2.setBounds(0, dimPan.PenY(2), dimPan.PenX(50), dimPan.PenY(10));
        labelSubTitulo2.setForeground(Color.YELLOW);
        labelSubTitulo2.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(50)));
        labelSubTitulo2.setHorizontalAlignment(SwingConstants.CENTER);
        panelLamina2.add(labelSubTitulo2);

        labelSubTitulo3 = new JLabel("ADMINISTRADOR");
        labelSubTitulo3.setBounds(0, dimPan.PenY(3), dimPan.PenX(50), dimPan.PenY(8));
        labelSubTitulo3.setForeground(Color.YELLOW);
        labelSubTitulo3.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(60)));
        labelSubTitulo3.setHorizontalAlignment(SwingConstants.CENTER);
        panelLamina3.add(labelSubTitulo3);

        //Panel 1
        labelSugerencia = new JLabel("<<<<<<<<<< Ingresa sin contraseña en modo ''Entrenamiento'' >>>>>>>>>>");
        labelSugerencia.setBounds(dimPan.PenX(0), dimPan.PenY(15), dimPan.PenX(50), dimPan.PenY(3));
        labelSugerencia.setForeground(Color.WHITE);
        labelSugerencia.setHorizontalAlignment(SwingConstants.CENTER);
        labelSugerencia.setFont(new Font("Andale Mono", 2, dimPan.tamanioLetra(15)));
        panelLamina1.add(labelSugerencia);

        labelNombre = new JLabel("Nombre:");
        labelNombre.setBounds(dimPan.PenX(0), dimPan.PenY(20), dimPan.PenX(23), dimPan.PenY(3));
        labelNombre.setForeground(Color.WHITE);
        labelNombre.setFont(new Font("Andale mono", 2, dimPan.tamanioLetra(30)));
        labelNombre.setHorizontalAlignment(SwingConstants.RIGHT);
        panelLamina1.add(labelNombre);

        labelContraseña = new JLabel("Contraseña:");
        labelContraseña.setBounds(dimPan.PenX(0), dimPan.PenY(27), dimPan.PenX(23), dimPan.PenY(3));
        labelContraseña.setForeground(Color.WHITE);
        labelContraseña.setFont(new Font("Andale mono", 2, dimPan.tamanioLetra(30)));
        labelContraseña.setHorizontalAlignment(SwingConstants.RIGHT);
        panelLamina1.add(labelContraseña);

        fieldJugador = new JTextField("Entrenamiento");
        fieldJugador.setBounds(dimPan.PenX(25), dimPan.PenY(20), dimPan.PenX(15), dimPan.PenY(3));
        panelLamina1.add(fieldJugador);

        fieldContrasenia = new JPasswordField();
        fieldContrasenia.setBounds(dimPan.PenX(25), dimPan.PenY(27), dimPan.PenX(15), dimPan.PenY(3));
        //fieldContrasenia.setEditable(false);
        panelLamina1.add(fieldContrasenia);

        checkMultijugador = new JCheckBox("Multijugador");
        checkMultijugador.setBounds(dimPan.PenX(22), dimPan.PenY(35), dimPan.PenX(20), dimPan.PenY(5));
        checkMultijugador.setForeground(Color.WHITE);
        checkMultijugador.setBackground(Color.GRAY);
        checkMultijugador.addChangeListener(this);
        panelLamina1.add(checkMultijugador);

        botonAgregar = new JButton("AGREGAR");
        botonAgregar.setBounds(dimPan.PenX(19), dimPan.PenY(40), dimPan.PenX(12), dimPan.PenY(5));
        botonAgregar.addActionListener(this);
        botonAgregar.setEnabled(false);
        botonAgregar.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(20)));
        botonAgregar.setBackground(new Color(169, 169, 169));
        botonAgregar.setForeground(Color.white);
        panelLamina1.add(botonAgregar);

        //Panel 2
        labelNombreRegistro = new JLabel("Nombre:");
        labelNombreRegistro.setBounds(dimPan.PenX(0), dimPan.PenY(15), dimPan.PenX(23), dimPan.PenY(3));
        labelNombreRegistro.setForeground(Color.WHITE);
        labelNombreRegistro.setFont(new Font("Andale mono", 2, dimPan.tamanioLetra(30)));
        labelNombreRegistro.setHorizontalAlignment(SwingConstants.RIGHT);
        panelLamina2.add(labelNombreRegistro);

        labelcontraseñaRegistro = new JLabel("Contraseña:");
        labelcontraseñaRegistro.setBounds(dimPan.PenX(0), dimPan.PenY(22), dimPan.PenX(23), dimPan.PenY(3));
        labelcontraseñaRegistro.setForeground(Color.WHITE);
        labelcontraseñaRegistro.setFont(new Font("Andale mono", 2, dimPan.tamanioLetra(30)));
        labelcontraseñaRegistro.setHorizontalAlignment(SwingConstants.RIGHT);
        panelLamina2.add(labelcontraseñaRegistro);

        labelConfirmaContraseñaRegistro = new JLabel("Repite la contraseña:");
        labelConfirmaContraseñaRegistro.setBounds(dimPan.PenX(0), dimPan.PenY(29), dimPan.PenX(23), dimPan.PenY(3));
        labelConfirmaContraseñaRegistro.setForeground(Color.WHITE);
        labelConfirmaContraseñaRegistro.setFont(new Font("Andale mono", 2, dimPan.tamanioLetra(30)));
        labelConfirmaContraseñaRegistro.setHorizontalAlignment(SwingConstants.RIGHT);
        panelLamina2.add(labelConfirmaContraseñaRegistro);

        labelListaJugadores = new JLabel("");
        labelListaJugadores.setBounds(dimPan.PenX(0), dimPan.PenY(90), dimPan.PenX(100), dimPan.PenY(5));
        labelListaJugadores.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(20)));
        labelListaJugadores.setForeground(Color.orange);
        labelListaJugadores.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelListaJugadores);

        fieldNombreRegistro = new JTextField();
        fieldNombreRegistro.setBounds(dimPan.PenX(25), dimPan.PenY(15), dimPan.PenX(15), dimPan.PenY(3));
        panelLamina2.add(fieldNombreRegistro);

        fieldContraseniaRegistro = new JPasswordField();
        fieldContraseniaRegistro.setBounds(dimPan.PenX(25), dimPan.PenY(22), dimPan.PenX(15), dimPan.PenY(3));
        panelLamina2.add(fieldContraseniaRegistro);

        fieldContraseniaRegistro2 = new JPasswordField();
        fieldContraseniaRegistro2.setBounds(dimPan.PenX(25), dimPan.PenY(29), dimPan.PenX(15), dimPan.PenY(3));
        panelLamina2.add(fieldContraseniaRegistro2);

        botonRegistrar = new JButton("CREAR");
        botonRegistrar.setBounds(dimPan.PenX(20), dimPan.PenY(40), dimPan.PenX(10), dimPan.PenY(5));
        botonRegistrar.addActionListener(this);
        //botonRegistrar.setContentAreaFilled(false);
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(20)));
        botonRegistrar.setBackground(new Color(169, 169, 169));
        panelLamina2.add(botonRegistrar);

        //Lamina 3
        comboNombreJugador = new JComboBox();
        comboNombreJugador.setBounds(dimPan.PenX(18), dimPan.PenY(20), dimPan.PenX(15), dimPan.PenY(3));
        panelLamina3.add(comboNombreJugador);
        comboNombreJugador.removeAllItems();
        comboNombreJugador.addItem("Elige un jugador");
        for (String list : directorio.list()) {
            if (!list.equalsIgnoreCase("Entrenamiento")) {
                comboNombreJugador.addItem(list);
            }
        }

        fieldContraseniaEliminar = new JPasswordField();
        fieldContraseniaEliminar.setBounds(dimPan.PenX(18), dimPan.PenY(30), dimPan.PenX(15), dimPan.PenY(3));
        panelLamina3.add(fieldContraseniaEliminar);

        botonEliminar = new JButton("ELIMINAR");
        botonEliminar.setBounds(dimPan.PenX(20), dimPan.PenY(40), dimPan.PenX(10), dimPan.PenY(5));
        botonEliminar.addActionListener(this);
        //botonEliminar.setContentAreaFilled(false);
        botonEliminar.setForeground(Color.RED);
        botonEliminar.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(20)));
        botonEliminar.setBackground(new Color(169, 169, 169));
        panelLamina3.add(botonEliminar);

        //Botones De abajo
        botonSalir = new JButton("SALIR");
        botonSalir.setBounds(dimPan.PenX(2), dimPan.PenY(90), dimPan.PenX(10), dimPan.PenY(5));
        botonSalir.addActionListener(this);
        botonSalir.setBackground(null);
        botonSalir.setContentAreaFilled(false);
        labelFondo.add(botonSalir);

        botonContinuar = new JButton("CONTINUAR");
        botonContinuar.setBounds(dimPan.PenX(85), dimPan.PenY(90), dimPan.PenX(10), dimPan.PenY(5));
        botonContinuar.addActionListener(this);
        botonContinuar.setContentAreaFilled(false);
        labelFondo.add(botonContinuar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == botonNext) {
            byte aux = indice;
            indice++;
            if (indice >= paneles.length) {
                indice = 0;
            }
            paneles[indice].setVisible(true);
            paneles[aux].setVisible(false);
        }
        if (e.getSource() == botonPrev) {
            byte aux = indice;
            indice--;
            if (indice <= -1) {
                indice = (byte) (paneles.length - 1);
            }
            paneles[indice].setVisible(true);
            paneles[aux].setVisible(false);
        }

        if (e.getSource() == botonContinuar) {

            if (checkMultijugador.isSelected()) {//Multijugador
                if (listaJugadores.size() > 1) {
                    menu = new ventanaMenu(listaJugadores);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "En modo multijugador, \n deben existir al menos 2 jugadores");
                }

            } else {//Individual
                File jugador = new File(ruta + fieldJugador.getText());
                nombreJugador = fieldJugador.getText();

                if (jugador.exists()) {
                    if (fieldJugador.getText().equals(archivo.recuperaDatos(ruta + fieldJugador.getText())[0])) {
                        if (fieldContrasenia.getText().equals(archivo.recuperaDatos(ruta + nombreJugador)[1])) {
                            listaJugador.add(nombreJugador);
                            menu = new ventanaMenu(listaJugador);
                            this.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "El jugador no existe");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El jugador no existe");
                }
            }
        }

        if (e.getSource() == botonEliminar) {

            if (comboNombreJugador.getSelectedItem().toString().equals("Elige un jugador")) {

                JOptionPane.showMessageDialog(null, "La opción no es válida");

            } else {
                //String contraseniaUsuario = archivo.recuperaDatos(ruta + comboNombreJugador.getSelectedItem().toString())[1];
                if (fieldContraseniaEliminar.getText().equals("mundolibre")) {//if (fieldContraseniaEliminar.getText().equals(contraseniaUsuario) || fieldContraseniaEliminar.getText().equals("mundolibre")) {
                    //Eliminar
                    archivo.eliminarArchivo(ruta + comboNombreJugador.getSelectedItem().toString());
                    archivo.eliminarArchivo("C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego1\\" + comboNombreJugador.getSelectedItem().toString());
                    archivo.eliminarArchivo("C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego2\\" + comboNombreJugador.getSelectedItem().toString());
                    archivo.eliminarArchivo("C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego3\\" + comboNombreJugador.getSelectedItem().toString());
                    archivo.eliminarArchivo("C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego4\\" + comboNombreJugador.getSelectedItem().toString());
                    //A continuacion se actualiza el comboBox
                    comboNombreJugador.removeAllItems();
                    comboNombreJugador.addItem("Elige un jugador");
                    for (String list : directorio.list()) {
                        if (!list.equalsIgnoreCase("Entrenamiento")) {
                            comboNombreJugador.addItem(list);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta \n vuelve a intentarlo");
                }
            }
            fieldContraseniaEliminar.setText("");
        }

        if (e.getSource() == botonRegistrar) {
            if (!fieldNombreRegistro.getText().equals("")) {
                File dir = new File(ruta + fieldNombreRegistro.getText());
                if (dir.exists()) {

                    JOptionPane.showMessageDialog(null, "El jugador ''" + fieldNombreRegistro.getText() + "'' ya existe!");

                } else {
                    if (fieldContraseniaRegistro.getText().equals(fieldContraseniaRegistro2.getText())) {
                        archivo.serializarDatos(ruta + fieldNombreRegistro.getText(), new String[]{fieldNombreRegistro.getText(), fieldContraseniaRegistro.getText()});
                        JOptionPane.showMessageDialog(null, "''" + fieldNombreRegistro.getText() + "'', fue registrado correctamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "Los campos de contraseña no coinciden!");
                    }
                    //A continuacion se actualiza el comboBox
                    comboNombreJugador.removeAllItems();
                    comboNombreJugador.addItem("Elige un jugador");
                    for (String list : directorio.list()) {
                        if (!list.equalsIgnoreCase("Entrenamiento")) {
                            comboNombreJugador.addItem(list);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Llena todos los campos...");
            }
            fieldNombreRegistro.setText("");
            fieldContraseniaRegistro.setText("");
            fieldContraseniaRegistro2.setText("");

        }

        if (e.getSource() == botonSalir) {
            System.exit(0);
        }

        if (e.getSource() == botonAgregar) {

            File jugador = new File(ruta + fieldJugador.getText());
            if (fieldJugador.getText().equals(archivo.recuperaDatos(ruta + fieldJugador.getText())[0]) && !fieldJugador.getText().equals("") && (fieldContrasenia.getText().equals(archivo.recuperaDatos(ruta + fieldJugador.getText())[1])) && !fieldJugador.getText().equalsIgnoreCase("Entrenamiento")) {
                if (!listaJugadores.contains(fieldJugador.getText())) {
                    listaJugadores.add(archivo.recuperaDatos(ruta + fieldJugador.getText())[0]);
                    String aux = "";
                    for (int i = 0; i < listaJugadores.size(); i++) {
                        aux = aux + " * " + listaJugadores.get(i);
                    }
                    labelListaJugadores.setText(aux);

                } else {
                    JOptionPane.showMessageDialog(null, "El jugador ya está en la lista");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Revise los campos");
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        if (checkMultijugador.isSelected() == true) {
            botonAgregar.setEnabled(true);
        } else {
            botonAgregar.setEnabled(false);
            labelListaJugadores.setText("");
            for (int i = 0; i < listaJugadores.size(); i++) {
                listaJugadores.remove(0);
            }
            System.out.println("Tamanio del arrelo es: " + listaJugadores.size());
        }
    }

    public static void main(String arg[]) {

        ventanaSesion ventana = new ventanaSesion();
        ventana.setVisible(true);

    }
}
