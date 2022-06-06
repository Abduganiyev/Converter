import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConverterDemo {
    public static void main(String[] args) throws IOException {

        refreshData();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Currencies: USD EUR CNY USZ");
        System.out.print("Enter first currency: ");
        String first = scanner.next();
        System.out.print("Enter second currency: ");
        scanner = new Scanner(System.in);
        String second = scanner.next();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        convert(first,second,amount);
    }

    private static void convert(String first, String second,Double amount) throws IOException{
        Gson gson = new Gson();
        BufferedReader input = new BufferedReader(new FileReader("src/main/resources/currencies.json"));

        Currency[] list = gson.fromJson(input, Currency[].class);

        double rate = 0.0;
        double rate2 = 0.0;
        for (Currency currency : list) {
            if (currency.getCcy().equalsIgnoreCase(first)) {
                 rate = Double.parseDouble(currency.getRate());
                 break;
            }
        }
        for (Currency currency : list) {
            if (currency.getCcy().equalsIgnoreCase(second)) {
                rate2 = Double.parseDouble(currency.getRate());
                break;
            }
        }

        if (first.equalsIgnoreCase("uzs"))
            System.out.printf("%.2f %s\n", rate2/(amount*1000),second);
        else if (second.equalsIgnoreCase("uzs")){
            System.out.printf("%.2f %s\n",rate*amount,second);
        } else
            System.out.printf("%.2f %s\n",(rate*amount)/rate2,second);
    }


    private static void refreshData() throws IOException {

        URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Currency> list = Arrays.asList(gson.fromJson(br, Currency[].class));

        writeToFile(list);
    }

    private static void writeToFile(List<Currency> def) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/currencies.json"))) {

            gson.toJson(def,bw);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
