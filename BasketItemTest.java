/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc.items;

import com.rbc.items.BasketItem;
import com.rbc.items.InvalidItemException;
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
public class BasketItemTest
{

   private final BiFunction<SupportedCurrency, SupportedCurrency, Float> rateConverter = (src, target)
        -> 
        {
            Map<String, Float> spotRateLookups= new HashMap<>();
            
            spotRateLookups.put("GBPUSD", 1.5F);
            spotRateLookups.put("USDGBP", 1.0F/1.5F);
            
            spotRateLookups.put("GBPEUR", 1.2F);
            spotRateLookups.put("EURGBP", 1.0F/1.2F);

            spotRateLookups.put("GBPJPY", 100F);
            spotRateLookups.put("JPYGBP", 1.0F/100.0F);

            String index= src.toString() + target.toString();
            
            if ( spotRateLookups.containsKey(index))
            {
                return spotRateLookups.get(index);
            }
           
            return 1.0f;
    };

    public BasketItemTest()
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

    @Test(expected = InvalidItemException.class)
    public void testNoType() throws InvalidItemException
    {
        BasketItem newItem = new BasketItem(null, "an item", SupportedCurrency.GBP, 1.0f);
    }

    @Test(expected = InvalidItemException.class)
    public void testNullDescription() throws InvalidItemException
    {
        BasketItem newItem = new BasketItem(BasketItem.ItemType.Banana, null, SupportedCurrency.GBP, 1.0f);
    }

    @Test(expected = InvalidItemException.class)
    public void testEmptyDescription() throws InvalidItemException
    {
        BasketItem newItem = new BasketItem(BasketItem.ItemType.Banana, "    ", SupportedCurrency.GBP, 1.0f);
    }

    @Test(expected = InvalidItemException.class)
    public void testnegativeunitCost() throws InvalidItemException
    {
        BasketItem newItem = new BasketItem(BasketItem.ItemType.Banana, "a special offer banana", SupportedCurrency.GBP, -1.0f);
    }

    @Test(expected = InvalidItemException.class)
    public void testnullCurrency() throws InvalidItemException
    {
        BasketItem newItem = new BasketItem(BasketItem.ItemType.Banana, "a special offer banana", null, -1.0f);
    }

    /**
     *
     * @throws InvalidItemException
     * @throws CostException
     */
    @Test
    public void testValidItems() throws InvalidItemException, CostException
    {
        BasketItem bananaItem = new BasketItem(BasketItem.ItemType.Banana, "a special offer banana", SupportedCurrency.GBP, 1.5f);

        assert (bananaItem.getCurrency().equals(SupportedCurrency.GBP));
        assert (bananaItem.getItemType().equals(BasketItem.ItemType.Banana));
        assert (bananaItem.getCost(SupportedCurrency.GBP, rateConverter) == 1.5f);

        BasketItem appleItem = new BasketItem(BasketItem.ItemType.Apple, "a special offer apple", SupportedCurrency.GBP, 1.5f);

        assert (appleItem.getCurrency().equals(SupportedCurrency.GBP));
        assert (appleItem.getItemType().equals(BasketItem.ItemType.Apple));
        assert (appleItem.getCost(SupportedCurrency.GBP, rateConverter) == 1.5f);

        BasketItem bananaItem2 = new BasketItem(BasketItem.ItemType.Banana, "a special offer banana", SupportedCurrency.GBP, 2.0f);

        assert (bananaItem2.getCurrency().equals(SupportedCurrency.GBP));
        assert (bananaItem2.getItemType().equals(BasketItem.ItemType.Banana));
        assert (bananaItem2.getCost(SupportedCurrency.USD, rateConverter) == 3f);

        BasketItem appleItem2 = new BasketItem(BasketItem.ItemType.Apple, "a special offer apple", SupportedCurrency.GBP, 1.5f);

        assert (appleItem2.getCurrency().equals(SupportedCurrency.GBP));
        assert (appleItem2.getItemType().equals(BasketItem.ItemType.Apple));
        assert (appleItem2.getCost(SupportedCurrency.USD, rateConverter) ==1.5f * 1.5f);

    }
}
