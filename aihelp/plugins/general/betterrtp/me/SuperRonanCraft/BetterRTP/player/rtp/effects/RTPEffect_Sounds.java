package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.packets.WrapperPlayServerNamedSoundEffect;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RTPEffect_Sounds {
   private boolean enabled;
   private String soundTeleport;
   private String soundDelay;

   void load() {
      FileOther.FILETYPE config = FileOther.FILETYPE.EFFECTS;
      this.enabled = config.getBoolean("Sounds.Enabled");
      if (this.enabled) {
         this.soundTeleport = config.getString("Sounds.Success");
         this.soundDelay = config.getString("Sounds.Delay");
      }

   }

   public void playTeleport(Player p) {
      if (this.enabled) {
         if (this.soundTeleport != null) {
            this.playSound(p.getLocation(), p, this.soundTeleport);
         }

      }
   }

   public void playDelay(Player p) {
      if (this.enabled) {
         if (this.soundDelay != null) {
            this.playSound(p.getLocation(), p, this.soundDelay);
         }

      }
   }

   void playSound(Location loc, Player p, String sound) {
      if (BetterRTP.getInstance().getSettings().isProtocolLibSounds()) {
         try {
            ProtocolManager pm = ProtocolLibrary.getProtocolManager();
            WrapperPlayServerNamedSoundEffect packet = new WrapperPlayServerNamedSoundEffect(pm.createPacket(Server.NAMED_SOUND_EFFECT));
            packet.setSoundName(sound);
            packet.setEffectPositionX((double)loc.getBlockX());
            packet.setEffectPositionY((double)loc.getBlockY());
            packet.setEffectPositionZ((double)loc.getBlockZ());
            packet.sendPacket(p);
         } catch (Exception | NoClassDefFoundError var6) {
            BetterRTP.getInstance().getLogger().severe("ProtocolLib Sounds is enabled in the effects.yml file, but no ProtocolLib plugin was found!");
            p.playSound(p.getLocation(), this.getSound(sound), 1.0F, 1.0F);
         }
      } else {
         p.playSound(p.getLocation(), this.getSound(sound), 1.0F, 1.0F);
      }

   }

   private Sound getSound(String sound) {
      try {
         return Sound.valueOf(sound.toUpperCase());
      } catch (IllegalArgumentException var3) {
         BetterRTP.getInstance().getLogger().info("The sound '" + sound + "' is invalid!");
         return null;
      }
   }
}
