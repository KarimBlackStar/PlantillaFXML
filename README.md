# PlantillaFXML
# MENÚS Y PROPIEDADES EN JAVAFX (MVC)

---

# CONCEPTOS PREVIOS

## PROPIEDADES

En JavaFX no usamos variables normales para la interfaz. Usamos propiedades.

### ¿Qué es una propiedad?
Es una variable inteligente que puede avisar cuando cambia su valor.

```java
StringProperty nombre = new SimpleStringProperty("");
BooleanProperty activo = new SimpleBooleanProperty(false);
```

---

## TIPOS IMPORTANTES

### Interfaces
```java
StringProperty
BooleanProperty
```

### Implementaciones
```java
SimpleStringProperty
SimpleBooleanProperty
```

---

## BINDING

Permite conectar propiedades del modelo con componentes visuales.

```java
txtArea.textProperty().addListener(...)
```

Si cambia el TextArea → cambia el modelo automáticamente.

---

# LISTENERS

```java
txtArea.textProperty().addListener((obs, viejo, nuevo) -> {

});
```

## Conceptos
- `obs` → propiedad observada
- `viejo` → valor anterior
- `nuevo` → valor nuevo

---

# FILECHOOSER

## Abrir archivos

```java
FileChooser selector = new FileChooser();

File fichero = selector.showOpenDialog(stage);
```

---

# LEER ARCHIVOS

```java
texto = Files.readString(
    fichero.toPath(),
    StandardCharsets.UTF_8
);
```

---

# CERRAR APP

```java
Platform.exit();
```

---

# ALERTAS

```java
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
```

---

# OPTIONAL

```java
Optional<ButtonType> res;
```

Sirve porque el usuario puede:
- aceptar
- cancelar
- cerrar ventana

---

# OPERADOR TERNARIO

```java
texto = condicion ? "Sí" : "No";
```

---

# FLUJO MVC

```txt
FXML → Vista
Controller → Lógica
Model → Datos
```

---

# FLUJO GENERAL

```txt
1. Inyección (@FXML)
2. Vinculación (setModelo)
3. Listener observa cambios
4. Eventos @FXML
5. Alert y FileChooser
```

---

# MODELO: FileModel.java

```java
package gal.cotarelo.model;

import javafx.beans.property.*;

public class FileModel {

    private final StringProperty contenido =
            new SimpleStringProperty("");

    private final StringProperty ruta =
            new SimpleStringProperty(null);

    private final BooleanProperty modificado =
            new SimpleBooleanProperty(false);

    // CONTENIDO

    public StringProperty contenidoProperty() {
        return contenido;
    }

    public String getContenido() {
        return contenido.get();
    }

    public void setContenido(String v) {
        contenido.set(v);
    }

    // RUTA

    public StringProperty rutaProperty() {
        return ruta;
    }

    public String getRuta() {
        return ruta.get();
    }

    public void setRuta(String v) {
        ruta.set(v);
    }

    // MODIFICADO

    public BooleanProperty modificadoProperty() {
        return modificado;
    }

    public boolean isModificado() {
        return modificado.get();
    }

    public void setModificado(boolean v) {
        modificado.set(v);
    }

    // EXISTE FICHERO

    public boolean tieneFichero() {

        return ruta.get() != null;
    }
}
```

---

# VISTA: FxmlVista.fxml

## BorderPane

```xml
<BorderPane>
</BorderPane>
```

---

# ZONAS DEL BORDERPANE

```txt
TOP
BOTTOM
LEFT
RIGHT
CENTER
```

---

# MENU BAR

```xml
<MenuBar>
</MenuBar>
```

---

# MENU

```xml
<Menu text="Archivo">
</Menu>
```

---

# MENUITEM

```xml
<MenuItem
    text="Abrir"
    onAction="#onAbrir"/>
```

---

# TEXTAREA

```xml
<TextArea fx:id="txtArea"/>
```

---

# LABEL

```xml
<Label fx:id="lblEstado"/>
```

---

# CONTROLADOR EN FXML

```xml
fx:controller="gal.cotarelo.controller.FxmlController"
```

---

# CONTROLADOR: FxmlController.java

```java
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
}
```

---

# NUEVO DOCUMENTO

```java
@FXML
private void onNuevo(ActionEvent e) {

    if(confirmarDescartarCambios()) {

        modelo.setContenido("");

        modelo.setRuta(null);

        modelo.setModificado(false);

        txtArea.clear();

        txtArea.setDisable(false);

        txtArea.requestFocus();

        actualizarEstado();
    }
}
```

---

# ABRIR ARCHIVO

```java
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

                modelo.setModificado(false);

                txtArea.setText(texto);

                txtArea.setDisable(false);

                actualizarEstado();

            } catch(IOException ex) {

                mostrarError(ex.getMessage());
            }
        }
    }
}
```

---

# GUARDAR

```java
@FXML
private void onGuardar(ActionEvent e) {

    guardarFisicamente();
}
```

---

# CERRAR DOCUMENTO

```java
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
```

---

# SALIR

```java
@FXML
private void onSalir(ActionEvent e) {

    if(confirmarDescartarCambios()) {

        Platform.exit();
    }
}
```

---

# GUARDAR FÍSICAMENTE

```java
Files.write(
    fichero.toPath(),
    modelo.getContenido().getBytes(
        StandardCharsets.UTF_8
    )
);
```

---

# CONFIRMAR CAMBIOS

```java
private boolean confirmarDescartarCambios() {

    Alert dialogo;

    Optional<ButtonType> res;

    dialogo = new Alert(
        Alert.AlertType.CONFIRMATION
    );

    res = dialogo.showAndWait();

    return true;
}
```

---

# ACTUALIZAR ESTADO

```java
private void actualizarEstado() {

    String textoRuta;

    textoRuta = modelo.tieneFichero()
            ? modelo.getRuta()
            : "Sin fichero";

    lblEstado.setText(textoRuta);
}
```

---

# MOSTRAR ERROR

```java
private void mostrarError(String mensaje) {

    Alert alert = new Alert(
        Alert.AlertType.ERROR
    );

    alert.setTitle("Error");

    alert.setHeaderText(null);

    alert.setContentText(mensaje);

    alert.showAndWait();
}
```

---

# MAIN.JAVA

```java
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

        scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Editor MVC");

        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
```

---

# PRÁCTICA MAYÚSCULAS

## FXML

```xml
<Menu text="Editar">

    <MenuItem
        text="Pasar a Mayúsculas"
        onAction="#onMayusculas"/>

</Menu>
```

---

# HANDLER MAYÚSCULAS

```java
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
```

---

# IMPORTS IMPORTANTES

```java
import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.beans.property.*;

import javafx.event.ActionEvent;
```

---

# CONCEPTOS MUY PREGUNTABLES

## @FXML
Conecta FXML con Java.

---

## Listener
Detecta cambios automáticamente.

---

## Binding
Sincroniza modelo y vista.

---

## FileChooser
Ventana para seleccionar archivos.

---

## Alert
Ventana emergente.

---

## Platform.exit()
Cierra correctamente JavaFX.

---

## MVC

```txt
MODEL → Datos
VIEW → Interfaz
CONTROLLER → Lógica
```
