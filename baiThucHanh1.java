import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class baiThucHanh1 {
    static int totalPrimes = 0;
    static ReentrantLock lock = new ReentrantLock();


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập n (số phần tử mảng, >100): ");
        int n = sc.nextInt();
        while (n <= 99) {
            System.out.print("Vui lòng nhập n > 100: ");
            n = sc.nextInt();
        }

        System.out.print("Nhập k (số luồng, >1): ");
        int k = sc.nextInt();
        while (k <= 1 || k > n) {
            System.out.print("Vui lòng nhập k > 1 và <= n: ");
            k = sc.nextInt();
        }

        // Tạo mảng ngẫu nhiên
        int[] A = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(200) + 1;  // 1..1000
        }
        System.out.print("Nhập phần tử cuối cùng (số nguyên): ");
        A[n - 1] = sc.nextInt();

        // Chia mảng thành k đoạn và tạo luồng
        int segmentSize = n / k;
        Thread[] threads = new Thread[k];

        for (int i = 0; i < k; i++) {
            int start = i * segmentSize;
            int end = (i == k - 1) ? n : (i + 1) * segmentSize;

            int threadId = i + 1;
            int[] segment = Arrays.copyOfRange(A, start, end);

            threads[i] = new Thread(() -> {
                int count = 0;
                for (int num : segment) {
                    if (isPrime(num)) {
                        count++;
                        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
                        System.out.println("T" + threadId + ": " + num + " : " + timeStr);

                        try {
                            Thread.sleep(10); // giả lập xử lý
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                lock.lock();
                try {
                    totalPrimes += count;
                } finally {
                    lock.unlock();
                }
            });
            threads[i].start();
        }

        // Chờ các luồng kết thúc
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Tổng số số nguyên tố tìm được: " + totalPrimes);

        sc.close();
    }

    // Hàm kiểm tra số nguyên tố
    static boolean isPrime(int num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        int sqrt = (int) Math.sqrt(num);
        for (int i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }

}
