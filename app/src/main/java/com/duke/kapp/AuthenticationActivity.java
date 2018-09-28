package com.duke.kapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthenticationActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText phone,enterCode;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        mAuth = FirebaseAuth.getInstance();
        phone = findViewById(R.id.phone);
        enterCode = findViewById(R.id.enterCode);

        findViewById(R.id.getCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();

            }
        });
        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();


            }
        });

        }

        private void verifySignInCode(){
            String code = enterCode.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
            if(phone == null){
                Toast.makeText(getApplicationContext(),"Please enter credentials",Toast.LENGTH_SHORT).show();
            }


        }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new Activity
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();



                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"Incorrect verification code",Toast.LENGTH_SHORT).show();

                            }



                        }
                    }
                });
    }



        private void sendVerificationCode(){
            String newPhone = phone.getText().toString();

            if(newPhone.isEmpty()){
                phone.setError("Phone number is required");
                phone.requestFocus();
                return;
            }
            if(newPhone.length() < 10){
                phone.setError("Please enter a valid number");
                phone.requestFocus();
                return;
            }


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    newPhone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks

        }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent = s;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {



            }
        };




    }


