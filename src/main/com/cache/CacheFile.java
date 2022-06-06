package com.cache;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * The {@code CacheFile} is stored within a {@code CacheArchive}. This
 * represents a file with a version that is meant to be compared to a user's
 * public files used by the client and is updated by the update server.
 * 
 * @author Albert Beaupre
 */
public class CacheFile {

	private final CacheArchive archive;
	private int index;
	private double version;
	private byte[] data;

	private long checksum;

	/**
	 * Constructs a new {@code CacheFile} with no byte data.
	 * 
	 * @param index the index of this file
	 */
	public CacheFile(CacheArchive archive, int index) {
		this.archive = archive;
		this.index = index;
		this.data = new byte[0];
		this.version = 1.0;
	}

	/**
	 * The bytes of data this file contains.
	 * 
	 * @return the bytes of data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Sets the given byte array of data to this {@code CacheFile}.
	 * 
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;

		this.generateChecksum();
	}

	/**
	 * The corresponding index of this {@code CacheFile}.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index of this {@code CacheFile} to the given {@code index}.
	 * 
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Returns the version of this {@code CacheFile}.
	 * 
	 * @return the version
	 */
	public double getVersion() {
		return version;
	}

	/**
	 * Sets the version of this {@code CacheFile} to the given {@code version}.
	 * 
	 * @param version the version to set
	 */
	public void setVersion(double version) {
		this.version = version;

		this.generateChecksum();
	}

	/**
	 * Returns the checksum of this {@code CacheFile}. This is generated any time
	 * the version or file data is changed.
	 * 
	 * @return the checksum
	 */
	public long getChecksum() {
		return checksum;
	}

	/**
	 * Generates a 512 bit long checksum.
	 */
	public void generateChecksum() {
		Hasher hasher = Hashing.sha512().newHasher();
		hasher.putInt(this.archive.getIndex());
		hasher.putInt(this.index);
		hasher.putDouble(this.version);
		hasher.putBytes(this.data);
		this.checksum = hasher.hash().asLong();
	}

	/**
	 * Returns the archive that this {@code CacheFile} is attached to.
	 * 
	 * @return the archive
	 */
	public CacheArchive getArchive() {
		return archive;
	}

	@Override
	public String toString() {
		return String.format("CacheFile[index=%s, version=%s, size=%s, checksum=%s]", this.index, this.version, this.data.length, this.checksum);
	}

}
