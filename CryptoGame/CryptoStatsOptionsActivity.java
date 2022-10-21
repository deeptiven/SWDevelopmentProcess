package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CryptoStatsOptionsActivity extends AppCompatActivity {

    private Button viewCryptoDetails;
    private Button disableCryptoDetails;
    private Button optionsCancel;
    private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_stats_option);
        database = new Database(this);

        final String cryptoTitle = getIntent().getStringExtra("cryptoTitle");

        viewCryptoDetails = (Button)findViewById(R.id.viewCryptoDetails);
        disableCryptoDetails = (Button)findViewById(R.id.disableCryptoDetails);
        optionsCancel = (Button)findViewById(R.id.optionsCancel);

        if(database.isCryptoDisabled(cryptoTitle)) {
            disableCryptoDetails.setVisibility(View.INVISIBLE);
        } else {
            disableCryptoDetails.setVisibility(View.VISIBLE);
        }

        viewCryptoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CryptoStatsOptionsActivity.this, CryptoStatsDetailsActivity.class);
                intent.putExtra("cryptoTitle", cryptoTitle);
                startActivity(intent);
                finish();
            }
        });

        disableCryptoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CryptoStatsOptionsActivity.this, DisableCryptogramActivity.class);
                intent.putExtra("cryptoTitle", cryptoTitle);
                startActivity(intent);
                finish();
            }
        });

        optionsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CryptoStatsOptionsActivity.this, CryptogramStats.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
