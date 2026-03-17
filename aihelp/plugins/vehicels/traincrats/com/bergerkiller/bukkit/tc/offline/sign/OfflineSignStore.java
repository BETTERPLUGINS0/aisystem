package com.bergerkiller.bukkit.tc.offline.sign;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.collections.ImplicitlySharedSet;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.offline.OfflineWorldMap;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.mountiplex.reflection.ReflectionUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;

public class OfflineSignStore implements LibraryComponent {
   private final TrainCarts plugin;
   private final ModuleLogger logger;
   private final OfflineWorldMap<OfflineSignStore.OfflineSignWorldStore> byWorld = new OfflineWorldMap();
   private final Map<String, OfflineSignStore.MetadataHandlerEntry<?>> handlers = new HashMap();
   private final Map<Class<?>, OfflineSignStore.MetadataHandlerEntry<?>> handlersByMetadataType = new HashMap();
   private final Map<String, List<OfflineSignStore.OfflineMetadataEntry<Object>>> pendingByMetadataType = new HashMap();
   private final ImplicitlySharedSet<OfflineSignStore.OfflineMetadataEntry<?>> allEntries = new ImplicitlySharedSet(new LinkedHashSet());
   private final OfflineSignLegacyImporter legacyImporter;
   private final OfflineSignStore.BackgroundWriter writer;
   private final OfflineSignStoreListener listener;

   public OfflineSignStore(TrainCarts plugin) {
      this.plugin = plugin;
      this.logger = new ModuleLogger(plugin, new String[]{"OfflineSignStore"});
      this.legacyImporter = new OfflineSignLegacyImporter(this, plugin);
      this.writer = new OfflineSignStore.BackgroundWriter(plugin.getDataFile(new String[]{"SignMetadata.dat"}));
      this.listener = new OfflineSignStoreListener(this);
   }

   public TrainCarts getPlugin() {
      return this.plugin;
   }

   public void load() {
      this.clearAllEntries();
      this.writer.load();
   }

   public void enable() {
      this.legacyImporter.enable();
      this.writer.start();
      this.plugin.register(this.listener);
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(var1.hasNext()) {
         World world = (World)var1.next();
         this.loadSignsOnWorld(world);
      }

   }

   public void disable() {
      Iterator var1 = this.handlers.values().iterator();

      while(var1.hasNext()) {
         OfflineSignStore.MetadataHandlerEntry<?> entry = (OfflineSignStore.MetadataHandlerEntry)var1.next();
         Plugin plugin = CommonUtil.getPluginByClass(entry.metadataType);
         String pluginNamePart = plugin == null ? "" : " [Plugin " + plugin.getName() + "] ";
         this.logger.log(Level.WARNING, "[Developer] " + pluginNamePart + "Sign metadata handler for " + entry.metadataTypeName + " is still registered! Please call unregisterHandler() in onDisable() to fix this warning!");
      }

      var1 = this.allEntries.cloneAsIterable().iterator();

      while(var1.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<?> entry = (OfflineSignStore.OfflineMetadataEntry)var1.next();
         if (!entry.clearHandler()) {
            this.removeEntry(entry);
         }
      }

      this.handlers.clear();
      this.handlersByMetadataType.clear();
      this.writer.stop();
   }

   public <H extends OfflineSignMetadataHandler<T>, T> H registerHandler(Class<T> metadataType, H handler) {
      OfflineSignStore.MetadataHandlerEntry<T> newHandlerEntry = new OfflineSignStore.MetadataHandlerEntry(metadataType, handler);
      OfflineSignStore.MetadataHandlerEntry<?> existing = (OfflineSignStore.MetadataHandlerEntry)this.handlers.put(newHandlerEntry.metadataTypeName, newHandlerEntry);
      if (existing != null) {
         this.handlers.put(newHandlerEntry.metadataTypeName, existing);
         if (existing.handler == handler) {
            return handler;
         } else {
            throw new IllegalStateException("A handler for " + existing.metadataTypeName + " is already registered: " + existing.handler.getClass().getName());
         }
      } else {
         this.handlersByMetadataType.clear();
         this.handlers.values().forEach((h) -> {
            this.handlersByMetadataType.put(h.metadataType, h);
         });
         List<OfflineSignStore.OfflineMetadataEntry<T>> entries = (List)CommonUtil.unsafeCast(this.pendingByMetadataType.remove(newHandlerEntry.metadataTypeName));
         if (entries != null) {
            Iterator var5 = entries.iterator();

            while(var5.hasNext()) {
               OfflineSignStore.OfflineMetadataEntry<T> entry = (OfflineSignStore.OfflineMetadataEntry)var5.next();
               if (!entry.isRemoved()) {
                  this.initHandler(entry, newHandlerEntry);
               }
            }
         }

         return handler;
      }
   }

   public void unregisterHandler(Class<?> metadataType) {
      OfflineSignStore.MetadataHandlerEntry<?> handlerEntry = (OfflineSignStore.MetadataHandlerEntry)this.handlersByMetadataType.get(metadataType);
      if (handlerEntry == null) {
         throw new IllegalArgumentException("Handler for type " + metadataType + " is not registered");
      } else {
         this.unregisterHandlerEntry(handlerEntry);
      }
   }

   public void unregisterHandler(OfflineSignMetadataHandler<?> handler) {
      List<OfflineSignStore.MetadataHandlerEntry<?>> handlerEntries = (List)this.handlers.values().stream().filter((e) -> {
         return e.handler == handler;
      }).collect(Collectors.toList());
      if (handlerEntries.isEmpty()) {
         throw new IllegalArgumentException("Handler is not registered");
      } else {
         handlerEntries.forEach((e) -> {
            this.unregisterHandlerEntry(e);
         });
      }
   }

   private void unregisterHandlerEntry(OfflineSignStore.MetadataHandlerEntry<?> handlerEntry) {
      this.handlers.remove(handlerEntry.metadataTypeName);
      this.handlersByMetadataType.clear();
      this.handlers.values().forEach((h) -> {
         this.handlersByMetadataType.put(h.metadataType, h);
      });
      List<OfflineSignStore.OfflineMetadataEntry<Object>> handlerEntries = (List)this.byWorld.values().stream().flatMap((v) -> {
         return v.values().stream();
      }).filter((e) -> {
         return e.handlerEntry == handlerEntry;
      }).collect(Collectors.toCollection(ArrayList::new));
      if (!handlerEntries.isEmpty()) {
         handlerEntries.forEach((e) -> {
            if (!e.clearHandler()) {
               this.removeEntry(e);
            }

         });
         this.pendingByMetadataType.put(handlerEntry.metadataTypeName, handlerEntries);
      }

   }

