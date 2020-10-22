package com.lcj.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.lcj.myapplication.adapter.UserNumberAdapter;
import com.lcj.myapplication.databinding.ActivityUserBinding;
import com.lcj.myapplication.pojo.Contact;

import java.io.IOException;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private UserNumberAdapter numberAdapter;
    private String[] numbers;
    private String letter;
    private String name;
    private String selectNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Contact user = (Contact)intent.getSerializableExtra("contact");
        if(user != null){
            String number = user.getNumber();
            numbers = number.split("\\n");
            letter = user.getLetter();
            name = user.getName();
        }

        binding.letter.setText(letter);
        binding.name.setText(name);

        binding.userNumberRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.userNumberRecyclerView.setLayoutManager(layoutManager);
        numberAdapter = new UserNumberAdapter(numbers);
        numberAdapter.setOnItemClickListener(new UserNumberAdapter.MyItemOnClickListener() {
            @Override
            public void onItemOnClick(View view, String number) throws IOException {
                selectNumber = number;
                if(ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    String[] list = { Manifest.permission.CALL_PHONE };
                    ActivityCompat.requestPermissions(UserActivity.this, list, 1);
                }else {
                    call();
                }
            }
        });
        binding.userNumberRecyclerView.setAdapter(numberAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                call();
            }else{
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void call(){
        try {
            if(selectNumber != null){
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + selectNumber));
                startActivity(intent);
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }


}