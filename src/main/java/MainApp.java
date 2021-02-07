
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainApp {

    public static void main(String args[]) throws IOException {

        File file;
        String strDirectory = null;
        System.out.println("Введите адрес веб-страницы:");

        Scanner scanner = new Scanner(System.in);
        String strURL = scanner.next();

        System.out.println("Введите адрес каталога для загрузки:");
        strDirectory = scanner.next();

        try {
            File directory  = new File(strDirectory);
            if (directory.isDirectory()) {
                file = File.createTempFile("web_page_", ".html", directory);
            } else {
                System.out.println("Каталог введенный для загрузки файла не найден, файл будет сохранен во времееный каталог");
                file = File.createTempFile("web_page_", ".html");
            }

            URL url = new URL(strURL);
            URLConnection connection = url.openConnection();

            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] buffer = new byte[16384];
            while (true) {
                int bytes = bis.read(buffer);
                if (bytes < 0) {
                    break;
                }
                bos.write(buffer, 0, bytes);
            }
            bis.close();
            bos.close();

            //char[] splitters = {' ', ',', '.', '!', '?','"', ';', ':', '[', ']', '(', ')', '\n', '\r', '\t'};

            //bos = null;
            //bos = new BufferedOutputStream(new FileOutputStream(File.createTempFile("result", ".tmp")), 100000);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]+\\s");
            String row = reader.readLine();
            Map<String, Integer> words = new HashMap<>();
            while (row != null) {
                Matcher matcher = pattern.matcher(row);
                ArrayList<String> matches = new ArrayList<>();
                String text = "";
                while(matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();

                    text = text + row.substring(start,end);
                    text.trim();
                }

                if(!text.isEmpty()) {
                    Arrays.stream(text.split("[ ]")).filter(w -> w.length() > 0)
                            .forEach(w -> words.put(w, words.getOrDefault(w, 0) + 1));
                }

                row = reader.readLine();
            }
            for (Map.Entry<String, Integer> keyValue : words.entrySet()) {
                System.out.println("Слово: '" + keyValue.getKey() + "', Количество в тексте: " + keyValue.getValue());
            }

            //File tmpFile = writeToFile(inputStream);
        } catch (MalformedURLException e) {
            System.out.println("Введен не корректный URL");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}