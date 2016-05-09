/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbc.items;

/**
 *
 * @author homa
 */
public enum SupportedCurrency
{

    GBP, USD, EUR, JPY;

    public static  class UnknownCcyException 
    extends Exception
    {
        public UnknownCcyException(String msg)
        {
            super(msg);
        }
    }
    
    public static String getCCYSymbol(SupportedCurrency ccy) throws UnknownCcyException
    {
        switch (ccy.toString())
        {
            case "GBP" : return "£"; 
            case "USD" : return "$";
            case "EUR" : return "€";
            case "JPY" : return "¥";
        }
        
        throw new UnknownCcyException("Unknown target ccy : " + ccy.toString());
    }
}
