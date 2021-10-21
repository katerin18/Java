import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Crawler {
        private static final int PORT = 80;
        private static final int TIMEOUT = 5000;


        static LinkedList <URLDepthPair> findRef = new LinkedList<>();
        static LinkedList <URLDepthPair> viewedRef = new LinkedList<>();

        public static void showResult(LinkedList<URLDepthPair> viewedRef) {
            for (URLDepthPair i: viewedRef) { // выводим все просмотренные ссылки с глубиной
                System.out.println("Ссылка: " + i.getURL() + "    Глубина: " + i.getDepth());
            }
        }

        public static void request(PrintWriter out,URLDepthPair pair) throws MalformedURLException {
            // выводим текст отправляемого HTTP запроса
            out.println("GET " + pair.getPath() + " HTTP/1.1");
            out.println("Host: " + pair.getHost());
            out.println("Connection: close");
            out.println();
        }

        public void Scan(String pair, int maxDepth) {
            findRef.add(new URLDepthPair(pair, 0));

            while (!findRef.isEmpty()) {
                URLDepthPair currentPair = findRef.removeFirst(); // ссылочка из очереди заходит в "кабинет"(переменную)

                if (currentPair.depth < maxDepth) {  // пока не израсходовали запасы по глубине обхода
                    try {
                        System.out.println("URL = " + currentPair.getHost() + currentPair.getPath());
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(currentPair.getHost(), PORT), TIMEOUT);
                        socket.setSoTimeout(TIMEOUT); // ждем ответа 5 секунд, иначе нас игнорят и мы его посылаем
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        request(out, currentPair);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            StringBuilder currentLink = new StringBuilder();  // нашли и записали ссыло4ку
                            if (line.contains(URLDepthPair.URL_PREFIX1)) {
                                for (int i = line.indexOf(URLDepthPair.URL_PREFIX1) + URLDepthPair.URL_PREFIX1.length(); (line.charAt(i) != '"') && (line.charAt(i) != '<'); i++) {
                                    currentLink.append(line.charAt(i));
                                }
                                URLDepthPair newPair = new URLDepthPair("http://" + currentLink.toString(), currentPair.depth + 1);
                                if (URLDepthPair.check(findRef, newPair) && URLDepthPair.check(viewedRef, newPair))
                                    findRef.add(newPair);
                            } else if (line.contains(URLDepthPair.URL_PREFIX2)) {
                                for (int i = line.indexOf(URLDepthPair.URL_PREFIX2) + URLDepthPair.URL_PREFIX2.length(); (line.charAt(i) != '\'') && (line.charAt(i) != '<'); i++) {
                                    currentLink.append(line.charAt(i));
                                }
                                URLDepthPair newPair = new URLDepthPair("http://" + currentLink.toString(), currentPair.depth + 1);
                                if (URLDepthPair.check(findRef, newPair) && URLDepthPair.check(viewedRef, newPair))
                                    findRef.add(newPair);
                            }


                        }
                        socket.close();
                    } catch (Exception e) {
                        System.err.println("Ошибка - " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                viewedRef.add(currentPair);  // добавляем ссылку в просмотренные
            }
            //showResult(viewedRef);

        }
        //http://aj-worldwildlife.myspecies.info/
        public static void main(String[] args) throws IOException {
            String httpPage = "http://gov.ru/";
            int depth = 2;

            Crawler Search = new Crawler();
            Search.Scan(httpPage, depth);
        }
    }
