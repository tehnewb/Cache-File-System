package com.cache;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import com.util.ArrayUtil;

/**
 * A CacheStore holds {@code CacheArchives}, which are index-based, and are
 * stored/retrieved to and from an array for fast access. The reason for this is
 * because cache files are constantly requested, and this is usually by a
 * server. It is suggested that the cache files are loaded entirely before
 * requesting them as the array holding the archives is manipulated when
 * adding/removing from this {@code CacheStore}.
 * 
 * @author Albert Beaupre
 *
 * @see com.cache.CacheArchive
 * @see com.cache.CacheFile
 */
public class CacheStore implements Iterable<CacheArchive> {

	private CacheArchive[] archives;

	/**
	 * Constructs an empty {@code CacheStore} with no {@code CacheArchive}.
	 */
	public CacheStore() {
		this.archives = new CacheArchive[0]; // empty
	}

	/**
	 * Adds the given {@code index to this {@code CacheStore}. The array storing all
	 * archives will be increased in capacity when this happens.
	 * 
	 * @param archive the archive to add
	 */
	public void addArchive(CacheArchive archive) {
		if (archive.getIndex() <= -1)
			throw new IllegalArgumentException("An archive index cannot be stored with a negative ID");
		if (archive.getIndex() > archives.length)
			throw new UnsupportedOperationException("The next archive index must be " + archives.length);
		this.archives = ArrayUtil.ensureCapacity(this.archives, archive.getIndex());
		this.archives[archive.getIndex()] = archive;
	}

	/**
	 * Removes the {@code CacheArchive} with the corresponding {@code index}. This
	 * will cause the array of archives to shift and all archives will have their
	 * index re-assigned based on their new position.
	 * 
	 * @param index the index corresponding to the archive
	 */
	public void removeArchive(int index) {
		this.archives[index] = null;
		this.archives = Arrays.stream(this.archives).filter(archive -> Objects.nonNull(archive)).toArray(CacheArchive[]::new);
		for (int i = 0; i < this.archives.length; i++)
			this.archives[i].setIndex(i);
	}

	/**
	 * Returns the {@code CacheArchive} with the corresponding {@code id}.
	 * 
	 * @param id the id of the index
	 * @return the cache index
	 */
	public CacheArchive getArchive(int id) {
		if (archives.length <= id)
			throw new ArrayIndexOutOfBoundsException("Index count is " + archives.length + ". Cannot access index: [" + id + "]");
		if (id <= -1)
			throw new ArrayIndexOutOfBoundsException("Cannot access an index with a negative ID");
		return archives[id];
	}

	/**
	 * Returns the {@code CacheArchive} with the corresponding {@code id}. If no
	 * {@code CacheArchive} exists, then one is created and returned.
	 * 
	 * @param id the id of the index
	 * @return the cache index
	 */
	public CacheArchive createOrGetArchive(int id) {
		try {
			return getArchive(id);
		} catch (Exception e) {
			CacheArchive index = new CacheArchive(id);
			addArchive(index);
			return index;
		}
	}

	/**
	 * Returns an array of {@code CacheArchive} in this {@code CacheStore}.
	 * 
	 * @return the array of archives
	 */
	public CacheArchive[] getArchives() {
		return this.archives;
	}

	/**
	 * Saves the given {@code CacheStore} to the given {@code file} using BZip2
	 * Compression.
	 * 
	 * @param store the cache store to save
	 * @param file  the file to save to
	 * @throws IOException if an error occurred saving the file
	 */
	public void save(File file) throws IOException {
		if (!file.exists()) {
			if (!file.createNewFile()) {
				return;
			}
		}

		if (this.archives.length == 0) {
			System.err.println("WARNING: Saving empty cache store");
		}

		int sizeInBytes = Integer.BYTES; // bytes for the indeces length

		for (int i = 0; i < this.archives.length; i++) {
			CacheArchive cacheIndex = this.getArchive(i);

			sizeInBytes += Integer.BYTES; // bytes for the index id
			sizeInBytes += Integer.BYTES; // bytes for file length 

			for (int j = 0; j < cacheIndex.getFiles().length; j++) {
				CacheFile cacheFile = cacheIndex.getFile(j);
				sizeInBytes += Integer.BYTES; // bytes for the file index
				sizeInBytes += Double.BYTES; // bytes for the file version
				sizeInBytes += Integer.BYTES; // bytes for the file length
				sizeInBytes += cacheFile.getData().length * Byte.BYTES; // add every byte count of the file length
			}
		}

		ByteBuffer storeBuffer = ByteBuffer.allocate(sizeInBytes);
		storeBuffer.putInt(this.archives.length);
		for (int i = 0; i < this.archives.length; i++) {
			CacheArchive cacheIndex = this.getArchive(i);

			storeBuffer.putInt(cacheIndex.getIndex());
			storeBuffer.putInt(cacheIndex.getFiles().length);

			for (int f = 0; f < cacheIndex.getFiles().length; f++) {
				CacheFile cacheFile = cacheIndex.getFile(f);

				storeBuffer.putInt(cacheFile.getIndex());
				storeBuffer.putDouble(cacheFile.getVersion());
				storeBuffer.putInt(cacheFile.getData().length);
				storeBuffer.put(cacheFile.getData(), 0, cacheFile.getData().length);
			}
		}
		Files.write(file.toPath(), storeBuffer.array());
	}

	/**
	 * Loads the given {@code file} and stores it into a {@code CacheStore}.
	 * 
	 * @param file the file to load
	 * @return the cache store to return
	 * @throws IOException if an error occurred loading the file
	 */
	public static CacheStore load(File file) throws IOException {
		CacheStore store = new CacheStore();
		ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));
		int indexCount = buffer.getInt();

		if (indexCount == 0) {
			System.err.println("WARNING: Loading empty cache");
		}

		for (int i = 0; i < indexCount; i++) {
			int indexID = buffer.getInt();
			int indexFileCount = buffer.getInt();
			CacheArchive archive = new CacheArchive(indexID);
			for (int j = 0; j < indexFileCount; j++) {
				int fileID = buffer.getInt();
				double version = buffer.getDouble();
				int fileSize = buffer.getInt();
				byte[] data = new byte[fileSize];
				buffer.get(data);

				CacheFile cacheFile = new CacheFile(archive, fileID);
				archive.addFile(cacheFile);
				cacheFile.setData(data);
				cacheFile.setVersion(version);
			}
			store.addArchive(archive);
		}
		return store;
	}

	/**
	 * Generates the {@code CacheFileRecord}s for each {@code CacheFile} within this
	 * {@code CacheStore} and stores it into an {@code ArrayList}.
	 * 
	 * @return a list of cache file records
	 */
	public ArrayList<CacheFileRecord> generateRecords() {
		ArrayList<CacheFileRecord> records = new ArrayList<>();
		Arrays.stream(this.archives).forEach(c -> {
			Arrays.stream(c.getFiles()).forEach(f -> {
				records.add(new CacheFileRecord(f.getIndex(), f.getVersion(), f.getData().length, f.getChecksum()));
			});
		});
		return records;
	}

	@Override
	public Iterator<CacheArchive> iterator() {
		return new Iterator<>() {
			private int cursor;

			@Override
			public boolean hasNext() {
				return cursor != archives.length;
			}

			@Override
			public CacheArchive next() {
				return archives[cursor++];
			}
		};
	}

	/**
	 * Holds the necessary information of a {@code CacheFile}, which will be used on
	 * an update server.
	 * 
	 * @author Albert Beaupre
	 */
	public record CacheFileRecord(int index, double version, int length, long checksum) {}
}
