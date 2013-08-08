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
