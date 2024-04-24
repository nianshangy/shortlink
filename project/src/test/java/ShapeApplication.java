import java.util.Scanner;

/**
 * 基于接口编程模拟生产形状
 */
public class ShapeApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String description = "请选择要绘制的图形[1.圆 2.圆角矩形 3.正方形 4.矩形]";

        System.out.println(description);

        int choice = scanner.nextInt();

        switch (choice) {

            case 1:

                System.out.println("请输入圆的半径");

                double radius = scanner.nextDouble();

                Circle circle = ProduceShapeFactory.produce(new CircleProducer(), radius);

                System.out.println(circle);

                break;

            case 2:

                System.out.println("请输入圆角矩形的长、宽和圆角半径");

                double length = scanner.nextDouble();
                double width = scanner.nextDouble();
                double cornerRadius = scanner.nextDouble();

                RoundedRectangle roundedRectangle = ProduceShapeFactory.produce(new RoundedRectangleProducer(), length, width, cornerRadius);

                System.out.println(roundedRectangle);

                break;

            case 3:

                System.out.println("请输入正方形的边长");

                double edge = scanner.nextDouble();

                Square square = ProduceShapeFactory.produce(new SquareProducer(), edge);

                System.out.println(square);

                break;

            case 4:

                System.out.println("请输入矩形的长和宽");

                double rectLength = scanner.nextDouble();
                double rectWidth = scanner.nextDouble();

                Rectangle rectangle = ProduceShapeFactory.produce(new RectangleProducer(), rectLength, rectWidth);

                System.out.println(rectangle);

                break;

            default:

                System.out.println("输入错误，请重新选择");

        }

    }
}

/**
 * 形状生产工厂类
 */
class ProduceShapeFactory {

    /**
     * 根据形状生产者接口实例和参数生产形状对象
     *
     * @param producer 形状生产者接口实例
     * @param edges    形状所需参数
     * @param <T>      形状类型
     * @return 生产的形状对象
     */
    public static <T> T produce(IShapeProducer<T> producer, double... edges) {
        return producer.produce(edges);
    }
}

/**
 * 圆形生产者，实现 IShapeProducer 接口
 */
class CircleProducer implements IShapeProducer<Circle> {
    @Override
    public Circle produce(double... edges) {
        if (edges.length != 1) {
            throw new IllegalArgumentException("Invalid number of arguments for Circle production.");
        }
        double radius = edges[0];
        return new Circle(radius);
    }
}

/**
 * 圆角矩形生产者，实现 IShapeProducer 接口
 */
class RoundedRectangleProducer implements IShapeProducer<RoundedRectangle> {
    @Override
    public RoundedRectangle produce(double... edges) {
        if (edges.length != 3) {
            throw new IllegalArgumentException("Invalid number of arguments for RoundedRectangle production.");
        }
        double length = edges[0];
        double width = edges[1];
        double cornerRadius = edges[2];
        return new RoundedRectangle(length, width, cornerRadius);
    }
}

/**
 * 正方形生产者，实现 IShapeProducer 接口
 */
class SquareProducer implements IShapeProducer<Square> {
    @Override
    public Square produce(double... edges) {
        if (edges.length != 1) {
            throw new IllegalArgumentException("Invalid number of arguments for Square production.");
        }
        double edge = edges[0];
        return new Square(edge);
    }
}

/**
 * 矩形生产者，实现 IShapeProducer 接口
 */
class RectangleProducer implements IShapeProducer<Rectangle> {
    @Override
    public Rectangle produce(double... edges) {
        if (edges.length != 2) {
            throw new IllegalArgumentException("Invalid number of arguments for Rectangle production.");
        }
        double length = edges[0];
        double width = edges[1];
        return new Rectangle(length, width);
    }
}

/**
 * 圆形类
 */
class Circle {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "radius=" + radius +
                '}';
    }
}

/**
 * 圆角矩形类
 */
class RoundedRectangle {
    private final double length;
    private final double width;
    private final double cornerRadius;

    public RoundedRectangle(double length, double width, double cornerRadius) {
        this.length = length;
        this.width = width;
        this.cornerRadius = cornerRadius;
    }

    @Override
    public String toString() {
        return "RoundedRectangle{" +
                "length=" + length +
                ", width=" + width +
                ", cornerRadius=" + cornerRadius +
                '}';
    }
}

/**
 * 正方形类
 */
class Square {
    private final double edge;

    public Square(double edge) {
        this.edge = edge;
    }

    @Override
    public String toString() {
        return "Square{" +
                "edge=" + edge +
                '}';
    }
}

/**
 * 矩形类
 */
class Rectangle {
    private final double length;
    private final double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "length=" + length +
                ", width=" + width +
                '}';
    }
}

/**
 * 形状生产者接口
 */
interface IShapeProducer<T> {
    public T produce(double... edges);
}

/**
 * 三角形生产者，实现 IShapeProducer 接口
 */
class TriangleProducer implements IShapeProducer<Triangle> {
    @Override
    public Triangle produce(double... edges) {
        if (edges.length != 3) {
            throw new IllegalArgumentException("Invalid number of arguments for Triangle production.");
        }
        double edge1 = edges[0];
        double edge2 = edges[1];
        double edge3 = edges[2];
        return new Triangle(edge1, edge2, edge3);
    }
}

/**
 * 三角形类
 */
class Triangle {
    private final double edge1;
    private final double edge2;
    private final double edge3;

    public Triangle(double edge1, double edge2, double edge3) {
        this.edge1 = edge1;
        this.edge2 = edge2;
        this.edge3 = edge3;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "edge1=" + edge1 +
                ", edge2=" + edge2 +
                ", edge3=" + edge3 +
                '}';
    }
}
