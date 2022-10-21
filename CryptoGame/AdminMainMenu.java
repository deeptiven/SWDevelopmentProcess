package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AdminMainMenu extends AppCompatActivity {

    private Button adminLogout;
    private Button adminCryptoStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_menu);

        adminLogout = (Button)findViewById(R.id.adminLogout);
        adminCryptoStats = (Button)findViewById(R.id.adminCryptoStats);

        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHAREDPREFS,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(AdminMainMenu.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        adminCryptoStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainMenu.this, CryptogramStats.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