   /** @deprecated */
   @Deprecated
   public <T> T computeIfAbsent(Block signBlock, Class<T> metadataType, Function<OfflineSign, ? extends T> factory) {
      return this.computeIfAbsent(signBlock, true, metadataType, factory);
   }

   /** @deprecated */
   @Deprecated
   public <T> T computeIfAbsent(Sign sign, Class<T> metadataType, Function<OfflineSign, ? extends T> factory) {
      return this.computeIfAbsent(sign, true, metadataType, factory);
   }

   /** @deprecated */
   @Deprecated
   public <T> T putIfPresent(OfflineBlock signBlock, T metadata) {
      return this.putIfPresent(signBlock, true, metadata);
   }

   /** @deprecated */
   @Deprecated
   public <T> T put(Block signBlock, T metadata) {
      return this.put(signBlock, true, metadata);
   }

   /** @deprecated */
   @Deprecated
   public <T> T put(Sign sign, T metadata) {
      return this.put(sign, true, metadata);
   }

   /** @deprecated */
   @Deprecated
   public <T> T remove(Block signBlock, Class<T> metadataType) {
      return this.remove(signBlock, true, metadataType);
   }

   /** @deprecated */
   @Deprecated
   public <T> T remove(OfflineBlock signBlock, Class<T> metadataType) {
      return this.remove(signBlock, true, metadataType);
   }

   public <T> T computeIfAbsent(RailLookup.TrackedSign sign, Class<T> metadataType, Function<OfflineSign, ? extends T> factory) {
      if (sign instanceof RailLookup.TrackedRealSign) {
         return this.computeIfAbsent(sign.sign, ((RailLookup.TrackedRealSign)sign).isFrontText(), metadataType, factory);
      } else {
         throw new IllegalArgumentException("Sign is not a real physical sign and cannot store metadata");
      }
   }

   public <T> T computeIfAbsent(Sign sign, boolean frontText, Class<T> metadataType, Function<OfflineSign, ? extends T> factory) {
      return this.computeIfAbsentImpl(OfflineWorld.of(sign.getWorld()), new IntVector3(sign.getX(), sign.getY(), sign.getZ()), frontText, () -> {
         return sign;
      }, metadataType, factory);
   }

   public <T> T computeIfAbsent(Block signBlock, boolean frontText, Class<T> metadataType, Function<OfflineSign, ? extends T> factory) {
      return this.computeIfAbsentImpl(OfflineWorld.of(signBlock.getWorld()), new IntVector3(signBlock), frontText, signFromBlockSupplier(signBlock), metadataType, factory);
   }

   private <T> T computeIfAbsentImpl(OfflineWorld world, IntVector3 position, boolean frontText, Supplier<Sign> signGetter, Class<T> metadataType, Function<OfflineSign, ? extends T> factory) {
      OfflineSignStore.MetadataHandlerEntry<T> handlerEntry = this.findHandlerByType(metadataType);
      OfflineSign sign = null;
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(world);
      List<OfflineSignStore.OfflineMetadataEntry<Object>> entries = atWorld.at(position);
      Iterator var11 = entries.iterator();

      OfflineSignStore.OfflineMetadataEntry newEntry;
      while(var11.hasNext()) {
         newEntry = (OfflineSignStore.OfflineMetadataEntry)var11.next();
         if (newEntry.sign.isFrontText() == frontText) {
            sign = newEntry.sign;
            if (newEntry.handlerEntry == handlerEntry) {
               return newEntry.metadata;
            }
         }
      }

      if (sign == null) {
         sign = OfflineSign.fromSign((Sign)signGetter.get(), frontText);
      }

      T metadata = factory.apply(sign);
      if (metadata != null) {
         newEntry = new OfflineSignStore.OfflineMetadataEntry(sign, handlerEntry, metadata);
         entries.add(newEntry);
         atWorld.atChunk(position.toChunkCoordinates()).add(newEntry);
         this.onEntryAdded(newEntry);
      }

      return metadata;
   }

   public <T> T putIfPresent(OfflineSign sign, T metadata) {
      return this.putIfPresent(sign.getBlock(), sign.isFrontText(), metadata);
   }

   public <T> T putIfPresent(RailLookup.TrackedSign sign, T metadata) {
      return sign instanceof RailLookup.TrackedRealSign ? this.putIfPresent(OfflineBlock.of(sign.signBlock), ((RailLookup.TrackedRealSign)sign).isFrontText(), metadata) : null;
   }

   public <T> T putIfPresent(OfflineBlock signBlock, boolean frontText, T metadata) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(signBlock.getWorld());
      OfflineSignStore.MetadataHandlerEntry<T> handlerEntry = this.findHandler(metadata);
      List<OfflineSignStore.OfflineMetadataEntry<Object>> entries = atWorld.at(signBlock.getPosition());
      Iterator var7 = entries.iterator();

      OfflineSignStore.OfflineMetadataEntry entry;
      do {
         if (!var7.hasNext()) {
            return null;
         }

         entry = (OfflineSignStore.OfflineMetadataEntry)var7.next();
      } while(entry.sign.isFrontText() != frontText || entry.handlerEntry != handlerEntry);

