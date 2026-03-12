package fr.xephi.authme.libs.org.postgresql.core.v3.adaptivefetch;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AdaptiveFetchCache {
   private final Map<String, AdaptiveFetchCacheEntry> adaptiveFetchInfoMap = new HashMap();
   private boolean adaptiveFetch;
   private final int minimumAdaptiveFetchSize;
   private int maximumAdaptiveFetchSize = -1;
   private long maximumResultBufferSize = -1L;

   public AdaptiveFetchCache(long maximumResultBufferSize, Properties info) throws SQLException {
      this.adaptiveFetch = PGProperty.ADAPTIVE_FETCH.getBoolean(info);
      this.minimumAdaptiveFetchSize = PGProperty.ADAPTIVE_FETCH_MINIMUM.getInt(info);
      this.maximumAdaptiveFetchSize = PGProperty.ADAPTIVE_FETCH_MAXIMUM.getInt(info);
      this.maximumResultBufferSize = maximumResultBufferSize;
   }

   public void addNewQuery(boolean adaptiveFetch, Query query) {
      if (adaptiveFetch && this.maximumResultBufferSize != -1L) {
         String sql = query.getNativeSql().trim();
         AdaptiveFetchCacheEntry adaptiveFetchCacheEntry = (AdaptiveFetchCacheEntry)this.adaptiveFetchInfoMap.get(sql);
         if (adaptiveFetchCacheEntry == null) {
            adaptiveFetchCacheEntry = new AdaptiveFetchCacheEntry();
         }

         adaptiveFetchCacheEntry.incrementCounter();
         this.adaptiveFetchInfoMap.put(sql, adaptiveFetchCacheEntry);
      }

   }

   public void updateQueryFetchSize(boolean adaptiveFetch, Query query, int maximumRowSizeBytes) {
      if (adaptiveFetch && this.maximumResultBufferSize != -1L) {
         String sql = query.getNativeSql().trim();
         AdaptiveFetchCacheEntry adaptiveFetchCacheEntry = (AdaptiveFetchCacheEntry)this.adaptiveFetchInfoMap.get(sql);
         if (adaptiveFetchCacheEntry != null) {
            int adaptiveMaximumRowSize = adaptiveFetchCacheEntry.getMaximumRowSizeBytes();
            if (adaptiveMaximumRowSize < maximumRowSizeBytes && maximumRowSizeBytes > 0) {
               int newFetchSize = (int)(this.maximumResultBufferSize / (long)maximumRowSizeBytes);
               newFetchSize = this.adjustFetchSize(newFetchSize);
               adaptiveFetchCacheEntry.setMaximumRowSizeBytes(maximumRowSizeBytes);
               adaptiveFetchCacheEntry.setSize(newFetchSize);
               this.adaptiveFetchInfoMap.put(sql, adaptiveFetchCacheEntry);
            }
         }
      }

   }

   public int getFetchSizeForQuery(boolean adaptiveFetch, Query query) {
      if (adaptiveFetch && this.maximumResultBufferSize != -1L) {
         String sql = query.getNativeSql().trim();
         AdaptiveFetchCacheEntry adaptiveFetchCacheEntry = (AdaptiveFetchCacheEntry)this.adaptiveFetchInfoMap.get(sql);
         if (adaptiveFetchCacheEntry != null) {
            return adaptiveFetchCacheEntry.getSize();
         }
      }

      return -1;
   }

   public void removeQuery(boolean adaptiveFetch, Query query) {
      if (adaptiveFetch && this.maximumResultBufferSize != -1L) {
         String sql = query.getNativeSql().trim();
         AdaptiveFetchCacheEntry adaptiveFetchCacheEntry = (AdaptiveFetchCacheEntry)this.adaptiveFetchInfoMap.get(sql);
         if (adaptiveFetchCacheEntry != null) {
            adaptiveFetchCacheEntry.decrementCounter();
            if (adaptiveFetchCacheEntry.getCounter() < 1) {
               this.adaptiveFetchInfoMap.remove(sql);
            } else {
               this.adaptiveFetchInfoMap.put(sql, adaptiveFetchCacheEntry);
            }
         }
      }

   }

   private int adjustFetchSize(int actualSize) {
      int size = this.adjustMaximumFetchSize(actualSize);
      size = this.adjustMinimumFetchSize(size);
      return size;
   }

   private int adjustMinimumFetchSize(int actualSize) {
      if (this.minimumAdaptiveFetchSize == 0) {
         return actualSize;
      } else {
         return this.minimumAdaptiveFetchSize > actualSize ? this.minimumAdaptiveFetchSize : actualSize;
      }
   }

   private int adjustMaximumFetchSize(int actualSize) {
      if (this.maximumAdaptiveFetchSize == -1) {
         return actualSize;
      } else {
         return this.maximumAdaptiveFetchSize < actualSize ? this.maximumAdaptiveFetchSize : actualSize;
      }
   }

   public boolean getAdaptiveFetch() {
      return this.adaptiveFetch;
   }

   public void setAdaptiveFetch(boolean adaptiveFetch) {
      this.adaptiveFetch = adaptiveFetch;
   }
}
