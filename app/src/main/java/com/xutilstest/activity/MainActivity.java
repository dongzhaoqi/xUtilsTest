package com.xutilstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    @Bind(R.id.btn_find)
    Button btnFind;
    @Bind(R.id.btn_find_by_condition)
    Button btnFindByCondition;
    @Bind(R.id.btn_insert)
    Button btnInsert;
    @Bind(R.id.btn_update)
    Button btnUpdate;
    private DbManager db;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
    }


    @OnClick({R.id.btn_network, R.id.btn_database,R.id.btn_delete,R.id.btn_find, R.id.btn_find_by_condition, R.id.btn_insert, R.id.btn_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_network:
                startActivity(new Intent(this, NetWorkActivity.class));
                break;
            case R.id.btn_database:
                startActivity(new Intent(this, DbActivity.class).putExtra("tag", "1"));
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_find:
                startActivity(new Intent(this, DbActivity.class).putExtra("tag", "0"));
                break;
            case R.id.btn_find_by_condition:
                startActivity(new Intent(this, DbActivity.class).putExtra("tag", "2"));
                break;
            case R.id.btn_insert:
                startActivity(new Intent(this, DbActivity.class).putExtra("tag", "3"));
                break;
            case R.id.btn_update:
                startActivity(new Intent(this, DbActivity.class).putExtra("tag", "4"));
                break;
        }
    }

    //清空数据库中的数据
    public void delete(){
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
        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
    }

}
