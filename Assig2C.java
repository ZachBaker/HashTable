import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;


public class Assig2C {

	public static void main(String[] args) {
		Random R;
	     PrintWriter ofile = null;
	     try {
	           ofile = new PrintWriter(new FileOutputStream(args[0]));
	     }
	     catch (FileNotFoundException e)
	     {
			 System.out.println("Output File DNE");
		 }
		 long seed = Long.parseLong(args[1]);
	     int [] sizes = {1009, 2003, 4001, 8009, 16001};
	     double [] alphas = {0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98};

	     STInterface<Integer,Integer> theST = null;
	     for (int t = 0; t < 3; t++)
	     {	
	     	R = new Random(seed);
	     	for (int i = 0; i < sizes.length; i++)
	     	{
	        	int size = sizes[i];
	        
	        	for (int j = 0; j < alphas.length; j++)
	        	{
	        		// Note the constructors below for the linear probing and double hashing
	        		// tables.  The second parameter provided is a boolean that indicates
	        		// whether or not automatic resizing should be used.  By default it IS
	        		// used, which keeps the tables from getting too full.  However, for this
	        		// exercise we want it to be turned off in order to fill the tables as
	        		// specified.  The false value passed into the constructor should prevent
	        		// resizing from being done.
	        		switch (t)
	        		{
	        			case 0: 
	        				theST = new LinearProbingHashST<Integer,Integer>(size, false); 
	        				break;
	        			case 1:
	        				theST = new SeparateChainingHashST<Integer,Integer>(size);
	        				break;
	        			case 2:
	        				theST = new DoubleHashST<Integer,Integer>(size, false);
	        				break;
	        				
	        		}
	            
	            	double alpha = alphas[j];      // Get next alpha value
	            	int n = (int) (size * alpha);  // How many items to insert
					
	            	int key;
	            	for (int k = 0; k < n; k++)
	            	{
	                	key = R.nextInt(50*size);  // Make range of values
	                                           // 50 times the size of the table
	                	theST.put(key,key);
	                }

	            	int numFound = 0;
	            	int notFound = 0;
	            	
	            	long foundTime = 0;
	            	long notFoundTime = 0;
	            	
	            	final int FindRuns = 200000;  // Do 50000 searches for each setup
	            	for (int ii = 0; ii < FindRuns; ii++)
	            	{
	                	key = R.nextInt(50*size);
	                	
	                	long startTime = System.nanoTime();

	                	boolean found = theST.contains(key);  // Each class must keep
	                		// track of the probes required during a call to the
	                		// contains method.  This value is stored until contains
	                		// (or get()) is called again, and it is retrieved using
	                		// the getProbes() method.

	                	if (found == true)
	                	{
	                   		numFound++;
	                   		foundTime += System.nanoTime() - startTime;
	                	}
	                	else
	                	{
	                   		notFound++;
	                   		notFoundTime += System.nanoTime() - startTime;
	                	}
	                }

	            	// Below I am calculating the averages

	            	double aveTFound, aveTNotFound, aveTTotal, fracFound;

	            	aveTFound = (double) foundTime / numFound;
	            	aveTNotFound = (double) notFoundTime / notFound;
	            	aveTTotal = (double) (foundTime + notFoundTime)/(numFound + notFound);
	            	fracFound = (double) numFound /(numFound + notFound);

					switch (t)
					{
						case 0:
	            			ofile.println("-----------Linear Probing Results--------------");
	            			break;
	            		case 1:
	            			ofile.println("----------Separate Chaining Results------------");
	            			break;
	            		case 2:
	            			ofile.println("-----------Double Hashing Results--------------");
	            			break;
	            	}
	            	ofile.println("Table size: " + size + "    Alpha: " + alpha);
	            	ofile.println("      Found: " + numFound + "  Not Found: " + notFound);
	           		ofile.println("      Fraction of successful finds: " + fracFound);
	            	ofile.println("      Ave time (nanoseconds) for successful find: " + aveTFound);
	            	ofile.println("      Ave time (nanoseconds) for unsuccess. find: " + aveTNotFound);
	            	ofile.println("      Ave time (nanoseconds) for all finds: " + aveTTotal);
	            	ofile.println();
	            } // end alphas loop

	        }   // end sizes loop
	     } // end algorithms loop
	     ofile.close();
	}

}
