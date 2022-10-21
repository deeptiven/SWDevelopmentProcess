package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.database.Cursor;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DisableCryptogramActivity extends AppCompatActivity {


    private Button disableCryptogramCancelButton;
    String cryptoTitle;
    private EditText cryptoName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_disable_cryptogram);

        cryptoTitle = getIntent().getStringExtra("cryptoTitle");
        cryptoName = (EditText)findViewById(R.id.cryptoName);
        cryptoName.setText(cryptoTitle);
    }

    public void disableCryptogram(View v) {

        Database database = new Database(this);
        EditText penaltyNumber = (EditText) findViewById(R.id.penaltyNumber);

        int reducePoints = Integer.parseInt(penaltyNumber.getText().toString());

        if(reducePoints < 0 || reducePoints > 10){
            penaltyNumber.setError("Penalty should be between 0-10 points");
            penaltyNumber.setText("0");
        }
        else
        {
            if(!database.disableCryptogram(cryptoTitle, reducePoints)) {
                Context context = getApplicationContext();
                CharSequence text = "Could not disable the cryptogram.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(DisableCryptogramActivity.this, CryptogramStats.class);
                startActivity(intent);
                finish();
            }
            Context context = getApplicationContext();
            CharSequence text = "The cryptogram " + cryptoTitle + " was disabled.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent intent = new Intent(DisableCryptogramActivity.this, CryptogramStats.class);
            startActivity(intent);
            finish();
        }
    }

    public void onAdminDisableCryptoCancelClick(View v) {
        Intent intent = new Intent(DisableCryptogramActivity.this, CryptogramStats.class);
        startActivity(intent);
        finish();
    }

}