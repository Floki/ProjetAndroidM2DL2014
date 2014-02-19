package com.nadt.keepthebeat;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomeScreenShortcutAdapter extends BaseAdapter {


	Context context;
	
    HomeScreenShortcutAdapter(Context c) {
    	context = c;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        final Object data = getItem(position);

        if (convertView == null) {

            tv = new TextView(context);
            tv.setGravity(Gravity.CENTER);

        } else {
            tv = (TextView) convertView;
        }

        CharSequence title = "Title";

        tv.setCompoundDrawablesWithIntrinsicBounds(
                null, null, null, null);
        tv.setText(title);
        tv.setTag(data);

        return tv;
    }

}
