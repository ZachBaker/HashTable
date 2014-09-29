// CS 1501 Summer 2013
// This interface provides the functionality required for the symbol table
// ADT.  The methods are almost self-explanatory, with the exception of the
// getProbes() method.  All of your hash table implementations must satisfy this
// interface.

public interface STInterface<Key, Value>
{
	public int size();	// How many items are in the ST?
	public boolean isEmpty();	// Is ST empty?
	public boolean contains(Key key);	// Is the key in the ST?
	public void put(Key key, Value val);	// Put the key, value pair into the ST
	public Value get(Key key);	// Retrieve the value associated with the key, or
								// null if the key is not found
	public void delete(Key key);	// Remove the key from the ST
	public Iterable<Key> keys();	// Return an Iterable collection of the keys
									// from the ST
									
	public int getProbes();		// Return the number of probes required in the last
								// lookup (either contains() or get()).  This is not
								// really a part of the ST interface but is added
								// here in order to evaluate the implementations as
								// required in Assignment 2
}