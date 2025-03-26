package app.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    List<Cupcake> cupcakes;

    public Order(List<Cupcake> cupcakes, int order_nr, User user, LocalDate localDate){
        this.cupcakes = new ArrayList<>();
    }


}





