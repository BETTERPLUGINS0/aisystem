package com.bergerkiller.bukkit.tc.rails;

import com.bergerkiller.bukkit.common.block.SignChangeTracker;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.utils.ListCallbackCollector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.logging.Level;
import org.bukkit.block.Block;

public final class TrackedSignLookup implements TrainCarts.Provider {
   private final TrainCarts plugin;
   private final List<TrackedSignLookup.SignSupplier> suppliers = new ArrayList();
   private final Map<String, TrackedSignLookup.RegisteredKeySerializer> serializersById = new HashMap();
   private final WeakHashMap<Class<?>, TrackedSignLookup.RegisteredKeySerializer> serializersByType = new WeakHashMap();
   private static final TrackedSignLookup.RegisteredKeySerializer MISSING_SERIALIZER = new TrackedSignLookup.RegisteredKeySerializer((String)null, (TrackedSignLookup.KeySerializer)null) {
      public byte[] serialize(TrainCarts plugin, Object uniqueKey) {
         return null;
      }
   };

   public TrackedSignLookup(TrainCarts plugin) {
      this.plugin = plugin;
      this.serializersByType.put(TrackedSignLookup.UnknownSignKey.class, new TrackedSignLookup.RegisteredKeySerializer((String)null, (TrackedSignLookup.KeySerializer)null) {
         public byte[] serialize(TrainCarts plugin, Object uniqueKey) {
            return ((TrackedSignLookup.UnknownSignKey)uniqueKey).data;
         }
      });
      this.registerSerializer("tc-realsign", new TrackedSignLookup.RealSignKeySerializer());
      this.registerSerializer("tc-uuid", new TrackedSignLookup.KeySerializer<UUID>() {
         public Class<UUID> getKeyType() {
            return UUID.class;
         }

         public UUID read(DataInputStream input) throws IOException {
            return StreamUtil.readUUID(input);
         }

         public void write(DataOutputStream output, UUID value) throws IOException {
            StreamUtil.writeUUID(output, value);
         }
      });
      this.registerSerializer("tc-string", new TrackedSignLookup.KeySerializer<String>() {
         public Class<String> getKeyType() {
            return String.class;
         }

         public String read(DataInputStream input) throws IOException {
            return input.readUTF();
         }

         public void write(DataOutputStream output, String value) throws IOException {
            output.writeUTF(value);
         }
      });
      this.register(new TrackedSignLookup.RealSignSupplier());
   }

   public TrainCarts getTrainCarts() {
      return this.plugin;
   }

   public RailLookup.TrackedSign getTrackedSign(Object uniqueKey) {
      Iterator var2 = this.suppliers.iterator();

      RailLookup.TrackedSign sign;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         TrackedSignLookup.SignSupplier supplier = (TrackedSignLookup.SignSupplier)var2.next();
         sign = supplier.getTrackedSign(uniqueKey);
      } while(sign == null);

