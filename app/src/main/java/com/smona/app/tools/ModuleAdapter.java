package com.smona.app.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by motianhu on 5/11/17.
 */

public class ModuleAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ModuleInfo> mApps;

    public ModuleAdapter(Context context, ArrayList<ModuleInfo> apps) {
        mContext = context;
        mApps = apps;
    }

    @Override
    public int getCount() {
        return mApps.size();
    }

    @Override
    public Object getItem(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModuleInfo info = mApps.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(com.smona.padora.tools.R.layout.module_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(com.smona.padora.tools.R.id.title);
            holder.image = (ImageView) convertView.findViewById(com.smona.padora.tools.R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(info.title);
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        ImageView image;
    }
}
