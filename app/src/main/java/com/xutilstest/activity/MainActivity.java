package com.xutilstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xutilstest.CustomApplication;
import com.xutilstest.R;
import com.xutilstest.db.News;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.btn_network)
    Button btnNetwork;
    @Bind(R.id.btn_database)
    Button btnDatabase;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    private DbManager db;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_network, R.id.btn_database,R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_network:
                startActivity(new Intent(this, NetWorkActivity.class));
                break;
            case R.id.btn_database:
                startActivity(new Intent(this, DbActivity.class));
                break;
            case R.id.btn_delete:
                delete();
                break;
        }
    }

    public void delete(){
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
    }


}
