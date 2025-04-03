package app.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Cupcake> cupcakes;
    private int order_nr;
    private String email;
    private LocalDate localDate;
public Order(int order_nr){
    this.order_nr = order_nr;
}
    public Order(List<Cupcake> cupcakes, int order_nr, String email, LocalDate localDate){
        this.cupcakes = cupcakes;
        this.order_nr = order_nr;
        this.email = email;
        this.localDate = localDate;
    }
    public Order(int order_nr, String email, LocalDate localDate){
        this.cupcakes = new ArrayList<>();
        this.order_nr = order_nr;
        this.email = email;
        this.localDate = localDate;
    }

    public Order(String email,LocalDate localDate) {
        this.localDate = localDate;
        this.email = email;
        this.cupcakes = new ArrayList<>();
    }

    public double getCupcakePrice(){
        double price = 0;
        for (Cupcake cupcake : cupcakes)
            price += cupcake.getPrice();
        return price;
    }

    public List<Cupcake> getCupcakes() {
        return cupcakes;
    }

    public void setCupcakes(List<Cupcake> cupcakes) {
        this.cupcakes = cupcakes;
    }

    public int getOrder_nr() {
        return order_nr;
    }

    public void setOrder_nr(int order_nr) {
        this.order_nr = order_nr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}


