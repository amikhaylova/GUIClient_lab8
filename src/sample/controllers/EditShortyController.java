package sample.controllers;

import Client.Authorizer;
import Enums.ClothesTypes;
import Enums.Colour;
import Enums.Currency;
import Enums.SocialStatus;
import ShortyClasses.Clothes;
import ShortyClasses.Coords;
import ShortyClasses.Money;
import ShortyClasses.Shorty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditShortyController {

    @FXML
    public Button backButton;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> clothesTypeCB;

    @FXML
    private ComboBox<String> clothesColorCB;

    @FXML
    private ComboBox<String> statusCB;

    @FXML
    private TextField coordXCB;

    @FXML
    private TextField coordYCB;

    @FXML
    private TextField moneyAmountCB;

    @FXML
    private ComboBox<String> currencyCB;

    String name;
    String clothesType;
    String clothesColor;
    String status;
    String coordX;
    String coordY;
    String amount;
    String currency;

    public static Shorty shorty = null;

    @FXML
    void initialize() {
        //заполняем comboBox
        clothesTypeCB.getItems().removeAll(clothesTypeCB.getItems());
        clothesTypeCB.getItems().addAll("шляпа", "футболка", "джинсы","нет одежды");
        clothesColorCB.getItems().removeAll(clothesColorCB.getItems());
        clothesColorCB.getItems().addAll("голубой", "зеленый", "красный", "нет цвета");
        statusCB.getItems().removeAll(statusCB.getItems());
        statusCB.getItems().addAll("тунеядец", "полицейский", "зэк", "художник", "доктор", "втшник", "неопределившийся");
        currencyCB.getItems().removeAll(currencyCB.getItems());
        currencyCB.getItems().addAll("сантик", "рубль", "доллар");



        backButton.setOnAction(event -> {
            if (!nameField.equals("") && !clothesTypeCB.equals("") && !clothesColorCB.equals("") && !statusCB.equals("")
            && !coordXCB.equals("") && !coordYCB.equals("") && !moneyAmountCB.equals("") && !currencyCB.equals("")){
                shorty = new Shorty();
                shorty.setName(nameField.getText());
                shorty.setCoords(new Coords(Double.parseDouble(coordXCB.getText()),Double.parseDouble(coordYCB.getText())));
                Clothes clothes = new Clothes(Colour.get(clothesColorCB.getValue()),ClothesTypes.get(clothesTypeCB.getValue()));
                shorty.setLook(clothes);
                shorty.setStatus(SocialStatus.get(statusCB.getValue()));
                Money money = new Money(Currency.get(currencyCB.getValue()),Integer.parseInt(moneyAmountCB.getText()));
                shorty.setBudget(money);
                shorty.setUser_login(Authorizer.user_login);
                System.out.println(shorty.getUser_login());
            }else{
                Alert alert = new Alert(Alert.AlertType.NONE, "Пожалуйста, заполните все поля", ButtonType.CLOSE);
                alert.show();
            }

            backButton.getScene().getWindow().hide();
        });


    }

    public static Shorty getShorty(){
        Shorty replyShorty = shorty;
        if (!(shorty == null)){
            shorty = null;
        }
        return replyShorty;
    }

}