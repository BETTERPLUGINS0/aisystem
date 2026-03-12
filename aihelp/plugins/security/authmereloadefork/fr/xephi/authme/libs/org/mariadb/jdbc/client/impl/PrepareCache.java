package fr.xephi.authme.libs.org.mariadb.jdbc.client.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.CachedPrepareResultPacket;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.PrepareResultPacket;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class PrepareCache extends LinkedHashMap<String, CachedPrepareResultPacket> implements fr.xephi.authme.libs.org.mariadb.jdbc.client.PrepareCache {
   private static final long serialVersionUID = -8922905563713952695L;
   private final int maxSize;
   private final transient StandardClient con;

   public PrepareCache(int size, StandardClient con) {
      super(size, 0.75F, true);
      this.maxSize = size;
      this.con = con;
   }

   public boolean removeEldestEntry(Entry<String, CachedPrepareResultPacket> eldest) {
      if (this.size() > this.maxSize) {
         ((CachedPrepareResultPacket)eldest.getValue()).unCache(this.con);
         return true;
      } else {
         return false;
      }
   }

   public synchronized Prepare get(String key, ServerPreparedStatement preparedStatement) {
      CachedPrepareResultPacket prepare = (CachedPrepareResultPacket)super.get(key);
      if (prepare != null && preparedStatement != null) {
         prepare.incrementUse(preparedStatement);
      }

      return prepare;
   }

   public synchronized Prepare put(String key, Prepare result, ServerPreparedStatement preparedStatement) {
      CachedPrepareResultPacket cached = (CachedPrepareResultPacket)super.get(key);
      if (cached != null) {
         cached.incrementUse(preparedStatement);
         ((CachedPrepareResultPacket)result).unCache(this.con);
         return cached;
      } else {
         if (((CachedPrepareResultPacket)result).cache()) {
            ((CachedPrepareResultPacket)result).incrementUse(preparedStatement);
            super.put(key, (CachedPrepareResultPacket)result);
         }

         return null;
      }
   }

   public CachedPrepareResultPacket get(Object key) {
      throw new IllegalStateException("not available method");
   }

   public CachedPrepareResultPacket put(String key, PrepareResultPacket result) {
      throw new IllegalStateException("not available method");
   }

   public void reset() {
      Iterator var1 = this.values().iterator();

      while(var1.hasNext()) {
         CachedPrepareResultPacket prep = (CachedPrepareResultPacket)var1.next();
         prep.reset();
      }

      this.clear();
   }
}
