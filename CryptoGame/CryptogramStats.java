package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CryptogramStats extends AppCompatActivity {

    TableLayout result;
    Button retToMenuCryptoStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram_stats);

        result = (TableLayout)findViewById(R.id.cryptoStats);
        result.setStretchAllColumns(true);
        result.bringToFront();

        retToMenuCryptoStats = (Button)findViewById(R.id.retToMenuCryptoStats);
        retToMenuCryptoStats.bringToFront();

        TableRow heading = new TableRow(this);

        TextView titleCol = new TextView(this);
        titleCol.setText("Title");
        heading.addView(titleCol);

        TextView creatorCol = new TextView(this);
        creatorCol.setText("Creator");
        heading.addView(creatorCol);

        TextView playedgamesCol = new TextView(this);
        playedgamesCol.setText("Played games");
        heading.addView(playedgamesCol);

        TextView percentwinCol = new TextView(this);
        percentwinCol.setText("Percent win");
        heading.addView(percentwinCol);

        TextView disabledCol = new TextView(this);
        disabledCol.setText("Disabled");
        heading.addView(disabledCol);

        result.addView(heading);

        try {
            Cursor cryptoStats = new Database(this).cryptoStats();
            //System.out.println(cryptoStats.getCount());

            if (cryptoStats.getCount() == 0) {
                // Display empty table
                System.out.println("No cryptogram exists");
            } else  {
                while(cryptoStats.moveToNext())    {
                    final String title = cryptoStats.getString(
                            cryptoStats.getColumnIndex("Title"));
                    String creator = cryptoStats.getString(
                            cryptoStats.getColumnIndex("Creator"));
                    int played_games = cryptoStats.getInt(
                            cryptoStats.getColumnIndex("Played_games"));
                    float percent_win = cryptoStats.getFloat(
                            cryptoStats.getColumnIndex("Percent_win"));
                    String disabled = cryptoStats.getString(
                            cryptoStats.getColumnIndex("Disabled"));

                    TableRow tr = new TableRow(this);

                    Button c1 = new Button(this);
                    c1.setText(title);
                    c1.setAllCaps(false);
                    tr.addView(c1);
                    c1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CryptogramStats.this, CryptoStatsOptionsActivity.class);
                            intent.putExtra("cryptoTitle", title);
                            startActivity(intent);
                            finish();
                        }
                    });


                    TextView c2 = new TextView(this);
                    c2.setText(creator);
                    tr.addView(c2);

                    TextView c3 = new TextView(this);
                    c3.setText(Integer.toString(played_games));
                    tr.addView(c3);

                    TextView c4 = new TextView(this);
                    c4.setText(String.format ("%.0f%%", percent_win * 100));
                    tr.addView(c4);

                    TextView c5 = new TextView(this);
                    c5.setText(disabled);
                    tr.addView(c5);

                    result.addView(tr);

                }
            }
        } catch (Exception e)   {
            Context context = getApplicationContext();
            CharSequence text = "Could not display the cryptogram statistics.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        retToMenuCryptoStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CryptogramStats.this, AdminMainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
