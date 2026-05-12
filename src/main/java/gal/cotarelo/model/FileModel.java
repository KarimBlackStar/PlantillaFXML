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