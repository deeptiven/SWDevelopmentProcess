package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Button loginButton;
    private Button logoutButton;
    private Button signupButton;
    private Button signupsignupButton;
    private Database database;
    private Button signupcancel;

    public static final String SHAREDPREFS = "sharedPerfs";
    public static final String LOGGEDINUSER = "loggedInUser";
    public static final String ISADMIN = "isAdmin";
    public static final String LOGINSUCCESS = "loginSuccess";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new Database(this);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        // email = (EditText)findViewById(R.id.email);
        loginButton = (Button)findViewById(R.id.login);
        signupButton = (Button)findViewById(R.id.signup);

        sharedPreferences = getSharedPreferences(LoginActivity.SHAREDPREFS,
                Context.MODE_PRIVATE);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginScreenUsername = username.getText().toString();
                String loginScreenPassword = password.getText().toString();

                setContentView(R.layout.signup_layout);

                username = (EditText)findViewById(R.id.username);
                password = (EditText)findViewById(R.id.password);
                email = (EditText)findViewById(R.id.email);

                username.setText(loginScreenUsername);
                password.setText(loginScreenPassword);

                signupsignupButton = (Button) findViewById(R.id.signupsignup);
                signupcancel = (Button) findViewById(R.id.signupcancel);

                signupsignupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUsernameEmpty = username.getText().toString().isEmpty();
                        boolean isPasswordEmpty = password.getText().toString().isEmpty();
                        boolean isEmailEmpty = email.getText().toString().isEmpty();
                        if (isUsernameEmpty || isPasswordEmpty || isEmailEmpty) {
                            if (isUsernameEmpty)    {
                                username.setError("Username cannot be empty");
                            }
                            if (isPasswordEmpty)    {
                                password.setError("Password cannot be empty");
                            }
                            if (isEmailEmpty)   {
                                email.setError("Email cannot be empty");
                            }
                        } else  {
                            if (database.signUpAction(username.getText().toString(),
                                    password.getText().toString(),
                                    email.getText().toString()))    {
                                database.loginAction(username.getText().toString(),
                                        password.getText().toString());

                                if (sharedPreferences.getBoolean(LOGINSUCCESS, false)){
                                    if (sharedPreferences.getBoolean(ISADMIN, false)) {
                                        Intent AdminMainMenuActivity = new Intent(v.getContext(), AdminMainMenu.class);
                                        startActivity(AdminMainMenuActivity);
                                        finish();
                                    } else  {
                                        Intent playerIntent = new Intent(LoginActivity.this, PlayerMainMenu.class);
                                        playerIntent.putExtra("playerName", username.getText().toString());
                                        startActivity(playerIntent);
                                        finish();
                                    }
                                } else  {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Invalid Username or Password";
                                    int duration = Toast.LENGTH_LONG;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            } else  {
                                Context context = getApplicationContext();
                                CharSequence text = "Could not create new user";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                    }
                });

                signupcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v) {
                boolean isUsernameEmpty = username.getText().toString().isEmpty();
                boolean isPasswordEmpty = password.getText().toString().isEmpty();

                if (isUsernameEmpty || isPasswordEmpty) {
                    if (isUsernameEmpty)    {
                        username.setError("Username cannot be empty");
                    }
                    if (isPasswordEmpty)    {
                        password.setError("Password cannot be empty");
                    }
                } else  {
                    database.loginAction(username.getText().toString(),
                            password.getText().toString());
                    if (sharedPreferences.getBoolean(LOGINSUCCESS, false))  {
                        if (sharedPreferences.getBoolean(ISADMIN, false)) {
                            Intent AdminMainMenuActivity = new Intent(v.getContext(), AdminMainMenu.class);
                            startActivity(AdminMainMenuActivity);
                            finish();
                        } else  {
                            Intent playerIntent = new Intent(LoginActivity.this, PlayerMainMenu.class);
                            playerIntent.putExtra("playerName", username.getText().toString());
                            startActivity(playerIntent);
                            finish();
                        }
                    } else  {
                        Context context = getApplicationContext();
                        CharSequence text = "Invalid Username or Password";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }
        });
    }
}
