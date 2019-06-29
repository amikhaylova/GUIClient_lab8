package Client;


import sample.Main;

import java.io.*;
import java.net.*;

public class TCPClient {

    public static Socket socket = null;
    public static DataOutputStream oos1;
    public static ObjectOutputStream oos2;
    public static DataInputStream ois;
    public static ObjectInputStream ois1;

    public static void connect()throws InterruptedException{

        boolean isFinished = false;

        while (!isFinished) {
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(InetAddress.getByName(Main.hostName), Main.port), 10000);
                isFinished = true;
                oos1 = new DataOutputStream(TCPClient.socket.getOutputStream());
                oos2 = new ObjectOutputStream(oos1);
                ois = new DataInputStream(TCPClient.socket.getInputStream());
                ois1 = new ObjectInputStream(ois);
            } catch (SocketTimeoutException | SocketException e) {
                System.out.println("Клиент еще не подключился к серверу, но он пытается.");
                Thread.sleep(1000);
            } catch (UnknownHostException e){
                System.out.println("Произошла ошибка: " + e + ". Перезапустите приложение и укажите существующий сервер.");
                System.exit(0);
            } catch (IOException e){
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
        System.out.println("Я подключился!");
    }

}