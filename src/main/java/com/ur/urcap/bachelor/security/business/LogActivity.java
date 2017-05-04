/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.business;

/**
 *
 * @author frede
 */
public class LogActivity
{

    public LogActivity(String time)
    {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return time + ": " + message;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final LogActivity other = (LogActivity) obj;

        return this.message.equals(other.message);

    }
    public String message;
    public String time;
}
