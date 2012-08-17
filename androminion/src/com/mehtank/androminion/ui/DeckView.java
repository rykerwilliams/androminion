package com.mehtank.androminion.ui;

import java.util.ArrayList;

import com.mehtank.androminion.Androminion;
import com.mehtank.androminion.R;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeckView extends FrameLayout {

	private static float textScale;
	
	Androminion top;

	TextView tv;
	TextView name;
	TextView pirates;
	TextView victoryTokens;
	
	boolean showCardCounts = true;
	
	public static enum ShowCardType {OBTAINED, TRASHED, REVEALED};

	public DeckView(Context context) {
		super(context);
		this.top = (Androminion) context;
		
        if(PreferenceManager.getDefaultSharedPreferences(top).getBoolean("hide_card_counts", false)) {
            showCardCounts = false;
        }
		
		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.LEFT + Gravity.TOP);

		name = new TextView(top);
		name.setLayoutParams(p);
		name.setTextSize(name.getTextSize() * textScale);
		addView(name);		
		
		LinearLayout ll = new LinearLayout(top);
        ll.setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.TOP + Gravity.RIGHT));
		addView(ll);

        LinearLayout.LayoutParams lp;
        
		lp = new LinearLayout.LayoutParams(
		    LinearLayout.LayoutParams.WRAP_CONTENT,
		    LinearLayout.LayoutParams.WRAP_CONTENT,
			Gravity.TOP + Gravity.RIGHT);
		
		pirates = new TextView(top);
		pirates.setTextSize((float) (pirates.getTextSize() * 0.75));
		pirates.setTextColor(Color.YELLOW);
		pirates.setBackgroundResource(R.drawable.pirates);
		pirates.setLayoutParams(lp);
		pirates.setVisibility(INVISIBLE);
		ll.addView(pirates);
		
        lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            Gravity.TOP + Gravity.LEFT);
        
		victoryTokens = new TextView(top);
		victoryTokens.setTextSize((float) (victoryTokens.getTextSize() * 0.75));
		victoryTokens.setTextColor(Color.BLACK);
		victoryTokens.setBackgroundResource(R.drawable.victorytokens);
		victoryTokens.setLayoutParams(lp);
		victoryTokens.setVisibility(INVISIBLE);
        ll.addView(victoryTokens);

        if(showCardCounts) {
    		p = new FrameLayout.LayoutParams(
    				FrameLayout.LayoutParams.WRAP_CONTENT,
    				FrameLayout.LayoutParams.WRAP_CONTENT,
    				Gravity.LEFT + Gravity.BOTTOM);
    		
    		tv = new TextView(top);
    		tv.setTextSize(tv.getTextSize() * textScale);
    		tv.setLayoutParams(p);
    		addView(tv);
        }
	}

	public void set(String nameStr, int deckSize, int handSize, int numCards, int pt, int vt, boolean highlight) {
		name.setText(nameStr);
		if (highlight) {
			name.setTextColor(Color.BLACK);
			name.setBackgroundColor(Color.GRAY);
		} else {
			name.setTextColor(Color.WHITE);
			name.setBackgroundColor(Color.BLACK);			
		}

		pirates.setText(" " + pt + " ");
		if (pt != 0) 
			pirates.setVisibility(VISIBLE);
		else
			pirates.setVisibility(INVISIBLE);

        victoryTokens.setText(" " + vt + " ");
        if (vt != 0) 
            victoryTokens.setVisibility(VISIBLE);
        else
            victoryTokens.setVisibility(INVISIBLE);

        if(showCardCounts) {
    		String str = "\n{ \u2261 " + deckSize + 
    					 "    \u261e " + handSize + 
    					 "    \u03a3 " + numCards + " }";
    		tv.setText(str);
        }
	}

	public static void setTextScale(double textScale) {
		DeckView.textScale = (float) textScale;
	}

	public void showCard(CardView c, ShowCardType type) {
		AlphaAnimation alpha;
		TranslateAnimation trans;
		AnimationSet anims = new AnimationSet(true);
		anims.setInterpolator(new LinearInterpolator());

		int left = getLeft();
		int top = getTop();
		ViewParent vp = getParent();
		while (vp != getRootView()) {
			left += ((View)vp).getLeft();
			top += ((View)vp).getTop();
			vp = vp.getParent();
		}
		
		switch (type) {
		case OBTAINED:
			alpha = new AlphaAnimation(0, 1);
			trans = new TranslateAnimation(
					TranslateAnimation.ABSOLUTE, left, 
					TranslateAnimation.ABSOLUTE, left, 
					TranslateAnimation.ABSOLUTE, top - getHeight()*2, 
					TranslateAnimation.ABSOLUTE, top);
			anims.setInterpolator(new DecelerateInterpolator());
			break;			
		case TRASHED:
			alpha = new AlphaAnimation(1, 0);
			trans = new TranslateAnimation(
					TranslateAnimation.ABSOLUTE, left, 
					TranslateAnimation.ABSOLUTE, left, 
					TranslateAnimation.ABSOLUTE, top, 
					TranslateAnimation.ABSOLUTE, top + getHeight()*2);
			anims.setInterpolator(new AccelerateInterpolator());
			break;
		default: //  REVEALED
			alpha = new AlphaAnimation(1, 0.5f);
			trans = new TranslateAnimation(
					TranslateAnimation.ABSOLUTE, left,
					TranslateAnimation.ABSOLUTE, left, 
					TranslateAnimation.ABSOLUTE, top, 
					TranslateAnimation.ABSOLUTE, top - getHeight()*0.5f);
		}
		anims.addAnimation(alpha);
		anims.addAnimation(trans);
		anims.setDuration(2500L);
		
		anims.setAnimationListener(new CVAnimListener(c));
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(CardView.WIDTH, FrameLayout.LayoutParams.WRAP_CONTENT);
		c.setLayoutParams(lp);
		((ViewGroup) getRootView()).addView(c);
		c.startAnimation(anims);

		cvs.add(c);
		runningAnims.add(anims);
	}

	static ArrayList<AnimationSet> runningAnims = new ArrayList<AnimationSet>();
	static ArrayList<CardView> cvs = new ArrayList<CardView>();
	
	private class CVAnimListener implements AnimationListener {
		CardView v;
		public CVAnimListener(CardView v) {
			this.v = v;
		}
		public void onAnimationEnd(Animation animation) {
			v.setVisibility(GONE);
			runningAnims.remove(animation);
			if (runningAnims.size() == 0) {
				for (CardView c : cvs)
					((ViewGroup) getRootView()).removeView(c);
				cvs.clear();
			}
		}
		@Override public void onAnimationRepeat(Animation animation) {}
		@Override public void onAnimationStart(Animation animation) {}
	}
}
