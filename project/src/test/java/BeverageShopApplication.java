public class BeverageShopApplication {
    public static void main(String[] args) {

    }
}

class Beverage{
    private String name;
    private double price;

    public Beverage(String name, double price){
        this.name = name;
        this.price = price;
    }
    public String getName(){
        return name;

    }
    public double getPrice(){
        return price;
    }

}

class RunTian extends Beverage{
    public RunTian() {
        super("润田矿泉水", 2);
    }
}

class Wahaha extends Beverage{
    public Wahaha(){
        super("娃哈哈", 3);
    }
}

class IceTea extends Beverage{
    public IceTea(){
        super("冰红茶", 4);
    }
}

class NongfuSpring extends Beverage{
    public NongfuSpring(){
        super("农夫山泉", 5);
    }
}
class RedBull extends Beverage{
    public RedBull(){
        super("红牛", 6);
    }
}

class WALOVI extends Beverage{
    public WALOVI(){
        super("王老吉", 7);
    }
}

class Customer{
    Beverage[] cart = new Beverage[10];
    int quantity = 0;

    public void take(Beverage beverage){
        if(quantity < cart.length){
            cart[quantity] = beverage;
            quantity++;
        }
    }
}