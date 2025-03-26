package app.entities;

public class Cupcake {
    private Topping topping;
    private Bottom bottom;
    private double price;

    Cupcake(Topping topping, Bottom bottom){
        this.topping = topping;
        this.bottom = bottom;
        price = topping.getPrice() + bottom.getPrice();
    }

    public double getPrice() {
        return price;
    }

    public Topping getTopping() {
        return topping;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
    }

    public void setBottom(Bottom bottom) {
        this.bottom = bottom;
    }
}