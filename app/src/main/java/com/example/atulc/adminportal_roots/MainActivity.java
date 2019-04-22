package com.example.atulc.adminportal_roots;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity{

    MainLayoutFragment mainLayoutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayoutFragment  = new MainLayoutFragment();
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frame_lay);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay, new SignInFragment()).commit();



    }



}
