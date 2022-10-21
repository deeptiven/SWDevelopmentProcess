package edu.gatech.seclass.sdpencryptor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SDPEncryptorActivity extends AppCompatActivity {

    private EditText message;
    private EditText key;
    private EditText rotate;
    private EditText encryptedMessage;
    private Button encryptButton;
    private String messageError;
    private String keyError;
    private String shiftError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (EditText)findViewById(R.id.messageInput);
        key = (EditText)findViewById(R.id.keyPhrase);
        rotate = (EditText)findViewById(R.id.shiftNumber);
        encryptedMessage = (EditText)findViewById(R.id.cipherText);
        encryptButton = (Button)findViewById(R.id.encryptButton);

        rotate.setText("0");

        messageError = getString(R.string.errormessage);
        keyError = getString(R.string.errorkey);
        shiftError = getString(R.string.errorshift);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorstatus = false;
                if(message.getText().toString().isEmpty() || message.getText().toString().matches("[0-9]+")) {
                    errorstatus = true;
                    message.setError(messageError);
                }
                if(key.getText().toString().isEmpty() || !key.getText().toString().matches("[a-zA-Z]+")) {
                    errorstatus = true;
                    key.setError(keyError);
                }
                if(!rotate.getText().toString().matches("[0-9]*") || Integer.parseInt(rotate.getText().toString())<1) {
                    errorstatus = true;
                    rotate.setError(shiftError);
                }
                if(!errorstatus) {
                    encryptedMessage.setText(rotate(encrypt(message.getText().toString(), key.getText().toString()), Integer.parseInt(rotate.getText().toString())));
                } else {
                    encryptedMessage.setText("");
                }
            }
        });
    }

    public static String reverse(String str, int start, int end) {
        char[] arr = str.toCharArray();
        while(start < end) {
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;

        }
        return new String(arr);
    }

    public static String rotate(String str, int pos) {
        pos = pos % str.length();
        str = reverse(str, 0 , pos-1);
        str = reverse(str, pos, str.length()-1);
        str = reverse(str, 0 , str.length()-1);
        return str;
    }

    public String encrypt(String message, String key) {
        String cipherText = "";
        int k = 0 ;
        key = key.toLowerCase();
        for(int i=0;i<message.length();i++) {
            int asciiValue = (int)message.charAt(i) + (int)(key.charAt(k % key.length()) % 97);
            if(message.charAt(i) >= 'a' && message.charAt(i) <= 'z') {
                if(asciiValue <= 122) {
                    cipherText+= (char)asciiValue;
                } else {
                    cipherText+= (char)(asciiValue - 122 + 97 -1);
                }
            } else if(message.charAt(i) >= 'A' && message.charAt(i) <= 'Z') {
                if(asciiValue  <= 90) {
                    cipherText+= (char)asciiValue;
                } else {
                    cipherText+= (char)(asciiValue - 90 +65 -1);
                }
            } else {
                cipherText+= message.charAt(i);
            }
            k++;
        }
        return cipherText;
    }
}
