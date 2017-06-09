package com.wechat.util;

public interface Coder {

	public String encode(byte[] data);

	public byte[] decode(String string);
	
}
