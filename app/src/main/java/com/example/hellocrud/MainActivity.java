package com.example.hellocrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // show activity in full screen

        setContentView(R.layout.activity_main);
        try {
            initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(new Intent(getApplicationContext(), CreateActivity.class));
        finish();
    }

    private void initConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dphtbwlmz");  // Replace with your actual Cloudinary cloud name
        config.put("api_key", "445195769296628");  // Replace with your Cloudinary API key
        config.put("api_secret", "JkCRMpzwB-W3SFl6U9XQogm1lT4");  // Replace with your Cloudinary API secret

        MediaManager.init(this, config);
    }
}
