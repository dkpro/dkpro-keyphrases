/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Provides methods to serialize and deserialize arbitrary objects to/from disk.
 *
 * @author Mateusz Parzonka
 *
 */
public class SerializationUtils
{

	/**
	 * @param object
	 *          object to be serialized
	 * @param filePath
	 *          the object will be written at this location, directories will be
	 *          created according to path
	 * @throws Exception
	 */
	public static void serializeToDisk(Serializable object, String filePath)
		throws Exception
	{
		File dir = new File(FilenameUtils.getFullPathNoEndSeparator(filePath));
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		else {
			if (dir.isFile()) {
				throw new IOException("Path to dir is a file!");
			}
		}
		ObjectOutputStream objOut = null;
		objOut = new ObjectOutputStream(new BufferedOutputStream(
				new FileOutputStream(filePath)));
		objOut.writeObject(object);
		objOut.flush();
		objOut.close();
	}

	/**
	 * Deserializes an object from disk.
	 *
	 * @param filePath
	 * @return an object. Clients have to cast the object to the expected type.
	 * @throws Exception
	 */
	public static Object deserializeFromDisk(String filePath)
		throws Exception
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(
				filePath)));

		return in.readObject();
	}


}
