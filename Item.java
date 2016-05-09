/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc.items;

import java.util.function.BiFunction;

/**
 *
 * @author homa The Item interface contract defines what each item in the basket
 * behaviour defines :- The getDescription method returns a String
 * getDescription of the item, this is free text, it is used for the hashCode
 * and equals override. The getCurrency method returns the currency that item is
 * costed in. The getCost method returns the cost of that item in the
 * targetCurrencty, it also can use the ccy converter if it finds that the item
 * ccy is not same as the target ccy The getItemType returns the type of item
 * that item contains
 */
public interface Item
{

    static enum ItemType
    {
        Banana, Orange, Apple, Lemon, Peache;
    }

    String getDescription();

    SupportedCurrency getCurrency();

    float getCost(SupportedCurrency targetCurrency, BiFunction<SupportedCurrency, SupportedCurrency, Float> ccyConverter) throws CostException;

    BasketItem.ItemType getItemType();
}
