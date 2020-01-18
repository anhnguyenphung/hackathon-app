/*
 * A very bad algorithm to encrypt an API key that should
 * be replaced in the final product!
 * Here's the original encryption in Python, written by Hung Tran.
 * ======================================================================
LENGTH_PER_CHAR = 8

def encrypt(api_str, shift_num):
    """
        Returns encrypted string
    """
    result_str = ""
    for c in api_str:
        order = ord(c)
        shifted = (order-ord('0')) << shift_num
        print("Order: {}; \tShifted: {}".format(order, shifted))
        s = str(shifted)
        s = ("0"*(LENGTH_PER_CHAR-len(s)))+s
        result_str += s
    return result_str

def decrypt(encrypted, shift_num):
    """
        Returns decrypted string
    """
    return_str = ""
    i = 0
    while i < len(encrypted):
        order = int(encrypted[i:i+LENGTH_PER_CHAR])
        i += LENGTH_PER_CHAR
        shifted = (order>>shift_num) + ord('0')
        print("Order: {}; \tShifted: {}".format(order, shifted))
        return_str += chr(shifted)
    return return_str

print()

def enc():
    print(encrypt(input("Input api_str: "), int(input("shift_num: "))))

def dec():
    print(decrypt(input("Input encrypted: "), int(input("shift_num: "))))
 *=============================================================================
 */
 
package com.example.map;
 
import java.lang.*;

public class KeyEncryption
{
    public static final int LENGTH_PER_CHAR = 8;
    private static final int CHAR_0_OFFSET = '0';
    public static String encrypt(String original, int shiftNum)
    {
        String result = "";
        for(int i = 0; i < original.length(); i++)
        {
            int order = original.charAt(i);
            // Get the ascii offset from '0', then shift
            // that numerical number by shiftNum.
            // This creates an esoteric number that is different from
            // Caesar cipher (shifting cipher)
            int shifted = (order - CHAR_0_OFFSET) << shiftNum;
            String s = String.valueOf(shifted);
            // Make the number exactly 8 digits
            for(int j = s.length(); j < LENGTH_PER_CHAR; j++)
            {
                s = "0" + s;
            }
            result += s;
        }
        return result;
    }
    public static String decrypt(String encrypted, int shiftNum)
    {
        String result = "";
        for(int i = 0; i < encrypted.length(); i+= LENGTH_PER_CHAR)
        {
            // Reverse the encrypt process
            int shiftedOffset = Integer.parseInt(encrypted.substring(i, i + LENGTH_PER_CHAR));
            int order = (shiftedOffset >> shiftNum) + CHAR_0_OFFSET;
            // Transform ascii order into String
            result += Character.toString((char) order);
        }
        return result;
    }
}
 
