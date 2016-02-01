package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.List;

public class HyperwalletList<T> {

	public int count;
	public int offset;
	public int limit;

	public List<T> data = new ArrayList<T>();

}
