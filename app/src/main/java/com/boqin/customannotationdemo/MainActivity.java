package com.boqin.customannotationdemo;

import com.boqin.customannotation.CustomAnnotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermission = (Button) findViewById(R.id.bt_permission);

        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @CustomAnnotation
    void showCamera() {
        Toast.makeText(this, "Annotation", Toast.LENGTH_SHORT).show();
    }
}
