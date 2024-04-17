import java.util.Scanner;

public class ShapeApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String tips = "请输入图形类型(1.三角形 2.圆形 3.矩形 4.正方形 5.梯形)";

        System.out.println(tips);

        int choice = scanner.nextInt();

        switch (choice) {

            case 1:

                System.out.println("请输入底边长");

                double bottom = scanner.nextDouble();

                System.out.println("请输入高");

                double height = scanner.nextDouble();

                Triangle triangle = new Triangle(bottom, height);

                System.out.printf("面积是%.2f", triangle.area());

                break;

            case 2:

                System.out.println("请输入半径");

                double radius = scanner.nextDouble();

                Circle circle = new Circle(radius);

                System.out.printf("面积是%.2f", circle.area());

                break;

            case 3:

                System.out.println("请输入长");

                double width = scanner.nextDouble();

                System.out.println("请输入宽");

                double height2 = scanner.nextDouble();

                Rectangle rectangle = new Rectangle(width, height2);

                System.out.printf("面积是%.2f", rectangle.area());

                break;

            case 4:

                System.out.println("请输入边长");

                double side = scanner.nextDouble();

                Square square = new Square(side);

                System.out.printf("面积是%.2f", square.area());

                break;

            case 5:

                System.out.println("请输入底边长");

                double bottom2 = scanner.nextDouble();

                System.out.println("请输入高");

                double height3 = scanner.nextDouble();

                System.out.println("请输入顶边长");

                double top = scanner.nextDouble();

                Trapezoid trapezoid = new Trapezoid(bottom2, top, height3);

                System.out.printf("面积是%.2f", trapezoid.area());

                break;

            default:

                System.out.println("输入错误");

        }

        scanner.close();

    }

}

class Triangle{
    private double bottom;

    private double height;

    public Triangle(double bottom, double height) {
        this.bottom = bottom;
        this.height = height;
    }
    public double area() {
        return bottom * height / 2;
    }
}

class Circle{
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }


    public double area() {
        return Math.PI * radius * radius;
    }

}
class Rectangle{

    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double area() {
        return width * height;
    }
}

class Square {
    private double side;

    public Square(double side) {
        this.side = side;
    }

    public double area() {
        return side * side;
    }
}

class Trapezoid{

    private double bottom;
    private double top;
    private double height;

    public Trapezoid(double bottom, double top, double height) {
        this.bottom = bottom;
        this.top = top;
        this.height = height;
    }
    public double area() {
        return (bottom + top) / 2 * height;
    }
}