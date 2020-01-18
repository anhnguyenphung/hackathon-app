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
 
 
