package cache;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.cache.CacheArchive;
import com.cache.CacheStore;

public class CacheTests {

	@Test
	void testCacheStoreFunctions() {
		CacheStore store = new CacheStore();

		/**
		 * Add archive with index 0
		 */
		store.addArchive(new CacheArchive(0));

		/**
		 * Checking if the archive with index 0 is within the cache store
		 */
		assertTrue(Objects.nonNull(store.getArchive(0)), "Archive 0 not found after adding archive 0");

		/**
		 * Checking if adding the archive increases the size of the cache store
		 */
		assertTrue(store.getArchives().length == 1, "Archive size is not 1 after adding archive");

		/**
		 * Removes archive with index 0
		 */
		store.removeArchive(0);

		/**
		 * Checking if removing the archive decreases the size of the cache store
		 */
		assertTrue(store.getArchives().length == 0, "Archive size is not 0 after removing archive");

		/**
		 * Retrieves an archive if one exists, or creates one if it doesn't exist
		 */
		store.createOrGetArchive(0);

		/**
		 * Checking if createOrGetArchive works correctly
		 */
		assertTrue(Objects.nonNull(store.getArchive(0)), "Archive 0 not found after createOrGetArchive 0");
	}

}
