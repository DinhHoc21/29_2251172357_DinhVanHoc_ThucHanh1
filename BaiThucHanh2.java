import java.util.concurrent.*;
import java.util.*;
public class BaiThucHanh2 {
    static final int MAX_CAPACITY = 200;
    static BlockingQueue<Integer> A = new LinkedBlockingQueue<>(MAX_CAPACITY);

    static Semaphore semProducer = new Semaphore(MAX_CAPACITY); // kiểm soát chỗ trống
    static Semaphore semConsumer = new Semaphore(0);            // kiểm soát dữ liệu có thể lấy

    static class Producer extends Thread {
        private final String name;

        public Producer(String name) {
            this.name = name;
        }

        public void run() {
            Random rand = new Random();
            while (true) {
                try {
                    Thread.sleep(rand.nextInt(1000)); // thời gian ngẫu nhiên
                    int value = rand.nextInt(1000);   // sinh giá trị ngẫu nhiên

                    semProducer.acquire(); // đợi chỗ trống
                    A.put(value);          // thêm vào hàng đợi
                    System.out.println(name + ": " + value + " - " + System.currentTimeMillis());
                    semConsumer.release(); // báo có dữ liệu

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer extends Thread {
        private final String name;

        public Consumer(String name) {
            this.name = name;
        }

        public void run() {
            Random rand = new Random();
            while (true) {
                try {
                    Thread.sleep(rand.nextInt(1000)); // thời gian ngẫu nhiên
                    semConsumer.acquire();            // đợi có dữ liệu
                    int value = A.take();             // lấy và xóa khỏi A

                    // Thực hiện nhiệm vụ xử lý tự định nghĩa (VD: bình phương)
                    int result = value * value;
                    System.out.println(name + ": " + value + " -> " + result + " - " + System.currentTimeMillis());

                    semProducer.release(); // báo có thêm chỗ trống

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        int k = 2; // số luồng sinh
        int h = 3; // số luồng xử lý

        // Tạo luồng sinh dữ liệu
        for (int i = 1; i <= k; i++) {
            new Producer("P" + i).start();
        }

        // Tạo luồng xử lý dữ liệu
        for (int i = 1; i <= h; i++) {
            new Consumer("C" + i).start();
        }
    }
}
