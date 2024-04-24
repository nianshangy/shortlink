import java.util.Scanner;

/**
 * 图形绘制接口
 */
interface ISHApeMold {
    void draw();
}

/**
 * 图形工厂类
 */
class ShapeFactory {
    private ISHApeMold drawShape;

    public void setDrawShape(ISHApeMold shape) {
        this.drawShape = shape;
    }

    public void draw() {
        drawShape.draw();
    }
}

/**
 * 圆形模具类，实现 ISHApeMold 接口
 */
class CircleMold implements ISHApeMold {
    private double radius;

    public CircleMold(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.printf("Draw a circle with a radius of %.1f\n", radius);
    }
}

/**
 * 三角形模具类，实现 ISHApeMold 接口
 */
class TriangleMold implements ISHApeMold {
    private double side1, side2, side3;

    public TriangleMold(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    @Override
    public void draw() {
        System.out.printf("Draw a triangle with side1 %.1f,side2 %.1f and side3 %.1f\n",
                side1, side2, side3);
    }
}

/**
 * 正方形模具类，实现 ISHApeMold 接口
 */
class SquareMold implements ISHApeMold {
    private double side;

    public SquareMold(double side) {
        this.side = side;
    }

    @Override
    public void draw() {
        System.out.printf("Draw a square with a side of %.1f\n", side);
    }
}

/**
 * 矩形模具类，实现 ISHApeMold 接口
 */
class RectangleMold implements ISHApeMold {
    private double width, height;

    public RectangleMold(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.printf("Draw a rectangle with width %.1f and height %.1f\n", width, height);
    }
}

/**
 * 主程序
 */
public class DrawShapeApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShapeFactory factory = new ShapeFactory();

        String description = "请选择要绘制的图形[1.圆 2.三角形 3.正方形 4.矩形]";
        System.out.println(description);

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("请输入圆的半径");
                double radius = scanner.nextDouble();
                factory.setDrawShape(new CircleMold(radius));
                factory.draw();
                break;

            case 2:
                System.out.println("请输入三角形的三边");
                double side1 = scanner.nextDouble();
                double side2 = scanner.nextDouble();
                double side3 = scanner.nextDouble();
                factory.setDrawShape(new TriangleMold(side1, side2, side3));
                factory.draw();
                break;

            case 3:
                System.out.println("请输入正方形的边长");
                double side = scanner.nextDouble();
                factory.setDrawShape(new SquareMold(side));
                factory.draw();
                break;

            case 4:
                System.out.println("请输入矩形的宽和高");
                double width = scanner.nextDouble();
                double height = scanner.nextDouble();
                factory.setDrawShape(new RectangleMold(width, height));
                factory.draw();
                break;

            default:
                System.out.println("输入错误");
        }
    }
}
