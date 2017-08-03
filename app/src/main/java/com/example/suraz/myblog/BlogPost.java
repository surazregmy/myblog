package com.example.suraz.myblog;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suraz.myblog.data.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static java.lang.Boolean.TRUE;

public class BlogPost extends AppCompatActivity {

    EditText title,content;
    Button draftbut, postbut;
    String title_str, content_str;
    HttpURLConnection urlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);

        title = (EditText) findViewById(R.id.title);
        content =(EditText) findViewById(R.id.content);

        postbut = (Button) findViewById(R.id.postbut);
        draftbut = (Button) findViewById(R.id.draftbut);

        postbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_str = title.getText().toString();
                content_str = content.getText().toString();
                new UpdatePost().execute();
                System.out.println("HElLO Update post");


            }
        });


    }

    private class UpdatePost extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            String userPass = "testuser"+":"+"testpassword";
            String encoded = Base64.encodeToString(userPass.getBytes(),Base64.DEFAULT);
            try {
                url = new URL("http://surajregmi.com.np/wp-json/wp/v2/posts");
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Authorization","Basic "+encoded);
              //  urlConnection.setRequestProperty("Content-type","application/json");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("title", title_str)
                        .appendQueryParameter("content", content_str)
                        .appendQueryParameter("status", "publish");
                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();

//                String reply;
//                InputStream in = urlConnection.getInputStream();
//                StringBuffer sb = new StringBuffer();
//                try {
//                    int chr;
//                    while ((chr = in.read()) != -1) {
//                        sb.append((char) chr);
//                    }
//                    reply = sb.toString();
//                } finally {
//                    in.close();
//                }
//                System.out.println(sb);

                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }
                //System.out.println(inputStream);
                System.out.println(urlConnection.getResponseCode());
                System.out.println(urlConnection.getResponseMessage());
                System.out.println("Inside the post bckgrouend");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String jsonArray){
            //System.out.println(jsonArray);
        }
    }

}
