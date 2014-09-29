
public class DoubleHashST<Key, Value> implements STInterface<Key,Value> {
	
	private int M,N,D,probes,deleted;
	
	private boolean autoresize;
	
	private Key[] keys;      // the keys
    private Value[] vals;    // the values
    
    //create an enum and corresponding array. Keeps the state of each location in the array for searching and placing in table
    private enum State {EMPTY,FULL,DELETED};
    private State [] states;
	
	public DoubleHashST(int capacity){
		N = 0;
		autoresize = true;
		deleted = 0;
		
		//if original M is not prime, increment upwards until it is
		for(int i = capacity; isPrime(capacity) == false; i++)
			capacity = i;
		
		M = capacity;
		
		capacity = capacity -1;
		//find the greatest number smaller than M that is prime
		for(int i = capacity-1; isPrime(capacity) == false; i--)
			capacity = i;
		
		D = capacity;
		
		keys = (Key[]) new Object[M];
		vals = (Value[]) new Object[M];
		states = new State[M];
		
		for(int i =0; i<M; i++){
			states[i] = State.EMPTY;
		}
	}
	
	public DoubleHashST(int capacity, boolean resize){
		N = 0;
		deleted = 0;
		
		//if original M is not prime, increment upwards until it is
		for(int i = capacity; isPrime(capacity) == false; i++)
			capacity = i;
		
		M = capacity;
		
		capacity = capacity -1;
		//find greatest prime number less than M, set it as D
		for(int i = capacity - 1; isPrime(capacity) == false; i--)
			capacity = i;
		
		D = capacity;
		
		//can manually set the auto-resize feature to on or off
		autoresize = resize;
		
		keys = (Key[]) new Object[M];
		vals = (Value[]) new Object[M];
		states = new State[M];

		for(int i =0; i<M; i++){
			states[i] = State.EMPTY;
		}
	}
	
	//Tests whether int is a prime number
	private boolean isPrime(int tester){
		for(int i = 2; i<tester; i++){
			if(tester % i == 0) return false;
		}
		return true;
	}
	
	//returns the number of items being held in the table
	public int size(){
		return N;
	}

	//returns true is table is empty, otherwise returns fals
	public boolean isEmpty(){
		if(N == 0)
			return true;
		else
			return false;
	}

	public boolean contains(Key key){
		return get(key) != null;
	}

	//places key and value into appropriate position in table
	public void put(Key key, Value val){
		
		if(N > 3*M/4 && autoresize)
			resize(2*M);
		
		int i = hash(key);

		while(states[i].equals(State.FULL) && !(key.equals(keys[i]))){
				
			if(i + hash2(key) >= M){
				i = i + hash2(key) - M;
			}
			
			else
				i = i + hash2(key);
		}
		
		keys[i] = key;
		vals[i] = val;
		states[i] = State.FULL;
		N++;
	}
	
	//resizes the table to the prime number greater than or equal to the new size indicated
	private void resize(int newSize){
		System.out.println("Resizing from M = " + M + " and N = " + N);
		
		for(int i = newSize; isPrime(newSize) == false; i++)
			newSize = i; 
			
		DoubleHashST<Key, Value> newST = new DoubleHashST<Key,Value>(newSize);
		for(int i = 0; i<M; i++){
			if(keys[i] !=null){
				newST.put(keys[i], vals[i]);
			}
		}
		keys = newST.keys;
		vals = newST.vals;
		states = newST.states;
		deleted = 0;
		M = newST.M;
		D = newST.D;

		System.out.println("	New Values are M = " + M + " and D = " + D);
	}
	
	//returns simple hashcode of key
	private int hash(Key key){
		return (key.hashCode() & 0x7fffffff) % M;
	}
	
	//calculates and returns a h2(x), which is to be used as an increment
	private int hash2(Key key){
		return((D - (key.hashCode() & 0x7fffffff % D)));
	}

	//attempts to find key in table and returns it; if not found returns null
	public Value get(Key key){
		probes = 0;
		int i = hash(key);
		
		//for the double hash implementation, searching does not depend on a null status of a key, but what state the position 
		//of the table is in
		while(states[i].equals(State.DELETED) || states[i].equals(State.FULL)){
			
			if(key.equals(keys[i]))
				return vals[i];
			
			else{
				i = i + hash2(key);
				
				if(i >= M)
					i = i - M;
				
				probes++;
			}
		}
		return null;
	}

	//if a key is in the table, deletes the key and value pair and sets that locations state to DELETED
	public void delete(Key key)
	{
		if(!contains(key)) return;
		
		int i = hash(key);
		
		if(key.equals(keys[i])){
		vals[i] = null;
		keys[i] = null;
		states[i] = State.DELETED;
		N--;
		deleted++;
		
		//if the deleted locations exceed 1/4 table size, create a new table by simply resizing
		if(deleted > M/4)
			resize(M);
		
		if(N > 0 && N <= M/8 && autoresize)
			resize(M/2); return;
		}
		
		else{	
			i = i + hash2(key);
			
			if(i > M)
				i = i - M;
			
			while(!(key.equals(keys[i]))){
				i = i +hash2(key);
				
				if(i > M)
					i = i - M;
			}
		}
		vals[i] = null;
		keys[i] = null;
		states[i] = State.DELETED;
		N--;
		deleted++;
		
		//if the deleted locations exceed 1/4 table size, create a new table by simply resizing
		if(deleted > M/4)
			resize(M);
		
		if(N > 0 && N <= M/8 && autoresize)
			resize(M/2);
				
	}

	public Iterable<Key> keys()
	{
		Queue<Key> queue = new Queue<Key>();
		
		for(int i = 0; i<M; i++)
			if (keys[i] != null) queue.enqueue(keys[i]);
		
		return queue;
	}

	//returns the probes required for the latest get() method called for a given key.
	//this includes deleted key since contains() is called to delete a key
	public int getProbes(){
		return probes;
	}
}
