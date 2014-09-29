// CS1501 Summer 2013
// Programming Project 2 Double Hashing Test program

import java.io.*;
import java.util.*;

public class Assig2B
{
	public static void main(String [] args)
	{
		int n = Integer.parseInt(args[0]);
		long seed = Long.parseLong(args[1]);
		Random R = new Random(seed);
		Integer [] data = new Integer[n];
		for (int i = 0; i < data.length; i++)
		{
			data[i] = new Integer(R.nextInt());
		}
	 	   
		STInterface<Integer,Integer> DHT;
		DHT = new DoubleHashST<Integer,Integer>(11);    
		Integer key;
		for (int k = 0; k < data.length; k++)
		{
			key = data[k];  // Make range of values
                                           // 4 times the size of the table
			DHT.put(key,key);  // insert key into each table
		}

		findData(data, DHT);
		for (int i = 0; i < data.length/2; i++)
		{
			key = data[i];
			DHT.delete(key);
		}
		findData(data, DHT);
		R = new Random(3*seed);
		for (int i = 0; i < n/3; i++)
		{
			key = new Integer(R.nextInt());
			DHT.put(key,key);
		}
		
		for (int i = data.length/2; i < data.length; i++)
		{
			key = data[i];
			DHT.delete(key);
		}
		
		R = new Random(3*seed);
		for (int i = 0; i < n/4; i++)
		{
			key = new Integer(R.nextInt());
			DHT.delete(key);
		}
		System.out.println("DHT Contains: ");
		for (Integer theKey: DHT.keys())
			System.out.println("\t" + theKey);
   }
   
	public static void findData(Integer [] data, STInterface<Integer,Integer> DHT)
	{
		System.out.println("Searching for keys:");
		for (int k = 0; k < data.length; k++)
		{
			Integer key = data[k];
			if (DHT.contains(key))
				System.out.println("\t" + key + " was found");
			else
				System.out.println("\t" + key + " was not found");
		}
		System.out.println();
	}

}