      return sign;
   }

   public List<RailLookup.TrackedSign> getOutputtingTrackedSigns(Block block) {
      List<RailLookup.TrackedSign> results = Collections.emptyList();

      TrackedSignLookup.SignSupplier supplier;
      for(Iterator var3 = this.suppliers.iterator(); var3.hasNext(); results = LogicUtil.combineUnmodifiableLists(results, supplier.getOutputtingTrackedSigns(this.plugin, block))) {
         supplier = (TrackedSignLookup.SignSupplier)var3.next();
      }

      return results;
   }

   public <T> List<OfflineDataBlock> serializeUniqueKeys(Collection<T> items, String name, Function<T, Object> uniqueKeyGetter) {
      return this.serializeUniqueKeys(items, name, uniqueKeyGetter, (item, data) -> {
      });
   }

   public <T> List<OfflineDataBlock> serializeUniqueKeys(Collection<T> items, String name, Function<T, Object> uniqueKeyGetter, BiConsumer<T, OfflineDataBlock> extraMetaApplier) {
      if (items.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<OfflineDataBlock> dataBlocks = new ArrayList(items.size());
         Iterator var6 = items.iterator();

         while(var6.hasNext()) {
            T item = var6.next();
            byte[] data = this.serializeUniqueKey(uniqueKeyGetter.apply(item));
            if (data != null) {
               OfflineDataBlock dataBlock = OfflineDataBlock.createWithData(name, data);
               extraMetaApplier.accept(item, dataBlock);
               dataBlocks.add(dataBlock);
            }
         }

         return Collections.unmodifiableList(dataBlocks);
      }
   }

   public byte[] serializeUniqueKey(Object uniqueKey) {
      if (uniqueKey == null) {
         return null;
      } else {
         TrackedSignLookup.RegisteredKeySerializer registered = (TrackedSignLookup.RegisteredKeySerializer)this.serializersByType.get(uniqueKey.getClass());
         if (registered == null) {
            registered = MISSING_SERIALIZER;
            Class<?> type = uniqueKey.getClass();
            Iterator var4 = this.serializersByType.entrySet().iterator();

            while(var4.hasNext()) {
               Entry<Class<?>, TrackedSignLookup.RegisteredKeySerializer> mapped = (Entry)var4.next();
               if (((Class)mapped.getKey()).isAssignableFrom(type)) {
                  registered = (TrackedSignLookup.RegisteredKeySerializer)mapped.getValue();
                  break;
               }
            }

            this.serializersByType.put(type, registered);
         }

         return registered.serialize(this.plugin, uniqueKey);
      }
   }

   public List<Object> deserializeUniqueKeys(List<OfflineDataBlock> dataBlocks) {
      if (dataBlocks.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<Object> uniqueKeys = new ArrayList(dataBlocks.size());
         Iterator var3 = dataBlocks.iterator();

         while(var3.hasNext()) {
            OfflineDataBlock dataBlock = (OfflineDataBlock)var3.next();
            Object uniqueKey = this.deserializeUniqueKey(dataBlock.data);
            if (uniqueKey != null) {
               uniqueKeys.add(uniqueKey);
            }
         }

         return Collections.unmodifiableList(uniqueKeys);
      }
   }

   public Object deserializeUniqueKey(byte[] data) {
      try {
         ByteArrayInputStream stream = new ByteArrayInputStream(data);

         TrackedSignLookup.UnknownSignKey var6;
         label56: {
            Object var12;
            try {
               DataInputStream dataStream = new DataInputStream(stream);

               label52: {
                  try {
                     String id = dataStream.readUTF();
                     TrackedSignLookup.RegisteredKeySerializer registered = (TrackedSignLookup.RegisteredKeySerializer)this.serializersById.get(id);
                     if (registered != null) {
                        var12 = registered.serializer.read(dataStream);
                        break label52;
                     }

                     var6 = new TrackedSignLookup.UnknownSignKey(id, data);
                  } catch (Throwable var9) {
                     try {
                        dataStream.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }

                     throw var9;
                  }

                  dataStream.close();
                  break label56;
               }

               dataStream.close();
            } catch (Throwable var10) {
               try {
                  stream.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }

            stream.close();
            return var12;
         }

         stream.close();
         return var6;
      } catch (Throwable var11) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to deserialize unique sign key", var11);
         return null;
      }
   }

   public void register(TrackedSignLookup.SignSupplier supplier) {
      if (!this.suppliers.contains(supplier)) {
         this.suppliers.add(supplier);
      }

   }

   public void unregister(TrackedSignLookup.SignSupplier supplier) {
      this.suppliers.remove(supplier);
   }

   public void registerSerializer(String id, TrackedSignLookup.KeySerializer<?> serializer) {
      Class<?> keyType = serializer.getKeyType();
      TrackedSignLookup.RegisteredKeySerializer registered = new TrackedSignLookup.RegisteredKeySerializer(id, serializer);
      this.serializersById.put(id, registered);
      this.serializersByType.put(keyType, registered);
      this.serializersByType.values().removeIf((s) -> {
         return s == MISSING_SERIALIZER;
      });
   }

   public void unregisterSerializer(String id) {
      this.serializersById.remove(id);
   }

   private static class UnknownSignKey {
      public final String id;
      public final byte[] data;

      public UnknownSignKey(String id, byte[] data) {
         this.id = id;
         this.data = data;
      }

      public int hashCode() {
         return this.id.hashCode();
      }

      public boolean equals(Object o) {
         return o instanceof TrackedSignLookup.UnknownSignKey ? Arrays.equals(this.data, ((TrackedSignLookup.UnknownSignKey)o).data) : false;
      }

      public String toString() {
         return "UnknownSignKey{" + this.id + "}@" + System.identityHashCode(this);
      }
   }

   public interface KeySerializer<T> {
      Class<T> getKeyType();

      T read(DataInputStream var1) throws IOException;

      void write(DataOutputStream var1, T var2) throws IOException;
   }

   private static class RealSignKeySerializer implements TrackedSignLookup.KeySerializer<TrackedSignLookup.RealSignKey> {
      private RealSignKeySerializer() {
      }

      public Class<TrackedSignLookup.RealSignKey> getKeyType() {
         return TrackedSignLookup.RealSignKey.class;
      }

      public TrackedSignLookup.RealSignKey read(DataInputStream input) throws IOException {
         byte version = input.readByte();
         if (version == 1) {
            OfflineBlock block = OfflineBlock.readFrom(input);
            boolean front = input.readBoolean();
            return new TrackedSignLookup.RealSignKey(block, front);
         } else {
            return null;
         }
      }

      public void write(DataOutputStream output, TrackedSignLookup.RealSignKey value) throws IOException {
         output.writeByte(1);
         OfflineBlock.writeTo(output, value.block);
         output.writeBoolean(value.front);
      }

      // $FF: synthetic method
      RealSignKeySerializer(Object x0) {
         this();
      }
   }

   private static class RealSignSupplier implements TrackedSignLookup.SignSupplier {
      private RealSignSupplier() {
      }

      public RailLookup.TrackedSign getTrackedSign(Object uniqueKey) {
         return uniqueKey instanceof TrackedSignLookup.RealSignKey ? ((TrackedSignLookup.RealSignKey)uniqueKey).findRealSign() : null;
      }

      public List<RailLookup.TrackedSign> getOutputtingTrackedSigns(TrainCarts trainCarts, Block block) {
         ListCallbackCollector<RailLookup.TrackedSign> signs = new ListCallbackCollector();
         trainCarts.getSignController().forWorld(block.getWorld()).forEachNearbyVerify(block, true, (entry) -> {
            if (entry.sign.isAttachedTo(block)) {
               if (entry.front.hasSignAction()) {
                  signs.accept(entry.front.createTrackedSign((RailPiece)null));
               }

               if (entry.back.hasSignAction()) {
                  signs.accept(entry.back.createTrackedSign((RailPiece)null));
               }
            }

         });
         return signs.result();
      }

      // $FF: synthetic method
      RealSignSupplier(Object x0) {
         this();
      }
   }

   @FunctionalInterface
   public interface SignSupplier {
      RailLookup.TrackedSign getTrackedSign(Object var1);

      default List<RailLookup.TrackedSign> getOutputtingTrackedSigns(TrainCarts trainCarts, Block block) {
         return Collections.emptyList();
      }
   }

   private static class RegisteredKeySerializer {
      public final String id;
      public final TrackedSignLookup.KeySerializer<Object> serializer;

      public RegisteredKeySerializer(String id, TrackedSignLookup.KeySerializer<?> serializer) {
         this.id = id;
         this.serializer = serializer;
      }

      public byte[] serialize(TrainCarts plugin, Object uniqueKey) {
         try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            byte[] var12;
            try {
               DataOutputStream dataStream = new DataOutputStream(stream);

               try {
                  dataStream.writeUTF(this.id);
                  this.serializer.write(dataStream, uniqueKey);
               } catch (Throwable var9) {
                  try {
                     dataStream.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               dataStream.close();
               var12 = stream.toByteArray();
            } catch (Throwable var10) {
               try {
                  stream.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }

            stream.close();
            return var12;
         } catch (Throwable var11) {
            plugin.getLogger().log(Level.SEVERE, "Failed to serialize unique sign key " + uniqueKey.getClass().getName(), var11);
            return null;
         }
      }
   }

   protected static final class RealSignKey {
      public final OfflineBlock block;
      public final boolean front;
      private final int hashCode;

      public RealSignKey(OfflineBlock block, boolean front) {
         this.block = block;
         this.front = front;
         this.hashCode = block.hashCode();
      }

      public RailLookup.TrackedSign findRealSign() {
         Block loaded = this.block.getLoadedBlock();
         if (loaded == null) {
            return null;
         } else {
            SignChangeTracker tracker = SignChangeTracker.track(loaded);
            if (tracker.isRemoved()) {
               return null;
            } else {
               RailLookup.TrackedSign sign = RailLookup.TrackedSign.forRealSign(tracker, this.front, RailPiece.NONE);
               sign.rail = null;
               return sign;
            }
         }
      }

      public int hashCode() {
         return this.hashCode;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof TrackedSignLookup.RealSignKey)) {
            return false;
         } else {
            TrackedSignLookup.RealSignKey other = (TrackedSignLookup.RealSignKey)o;
            return this.block.equals(other.block) && this.front == other.front;
         }
      }

      public String toString() {
         return "RealSign{block=" + this.block + " side=" + (this.front ? "front" : "back") + "}";
      }
   }
}
