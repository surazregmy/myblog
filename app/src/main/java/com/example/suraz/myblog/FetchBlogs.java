package com.example.suraz.myblog;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.suraz.myblog.data.Post;
import com.example.suraz.myblog.data.PostListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class FetchBlogs extends AppCompatActivity {

    ListView listView ;
    HttpURLConnection urlConnection;
    ProgressDialog progressDialog;
    List<Post> posts = new LinkedList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_blogs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listView = (ListView) findViewById(R.id.list);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new FetchServerData().execute();
        System.out.println("HElLO");
        System.out.println(posts);
        viewList();
    }
    public void viewList() {
        final PostListAdapter adapter = new PostListAdapter(getApplicationContext(), posts);
        listView.setAdapter(adapter);
    }

    private class FetchServerData extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(FetchBlogs.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray jsonArray = null;
            BufferedReader reader;

            try {
                URL url = new URL("http://surajregmi.com.np/wp-json/wp/v2/posts");
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null){
                    System.out.println(">>>>>>>>>>");
                    buffer.append(line);
                }
                System.out.println("Buffere = "+ buffer.toString());
                jsonArray = new JSONArray(buffer.toString());


                System.out.println("THE JSON Array is here ==> ");
                System.out.println(jsonArray);
                System.out.println(jsonArray.length());




                for (int i=0; i < jsonArray.length(); i++){
                    System.out.println("I am inside the loop");
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject titleobject =jsonObject.getJSONObject("title");
                    JSONObject excerptObject = jsonObject.getJSONObject("excerpt");

                    String title = titleobject.getString("rendered");
                    String excerpt = excerptObject.getString("rendered");
                    String author = jsonObject.getString("author");

                    String authorname="";
                    if(author.equals("1")){
                        authorname= "suraj";
                    }

//                    String testexcerpt = "bb";

                    String testexcerpt = Jsoup.parse(excerpt).text();



                    System.out.println(testexcerpt);
                    Post post1 = new Post(i,title,testexcerpt,authorname);
                    posts.add(post1);


                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray){
            super.onPostExecute(jsonArray);
            System.out.println(jsonArray);
            Toast.makeText(FetchBlogs.this, "completed", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

}
