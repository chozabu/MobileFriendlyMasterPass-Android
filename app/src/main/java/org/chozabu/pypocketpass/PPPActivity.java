package org.chozabu.pypocketpass;

import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import java.math.BigInteger;

import org.chozabu.pypocketpass.SCrypt;

/**
 * A PassWord Generator.
 */
public class PPPActivity extends AppCompatActivity { //implements LoaderCallbacks<Cursor> {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText mPassOutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppp);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPassOutView = (EditText) findViewById(R.id.passOut);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }




    /**
     * Generates a password from inputs
     */
    private void attemptLogin() {
        String text = mPasswordView.getText().toString();
        String site = mEmailView.getText().toString();
        String shash = SCrypt.scrypt(text, site);
        String words = "word1 word2 word3";
        String[] wlist = words.split(" ");
        String result = "";

        int desired_word_count = 5;
        int chunklen = shash.length() / desired_word_count;

        int ival = 0;
        for (int i = 0; i < desired_word_count; i++) {
            String chunk = shash.substring(i * chunklen, (i + 1) * chunklen);
            ival = new BigInteger(chunk.getBytes()).intValue();
            result = result.concat(wlist[ival % wlist.length]);
            result = result.concat(".");
        }
        result = result.concat(Integer.toString(ival));
        mPassOutView.setText(result);
    }

}

