package com.github.asifmujteba.easyvolley;

/**
 * Created by asifmujteba on 07/08/15.
 */
public interface ASFCache {
    ASFEntry get(String key);
    void put(String key, ASFEntry entry);
    void initialize();
    void remove(String key);
    void clear();
    int getCurrentSize();
    int getMaxSize();
    void setMaxSize(int maxSize);

    class ASFEntry {
        private static final String TAG = ASFEntry.class.getName().toString();

        private Object mData;
        private int mSize;
        private String mUrl;
        private long timeStampMillis;

        public ASFEntry(Object data, int size, String url, long timeStampMillis) {
            this.mData = data;
            this.mSize = size;
            this.mUrl = url;
            this.timeStampMillis = timeStampMillis;
        }

        public Object getData() {
            return mData;
        }

        public int getSize() {
            return mSize;
        }

        public String getUrl() {
            return mUrl;
        }

        public long getTimeStampMillis() {
            return timeStampMillis;
        }
    }
}
