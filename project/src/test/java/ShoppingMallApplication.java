

public class ShoppingMallApplication {
    public static void main(String[] args) {
        HuaWei huaWei = new HuaWei(1200);
        huaWei.setDiscount(new Discount(0.9));


    }
}

class Shopper {

    private Integer cost;

    public void buy() {

    }


}

interface IDiscount {
    public double cost(int price);
}

class Discount implements IDiscount {

    double rate;

    public Discount(double rate) {
        this.rate = rate;
    }

    @Override
    public double cost(int price) {
        return price * rate;
    }
}

abstract class Goods {
    public String name;
    public Integer price;
    IDiscount discount;

    public double getCost() {
        if (discount != null) {
            return discount.cost(price);
        }
        return price;
    }
}

class HuaWei extends Goods {

    public HuaWei(Integer price) {
        this.name = "HuaWei";
        this.price = price;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

}

