package com.lcj.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lcj.myapplication.R;
import com.lcj.myapplication.pojo.Contact;


import java.io.IOException;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    private ArrayList<Contact> mContact;
    public MyItemOnClickListener mListener;
    public MyItemOnLongClickListener mLongListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView numberView;
        private TextView letterView;
        private TextView tv_item_tag;
        private RelativeLayout userView;

        public MyViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            numberView = (TextView) view.findViewById(R.id.number);
            letterView = (TextView) view.findViewById(R.id.letter);
            tv_item_tag = (TextView) view.findViewById(R.id.tv_item_tag);
            userView = (RelativeLayout) view.findViewById(R.id.user);
        }
    }

    public ContactAdapter(ArrayList<Contact> mContact) {
        for (int i = 0; i < mContact.size(); i++) {
            for (int j = i + 1; j < mContact.size(); j++) {
                if (mContact.get(i).getName().equals(mContact.get(j).getName())) {
                    String number = mContact.get(i).getNumber() + "\n" + mContact.get(j).getNumber();
                    mContact.get(i).setNumber(number);
                    mContact.remove(j);
                }
            }
        }

        this.mContact = mContact;
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_contact, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String name = String.valueOf(mContact.get(position).getName());
        String number = String.valueOf(mContact.get(position).getNumber());
        String letter = String.valueOf(mContact.get(position).getLetter());
        if (!letterCompareSection(position)) {
            holder.tv_item_tag.setText(letter);
            holder.tv_item_tag.setVisibility(View.VISIBLE);
        } else {
            holder.tv_item_tag.setVisibility(View.GONE);
        }
        holder.nameView.setText(name);
        holder.numberView.setText(number);
        holder.letterView.setText(letter);
        if(mListener != null){
            holder.userView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mListener.onItemOnClick(v, mContact.get(position));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if(mLongListener != null){
            holder.userView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        mLongListener.onItemLongClick(v, mContact.get(position));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContact.size();
    }

    private Boolean letterCompareSection(int position) {
        if (position == 0) {
            return false;
        }
        String letter1 = mContact.get(position).getLetter();
        String letter2 = mContact.get(position - 1 ).getLetter();
        Boolean result = letter1.equals(letter2);
        return result;
    }

    public void setOnItemClickListener(MyItemOnClickListener listener){
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(MyItemOnLongClickListener listener) {
        this.mLongListener = listener;
    }

    public interface MyItemOnClickListener {
        void onItemOnClick(View view, Contact contact) throws IOException;
    }

    public interface MyItemOnLongClickListener {
        void onItemLongClick(View view, Contact contact) throws IOException;
    }

}
