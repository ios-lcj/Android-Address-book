package com.lcj.myapplication.Utils;

import com.lcj.myapplication.pojo.Contact;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Contact> {

    @Override
    public int compare(Contact arg0, Contact arg1) {
        // TODO Auto-generated method stub
        if (arg0.getLetter().equals("@") || arg1.getLetter().equals("#")) {
            return -1;
        } else if (arg0.getLetter().equals("#") || arg1.getLetter().equals("@")) {
            return 1;
        } else {
            // 升序

            return arg0.getLetter().compareTo(arg1.getLetter());
            //    return arg1.getLetter().compareTo(arg0.getLetter()); // 降序
        }
    }
}