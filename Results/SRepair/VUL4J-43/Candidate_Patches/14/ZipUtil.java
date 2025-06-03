/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/

package org.eclipse.rdf4j.common.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Zip-related utilities.
 */
public class ZipUtil {

	/**
	 * Magic number for ZIP files (4 bytes: <tt>0x04034b50</tt>).
	 */
	private final static byte MAGIC_NUMBER[] = { (byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04 };

Function ID: 1
Function ID: 1 has no buggy code, no modifications are necessary for this function to fix the bug.
	/**
	 * Extract the contents of a zipfile to a directory.
	 * 
	 * @param zipFile
	 *        the zip file to extract
	 * @param destDir
	 *        the destination directory
	 * @throws IOException
	 *         when something untoward happens during the extraction process
	 */
public static void extract(File zipFile, File destDir)
		throws IOException
	{
		try (ZipFile zf = new ZipFile(zipFile)) {
			extract(zf, destDir);
		} catch (IOException e) {
			throw new IOException("Error while extracting zip file: " + e.getMessage());
		}
	}
	/**
	 * Extract the contents of a zipfile to a directory.
	 * 
	 * @param zipFile
	 *        the zip file to extract
	 * @param destDir
	 *        the destination directory
	 * @throws IOException
	 *         when something untowards happens during the extraction process
	 */
public static void extract(ZipFile zipFile, File destDir)
		throws IOException
	{
		assert destDir.isDirectory();

		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();

			// Check if the entry is trying to traverse outside of the destination directory
			File entryFile = new File(destDir, entry.getName());
			if (!entryFile.getAbsolutePath().startsWith(destDir.getAbsolutePath() + File.separator)) {
				throw new IOException("Zip entry is trying to traverse outside of the destination directory");
			}

			writeEntry(zipFile, entry, destDir);
		}
	}
	/**
	 * Write an entry to a zip file.
	 * 
	 * @param zipFile
	 *        the zip file to read from
	 * @param entry
	 *        the entry to process
	 * @param destDir
	 *        the file to write to
	 * @throws IOException
	 *         if the entry could not be processed
	 */
public static void writeEntry(ZipFile zipFile, ZipEntry entry, File destDir)
		throws IOException
	{
		File outFile = new File(destDir, entry.getName());

		if (entry.isDirectory()) {
			outFile.mkdirs();
		}
		else {
			outFile.getParentFile().mkdirs();

			try (InputStream in = zipFile.getInputStream(entry)) {
				// Check if the entry is trying to traverse outside of the destination directory
				File outFileParent = outFile.getParentFile();
				if (!outFileParent.getAbsolutePath().startsWith(destDir.getAbsolutePath() + File.separator)) {
					throw new IOException("Zip entry is trying to traverse outside of the destination directory");
				}

				IOUtil.writeStream(in, outFile);
			}
		}
	}}
