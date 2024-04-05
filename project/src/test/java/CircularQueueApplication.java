///*import java.util.Scanner;
//
//public class ScoreApplication {
//
//    public static void main(String[] args) {
//
//        Scanner scanner = new Scanner(System.in);
//        int capacity = scanner.nextInt();
//        Queue queue = new Queue(capacity);
//
//        queue.put(scanner.nextInt());
//
//        queue.put(scanner.nextInt());
//
//        queue.put(scanner.nextInt());
//
//        queue.get();
//
//        queue.get();
//
//        queue.put(scanner.nextInt());
//
//        queue.put(scanner.nextInt());
//
//        queue.print();*/
///*    }
//}
//
//class Queue {
//    int capacity;
//    int value;
//    Queue next;
//
//    public void put(int value){
//
//    }
//
//    public int get(){
//
//    }
//
//    public void print(){
//
//    }
//}*/
//
//import java.util.Scanner;
//
//class Node {
//    int data;
//    Node next;
//    Node(int data) {
//        this.data = data;
//        this.next = null;
//    }
//}
//
//
//class CircularQueue {
//
//    private int capacity;
//
//    private int front;
//
//    private int rear;
//
//    private Node[] queueArray;
//
//
//    public CircularQueue(int capacity) {
//
//        this.capacity = capacity;
//
//        this.queueArray = new Node[capacity];
//
//        this.front = -1;
//
//        this.rear = -1;
//
//    }
//
//
//    public boolean isEmpty() {
//
//        return front == -1;
//
//    }
//
//
//    public boolean isFull() {
//
//        return (rear + 1) % capacity == front;
//
//    }
//
//
//    public boolean put(int item) {
//
//        if (isFull()) {
//
//            return false;
//
//        }
//
//        if (isEmpty()) {
//
//            front = 0;
//
//            rear = 0;
//
//        } else {
//
//            rear = (rear + 1) % capacity;
//
//        }
//
//        queueArray[rear] = new Node(item);
//
//        return true;
//
//    }
//
//
//    public int get() {
//
//        if (isEmpty()) {
//
//            return Integer.MIN_VALUE;
//
//        }
//
//        int result = queueArray[front].data;
//
//        if (front == rear) {
//
//            front = -1;
//
//            rear = -1;
//
//        } else {
//
//            front = (front + 1) % capacity;
//
//        }
//
//        return result;
//
//    }
//
//
//    public void print() {
//
//        if (isEmpty()) {
//
//            System.out.println("empty");
//
//        } else {
//
//            Node current = queueArray[front];
//
//            do {
//
//                System.out.print(current.data + " ");
//
//                current = current.next;
//
//                if (current == queueArray[rear]) {
//
//                    break;
//
//                }
//
//                current = queueArray[(int) (current.data % capacity)];
//
//            } while (current != null);
//
//        }
//
//        System.out.println();
//
//    }
//
//}
//
//public class CircularQueueApplication {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int capacity = scanner.nextInt();
//        CircularQueue queue = new CircularQueue(capacity);
//        queue.put(scanner.nextInt());
//        queue.put(scanner.nextInt());
//        queue.put(scanner.nextInt());
//        queue.get();
//        queue.get();
////        queue.put(scanner.nextInt());
////        queue.put(scanner.nextInt());
//        queue.print();
//    }
//}

import java.util.Scanner;

//
//class CircularQueue {
//    private int[] data;
//    private int head;
//    private int tail;
//    private int size;
//
//    public CircularQueue(int capacity) {
//        data = new int[capacity];
//        head = 0;
//        tail = 0;
//        size = 0;
//    }
//
//    public boolean put(int value) {
//        if (size == data.length) {
//            return false;
//        }
//        data[tail] = value;
//        tail = (tail + 1) % data.length;
//        size++;
//        return true;
//    }
//
//    public int get() {
//        if (size == 0) {
//            return Integer.MIN_VALUE;
//        }
//        int value = data[head];
//        head = (head + 1) % data.length;
//        size--;
//        return value;
//    }
//
//    public void print() {
//        if (size == 0) {
//            System.out.println("empty");
//            return;
//        }
//        for (int i = 0; i < size; i++) {
//            System.out.print(data[(head + i) % data.length] + " ");
//        }
//        System.out.println();
//    }
//}
//
public class CircularQueueApplication {
    public static void main(String[] ags) {
        Scanner scanner = new Scanner(System.in);
        int capacity = scanner.nextInt();
        CircularQueue queue = new CircularQueue(capacity);
        queue.put(scanner.nextInt());
        queue.put(scanner.nextInt());
        queue.put(scanner.nextInt());
        queue.get();
        queue.get();
        queue.put(scanner.nextInt());
        queue.put(scanner.nextInt());
        queue.print();
    }
}


class CircularQueue {
    public int capacity; //队列长度
    public int front; //头结点
    public int rear; //尾结点

    public int[] arr;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        arr = new int[capacity];
        this.front = 0;
        this.rear = 0;
    }

    //入队
    public void put(int i) { //i为要入列队的数
        //入队列前先判断队列是否已经满了
        if (isFull()) {
            System.out.println("队列已满，不能再加入数据！");
            return;
        }
        arr[rear] = i; //未满，执行入列操作
        rear = (rear + 1) % capacity;
    }

    //出队
    public void get() {
        //出队前先判断队列是否为空
        if (isEmpty()) {
            System.out.println("empty");
            return;
        }
        //队列中有数据，则执行出列操作
        System.out.print(arr[front] + " ");
        front = (front + 1) % capacity;
    }

    //打印队列全部数据
    public void print(){
        if (isEmpty()) {
            System.out.println("empty");
            return;
        }
        //从front开始遍历
        for (int i = front; i < front + size(); i++) {
            System.out.print(arr[i % capacity] + " ");
        }
    }

    //队列判满
    public boolean isFull() {
        return (rear + 1) % capacity == front;
    }

    //队列判空
    public boolean isEmpty() {
        return front == rear;
    }

    //队列存有数据长度
    public int size(){
        return (rear + capacity - front) % capacity;
    }
}