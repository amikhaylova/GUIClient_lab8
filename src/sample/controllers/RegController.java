package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.ResourceBundle;

import static Client.Authorizer.registrate;

public class RegController {

    @FXML
    private TextField logText;

    @FXML
    private Button getPasButton;

    @FXML
    private Button backButton;

    @FXML
    private PasswordField pas1Field;

    @FXML
    private PasswordField pas2Field;

    @FXML
    private TextField emailButton;

    String login;
    String pas1;
    String pas2;
    String email;

    @FXML
    void initialize() {
        getPasButton.setOnAction(event -> {
            login = logText.getText().trim();
            pas1 = pas1Field.getText().trim();
            pas2 = pas2Field.getText().trim();
            email = emailButton.getText().trim();

            if(!login.equals("") && !pas1.equals("") && !pas2.equals("") && !email.equals("")){
                if (pas1.equals(pas2)){
                    regUser (login, pas1, email);
                }else {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Введенные пароли не совпадают", ButtonType.CLOSE);
                    alert.show();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.NONE, "Пожалуйста, заполните все поля", ButtonType.CLOSE);
                alert.show();
            }
        });

        backButton.setOnAction(event -> {
            //прячем старое окно
            backButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            //указываем местонахождение файла
            loader.setLocation(getClass().getResource("/sample/models/auth.fxml"));
            loader.setResources(ResourceBundle.getBundle("Bundles.Locale", Main.locale));

            //загружаем файл
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //получаем полный путь к файлу, который необходио загрузить
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 680, 440));
            stage.setResizable(false);
            stage.show();
        });
    }

    private void regUser(String login, String pas1, String email) {
        String serverAnswer;
        serverAnswer = registrate(login, pas1, email);
        if (serverAnswer.equals("Данный логин занят")){
            Alert alert = new Alert(Alert.AlertType.NONE, "Данный логин занят", ButtonType.CLOSE);
            alert.show();
            logText.clear();
        }else if (serverAnswer.equals("Не отправлено")){
            Alert alert = new Alert(Alert.AlertType.NONE, "Регистрация прошла успешно, но письмо мы вам не отправили", ButtonType.CLOSE);
            alert.show();
        }else if (serverAnswer.equals("Регистрация прошла успешно")){
            Alert alert = new Alert(Alert.AlertType.NONE, "Регистрация прошла успешно", ButtonType.CLOSE);
            alert.show();
            emailButton.clear();
            logText.clear();
            pas1Field.clear();
            pas2Field.clear();
        }
    }

}