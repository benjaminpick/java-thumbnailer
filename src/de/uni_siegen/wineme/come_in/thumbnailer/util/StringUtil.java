/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */

package de.uni_siegen.wineme.come_in.thumbnailer.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class StringUtil {
	static Random rand = new Random();
	static String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

	/**
	 * Create a random ASCII-String consisting of a certain number of chars.
	 * 
	 * @param numChars	How many chars should the string have
	 * @return	Random String
	 */
	public static String randomString(int numChars)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numChars; i++) {
			sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}
		return sb.toString();
	}
	
	/**
	 * Transpose a string from Hex into 36-Base string.
	 * @param str	String of a Base-16-Number (e.g., MD5-Hash)
	 * @return	Equivalent of it as 36-Base-Number (less characters)
	 */
	public static String transposeString(String str)
	{
		BigInteger bi = new BigInteger(str, 16);
		return bi.toString(Character.MAX_RADIX);
	}
	
	/**
	 * Create an MD5 Hash of an input String.
	 * Uses the MD5 Algorithm of MessageDigest.
	 * @param input	String to Hash
	 * @return	Hash (Hex-Encoded)
	 */
	public static String md5(String input){
        StringBuilder res = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] md5 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < md5.length; i++) {
                tmp = (Integer.toHexString(0xFF & md5[i]));
                if (tmp.length() == 1) {
                    res.append("0").append(tmp);
                } else {
                    res.append(tmp);
                }
            }
        } catch (NoSuchAlgorithmException ex) { return ""; }
        return res.toString();
    }
}
