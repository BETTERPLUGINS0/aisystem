package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialogs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow.CowVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow.CowVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.FrogVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.FrogVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig.PigVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig.PigVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instruments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.IJukeboxSong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSongs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPatterns;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timelines;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biome;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting.PaintingVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting.PaintingVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class SynchronizedRegistriesHandler {
   private static final boolean FORCE_PER_USER_REGISTRIES = Boolean.getBoolean("packetevents.force-per-user-registries");
   private static final Map<ResourceLocation, SynchronizedRegistriesHandler.RegistryEntry<?>> REGISTRY_KEYS = new HashMap();

   private SynchronizedRegistriesHandler() {
   }

   public static void init() {
      if (REGISTRY_KEYS.isEmpty()) {
         throw new AssertionError();
      }
   }

   @Nullable
   public static SynchronizedRegistriesHandler.RegistryEntry<?> getRegistryEntry(ResourceLocation registryKey) {
      return (SynchronizedRegistriesHandler.RegistryEntry)REGISTRY_KEYS.get(registryKey);
   }

   public static void handleRegistry(User user, PacketWrapper<?> wrapper, ResourceLocation registryName, List<WrapperConfigServerRegistryData.RegistryElement> elements) {
      Object cacheKey = PacketEvents.getAPI().getServerManager().getRegistryCacheKey(user, wrapper.getServerVersion().toClientVersion());
      handleRegistry(user, wrapper, registryName, elements, cacheKey);
   }

   public static void handleRegistry(User user, PacketWrapper<?> wrapper, ResourceLocation registryName, List<WrapperConfigServerRegistryData.RegistryElement> elements, @Nullable Object cacheKey) {
      SynchronizedRegistriesHandler.RegistryEntry<?> registryData = (SynchronizedRegistriesHandler.RegistryEntry)REGISTRY_KEYS.get(registryName);
      if (registryData != null) {
         SimpleRegistry syncedRegistry;
         if (!FORCE_PER_USER_REGISTRIES && cacheKey != null) {
            syncedRegistry = registryData.computeSyncedRegistry(cacheKey, () -> {
               return registryData.createFromElements(elements, wrapper);
            });
         } else {
            syncedRegistry = registryData.createFromElements(elements, wrapper);
         }

         user.putRegistry(syncedRegistry);
      }
   }

   public static void handleLegacyRegistries(User user, PacketWrapper<?> wrapper, NBTCompound registryData) {
      Object cacheKey = PacketEvents.getAPI().getServerManager().getRegistryCacheKey(user, wrapper.getServerVersion().toClientVersion());
      Iterator var4 = registryData.getTags().values().iterator();

      while(var4.hasNext()) {
         NBT tag = (NBT)var4.next();
         if (tag instanceof NBTList) {
            NBTList<NBTCompound> list = (NBTList)tag;
            handleRegistry(user, wrapper, DimensionTypes.getRegistry().getRegistryKey(), WrapperConfigServerRegistryData.RegistryElement.convertNbt(list), cacheKey);
         } else {
            NBTCompound compound = (NBTCompound)tag;
            ResourceLocation registryName = new ResourceLocation(compound.getStringTagValueOrThrow("type"));
            NBTList<NBTCompound> nbtElements = compound.getCompoundListTagOrNull("value");
            if (nbtElements != null) {
               handleRegistry(user, wrapper, registryName, WrapperConfigServerRegistryData.RegistryElement.convertNbt(nbtElements), cacheKey);
            }
         }
      }

   }

   static {
      Stream.of(new SynchronizedRegistriesHandler.RegistryEntry(Biomes.getRegistry(), Biome.CODEC), new SynchronizedRegistriesHandler.RegistryEntry(ChatTypes.getRegistry(), ChatType::decode), new SynchronizedRegistriesHandler.RegistryEntry(TrimPatterns.getRegistry(), TrimPattern::decode), new SynchronizedRegistriesHandler.RegistryEntry(TrimMaterials.getRegistry(), TrimMaterial::decode), new SynchronizedRegistriesHandler.RegistryEntry(WolfVariants.getRegistry(), WolfVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(WolfSoundVariants.getRegistry(), WolfSoundVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(PigVariants.getRegistry(), PigVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(FrogVariants.getRegistry(), FrogVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(CatVariants.getRegistry(), CatVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(CowVariants.getRegistry(), CowVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(ChickenVariants.getRegistry(), ChickenVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(ZombieNautilusVariants.getRegistry(), ZombieNautilusVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(PaintingVariants.getRegistry(), PaintingVariant::decode), new SynchronizedRegistriesHandler.RegistryEntry(DimensionTypes.getRegistry(), DimensionType.CODEC), new SynchronizedRegistriesHandler.RegistryEntry(DamageTypes.getRegistry(), DamageType::decode), new SynchronizedRegistriesHandler.RegistryEntry(BannerPatterns.getRegistry(), BannerPattern::decode), new SynchronizedRegistriesHandler.RegistryEntry(EnchantmentTypes.getRegistry(), EnchantmentType::decode), new SynchronizedRegistriesHandler.RegistryEntry(JukeboxSongs.getRegistry(), IJukeboxSong::decode), new SynchronizedRegistriesHandler.RegistryEntry(Instruments.getRegistry(), Instrument::decode), new SynchronizedRegistriesHandler.RegistryEntry(Dialogs.getRegistry(), Dialog::decodeDirect), new SynchronizedRegistriesHandler.RegistryEntry(Timelines.getRegistry(), Timeline.CODEC)).forEach((entry) -> {
         REGISTRY_KEYS.put(entry.getRegistryKey(), entry);
      });
   }

   @ApiStatus.Internal
   public static final class RegistryEntry<T extends MappedEntity & CopyableEntity<T> & DeepComparableEntity> {
      private final IRegistry<T> baseRegistry;
      private final SynchronizedRegistriesHandler.NbtEntryDecoder<T> decoder;
      private final Map<Object, SimpleRegistry<T>> syncedRegistries;

      public RegistryEntry(IRegistry<T> baseRegistry, SynchronizedRegistriesHandler.LegacyNbtEntryDecoder<T> decoder) {
         this(baseRegistry, decoder.upgrade());
      }

      public RegistryEntry(IRegistry<T> baseRegistry, NbtDecoder<T> decoder) {
         this(baseRegistry, SynchronizedRegistriesHandler.NbtEntryDecoder.fromDecoder(decoder));
      }

      public RegistryEntry(IRegistry<T> baseRegistry, SynchronizedRegistriesHandler.NbtEntryDecoder<T> decoder) {
         this.syncedRegistries = new ConcurrentHashMap(2);
         this.baseRegistry = baseRegistry;
         this.decoder = decoder;
      }

      @Nullable
      public SimpleRegistry<T> getSyncedRegistry(Object key) {
         return (SimpleRegistry)this.syncedRegistries.get(key);
      }

      public SimpleRegistry<T> computeSyncedRegistry(Object key, Supplier<SimpleRegistry<?>> registry) {
         return (SimpleRegistry)this.syncedRegistries.computeIfAbsent(key, ($) -> {
            return (SimpleRegistry)registry.get();
         });
      }

      private void handleElement(SimpleRegistry<T> registry, WrapperConfigServerRegistryData.RegistryElement element, int id, PacketWrapper<?> wrapper) {
         ResourceLocation elementName = element.getId();
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         T baseEntry = this.baseRegistry.getByName(version, elementName);
         TypesBuilderData data = new SimpleTypesBuilderData(elementName, id);
         T copiedBaseEntry = baseEntry == null ? null : ((CopyableEntity)baseEntry).copy(data);
         if (element.getData() != null) {
            T value = (MappedEntity)this.decoder.decode(element.getData(), wrapper, data);
            if (!((DeepComparableEntity)value).deepEquals(copiedBaseEntry)) {
               registry.define(elementName, id, value);
               return;
            }
         }

         if (copiedBaseEntry != null) {
            registry.define(elementName, id, copiedBaseEntry);
         } else {
            PacketEvents.getAPI().getLogger().warning("Unknown registry entry " + elementName + " for " + this.getRegistryKey());
         }
      }

      public SimpleRegistry<T> createFromElements(List<WrapperConfigServerRegistryData.RegistryElement> elements, PacketWrapper<?> wrapper) {
         SimpleRegistry<T> registry = new SimpleRegistry(this.getRegistryKey());

         for(int id = 0; id < elements.size(); ++id) {
            WrapperConfigServerRegistryData.RegistryElement element = (WrapperConfigServerRegistryData.RegistryElement)elements.get(id);
            this.handleElement(registry, element, id, wrapper);
         }

         return registry;
      }

      public ResourceLocation getRegistryKey() {
         return this.baseRegistry.getRegistryKey();
      }
   }

   @FunctionalInterface
   @ApiStatus.Internal
   public interface LegacyNbtEntryDecoder<T> {
      T decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data);

      default SynchronizedRegistriesHandler.NbtEntryDecoder<T> upgrade() {
         return (nbt, wrapper, data) -> {
            return this.decode(nbt, wrapper.getServerVersion().toClientVersion(), data);
         };
      }
   }

   @FunctionalInterface
   @ApiStatus.Internal
   public interface NbtEntryDecoder<T> {
      static <T extends MappedEntity & CopyableEntity<T>> SynchronizedRegistriesHandler.NbtEntryDecoder<T> fromDecoder(NbtDecoder<T> decoder) {
         return (tag, wrapper, data) -> {
            return ((CopyableEntity)((MappedEntity)decoder.decode(tag, wrapper))).copy(data);
         };
      }

      T decode(NBT tag, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data);
   }
}
