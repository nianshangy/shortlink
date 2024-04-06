import java.util.Random;

public class PlayPuke {
    public static void main(String[] args) {
        String[] puke = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        Puke[] pukes = new Puke[13];
        initializationPuke(pukes);
        Player[] players = new Player[]{
                new Player("jack", 0),
                new Player("jab", 0),
                new Player("anm", 0)
        };
        for (int i = 0; i < 3; i++) { // 每个玩家抽3张牌
            for (Player player : players) {
                int num, count;
                int l = getRandomIndex(pukes);
                String card = pukes[l].getCard();
                num = getCardValue(card);
                pukes[l].setSize(pukes[l].getSize() - 1); // 抽完牌后减少一张牌
                System.out.print(player.getName() + "抽到的第" + (i + 1) + "张牌：" + card + "  ");
                player.countNum(num);
                count = player.getCount() <= 21 ? player.getCount() : player.getCount() % 21;
                System.out.println("总点数：" + count);
            }
        }
        determineWinner(players);
    }

    public static void initializationPuke(Puke[] pukes) {
        String[] pukeArr = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (int i = 0; i < 13; i++) {
            Puke puke = new Puke(pukeArr[i], 4);
            pukes[i] = puke;
        }
    }

    public static int getRandomIndex(Puke[] pukes) {
        while (true) {
            int l = new Random().nextInt(13);
            if (pukes[l].getSize() > 0) {
                return l;
            }
        }
    }

    public static int getCardValue(String card) {
        if (card.equals("A")) {
            return 11; // 默认A为11点，后面会根据实际情况调整
        } else if (card.equals("10") || card.equals("J") || card.equals("Q") || card.equals("K")) {
            return 10;
        } else {
            return Integer.parseInt(card);
        }
    }

    public static void determineWinner(Player[] players) {
        int maxScore = 0;
        String winner = "";
        for (Player player : players) {
            if (player.getCount() > maxScore && player.getCount() <= 21) {
                maxScore = player.getCount();
                winner = player.getName();
            }
        }
        System.out.println("赢家是：" + winner + "，得分是" + maxScore);
    }
}

class Puke {
    String card;
    int size;

    public Puke(String card, int size) {
        this.card = card;
        this.size = size;
    }

    public String getCard() {
        return card;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

class Player {
    String name;
    int count;

    public Player(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public void countNum(int num) {
        // 如果是A，需要根据当前点数决定是1点还是11点
        if (num == 11 && this.count + num > 21) {
            num = 1;
        }
        this.count += num;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}

