package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;


public class AddNewCryptogram extends AppCompatActivity {

    Spinner fromDropDown;
    Spinner toDropDown;

    Button replaceButton;
    Button clearButton;
    Button playerMenuButton;
    Button saveCryptoButton;

    EditText cryptoTitle;
    EditText unencodedPhrase;
    EditText hintPhrase;
    TextView encodedPhrase;

    String cryptoTitle_str;
    String unencodedPhrase_str;
    String hintPhrase_str;
    String encodedPhrase_str;

    Map<Character, Character> letterHM;

    List<Character> alphaList;
    List<Character> toList;
    List<Character> fromList;

    TreeSet<Character> fromSet;
    TreeSet<Character> toSet;
    TreeSet<Character> removedFromSet;
    TreeSet<Character> removedToSet;

    Character fromLetter;
    Character toLetter;

    Character[] aphabetVector;

    SharedPreferences sharedPreferences;

    final int points = 5;

    private boolean titleFlag;
    private boolean UnencodedPhraseFlag;
    private boolean hintFlag;
    private boolean encodedPhraseFlag;
    private boolean createNewCryptogramFlag;

    private Database db;
   // LocalDate date = LocalDate.now();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy ");
//    DateTimeFormatter formatter;

    Context context;
    int duration = Toast.LENGTH_SHORT/2; //1 second
    int userID;
    Toast toast;

    private Handler mHandler;
    private Runnable menuRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Database db = new Database(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cryptogram);

        this.db = new Database(this);
        final String player = getIntent().getStringExtra("playerName");
        userID =  db.getUserid(player);

        context = getApplicationContext();
        toast = new Toast(context);


        letterHM = new HashMap<>();
        alphaList = new ArrayList<>();

        fromSet = new TreeSet<>();
        toSet = new TreeSet<>();
        removedFromSet = new TreeSet<>();
        removedToSet = new TreeSet<>();

