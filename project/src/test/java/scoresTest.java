import java.util.ArrayList;

public class scoresTest {
    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("张三", 90));
        students.add(new Student("李四", 80));
        students.add(new Student("王五", 70));
        students.add(new Student("赵六", 60));
        students.add(new Student("孙七", 50));

        students.stream().forEach((student) -> {
            System.out.println(student.getScore());
        });
    }
}

class Student {
    private String name;
    private int score;
    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
}