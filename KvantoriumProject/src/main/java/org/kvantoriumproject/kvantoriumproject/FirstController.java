package org.kvantoriumproject.kvantoriumproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;

public class FirstController {
    private final String FILE_DANNYH = "habits.json";

    @FXML private TableView<Privychka> tablitsa;
    @FXML private TableColumn<Privychka, String> nazvanieKol;
    @FXML private TableColumn<Privychka, String> chastotaKol;
    @FXML private TableColumn<Privychka, LocalDate> dataNachalaKol;
    @FXML private TableColumn<Privychka, Boolean> statusKol;
    @FXML private CheckBox vklyuchitUvedomleniya;

    private ObservableList<Privychka> spisokPrivychek = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        nastroitTablitsu();
        zagruzitPrivychki();
    }

    private void nastroitTablitsu() {
        nazvanieKol.setCellValueFactory(new PropertyValueFactory<>("nazvanie"));
        chastotaKol.setCellValueFactory(new PropertyValueFactory<>("chastota"));
        dataNachalaKol.setCellValueFactory(new PropertyValueFactory<>("dataNachala"));
        statusKol.setCellValueFactory(new PropertyValueFactory<>("vypolneno"));

        tablitsa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablitsa.setItems(spisokPrivychek);
    }

    private void zagruzitPrivychki() {
        try {
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(FILE_DANNYH));
            for (Object obj : jsonArray) {
                JSONObject json = (JSONObject) obj;
                spisokPrivychek.add(new Privychka(
                        (String) json.get("nazvanie"),
                        (String) json.get("chastota"),
                        LocalDate.parse((String) json.get("dataNachala")),
                        (Boolean) json.get("vypolneno")
                ));
            }
        } catch (Exception e) {
            pokazatOshibku("Ошибка при загрузке данных");
        }
    }

    private void sohranitPrivychki() {
        try (FileWriter writer = new FileWriter(FILE_DANNYH)) {
            JSONArray jsonArray = new JSONArray();
            for (Privychka p : spisokPrivychek) {
                JSONObject json = new JSONObject();
                json.put("nazvanie", p.getNazvanie());
                json.put("chastota", p.getChastota());
                json.put("dataNachala", p.getDataNachala().toString());
                json.put("vypolneno", p.isVypolneno());
                jsonArray.add(json);
            }
            writer.write(jsonArray.toJSONString());
        } catch (Exception e) {
            pokazatOshibku("Ошибка при сохранении");
        }
    }

    @FXML
    private void dobavitPrivychku() {
        Dialog<Privychka> dialog = new Dialog<>();
        dialog.setTitle("Новая привычка");

        TextField poleNazvanie = new TextField();
        poleNazvanie.setPromptText("Бегать по утрам");
        TextField poleChastota = new TextField();
        poleChastota.setPromptText("Каждый день");
        DatePicker viborDaty = new DatePicker();

        dialog.getDialogPane().setContent(new GridPane() {{
            addRow(0, new Label("Название:"), poleNazvanie);
            addRow(1, new Label("Повтор:"), poleChastota);
            addRow(2, new Label("Начало:"), viborDaty);
            setHgap(10);
            setVgap(10);
        }});

        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Готово", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        dialog.setResultConverter(tipKnopki -> {
            if (tipKnopki.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new Privychka(
                        poleNazvanie.getText(),
                        poleChastota.getText(),
                        viborDaty.getValue(),
                        false
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(privychka -> {
            spisokPrivychek.add(privychka);
            sohranitPrivychki();
        });
    }

    @FXML
    private void udalitPrivychku() {
        Privychka vybrannaya = tablitsa.getSelectionModel().getSelectedItem();
        if (vybrannaya == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Точно удалить?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            spisokPrivychek.remove(vybrannaya);
            sohranitPrivychki();
        }
    }

    private void pokazatOshibku(String text) {
        new Alert(Alert.AlertType.ERROR, text).show();
    }
}