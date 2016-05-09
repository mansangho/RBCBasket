/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc;

import com.rbc.items.Basket;
import com.rbc.items.BasketImpl;
import com.rbc.items.BasketItem;
import com.rbc.items.CostException;
import com.rbc.items.InvalidItemException;
import com.rbc.items.SupportedCurrency;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 *
 * @author homa
 */
public class RBCBasketDemo
{

    private static BiFunction<SupportedCurrency, SupportedCurrency, Float> rateConverter = (src, target)
        -> 
        {
            Map<String, Float> spotRateLookups = new HashMap<>();

            spotRateLookups.put("GBPUSD", 1.5F);
            spotRateLookups.put("USDGBP", 1.0F / 1.5F);

            spotRateLookups.put("GBPEUR", 1.2F);
            spotRateLookups.put("EURGBP", 1.0F / 1.2F);

            spotRateLookups.put("GBPJPY", 100F);
            spotRateLookups.put("JPYGBP", 1.0F / 100.0F);

            float gbpusd = 1.5f;
            float jpygbp = 1.0f / 100.0f;
            float jpyusd = gbpusd * jpygbp;

            spotRateLookups.put("USDJPY", 1.0f / jpyusd);
            spotRateLookups.put("JPYUSD", jpyusd);

            String index = src.toString() + target.toString();

            if (spotRateLookups.containsKey(index))
            {
                return spotRateLookups.get(index);
            }

            return null;
    };

    public static void LoadUpBasket(Basket basket) throws InvalidItemException
    {
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.50f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana 1", SupportedCurrency.GBP, 0.55f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana 2", SupportedCurrency.GBP, 0.54f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana 3", SupportedCurrency.GBP, 0.54f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana 4", SupportedCurrency.GBP, 0.6f));

        basket.add(new BasketItem(BasketItem.ItemType.Apple, "Discount apple", SupportedCurrency.GBP, 0.50f));
        basket.add(new BasketItem(BasketItem.ItemType.Apple, "Discount apple 1", SupportedCurrency.GBP, 0.55f));
        basket.add(new BasketItem(BasketItem.ItemType.Apple, "Discount apple 2", SupportedCurrency.GBP, 0.54f));

        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Discount lemon", SupportedCurrency.GBP, 0.50f));

        basket.add(new BasketItem(BasketItem.ItemType.Orange, "Discount orange", SupportedCurrency.GBP, 0.50f));
        basket.add(new BasketItem(BasketItem.ItemType.Orange, "Discount orange 1", SupportedCurrency.GBP, 0.55f));
        basket.add(new BasketItem(BasketItem.ItemType.Orange, "Discount orange 2", SupportedCurrency.GBP, 0.54f));
        basket.add(new BasketItem(BasketItem.ItemType.Orange, "Discount orange 3", SupportedCurrency.GBP, 0.54f));

        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Discount peache", SupportedCurrency.GBP, 0.50f));
        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Discount peache 1", SupportedCurrency.GBP, 0.55f));
        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Discount peache 2", SupportedCurrency.GBP, 0.54f));
    }

    public static void ShowBasketPricesAndTotal(Basket basket) throws CostException, SupportedCurrency.UnknownCcyException
    {
        float totalCost = basket.getTotal(RBCBasketDemo.rateConverter);
        
        System.out.printf("\n The basket total cost is %s%.2f\n", SupportedCurrency.getCCYSymbol(SupportedCurrency.GBP), totalCost);
    }

    public static void main(String[] args) throws InvalidItemException, CostException, SupportedCurrency.UnknownCcyException
    {
        Basket basket = new BasketImpl(SupportedCurrency.GBP);

        LoadUpBasket(basket);
        ShowBasketPricesAndTotal(basket);
    }
}
