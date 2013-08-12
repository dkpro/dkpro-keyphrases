package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;

/**
 * Provides assumes that should hold before each test.
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class AbstractBookIndexingTestBase

{

	public final static double EPSILON = 0.00000001;

	@Before
	public void beforeMethod()
	{
		Assume.assumeTrue(isSufficientHeapSize());
	}

	private static boolean isSufficientHeapSize()
	{
		/*
		 * Specified the lower heapsize limit in MB instead of the MiB because we
		 * have slightly less RAM returned by maxMemory(). It seems that when
		 * passing "-Xms${MEM}M -Xmx${MEM}" to the JVM the following holds: ${MEM} *
		 * 1000 * 1000 < getRuntime().maxMemory() < {MEM} * 1024 * 1024
		 */

		// System.out.println("MB: " + 128 * 1000 * 1000);
		// System.out.println("MiB: " + 128 * 1024 * 1024);
		// System.out.println("Runtime: " + Runtime.getRuntime().maxMemory());

		return Runtime.getRuntime().maxMemory() >= 256 * 1000 * 1000;
	}

	public static <T> List<T> list(T... objects)
	{
		List<T> result = new ArrayList<T>();
		for (T t : objects)
			result.add(t);
		return result;
	}

}
