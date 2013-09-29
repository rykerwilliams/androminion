package com.mehtank.androminion.server;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;

import com.mehtank.androminion.R;
import com.vdom.api.Card;
import com.vdom.api.GameType;
import com.vdom.core.Player.SpiceMerchantOption;
import com.vdom.core.Player.TorturerOption;

public class Strings {
	@SuppressWarnings("unused")
	private static final String TAG = "Strings";
	
    static HashMap<Card, String> nameCache = new HashMap<Card, String>();
    static HashMap<Card, String> descriptionCache = new HashMap<Card, String>();
    static HashMap<String, String> expansionCache = new HashMap<String, String>();
    static HashMap<GameType, String> gametypeCache = new HashMap<GameType, String>();
	public static Context context;
    
    public static String getCardName(Card c) {
        String name = nameCache.get(c);
        if(name == null) {
           try {
               Resources r = context.getResources();
               int id = r.getIdentifier(c.getSafeName() + "_name", "string", context.getPackageName());
               name = r.getString(id);
           }
           catch(Exception e) {
               e.printStackTrace();
           }
           if(name == null) {
               name = c.getName();
           }
           
           nameCache.put(c, name);
        }
        return name;
    }
    
    public static String getCardDescription(Card c) {
        String description = descriptionCache.get(c);
        if(description == null) {
            try {
                Resources r = context.getResources();
                int id = r.getIdentifier(c.getSafeName() + "_desc", "string", context.getPackageName());
                description = r.getString(id);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            if(description == null) {
                description = c.getDescription();
            }
            
            descriptionCache.put(c, description);
        }
        return description;
    }
    
    public static String getCardExpansion(Card c) {
        if (c.getExpansion().isEmpty()) {
            // Victory cards (e.g. "Duchy") don't have a single expansion;
            // they're both in Base and Intrigue.
            return "";
        }
        
        String expansion = expansionCache.get(c.getExpansion());
        
        if (expansion == null) {
            try {
                Resources r = context.getResources();
                int id = r.getIdentifier(c.getExpansion(), "string", context.getPackageName());
                expansion = r.getString(id);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            
            if(expansion.equals("")) 
            {
                expansion = c.getExpansion();
            }
            
            expansionCache.put(c.getExpansion(), expansion);
        }
        return expansion;
    }
    
    public static String getGameTypeName(GameType g) {
    	
    	String gametype = gametypeCache.get(g);
    	if (gametype==null){
    		try {
    			Resources r = context.getResources();
    			int id = r.getIdentifier(g.name() + "_gametype", "string", context.getPackageName());
    			gametype = r.getString(id);
    		}      
    		catch(Exception e) {
    			//e.printStackTrace();
    		}
    	}
    	if (gametype == null){
//          Fallback is the name in the enumeration    		
    		gametype = g.getName();
    	}
    	
    	gametypeCache.put(g, gametype);
    	return gametype;
    }
    
    public static GameType getGameTypefromName(String s){
    	
    	for (GameType g : GameType.values()) {
    	  if (getGameTypeName(g).equals(s))
    	  { return g; }
    	}
    	return null;
    	}

    
    public static String format(String str, Object... args) {
        return String.format(str, args);
    }

    public static String format(int resId, Object... args) {
        return String.format(context.getString(resId), args);
    }
    
    public static String getString(int resId) {
        return context.getString(resId);
    }

    /**
     * Takes an option object (TODO(matt): make a class that's more restrictive than Object that
     * these options can inherit from) and returns the string the corresponds to the option.
     */
    public static String getOptionText(Object option) {
        if (option instanceof SpiceMerchantOption) {
            // Actually, if this works without a cast, and it appears that it does, we don't need
            // the outer if-statements.  But maybe this way is a little more organized?
            if (option == SpiceMerchantOption.AddCardsAndAction) {
                return getString(R.string.spice_merchant_option_one);
            } else if (option == SpiceMerchantOption.AddGoldAndBuy) {
                return getString(R.string.spice_merchant_option_two);
            }
        } else if (option instanceof TorturerOption) {
            if (option == TorturerOption.TakeCurse) {
                return getString(R.string.torturer_option_one);
            } else if (option == TorturerOption.DiscardTwoCards) {
                return getString(R.string.torturer_option_two);
            }
        }
        throw new RuntimeException("I got passed an option object that I don't understand!");
    }
}
