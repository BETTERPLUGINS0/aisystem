/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  net.minecraft.core.Holder
 *  net.minecraft.network.PacketDataSerializer
 *  net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData
 *  net.minecraft.world.level.biome.BiomeBase
 *  net.minecraft.world.level.chunk.ChunkSection
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_20_R3.CraftWorld
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.biomes.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.util.Arrays;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomesHandler;
import net.advancedplugins.seasons.biomes.nms.NMSBiome_1_20_3;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.objects.INMSBiome;
import net.advancedplugins.seasons.objects.IProtocolLib;
import net.minecraft.core.Holder;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Packets_1_20_3
implements IProtocolLib {
    private final INMSBiome nmsBiome;
    private final BiomesHandler biomesHandler;

    public Packets_1_20_3(JavaPlugin javaPlugin, BiomesHandler biomesHandler, INMSBiome iNMSBiome) {
        this.nmsBiome = iNMSBiome;
        this.biomesHandler = biomesHandler;
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter((Plugin)javaPlugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Server.MAP_CHUNK}){

            public void onPacketSending(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (!Core.getWorldHandler().isWorldEnabled(player.getWorld().getName())) {
                    return;
                }
                boolean bl = Core.getBedrockHandler().getBedrockPlayers().contains(player.getUniqueId());
                Season season = Core.getSeasonHandler().getSeason(player.getWorld());
                ClientboundLevelChunkPacketData clientboundLevelChunkPacketData = (ClientboundLevelChunkPacketData)packetEvent.getPacket().getSpecificModifier(ClientboundLevelChunkPacketData.class).read(0);
                ChunkSection[] chunkSectionArray = Packets_1_20_3.this.getChunkSections(clientboundLevelChunkPacketData, player.getWorld());
                for (ChunkSection chunkSection : chunkSectionArray) {
                    for (int i = 0; i < 4; ++i) {
                        for (int j = 0; j < 4; ++j) {
                            for (int k = 0; k < 4; ++k) {
                                Integer n = Packets_1_20_3.this.getBiome(Packets_1_20_3.this.nmsBiome.getBiomeId((BiomeBase)chunkSection.c(i, j, k).a()), season, bl);
                                if (n == null) continue;
                                chunkSection.setBiome(i, j, k, Holder.a((Object)((BiomeBase)NMSBiome_1_20_3.registrywritable.a(n.intValue()))));
                            }
                        }
                    }
                }
                try {
                    Field field = clientboundLevelChunkPacketData.getClass().getDeclaredField("c");
                    field.setAccessible(true);
                    field.set(clientboundLevelChunkPacketData, Packets_1_20_3.this.convertToBytes(chunkSectionArray));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    @Override
    public byte[] convertToBytes(ChunkSection[] chunkSectionArray) {
        byte[] byArray = new byte[Arrays.stream(chunkSectionArray).mapToInt(ChunkSection::j).sum()];
        ByteBuf byteBuf = Unpooled.wrappedBuffer((byte[])byArray).writerIndex(0);
        Arrays.stream(chunkSectionArray).forEach(chunkSection -> chunkSection.c(new PacketDataSerializer(byteBuf)));
        return byArray;
    }

    @Override
    public Integer getBiome(int n, Season season, boolean bl) {
        return this.biomesHandler.getBiomeReplacement(n, season, bl);
    }

    @Override
    public ChunkSection[] getChunkSections(ClientboundLevelChunkPacketData clientboundLevelChunkPacketData, World world) {
        PacketDataSerializer packetDataSerializer = clientboundLevelChunkPacketData.a();
        ChunkSection[] chunkSectionArray = new ChunkSection[((CraftWorld)world).getHandle().am()];
        for (int i = 0; i < chunkSectionArray.length; ++i) {
            ChunkSection chunkSection = new ChunkSection(NMSBiome_1_20_3.registrywritable);
            chunkSection.a(packetDataSerializer);
            chunkSectionArray[i] = chunkSection;
        }
        return chunkSectionArray;
    }
}

