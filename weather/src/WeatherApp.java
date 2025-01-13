import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class WeatherApp {
    public static void main(String[] args) {
        String apiKey = "0404901efadba432aaeaa87362bf9936";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Geben Sie den Namen der Stadt ein: ");
        String stadt = scanner.nextLine();
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + stadt + "&lang=de&appid=" + apiKey;

        try {
            URI uri = URI.create(urlString);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Fehler: Wetterdaten konnten nicht abgerufen werden.");
                return;
            }
            Scanner responseScanner = new Scanner(conn.getInputStream());
            StringBuilder jsonString = new StringBuilder();
            while (responseScanner.hasNext()) {
                jsonString.append(responseScanner.nextLine());
            }
            responseScanner.close();

            String response = jsonString.toString();

            String wetter = "Unbekannt";
            if (response.contains("\"description\":\"")) {
                int start = response.indexOf("\"description\":\"") + 14;
                int end = response.indexOf("\"", start);
                wetter = response.substring(start, end);
            }
            double temperatur = 0;
            if (response.contains("\"temp\":")) {
                int start = response.indexOf("\"temp\":") + 7;
                int end = response.indexOf(",", start);
                temperatur = Double.parseDouble(response.substring(start, end)) - 273.15;
            }
            double windSpeed = 0;
            if (response.contains("\"speed\":")) {
                int start = response.indexOf("\"speed\":") + 8;
                int end = response.indexOf(",", start);
                windSpeed = Double.parseDouble(response.substring(start, end));
            }
            System.out.println("Wetter in " + stadt + ": " + wetter);
            System.out.println("Temperatur: " + String.format("%.2f", temperatur) + " °C");
            System.out.println("Windgeschwindigkeit: " + windSpeed + " m/s");

        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}