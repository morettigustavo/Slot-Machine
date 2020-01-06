package br.senai.sp.slot;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public AnimationDrawable adAnimacao, adAnimacao2, adAnimacao3;
    Button btnJogar, btnAdicionar, btnRemover, btnCreditos;
    int iImagens[] = {R.drawable.imagem2, R.drawable.imagem1, R.drawable.imagem4, R.drawable.imagem5, R.drawable.imagem3, R.drawable.imagem6};
    int iMultiplicadores[] = {10, 8, 6, 4, 2, 0};
    TextView tvSaldo, tvPartidas, tvPremio, tvAposta, tvtSaldo, tvtPartidas, tvtPremio, tvtAposta;
    ImageView ivPrimeira, ivSegunda, ivTerceira, ivMusica;
    int iSaldo = 500, iAposta = 0, iPremio = 0, iPartidas = 0, iTotal = 500;
    SeekBar pbQnt;
    boolean bMusica = true;
    MediaPlayer mpMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);
        instanciarObjetos();
        trocarFontes();

        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogar();
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apostar(10, false);
            }
        });

        btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apostar(-10, false);
            }
        });

        pbQnt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                apostar(progress, true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnCreditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarCreditos();
            }
        });

        ivMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mutarMusica();
            }
        });
    }

    private void mutarMusica() {
        if (bMusica) {
            ivMusica.setImageResource(R.drawable.sem);
            mpMusica.pause();
            bMusica = false;
        } else {
            ivMusica.setImageResource(R.drawable.musica);
            mpMusica.start();
            bMusica = true;
        }
    }

    private void adicionarCreditos() {
        new Thread() {
            @Override
            public void run() {
                if (iTotal == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnCreditos.setEnabled(false);
                            btnAdicionar.setEnabled(false);
                            btnRemover.setEnabled(false);
                            pbQnt.setEnabled(false);
                        }
                    });
                    MediaPlayer mpMoedas = MediaPlayer.create(MainActivity.this, R.raw.creditos);
                    mpMoedas.setLooping(false);
                    mpMoedas.setVolume(0.2f, 0.2f);
                    mpMoedas.start();
                    try {
                        Thread.sleep(3500);
                        for (int i = 0; i < 501; i += 10) {
                            Thread.sleep(100);
                            iSaldo = +i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    trocarTexto();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnCreditos.setEnabled(true);
                                btnAdicionar.setEnabled(true);
                                btnRemover.setEnabled(true);
                                pbQnt.setEnabled(true);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mpMoedas.stop();
                }
                super.run();
            }
        }.start();
    }

    private void apostar(int iValor, boolean barra) {
        if (barra) {
            iAposta = iValor;
            iSaldo = iTotal - iValor;
        } else {
            if ((iSaldo > 0 || iValor < 0) && (iAposta > 0 || iValor > 0)) {
                iAposta += iValor;
                iSaldo += iValor * -1;
                pbQnt.setProgress(iAposta);
            }
        }
        trocarTexto();
    }

    private void jogar() {
        if (iAposta != 0) {
            iPremio = 0;
            trocarTexto();
            if (iTotal > 0) {
                animar();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        adAnimacao.stop();
                        adAnimacao2.stop();
                        adAnimacao3.stop();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int iRoleta1 = (int) (Math.random() * 5);
                                int iRoleta2 = (int) (Math.random() * 5);
                                int iRoleta3 = (int) (Math.random() * 5);
                                ivPrimeira.setBackgroundResource(iImagens[iRoleta1]);
                                ivSegunda.setBackgroundResource(iImagens[iRoleta2]);
                                ivTerceira.setBackgroundResource(iImagens[iRoleta3]);
                                if (iRoleta1 == iRoleta2 && iRoleta2 == iRoleta3) {
                                    iPremio = iMultiplicadores[iRoleta1] * iAposta;
                                    iSaldo += iPremio;
                                } else {
                                    iSaldo = iSaldo - iAposta;
                                }
                                iPartidas++;
                                trocarTexto();
                            }
                        });
                    }
                }.start();
            }
        } else {
            Toast.makeText(this, "Você deve apostar algo para jogar", Toast.LENGTH_SHORT).show();
        }
        trocarTexto();
    }

    private void instanciarObjetos() {
        btnJogar = findViewById(R.id.btnJogar);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnRemover = findViewById(R.id.btnRemover);
        btnCreditos = findViewById(R.id.btnCreditos);

        tvSaldo = findViewById(R.id.tvSaldo);
        tvPartidas = findViewById(R.id.tvPartidas);
        tvPremio = findViewById(R.id.tvPremio);
        tvAposta = findViewById(R.id.tvAposta);

        tvtSaldo = findViewById(R.id.tvtSaldo);
        tvtPartidas = findViewById(R.id.tvtPartidas);
        tvtPremio = findViewById(R.id.tvtPremio);
        tvtAposta = findViewById(R.id.tvtAposta);

        ivPrimeira = findViewById(R.id.ivPrimeira);
        ivSegunda = findViewById(R.id.ivSegunda);
        ivTerceira = findViewById(R.id.ivTerceira);
        ivMusica = findViewById(R.id.ivMusica);

        pbQnt = findViewById(R.id.pbQnt);

        mpMusica = MediaPlayer.create(this, R.raw.musica);
        mpMusica.setLooping(true);
        mpMusica.setVolume(0.2f, 0.2f);
    }

    private void trocarFontes() {
        final Typeface texto = Typeface.createFromAsset(getAssets(), "texto.otf");
        btnJogar.setTypeface(texto);
        btnCreditos.setTypeface(texto);

        tvSaldo.setTypeface(texto);
        tvPartidas.setTypeface(texto);
        tvPremio.setTypeface(texto);
        tvAposta.setTypeface(texto);

        tvtSaldo.setTypeface(texto);
        tvtPartidas.setTypeface(texto);
        tvtPremio.setTypeface(texto);
        tvtAposta.setTypeface(texto);
    }

    private void animar() {
        ImageView ivPrimeira = findViewById(R.id.ivPrimeira);
        ivPrimeira.setBackgroundResource(R.drawable.roleta);
        adAnimacao = (AnimationDrawable) ivPrimeira.getBackground();
        adAnimacao.start();

        ImageView ivSegunda = findViewById(R.id.ivSegunda);
        ivSegunda.setBackgroundResource(R.drawable.roleta2);
        adAnimacao2 = (AnimationDrawable) ivSegunda.getBackground();
        adAnimacao2.start();

        ImageView ivTerceira = findViewById(R.id.ivTerceira);
        ivTerceira.setBackgroundResource(R.drawable.roleta3);
        adAnimacao3 = (AnimationDrawable) ivTerceira.getBackground();
        adAnimacao3.start();
    }

    private void trocarTexto() {
        tvAposta.setText("R$ " + String.format("%05d", iAposta));
        tvPremio.setText("R$ " + String.format("%05d", iPremio));
        tvSaldo.setText("R$ " + String.format("%05d", iSaldo));
        tvPartidas.setText(String.format("%03d", iPartidas));
        iTotal = iSaldo + iAposta;
        pbQnt.setMax(iTotal);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mpMusica != null) {
            mpMusica.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (bMusica) {
            if (mpMusica != null) {
                mpMusica.start();
            }
        }
    }
}