package com.guilsch.multivoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class LanguageSpinnerAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    Language[] languageList;

    public LanguageSpinnerAdapter(Context applicationContext) {
        this.inflater = (LayoutInflater.from(applicationContext));
        this.context = applicationContext;
        this.languageList = Param.TARGET_LANGUAGES_FULL;
    }

    @Override
    public int getCount() {
        return languageList.length;
    }

    @Override
    public Object getItem(int position) {
        return languageList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView);
    }

    public View createView(int position, View convertView) {
        View itemView = inflater.inflate(R.layout.adapter_language_selector, null);

        Language lang = Param.TARGET_LANGUAGES_FULL[position];

        TextView langName = itemView.findViewById(R.id.lang_name);
        ImageView langFlag = itemView.findViewById(R.id.lang_flag);

        langFlag.setClipToOutline(true);

        langName.setText(lang.getName());
        langFlag.setImageDrawable(ContextCompat.getDrawable(context, lang.getFlagAddress()));

        return itemView;
    }
}
