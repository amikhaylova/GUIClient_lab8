package sample;

import Client.TCPClient;
import com.sun.deploy.util.ArrayUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static int port = -1;
    public static String hostName = "";
    public static FXMLLoader loader;
    public static Locale locale = new Locale("ru");

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("models/auth.fxml"));
        loader.setResources(ResourceBundle.getBundle("Bundles.Locale", locale));
        Parent root = loader.load();
        primaryStage.setTitle("The best application ever");
        primaryStage.setScene(new Scene(root, 680, 440));
        primaryStage.setResizable(false);
        primaryStage.show();
    }




    public static void main(String[] args) {
        port = Integer.parseInt(args[0]);
        hostName = args[1];
        try {
            TCPClient.connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
