package com.xutilstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xutilstest.CustomApplication;
import com.xutilstest.R;
import com.xutilstest.adatper.NewsAdapter;
import com.xutilstest.db.News;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 7/4/2016.
 */
public class DbActivity extends AppCompatActivity {
    @Bind(R.id.news_list)
    RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    TextView empty_view;
    private DbManager db;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private News news;
    String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others);
        ButterKnife.bind(this);

        try {
            init();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void init() throws DbException {
        String tag = getIntent().getStringExtra("tag");

        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

        if ("0".equals(tag)) {
            //取第一条数据
            newsList = new ArrayList<>();
            news = db.selector(News.class).findFirst();
            if (news != null) {
                newsList.add(news);
            }
        } else if ("1".equals(tag)) {
            //取所有数据
            newsList = db.selector(News.class).findAll();
        } else if ("2".equals(tag)) {
            //根据条件查询
            newsList = db.selector(News.class).where("ctime", "=", time).limit(3).findAll();
        }else if("3".equals(tag)){
            //插入数据
            newsList = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                News news = new News();
                news.setCtime("2016-08-10");
                news.setDescription("哈哈哈" + i);
                news.setTitle("我是标题" + i);
                news.setPicUrl("http://img5.duitang.com/uploads/item/201507/14/20150714175013_UZ8VF.jpeg");
                newsList.add(news);
            }
            for(News news : newsList){
                db.saveOrUpdate(news);
            }
        }else if("4".equals(tag)){
            newsList = db.selector(News.class).findAll();
            db.update(News.class, WhereBuilder.b("title", "=", "我是标题2"), new KeyValue("title", "我被改变了"));
            newsList = db.selector(News.class).findAll();
        }


        if (newsList.size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
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
