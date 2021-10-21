import java.util.*;

// отслеживает URL-адрес, который необходимо обработать, а также URL-адрес, который уже видели
public class URLPool {
    private int maxDepth;

    // текущее количество потоков в состоянии ожидания (wait())
    private int waitCount = 0;

    private LinkedList<URLDepthPair> pendingURLs;  // список ожидания

    private LinkedList<URLDepthPair> processedURLs;  // список обработанных

    private HashSet<String> seenURLs; // посещенные URL

    public URLPool(int max) {  // создает URL пул с заданной глубиной
        pendingURLs = new LinkedList<URLDepthPair>();
        processedURLs = new LinkedList<URLDepthPair>();
        seenURLs = new HashSet<String>();

        maxDepth = max;
    }

    public synchronized int getWaitCount() {  // для нормальной работы с потоками (synchronized)
        return waitCount;
    }

    public synchronized void add(URLDepthPair nextPair) {   // добавление ссылки в список ожидания
        String newURL = nextPair.getURL().toString();

        // Вырезает "/" из URL
        String trimURL = (newURL.endsWith("/")) ? newURL.substring(0, newURL.length() -1) : newURL;
        if (seenURLs.contains(trimURL)){
            return;
        }
        seenURLs.add(trimURL);

        if (nextPair.getDepth() < maxDepth) {
            pendingURLs.add(nextPair);
            notify(); // сообщаем приостановленному потоку о новом URL
        }
        processedURLs.add(nextPair);
    }

    public synchronized URLDepthPair get() {
        // приостанавливаем поток, пока не появится новый URL
        while (pendingURLs.size() == 0) {
            waitCount++;
            try {
                wait();
            }
            catch (InterruptedException e) {  // иначе мы возвращаем первую ссылку на отработку
                System.out.println("Отработано исключение InterruptedException - " +
                        e.getMessage());
            }
            waitCount--;
        }
        return pendingURLs.removeFirst();
    }

    public synchronized void printURLs() {   // вывод всех обработанных URL
        System.out.println("\nКол-во уникальных URLs: " + processedURLs.size());
        while (!processedURLs.isEmpty()) {
            System.out.println(processedURLs.removeFirst());
        }
    }
}
