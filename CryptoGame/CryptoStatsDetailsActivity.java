package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CryptoStatsDetailsActivity extends AppCompatActivity {

    private EditText encryptedPhrase;
    private Button disableCryptoDetails;
    private Button cryptoDetailsCancel;
    private Database database;
    private EditText solutionPhrase;
    private EditText hint;
    private EditText cryptoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_crypto_details);
        database = new Database(this);

        cryptoName = (EditText)findViewById(R.id.cryptoName);

        encryptedPhrase = (EditText) findViewById(R.id.encryptedPhrase);
        solutionPhrase = (EditText) findViewById(R.id.solutionPhrase);
        hint = (EditText) findViewById(R.id.hint);

        cryptoDetailsCancel = (Button) findViewById(R.id.cryptoDetailsCancel);


        final String cryptoTitle = getIntent().getStringExtra("cryptoTitle");
        Cursor result = database.getCryptoDetails(cryptoTitle);

        cryptoName.setText(cryptoTitle);

        encryptedPhrase.setText(result.getString(result.getColumnIndex("Encrypted_Phrase")));
        solutionPhrase.setText(result.getString(result.getColumnIndex("Solution_Phrase")));
        hint.setText(result.getString(result.getColumnIndex("Hint")));

        cryptoDetailsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CryptoStatsDetailsActivity.this, CryptogramStats.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
