package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.VanillaBiomeProperties;
import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.registry.master.ConfigRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwfulBukkitHacks {
   private static final Logger LOGGER = LoggerFactory.getLogger(AwfulBukkitHacks.class);
   private static final Map<ResourceLocation, List<ResourceLocation>> terraBiomeMap = new HashMap();

   public static void registerBiomes(ConfigRegistry configRegistry) {
      try {
         LOGGER.info("Hacking biome registry...");
         MappedRegistry<Biome> biomeRegistry = (MappedRegistry)RegistryFetcher.biomeRegistry();
         Reflection.MAPPED_REGISTRY.setFrozen(biomeRegistry, false);
         configRegistry.forEach((pack) -> {
            pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
               try {
                  BukkitPlatformBiome platformBiome = (BukkitPlatformBiome)biome.getPlatformBiome();
                  NamespacedKey vanillaBukkitKey = platformBiome.getHandle().getKey();
                  ResourceLocation vanillaMinecraftKey = ResourceLocation.fromNamespaceAndPath(vanillaBukkitKey.getNamespace(), vanillaBukkitKey.getKey());
                  VanillaBiomeProperties vanillaBiomeProperties = (VanillaBiomeProperties)biome.getContext().get(VanillaBiomeProperties.class);
                  Biome platform = NMSBiomeInjector.createBiome((Biome)((Reference)biomeRegistry.get(vanillaMinecraftKey).orElseThrow()).value(), vanillaBiomeProperties);
                  ResourceLocation delegateMinecraftKey = ResourceLocation.fromNamespaceAndPath("terra", NMSBiomeInjector.createBiomeID(pack, key));
                  NamespacedKey delegateBukkitKey = NamespacedKey.fromString(delegateMinecraftKey.toString());
                  ResourceKey<Biome> delegateKey = ResourceKey.create(Registries.BIOME, delegateMinecraftKey);
                  Reference<Biome> holder = biomeRegistry.register(delegateKey, platform, RegistrationInfo.BUILT_IN);
                  Reflection.REFERENCE.invokeBindValue(holder, platform);
                  platformBiome.getContext().put(new BukkitBiomeInfo(delegateBukkitKey));
                  platformBiome.getContext().put(new NMSBiomeInfo(delegateKey));
                  Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerMap = Reflection.VILLAGER_TYPE.getByBiome();
                  villagerMap.put(delegateKey, (ResourceKey)Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(), (ResourceKey)villagerMap.getOrDefault(delegateKey, VillagerType.PLAINS)));
                  ((List)terraBiomeMap.computeIfAbsent(vanillaMinecraftKey, (i) -> {
                     return new ArrayList();
                  })).add(delegateKey.location());
                  LOGGER.debug("Registered biome: " + String.valueOf(delegateKey));
               } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var14) {
                  throw new RuntimeException(var14);
               }
            });
         });
         Reflection.MAPPED_REGISTRY.setFrozen(biomeRegistry, true);
         LOGGER.info("Doing tag garbage....");
         Map<TagKey<Biome>, List<Holder<Biome>>> collect = (Map)biomeRegistry.getTags().collect(HashMap::new, (map, pair) -> {
            map.put(pair.key(), new ArrayList(Reflection.HOLDER_SET.invokeContents(pair).stream().toList()));
         }, HashMap::putAll);
         terraBiomeMap.forEach((vb, terraBiomes) -> {
            NMSBiomeInjector.getEntry(biomeRegistry, vb).ifPresentOrElse((vanilla) -> {
               terraBiomes.forEach((tb) -> {
                  NMSBiomeInjector.getEntry(biomeRegistry, tb).ifPresentOrElse((terra) -> {
                     LOGGER.debug("{} (vanilla for {}): {}", new Object[]{((ResourceKey)vanilla.unwrapKey().orElseThrow()).location(), ((ResourceKey)terra.unwrapKey().orElseThrow()).location(), vanilla.tags().toList()});
                     vanilla.tags().forEach((tag) -> {
                        ((List)collect.computeIfAbsent(tag, (t) -> {
                           return new ArrayList();
                        })).add(terra);
                     });
                  }, () -> {
                     LOGGER.error("No such biome: {}", tb);
                  });
               });
            }, () -> {
               LOGGER.error("No vanilla biome: {}", vb);
            });
         });
         resetTags(biomeRegistry);
         bindTags(biomeRegistry, collect);
      } catch (IllegalArgumentException | SecurityException var3) {
         throw new RuntimeException(var3);
      }
   }

   private static <T> void bindTags(MappedRegistry<T> registry, Map<TagKey<T>, List<Holder<T>>> tagEntries) {
      Map<Reference<T>, List<TagKey<T>>> map = new IdentityHashMap();
      Reflection.MAPPED_REGISTRY.getByKey(registry).values().forEach((entry) -> {
         map.put(entry, new ArrayList());
      });
      tagEntries.forEach((tag, entries) -> {
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            Holder<T> holder = (Holder)var3.next();
            if (!(holder instanceof Reference)) {
               String var10002 = String.valueOf(holder);
               throw new IllegalStateException("Found direct holder " + var10002 + " value in tag " + String.valueOf(tag));
            }

            Reference<T> reference = (Reference)holder;
            ((List)map.get(reference)).add(tag);
         }

      });
      Map<TagKey<T>, Named<T>> map2 = new IdentityHashMap((Map)registry.getTags().collect(Collectors.toMap(Named::key, (named) -> {
         return named;
      })));
      tagEntries.forEach((tag, entries) -> {
         Reflection.HOLDER_SET.invokeBind((Named)map2.computeIfAbsent(tag, (key) -> {
            return Reflection.MAPPED_REGISTRY.invokeCreateTag(registry, key);
         }), entries);
      });
      Reflection.HolderReferenceProxy var10001 = Reflection.HOLDER_REFERENCE;
      Objects.requireNonNull(var10001);
      map.forEach(var10001::invokeBindTags);
      Reflection.MAPPED_REGISTRY.setAllTags(registry, Reflection.MAPPED_REGISTRY_TAG_SET.invokeFromMap(map2));
   }

   private static void resetTags(MappedRegistry<?> registry) {
      registry.getTags().forEach((entryList) -> {
         Reflection.HOLDER_SET.invokeBind(entryList, List.of());
      });
      Reflection.MAPPED_REGISTRY.getByKey(registry).values().forEach((entry) -> {
         Reflection.HOLDER_REFERENCE.invokeBindTags(entry, Set.of());
      });
   }
}
