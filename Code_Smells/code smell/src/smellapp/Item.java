package smellapp;

class Item {
    private String name;
    private double price;
    private int quantity;
    private DiscountType discountType;
    private double discountAmount;

    public Item(String name, double price, int quantity) { 
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discountType = null;
        this.discountAmount = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    //setter to set the discount type.
    public void setDiscountType(DiscountType discountType){
        this.discountType = discountType;
    }

    //setter to set the discount amount.
    public void setDiscountAmount(double discountAmount){
        this.discountAmount = discountAmount;
    }


}

