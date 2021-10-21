import java.io.*;
import java.net.*;
import java.util.regex.*;
public class CrawlerTask implements Runnable {   // (implements Runnable) - для многопоточности

    //Этот класс просматривает веб-сайты и добавляет URL в пул для обработки другими потоками
    public static final String LINK_REGEX = "href\\s*=\\s*\"([^$^\"]*)\"";
    public static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX, Pattern.CASE_INSENSITIVE);

    public static int maxPatience = 5; // время ожидания сервера сокетом

    private URLPool pool;

    public CrawlerTask(URLPool p) {
        pool = p;
    }


    // создает сокет для отправки HTTP-запроса на страницу "NextPAir"
    public Socket sendRequest(URLDepthPair nextPair)
            throws UnknownHostException, SocketException, IOException {     // подключаемся к сайту
        //Создет HTTP сокет
        Socket socket = new Socket(nextPair.getHost(), 80);
        socket.setSoTimeout(maxPatience * 1000);

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);

        String headersDebug = String.format("[Headers]GET %s HTTP/1.1 |Host: %s |Connection: close", nextPair.getDocPath(), nextPair.getHost());
        //System.out.println(headersDebug); // Отладка

        writer.println("GET " + nextPair.getDocPath() + " HTTP/1.1");
        writer.println("Host: " + nextPair.getHost());
        writer.println("Connection: close");
        writer.println();

        return socket;
    }

    //Обработка URL-адреса и добавление всех найденных ссылок в пул
    public void processURL(URLDepthPair url) throws IOException {
        Socket socket;
        try {
            socket = sendRequest(url);
        } catch (UnknownHostException e) {
            System.err.println("хост " + url.getHost() + " не определен");
            return;
        } catch (SocketException e) {
            System.err.println("ошибка при подключении: " + url.getURL() +
                    " - " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Не удалось получить страницу " + url.getURL() +
                    " - " + e.getMessage());
            return;
        }

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        // поиск ссылок на странице

        String line;
        while ((line = reader.readLine()) != null) {
            //System.out.println(line); //Отладка
            Matcher LinkFinder = LINK_PATTERN.matcher(line);
            while (LinkFinder.find()) {
                String newURL = LinkFinder.group(1);
                //System.out.println(newURL);

                URL newSite;
                try {
                    if (URLDepthPair.isAbsolute(newURL)) {
                        newSite = new URL(newURL);
                    } else {
                        newSite = new URL(url.getURL(), newURL);
                    }
                    //System.out.println(newSite); // For debugging
                    pool.add(new URLDepthPair(newSite, url.getDepth() + 1));
                } catch (MalformedURLException e) {
                    System.err.println("ошибка URL - " + e.getMessage());
                }
            }
        }
        reader.close();

        // Закрытие сокета
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Не удалось зыкрыть соединение с " + url.getHost() +
                    " - " + e.getMessage());
        }
    }

    // обработка первого URL из пула
    public void run() {  // типа основной метод
        URLDepthPair nextPair;
        while (true) {
            nextPair = pool.get();
            try {
                processURL(nextPair);
            } catch (IOException e) {
                System.err.println("ошибка чтения страницы " + nextPair.getURL() +
                        " - " + e.getMessage());
            }
        }
    }
}
