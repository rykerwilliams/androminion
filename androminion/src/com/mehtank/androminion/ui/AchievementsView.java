package com.mehtank.androminion.ui;

import com.mehtank.androminion.Androminion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AchievementsView extends FrameLayout {

    public AchievementsView(Context context) {
    	super(context);
    	init(context);
    }
    
	public AchievementsView(Context context, AttributeSet attrs) {
		super(context, attrs);
    	init(context);
	}
    
    private void init(Context top) {
        ScrollView sv = new ScrollView(top);
        sv.setVerticalScrollBarEnabled(true);
        LinearLayout ll = new LinearLayout(top);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(0x66000000);
        
        Achievements achievements = new Achievements((Androminion) top);
        for(int i=0; i < Achievements.keys.length; i++) {
            final boolean b = achievements.hasAchieved(Achievements.keys[i]);
            final CheckBox cb = new CheckBox(top);
            cb.setTextSize(cb.getTextSize() * .75f);
            cb.setText(Achievements.text[i]);
            cb.setChecked(b);
            cb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb.setChecked(b);
                }
            });
            ll.addView(cb);
        }
        
        sv.addView(ll);
        addView(sv);
    }
}
