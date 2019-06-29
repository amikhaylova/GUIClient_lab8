package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import Client.Authorizer;
import Client.TCPClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import sample.Main;

public class AuthController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField pasText;

    @FXML
    private Button authButton;

    @FXML
    private Button regButton;

    @FXML
    private ComboBox<String> languageCB;

    @FXML
    void initialize() {

        languageCB.getItems().removeAll(languageCB.getItems());
        languageCB.getItems().addAll(Main.loader.getResources().getString("ru"), Main.loader.getResources().getString("en"));

        languageCB.setOnAction(event ->{
            System.out.println("Я тут");
            String language = languageCB.getValue();
            if (language.equals("Русский")){
                Main.locale = new Locale ("ru");
            }else if (language.equals("Английский")){
                Main.locale = new Locale("en");
            }else if (language.equals("Russian")){
                Main.locale = new Locale("ru");
            }else if (language.equals("English")){
                Main.locale = new Locale("en");
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AuthController.class.getResource("/sample/models/auth.fxml"));
            loader.setResources(ResourceBundle.getBundle("Bundles.Locale", Main.locale));
            Parent root = null;
            try {
                //System.out.println(loader.getLocation());
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("The best application ever");
            stage.setScene(new Scene(root, 680, 440));
            stage.setResizable(false);
            languageCB.getScene().getWindow().hide();
            stage.show();
        });

        authButton.setOnAction(event -> {
            String login = loginText.getText().trim();
            String password = pasText.getText().trim();

            if (!login.equals("") && !password.equals("")){
                authUser(login,password);
            }else{
                Alert alert = new Alert(Alert.AlertType.NONE, "Поля логин и(или) пароль не заполнены.", ButtonType.CLOSE);
                //alert.setTitle("Предупреждение");
                alert.show();
            }
        });

        regButton.setOnAction(event -> {
            //прячем старое окно
            regButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            //указываем местонахождение файла
            loader.setLocation(getClass().getResource("/sample/models/reg.fxml"));
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

    private void authUser(String login, String password) {
        String serverAnswer;
        try {
            serverAnswer = Authorizer.authorize(login,password);
            if  (serverAnswer.equals("Данного логина не существует")) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Введенного логина не существует", ButtonType.CLOSE);
                alert.show();
            }else if (serverAnswer.equals("Введенный пароль неверен")){
                Alert alert = new Alert(Alert.AlertType.NONE, "Введенный пароль неверен", ButtonType.CLOSE);
                alert.show();
            }else if (serverAnswer.equals("Авторизация прошла успешно")){
                //прячем старое окно
                regButton.getScene().getWindow().hide();

                FXMLLoader loader = new FXMLLoader();
                //указываем местонахождение файла
                loader.setLocation(getClass().getResource("/sample/models/app.fxml"));
                loader.setResources(ResourceBundle.getBundle("Bundles.Locale", Main.locale));
                //загружаем файл
                Parent root = null;
                try {
                   root= loader.load();
                } catch (IOException e) {
                }
                //получаем полный путь к файлу, который необходио загрузить
                //Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1185, 780));
                stage.setResizable(false);
                stage.show();
            }else if (serverAnswer.equals("failed")){
                Alert alert = new Alert(Alert.AlertType.NONE, "Что-то пошло не так", ButtonType.CLOSE);
                alert.show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
