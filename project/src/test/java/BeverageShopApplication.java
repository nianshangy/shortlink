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

/*
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
*//*




class BeverageShopApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Customer zhansan = new Customer();

        System.out.println("请输入要购买的饮料[1.润田矿泉水 2.可口可乐 3.王老吉 4.娃哈哈 5.冰红茶]和数量");

        String input = scanner.nextLine();
        Scanner reader = new Scanner(input).useDelimiter(",");

        while (reader.hasNext()) {
            String[] choices = reader.next().split(" ");
            int choice = Integer.parseInt(choices[0]);
            int quantity = Integer.parseInt(choices[1]);

            switch (choice) {
                case 1:
                    zhansan.take("润田矿泉水", quantity);
                    break;
                case 2:
                    zhansan.take("可口可乐", quantity);
                    break;
                case 3:
                    zhansan.take("王老吉", quantity);
                    break;
                case 4:
                    zhansan.take("娃哈哈", quantity);
                    break;
                case 5:
                    zhansan.take("冰红茶", quantity);
                    break;
                default:
                    System.out.println("请输入正确的选项");
                    break;
            }
        }
        zhansan.checkout();
    }
}

class Customer {
    private Map<String, Double> beveragePrices = new HashMap<>();
    private Map<String, Integer> shoppingCart = new HashMap<>();

    public Customer() {
        // 初始化饮料价格
        beveragePrices.put("润田矿泉水", 2.0);
        beveragePrices.put("可口可乐", 2.0);
        beveragePrices.put("王老吉", 3.0);
        beveragePrices.put("娃哈哈", 2.5);
        beveragePrices.put("冰红茶", 3.5);
    }

    public void take(String beverage, int quantity) {
        if (!beveragePrices.containsKey(beverage)) {
            System.out.println("无效的饮料选择");
            return;
        }
        shoppingCart.put(beverage, shoppingCart.getOrDefault(beverage, 0) + quantity);
    }

    public void checkout() {
        double totalCost = 0.0;

        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            String beverage = entry.getKey();
            int quantity = entry.getValue();
            double price = beveragePrices.get(beverage);

            totalCost += price * quantity;
        }

        System.out.printf("总价:%.1f\n", totalCost);
    }
}


class ScoreSortApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入成绩[在一行中输入，成绩之间用空格分割]");
        String input = scanner.nextLine();
        int[] scores = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray();

        String description = "请选择排序算法[1.冒泡排序 2.选择排序 3.插入排序]";
        System.out.println(description);
        int choice = scanner.nextInt();

        ScoreHelper scoreHelper = new ScoreHelper(scores);

        ISort sortAlgorithm;
        switch (choice) {
            case 1:
                sortAlgorithm = new BubbleSort();
                break;
            case 2:
                sortAlgorithm = new SelectionSort();
                break;
            case 3:
                sortAlgorithm = new InsertSort();
                break;
            default:
                System.out.println("无效的选择。程序退出");
                return;
        }

        scoreHelper.sort(sortAlgorithm);

        System.out.println(Arrays.toString(scoreHelper.getScores()).replace("[", "").replace("]", "").replace(",", ""));
    }
}

class ScoreHelper {
    private int[] scores;

    public ScoreHelper(int[] scores) {
        this.scores = scores;
    }

    public int[] getScores() {
        return scores;
    }

    public void sort(ISort sortAlgorithm) {
        sortAlgorithm.sort(scores);
    }
}

interface ISort {
    void sort(int[] scores);
}

class BubbleSort implements ISort {
    @Override
    public void sort(int[] scores) {
        for (int i = 0; i < scores.length - 1; i++) {
            for (int j = 0; j < scores.length - i - 1; j++) {
                if (scores[j] > scores[j + 1]) {
                    int temp = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = temp;
                }
            }
        }
    }
}

class SelectionSort implements ISort {
    @Override
    public void sort(int[] scores) {
        for (int i = 0; i < scores.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < scores.length; j++) {
                if (scores[j] < scores[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = scores[i];
            scores[i] = scores[minIndex];
            scores[minIndex] = temp;
        }
    }
}

class InsertSort implements ISort {
    @Override
    public void sort(int[] scores) {
        for (int i = 1; i < scores.length; i++) {
            int key = scores[i];
            int j = i - 1;

            while (j >= 0 && scores[j] > key) {
                scores[j + 1] = scores[j];
                j--;
            }
            scores[j + 1] = key;
        }
    }
}
*/
