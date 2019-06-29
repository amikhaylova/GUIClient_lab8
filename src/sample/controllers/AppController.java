package sample.controllers;


import Client.Authorizer;
import Collection.CollectionWork;
import Enums.SocialStatus;
import ShortyClasses.Clothes;
import ShortyClasses.Coords;
import ShortyClasses.Money;
import ShortyClasses.Shorty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.Main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import static Client.TCPClient.*;

public class AppController {

    @FXML
    private TableView<Shorty> table;

    @FXML
    private TableColumn<Shorty, String> loginCol;

    @FXML
    private TableColumn<Shorty, String> nameCol;

    @FXML
    private TableColumn<Shorty, LocalDateTime> dateCol;

    @FXML
    private TableColumn<Shorty, Clothes> clothCol;

    @FXML
    private TableColumn<Shorty, SocialStatus> statusCol;

    @FXML
    private TableColumn<Shorty, Coords> coordsCol;

    @FXML
    private TableColumn<Shorty, Money> budgetCol;


    @FXML
    private Canvas canvas;

    @FXML
    private Button test;

    @FXML
    private ComboBox<String> command;

    @FXML
    private Button submitBut;

    @FXML
    private AnchorPane editPane;

    @FXML
    private Label userLogin;

    @FXML
    private Rectangle userColorSq;

    //private Color user_color;

    //private GraphicsContext gc = canvas.getGraphicsContext2D();



    void draw_coord_sys(GraphicsContext gc){
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(w/2, 0, w/2,h);
        gc.strokeLine(0, h/2, w,h/2);
        gc.strokeLine(0,0,w,0);
        gc.strokeLine(0,h,w,h);
        gc.strokeLine(0,0,0,h);
        gc.strokeLine(w,0,w,h);
    }

    void draw_shorty (Shorty shorty, Canvas canvas,GraphicsContext gc){

        double cx = canvas.getWidth()/2;
        double cy = canvas.getHeight()/2;
        double r = 30;
        double b_len = 40;

        gc.setStroke(Color.valueOf(CollectionWork.colors.get(shorty.getUser_login())));
        gc.setLineWidth(4);

        double x = shorty.getCoords().getX();
        double y = shorty.getCoords().getY();

        gc.strokeOval(cx-r/2+x,cy-r/2-y,r,r);
        gc.strokeLine(cx+x,cy+r/2-y, cx+x, cy+r/2+b_len-y);
        gc.strokeLine(cx+x,cy+r/2+b_len/2-y, cx+20+x, cy+r/2-y);
        gc.strokeLine(cx+x,cy+r/2+b_len/2-y, cx+80+x, cy+r/2-y);
        gc.strokeLine(cx+x, cy+r/2+b_len-y, cx+20+x, cy+r/2+65-y);
        gc.strokeLine(cx+x, cy+r/2+b_len-y, cx+80+x, cy+r/2+65-y);
    }

    void undraw_shorty (Canvas canvas,GraphicsContext gc){
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        draw_coord_sys(gc);
        for (Shorty sh: CollectionWork.obList){
            draw_shorty(sh, canvas, canvas.getGraphicsContext2D());
        }
    }

    @FXML
    void initialize() {
        //System.out.println("Дошел до сюда!");
        //заполняем таблицу коротышками
        loginCol.setCellValueFactory(new PropertyValueFactory<>("user_login"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timeOfCreation"));
        clothCol.setCellValueFactory(new PropertyValueFactory<>("look"));
        coordsCol.setCellValueFactory(new PropertyValueFactory<>("coords"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        budgetCol.setCellValueFactory(new PropertyValueFactory<>("budget"));

        //System.out.println("Перед заполнением");

        table.setItems(CollectionWork.init());

        //System.out.println("После заполнения");

        userLogin.setText(Authorizer.user_login);

        //заполняем comboBox
        command.getItems().removeAll(command.getItems());
        command.getItems().addAll("Добавить элемент", "Удалить элемент", "Удалить первый", "Добавить, если минимальный", "Информация о коллекции");

        //рисуем оси
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        draw_coord_sys(canvas.getGraphicsContext2D());

        //инициализируем цвет
        userColorSq.setFill(Color.valueOf(Authorizer.user_color));

        //рисуем коротышек
        for (Shorty sh: CollectionWork.obList){
            draw_shorty(sh, canvas, canvas.getGraphicsContext2D());
        }

        CollectionWork.obList.addListener(new ListChangeListener<Shorty>() {
            @Override
            public void onChanged(Change<? extends Shorty> c) {
                table.setItems(CollectionWork.obList);
            }
        });
        submitBut.setOnAction(event -> {
            Shorty shorty;
            String commandChoice = command.getValue();
            String commandForClient = "";
            String answerForClient = "";
            System.out.println("Выбранная команда: " + commandChoice);
            switch (commandChoice){
                case("Добавить элемент"):
                    commandForClient = "add";
                    break;
                case("Удалить элемент"):
                    commandForClient = "remove";
                    break;
                case("Удалить первый"):
                    commandForClient = "remove_first";
                    break;
                case("Добавить, если минимальный"):
                    commandForClient = "add_if_min";
                    break;
                case("Информация о коллекции"):
                    commandForClient = "info";
                    break;
            }
            try {
                write(oos1, commandForClient);
                System.out.println("Отправил команду " + commandChoice);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (commandChoice.equals("Добавить элемент") || commandChoice.equals("Удалить элемент") || commandChoice.equals("Добавить, если минимальный")){
                FXMLLoader loader = new FXMLLoader();
                //указываем местонахождение файла
                loader.setLocation(getClass().getResource("/sample/models/edit.fxml"));
                loader.setResources(ResourceBundle.getBundle("Bundles.Locale", Main.locale));
                //загружаем файл
                try {
                    loader.load();
                } catch (IOException e) {
                }
                //получаем полный путь к файлу, который необходио загрузить
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 580, 630));
                stage.setResizable(false);
                stage.showAndWait();
                shorty = EditShortyController.shorty;
                try {
                    oos2.writeObject(shorty);
                    System.out.println("Отправил коротышку " + shorty.toString());
                    answerForClient = ois.readUTF();
                    System.out.println("Получил ответ от сервера: " + answerForClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (answerForClient.equals("Коротышка добавлен в базу данных.")){
                    CollectionWork.obList.add(shorty);
                    draw_shorty(shorty,canvas, canvas.getGraphicsContext2D());
                }else if (answerForClient.equals("Коротышка удален из базы данных")){
                    CollectionWork.obList.remove(shorty);
                    undraw_shorty(canvas, canvas.getGraphicsContext2D());
                }

                Alert alert = new Alert(Alert.AlertType.NONE, answerForClient, ButtonType.CLOSE);
                alert.show();
                return;
            }
            try {
                answerForClient = ois.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
                if (answerForClient.equals("Первый коротышка удален из базы данных")){
                    CollectionWork.obList.remove(0);
                    undraw_shorty(canvas, canvas.getGraphicsContext2D());
                }
            Alert alert = new Alert(Alert.AlertType.NONE, answerForClient, ButtonType.CLOSE);
            alert.show();
        });


    }
    private static void write (DataOutputStream oos, String string) throws IOException{
        oos.writeUTF(string);
        oos.writeUTF(string);
        oos.writeUTF(string);
        oos.writeUTF(string);
        oos.writeUTF(string);
        oos.writeUTF(string);
    }



}