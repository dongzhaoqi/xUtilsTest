package com.xutilstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.xutilstest.CustomApplication;
import com.xutilstest.R;
import com.xutilstest.adatper.NewsAdapter;
import com.xutilstest.db.News;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 7/4/2016.
 */
public class DbActivity extends AppCompatActivity {
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
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                Toast.makeText(DbActivity.this, "position:" + data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(newsAdapter);

    }

}
