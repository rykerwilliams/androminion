package com.vdom.api;

import com.vdom.core.Cards.Type;
import com.vdom.core.MoveContext;


public interface Card {
    public Type getType();

    public String getName();

    public String getSafeName();
    
    public String getExpansion();

    public String getStats();

    public String getDescription();

    public int getCost(MoveContext context);

    public int getCost(MoveContext context, boolean buyPhase);

    public boolean costPotion();
    
    public boolean isPrize();
    
    public boolean isShelter();
    
    public boolean isRuins();
    
    public boolean isKnight();
    
    public Integer getId();
    
    public void isBought(MoveContext context);
    
    public void isTrashed(MoveContext context);
    
    //public void isGained(MoveContext context);
}
