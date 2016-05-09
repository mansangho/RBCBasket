/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc.items;

import java.util.HashSet;
import java.util.function.BiFunction;

/**
 *
 * @author homa
 */
public class BasketImpl implements Basket
{

    private final SupportedCurrency ccy;
    private final HashSet<Item> items;

    public BasketImpl(SupportedCurrency ccy)
    {
        this.ccy = ccy;
        this.items = new HashSet<>();
    }

    @Override
    public void add(Item item)
    {
        if (!this.items.contains(item))
        {
            this.items.add(item);
        }
    }

    @Override
    public void remove(Item item)
    {
        if (this.items.contains(item))
        {
            this.items.remove(item);
        }
    }

    @Override
    public float getTotal(BiFunction<SupportedCurrency, SupportedCurrency, Float> ccyConverter) throws CostException
    {
        float total = 0.0f;
        for (Item item : this.items)
        {
            total += item.getCost(this.ccy, ccyConverter);
        }

        return total;
    }

    @Override
    public void removeAll()
    {
        this.items.clear();
    }

}
