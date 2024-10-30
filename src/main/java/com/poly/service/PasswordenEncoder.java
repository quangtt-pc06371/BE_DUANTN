package com.poly.service;

public interface PasswordenEncoder {
String encode(CharSequence rawPassword);

boolean matches(CharSequence rawPassword , String encodePassword);

default boolean upgradeEncodeing(String encodePassword ) {return false;}

}
