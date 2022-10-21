package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayerMainMenu extends AppCompatActivity  {

    private Button playerCreateCrypto;
    private Button playerSolveCrypto;
    private Button playerViewScores;
    private Button playerLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String player = getIntent().getStringExtra("playerName");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_main_menu);

        playerCreateCrypto = (Button)findViewById(R.id.playerCreateCrypto);
        playerSolveCrypto = (Button)findViewById(R.id.playerSolveCrypto);
        playerViewScores = (Button)findViewById(R.id.playerViewScores);
        playerLogout = (Button)findViewById(R.id.playerLogout);

        playerCreateCrypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerMainMenu.this, AddNewCryptogram.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        });

        playerSolveCrypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerMainMenu.this, SolveCryptogram.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        });

        playerViewScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerMainMenu.this, PlayerViewScores.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        });

        playerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHAREDPREFS,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(PlayerMainMenu.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}
