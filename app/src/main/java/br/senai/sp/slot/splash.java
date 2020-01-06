package br.senai.sp.slot;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    ProgressBar pbSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash);

        TextView tvTitulo = findViewById(R.id.tvTitulo);
        final Typeface texto = Typeface.createFromAsset(getAssets(), "texto.otf");
        tvTitulo.setTypeface(texto);
        pbSplash = findViewById(R.id.pbSplash);

        new Thread(){
            @Override
            public void run() {
                int i;
                for (i=0; i<101; i++){
                    pbSplash.setProgress(i);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intencao = new Intent(splash.this, MainActivity.class);
                startActivity(intencao);
                finish();
            }
        }.start();
    }
}
