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
public class InvalidItemException extends Exception
{

    InvalidItemException(String msg)
    {
        super(msg);
    }

}
