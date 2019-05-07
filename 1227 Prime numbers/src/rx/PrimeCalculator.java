package rx;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PrimeCalculator {
	public static List<Integer> getPrimes(int amount) {
		List<Integer> primes = new LinkedList<Integer>();
		int lastPrime = 0;
		while (primes.size() < amount) {
			lastPrime = nextPrime(lastPrime);
			primes.add(lastPrime);
		}
		return primes;
	}
	public static int nextPrime(Integer base) {
		int prime = 2;
		int current = base;
		List<Integer> primes = new LinkedList<Integer>();
		primes.add(2);
		Outer:
		for(int n = 3; prime <= current ; n++) {
			Iterator<Integer> it = primes.iterator();
			while (it.hasNext()) {
				if(n % it.next() == 0)
					continue Outer;
			}
			//number is not divided by any prime, so it is prime
			primes.add(n);
			prime = n;
		}
		//System.out.println(primes.size() / (double)current);
		return prime;
	}
	
	public static int nextPrime(int base) { //takes 25% less time than (Integer) one (no autoboxing)
		if (base < 1)
			base = 1;
		int current = base;
		int prime = 2;
		int[] primes = new int[base]; //amount of prime numbers is always lower than amount of all numbers
		int ind = 0;
		int primesCount = 1;
		primes[ind] = 2;
		Outer:
		for(int n = 3; prime <= current ; n++) {
			int i = 0;
			while (i < primesCount) {
				if(n % primes[i] == 0)
					continue Outer;
				else
					i++;
			}
			//number is not divided by any prime, so it is a prime number
			primes[++ind] = n;
			primesCount++;
			prime = n;
		}
//		System.out.print("[");
//		for(int i = 0; i < primesCount; i++)
//			System.out.print(primes[i]+" ");
//		System.out.println("]");
		return prime;
	}
	
	public static void timeTestReflect(Object invTarget, Method m, Object[] args, int iterations) {
		if (m == null)
			return;
		long t1 = System.nanoTime();
		System.out.println("testing: " + m.getName() + Arrays.toString(m.getParameterTypes()));
		try {
			for(int i = 0; i < iterations; i++)
				m.invoke(invTarget, args);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		long delta = System.nanoTime()-t1;
		long perIteration = delta / iterations;
		System.out.println("Time total/per iteration: "+ delta + " / " + perIteration);
	}
	
	public static void timeTest(int arg, int iterations) {
		System.out.println("testing nextPrime(int):");
		long t1 = System.nanoTime();		
		for (int i = 1; i < iterations; i++)
			nextPrime(arg);		
		long delta = System.nanoTime()-t1;
		long perIteration = delta / iterations;
		System.out.println("Time total/per iteration: "+ delta + " / " + perIteration);
		
		System.out.println("testing nextPrime(Integer):");
		long t2 = System.nanoTime();		
		for (int i = 1; i < iterations; i++)
			nextPrime(new Integer(arg));		
		long delta2 = System.nanoTime()-t2;
		long perIteration2 = delta2 / iterations;
		System.out.println("Time total/per iteration: "+ delta2 + " / " + perIteration2);
	}

	public static void main(String[] args) throws Exception{
//		final int value = 1000;
//		final int loops = 40000;
//		
//		timeTestReflect(null, PrimeCalculator.class.getDeclaredMethod(
//				"nextPrime", int.class), new Integer[] {value}, loops);
//		timeTestReflect(null, PrimeCalculator.class.getDeclaredMethod(
//				"nextPrime", Integer.class), new Integer[] {value}, loops);
//		
//		timeTest(value, loops);
		
//		for (int i = 1; i < 100; i = (int)(i * 1.15 + 1))
//			System.out.println(nextPrime(i));
		System.out.println(getPrimes(100));
	}

}

/*
 * 
 * 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 
 * 67, 71, 73, 79, 83, 89, 97, 101
 * 
 */
