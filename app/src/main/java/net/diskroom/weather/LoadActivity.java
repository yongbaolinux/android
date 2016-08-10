package net.diskroom.weather;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        handler = new Handler(){};
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                LoadActivity.this.startActivity(mainIntent);
                LoadActivity.this.finish();
            }
        };
        handler.postDelayed(runnable,2000);
    }
}
