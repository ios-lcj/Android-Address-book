package com.lcj.myapplication.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.lcj.myapplication.R;
import com.xujiaji.happybubble.BubbleDialog;

/**
 * 自定义可操作性dialog
 * Created by JiajiXu on 17-12-11.
 */

public class CustomOperateDialogView extends BubbleDialog implements View.OnClickListener {
    private ViewHolder mViewHolder;
    private OnClickCustomButtonListener mListener;

    public CustomOperateDialogView(Context context) {
        super(context);
        calBar(true);
        setTransParentBackground();
        setOffsetX(100);
        setOffsetY(10);
//        setLayout(300, 250,0);
        View rootView = LayoutInflater.from(context).inflate(R.layout.bubble_dialog, null);
        mViewHolder = new ViewHolder(rootView);
        addContentView(rootView);
        mViewHolder.buttonDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (mListener != null)
        {
            mListener.onClick();
        }
    }

    private static class ViewHolder
    {
        Button buttonDelete;
        public ViewHolder(View rootView)
        {
            buttonDelete = rootView.findViewById(R.id.buttonDelete);
        }
    }

    public void setClickListener(OnClickCustomButtonListener listener)
    {
        this.mListener = listener;
    }

    public interface OnClickCustomButtonListener
    {
        void onClick();
    }
}

