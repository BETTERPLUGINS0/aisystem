/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$c
 *  net.minecraft.core.IRegistry
 *  net.minecraft.core.RegistrationInfo
 *  net.minecraft.core.RegistryMaterials
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket
 *  net.minecraft.resources.MinecraftKey
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.dedicated.DedicatedServer
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.server.level.WorldServer
 *  net.minecraft.server.network.PlayerConnection
 *  net.minecraft.world.level.biome.BiomeBase
 *  net.minecraft.world.level.biome.BiomeBase$TemperatureModifier
 *  net.minecraft.world.level.biome.BiomeBase$a
 *  net.minecraft.world.level.biome.BiomeFog$GrassColor
 *  net.minecraft.world.level.biome.BiomeFog$a
 *  net.minecraft.world.level.chunk.Chunk
 *  net.minecraft.world.level.chunk.ChunkSection
 *  net.minecraft.world.level.chunk.status.ChunkStatus
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.NamespacedKey
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_21_R4.CraftChunk
 *  org.bukkit.craftbukkit.v1_21_R4.CraftServer
 *  org.bukkit.craftbukkit.v1_21_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.biomes.nms;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.AdvancedBiomeBase;
import net.advancedplugins.seasons.biomes.SeasonData;
import net.advancedplugins.seasons.enums.ColorType;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.objects.INMSBiome;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R4.CraftServer;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSBiome_1_21_4
implements INMSBiome {
    private static CraftServer craftserver;
    private static DedicatedServer dedicatedserver;
    public static IRegistry<BiomeBase> registrywritable;
    private static final Set<String> fullBiomeNames;
    private static BiMap<Integer, String> biomeIds;
    public BiomeBase.a newBiome;
    private int biomeID;
    private Holder<BiomeBase> biomeBaseHolder = null;

    public NMSBiome_1_21_4() {
        craftserver = (CraftServer)Bukkit.getServer();
        dedicatedserver = craftserver.getServer();
        registrywritable = dedicatedserver.ba().f(Registries.aG);
        NMSBiome_1_21_4.unlockRegistry();
        boolean bl = fullBiomeNames.isEmpty();
        registrywritable.forEach(biomeBase -> {
            MinecraftKey minecraftKey = registrywritable.b(biomeBase);
            if (minecraftKey == null) {
                return;
            }
            if (bl) {
                fullBiomeNames.add(minecraftKey.toString());
            }
            biomeIds.put(registrywritable.a(biomeBase), minecraftKey.toString().toLowerCase(Locale.ROOT));
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public NMSBiome_1_21_4(AdvancedBiomeBase advancedBiomeBase, SeasonType seasonType, SeasonData seasonData, int n) {
        try {
            NMSBiome_1_21_4.unlockRegistry();
            ResourceKey resourceKey = ResourceKey.a((ResourceKey)Registries.aG, (MinecraftKey)MinecraftKey.a((String)"advanced", (String)(advancedBiomeBase.getName() + "_" + seasonType.name().toLowerCase() + "_" + n)));
            Optional optional = registrywritable.a(ResourceKey.a((ResourceKey)Registries.aG, (MinecraftKey)MinecraftKey.a((String)"minecraft", (String)advancedBiomeBase.getBiomes().get(0).toLowerCase())));
            if (optional.isEmpty()) {
                throw new IllegalStateException("Could not find vanilla biome: " + advancedBiomeBase.getBiomes().get(0));
            }
            BiomeBase biomeBase = (BiomeBase)((Holder.c)optional.get()).a();
            IRegistry<BiomeBase> iRegistry = registrywritable;
            synchronized (iRegistry) {
                this.newBiome = new BiomeBase.a();
                BiomeFog.a a2 = new BiomeFog.a();
                this.newBiome.a(biomeBase.c());
                this.newBiome.a(biomeBase.d());
                this.newBiome.a(biomeBase.b());
                this.newBiome.b(0.05f);
                this.newBiome.a(0.75f);
                if (!seasonData.isSnow() && !seasonData.getWinterFreeze().booleanValue()) {
                    this.newBiome.a(BiomeBase.TemperatureModifier.b);
                } else {
                    this.newBiome.a(BiomeBase.TemperatureModifier.a);
                    this.newBiome.a(0.0f);
                }
                a2.a(BiomeFog.GrassColor.a);
                a2.d(ASManager.hexToDecimal(seasonData.getColors().get((Object)ColorType.SKY)));
                a2.b(ASManager.hexToDecimal(seasonData.getColors().get((Object)ColorType.WATER)));
                a2.c(ASManager.hexToDecimal(seasonData.getColors().get((Object)ColorType.WATERFOG)));
                a2.g(ASManager.hexToDecimal(seasonData.getColors().get((Object)ColorType.GRASS)));
                a2.e(ASManager.hexToDecimal(seasonData.getColors().get((Object)ColorType.TREE)));
                a2.a(ASManager.hexToDecimal(seasonData.getColors().get((Object)ColorType.FOG)));
                this.newBiome.a(a2.b());
                BiomeBase biomeBase2 = this.newBiome.a();
                RegistryMaterials registryMaterials = (RegistryMaterials)registrywritable;
                registryMaterials.f((Object)biomeBase2);
                registryMaterials.a(resourceKey, (Object)biomeBase2, RegistrationInfo.a);
                this.biomeID = this.getBiomeId(biomeBase2);
            }
        } catch (Exception exception) {
            Core.getInstance().getLogger().severe("Failed to create biome " + advancedBiomeBase.getName() + " for season " + seasonType.name() + ": " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    @Override
    public Holder<BiomeBase> getBiome() {
        if (this.biomeBaseHolder == null) {
            this.biomeBaseHolder = Holder.a((Object)((BiomeBase)registrywritable.a(this.biomeID)));
        }
        return this.biomeBaseHolder;
    }

    private static void unlockRegistry() {
        try {
            RegistryMaterials registryMaterials = (RegistryMaterials)registrywritable;
            Field field2 = RegistryMaterials.class.getDeclaredField("m");
            field2.setAccessible(true);
            if (field2.get(registryMaterials) == null) {
                field2.set(registryMaterials, new IdentityHashMap());
            }
            Arrays.stream(RegistryMaterials.class.getDeclaredFields()).filter(field -> field.getType().equals(Boolean.TYPE)).forEach(field -> {
                try {
                    field.setAccessible(true);
                    field.setBoolean(registryMaterials, false);
                } catch (IllegalAccessException illegalAccessException) {
                    throw new RuntimeException(illegalAccessException);
                }
            });
            Field field3 = RegistryMaterials.class.getDeclaredField("k");
            field3.setAccessible(true);
            String string = MinecraftVersion.isPaper() ? "TagSet" : "a";
            Class<?> clazz = Class.forName(RegistryMaterials.class.getName() + "$" + string);
            Method method = clazz.getDeclaredMethod("a", new Class[0]);
            method.setAccessible(true);
            field3.set(registryMaterials, method.invoke(null, new Object[0]));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendChunkUpdate(Chunk chunk, Player player) {
        try {
            EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
            PlayerConnection playerConnection = entityPlayer.f;
            if (playerConnection != null) {
                CraftChunk craftChunk = (CraftChunk)chunk;
                net.minecraft.world.level.chunk.Chunk chunk2 = (net.minecraft.world.level.chunk.Chunk)craftChunk.getHandle(ChunkStatus.n);
                CraftWorld craftWorld = (CraftWorld)chunk.getWorld();
                WorldServer worldServer = craftWorld.getHandle();
                playerConnection.b((Packet)new ClientboundLevelChunkWithLightPacket(chunk2, worldServer.B_(), null, null));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int getBiomeId(Holder<BiomeBase> holder) {
        return this.getBiomeId((BiomeBase)holder.a());
    }

    @Override
    public Integer getVanillaBiomeId(String string) {
        NamespacedKey namespacedKey = NamespacedKey.fromString((String)string.toLowerCase(Locale.ROOT));
        if (namespacedKey == null) {
            return null;
        }
        return (Integer)biomeIds.inverse().get(string);
    }

    @Override
    public String getDefaultBiome(int n) {
        return (String)biomeIds.get(n);
    }

    @Override
    public Holder<BiomeBase> getBiome(int n) {
        return Holder.a((Object)((BiomeBase)registrywritable.a(n)));
    }

    @Override
    public int getBiomeId(BiomeBase biomeBase) {
        return registrywritable.a((Object)biomeBase);
    }

    @Override
    public Collection<String> getBiomes() {
        return Collections.unmodifiableCollection(fullBiomeNames);
    }

    @Override
    public String getBiome(Location location) {
        World world = location.getWorld();
        WorldServer worldServer = ((CraftWorld)world).getHandle();
        Holder holder = worldServer.getNoiseBiome(location.getBlockX() >> 2, location.getBlockY() >> 2, location.getBlockZ() >> 2);
        MinecraftKey minecraftKey = registrywritable.b((Object)((BiomeBase)holder.a()));
        return Objects.toString(minecraftKey);
    }

    @Override
    public String getType(ChunkSection chunkSection, int n, int n2, int n3) {
        return String.valueOf(chunkSection.a(n, n2, n3).b().k());
    }

    public static BiMap<Integer, String> getBiomeIds() {
        return biomeIds;
    }

    @Override
    public int getBiomeID() {
        return this.biomeID;
    }

    static {
        fullBiomeNames = new HashSet<String>();
        biomeIds = HashBiMap.create();
    }
}

