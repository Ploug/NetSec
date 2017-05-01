/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.security.exceptions;

/**
 *
 * @author frede
 */
public class UninstalledProgramException extends Exception {
  public UninstalledProgramException() { super(); }
  public UninstalledProgramException(String message) { super(message); }
  public UninstalledProgramException(String message, Throwable cause) { super(message, cause); }
  public UninstalledProgramException(Throwable cause) { super(cause); }
}
