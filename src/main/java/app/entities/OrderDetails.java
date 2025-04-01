package app.entities;

public class OrderDetails {
    int id;
    int order_nr;
    String topping;
    String bottom;
    int price;

    public OrderDetails(int id, int order_nr, String topping, String bottom) {
        this.id = id;
        this.order_nr = order_nr;
        this.topping = topping;
        this.bottom = bottom;
        this.price = price;
    }

    public OrderDetails(int order_nr, String bottom, String topping) {
        this.bottom = bottom;
        this.topping = topping;
        this.order_nr = order_nr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_nr() {
        return order_nr;
    }

    public void setOrder_nr(int order_nr) {
        this.order_nr = order_nr;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }
}
