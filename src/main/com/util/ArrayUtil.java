package com.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

/**
 * @author Albert Beaupre
 */
@SuppressWarnings("unchecked")
public final class ArrayUtil {

	/** A pseudorandom number generator **/
	private static Random r = new Random();

	/**
	 * Creates an array starting from the specified {@code start} argument and
	 * ending at the {@code end} argument incrementing at the specified
	 * {@code increment} argument.
	 * 
	 * @param start     the starting value
	 * @param end       the ending value
	 * @param increment the value to increment by
	 * @return an array filled with the values ranging from start to end by the
	 *         increment value
	 */
	public static double[] range(double start, double end, double increment) {
		double[] a = new double[(int) (Math.abs(start - end) / increment)];
		int index = 0;
		double j = start < end ? (increment < 0 ? -increment : increment) : (increment < 0 ? increment : -increment);
		for (double i = start; start < end ? i <= end : i >= end; i += j) {
			a = Arrays.copyOf(a, index + 1);
			a[index++] = i;
		}
		return a;
	}

	/**
	 * Creates an array starting from the specified {@code start} argument and
	 * ending at the {@code end} argument incrementing at the specified
	 * {@code increment} argument.
	 * 
	 * @param start     the starting value
	 * @param end       the ending value
	 * @param increment the value to increment by
	 * @return an array filled with the values ranging from start to end by the
	 *         increment value
	 */
	public static int[] range(int start, int end, int increment) {
		int[] a = new int[Math.abs(start - end) / increment];
		int index = 0;
		int j = start < end ? (increment < 0 ? -increment : increment) : (increment < 0 ? increment : -increment);
		for (int i = start; start < end ? i <= end : i >= end; i += j) {
			a = Arrays.copyOf(a, index + 1);
			a[index++] = i;
		}
		return a;
	}

	/**
	 * Creates an array starting from the specified {@code start} argument and
	 * ending at the {@code end} argument incrementing at the specified
	 * {@code increment} argument.
	 * 
	 * @param start     the starting value
	 * @param end       the ending value
	 * @param increment the value to increment by
	 * @return an array filled with the values ranging from start to end by the
	 *         increment value
	 */
	public static float[] range(float start, float end, float increment) {
		float[] a = new float[(int) (Math.abs(start - end) / increment)];
		int index = 0;
		float j = start < end ? (increment < 0 ? -increment : increment) : (increment < 0 ? increment : -increment);
		for (float i = start; start < end ? i <= end : i >= end; i += j) {
			a = Arrays.copyOf(a, index + 1);
			a[index++] = i;
		}
		return a;
	}

	/**
	 * Creates an array starting from the specified {@code start} argument and
	 * ending at the {@code end} argument incrementing at the specified
	 * {@code increment} argument.
	 * 
	 * @param start     the starting value
	 * @param end       the ending value
	 * @param increment the value to increment by
	 * @return an array filled with the values ranging from start to end by the
	 *         increment value
	 */
	public static short[] range(short start, short end, short increment) {
		short[] a = new short[Math.abs(start - end) / increment];
		int index = 0;
		short j = (short) (start < end ? (increment < 0 ? -increment : increment) : (increment < 0 ? increment : -increment));
		for (short i = start; start < end ? i <= end : i >= end; i += j) {
			a = Arrays.copyOf(a, index + 1);
			a[index++] = i;
		}
		return a;
	}

	/**
	 * Creates an array starting from the specified {@code start} argument and
	 * ending at the {@code end} argument incrementing at the specified
	 * {@code increment} argument.
	 * 
	 * @param start     the starting value
	 * @param end       the ending value
	 * @param increment the value to increment by
	 * @return an array filled with the values ranging from start to end by the
	 *         increment value
	 */
	public static long[] range(long start, long end, long increment) {
		long[] a = new long[(int) (Math.abs(start - end) / increment)];
		int index = 0;
		long j = start < end ? (increment < 0 ? -increment : increment) : (increment < 0 ? increment : -increment);
		for (long i = start; start < end ? i <= end : i >= end; i += j) {
			a = Arrays.copyOf(a, index + 1);
			a[index++] = i;
		}
		return a;
	}