      T oldValue = entry.metadata;
      entry.setMetadata(metadata);
      return oldValue;
   }

   public <T> T put(RailLookup.TrackedSign sign, T metadata) {
      if (sign instanceof RailLookup.TrackedRealSign) {
         return this.put(sign.sign, ((RailLookup.TrackedRealSign)sign).isFrontText(), metadata);
      } else {
         throw new IllegalArgumentException("Sign is not a real physical sign and cannot store metadata");
      }
   }

   public <T> T put(Sign sign, boolean frontText, T metadata) {
      return this.putImpl(OfflineWorld.of(sign.getWorld()), new IntVector3(sign.getX(), sign.getY(), sign.getZ()), frontText, () -> {
         return sign;
      }, metadata);
   }

   public <T> T put(Block signBlock, boolean frontText, T metadata) {
      return this.putImpl(OfflineWorld.of(signBlock.getWorld()), new IntVector3(signBlock), frontText, signFromBlockSupplier(signBlock), metadata);
   }

   private <T> T putImpl(OfflineWorld world, IntVector3 position, boolean frontText, Supplier<Sign> signGetter, T metadata) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(world);
      OfflineSignStore.MetadataHandlerEntry<T> handlerEntry = this.findHandler(metadata);
      OfflineSign offlineSign = null;
      List<OfflineSignStore.OfflineMetadataEntry<Object>> entries = atWorld.at(position);
      Iterator var10 = entries.iterator();

      while(var10.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)var10.next();
         if (entry.sign.isFrontText() == frontText) {
            offlineSign = entry.sign;
            if (entry.handlerEntry == handlerEntry) {
               T oldValue = entry.metadata;
               entry.setMetadata(metadata);
               return oldValue;
            }
         }
      }

      if (offlineSign == null) {
         offlineSign = OfflineSign.fromSign((Sign)signGetter.get(), frontText);
      }

      OfflineSignStore.OfflineMetadataEntry<T> newEntry = new OfflineSignStore.OfflineMetadataEntry(offlineSign, handlerEntry, metadata);
      entries.add(newEntry);
      atWorld.atChunk(position.toChunkCoordinates()).add(newEntry);
      this.onEntryAdded(newEntry);
      return null;
   }

   public <T> Collection<OfflineSignStore.Entry<T>> getAllEntries(Class<T> metadataType) {
      OfflineSignStore.MetadataHandlerEntry<T> handler = this.tryFindHandlerByType(metadataType);
      if (handler == null) {
         return Collections.emptyList();
      } else if (handler.metadataType == metadataType) {
         return Collections.unmodifiableCollection(handler.entries);
      } else {
         ArrayList<OfflineSignStore.Entry<T>> entriesFiltered = new ArrayList(handler.entries.size());
         Iterator var4 = handler.entries.iterator();

         while(var4.hasNext()) {
            OfflineSignStore.Entry<T> entry = (OfflineSignStore.Entry)var4.next();
            if (metadataType.isInstance(entry.getMetadata())) {
               entriesFiltered.add(entry);
            }
         }

         return entriesFiltered;
      }
   }

   public <T> T get(OfflineSign sign, Class<T> metadataType) {
      return this.get(sign.getBlock(), sign.isFrontText(), metadataType);
   }

   public <T> T get(RailLookup.TrackedSign sign, Class<T> metadataType) {
      return sign instanceof RailLookup.TrackedRealSign ? this.get(sign.sign, ((RailLookup.TrackedRealSign)sign).isFrontText(), metadataType) : null;
   }

   public <T> T get(Block signBlock, boolean frontText, Class<T> metadataType) {
      return this.get(OfflineWorld.of(signBlock.getWorld()), new IntVector3(signBlock), frontText, metadataType);
   }

   public <T> T get(Sign sign, boolean frontText, Class<T> metadataType) {
      return this.get(OfflineWorld.of(sign.getWorld()), new IntVector3(sign.getX(), sign.getY(), sign.getZ()), frontText, metadataType);
   }

   public <T> T get(OfflineBlock signBlock, boolean frontText, Class<T> metadataType) {
      return this.get(signBlock.getWorld(), signBlock.getPosition(), frontText, metadataType);
   }

   public <T> T get(OfflineWorld world, IntVector3 position, boolean frontText, Class<T> metadataType) {
      Iterator var5 = this.forWorld(world).at(position).iterator();

      while(var5.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<?> entry = (OfflineSignStore.OfflineMetadataEntry)var5.next();
         if (entry.sign.isFrontText() == frontText) {
            Object metadata = entry.getMetadata();
            if (metadataType.isInstance(metadata)) {
               return metadata;
            }
         }
      }

      return null;
   }

   public void removeAll(Block signBlock) {
      this.removeAll(OfflineBlock.of(signBlock));
   }

   public void removeAll(Block signBlock, boolean frontText) {
      this.removeAll(OfflineBlock.of(signBlock), frontText);
   }

   public void removeAll(OfflineBlock signBlock) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(signBlock.getWorld());

      boolean hasMoreMetadata;
      do {
         Iterator<OfflineSignStore.OfflineMetadataEntry<Object>> iter = atWorld.at(signBlock.getPosition()).iterator();
         if (!iter.hasNext()) {
            return;
         }

         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
         hasMoreMetadata = iter.hasNext();
         iter.remove();
         atWorld.atChunk(signBlock.getPosition().toChunkCoordinates()).remove(entry);
         this.onEntryRemoved(entry);
      } while(hasMoreMetadata);

   }

   public void removeAll(OfflineBlock signBlock, boolean frontText) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(signBlock.getWorld());

      boolean hasMoreMetadata;
      do {
         Iterator iter = atWorld.at(signBlock.getPosition()).iterator();

         OfflineSignStore.OfflineMetadataEntry entry;
         do {
            if (!iter.hasNext()) {
               return;
            }

            entry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
         } while(entry.sign.isFrontText() != frontText);

         hasMoreMetadata = iter.hasNext();
         iter.remove();
         atWorld.atChunk(signBlock.getPosition().toChunkCoordinates()).remove(entry);
         this.onEntryRemoved(entry);
      } while(hasMoreMetadata);

   }

   public <T> T remove(OfflineSign sign, Class<T> metadataType) {
      return this.remove(sign.getBlock(), sign.isFrontText(), metadataType);
   }

   public <T> T remove(RailLookup.TrackedSign sign, Class<T> metadataType) {
      return sign instanceof RailLookup.TrackedRealSign ? this.remove(sign.signBlock, ((RailLookup.TrackedRealSign)sign).isFrontText(), metadataType) : null;
   }

   public <T> T remove(Block signBlock, boolean frontText, Class<T> metadataType) {
      return this.remove(OfflineBlock.of(signBlock), frontText, metadataType);
   }

   public <T> T remove(OfflineBlock signBlock, boolean frontText, Class<T> metadataType) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(signBlock.getWorld());
      Iterator iter = atWorld.at(signBlock.getPosition()).iterator();

      while(iter.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
         if (entry.sign.isFrontText() == frontText) {
            Object metadata = entry.getMetadata();
            if (metadataType.isInstance(metadata)) {
               iter.remove();
               atWorld.atChunk(signBlock.getPosition().toChunkCoordinates()).remove(entry);
               this.onEntryRemoved(entry);
               return metadata;
            }
         }
      }

      return null;
   }

   public void verifySign(Sign sign) {
      IntVector3 position = new IntVector3(sign.getX(), sign.getY(), sign.getZ());
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(sign.getWorld());
      Iterator iter = atWorld.at(position).iterator();

      while(iter.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
         if (!entry.sign.verify(sign)) {
            OfflineSign newSign = OfflineSign.fromSign(sign, entry.sign.isFrontText());
            if (!entry.callOnSignChanged(newSign)) {
               iter.remove();
               atWorld.atChunk(position.toChunkCoordinates()).remove(entry);
               this.onEntryRemoved(entry);
            }
         }
      }

   }

   public <T> T verifySign(Sign sign, boolean frontText, Class<T> metadataType) {
      IntVector3 position = new IntVector3(sign.getX(), sign.getY(), sign.getZ());
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(sign.getWorld());
      Iterator<OfflineSignStore.OfflineMetadataEntry<Object>> iter = atWorld.at(position).iterator();
      Object result = null;

      while(true) {
         while(true) {
            OfflineSignStore.OfflineMetadataEntry entry;
            do {
               if (!iter.hasNext()) {
                  return result;
               }

               entry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
            } while(entry.sign.isFrontText() != frontText);

            if (!entry.sign.verify(sign)) {
               OfflineSign newSign = OfflineSign.fromSign(sign, frontText);
               if (!entry.callOnSignChanged(newSign)) {
                  iter.remove();
                  atWorld.atChunk(position.toChunkCoordinates()).remove(entry);
                  this.onEntryRemoved(entry);
                  continue;
               }
            }

            Object metadata = entry.getMetadata();
            if (metadataType != null && metadataType.isInstance(metadata)) {
               result = metadata;
            }
         }
      }
   }

   private void removeEntry(OfflineSignStore.OfflineMetadataEntry<?> entryToRemove) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(entryToRemove.sign.getWorld());
      Iterator iter = atWorld.at(entryToRemove.sign.getPosition()).iterator();

      while(iter.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
         if (entry == entryToRemove) {
            iter.remove();
            atWorld.atChunk(entry.sign.getPosition().toChunkCoordinates()).remove(entry);
            this.onEntryRemoved(entry);
            break;
         }
      }

   }

   private static Supplier<Sign> signFromBlockSupplier(Block block) {
      return () -> {
         Sign bukkitSign = BlockUtil.getSign(block);
         if (bukkitSign == null) {
            throw new IllegalArgumentException(String.format("Block on world %s at [x=%d y=%d z=%d] is not a sign", block.getWorld().getName(), block.getX(), block.getY(), block.getZ()));
         } else {
            return bukkitSign;
         }
      };
   }

   private OfflineSignStore.OfflineSignWorldStore forWorld(OfflineWorld world) {
      return (OfflineSignStore.OfflineSignWorldStore)this.byWorld.computeIfAbsent(world, OfflineSignStore.OfflineSignWorldStore::new);
   }

   private OfflineSignStore.OfflineSignWorldStore forWorld(World world) {
      return (OfflineSignStore.OfflineSignWorldStore)this.byWorld.computeIfAbsent(world, OfflineSignStore.OfflineSignWorldStore::new);
   }

   private void clearAllEntries() {
      this.byWorld.clear();
      this.allEntries.clear();
      this.pendingByMetadataType.clear();
   }

   private void loadEntry(String metadataTypeName, OfflineSignStore.OfflineMetadataEntry<Object> newEntry) {
      OfflineSignStore.OfflineSignWorldStore forWorld = this.forWorld(newEntry.sign.getWorld());
      forWorld.at(newEntry.sign.getPosition()).add(newEntry);
      forWorld.atChunk(newEntry.sign.getPosition().toChunkCoordinates()).add(newEntry);
      this.allEntries.add((OfflineSignStore.OfflineMetadataEntry)CommonUtil.unsafeCast(newEntry));
      OfflineSignStore.MetadataHandlerEntry<?> handler = (OfflineSignStore.MetadataHandlerEntry)this.handlers.get(metadataTypeName);
      if (handler != null) {
         this.initHandler(newEntry, (OfflineSignStore.MetadataHandlerEntry)CommonUtil.unsafeCast(handler));
      } else {
         List<OfflineSignStore.OfflineMetadataEntry<Object>> pending = (List)this.pendingByMetadataType.computeIfAbsent(metadataTypeName, (k) -> {
            return new ArrayList();
         });
         pending.add(newEntry);
      }

   }

   private <T> void initHandler(OfflineSignStore.OfflineMetadataEntry<T> entry, OfflineSignStore.MetadataHandlerEntry<T> handler) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(entry.sign.getWorld());
      List<OfflineSignStore.OfflineMetadataEntry<Object>> entriesAtBlock = atWorld.at(entry.sign.getPosition());
      boolean hasDuplicateEntry = false;
      Iterator iter = entriesAtBlock.iterator();

      while(iter.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> existingEntry = (OfflineSignStore.OfflineMetadataEntry)iter.next();
         if (existingEntry != entry && existingEntry.sign.isFrontText() == entry.sign.isFrontText() && existingEntry.handlerEntry == handler) {
            hasDuplicateEntry = true;
            break;
         }
      }

      if (hasDuplicateEntry || !entry.setHandler(handler)) {
         iter = entriesAtBlock.iterator();

         while(iter.hasNext()) {
            if (iter.next() == entry) {
               iter.remove();
               atWorld.atChunk(entry.sign.getPosition().toChunkCoordinates()).remove(entry);
               break;
            }
         }

         this.onEntryRemoved(entry);
      }

   }

   protected void unloadSignsOnWorld(World world) {
      Iterator var2 = (new ArrayList(this.forWorld(world).values())).iterator();

      while(var2.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)var2.next();
         if (entry.handlerEntry != null && entry.handlerEntry.handler.isUnloadedWorldsIgnored() && !entry.unload()) {
            this.removeEntry(entry);
         }
      }

   }

   protected void loadSignsOnWorld(World world) {
      Iterator var2 = (new ArrayList(this.forWorld(world).values())).iterator();

      while(var2.hasNext()) {
         OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)var2.next();
         if (!entry.addedToHandler && entry.handlerEntry != null) {
            if (entry.decodeMetadata()) {
               entry.callOnLoaded();
            } else {
               this.removeEntry(entry);
            }
         }
      }

      Chunk[] var6 = world.getLoadedChunks();
      int var7 = var6.length;

      for(int var4 = 0; var4 < var7; ++var4) {
         Chunk chunk = var6[var4];
         this.verifySignsInChunk(chunk);
      }

   }

   protected void verifySignsInChunk(Chunk chunk) {
      OfflineSignStore.OfflineSignWorldStore atWorld = this.forWorld(OfflineWorld.of(chunk.getWorld()));
      Iterator<OfflineSignStore.OfflineMetadataEntry<Object>> entriesAtChunk = atWorld.atChunk(new IntVector2(chunk)).iterator();
      if (entriesAtChunk.hasNext()) {
         HashMap signsByBlock;
         try {
            Collection<BlockState> blockStates = WorldUtil.getBlockStates(chunk);
            signsByBlock = new HashMap(blockStates.size());
            Iterator var6 = blockStates.iterator();

            while(var6.hasNext()) {
               BlockState state = (BlockState)var6.next();
               if (state instanceof Sign) {
                  Sign s = (Sign)state;
                  signsByBlock.put(new IntVector3(s.getX(), s.getY(), s.getZ()), s);
               }
            }
         } catch (Throwable var9) {
            this.logger.log(Level.SEVERE, String.format("Failed to read BlockStates in chunk {world=%s, x=%d, z=%d}, verify failed", chunk.getWorld().getName(), chunk.getX(), chunk.getZ()), var9);
            return;
         }

         do {
            OfflineSignStore.OfflineMetadataEntry<Object> entry = (OfflineSignStore.OfflineMetadataEntry)entriesAtChunk.next();
            Sign sign = (Sign)signsByBlock.get(entry.sign.getPosition());
            if (sign != null) {
               if (entry.sign.verify(sign)) {
                  continue;
               }

               OfflineSign newSign = OfflineSign.fromSign(sign, entry.sign.isFrontText());
               if (entry.callOnSignChanged(newSign)) {
                  continue;
               }
            }

            entriesAtChunk.remove();
            atWorld.at(entry.sign.getPosition()).remove(entry);
            this.onEntryRemoved(entry);
         } while(entriesAtChunk.hasNext());

      }
   }

   private void onEntryAdded(OfflineSignStore.OfflineMetadataEntry<?> entry) {
      this.allEntries.add((OfflineSignStore.OfflineMetadataEntry)CommonUtil.unsafeCast(entry));
      entry.handlerEntry.entries.add((OfflineSignStore.OfflineMetadataEntry)CommonUtil.unsafeCast(entry));
      this.writer.changed();
      entry.callOnAdded();
   }

   private void onEntryRemoved(OfflineSignStore.OfflineMetadataEntry<?> entry) {
      if (this.allEntries.remove(entry)) {
         this.writer.changed();
      }

      entry.removed = true;
      entry.callOnRemoved();
   }

   private <T> OfflineSignStore.MetadataHandlerEntry<T> findHandler(T metadataValue) {
      if (metadataValue == null) {
         throw new IllegalArgumentException("Metadata value type is null");
      } else {
         return this.findHandlerByType(metadataValue.getClass());
      }
   }

   private <T> OfflineSignStore.MetadataHandlerEntry<T> findHandlerByType(Class<?> metadataType) {
      OfflineSignStore.MetadataHandlerEntry<T> handler = this.tryFindHandlerByType(metadataType);
      if (handler != null) {
         return handler;
      } else {
         throw new IllegalArgumentException("No handler is registered for metadata type " + metadataType);
      }
   }

   private <T> OfflineSignStore.MetadataHandlerEntry<T> tryFindHandlerByType(Class<?> metadataType) {
      OfflineSignStore.MetadataHandlerEntry<?> handler = (OfflineSignStore.MetadataHandlerEntry)this.handlersByMetadataType.get(metadataType);
      if (handler != null) {
         return (OfflineSignStore.MetadataHandlerEntry)CommonUtil.unsafeCast(handler);
      } else {
         Iterator var3 = ((List)ReflectionUtil.getAllClassesAndInterfaces(metadataType).collect(Collectors.toList())).iterator();

         Class superType;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            superType = (Class)var3.next();
         } while((handler = (OfflineSignStore.MetadataHandlerEntry)this.handlersByMetadataType.get(superType)) == null);

         this.handlersByMetadataType.put(metadataType, handler);
         return (OfflineSignStore.MetadataHandlerEntry)CommonUtil.unsafeCast(handler);
      }
   }

   private static void atomicMove(File fromFile, File toFile) throws Throwable {
      try {
         Files.move(fromFile.toPath(), toFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (UnsupportedOperationException | AtomicMoveNotSupportedException var3) {
         if (!fromFile.exists()) {
            throw new IOException("File " + fromFile + " does not exist");
         } else if (!toFile.delete() || !fromFile.renameTo(toFile)) {
            if (StreamUtil.tryCopyFile(fromFile, toFile)) {
               fromFile.delete();
            } else {
               throw new IOException("Atomic move from " + fromFile + " to " + toFile + " failed");
            }
         }
      }
   }

   private final class OfflineMetadataEntry<T> implements OfflineSignStore.Entry<T> {
      public OfflineSign sign;
      private OfflineSignStore.MetadataHandlerEntry<T> handlerEntry;
      private byte[] encodedData;
      private T metadata;
      private boolean removed;
      private boolean addedToHandler;

      public OfflineMetadataEntry(OfflineSign sign, OfflineSignStore.MetadataHandlerEntry<T> handlerEntry, T metadata) {
         this.sign = sign;
         this.handlerEntry = handlerEntry;
         this.encodedData = null;
         this.metadata = metadata;
         this.removed = false;
         this.addedToHandler = false;
      }

      public OfflineMetadataEntry(OfflineSign sign, byte[] encodedData) {
         this.sign = sign;
         this.handlerEntry = null;
         this.encodedData = encodedData;
         this.metadata = null;
         this.removed = false;
         this.addedToHandler = false;
      }

      public OfflineSign getSign() {
         return this.sign;
      }

      public T getMetadata() {
         return this.metadata;
      }

      public boolean isRemoved() {
         return this.removed;
      }

      public boolean clearHandler() {
         if (this.handlerEntry == null) {
            return true;
         } else if (this.encodeMetadata() == null) {
            return false;
         } else {
            this.handlerEntry.entries.remove(this);
            this.callOnUnloaded();
            this.handlerEntry = null;
            return true;
         }
      }

      public boolean setHandler(OfflineSignStore.MetadataHandlerEntry<T> handlerEntry) {
         if (this.handlerEntry == handlerEntry) {
            return true;
         } else if (this.handlerEntry != null) {
            OfflineSignStore.this.logger.log(Level.SEVERE, "Attempted to register handler " + handlerEntry.handler.getClass().getName() + " for sign " + this.sign + " but another handler was already registered");
            OfflineSignStore.this.logger.log(Level.SEVERE, "Handler currently registered: " + this.handlerEntry.handler.getClass().getName());
            return false;
         } else if (this.encodedData == null) {
            OfflineSignStore.this.logger.log(Level.SEVERE, "Attempted to decode metadata for sign " + this.sign + " but no encoded data is available to decode");
            return false;
         } else {
            this.handlerEntry = handlerEntry;
            this.handlerEntry.entries.add(this);
            if (!this.sign.getWorld().isLoaded() && handlerEntry.handler.isUnloadedWorldsIgnored()) {
               return true;
            } else if (!this.decodeMetadata()) {
               return false;
            } else {
               this.callOnLoaded();
               return true;
            }
         }
      }

      public boolean unload() {
         if (this.encodeMetadata() == null) {
            return false;
         } else {
            this.callOnUnloaded();
            this.metadata = null;
            return true;
         }
      }

      public boolean decodeMetadata() {
         try {
            ByteArrayInputStream b_stream = new ByteArrayInputStream(this.encodedData);

            boolean var7;
            label92: {
               try {
                  InflaterInputStream d_stream;
                  label97: {
                     d_stream = new InflaterInputStream(b_stream);

                     try {
                        label98: {
                           DataInputStream stream = new DataInputStream(d_stream);

                           label84: {
                              try {
                                 label99: {
                                    OfflineSign.readFrom(stream);
                                    stream.readUTF();
                                    int metadataVersion = Util.readVariableLengthInt(stream);
                                    if (metadataVersion == this.handlerEntry.handler.getMetadataVersion()) {
                                       this.metadata = this.handlerEntry.handler.onDecode(stream, this.sign);
                                       if (this.metadata == null) {
                                          throw new IllegalStateException("Decoded metadata is null");
                                       }
                                       break label84;
                                    }

                                    OfflineSignMetadataHandler.DataMigrationDecoder decoder;
                                    try {
                                       decoder = this.handlerEntry.handler.getMigrationDecoder(this.sign, metadataVersion);
                                       if (decoder == null) {
                                          throw new UnsupportedOperationException("Not supported");
                                       }
                                    } catch (UnsupportedOperationException var11) {
                                       OfflineSignStore.this.logger.log(Level.WARNING, "Failed to decode metadata for sign " + this.sign + ": Unsupported data version (type=" + this.handlerEntry.metadataTypeName + ")");
                                       var7 = false;
                                       break label99;
                                    }

                                    this.metadata = decoder.onDecode(stream, this.sign, metadataVersion);
                                    if (this.metadata == null) {
                                       throw new IllegalStateException("Failed to migrate metadata: decoded metadata is null");
                                    }
                                    break label84;
                                 }
                              } catch (Throwable var12) {
                                 try {
                                    stream.close();
                                 } catch (Throwable var10) {
                                    var12.addSuppressed(var10);
                                 }

                                 throw var12;
                              }

                              stream.close();
                              break label98;
                           }

                           stream.close();
                           break label97;
                        }
                     } catch (Throwable var13) {
                        try {
                           d_stream.close();
                        } catch (Throwable var9) {
                           var13.addSuppressed(var9);
                        }

                        throw var13;
                     }

                     d_stream.close();
                     break label92;
                  }

                  d_stream.close();
               } catch (Throwable var14) {
                  try {
                     b_stream.close();
                  } catch (Throwable var8) {
                     var14.addSuppressed(var8);
                  }

                  throw var14;
               }

               b_stream.close();
               return true;
            }

            b_stream.close();
            return var7;
         } catch (OfflineSignMetadataHandler.InvalidMetadataException var15) {
            return false;
         } catch (Throwable var16) {
            OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to decode metadata for sign " + this.sign, var16);
            return false;
         }
      }

      public byte[] encodeMetadata() {
         byte[] encodedData = this.encodedData;
         if (encodedData == null) {
            synchronized(this) {
               encodedData = this.encodedData;
               if (encodedData == null) {
                  try {
                     ByteArrayOutputStream b_stream = new ByteArrayOutputStream();

                     try {
                        DeflaterOutputStream d_stream = new DeflaterOutputStream(b_stream);

                        try {
                           DataOutputStream stream = new DataOutputStream(d_stream);

                           try {
                              OfflineSign.writeTo(stream, this.sign);
                              stream.writeUTF(this.handlerEntry.metadataTypeName);
                              Util.writeVariableLengthInt(stream, this.handlerEntry.handler.getMetadataVersion());
                              this.handlerEntry.handler.onEncode(stream, this.sign, this.metadata);
                           } catch (Throwable var12) {
                              try {
                                 stream.close();
                              } catch (Throwable var11) {
                                 var12.addSuppressed(var11);
                              }

                              throw var12;
                           }

                           stream.close();
                        } catch (Throwable var13) {
                           try {
                              d_stream.close();
                           } catch (Throwable var10) {
                              var13.addSuppressed(var10);
                           }

                           throw var13;
                        }

                        d_stream.close();
                        this.encodedData = encodedData = b_stream.toByteArray();
                     } catch (Throwable var14) {
                        try {
                           b_stream.close();
                        } catch (Throwable var9) {
                           var14.addSuppressed(var9);
                        }

                        throw var14;
                     }

                     b_stream.close();
                  } catch (OfflineSignMetadataHandler.InvalidMetadataException var15) {
                     return null;
                  } catch (Throwable var16) {
                     OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to encode metadata for sign " + this.sign, var16);
                     return null;
                  }
               }
            }
         }

         return encodedData;
      }

      public boolean callOnSignChanged(OfflineSign newSign) {
         T oldMetadata = this.metadata;
         if (this.handlerEntry != null && oldMetadata != null) {
            Object newMetadata;
            try {
               newMetadata = this.handlerEntry.handler.onSignChanged(OfflineSignStore.this, this.sign, newSign, oldMetadata);
               if (newMetadata == null) {
                  return false;
               }
            } catch (Throwable var5) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to handle onSignChanged for sign " + newSign, var5);
               return false;
            }

            this.sign = newSign;
            this.setMetadataFireEvent(newMetadata);
            return true;
         } else {
            return false;
         }
      }

      public void callOnAdded() {
         if (!this.addedToHandler && this.handlerEntry != null) {
            try {
               this.handlerEntry.handler.onAdded(OfflineSignStore.this, this.sign, this.metadata);
            } catch (Throwable var2) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to handle onAdded for sign " + this.sign, var2);
            }

            this.addedToHandler = true;
         }

      }

      public void callOnRemoved() {
         if (this.addedToHandler && this.handlerEntry != null && this.metadata != null) {
            try {
               this.handlerEntry.handler.onRemoved(OfflineSignStore.this, this.sign, this.metadata);
            } catch (Throwable var2) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to handle onRemoved for sign " + this.sign, var2);
            }

            this.addedToHandler = false;
         }

      }

      public void callOnLoaded() {
         if (!this.addedToHandler && this.handlerEntry != null) {
            try {
               this.handlerEntry.handler.onLoaded(OfflineSignStore.this, this.sign, this.metadata);
            } catch (Throwable var2) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to handle onLoaded for sign " + this.sign, var2);
            }

            this.addedToHandler = true;
         }

      }

      public void callOnUnloaded() {
         if (this.addedToHandler && this.handlerEntry != null && this.metadata != null) {
            try {
               this.handlerEntry.handler.onUnloaded(OfflineSignStore.this, this.sign, this.metadata);
            } catch (Throwable var2) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to handle onUnloaded for sign " + this.sign, var2);
            }

            this.addedToHandler = false;
         }

      }

      public void setMetadata(T metadata) {
         if (metadata == null) {
            throw new IllegalArgumentException("New metadata is null");
         } else if (!metadata.equals(this.metadata)) {
            this.setMetadataFireEvent(metadata);
         }
      }

      private void setMetadataFireEvent(T metadata) {
         T oldMetadata = this.metadata;
         synchronized(this) {
            this.metadata = metadata;
            this.encodedData = null;
         }

         if (this.handlerEntry != null) {
            try {
               this.handlerEntry.handler.onUpdated(OfflineSignStore.this, this.sign, oldMetadata, metadata);
            } catch (Throwable var5) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to handle onUpdated for sign " + this.sign, var5);
            }
         }

         OfflineSignStore.this.writer.changed();
      }

      public void remove() {
         OfflineSignStore.this.removeEntry(this);
      }
   }

   private class BackgroundWriter {
      private Thread thread;
      private final Object lock = new Object();
      private final File saveFile;
      private volatile boolean savingNeeded = false;
      private volatile boolean shuttingDown = false;

      public BackgroundWriter(File saveFile) {
         this.saveFile = saveFile;
      }

      public void changed() {
         synchronized(this.lock) {
            this.savingNeeded = true;
            this.lock.notifyAll();
         }
      }

      public void start() {
         this.shuttingDown = false;
         if (this.thread == null) {
            this.thread = new Thread(this::runWorker, "TrainCarts:SignMetadataWriterThread");
            this.thread.setDaemon(true);
            this.thread.start();
         }

      }

      public void stop() {
         synchronized(this.lock) {
            this.shuttingDown = true;
            this.lock.notifyAll();
         }

         if (this.thread != null) {
            try {
               this.thread.join(10000L);
               if (this.thread.isAlive()) {
                  OfflineSignStore.this.logger.log(Level.WARNING, "Saving sign metadata is taking longer than 10s");
                  this.thread.join();
               }
            } catch (InterruptedException var3) {
            }

            this.thread = null;
         }

      }

      private void runWorker() {
         long MIN_SAVE_INTERVAL = 5000L;
         long lastSaveTS = System.currentTimeMillis() - 5000L;

         do {
            boolean doSave = false;
            synchronized(this.lock) {
               try {
                  while(!this.savingNeeded && !this.shuttingDown) {
                     this.lock.wait();
                  }

                  while(!this.shuttingDown) {
                     long remaining = lastSaveTS + 5000L - System.currentTimeMillis();
                     if (remaining <= 0L) {
                        break;
                     }

                     this.lock.wait(remaining);
                  }
               } catch (InterruptedException var10) {
               }

               doSave = this.savingNeeded;
               this.savingNeeded = false;
            }

            if (doSave) {
               lastSaveTS = System.currentTimeMillis();
               this.save();
            }
         } while(!this.shuttingDown);

      }

      public void load() {
         if (this.saveFile.exists()) {
            try {
               FileInputStream f_stream = new FileInputStream(this.saveFile);

               try {
                  DataInputStream stream = new DataInputStream(f_stream);

                  try {
                     this.load(stream);
                  } catch (Throwable var7) {
                     try {
                        stream.close();
                     } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                     }

                     throw var7;
                  }

                  stream.close();
               } catch (Throwable var8) {
                  try {
                     f_stream.close();
                  } catch (Throwable var5) {
                     var8.addSuppressed(var5);
                  }

                  throw var8;
               }

               f_stream.close();
            } catch (EOFException var9) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Reached unexpected end-of-file while reading sign metadata (corrupted file?)");
            } catch (IOException var10) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to read sign metadata", var10);
            }
         }

      }

      private void load(DataInputStream stream) throws IOException {
         int versionCode = Util.readVariableLengthInt(stream);
         if (versionCode == 1) {
            OfflineSignStore.this.logger.log(Level.WARNING, "Upgrading offline sign metadata format from V1 to V2");
            DataInputStream upgraded = OfflineSignStoreUpgradeV1ToV2.upgrade(stream);

            try {
               this.load(upgraded);
            } catch (Throwable var18) {
               if (upgraded != null) {
                  try {
                     upgraded.close();
                  } catch (Throwable var12) {
                     var18.addSuppressed(var12);
                  }
               }

               throw var18;
            }

            if (upgraded != null) {
               upgraded.close();
            }

         } else if (versionCode != 2) {
            OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to read sign metadata: unsupported version " + versionCode);
         } else {
            while(stream.available() > 0) {
               byte[] encodedData = Util.readByteArray(stream);
               ByteArrayInputStream m_b_stream = new ByteArrayInputStream(encodedData);

               OfflineSign sign;
               String metadataTypeName;
               try {
                  InflaterInputStream m_d_stream = new InflaterInputStream(m_b_stream);

                  try {
                     DataInputStream m_stream = new DataInputStream(m_d_stream);

                     try {
                        sign = OfflineSign.readFrom(m_stream);
                        metadataTypeName = m_stream.readUTF();
                     } catch (Throwable var15) {
                        try {
                           m_stream.close();
                        } catch (Throwable var14) {
                           var15.addSuppressed(var14);
                        }

                        throw var15;
                     }

                     m_stream.close();
                  } catch (Throwable var16) {
                     try {
                        m_d_stream.close();
                     } catch (Throwable var13) {
                        var16.addSuppressed(var13);
                     }

                     throw var16;
                  }

                  m_d_stream.close();
               } catch (Throwable var17) {
                  try {
                     m_b_stream.close();
                  } catch (Throwable var11) {
                     var17.addSuppressed(var11);
                  }

                  throw var17;
               }

               m_b_stream.close();
               OfflineSignStore.OfflineMetadataEntry newEntry = OfflineSignStore.this.new OfflineMetadataEntry(sign, encodedData);
               OfflineSignStore.this.loadEntry(metadataTypeName, newEntry);
            }

         }
      }

      public void save() {
         List<OfflineSignStore.OfflineMetadataEntry<?>> encodeFailures = new ArrayList();
         File tmpFile = new File(this.saveFile.getParentFile(), this.saveFile.getName() + "." + System.currentTimeMillis() + ".tmp");
         boolean saveSuccessful = false;

         try {
            FileOutputStream f_stream = new FileOutputStream(tmpFile);

            try {
               DataOutputStream stream = new DataOutputStream(f_stream);

               try {
                  Util.writeVariableLengthInt(stream, 2);
                  Iterator var6 = OfflineSignStore.this.allEntries.cloneAsIterable().iterator();

                  while(var6.hasNext()) {
                     OfflineSignStore.OfflineMetadataEntry<?> entry = (OfflineSignStore.OfflineMetadataEntry)var6.next();
                     byte[] encodedData = entry.encodeMetadata();
                     if (encodedData != null) {
                        Util.writeByteArray(stream, encodedData);
                     } else {
                        encodeFailures.add(entry);
                     }
                  }
               } catch (Throwable var12) {
                  try {
                     stream.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }

                  throw var12;
               }

               stream.close();
            } catch (Throwable var13) {
               try {
                  f_stream.close();
               } catch (Throwable var10) {
                  var13.addSuppressed(var10);
               }

               throw var13;
            }

            f_stream.close();
            saveSuccessful = true;
         } catch (IOException var14) {
            OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to write sign metadata", var14);
         }

         if (saveSuccessful) {
            try {
               OfflineSignStore.atomicMove(tmpFile, this.saveFile);
            } catch (Throwable var9) {
               OfflineSignStore.this.logger.log(Level.SEVERE, "Failed to finalize writing sign metadata", var9);
            }
         }

         if (!encodeFailures.isEmpty()) {
            CommonUtil.getPluginExecutor(OfflineSignStore.this.plugin).execute(() -> {
               encodeFailures.forEach((x$0) -> {
                  OfflineSignStore.this.removeEntry(x$0);
               });
            });
         }

      }
   }

   private static class MetadataHandlerEntry<T> {
      public final Class<T> metadataType;
      public final String metadataTypeName;
      public final OfflineSignMetadataHandler<T> handler;
      public final Set<OfflineSignStore.OfflineMetadataEntry<T>> entries;

      public MetadataHandlerEntry(Class<T> metadataType, OfflineSignMetadataHandler<T> handler) {
         this.metadataType = metadataType;
         this.metadataTypeName = metadataType.getName();
         this.handler = handler;
         this.entries = new LinkedHashSet();
      }
   }

   private static final class OfflineSignWorldStore {
      private final OfflineWorld world;
      private final ListMultimap<IntVector3, OfflineSignStore.OfflineMetadataEntry<Object>> byBlockCoordinates;
      private final ListMultimap<IntVector2, OfflineSignStore.OfflineMetadataEntry<Object>> byChunkCoordinates;

      public OfflineSignWorldStore(World world) {
         this(OfflineWorld.of(world));
      }

      public OfflineSignWorldStore(OfflineWorld world) {
         this.world = world;
         this.byBlockCoordinates = ArrayListMultimap.create(1000, 1);
         this.byChunkCoordinates = ArrayListMultimap.create(500, 1);
      }

      public Collection<OfflineSignStore.OfflineMetadataEntry<Object>> values() {
         return this.byBlockCoordinates.values();
      }

      public List<OfflineSignStore.OfflineMetadataEntry<Object>> at(IntVector3 coordinate) {
         return this.byBlockCoordinates.get(coordinate);
      }

      public List<OfflineSignStore.OfflineMetadataEntry<Object>> atChunk(IntVector2 chunkCoordinates) {
         return this.byChunkCoordinates.get(chunkCoordinates);
      }
   }

   public interface Entry<T> {
      OfflineSign getSign();

      boolean isRemoved();

      T getMetadata();

      void setMetadata(T var1);

      void remove();
   }
}
