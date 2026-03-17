package com.bergerkiller.bukkit.tc.offline.sign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface OfflineSignMetadataHandler<T> {
   default int getMetadataVersion() {
      return 0;
   }

   default OfflineSignMetadataHandler.DataMigrationDecoder<T> getMigrationDecoder(OfflineSign sign, int dataVersion) {
      throw new UnsupportedOperationException("Not supported");
   }

   void onUpdated(OfflineSignStore var1, OfflineSign var2, T var3, T var4);

   void onAdded(OfflineSignStore var1, OfflineSign var2, T var3);

   void onRemoved(OfflineSignStore var1, OfflineSign var2, T var3);

   default void onLoaded(OfflineSignStore store, OfflineSign sign, T metadata) {
      this.onAdded(store, sign, metadata);
   }

   default void onUnloaded(OfflineSignStore store, OfflineSign sign, T metadata) {
      this.onRemoved(store, sign, metadata);
   }

   default T onSignChanged(OfflineSignStore store, OfflineSign oldSign, OfflineSign newSign, T metadata) {
      return null;
   }

   void onEncode(DataOutputStream var1, OfflineSign var2, T var3) throws IOException;

   T onDecode(DataInputStream var1, OfflineSign var2) throws IOException;

   default boolean isUnloadedWorldsIgnored() {
      return true;
   }

   public static final class InvalidMetadataException extends RuntimeException {
      private static final long serialVersionUID = 1301135081987007765L;
   }

   public interface DataMigrationDecoder<T> {
      T onDecode(DataInputStream var1, OfflineSign var2, int var3) throws IOException;
   }
}
