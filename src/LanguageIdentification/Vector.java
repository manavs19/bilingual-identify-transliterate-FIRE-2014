package LanguageIdentification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Vector implements Cloneable
{
	HashMap<String, Double> entries = new HashMap<String, Double>();
	
	public Vector()
	{ }
	
	public Vector(File inputFile)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				String[] tokens = line.split("\t");
				String word = tokens[0];
				Double occurences = new Double(tokens[1]);
				entries.put(word, occurences);
			}
			reader.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public void writeToFile(File outputFile)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			for(String key : entries.keySet())
			{
				writer.write(key + "\t" + entries.get(key) + "\n");
			}
			writer.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vector clone()
	{
		Vector v = new Vector();
		v.entries = (HashMap<String, Double>) this.entries.clone();
		return v;
	}
	
	public boolean hasKey(String key)
	{
		return entries.containsKey(key);
	}
	
	public void setValue(String key, double value)
	{
		entries.put(key, value);
	}
	
	public double getValue(String key)
	{
		if(entries.containsKey(key))
			return entries.get(key).doubleValue();
		return 0f;
	}
	
	public Set<String> getKeys()
	{
		return entries.keySet();
	}
	
	public void increment(String key)
	{
		increment(key, 1.0f);
	}
	
	public void increment(String key, double amount)
	{
		if(entries.containsKey(key))
			entries.put(key, entries.get(key) + amount);
		else
			entries.put(key, amount);
	}
	
	public double dotProduct(Vector other)
	{
		double product = 0;
		for(String key : entries.keySet())
			if(other.entries.containsKey(key))
				product += entries.get(key).doubleValue() * other.entries.get(key).doubleValue();
		return product;
	}
	
	public double cosineSimilarity(Vector other)
	{
		if(entries.isEmpty() || other.entries.isEmpty())
			return 0.0f;
		return (double) (dotProduct(other)/(Math.sqrt(dotProduct(this)) * Math.sqrt(other.dotProduct(other))));
	}
	
	public Vector scalarProduct(double coefficient)
	{
		for(String key : entries.keySet())
			entries.put(key, entries.get(key) * coefficient);
		return this;
	}
	
	public static Vector scalarProduct(Vector vector, double coefficient)
	{
		Vector result = new Vector();
		for(String key : vector.entries.keySet())
			result.entries.put(key, vector.entries.get(key).doubleValue() * coefficient);
		return result;
	}
	
	public void add(Vector other)
	{
		for(String key : other.entries.keySet())
		{
			if(entries.containsKey(key))
				entries.put(key, entries.get(key) + other.entries.get(key));
			else
				entries.put(key, other.entries.get(key));
		}
	}
	
	public static Vector add(Vector a, Vector b)
	{
		Vector result = new Vector();
		for(String key : a.entries.keySet())
		{
			if(result.entries.containsKey(key))
				result.entries.put(key, a.entries.get(key) + a.entries.get(key));
			else
				result.entries.put(key, a.entries.get(key));
		}
		for(String key : b.entries.keySet())
		{
			if(result.entries.containsKey(key))
				result.entries.put(key, result.entries.get(key) + b.entries.get(key));
			else
				result.entries.put(key, b.entries.get(key));
		}
		return result;
	}
	
	public Vector normalize()
	{
		double norm = 0f;
		for(String key : entries.keySet())
			norm += entries.get(key)*entries.get(key);
		norm = (double) Math.sqrt(norm);
		return scalarProduct(1.0f/norm);
	}
	
	public Vector normalizeL1()
	{
		double norm = 0f;
		for(String key : entries.keySet())
			norm += entries.get(key);
		return scalarProduct(1.0f/norm);
	}
	
	public static Vector normalized(Vector vector)
	{
		double norm = 0f;
		for(String key : vector.entries.keySet())
			norm += vector.entries.get(key)*vector.entries.get(key);
		norm = (double) Math.sqrt(norm);
		return scalarProduct(vector, 1.0f/norm);
	}
	
	public double l1Norm()
	{
		double norm = 0;
		for(String key : entries.keySet())
			norm += entries.get(key);
		return norm;
	}
	
	@Override
	public String toString()
	{
		String out = "";
		for(String key : entries.keySet())
			out += key + "\t" + entries.get(key) + "\n";
		return out;
	}

	public double l0Norm()
	{
		return entries.keySet().size();
	}
}
