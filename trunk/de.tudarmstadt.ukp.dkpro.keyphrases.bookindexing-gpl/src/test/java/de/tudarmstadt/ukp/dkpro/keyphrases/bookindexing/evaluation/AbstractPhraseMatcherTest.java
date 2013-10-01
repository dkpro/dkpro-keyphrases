/*******************************************************************************
 * Copyright 2013

 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation;

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
public abstract class AbstractPhraseMatcherTest

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

		return Runtime.getRuntime().maxMemory() >= 512 * 1000 * 1000;
	}

	public static <T> List<T> list(T... objects)
	{
		List<T> result = new ArrayList<T>();
		for (T t : objects)
			result.add(t);
		return result;
	}

}
