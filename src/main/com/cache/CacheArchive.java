package com.cache;

import java.util.Arrays;
import java.util.Objects;

import com.util.ArrayUtil;

/**
 * The {@code CacheArchive} holds multiple {@code CacheFiles} and are
 * index-based so they can be stored or retrieved fast. This is necessary as the
 * files are constantly being requested by the update server.
 * 
 * @author Albert Beaupre
 */
public class CacheArchive {

	private int index;
	private CacheFile[] files;

	/**
	 * Constructs an empty {@code CacheArchive} with the given {@code index} as the
	 * corresponding index to store this in a {@code CacheStore}.
	 * 
	 * @param index the index to set
	 */
	public CacheArchive(int index) {
		this.index = index;
		this.files = new CacheFile[0];
	}

	/**
	 * Adds the given {@code file} to this {@code CacheArchive}.
	 * 
	 * @param file the file to add
	 */
	public void addFile(CacheFile file) {
		if (file.getIndex() <= -1)
			throw new IllegalArgumentException("A cache file cannot be stored with a negative ID");
		this.files = ArrayUtil.ensureCapacity(this.files, file.getIndex());
		this.files[file.getIndex()] = file;
	}

	/**
	 * Removes the {@code CacheFile} with the corresponding {@code index}. This will
	 * cause the array of files to shift and all files will have their index
	 * re-assigned based on their new position.
	 * 
	 * @param index the index corresponding to the file
	 */
	public void removeFile(int index) {
		this.files[index] = null;
		this.files = Arrays.stream(this.files).filter(archive -> Objects.nonNull(archive)).toArray(CacheFile[]::new);
		for (int i = 0; i < this.files.length; i++)
			this.files[i].setIndex(i);
	}

	/**
	 * Adds a new {@code CacheFile} to this {@code CacheArchive} with the given
	 * {@code id} and {@code data}.
	 * 
	 * @param id   the id of the file
	 * @param data the data in bytes of the file
	 */
	public void addFile(int id, byte[] data) {
		CacheFile file = new CacheFile(this, id);
		file.setData(data);
		this.addFile(file);
	}

	/**
	 * Returns the {@code CacheFile} within the array of files with the given
	 * {@code id}.
	 * 
	 * @param id the id of the file
	 * @return the cache file
	 */
	public CacheFile getFile(int id) {
		if (files.length <= id)
			throw new ArrayIndexOutOfBoundsException("File count is " + files.length + ". Cannot access file: [" + id + "]");
		if (id <= -1)
			throw new ArrayIndexOutOfBoundsException("Cannot access a file with a negative ID");
		return files[id];
	}

	/**
	 * Returns the {@code CacheFile} within the array of files with the given
	 * {@code id}. If no {@code CacheFile} exists, then one is created and returned.
	 * 
	 * @param id the id of the file
	 * @return the cache file
	 */
	public CacheFile createOrGetFile(int id) {
		try {
			return getFile(id);
		} catch (Exception e) {
			CacheFile file = new CacheFile(this, id);
			this.addFile(file);
			return file;
		}
	}

	/**
	 * Returns the array of {@code CacheFile} in this {@code CacheArchive}.
	 * 
	 * @return the array of files
	 */
	public CacheFile[] getFiles() {
		return files;
	}

	/**
	 * Returns the index of this {@code CacheArchive}.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index of this {@code CacheArchive} to the given {@code index}.
	 * 
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

}
