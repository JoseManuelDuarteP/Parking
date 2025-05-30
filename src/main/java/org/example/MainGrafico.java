package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainGrafico extends Application {

    static List<Vehiculo> vehiculos = new ArrayList<>();
    static List<Estancia> estanciasActuales = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        Vehiculo v = new Vehiculo("AAA", TipoVehiculo.OFICIAL);
        vehiculos.add(v);
        v = new Vehiculo("BBB", TipoVehiculo.RESIDENTE);
        vehiculos.add(v);
        v = new Vehiculo("CCC", TipoVehiculo.RESIDENTE);
        vehiculos.add(v);

        Label titulo = new Label("Gestión de Parking");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 35; -fx-text-fill: RED;");

        Button regEnt = new Button("Registrar entrada");
        regEnt.setOnAction(e -> registrarEntrada());
        Button regSal = new Button("Registrar salida");
        regSal.setOnAction(e -> registrarSalida());
        Button darAltOfi = new Button("Dar de alta coche oficial");
        darAltOfi.setOnAction(e -> darAltOfi());
        Button darAltRes = new Button("Dar de alta coche residente");
        darAltRes.setOnAction(e -> darAltRes());
        Button empezarMes = new Button("Empezar mes");
        empezarMes.setOnAction(e -> empezarMes());
        Button generarInforme = new Button("Generar informe");
        generarInforme.setOnAction(e -> generarInforme());
        Button verParking = new Button("Ver parking");
        verParking.setOnAction(e -> verParking());
        Button salir = new Button("Salir");
        salir.setOnAction(e -> primaryStage.close());

        VBox botones = new VBox(10, regEnt, regSal, darAltOfi, darAltRes
        , empezarMes, generarInforme, verParking, salir);
        botones.setStyle("-fx-alignment: center;");
        botones.setFillWidth(true);

        for (Node b : botones.getChildren()) {
            b.setStyle("-fx-pref-width: 180; -fx-pref-height: 30;");
            VBox.setMargin(b, new Insets(0, 0, 0, 30));
        }
        salir.setStyle("-fx-pref-width: 180; -fx-pref-height: 30;-fx-font-weight: bold");

        VBox clima = verTiempo();

        BorderPane root = new BorderPane();
        root.setTop(titulo);
        root.setLeft(botones);
        root.setRight(clima);
        BorderPane.setAlignment(titulo, Pos.CENTER);
        BorderPane.setAlignment(clima, Pos.CENTER);

        Image fondo = new Image(getClass().getResource("/parking.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                fondo,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false));
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root, 900, 534);
        primaryStage.setTitle("Parking");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void registrarEntrada() {
        Stage ventanaEntrada = new Stage();
        ventanaEntrada.setTitle("Registrar entrada");

        CheckBox checkBox = new CheckBox("¿Se trata de un vehiculo no propietario?");

        TextField textMatricula = new TextField();
        textMatricula.setPromptText("Ingrese matrícula");

        Button registrarEntrada = new Button("Registrar entrada");

        registrarEntrada.setOnAction(e -> {
            String matricula = textMatricula.getText();

            if (matricula.isEmpty()) {
                mostrarAlerta("Error", "Matrícula vacía");
                return;
            }

            if (checkBox.isSelected()) {
                Vehiculo v = new Vehiculo(matricula, TipoVehiculo.NO_RESIDENTE);
                vehiculos.add(v);

                Estancia es = new Estancia(v);
                estanciasActuales.add(es);
                mostrarAlerta("Éxito", "Estancia registrada exitosamente");

            } else {
                Optional<Vehiculo> vehiculoAct = vehiculos.stream()
                        .filter(v -> v.getMatricula().equals(matricula)).findFirst();

                if (vehiculoAct.isPresent()) {
                    Estancia es = new Estancia(vehiculoAct.get());
                    estanciasActuales.add(es);
                    mostrarAlerta("Éxito", "Estancia registrada exitosamente");
                } else {
                    mostrarAlerta("Error", "El vehiculo no existe");
                }
            }

            textMatricula.clear();
        });

        VBox layout = new VBox(10, checkBox, textMatricula, registrarEntrada);
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 350, 200);

        ventanaEntrada.setScene(scene);
        ventanaEntrada.show();
    }

    private static void registrarSalida() {
        LocalDateTime fechaSalida = LocalDateTime.now();

        Stage ventanaSalida = new Stage();
        ventanaSalida.setTitle("Registrar salida");

        TextField textMatricula = new TextField();
        textMatricula.setPromptText("Ingrese matrícula: ");

        Button registrarSalida = new Button("Registrar salida");

        registrarSalida.setOnAction(e -> {
            String matricula = textMatricula.getText();

            if (matricula.isEmpty()) {
                mostrarAlerta("Error", "Matrícula vacía");
                return;
            }

            Optional<Estancia> estanciaAct = estanciasActuales.stream()
                    .filter(p -> p.getVehiculo().getMatricula().equals(matricula)).findFirst();

            if (estanciaAct.isPresent()) {
                Optional<Vehiculo> vehiculoAct = vehiculos.stream()
                        .filter(p -> p.getMatricula().equals(matricula)).findFirst();

                estanciaAct.get().setFechaSalida(fechaSalida);
                estanciaAct.get().cobro();

                if (vehiculoAct.get().getTipo().equals(TipoVehiculo.NO_RESIDENTE)) {
                    mostrarAlerta("Éxito", "Salida registrada exitosamente"
                    + "\nImporte: " + estanciaAct.get().getImporte() + "€");
                    vehiculos.remove(vehiculoAct.get());
                    estanciasActuales.remove(estanciaAct.get());
                    return;
                }

                vehiculoAct.get().addEstancia(estanciaAct.get());
                estanciasActuales.remove(estanciaAct.get());
                mostrarAlerta("Éxito", "Salida registrada exitosamente");

            } else {
                mostrarAlerta("Error", "El vehiculo no existe o no se encuntra aparcado");
            }
        });

        VBox layout = new VBox(10, textMatricula, registrarSalida);
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 350, 200);

        ventanaSalida.setScene(scene);
        ventanaSalida.show();
    }

    private static void darAltOfi() {
        Stage ventanaAltOfi = new Stage();
        ventanaAltOfi.setTitle("Dar de alta coche oficial");

        TextField textMatricula = new TextField();
        textMatricula.setPromptText("Introduzca la matrícula");

        Button registrarAlta = new Button("Registrar alta");

        registrarAlta.setOnAction(e -> {
            String matricula = textMatricula.getText();

            if (matricula.isEmpty()) {
                mostrarAlerta("Error", "Matrícula vacía");
                return;
            }

            Vehiculo v = new Vehiculo(matricula, TipoVehiculo.OFICIAL);
            vehiculos.add(v);

            mostrarAlerta("Éxito", "Vehiculo oficial registrado exitosamente");
        });

        VBox layout = new VBox(10, textMatricula, registrarAlta);
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 350, 200);

        ventanaAltOfi.setScene(scene);
        ventanaAltOfi.show();
    }

    private static void darAltRes() {
        Stage ventanaAltRes = new Stage();
        ventanaAltRes.setTitle("Dar de alta coche residente");

        TextField textMatricula = new TextField();
        textMatricula.setPromptText("Introduzca la matrícula");

        Button registrarAlta = new Button("Registrar alta");

        registrarAlta.setOnAction(e -> {
            String matricula = textMatricula.getText();

            if (matricula.isEmpty()) {
                mostrarAlerta("Error", "Matrícula vacía");
                return;
            }

            Vehiculo v = new Vehiculo(matricula, TipoVehiculo.RESIDENTE);
            vehiculos.add(v);

            mostrarAlerta("Éxito", "Vehiculo residente registrado exitosamente");
        });

        VBox layout = new VBox(10, textMatricula, registrarAlta);
        layout.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(layout, 350, 200);

        ventanaAltRes.setScene(scene);
        ventanaAltRes.show();
    }

    private static void empezarMes() {
        Stage ventanaMes = new Stage();
        ventanaMes.setTitle("Empezar mes");

        Label label = new Label("¿Está seguro?");
        Button si = new Button("Sí");
        Button no = new Button("No");

        si.setOnAction(e -> {
            for (Vehiculo v : vehiculos) {
                v.limpiarEstancias();
            }
            ventanaMes.close();
        });

        no.setOnAction(e -> {
            ventanaMes.close();
        });

        HBox botones = new HBox(10, si, no);
        botones.setStyle("-fx-padding: 20; -fx-alignment: center;");

        BorderPane layout = new BorderPane();
        BorderPane.setAlignment(label, Pos.CENTER);
        layout.setTop(label);
        layout.setBottom(botones);
        Scene scene = new Scene(layout, 350, 75);

        ventanaMes.setScene(scene);
        ventanaMes.show();
    }

    private static void generarInforme() {
        Stage ventanaInforme = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Fichero de informe");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt")
        );

        File informe = fileChooser.showSaveDialog(ventanaInforme);

        if (informe != null) {
            try {
                FileWriter fw = new FileWriter(informe);
                fw.write("Matrícula:    Tiempo estacionado (min.):    Cantidad a pagar:\n");
                List<Vehiculo> vehiculosRes = vehiculos.stream()
                        .filter(p -> p.getTipo().equals(TipoVehiculo.RESIDENTE)).toList();

                for (Vehiculo v : vehiculosRes) {
                    fw.write(v.getMatricula() + "           " + v.tiempoTotal() + "                             " + v.pagoTotal() + "\n");
                }

                fw.write("\nAVISO: ESTE INFORME NO TIENE EN CUENTA LA ESTANCIA ACTUAL EN CASO DE HABERLA");

                fw.flush();
                fw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void verParking() {
        int columnas = 5;
        int fila;
        int columna;
        int totalPlazas= 20;

        Stage ventanaParking = new Stage();
        ventanaParking.setTitle("Parking");

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);

        for (int i = 0; i < totalPlazas; i++) {
            StackPane plaza = crearPlaza(i, estanciasActuales);
            fila = i / columnas;
            columna = i % columnas;
            layout.add(plaza, columna, fila);
        }

        Label consejo = new Label("Consejo: Poner el ratón encima de la plaza porporciona información del coche");

        VBox contenedor = new VBox(10, layout, consejo);
        contenedor.setAlignment(Pos.CENTER);

        Scene scene = new Scene(contenedor, 600, 400);
        ventanaParking.setScene(scene);
        ventanaParking.show();
    }

    private static StackPane crearPlaza(int index, List<Estancia> estancias) {
        Rectangle rect = new Rectangle(100, 60);
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.LIGHTGREEN);

        Label label;
        StackPane stackPane;

        if (index < estancias.size()) {
            String matricula = estancias.get(index).getVehiculo().getMatricula();
            label = new Label(matricula);
            rect.setFill(Color.LIGHTSALMON);

            stackPane = new StackPane(rect, label);

            Vehiculo vehiculo = estancias.get(index).getVehiculo();
            Tooltip tooltip = new Tooltip(vehiculo.toString());
            tooltip.setStyle("-fx-background-color: #fcf3cf; -fx-text-fill: black; -fx-font-weight: bold;");
            Tooltip.install(stackPane, tooltip);

        } else {
            label = new Label("Libre");
            stackPane = new StackPane(rect, label);
        }
        label.setStyle("-fx-font-weight: bold;");

        return stackPane;
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private static VBox verTiempo() throws MalformedURLException {
        String token = "252ae1fd9f2285dbfc1a113d113462ce";
        String loca = "Valencia";
        String urlStr = "http://api.weatherstack.com/current?access_key=" + token + "&query=" + loca;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject current = json.getAsJsonObject("current");

            int temp = current.get("temperature").getAsInt();

            JsonArray descriptions = current.getAsJsonArray("weather_descriptions");
            String desc = descriptions.get(0).getAsString();

            JsonArray icons = current.getAsJsonArray("weather_icons");
            String iconUrl = icons.get(0).getAsString();

            JsonObject location = json.getAsJsonObject("location");
            String ciudad = location.get("name").getAsString();

            javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(iconUrl);
            icon.setFitHeight(50);
            icon.setFitWidth(50);
            Label l1 = new Label(ciudad);
            Label l2 = new Label("Temperatura: " + temp + "°C");
            Label l3 = new Label("Descripción: " + desc);

            VBox layout = new VBox(10, icon, l1, l2, l3);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-padding: 20; -fx-background-color: white; " +
                    "-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-radius: 5;");
            layout.setMaxHeight(200);
            return layout;

        } catch (Exception ex) {
            VBox errorBox = new VBox(new Label("Error: " + ex.getMessage()));
            errorBox.setAlignment(Pos.CENTER);
            return errorBox;
        }

    }

}
