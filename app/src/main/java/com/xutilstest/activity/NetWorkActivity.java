package com.xutilstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xutilstest.ConstantValue;
import com.xutilstest.CustomApplication;
import com.xutilstest.R;
import com.xutilstest.adatper.NewsAdapter;
import com.xutilstest.db.News;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 7/4/2016.
 */
public class NetWorkActivity extends AppCompatActivity {
    @Bind(R.id.news_list)
    RecyclerView recyclerView;
    private DbManager db;
    private NewsAdapter newsAdapter;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

        try {
            newsList = db.selector(News.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        for (News news : newsList){
            try {
                db.delete(news);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ConstantValue.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        getData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("apikey", ConstantValue.APIKEY);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getData(JSONObject response) {
        String result = response.optJSONArray("newslist").toString();
        newsList = JSONArray.parseArray(result, News.class);

        for (News news : newsList){
            try {
                db.save(news);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                Toast.makeText(NetWorkActivity.this, "position:" + data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(newsAdapter);
    }
}
