package com.github.asifmujteba.easyvolley;

import java.util.HashMap;

/**
 * Created by asifmujteba on 07/08/15.
 */
public class ASFMemoryCache implements ASFCache {
    private static final String TAG = ASFMemoryCache.class.getName().toString();

    /*
    * Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
    * and use 1/8th of the available memory for this memory cache.
    */
    public static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);

    private int maxSize = DEFAULT_CACHE_SIZE;
    private HashMap<String, ASFEntry> mEntries;

    protected ASFMemoryCache() {
        initialize();
    }

    @Override
    public ASFEntry get(String key) {
        if (mEntries.containsKey(key)) {
            return mEntries.get(key);
        }
        return null;
    }

    @Override
    public void put(String key, ASFEntry newEntry) {
        if (newEntry.getSize() > getMaxSize()) { // don't bother caching
            return;
        }

        int totalSize = getCurrentSize();
        while (totalSize + newEntry.getSize() > getMaxSize()) { // have to reduce cache size
            ASFEntry entry = findLeastUsedEntry();
            if (entry != null) {
                remove(entry.getUrl());
            }
            totalSize = getCurrentSize();
        }

        synchronized (mEntries) {
            mEntries.put(key, newEntry);
        }
    }

    private ASFEntry findLeastUsedEntry() {
        ASFEntry leastRecentEntry = null;
        for (ASFEntry entry : mEntries.values()) {
            if (leastRecentEntry == null) {
                leastRecentEntry = entry;
            }
            else if (entry.getTimeStampMillis() < leastRecentEntry.getTimeStampMillis()) {
                leastRecentEntry = entry;
            }
        }
        return leastRecentEntry;
    }

    @Override
    public void initialize() {
        mEntries = new HashMap<>();
    }

    @Override
    public void remove(String key) {
        synchronized (mEntries) {
            if (mEntries.containsKey(key)) {
                mEntries.remove(key);
            }
        }
    }

    @Override
    public void clear() {
        mEntries.clear();
    }

    public int getCurrentSize() {
        int total = 0;
        for (ASFEntry entry : mEntries.values()) {
            total += entry.getSize();
        }
        return total;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
