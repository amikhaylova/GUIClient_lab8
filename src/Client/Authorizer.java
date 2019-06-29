package Client;

import Collection.CollectionWork;
import ShortyClasses.Shorty;

import java.io.*;
import java.util.Map;
import java.util.Vector;

import static Client.TCPClient.*;

public class Authorizer {

    static String state = "";
    static String serverAnswer = "";
    public static String user_login;
    public static String user_color;

    public static String registrate(String login, String pas1, String email){
        try{String color = RandomColor.getRandomColor();
            state = "registration";
            write(oos1, state);
            write(oos1, login);
            write(oos1, pas1);
            write(oos1, email);
            write(oos1, color);
            serverAnswer = ois.readUTF();
            if (serverAnswer.equals("Не отправлено") || serverAnswer.equals("Регистрация прошла успешно")){
                CollectionWork.colors.put(login, color);
            }
            return serverAnswer;
        }catch(IOException e){
            return  serverAnswer;
        }
    }

    public static String authorize (String login, String password)throws InterruptedException{
        try{
            state = "authorization";
            write(oos1, state);
            Thread.sleep(1000);
            write(oos1,login);
            serverAnswer = ois.readUTF();
            if (serverAnswer.equals("1")){
                write(oos1,password);
                serverAnswer = ois.readUTF();
                if (serverAnswer.equals("Авторизация прошла успешно")){
                    user_login = login;
                    CollectionWork.fillCollection((Vector<Shorty>)ois1.readObject());
                    CollectionWork.colors.putAll((Map<String,String>)ois1.readObject());
                    user_color = CollectionWork.colors.get(user_login);
                }
                return serverAnswer;
            }
                return serverAnswer;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "failed";
        } catch (IOException e) {
            e.printStackTrace();
            return "failed";
        }
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
