package com.example.aims;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    EditText name, id, pass, conf_pass,adminPass;
    Button register,back;
    FirebaseAuth firebaseAuth;
    int cnt=0;
    String adpass;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.editText4);
        id = findViewById(R.id.editText8);
        pass = findViewById(R.id.editText11);
        conf_pass = findViewById(R.id.editText12);
        register = findViewById(R.id.button3);
        back = findViewById(R.id.button4);
        adminPass = (EditText) findViewById(R.id.adminPass);

        // Read from the database
        myRef = FirebaseDatabase.getInstance().getReference().child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                adpass = dataSnapshot.child("Pass").getValue(String.class).toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        firebaseAuth  =  FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Register.this);
                progressDialog.setMessage("Wait while");
                progressDialog.show();
                String ADMIN = adminPass.getText().toString();
                if(validate() && ADMIN.equals(adpass)){
                    ///Upload Data to database..
                    String user_email = id.getText().toString().trim();
                    String password  = conf_pass.getText().toString().trim();
                    String c_pass = pass.getText().toString().trim();
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users");
                    myRef.child("Email").setValue(user_email);
                    myRef.child("pass1").setValue(password);
                    myRef.child("pass2").setValue(c_pass);
                    firebaseAuth.createUserWithEmailAndPassword(user_email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && cnt<5) {
                                Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                cnt++;
                                progressDialog.dismiss();
                                startActivity(new Intent(Register.this,MainActivity.class));
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed Attempt",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Admin Pass wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean validate(){
        Boolean res = false;

        String i = id.getText().toString();
        String p = pass.getText().toString();
       String x = pass.getText().toString().trim();
       String y = conf_pass.getText().toString().trim();

        if(i.isEmpty() || p.isEmpty()){
            Toast.makeText(this,"Please Enter all the field",Toast.LENGTH_SHORT).show();
        }
        else if (!x.equals(y)){
            Toast.makeText(this,"Passord and Confirm Passord didn't match",Toast.LENGTH_SHORT).show();
        }
        else{
            res = true;

        }
        return res;
    }
}
