package com.example.pjobjava;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pjobjava.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<String> title;
    ArrayAdapter<String> listAdapter;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      initalizeUserlist();
      binding.btnView.setOnClickListener(

              new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              new FetchData().start();
          }
      });
    }

    private void initalizeUserlist() {
        title = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,title);
        binding.textView.setAdapter(listAdapter);
    }

    class FetchData extends Thread {
    String data = "";

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Wait a second");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("http://wspc52.herokuapp.com/");
                HttpURLConnection  httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray information = jsonObject.getJSONArray("information");

                    for(int i=0;i<information.length();i++){
                        JSONObject names = information.getJSONObject(i);
                        String titre = names.getString("title");
                        String descr = names.getString("description");
                        String comp = names.getString("company");
                        String hrefe = names.getString("href");
                        title.add(titre);
                        title.add(i,descr);
                        title.add(i+1,comp);
                        title.add(i+2,hrefe);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                title.add("No connection");
            } catch (IOException e) {
                e.printStackTrace();
                title.add("Erro of data");
            } catch (JSONException e) {
                e.printStackTrace();
                title.add("Error of parsing");
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                        listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
// .\gradlew assembleDebug