
public class GenericsTest {
    public static void main(String[] args) {
        plusUtils plusUtils = new plusUtils();
        System.out.println(plusUtils.plus(1, 2));
        System.out.println(plusUtils.plus(1.1, 2.2));
        /*System.out.println(plusUtils.plus("1", "2"));
        System.out.println(plusUtils.plus(1, 2.2));
        System.out.println(plusUtils.plus(1, "2"));*/
    }
}

class plusUtils {
    public <T> T plus(T a, T b) {
        if (a instanceof Integer && b instanceof Integer) {
            return (T) (Integer) ((Integer) a + (Integer)b);
        }
        if (a instanceof Double && b instanceof Double) {
            return (T) (Double) ((Double) a + (Double)b);
        }
        return null;
    }

    public <T> T add(T a, T b) {
        if (a instanceof Integer && b instanceof Integer) {
            Integer a1 = (Integer) a;
            Integer b1 = (Integer) b;
            Integer c = a1 + b1;
            return (T) c;
        }
        return null;
    }

    public <T> T plusDouble(T a, T b) {
        Double a1 = Double.parseDouble(a.toString());
        Double b1 = Double.parseDouble(b.toString());
        Double c = a1 + b1;
        return (T) c;
    }
    /*public <T> T plusInt(T a, T b) {
        Integer a1 = Integer.parseInt(a.toString());
        Integer b1 = Integer.parseInt(b.toString());
        Integer c = a1 + b1;
        return (T) c;
    }*/
}

