public class ScoreApplication {
    public static void main(String[] args) {
        int[] ints = new int[]{80,21,34,65,77,98};
        ScoreHelper scoreHelper = new ScoreHelper(ints);
        scoreHelper.avg();
    }
}

class ScoreHelper{
    int[] scores;

    public ScoreHelper(int[] scores){
        this.scores = scores;
    }
    public ScoreHelper(){
    }
    public double avg(){
        return 60;
    }
}
