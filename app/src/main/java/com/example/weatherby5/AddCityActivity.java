package com.example.weatherby5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;


public class AddCityActivity extends AppCompatActivity {
    private EditText et;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        et = findViewById(R.id.editText);
        button = findViewById(R.id.button);
    }

    public void addACity(View view)
    {
        Intent j = getIntent();
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("str",et.getText().toString());
        i.putExtra("flag",true);
        startActivity(i);



    }
}
