package com.lcj.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lcj.myapplication.Utils.CharacterParser;
import com.lcj.myapplication.Utils.PinyinComparator;
import com.lcj.myapplication.adapter.ContactAdapter;
import com.lcj.myapplication.databinding.ActivityMainBinding;
import com.lcj.myapplication.pojo.Contact;
import com.lcj.myapplication.view.CustomOperateDialogView;
import com.lcj.myapplication.view.SideBarView;
import com.xujiaji.happybubble.Auto;
import com.xujiaji.happybubble.BubbleDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Contact> mContact = new ArrayList<>();
    private ContactAdapter viewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CharacterParser mParser = CharacterParser.getInstance();
    private PinyinComparator mComparator = new PinyinComparator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initData();
    }

    private void initData() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            String[] list = { Manifest.permission.READ_CONTACTS };
            ActivityCompat.requestPermissions(this, list, 1);
        }else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts();
            }
        }
    }

    private void readContacts(){
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        cursor.getCount();
        ArrayList<Contact> data = new ArrayList<>();
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contact contact = new Contact();
            contact.setName(name);
            contact.setNumber(number);
            data.add(contact);
        }
        cursor.close();

        mContact = filledData(data);
        Collections.sort(mContact, mComparator);

        binding.recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(layoutManager);
        viewAdapter = new ContactAdapter(mContact);
        viewAdapter.setOnItemClickListener(new ContactAdapter.MyItemOnClickListener() {
            @Override
            public void onItemOnClick(View view, Contact contact) throws IOException {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        });
        viewAdapter.setOnItemLongClickListener(new ContactAdapter.MyItemOnLongClickListener() {
            @Override
            public void onItemLongClick(View view, final Contact contact) throws IOException {
                initPopWindow(view, contact);
            }
        });
        binding.recyclerview.setAdapter(viewAdapter);
        binding.viewSidebar.setLetterTouchListener(new SideBarView.LetterTouchListener(){
            @Override
            public void setLetter(String letter) {
                for(int i = 0 ; i < mContact.size(); i++ ){
                    if(letter.equals(mContact.get(i).getLetter())){
                        binding.recyclerview.scrollToPosition(i);
                    }
                }
            }
        });
    }

    // 为list填充数据
    private ArrayList<Contact> filledData(ArrayList<Contact> data){
        ArrayList<Contact> list = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            Contact sm = new Contact();
            sm.setName(data.get(i).getName());
            sm.setNumber(data.get(i).getNumber());
            String pinyin = mParser.getSelling(data.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sm.setLetter(sortString);
            } else {

                sm.setLetter("#");
            }
            list.add(sm);
        }
        return list;
    }

    private void initPopWindow(View v, final Contact contact) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble_dialog, null, false);
        Button btn_xixi = (Button) view.findViewById(R.id.buttonDelete);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setHeight(200);
        popWindow.setBackgroundDrawable(new ColorDrawable(0xBDBFC1));
        popWindow.getBackground().setAlpha(100);    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 800, 0);

        //设置popupWindow里的按钮的事件
        btn_xixi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContact.remove(contact);
                viewAdapter.notifyDataSetChanged();
                popWindow.dismiss();
            }
        });
    }


}