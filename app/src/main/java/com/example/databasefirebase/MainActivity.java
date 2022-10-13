package com.example.databasefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.databasefirebase.Model.UserModel;
import com.example.databasefirebase.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
FirebaseDatabase database;
DatabaseReference reference,reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         database = FirebaseDatabase.getInstance();
         reference =database.getReference("Details");
         reference1 = database.getReference("Names");
        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.show();

                String name =  binding.Name.getText().toString();
                String email = binding.Email.getText().toString();
                String password = binding.Password.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                      progressDialog.dismiss();
                    return;
                }
                else if(email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter Email !!", Toast.LENGTH_SHORT).show();
                      progressDialog.dismiss();
                    return;
                }
                else if(password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter Password !!", Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
                    return;
                }
                else
                {
                    UserModel userModel = new UserModel(name,email,password);
                    reference1.child(password).setValue(userModel);
                    reference.child(name).setValue(userModel);
                    progressDialog.dismiss();

                }
            }
        });
        binding.retrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.show();
                String name =  binding.Name.getText().toString();
                String email = binding.Email.getText().toString();
                String password = binding.Password.getText().toString();
                  reference1.child(password).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Email = String.valueOf(snapshot.child("email").getValue());
                        String Name = String.valueOf(snapshot.child("name").getValue());
                        binding.Name.setText(Name);
                        binding.Email.setText(Email);
                        progressDialog.dismiss();
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                          Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();
                      }
                  });
            }
        });
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.show();
                String Name = binding.refName.getText().toString();
                String Email = binding.updateEmail.getText().toString();
                HashMap map = new HashMap();
                map.put("email",Email);
                reference.child(Name).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                      if(task.isSuccessful()){
                          binding.updateEmail.setText("");
                          binding.refName.setText("");
                          Toast.makeText(MainActivity.this, "Suceesfull !!", Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();
                      }else{
                          Toast.makeText(MainActivity.this, "Failed !!", Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();
                      }
                    }
                });
            }
        });
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.show();
                String Name = binding.refName.getText().toString();
                reference.child(Name).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          binding.refName.setText("");
                          Toast.makeText(MainActivity.this, "Deletion Sucessfull !", Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();
                      }
                      else{
                      Toast.makeText(MainActivity.this, "Failed Deletion !!", Toast.LENGTH_SHORT).show();
                      }
                    }
                });
            }
        });
    }
}