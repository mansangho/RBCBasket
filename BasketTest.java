/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc.items;

import com.rbc.items.BasketItem;
import com.rbc.items.BasketImpl;
import com.rbc.items.InvalidItemException;
import com.rbc.items.Basket;
import com.rbc.items.CostException;
import com.rbc.items.SupportedCurrency;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author homa
 */
public class BasketTest
{

    private static boolean IsEquals(float src, float target)
    {
        float error = src - target;
        if (error <= -0.0)
        {
            error = error * -1.0f;
        }

        float epsilon = 0.000001f;

        if (error < epsilon)
        {
            return true;
        }

        return false;
    }
    
    private BiFunction<SupportedCurrency, SupportedCurrency, Float> rateConverter = (src, target)
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

    public BasketTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void test1ItemStirlingBananaStirlingBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.GBP);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.50f));

        float cost = basket.getTotal(rateConverter);

        assert (cost == 0.5f);
    }

    @Test
    public void test2StirlingBananaStirlingBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.GBP);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.50f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Premium banana", SupportedCurrency.GBP, 1.0f));

        float cost = basket.getTotal(rateConverter);

        assert (cost == (0.5f + 1.0f));
    }

    @Test
    public void test1ItemStirlingUSDBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.USD);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.50f));

        float cost = basket.getTotal(rateConverter);

        Float rate = rateConverter.apply(SupportedCurrency.GBP, SupportedCurrency.USD) ;
        assert (rate != null);
        
        assert (IsEquals(cost, rate * 0.5f));
    }

    @Test
    public void test2ItemsStirlingBananaUSDBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.USD);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.50f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Premium banana", SupportedCurrency.GBP, 1.0f));

        float cost = basket.getTotal(rateConverter);

        Float rate = rateConverter.apply(SupportedCurrency.GBP, SupportedCurrency.USD) ;
        assert (rate != null);
        
        assert (IsEquals(cost, rate * 0.5f
            + rate * 1.0f));
    }

    @Test
    public void test2ItemsJPYGBPBananaUSDBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.USD);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.JPY, 75.0f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Premium banana", SupportedCurrency.GBP, 1.0f));

        float cost = basket.getTotal(rateConverter);
       
        Float jpyGbpRate = rateConverter.apply(SupportedCurrency.JPY, SupportedCurrency.GBP);
        assert (jpyGbpRate != null);

        Float gbpUsdRate = rateConverter.apply(SupportedCurrency.GBP, SupportedCurrency.USD);
        assert (gbpUsdRate != null);

        assert (IsEquals(cost, ( jpyGbpRate * 75.0f + 1.0f)
            * gbpUsdRate));

    }

    @Test
    public void testGeneralGBPBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.GBP);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.5f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Premium banana", SupportedCurrency.GBP, 1.0f));
        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Discount peache", SupportedCurrency.GBP, 1.3f));
        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Premium peache", SupportedCurrency.GBP, 1.9f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Discount lemon", SupportedCurrency.GBP, 0.75f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Premium lemon", SupportedCurrency.GBP, 0.95f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Premium lemon", SupportedCurrency.GBP, 0.95f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Deluxe lemon", SupportedCurrency.GBP, 1.9f));

        float cost = basket.getTotal(rateConverter);

        assert (IsEquals(cost, 0.5f + 1.0f + 1.3f + 1.9f + 0.75f + 0.95f + 0.95f + 1.9f));

    }
    
    @Test (expected = CostException.class)
    public void testGeneralEURBasket() throws InvalidItemException, CostException
    {
        Basket basket = new BasketImpl(SupportedCurrency.EUR);

        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Discount banana", SupportedCurrency.GBP, 0.5f));
        basket.add(new BasketItem(BasketItem.ItemType.Banana, "Premium banana", SupportedCurrency.GBP, 1.0f));
        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Discount peache", SupportedCurrency.GBP, 1.3f));
        basket.add(new BasketItem(BasketItem.ItemType.Peache, "Premium peache", SupportedCurrency.GBP, 1.9f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Discount lemon", SupportedCurrency.GBP, 0.75f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Premium lemon", SupportedCurrency.GBP, 0.95f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Premium lemon", SupportedCurrency.GBP, 0.95f));
        basket.add(new BasketItem(BasketItem.ItemType.Lemon, "Deluxe lemon", SupportedCurrency.JPY, 190f));

        float cost = basket.getTotal(rateConverter);

        assert (IsEquals(cost, 0.5f + 1.0f + 1.3f + 1.9f + 0.75f + 0.95f + 0.95f + 1.9f));

    }

}