	/**
	 * Creates an array starting from the specified {@code start} argument and
	 * ending at the {@code end} argument incrementing at the specified
	 * {@code increment} argument.
	 * 
	 * @param start     the starting value
	 * @param end       the ending value
	 * @param increment the value to increment by
	 * @return an array filled with the values ranging from start to end by the
	 *         increment value
	 */
	public static byte[] range(byte start, byte end, byte increment) {
		byte[] a = new byte[Math.abs(start - end) / increment];
		int index = 0;
		byte j = (byte) (start < end ? (increment < 0 ? -increment : increment) : (increment < 0 ? increment : -increment));
		for (byte i = start; start < end ? i <= end : i >= end; i += j) {
			a = Arrays.copyOf(a, index + 1);
			a[index++] = i;
		}
		return a;
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static <T> void shuffle(T[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(int[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(short[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(float[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(byte[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(boolean[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(long[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(double[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Shuffles the elements within the specified {@code Array}.
	 * 
	 * @param a the array to shuffle
	 * @return the shuffled array
	 */
	public static void shuffle(char[] a) {
		for (int i = 0; i < a.length; i++)
			swap(i, r.nextInt(a.length), a);
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static <T> void swap(int i, int j, T[] a) {
		T old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, int[] a) {
		int old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, short[] a) {
		short old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, float[] a) {
		float old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, byte[] a) {
		byte old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, boolean[] a) {
		boolean old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, long[] a) {
		long old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, double[] a) {
		double old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Swaps the specified indicies {@code i} and {@code j} in the specified array.
	 * 
	 * @param i the first index to swap
	 * @param j the second index to swap
	 * @param a the array to swap the indicies
	 */
	public static void swap(int i, int j, char[] a) {
		char old = a[i];
		a[i] = a[j];
		a[j] = old;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type} using the specified {@code equals} filter to check if a type is
	 * similar.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static <T> boolean contains(T t, T[] a, Predicate<T> equals) {
		for (Object o : a)
			if (o != null && equals.test((T) o))
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static <T> boolean contains(T t, T[] a) {
		for (Object o : a)
			if (o != null && o.equals(t))
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(int t, int[] a) {
		for (int o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(short t, short[] a) {
		for (short o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(double t, double[] a) {
		for (double o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(float t, float[] a) {
		for (float o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(byte t, byte[] a) {
		for (byte o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(char t, char[] a) {
		for (char o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(long t, long[] a) {
		for (long o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns true if the specified {@code Array} contains the specified
	 * {@code type}; false is returned otherwise.
	 * 
	 * @param a the array to search
	 * @param t the object to check
	 * @return true if the array contains the specified type; return false otherwise
	 */
	public static boolean contains(boolean t, boolean[] a) {
		for (boolean o : a)
			if (o == t)
				return true;
		return false;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static <T> int indexOf(T o, T[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(int o, int[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(short o, short[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(byte o, byte[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(float o, float[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(double o, double[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(long o, long[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(char o, char[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the specified {@code o} argument within the specified
	 * {@code a} Array. If there is no index, -1 is returned instead.
	 * 
	 * @param o the value to search for
	 * @param a the array to search
	 * @return the index of the value; return -1 otherwise
	 */
	public static int indexOf(boolean o, boolean[] a) {
		for (int i = 0; i < a.length; i++)
			if (o == a[i])
				return i;
		return -1;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static <T> T[] removeIndex(T[] a, int index) {
		T[] n = (T[]) Array.newInstance(a[0].getClass(), a.length - 1);
		System.arraycopy(a, 0, n, 0, index);
		System.arraycopy(a, index + 1, n, index, a.length - index - 1);
		return n;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static byte[] removeIndex(byte[] a, int index) {
		byte[] b = new byte[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static int[] removeIndex(int[] a, int index) {
		int[] b = new int[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static short[] removeIndex(short[] a, int index) {
		short[] b = new short[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static long[] removeIndex(long[] a, int index) {
		long[] b = new long[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static float[] removeIndex(float[] a, int index) {
		float[] b = new float[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static double[] removeIndex(double[] a, int index) {
		double[] b = new double[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static char[] removeIndex(char[] a, int index) {
		char[] b = new char[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code index} removed from the
	 * specified {@code a}.
	 * 
	 * @param a     the array to remove the index from
	 * @param index the index to remove
	 * @return the array with the removed index
	 */
	public static boolean[] removeIndex(boolean[] a, int index) {
		boolean[] b = new boolean[a.length - 1];
		System.arraycopy(a, 0, b, 0, index);
		System.arraycopy(a, index + 1, b, index, a.length - index - 1);
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static <T> T[] removeElement(T[] a, T element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static boolean[] removeElement(boolean[] a, boolean element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static byte[] removeElement(byte[] a, byte element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static char[] removeElement(char[] a, char element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static float[] removeElement(float[] a, float element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static double[] removeElement(double[] a, double element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static long[] removeElement(long[] a, long element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static short[] removeElement(short[] a, short element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} removed from the
	 * specified {@code a}.
	 * 
	 * @param a       the array to remove the element from
	 * @param element the element to remove
	 * @return the array with the removed element
	 */
	public static int[] removeElement(int[] a, int element) {
		return removeIndex(a, indexOf(element, a));
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static <T> T[] addElement(T[] a, T... element) {
		T[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static int[] addElement(int[] a, int... element) {
		int[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static short[] addElement(short[] a, short... element) {
		short[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static long[] addElement(long[] a, long... element) {
		long[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static float[] addElement(float[] a, float... element) {
		float[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static double[] addElement(double[] a, double... element) {
		double[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static byte[] addElement(byte[] a, byte... element) {
		byte[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static char[] addElement(char[] a, char... element) {
		char[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * end of the specified {@code a}
	 * 
	 * @param a       the array to add the element to
	 * @param element the element to add
	 * @return the array with the added element
	 */
	public static boolean[] addElement(boolean[] a, boolean... element) {
		boolean[] b = Arrays.copyOf(a, a.length + element.length);
		for (int i = element.length - 1; i >= 0; i--)
			b[b.length - (i + 1)] = element[i];
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static <T> T[] insertElement(T[] a, int index, T element) {
		T[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static int[] insertElement(int[] a, int index, int element) {
		int[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static long[] insertElement(long[] a, int index, long element) {
		long[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static short[] insertElement(short[] a, int index, short element) {
		short[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static byte[] insertElement(byte[] a, int index, byte element) {
		byte[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static char[] insertElement(char[] a, int index, char element) {
		char[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static float[] insertElement(float[] a, int index, float element) {
		float[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static double[] insertElement(double[] a, int index, double element) {
		double[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns the {@code Array} with the specified {@code element} inserted at the
	 * specified {@code index} of the specified {@code a}.
	 * 
	 * @param a       the array to insert the element to
	 * @param index   the index to insert the element at
	 * @param element the element to insert
	 * @return the array with the inserted element
	 */
	public static boolean[] insertElement(boolean[] a, int index, boolean element) {
		boolean[] b = Arrays.copyOf(a, a.length + 1);
		System.arraycopy(b, index, b, index + 1, a.length - index);
		b[index] = element;
		return b;
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static <T> T randomElement(T[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static int randomElement(int[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static short randomElement(short[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static long randomElement(long[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static byte randomElement(byte[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static float randomElement(float[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static double randomElement(double[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static char randomElement(char[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Returns a pseudorandom element from the specified {@code a}.
	 * 
	 * @param a the array to retrieve the pseudorandomly retrieved element from
	 * @return the pseudorandomly retrieved element
	 */
	public static boolean randomElement(boolean[] a) {
		return a[r.nextInt(a.length)];
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static <T> T[] reverse(T[] a) {
		T[] reverse = (T[]) Array.newInstance(a[0].getClass(), a.length);
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static int[] reverse(int[] a) {
		int[] reverse = new int[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static short[] reverse(short[] a) {
		short[] reverse = new short[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static long[] reverse(long[] a) {
		long[] reverse = new long[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static byte[] reverse(byte[] a) {
		byte[] reverse = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static float[] reverse(float[] a) {
		float[] reverse = new float[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static double[] reverse(double[] a) {
		double[] reverse = new double[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static char[] reverse(char[] a) {
		char[] reverse = new char[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Reverses the elements of the specified {@code a}.
	 * 
	 * @param a the array to reverse
	 * @return the reversed array
	 */
	public static boolean[] reverse(boolean[] a) {
		boolean[] reverse = new boolean[a.length];
		for (int i = a.length - 1; i >= 0; i--)
			reverse[i] = a[a.length - 1 - i];
		return reverse;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static <T> T[] filter(T[] a, Predicate<T> predicate, Class<T> clazz) {
		T[] arr = (T[]) Array.newInstance(clazz, 0);
		for (T t : a)
			if (predicate.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static int[] filter(int[] a, Predicate<Integer> filter) {
		int[] arr = new int[0];
		for (int t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static short[] filter(short[] a, Predicate<Short> filter) {
		short[] arr = new short[0];
		for (short t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static long[] filter(long[] a, Predicate<Long> filter) {
		long[] arr = new long[0];
		for (long t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static byte[] filter(byte[] a, Predicate<Byte> filter) {
		byte[] arr = new byte[0];
		for (byte t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static float[] filter(float[] a, Predicate<Float> filter) {
		float[] arr = new float[0];
		for (float t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static double[] filter(double[] a, Predicate<Double> filter) {
		double[] arr = new double[0];
		for (double t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static char[] filter(char[] a, Predicate<Character> filter) {
		char[] arr = new char[0];
		for (char t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Filters the specified {@code a} using the specified {@code predicate} for
	 * filtering.
	 * 
	 * @param a         the array to filter
	 * @param predicate the predicate to use for filtering
	 * @return the filtered array
	 */
	public static boolean[] filter(boolean[] a, Predicate<Boolean> filter) {
		boolean[] arr = new boolean[0];
		for (boolean t : a)
			if (filter.test(t))
				arr = addElement(arr, t);
		return arr;
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param <T>      the array type
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static <T> T[] ensureCapacity(T[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), newLength);
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static byte[] ensureCapacity(byte[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			byte[] result = new byte[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static char[] ensureCapacity(char[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			char[] result = new char[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static int[] ensureCapacity(int[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			int[] result = new int[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static short[] ensureCapacity(short[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			short[] result = new short[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static float[] ensureCapacity(float[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			float[] result = new float[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static double[] ensureCapacity(double[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			double[] result = new double[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static long[] ensureCapacity(long[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			long[] result = new long[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}

	/**
	 * Ensures the capacity of the given {@code array} is within the value of the
	 * {@code maxIndex}.
	 * 
	 * @param array    the array to ensure capacity
	 * @param maxIndex the maximum index to have on the array
	 * @return the newly resized array if it was increased in capacity
	 */
	public static boolean[] ensureCapacity(boolean[] array, int maxIndex) {
		if (maxIndex < array.length) {
			return array;
		} else {
			int newLength = array.length + 1;
			boolean[] result = new boolean[newLength];
			System.arraycopy(array, 0, result, 0, array.length);
			return result;
		}
	}
}