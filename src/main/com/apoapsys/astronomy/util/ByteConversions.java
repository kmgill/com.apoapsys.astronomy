/*
 * Copyright (C) 2011 Kevin M. Gill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apoapsys.astronomy.util;


/** Provides a set of common byte-wise manipulation and datatype conversion operations.
 * 
 * @author Kevin M. Gill
 *
 */
public class ByteConversions
{
	public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LSBFIRST;
	
	protected ByteConversions()
	{
		
	}
	
	/** Converts a 4 byte array to a float using the default byte order.
	 * 
	 * @param bytes A 4 byte array
	 * @return
	 */
	public static float bytesToFloat(byte[] bytes)
	{
		return bytesToFloat(bytes,  DEFAULT_BYTE_ORDER);
	}
	
	/** Converts a 4 byte array to a float using the specified byte order.
	 * 
	 * @param bytes A 4 byte array
	 * @param byteOrder
	 * @return
	 */
	public static float bytesToFloat(byte[] bytes, ByteOrder byteOrder)
	{
		return bytesToFloat(bytes[0], bytes[1], bytes[2], bytes[3], byteOrder);
	}
	
	/** Converts 4 bytes to a float using the specified byte order.
	 * 
	 * @param b00
	 * @param b01
	 * @param b10
	 * @param b11
	 * @param byteOrder
	 * @return
	 */
	public static float bytesToFloat(byte b00, byte b01, byte b10, byte b11, ByteOrder byteOrder)
	{
		int intBits = 0;
		if (byteOrder == ByteOrder.MSBFIRST) {
			intBits = ((b00 & 0xFF) << 24) |
				((b01 & 0xFF) << 16) |
				((b10 & 0xFF) << 8) |
				(b11 & 0xFF);
		} else if (byteOrder == ByteOrder.LSBFIRST || byteOrder == ByteOrder.INTEL_BYTE_ORDER) {
			intBits = ((b11 & 0xFF) << 24) |
				((b10 & 0xFF) << 16) |
				((b01 & 0xFF) << 8) |
				(b00 & 0xFF);
		} else if (byteOrder == ByteOrder.INTEL_OR_MOTOROLA) {
			intBits = ((b10 & 0xFF) << 24) |
				((b11 & 0xFF) << 16) |
				((b00 & 0xFF) << 8) |
				(b01 & 0xFF);
		}
		
		return Float.intBitsToFloat(intBits);

	}
	
