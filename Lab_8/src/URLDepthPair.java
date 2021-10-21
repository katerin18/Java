import java.net.*;
import java.util.regex.*;

// Хранилище URL и их и глубины
public class URLDepthPair {
    // Регулярное выражение для поиска URL
    public static final String URL_REGEX = "(https?:\\/\\/)((\\w+\\.)+\\.(\\w)+[~:\\S\\/]*)";  // регулярка для поиска ссылок
    public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    private URL URL;
    private int depth;

    public URLDepthPair(URL url, int d) throws MalformedURLException {
        // Предполагается, что приходящий URL адрес является абсолютным
        URL = new URL(url.toString());
        depth = d;
    }

    @Override
    public String toString() {
        return "URL: " + URL.toString() + ", Depth: " + depth;
    }

    // возвращает URL
    public URL getURL() {
        return URL;
    }

    // возвращает глубину
    public int getDepth() {
        return depth;
    }

    // возвращает имя Хоста
    public String getHost() {
        return URL.getHost();
    }

    // возвращает путь к файлу на сервере
    public String getDocPath() {
        if (URL.getPath().equals(""))
            return "/";
        else
            return URL.getPath();
    }

    // проверка на абсолютный URL
    public static boolean isAbsolute(String url) {
        Matcher URLChecker = URL_PATTERN.matcher(url);
        if (!URLChecker.find()) {
            return false;
        }
        return true;
    }
}
