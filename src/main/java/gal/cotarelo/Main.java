package gal.cotarelo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import gal.cotarelo.model.FileModel;
import gal.cotarelo.controller.FxmlController;

public class Main extends Application {

    @Override
    public void start(Stage stage)
            throws Exception {

        FXMLLoader loader;

        Parent root;

        FileModel modelo;

        FxmlController controller;

        Scene scene;

        // Cargar FXML

        loader = new FXMLLoader(
            getClass().getResource(
                "/gal/cotarelo/view/FxmlVista.fxml"
            )
        );

        root = loader.load();

        // Modelo

        modelo = new FileModel();

        // Controlador

        controller = loader.getController();

        controller.setModelo(modelo, stage);

        // Escena

        scene = new Scene(root, 800, 600);

        stage.setScene(scene);

        stage.setTitle("Editor MVC");

        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}