package gal.cotarelo.controller;

import gal.cotarelo.model.FileModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.Optional;
import java.util.ResourceBundle;

public class FxmlController implements Initializable {

    @FXML
    private TextArea txtArea;

    @FXML
    private Label lblEstado;

    private FileModel modelo;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txtArea.setDisable(true);
    }

    public void setModelo(FileModel modelo, Stage stage) {

        this.modelo = modelo;
        this.stage = stage;

        txtArea.textProperty().addListener(
            (obs, anterior, nuevo) -> {

                modelo.setContenido(nuevo);
                modelo.setModificado(true);

                actualizarEstado();
            }
        );
    }

    @FXML
    private void onNuevo(ActionEvent e) {

        if(confirmarDescartarCambios()) {

            modelo.setContenido("");

            modelo.setRuta(null);

            txtArea.clear();

            modelo.setModificado(false);

            txtArea.setDisable(false);

            txtArea.requestFocus();

            actualizarEstado();
        }
    }

    @FXML
    private void onAbrir(ActionEvent e) {

        FileChooser selector;

        File fichero;

        String texto;

        if(confirmarDescartarCambios()) {

            selector = new FileChooser();

            selector.setTitle("Abrir fichero");

            fichero = selector.showOpenDialog(stage);

            if(fichero != null) {

                try {

                    texto = Files.readString(
                        fichero.toPath(),
                        StandardCharsets.UTF_8
                    );

                    modelo.setContenido(texto);

                    modelo.setRuta(
                        fichero.getAbsolutePath()
                    );

                    txtArea.setText(texto);

                    modelo.setModificado(false);

                    txtArea.setDisable(false);

                    actualizarEstado();

                } catch(IOException ex) {

                    mostrarError(ex.getMessage());
                }
            }
        }
    }

    @FXML
    private void onGuardar(ActionEvent e) {
        guardarFisicamente();
    }

    private void guardarFisicamente() {
        if (!modelo.tieneFichero()) {
            FileChooser selector = new FileChooser();
            selector.setTitle("Guardar fichero");
            File fichero = selector.showSaveDialog(stage);
            if (fichero != null) {
                modelo.setRuta(fichero.getAbsolutePath());
            } else {
                return;
            }
        }

        try {
            Files.write(
                new File(modelo.getRuta()).toPath(),
                modelo.getContenido().getBytes(
                    StandardCharsets.UTF_8
                )
            );
            modelo.setModificado(false);
            actualizarEstado();
        } catch (IOException ex) {
            mostrarError(ex.getMessage());
        }
    }

    @FXML
    private void onCerrar(ActionEvent e) {

        if(confirmarDescartarCambios()) {

            modelo.setRuta(null);

            txtArea.clear();

            txtArea.setDisable(true);

            actualizarEstado();

            modelo.setModificado(false);
        }
    }

    @FXML
    private void onSalir(ActionEvent e) {

        if(confirmarDescartarCambios()) {

            Platform.exit();
        }
    }

    private boolean confirmarDescartarCambios() {

        if (!modelo.isModificado()) {
            return true;
        }

        Alert dialogo = new Alert(
            Alert.AlertType.CONFIRMATION
        );
        dialogo.setTitle("Guardar cambios");
        dialogo.setHeaderText("El archivo ha sido modificado.");
        dialogo.setContentText("¿Desea guardar los cambios antes de continuar?");

        ButtonType btnGuardar = new ButtonType("Guardar");
        ButtonType btnDescartar = new ButtonType("Descartar");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialogo.getButtonTypes().setAll(btnGuardar, btnDescartar, btnCancelar);

        Optional<ButtonType> res = dialogo.showAndWait();

        if (res.isPresent()) {
            if (res.get() == btnGuardar) {
                guardarFisicamente();
                return !modelo.isModificado();
            } else if (res.get() == btnDescartar) {
                return true;
            }
        }
        return false;
    }

    private void actualizarEstado() {

        String textoRuta;

        textoRuta = modelo.tieneFichero()
                ? modelo.getRuta()
                : "Sin fichero";

        if (modelo.isModificado()) {
            textoRuta += " *";
        }

        lblEstado.setText(textoRuta);
    }

    private void mostrarError(String mensaje) {

        Alert alert = new Alert(
            Alert.AlertType.ERROR
        );

        alert.setTitle("Error");

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    @FXML
    private void onMayusculas(ActionEvent e) {

        String textoActual;

        String textoMayusculas;

        // Obtener texto

        textoActual = txtArea.getText();

        // Transformar

        textoMayusculas =
                textoActual.toUpperCase();

        // Actualizar vista

        txtArea.setText(textoMayusculas);
    }
}