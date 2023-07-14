import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

class Product {
    private int id;
    private String title;
    private double rating;

    public Product(int id, String title, double rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public static void main(String[] args) {
        String url = "https://dummyjson.com/products";
        String consId = "1234567";
        String userKey = "faY738sH";

        try {
            // Mengirim permintaan GET ke URL
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Cons-ID", consId);
            connection.setRequestProperty("User-Key", userKey);

            // Menerima respon JSON
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parsing JSON dan mendapatkan daftar produk
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray productsArray = jsonResponse.getJSONArray("products");
                int length = productsArray.length();
                Product[] products = new Product[length];

                for (int i = 0; i < length; i++) {
                    JSONObject productObj = productsArray.getJSONObject(i);
                    int id = productObj.getInt("id");
                    String title = productObj.getString("title");
                    double rating = productObj.getDouble("rating");

                    Product product = new Product(id, title, rating);
                    products[i] = product;
                }

                // Menggunakan Selection Sort untuk mengurutkan produk berdasarkan rating
                for (int i = 0; i < length - 1; i++) {
                    int minIndex = i;
                    for (int j = i + 1; j < length; j++) {
                        if (products[j].getRating() < products[minIndex].getRating()) {
                            minIndex = j;
                        }
                    }
                    Product temp = products[minIndex];
                    products[minIndex] = products[i];
                    products[i] = temp;
                }

                // Menampilkan produk yang sudah diurutkan
                System.out.println("Produk yang diurutkan berdasarkan rating (dari yang terkecil ke terbesar):");
                for (Product product : products) {
                    System.out.println("ID: " + product.getId() + ", Judul: " + product.getTitle() + ", Rating: " + product.getRating());
                }
            } else {
                System.out.println("Permintaan gagal. Kode respons: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
