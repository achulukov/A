import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=pmZ1DphF42N5zYYj7eVePMVfkOXbkhTZO3vo9kIr");

        CloseableHttpResponse response = httpClient.execute(request);

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        String jsocString = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        NASAAnswer nasaAnswer = gson.fromJson(jsocString, NASAAnswer.class);

        System.out.println(nasaAnswer.toString());

        request = new HttpGet(nasaAnswer.getUrl());

        response = httpClient.execute(request);

        try(FileOutputStream fileOutputStream = new FileOutputStream("pic.jpg")) {

            byte[] bytes =  response.getEntity().getContent().readAllBytes();

            System.out.println(bytes[6]);

            fileOutputStream.write(bytes, 0, bytes.length);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }





    }

}
