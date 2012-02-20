package com.vdom.api;

import com.vdom.core.Cards;
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

    public Integer getId();
    
    public void isBought(MoveContext context);
}
