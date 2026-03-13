package me.casperge.realisticseasons.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.Iterator;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.landplugins.LandPlugin;
import me.casperge.realisticseasons.commands.ToggleSeasonsCommand;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SubSeason;
import me.casperge.realisticseasons1_18_R2.ProtocolLibUtils1_18_R2;
import org.bukkit.entity.Player;
import world.ofunny.bpm.Floodgate.FloodgateAPI;

public class ChunkPacketEventProtocolLib1_18_R2 {
   private ProtocolLibUtils1_18_R2 protutils = new ProtocolLibUtils1_18_R2();

   public ChunkPacketEventProtocolLib1_18_R2(final RealisticSeasons var1) {
      ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(var1, ListenerPriority.LOWEST, new PacketType[]{Server.MAP_CHUNK}) {
         public void onPacketSending(PacketEvent var1x) {
            PacketContainer var2 = var1x.getPacket();
            Player var3 = var1x.getPlayer();
            if (var1.getSettings().affectBiomes) {
               int var4 = (Integer)var2.getIntegers().read(0);
               int var5 = (Integer)var2.getIntegers().read(1);
               Season var6 = var1.getSeasonManager().getSeason(var3.getWorld());
               SubSeason var7 = null;
               if (var6 == Season.FALL || var6 == Season.WINTER || var6 == Season.SPRING || var6 == Season.SUMMER) {
                  var7 = var1.getSeasonManager().getSubSeason(var3.getWorld());
               }

               if (!var1.hasSeasons(var4, var5, var3.getWorld())) {
                  if (!var1.hasLandPlugin()) {
                     return;
                  }

                  boolean var8 = false;
                  Iterator var9 = var1.getLandPluginAPIs().iterator();

                  while(var9.hasNext()) {
                     LandPlugin var10 = (LandPlugin)var9.next();
                     if (var10.getPermanentSeason(var4, var5, var3.getWorld()) != Season.DISABLED) {
                        var6 = var10.getPermanentSeason(var4, var5, var3.getWorld());
                        var8 = true;
                     }
                  }

                  if (!var8) {
                     return;
                  }
               }

               if (ToggleSeasonsCommand.disabled.contains(var3.getUniqueId())) {
                  return;
               }

               if (var6 == Season.WINTER) {
                  if (!FloodgateAPI.get().isBedrockPlayer(var3) && var1.supportsCustomBiomes(var3)) {
                     ChunkPacketEventProtocolLib1_18_R2.this.protutils.readPacket(var2, var3.getWorld(), Season.WINTER.intValue(), false, var7.getPhase());
                     return;
                  }

                  ChunkPacketEventProtocolLib1_18_R2.this.protutils.readPacket(var2, var3.getWorld(), Season.WINTER.intValue(), true, var7.getPhase());
                  return;
               }

               if (var6 == Season.FALL) {
                  if (!FloodgateAPI.get().isBedrockPlayer(var3) && var1.supportsCustomBiomes(var3)) {
                     ChunkPacketEventProtocolLib1_18_R2.this.protutils.readPacket(var2, var3.getWorld(), Season.FALL.intValue(), false, var7.getPhase());
                     return;
                  }

                  ChunkPacketEventProtocolLib1_18_R2.this.protutils.readPacket(var2, var3.getWorld(), Season.FALL.intValue(), true, var7.getPhase());
                  return;
               }

               if (var6 == Season.SPRING) {
                  if (!FloodgateAPI.get().isBedrockPlayer(var3) && var1.supportsCustomBiomes(var3)) {
                     ChunkPacketEventProtocolLib1_18_R2.this.protutils.readPacket(var2, var3.getWorld(), Season.SPRING.intValue(), false, var7.getPhase());
                     return;
                  }

                  return;
               }

               if (var6 == Season.SUMMER) {
                  if (!FloodgateAPI.get().isBedrockPlayer(var3) && var1.supportsCustomBiomes(var3)) {
                     ChunkPacketEventProtocolLib1_18_R2.this.protutils.readPacket(var2, var3.getWorld(), Season.SUMMER.intValue(), false, var7.getPhase());
                     return;
                  }

                  return;
               }
            }

         }
      });
   }
}
