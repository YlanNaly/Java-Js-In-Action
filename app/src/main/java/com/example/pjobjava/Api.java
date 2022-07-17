package com.example.pjobjava;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class Api {
    private static HttpURLConnection connection;
    public static void main(String[] args) {
        BufferedReader reader ;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try{
            URL url =new URL("https://wspc52.herokuapp.com/1");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(6000);

        int status = connection.getResponseCode();

        if(status > 250){
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
          while((line = reader.readLine())!= null){
              responseContent.append(line);
          }
        reader.close();
        }
            System.out.println(responseContent.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
