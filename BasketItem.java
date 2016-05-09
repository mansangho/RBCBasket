/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc.items;

import java.util.function.BiFunction;

/**
 *
 * @author homa
 * 
 * The item implementation class for the Item interface.
 *
 */
public class BasketItem implements Item
{
    private final String description;
    private final float unitCost;
    private final SupportedCurrency ccy;
    private final ItemType itemType;

    /**
     * The constructor for a new BasketItem
     * 
     * @param itemType : The type of item 
     * @param desc  : A free text field describing this particular item
     * @param ccy   : The ccy in which the cost of this item is described in
     * @param unitCost : The numerical value// cost of this item
     * @throws InvalidItemException  : The exception thrown if the constructor does not like the parameters passed
     */
    public BasketItem(ItemType itemType, String desc, SupportedCurrency ccy, float unitCost) throws InvalidItemException
    {
        boolean hasError = false;
        StringBuilder errorBuilder = new StringBuilder();

        if (unitCost < 0.0f)
        {
            hasError = true;
            errorBuilder.append("unitCost can not be less than 0.0. Invalid unitCost = ").append(unitCost).append(". ");
        }
        if (desc == null || desc.trim().length() == 0)
        {
            hasError = true;
            errorBuilder.append("desc can not be null or empty (spaces). Invalid description = ").append(desc).append(". ");
        }
        if (itemType == null)
        {
            hasError = true;
            errorBuilder.append("itemType can not be null. ");
        }

        if (ccy == null)
        {
            hasError = true;
            errorBuilder.append("ccy can not be null. ");
        }

        if (hasError)
        {
            throw new InvalidItemException(errorBuilder.toString());
        }

        this.description = desc;
        this.unitCost = unitCost;
        this.ccy = ccy;
        this.itemType = itemType;
    }

    @Override
    /**
     * Get the free text description of this item
     */
    public String getDescription()
    {
        return this.description;
    }

    @Override
    /**
     * Get the currency code of this item 
     */
    public SupportedCurrency getCurrency()
    {
        return this.ccy;
    }

    @Override
    /**
     * This method calculates the cost of the item in the target ccy, it will use the supplied ccyConverter to do conversion if it detectes that the item and the target ccy is not the same. 
     * 
     * @param targetCurrency : Calculate the total cost of this basked in this currency
     * @param ccyConverter : The call back function of the ccy rate converter
     * @return : Return the cost of this item in the target currency, 
     * @throws CostException  : The exception that is thrown if it can not find the spot rate pair for ccy conversion
     */
    public float getCost(SupportedCurrency targetCurrency, BiFunction<SupportedCurrency, SupportedCurrency, Float> ccyConverter) throws CostException
    {
        if (targetCurrency == null)
        {
            throw new CostException("targetCurrency can not be null");
        }

        if (this.ccy.equals(targetCurrency))
        {
            return this.unitCost;
        }
        else
        {
            Float exchangeRate = ccyConverter.apply(this.ccy, targetCurrency);
            if (exchangeRate == null)
            {
                throw new CostException("Exchange Rate for " + this.ccy + targetCurrency + " is not found.");
            }
            
            return this.unitCost * exchangeRate;
        }
    }

    /**
     * Get the item type
     * @return   ItemType
     */
    @Override
    public BasketItem.ItemType getItemType()
    {
        return this.itemType;
    }

    @Override
    public int hashCode()
    {
        int currentHash = 17;

        currentHash = 31 * currentHash ^ super.hashCode();
        currentHash = 31 * currentHash ^ this.description.hashCode();
        currentHash = 31 * currentHash ^ Float.floatToIntBits(this.unitCost);
        currentHash = 31 * currentHash ^ this.itemType.hashCode();
        currentHash = 31 * currentHash ^ this.ccy.hashCode();

        return currentHash;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }

        if (other instanceof BasketItem)
        {
            if (other == this)
            {
                return true;
            }
            BasketItem otherItem = (BasketItem) other;
            return
                super.equals(otherItem)
                && this.ccy.equals(otherItem.ccy)
                && this.description.equals(otherItem.description)
                && this.itemType == otherItem.itemType
                && this.unitCost == otherItem.unitCost;
        }
        else
        {
            return false;
        }
    }
}
