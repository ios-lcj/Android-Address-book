package com.lcj.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lcj.myapplication.R;
import java.io.IOException;


public class UserNumberAdapter extends RecyclerView.Adapter<UserNumberAdapter.MyViewHolder> {
    private String[] mNumber;
    public MyItemOnClickListener mListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView numberView;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            numberView = (TextView) view.findViewById(R.id.number);
            imageView = (ImageView) view.findViewById(R.id.call_image);
        }
    }

    public UserNumberAdapter(String[] mNumber){
        this.mNumber = mNumber;
    }

    @NonNull
    @Override
    public UserNumberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_number_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String number = String.valueOf(mNumber[position]);
        holder.numberView.setText(number);
        if(mListener != null){
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mListener.onItemOnClick(v, mNumber[position]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mNumber.length;
    }


    public void setOnItemClickListener(MyItemOnClickListener listener){
        this.mListener = listener;
    }

    public interface MyItemOnClickListener {
        void onItemOnClick(View view, String number) throws IOException;
    }

}
