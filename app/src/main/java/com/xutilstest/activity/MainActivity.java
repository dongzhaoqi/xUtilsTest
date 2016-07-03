package com.xutilstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xutilstest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.btn_network)
    Button btnNetwork;
    @Bind(R.id.btn_database)
    Button btnDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }



    @OnClick({R.id.btn_network, R.id.btn_database})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_network:
                startActivity(new Intent(this, NetWorkActivity.class));
                break;
            case R.id.btn_database:
                startActivity(new Intent(this, DbActivity.class));
                break;
        }
    }
}
