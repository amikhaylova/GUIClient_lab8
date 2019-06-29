package Collection;

import Enums.Currency;
import Enums.SocialStatus;
import ShortyClasses.Coords;
import ShortyClasses.Shorty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CollectionWork {

    public static ArrayList<Shorty> list = new ArrayList<>();
    public static ObservableList<Shorty> obList;
    public static Map<String, String> colors = new HashMap<>();

    public static void fillCollection (Vector<Shorty> vector){
        list = new ArrayList<>(vector);
    }

    public static ObservableList<Shorty> init(){
        obList = FXCollections.observableList(list);
        return obList;
    }


}