	/** Translates the byte to a two-character hexidecimal string representation.
	 * 
	 * @param b00
	 * @return
	 */
	public static String toHex(byte b00)
	{
		String s = Integer.toString(b00 & 0xFF, 16).toUpperCase();
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
	
	/** Converts a float to a 4 byte array using the default byte order.
	 * 
	 * @param value
	 * @return A 4 byte array
	 */
	public static byte[] floatToBytes(float value)
	{
		return floatToBytes(value, DEFAULT_BYTE_ORDER);
	}
	
	/** Converts a float to a 4 byte array using the specified byte order.
	 * 
	 * @param value
	 * @param byteOrder
	 * @return A 4 byte array/
	 */
	public static byte[] floatToBytes(float value, ByteOrder byteOrder)
	{
		byte[] buffer = new byte[4];
		floatToBytes(value, buffer, byteOrder);
		return buffer;
	}
	
	public static void floatToBytes(float value, byte[] buffer)
	{
		floatToBytes(value, buffer, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	public static void floatToBytes(float value, byte[] buffer, ByteOrder byteOrder)
	{
		int bits = Float.floatToIntBits(value);


		if (byteOrder == ByteOrder.MSBFIRST) {
			buffer[0] = (byte) (bits >>> 24);
			buffer[1] = (byte) (bits >>> 16);
			buffer[2] = (byte) (bits >>> 8);
			buffer[3] = (byte) (bits);

		} else {
			buffer[0] = (byte) (bits);
			buffer[1] = (byte) (bits >>> 8);
			buffer[2] = (byte) (bits >>> 16);
			buffer[3] = (byte) (bits >>> 24);
			
		}

	}
	
	/** Converts a float to a 4 byte array using the specified byte order.
	 * 
	 * @param value
	 * @param byteOrder
	 * @return A 4 byte array/
	 */
	public static byte[] doubleToBytes(double value)
	{
		return doubleToBytes(value, DEFAULT_BYTE_ORDER);
	}
	
	/** Converts a float to a 4 byte array using the specified byte order.
	 * 
	 * @param value
	 * @param byteOrder
	 * @return A 4 byte array/
	 */
	public static byte[] doubleToBytes(double value, ByteOrder byteOrder)
	{
		byte[] buffer = new byte[8];
		doubleToBytes(value, buffer, byteOrder);
		return buffer;
	}
	
	public static void doubleToBytes(double value, byte[] buffer)
	{
		doubleToBytes(value, buffer, DEFAULT_BYTE_ORDER);
	}
	
	public static void doubleToBytes(double value, byte[] buffer, ByteOrder byteOrder)
	{
		long bits = Double.doubleToLongBits(value);
		longToBytes(bits, buffer, byteOrder);
	}
	
	public static byte[] intToBytes(int value)
	{
		return intToBytes(value, DEFAULT_BYTE_ORDER);
	}

	public static byte[] intToBytes(int bits, ByteOrder byteOrder)
	{
		byte[] buffer = new byte[4];
		intToBytes(bits, buffer, byteOrder);
		return buffer;
	}
	
	public static void intToBytes(int bits, byte[] buffer)
	{
		intToBytes(bits, buffer, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	public static void intToBytes(int bits, byte[] buffer, ByteOrder byteOrder)
	{

		if (byteOrder == ByteOrder.LSBFIRST) {
			buffer[0] = (byte) (0xFF & (bits >>> 24));
			buffer[1] = (byte) (0xFF & (bits >>> 16));
			buffer[2] = (byte) (0xFF & (bits >>> 8));
			buffer[3] = (byte) (0xFF & (bits));
		} else {
			buffer[0] = (byte) (0xFF & (bits));
			buffer[1] = (byte) (0xFF & (bits >>> 8));
			buffer[2] = (byte) (0xFF & (bits >>> 16));
			buffer[3] = (byte) (0xFF & (bits >>> 24));
		}

	}
	
	/** Converts a 4 byte array to an integer using the default byte order.
	 * 
	 * @param bytes A 4 byte array.
	 * @return
	 */
	public static int bytesToInt(byte[] bytes)
	{
		return bytesToInt(bytes, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	/** Converts a 4 byte array to an integer using the specified byte order.
	 * 
	 * @param bytes A 4 byte array.
	 * @param byteOrder
	 * @return
	 */
	public static int bytesToInt(byte[] bytes, ByteOrder byteOrder)
	{
		return bytesToInt(bytes[0], bytes[1], bytes[2], bytes[3], byteOrder);
	}
	
	/** Converts 4 bytes to an integer using the default byte order.
	 * 
	 * @param b00
	 * @param b01
	 * @param b10
	 * @param b11
	 * @return
	 */
	public static int bytesToInt(byte b00, byte b01, byte b10, byte b11)
	{
		return bytesToInt(b00, b01, b10, b11, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	/** Translates a single byte (considered unsigned) into an integer.
	 * 
	 * @param b00
	 * @return
	 */
	public static int byteToInt(byte b00)
	{
		int intBits = 0;
		intBits = (b00 & 0xFF);
		return intBits;
	}
	
	/** Converts the bytes to an integer using the specified byte order.
	 * 
	 * @param b00
	 * @param b01
	 * @param b10
	 * @param b11
	 * @param byteOrder
	 * @return
	 */
	public static int bytesToInt(byte b00, byte b01, byte b10, byte b11, ByteOrder byteOrder)
	{
		int intBits = 0;
		if (byteOrder == ByteOrder.MSBFIRST) {
			intBits = ((b11 & 0xFF) << 24) |
				((b10 & 0xFF) << 16) |
				((b01 & 0xFF) << 8) |
				(b00 & 0xFF);
		} else if (byteOrder == ByteOrder.LSBFIRST || byteOrder == ByteOrder.INTEL_BYTE_ORDER) {
			intBits = ((b00 & 0xFF) << 24) |
				((b01 & 0xFF) << 16) |
				((b10 & 0xFF) << 8) |
				(b11 & 0xFF);
		}
		return intBits;
	}
	
	
	
	/** Converts a 2 byte array to an integer using the default byte order.
	 * 
	 * @param bytes A 2 byte array.
	 * @return
	 */
	public static int bytesToShort(byte[] bytes)
	{
		return bytesToShort(bytes, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	/** Converts a 2 byte array to an integer using the specified byte order.
	 * 
	 * @param bytes A 2 byte array.
	 * @param byteOrder
	 * @return
	 */
	public static int bytesToShort(byte[] bytes, ByteOrder byteOrder)
	{
		return bytesToShort(bytes[0], bytes[1], byteOrder);
	}
	
	
	/** Converts 2 bytes to an short integer using the default byte order.
	 * 
	 * @param b00
	 * @param b01
	 * @return
	 */
	public static int bytesToShort(byte b00, byte b01)
	{
		return bytesToShort(b00, b01, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	
	/** Converts the bytes to an short integer using the specified byte order.
	 * 
	 * @param b00
	 * @param b01
	 * @param byteOrder
	 * @return
	 */
	public static int bytesToShort(byte b00, byte b01, ByteOrder byteOrder)
	{
		int intBits = 0;
		if (byteOrder == ByteOrder.MSBFIRST || byteOrder == ByteOrder.INTEL_OR_MOTOROLA) {
			intBits = ((b01 & 0xFF) << 8) |
				(b00 & 0xFF);
		} else if (byteOrder == ByteOrder.LSBFIRST || byteOrder == ByteOrder.INTEL_BYTE_ORDER) {
			intBits = ((b00 & 0xFF) << 24) |
				((b01 & 0xFF) << 16);
		}
		return intBits;
	}
	
	
	
	/** Converts an 8 byte array to a double using the default byte order.
	 * 
	 * @param bytes
	 * @return
	 */
	public static double bytesToDouble(byte[] bytes)
	{
		return bytesToDouble(bytes, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	/** Converts an 8 byte array to a double using the specified byte order.
	 * 
	 * @param bytes
	 * @param byteOrder
	 * @return
	 */
	public static double bytesToDouble(byte[] bytes, ByteOrder byteOrder)
	{
		return Double.longBitsToDouble(bytesToLong(bytes, byteOrder));
	}
	
	public static double bytesToDouble(byte b000, byte b001, byte b010, byte b011, byte b100, byte b101, byte b110, byte b111, ByteOrder byteOrder)
	{
		return Double.longBitsToDouble(bytesToLong(b000, b001, b010, b011, b100, b101, b110, b111, byteOrder));
	}
	
	/** Converts an 8 byte array to a long using the default byte order.
	 * 
	 * @param bytes
	 * @return
	 */
	public static long bytesToLong(byte[] bytes)
	{
		return bytesToLong(bytes, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	/** Converts an 8 byte array to a long using the specified byte order.
	 * 
	 * @param bytes
	 * @param byteOrder
	 * @return
	 */
	public static long bytesToLong(byte[] bytes, ByteOrder byteOrder)
	{
		long longBits = 0;
		
		if (byteOrder == ByteOrder.MSBFIRST) {
			for(int i =0; i < 8; i++){      
				longBits <<= 8;  
				longBits ^= (long)bytes[i] & 0xFF;      
			}  
		} else if (byteOrder == ByteOrder.LSBFIRST || byteOrder == ByteOrder.INTEL_BYTE_ORDER) {
			for(int i = 7; i >= 0; i--){      
				longBits <<= 8;  
				longBits ^= (long)bytes[i] & 0xFF;      
			}  
		}
		return longBits;

	}	
	
	
	public static long bytesToLong(byte b000, byte b001, byte b010, byte b011, byte b100, byte b101, byte b110, byte b111, ByteOrder byteOrder)
	{
		byte[] buffer = {b000, b001, b010, b011,
						 b100, b101, b110, b111
		};
		return bytesToLong(buffer, byteOrder);
	}
	
	
	
	
	
	public static byte[] longToBytes(int value)
	{
		return longToBytes(value, DEFAULT_BYTE_ORDER);
	}

	public static byte[] longToBytes(long bits, ByteOrder byteOrder)
	{
		byte[] buffer = new byte[8];
		longToBytes(bits, buffer, byteOrder);
		return buffer;
	}
	
	public static void longToBytes(long bits, byte[] buffer)
	{
		longToBytes(bits, buffer, ByteConversions.DEFAULT_BYTE_ORDER);
	}
	
	public static void longToBytes(long bits, byte[] buffer, ByteOrder byteOrder)
	{

		if (byteOrder == ByteOrder.MSBFIRST) {
			buffer[0] = (byte)(bits >>> 56);
			buffer[1] = (byte)(bits >>> 48);
			buffer[2] = (byte)(bits >>> 40);
			buffer[3] = (byte)(bits >>> 32);
			buffer[4] = (byte)(bits >>> 24);
			buffer[5] = (byte)(bits >>> 16);
			buffer[6] = (byte)(bits >>> 8);
			buffer[7] = (byte)(bits);
		} else {
			buffer[0] = (byte)(bits);
			buffer[1] = (byte)(bits >>> 8);
			buffer[2] = (byte)(bits >>> 16);
			buffer[3] = (byte)(bits >>> 24);
			buffer[4] = (byte)(bits >>> 32);
			buffer[5] = (byte)(bits >>> 40);
			buffer[6] = (byte)(bits >>> 48);
			buffer[7] = (byte)(bits >>> 56);
		}
	}
	
	
	
	
	
	
}
