package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

public class SolveCryptogram extends AppCompatActivity {

    String cryptoTitle_str = "";
    String encodedPhrase_str = "";
    String hint_str = "";
    String unencodedPhrase_str;
    String solution_str;
    String point_str;
    String correct_unencodedPhrase_str;

    int pointsTobet;
    int maxAttempts = 5;
    int attemptsRemaining;

    Map<Character, Character> letterHM;

    TreeSet<Character> fromSet;
    TreeSet<Character> toSet;
    List<Character> fromList;


    Character[] aphabetVector;

    Button decodeButton;
    Button clearButton;
    Button playerMenuButton;
    Button submitAnswerButton;
    Button submitPoints;

    TextView cryptoTitle;
    TextView unencodedPhrase;
    TextView attemptsTxt;
    TextView hintOutput;
    TextView encodedPhrase;
    TextView attempts;
    TextView encodedLetters;

    EditText points;
    EditText solution;

    boolean updatePointsFlag = false;
    boolean updatePlayCryptoAction = false;

    Database db;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqlDb;

    Context context;
    int duration = Toast.LENGTH_SHORT/4;
    Toast toast;
    int userID;
    int player_points;
    String cryptoTitleID;

    private Handler mHandler;
    private Runnable menuRunnable;

    SharedPreferences sharedPreferencesGame;
    SharedPreferences.Editor editor;
    public static LinkedTreeMap<String, String> cryptoInfo = new LinkedTreeMap<>();
    public static HashMap<String , LinkedTreeMap<String, String>> data = new HashMap<>();
    public static final String SHARED_PERFERENCES_GAME = "sharedPerferencesGame";
    public static final String INGAMEPROGRESS = "inProgress_flag";
    public static final String CRYPTOTITLE = "cryptoTitle_str";
    public static final String ENCODEDPHRASE = "encodedPhrase_str";
    public static final String HINT = "hint_str";
    public static final String ATTEMPTSREMAIN = "maxAttempts";
    public static final String UNENCODEDPHRASE = "unencodedPhrase_str";
    public static final String POINTS = "point_str";
    public static final String ANSWER = "correct_unencodedPhrase_str";
    public static final String ENCODEDLETTERS = "encodedLetters_str";
    public static final String ISCRYPTOSOLVED = "isCryptoSolved";

