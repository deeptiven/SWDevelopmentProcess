package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerViewScores extends AppCompatActivity {

    TableLayout result;
    Button retToMenuPlayerScores;
    String player = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view_scores);

        player = getIntent().getStringExtra("playerName");
        result = (TableLayout)findViewById(R.id.playerScores);
        result.setStretchAllColumns(true);
        result.bringToFront();

        retToMenuPlayerScores = (Button)findViewById(R.id.retToMenuPlayerScores);
        retToMenuPlayerScores.bringToFront();

        TableRow heading = new TableRow(this);

        TextView usernameCol = new TextView(this);
        usernameCol.setText("Username");
        heading.addView(usernameCol);

        TextView cryptosAttemptedCol = new TextView(this);
        cryptosAttemptedCol.setText("Cryptograms attempted");
        heading.addView(cryptosAttemptedCol);

        TextView pointsCol = new TextView(this);
        pointsCol.setText("Points");
        heading.addView(pointsCol);

        result.addView(heading);

        try {
            Cursor playerScores = new Database(this).playerScores();
            System.out.println(playerScores.getCount());

            if (playerScores.getCount() == 0) {
                // Display empty table
                System.out.println("Empty table");
            } else  {
                while(playerScores.moveToNext())    {
                    String username = playerScores.getString(
                            playerScores.getColumnIndex("Username"));
                    long cryptosAttempted = playerScores.getLong(
                            playerScores.getColumnIndex("CRYPTOGRAMS_ATTEMPTED"));
                    long points = playerScores.getLong(
                            playerScores.getColumnIndex("Points"));

                    TableRow tr = new TableRow(this);

                    TextView c1 = new TextView(this);
                    c1.setText(username);
                    tr.addView(c1);

                    TextView c2 = new TextView(this);
                    c2.setText(Long.toString(cryptosAttempted));
                    tr.addView(c2);

                    TextView c3 = new TextView(this);
                    c3.setText(Long.toString(points));
                    tr.addView(c3);

                    result.addView(tr);

                }
            }
        } catch (Exception e)   {
            Context context = getApplicationContext();
            CharSequence text = "Could not display the Player Scores.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        retToMenuPlayerScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerViewScores.this, PlayerMainMenu.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        });
    }
}
