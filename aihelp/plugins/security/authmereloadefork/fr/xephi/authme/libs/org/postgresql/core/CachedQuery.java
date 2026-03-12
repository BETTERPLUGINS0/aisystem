package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.CanEstimateSize;

public class CachedQuery implements CanEstimateSize {
   public final Object key;
   public final Query query;
   public final boolean isFunction;
   private int executeCount;

   public CachedQuery(Object key, Query query, boolean isFunction) {
      assert key instanceof String || key instanceof CanEstimateSize : "CachedQuery.key should either be String or implement CanEstimateSize. Actual class is " + key.getClass();

      this.key = key;
      this.query = query;
      this.isFunction = isFunction;
   }

   public void increaseExecuteCount() {
      if (this.executeCount < Integer.MAX_VALUE) {
         ++this.executeCount;
      }

   }

   public void increaseExecuteCount(int inc) {
      int newValue = this.executeCount + inc;
      if (newValue > 0) {
         this.executeCount = newValue;
      }

   }

   public int getExecuteCount() {
      return this.executeCount;
   }

   public long getSize() {
      long queryLength;
      if (this.key instanceof String) {
         queryLength = (long)((String)this.key).length() * 2L;
      } else {
         queryLength = ((CanEstimateSize)this.key).getSize();
      }

      return queryLength * 2L + 100L;
   }

   public String toString() {
      return "CachedQuery{executeCount=" + this.executeCount + ", query=" + this.query + ", isFunction=" + this.isFunction + '}';
   }
}
