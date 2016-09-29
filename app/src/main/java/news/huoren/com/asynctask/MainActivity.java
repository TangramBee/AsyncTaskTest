package news.huoren.com.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyAdapter myAdapter;
    private static String URL="http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleView = (RecyclerView) findViewById(R.id.myRecycleView);
        mRecycleView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的,设置这个选项可以提高性能
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLinearLayoutManager);

        new NewsAysncTask().execute(URL);

    }

    class NewsAysncTask extends AsyncTask<String, Void, List<NewsBean>>{

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJosnData(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            myAdapter = new MyAdapter(MainActivity.this, newsBeen);
            mRecycleView.setAdapter(myAdapter);
        }
    }

    private List<NewsBean> getJosnData(String param) {
        List<NewsBean> mNewsBean = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(param).openStream());
            JSONObject jsonObject;
            NewsBean newsBean;
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                newsBean = new NewsBean();
                newsBean.newIconUrl = jsonObject.getString("picSmall");
                newsBean.newsTile = jsonObject.getString("name");
                newsBean.newContent = jsonObject.getString("description");
                mNewsBean.add(newsBean);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mNewsBean;
    }

    private String readStream(InputStream is) {
        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8"); //字节流转化为字符流
            BufferedReader br = new BufferedReader(isr); //从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。
            while ((line = br.readLine()) != null) {
                result += line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
