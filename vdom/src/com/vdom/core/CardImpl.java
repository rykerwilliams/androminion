package com.vdom.core;

import com.vdom.api.Card;

public class CardImpl implements Card {
    Cards.Type type;
    String name;
    int cost;
    boolean costPotion = false;

    String description = "";
    String expansion = "";
    protected int vp;
    boolean trashOnUse = false;
    boolean trashed = false;
    int numberTimesAlreadyPlayed = 0;
    boolean isPrize = false;
    boolean isShelter = false;
    boolean isRuins = false;
    boolean isKnight = false;
    int cloneCount = 1;

    private Integer id;

    static int maxNameLen;
    public boolean templateCard = true;
    String safeName;

    protected CardImpl(Cards.Type type, int cost) {
        this.type = type;
        this.name = type.toString();
        if (maxNameLen < name.length()) {
            maxNameLen = name.length();
        }
        this.cost = cost;
    }
    
    public CardImpl(Builder builder) {
        this(builder.type, builder.cost);
        costPotion = builder.costPotion;
        vp = builder.vp;
        description = builder.description;
        expansion = builder.expansion;
        isPrize = builder.isPrize;
        isShelter = builder.isShelter;
        isRuins = builder.isRuins;
        isKnight = builder.isKnight;
        trashOnUse = builder.trashOnUse;
    }

    public static class Builder {
        protected Cards.Type type;
        protected String name;
        protected int cost;
        protected int vp = 0;

        protected boolean costPotion = false;
        protected String description = "";
        protected String expansion = "";

	    protected boolean isPrize = false;
	    protected boolean isShelter = false;
	    protected boolean isRuins = false;
	    protected boolean isKnight = false;
        protected boolean trashOnUse = false;


        public Builder(Cards.Type type, int cost) {
            this.type = type;
            this.name = type.toString();
            this.cost = cost;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder expansion(String val) {
            expansion = val;
            return this;
        }

        public Builder costPotion() {
            costPotion = true;
            return this;
        }

        public Builder vp(int val) {
            vp = val;
            return this;
        }

        public Builder isPrize() {
            isPrize = true;
            return this;
        }
        
        public Builder isShelter() {
        	isShelter = true;
        	return this;
        }

        public Builder isRuins() {
        	isRuins = true;
        	return this;
        }

        public Builder trashOnUse() {
            trashOnUse = true;
            return this;
        }

        public CardImpl build() {
            return new CardImpl(this);
        }

    }
    protected CardImpl() {
    }

    public String getSafeName() {
        /*if(safeName == null) {
            StringBuilder sb = new StringBuilder();
            for(char c : getName().toCharArray()) {
                if(Character.isLetterOrDigit(c)) {
                    sb.append(c);
                }
            }
            safeName = sb.toString();
        }*/
        
        return name;
    }

    protected void checkInstantiateOK() {
        if (!templateCard) {
            Thread.dumpStack();
            Util.debug("Trying to create a real card from a real card instead of a template");
        }
    }

    public CardImpl instantiate() {
        checkInstantiateOK();
        CardImpl c = new CardImpl();
        copyValues(c);
        return c;
    }

    protected void copyValues(CardImpl c) {
        c.templateCard = false;
        c.id = Game.cardSequence++;

        c.type = type;
        c.name = name;
        c.cost = cost;
        c.costPotion = costPotion;
        c.description = description;
        c.expansion = expansion;
        c.isPrize = isPrize;
        c.isShelter = isShelter;
        c.isRuins = isRuins;
        c.isKnight = isKnight;
        c.vp = vp;
    }

    public Cards.Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCost(MoveContext context) {
        if (context == null)
            return cost;
        return getCost(context, context.buyPhase);
    }

    public int getCost(MoveContext context, boolean buyPhase) {
        int costModifier = 0;
        costModifier -= (this instanceof ActionCardImpl) ? (2 * context.quarriesPlayed) : 0;
        costModifier -= (buyPhase && this.equals(Cards.peddler)) ? (2 * context.getActionCardsInPlayThisTurn()) : 0;

        return Math.max(0, cost + costModifier + context.cardCostModifier);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public void play(Game game, MoveContext context) {
    }

    public String getStats() {
        StringBuilder sb = new StringBuilder();
		    sb.append ("(" + cost + (costPotion ? "p)" : ") "));
        if (vp > 0) {
            sb.append(", " + vp + " victory point");
            if (vp > 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }

    public String getExpansion() {
        return expansion;
    }

    public String getDescription() {
        return description;
    }
    
    public String toString() {
        return name; // + " (id=" + id + ")";
    }

    public boolean equals(Object object) {
        return (object != null) && name.equals(((Card) object).getName()); // took away typecheck
    }
    
    public int hashCode() {
        return name.hashCode();
    }

    public boolean costPotion() {
        return costPotion;
    }
    @Override
    public boolean isPrize() {
        return isPrize;
    }
    @Override
    public boolean isShelter() {
    	return isShelter;
    }
    @Override
    public boolean isRuins() {
    	return isRuins;
    }
    @Override
    public boolean isKnight() {
    	return isKnight;
    }
    
    @Override
    public void isBought(MoveContext context) {
    }
    
    @Override
    public void isTrashed(MoveContext context)
    {
    }

	/*@Override
	public void isGained(MoveContext context) {
		
	}*/
}