     String player= new String();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cryptogram);



        databaseHelper = new DatabaseHelper(this);
        sqlDb = databaseHelper.getWritableDatabase();
        db = new Database(this);
        player = getIntent().getStringExtra("playerName");
        userID =  db.getUserid(player);
        player_points = db.playerPoints(userID);

        attemptsRemaining = maxAttempts;
        context = getApplicationContext();
        toast = new Toast(context);

        fromList = new ArrayList<>();
        fromSet = new TreeSet<>();
        toSet = new TreeSet<>();
        letterHM = new HashMap<>();

        submitPoints = findViewById(R.id.submitPoints);
        cryptoTitle = findViewById(R.id.cryptogramTitle);
        unencodedPhrase = findViewById(R.id.decodedOutput);
        attempts = findViewById(R.id.attemptsOutput);
        hintOutput = findViewById(R.id.hintOutput);
        encodedPhrase = findViewById(R.id.encodedPhrase);
        points = findViewById(R.id.pointsInput);
        solution = findViewById(R.id.solutionInput);
        encodedLetters = findViewById(R.id.encodedLetters);
        decodeButton = findViewById(R.id.decodeButton);
        clearButton = findViewById(R.id.clearButton);
        submitAnswerButton = findViewById(R.id.submitButton);

        sharedPreferencesGame = getSharedPreferences(SolveCryptogram.SHARED_PERFERENCES_GAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferencesGame.edit();

        //String cryptoGame = "CRYPTOGRAM_TITLE";
        //initalize(player, cryptoGame);





            loadData();

            /*attempts.setText(sharedPreferencesGame.getString(ATTEMPTSREMAIN,"0"));
            cryptoTitle.setText(sharedPreferencesGame.getString(CRYPTOTITLE,""));
            encodedPhrase.setText(sharedPreferencesGame.getString(ENCODEDPHRASE,""));
            encodedLetters.setText(sharedPreferencesGame.getString(ENCODEDLETTERS,""));
            unencodedPhrase.setText(sharedPreferencesGame.getString(UNENCODEDPHRASE,""));
            points.setText(sharedPreferencesGame.getString(POINTS,""));
            hint_str = sharedPreferencesGame.getString(HINT,"");
            correct_unencodedPhrase_str = sharedPreferencesGame.getString(ANSWER,"");

*/




        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            letterHM.put(ch, ch);
        }

        solution.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                solution_str = solution.getText().toString();
            }
        });

        attempts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if ((!s.toString().isEmpty()) && (s.toString() != null) && (s.toString().matches("[0-9]+"))) {
                    attemptsRemaining = Integer.parseInt(s.toString());
                    if(attemptsRemaining <=2){
                        hintOutput.setText(hint_str);
                    }
                }
            }
        });


        points.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {

                if (checkValidPoints(s.toString(), player_points)) {
                    pointsTobet = Integer.parseInt(s.toString());
                    submitPoints.setEnabled(true);
                }
                else
                {
                    submitPoints.setEnabled(false);
                }

            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                solution.setText("");
                unencodedPhrase.setText("");
                letterHM.clear();
                for (char ch = 'A'; ch <= 'Z'; ++ch) {
                    letterHM.put(ch, ch);
                }
            }

        });

        playerMenuButton = findViewById(R.id.menuButton);
        playerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(SolveCryptogram.this, PlayerMainMenu.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        });


        submitPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPoints.setEnabled(false);
                points.setEnabled(false);
                decodeButton.setEnabled(true);
                clearButton.setEnabled(true);
                submitAnswerButton.setEnabled(true);
                getCryptograms();
            }
        });

        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                solution_str = solution.getText().toString();
                if (checkValidSolution(solution_str)) {

                    buildFromSet(encodedPhrase.getText().toString());
                    fromList.clear();
                    fromList.addAll(fromSet);    //convert from set to ArrayList

                    for (int i = 0; i < solution_str.length(); i++) {
                        if (Character.isLetter(solution_str.charAt(i))) {
                            letterHM.put(Character.toUpperCase(fromList.get(i)),
                                    Character.toUpperCase(solution_str.charAt(i)));
                        }
                    }
                    decode(encodedPhrase.getText().toString());
                }

            }
        });

        menuRunnable = new Runnable() {
            @Override
            public void run() {

                cryptoTitle.setText("");
                unencodedPhrase.setText("");
                attempts.setText("");
                hintOutput.setText("");
                encodedPhrase.setText("");
                points.setText("");
                solution.setText("");
                encodedLetters.setText("");

                Intent intent = new Intent(SolveCryptogram.this, PlayerMainMenu.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        };

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerMenuButton = findViewById(R.id.menuButton);
                decodeButton = findViewById(R.id.decodeButton);
                solution_str = solution.getText().toString();
                unencodedPhrase_str = unencodedPhrase.getText().toString();

                cryptoTitleID =  Integer.toString(db.getCryptoID(cryptoTitle_str));
                point_str = points.getText().toString();
                boolean validSolFlag = checkValidSolution(solution_str);
                boolean validPtsFlag = checkValidPoints(point_str, player_points);
                mHandler = new Handler();

                if (validSolFlag && validPtsFlag) {

                    if ((unencodedPhrase_str.length() < 1) || (unencodedPhrase_str.isEmpty()) || (unencodedPhrase_str == null) || !unencodedPhrase_str.equals(solution_str)){
                        decodeButton = findViewById(R.id.decodeButton);
                        decodeButton.performClick();
                    }

                    maxAttempts = Math.min(Integer.parseInt(attempts.getText().toString()), maxAttempts);
                    maxAttempts = Math.max(0,--maxAttempts);
                    attempts.setText(Integer.toString(maxAttempts));
                    pointsTobet = Integer.parseInt(point_str);

                    if (correct_unencodedPhrase_str.equals(unencodedPhrase_str)){
                        data.clear();
                        editor.remove(player);
                        editor.apply();

                        submitAnswerButton.setEnabled(false);
                        decodeButton.setEnabled(false);
                        updatePlayCryptoAction = db.playCryptoAction(userID, cryptoTitleID, 1);
                        toast = Toast.makeText(context, "Solution Was Successful", (duration));
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();

                        if (maxAttempts >= 2) {
                            updatePointsFlag = updatePoints(player, pointsTobet);
                        } else if (maxAttempts < 2) {
                            updatePointsFlag =  updatePoints(player,0);
                        }
                        if(updatePointsFlag) {
                            mHandler.postDelayed(menuRunnable, 5000); }
                    } else {
                        saveData();

                        toast = Toast.makeText(context, "Solution Was Unsuccessful", (duration));
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        //updatePlayCryptoAction = db.playCryptoAction(userID, cryptoTitleID, 0);
                        if (maxAttempts==0) {
                            updatePlayCryptoAction = db.playCryptoAction(userID, cryptoTitleID, 0);
                            // Delete cryptogram data
                            data.clear();
                            editor.remove(player);
                            editor.apply();

                            updatePointsFlag =  updatePoints(player,-1 * pointsTobet);
                            if (updatePointsFlag) {
                                submitAnswerButton.setEnabled(false);
                                decodeButton.setEnabled(false);
                                mHandler.postDelayed(menuRunnable, 5000);
                            }
                        }
                    }
                }
            }

        });

    }


 ;
    private boolean updatePoints(String usr, int points) {

        String mesg1 = "Cryptogram game was won and " + Math.abs(points) + " point(s) were added to player score";
        String mesg2 = "Cryptogram game was lost and " + Math.abs(points) + " point(s) were removed from player score";
        String mesg3 = "Cryptogram game was won but No point(s) were added to player score";
        String mesg4 = "Error Updating Points in Database";
        boolean updateFlag = true;

        if (points > 0) {
            updateFlag = db.adjustPlayerPoints(usr, "increase", Math.abs(points));
            toast = Toast.makeText(context, mesg1, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else if (points < 0) {
            updateFlag = db.adjustPlayerPoints(usr, "decrease", Math.abs(points));
            toast = Toast.makeText(context, mesg2, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else if (points == 0) {
            updateFlag = true;
            toast = Toast.makeText(context, mesg3, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        if(!updateFlag){
            toast = Toast.makeText(context, mesg4, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        return updateFlag;
    }

    private boolean checkValidPoints(String s, int player_points){

        boolean pointsFlag = true;
        String mesg1 = "Points must be an integer between 1 and 10 inclusive";
        String mesg2 = "Points field is blank";
        String mesg3 = "Points must be less then your total points";

        if ((!s.isEmpty()) && (s != null) && (s.matches("[0-9]+"))) {
            pointsTobet = Integer.parseInt(s.toString());
            if ((pointsTobet > 10) || (pointsTobet < 1)) {
                points.setError(mesg1);
                pointsFlag = false;
            }
            if ((pointsTobet > player_points)){
                points.setError(mesg3);
                pointsFlag = false;
            }
        }

        if ((s.isEmpty()) || (s == null) || (s.length() < 1)) {
            points.setError(mesg2);
            pointsFlag = false;
        }


        if (pointsFlag) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkValidSolution(String str) {

        boolean allLettersFlag = str.matches("[a-zA-Z]+");
        boolean emptyKeyFlag = str.isEmpty();
        boolean sameCharFlag = false;
        boolean uniqueCharFlag = true;
        boolean errorFlag = false;
        String encodedSet = encodedLetters.getText().toString();
        String mesg1 = "Key Mapping is blank";
        String mesg2 = "Key Mapping must contain only letters";
        String mesg4 = "Keys cannot map to themselves";
        String mesg3 = "Key Mapping must contain " + encodedSet.length() + " letters";
        String mesg5 = "Each letter must have a unique key mapping";

        if (str.length() == encodedSet.length()) {
            for (int i = 0; i < str.length(); i++)
                for (int j = i + 1; j < str.length(); j++)
                    if (str.charAt(i) == str.charAt(j))
                        uniqueCharFlag = false;
        }

        if (str.length() == encodedSet.length()) {
            for (int i = 0; i < str.length(); i++) {
                if (Character.isLetter(str.charAt(i))) {
                    if (Character.toUpperCase(str.charAt(i)) == Character.toUpperCase(encodedSet.charAt(i))) {
                        sameCharFlag = true;
                    }
                }
            }
        }

        if (emptyKeyFlag || str == null || str.length() <1) {
            solution.setError(mesg1);
            errorFlag = true;
        } else if (!allLettersFlag) {
            solution.setError(mesg2);
            errorFlag = true;
        } else if (str.length() != encodedSet.length()) {
            solution.setError(mesg3);
            errorFlag = true;
        } else if (sameCharFlag) {
            solution.setError(mesg4);
            errorFlag = true;
        } else if (!uniqueCharFlag) {
            solution.setError(mesg5);
            errorFlag = true;
        }

        if (errorFlag){
            return false;
        } else {
            return true;
        }

    }

    private void buildFromSet(String str) {
        char[] charray = str.toCharArray();
        fromSet.clear();
        for (char ch : charray) {
            if (Character.isLetter(ch)) {
                fromSet.add(Character.toUpperCase(ch));
            }
        }
    }

    private void getCryptograms() {

        final String player = getIntent().getStringExtra("playerName");
        StringBuilder results = new StringBuilder();

        try{

           Cursor randomCrypto = db.randomCrypto(Integer.toString(userID));

            // No cryptogram exists
            if (randomCrypto.getCount() == 0) {
                CharSequence text = "No More Random Cryptogram";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(SolveCryptogram.this, PlayerMainMenu.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            } else if (randomCrypto.getCount() > 1) {   // More than one cryptogram returned
                CharSequence text = "Database Error Return more than one cryptogram";
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }  else if (randomCrypto.getCount() == 1){
                randomCrypto.moveToFirst();
                encodedPhrase_str = randomCrypto.getString(
                        randomCrypto.getColumnIndex("cryptogram_encoded_phrase"));
                hint_str = randomCrypto.getString(
                        randomCrypto.getColumnIndex("cryptogram_hint"));
                cryptoTitle_str = randomCrypto.getString(
                        randomCrypto.getColumnIndex("cryptogram_title"));
                correct_unencodedPhrase_str = randomCrypto.getString(
                        randomCrypto.getColumnIndex("cryptogram_solution"));

                attempts.setText(Integer.toString(maxAttempts));
                cryptoTitle.setText(cryptoTitle_str);
                encodedPhrase.setText(encodedPhrase_str);

                buildFromSet(encodedPhrase.getText().toString());
                Iterator<Character> itr = fromSet.iterator();
                while(itr.hasNext()){
                    results.append(itr.next());
                }
                encodedLetters.setText(results.toString());
                unencodedPhrase.setText("");
                randomCrypto.close(); //close the cursor
            }
        } catch (Exception e)   {
            CharSequence text = "Could not retrieve and display the cryptogram.";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }


    private void initalize(String user, String game) {

        cryptoTitle_str = "Cryptogram Test";  //replace
        encodedPhrase_str = "Faefcdb$#@123";  //replace
        hint_str = "Test Hint";
        correct_unencodedPhrase_str = "Testing$#@123";

        StringBuilder results = new StringBuilder();

        attempts.setText(Integer.toString(maxAttempts));
        cryptoTitle.setText(cryptoTitle_str);
        encodedPhrase.setText(encodedPhrase_str);

        buildFromSet(encodedPhrase.getText().toString());
        Iterator<Character> itr = fromSet.iterator();
        while(itr.hasNext()){
            results.append(itr.next());
        }

        encodedLetters.setText(results.toString());

    }



    private void decode(String str) {
        Character lowerCase_str;
        Character upperCase_str;
        StringBuilder results = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {

            //check if character is a letter
            if (Character.isLetter(str.charAt(i))) {
                //convert to upper case
                upperCase_str = Character.toUpperCase(str.charAt(i));
                //check if letter hash map contains the upper case as a key
                if (letterHM.containsKey(upperCase_str)) {
                    //If letter is a key, check if it is upper case
                    if (Character.isUpperCase(str.charAt(i))) {
                        //append the upper case mapped letter to the string
                        results.append(letterHM.get(str.charAt(i)));
                    } else {
                        //convert letter to Upper case key
                        upperCase_str = Character.toUpperCase(str.charAt(i));
                        //get the mapped letter based off the key and convert back to lower case
                        lowerCase_str = Character.toLowerCase(letterHM.get(upperCase_str));
                        //append the lower case mapped letter to the string
                        results.append(lowerCase_str);
                    }

                } else {
                    results.append(str.charAt(i));
                }

            } else {
                results.append(str.charAt(i));
            }

        }

        unencodedPhrase.setText(results.toString());
        unencodedPhrase_str = results.toString();
    }

    private void saveData() {
        cryptoInfo.put("attepsRemaining", attempts.getText().toString());
        cryptoInfo.put("cryptoTitle", cryptoTitle_str);
        cryptoInfo.put("encodedPhrase", encodedPhrase_str);
        cryptoInfo.put("encodedLetters", encodedLetters.getText().toString());
        cryptoInfo.put("unencodedPhrase", unencodedPhrase_str);
        cryptoInfo.put("points", point_str);
        cryptoInfo.put("hint", hint_str);
        cryptoInfo.put("correct_unencodedPhrase", correct_unencodedPhrase_str);
        cryptoInfo.put("bet_points", points.getText().toString());
        cryptoInfo.put("solution_field", solution.getText().toString());

        data.put(player, cryptoInfo);

        editor.putString(SolveCryptogram.ATTEMPTSREMAIN, attempts.toString());
        editor.putString(SolveCryptogram.ANSWER, unencodedPhrase_str);
        editor.putString(SolveCryptogram.CRYPTOTITLE, cryptoTitle_str);
        editor.putString(SolveCryptogram.ENCODEDLETTERS, encodedLetters.toString());
        editor.putString(SolveCryptogram.HINT, hint_str);
        editor.putString(SolveCryptogram.POINTS, point_str);

        //SharedPreferences.Editor editor = sharedPreferencesGame.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(player, json);
        editor.apply();
    }

    private void loadData() {
        Gson gson = new Gson();
        String json =  sharedPreferencesGame.getString(player, null);
        submitPoints.setEnabled(false);
        decodeButton.setEnabled(false);
        clearButton.setEnabled(false);
        submitAnswerButton.setEnabled(false);

        if (json != null) {
                data =  gson.fromJson(json, HashMap.class);
        }

        if(data == null || data.isEmpty() || (data.get(player) != null && data.get(player).get("cryptoTitle").isEmpty())) {
            data = new HashMap<>();
            toast = Toast.makeText(context, "Enter points to bet to solve the cryptogram.", (duration));
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else  {
            LinkedTreeMap<String, String> userData = data.get(player);
            encodedPhrase_str = userData.get("encodedPhrase");
            hint_str = userData.get("hint");
            cryptoTitle_str = userData.get("cryptoTitle");
            correct_unencodedPhrase_str = userData.get("correct_unencodedPhrase");

            StringBuilder results = new StringBuilder();

            attempts.setText(userData.get("attepsRemaining"));
            cryptoTitle.setText(userData.get("cryptoTitle"));
            encodedPhrase.setText(userData.get("encodedPhrase"));
            points.setText(userData.get("bet_points"));
            solution.setText(userData.get("solution_field"));

            if(Integer.parseInt(userData.get("attepsRemaining")) < 3)
            {
                hintOutput.setText(userData.get("hint"));
            }

            points.setEnabled(false);
            submitPoints.setEnabled(false);
            decodeButton.setEnabled(true);
            clearButton.setEnabled(true);
            submitAnswerButton.setEnabled(true);

            buildFromSet(encodedPhrase.getText().toString());
            Iterator<Character> itr = fromSet.iterator();
            while (itr.hasNext()) {
                results.append(itr.next());
            }
            encodedLetters.setText(results.toString());
            unencodedPhrase.setText("");

        }
    }

}
