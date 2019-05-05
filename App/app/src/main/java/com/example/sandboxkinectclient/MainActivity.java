package com.example.sandboxkinectclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DPadView dpad = new DPadView(this);
        super.onCreate(savedInstanceState);
        setContentView(dpad);
    }
}