        fromList = new ArrayList<>();
        toList = new ArrayList<>();

        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            toSet.add(ch);
            alphaList.add(ch);
            letterHM.put(ch, ch);
        }

        aphabetVector = alphaList.toArray(new Character[alphaList.size()]);

        cryptoTitle = findViewById(R.id.cryptoTitleInput);
        unencodedPhrase = findViewById(R.id.unencodedPhraseInput);
        encodedPhrase = findViewById(R.id.encodedPhrase);
        hintPhrase = findViewById(R.id.hintInput);

        fromDropDown = findViewById(R.id.fromSpinner);
        toDropDown = findViewById(R.id.toSpinner);

        cryptoTitle.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                cryptoTitle_str = s.toString();
            }
        });

        hintPhrase.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                hintPhrase_str = s.toString();
            }
        });

        unencodedPhrase.setEnabled(true);
        unencodedPhrase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                unencodedPhrase_str = s.toString();
                encrypt(unencodedPhrase_str);

                char[] charray = unencodedPhrase.getText().toString().toCharArray();
                //use tree set to get unique characters from unencodedPhrase input
                fromSet.clear();

                for (char ch : charray) {
                    if (Character.isLetter(ch)) {
                        fromSet.add(Character.toUpperCase(ch));
                    }
                }


                if (removedFromSet.size() > 0) {
                    //Removed previously mapped letters from From Set
                    fromSet.removeAll(removedFromSet);
                }

                //toSet.removeAll(fromSet);

                if (removedToSet.size() > 0) {
                    //Removed previously mapped letters from To Set
                    toSet.removeAll(removedToSet);
                }

                fromList.clear();
                fromList.addAll(fromSet);    //convert from set to ArrayList

                toList.clear();
                toList.addAll(toSet);

                ArrayAdapter<Character> fromArrayAdapter = new ArrayAdapter<>
                        (getApplicationContext(), android.R.layout.simple_spinner_item, fromList);
                fromDropDown.setAdapter(fromArrayAdapter);

                ArrayAdapter<Character> toArrayAdapter = new ArrayAdapter<>
                        (getApplicationContext(), android.R.layout.simple_spinner_item, toList);
                toDropDown.setAdapter(toArrayAdapter);

            }
        });


        fromDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (fromList.size() > 0) {
                    fromLetter = fromList.get(pos);
                } else {
                    fromLetter = ' ';
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        toDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (toList.size() > 0) {
                    toLetter = toList.get(pos);
                } else {
                    toLetter = ' ';
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        replaceButton = findViewById(R.id.replaceButton);
        replaceButton.setEnabled(true);
        replaceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                letterHM.put(Character.toUpperCase(fromLetter), Character.toUpperCase(toLetter));

                if ((toList.size() > 0 && fromList.size() > 0) && (fromLetter != toLetter)) {

                    encrypt(unencodedPhrase_str);

                    removedFromSet.add(Character.toUpperCase(fromLetter));
                    removedToSet.add(Character.toUpperCase(toLetter));
                    fromList.remove(Character.valueOf(fromLetter));
                    toList.remove(Character.valueOf(toLetter));

                    ArrayAdapter<Character> fromArrayAdapter = new ArrayAdapter<>
                            (getApplicationContext(), android.R.layout.simple_spinner_item, fromList);
                    fromDropDown.setAdapter(fromArrayAdapter);

                    ArrayAdapter<Character> toArrayAdapter = new ArrayAdapter<>
                            (getApplicationContext(), android.R.layout.simple_spinner_item, toList);
                    toDropDown.setAdapter(toArrayAdapter);

                }

                if (fromLetter == toLetter) {
                    toast = Toast.makeText(context, "Letters cannot map to themselves", duration);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }

            }
        });

        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                toSet.clear();
                alphaList.clear();
                letterHM.clear();
                removedFromSet.clear();
                removedToSet.clear();

                for (char ch = 'A'; ch <= 'Z'; ++ch) {
                    toSet.add(ch);
                    alphaList.add(ch);
                    letterHM.put(ch, ch);
                }

                unencodedPhrase.setText("");
                encodedPhrase.setText("");
            }

        });

        menuRunnable = new Runnable() {
            @Override
            public void run() {

                cryptoTitle.setText("");
                unencodedPhrase.setText("");
                encodedPhrase.setText("");
                hintPhrase.setText("");

                fromSet.clear();
                toSet.clear();
                alphaList.clear();
                letterHM.clear();
                removedFromSet.clear();
                removedToSet.clear();

                for (char ch = 'A'; ch <= 'Z'; ++ch) {
                    toSet.add(ch);
                    alphaList.add(ch);
                    letterHM.put(ch, ch);
                }

                unencodedPhrase.setEnabled(false);
                replaceButton.setEnabled(false);

                Intent intent = new Intent(AddNewCryptogram.this, PlayerMainMenu.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        };

        playerMenuButton = findViewById(R.id.menuButton);
        playerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewCryptogram.this, PlayerMainMenu.class);
                intent.putExtra("playerName", player);
                startActivity(intent);
                finish();
            }
        });

        saveCryptoButton = findViewById(R.id.saveCryptoButton);
        saveCryptoButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v){

                boolean updateFlag = true;
                mHandler = new Handler();
                CharSequence errorMsg = "Create Cryptogram Database error";
                CharSequence successMsg;
                titleFlag = checkValidCryptoTitle(cryptoTitle.getText().toString());
                UnencodedPhraseFlag = checkValidUnencodedPhrase(unencodedPhrase.getText().toString());
                hintFlag = checkValidHint(hintPhrase.getText().toString());
                encodedPhraseFlag = checkValidEncodedPhrase(encodedPhrase.getText().toString(), unencodedPhrase.getText().toString());



                if ((titleFlag) && (UnencodedPhraseFlag) && (hintFlag) && (encodedPhraseFlag)) {

                    createNewCryptogramFlag = AddNewCryptogram.this.db.createCryptogramAction(cryptoTitle_str, unencodedPhrase.getText().toString(), hintPhrase_str,
                            encodedPhrase_str, 1, userID , format.format(calendar.getTime()));

                    if (!createNewCryptogramFlag) {
                        toast = Toast.makeText(context, errorMsg, duration);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        mHandler.postDelayed(menuRunnable, 3000);

                    } else {
                        if (AddNewCryptogram.this.db.adjustPlayerPoints(player, "increase", 5)) {
                            successMsg = "Cryptogram "+ cryptoTitle_str +" was successfully created and 5 points were added to player score.";
                        } else {
                            successMsg = "Cryptogram "+ cryptoTitle_str +" was successfully created but error adding 5 points to player score.";
                        }
                        toast = Toast.makeText(context, successMsg, duration);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        mHandler.postDelayed(menuRunnable, 3000);
                        }
                }
            }
        });

    }



    private boolean checkValidCryptoTitle(String tmpTitle) {

        boolean emptyFlag = tmpTitle.isEmpty();
        boolean uniqueTitleFlag = true;
        String mesg1 = "Missing Cryptogram Title";
        String mesg2= "Cryptogram Title Already Exist";

        uniqueTitleFlag =  db.uniqueCryptoTitle(tmpTitle);

        if((tmpTitle == null) || (emptyFlag) || (tmpTitle.length() < 1)){
            cryptoTitle.setError(mesg1);
            return false;
        } if(!uniqueTitleFlag) {
            cryptoTitle.setError(mesg2);
            return false;
        } else {
            return true;
        }


    }

    private boolean checkValidHint(String str) {

        boolean emptyFlag = str.isEmpty();
        String mesg = "Missing Hint";

        if((str == null) || (emptyFlag) || (str.length() < 1)){
            hintPhrase.setError(mesg);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkValidEncodedPhrase(String encoded, String unencoded) {

        boolean letterFlag = encoded.matches(".*[a-zA-Z]+.*");
        boolean emptyFlag = encoded.isEmpty();
        boolean sameCharFlag = false;

        CharSequence mesg1 = "Missing Encoded Phrase";
        CharSequence mesg2 = "Encoded Phrase must contain at least one letter";
        CharSequence mesg3 = "Unique encoded letters must replace each unique letter";

        for (int i= 0; i <unencoded.length(); i++) {
            if (Character.isLetter(unencoded.charAt(i))) {
                if (encoded.charAt(i) ==  unencoded.charAt(i)) {
                    sameCharFlag = true;
                }
            }
        }

            if((encoded == null) || (emptyFlag) || (encoded.length() < 1)){
            toast = Toast.makeText(context, mesg1, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return false;
        } else if (!letterFlag)   {
            toast = Toast.makeText(context, mesg2, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return false;
        } else if (sameCharFlag)   {
            toast = Toast.makeText(context, mesg3, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkValidUnencodedPhrase(String str) {

        boolean letterFlag = str.matches(".*[a-zA-Z]+.*");
        boolean emptyLetterFlag = str.isEmpty();
        String mesg1 = "Unencoded Phrase is blank";
        String mesg2 = "Unencoded Phrase must contain at least one letter";

        if((str == null)  || (emptyLetterFlag) || (str.length() < 1)) {
            unencodedPhrase.setError(mesg1);
            return false;
        } else if ((!letterFlag)) {
            unencodedPhrase.setError(mesg2);
            return false;
        } else {
            return true;
        }

    }

    private void encrypt(String str) {
        // str = "tEsting";
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

        encodedPhrase.setText(results.toString());
        encodedPhrase_str = results.toString();
    }

}
