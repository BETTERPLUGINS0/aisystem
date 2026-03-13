package tntrun.arena.handlers;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.utils.Sounds;

public class SoundHandler extends Sounds {
   private TNTRun plugin;

   public SoundHandler(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void NOTE_PLING(Player p, float volume, float pitch) {
      NamespacedKey key = this.getKey("block_note_block_pling");
      p.playSound(p.getLocation(), (Sound)Registry.SOUNDS.get(key), volume, pitch);
   }

   public void ARENA_START(Player p) {
      if (this.isSoundEnabled("arenastart")) {
         p.playSound(p.getLocation(), this.getSound("arenastart"), this.getVolume("arenastart"), this.getPitch("arenastart"));
      }

   }

   public void ITEM_SELECT(Player p) {
      if (this.isSoundEnabled("itemselect")) {
         p.playSound(p.getLocation(), this.getSound("itemselect"), this.getVolume("itemselect"), this.getPitch("itemselect"));
      }

   }

   public void INVITE_MESSAGE(Player p) {
      if (this.isSoundEnabled("invitationmessage")) {
         p.playSound(p.getLocation(), this.getSound("invitationmessage"), this.getVolume("invitationmessage"), this.getPitch("invitationmessage"));
      }

   }

   public void BLOCK_BREAK(Block fblock) {
      if (this.isSoundEnabled("blockbreak")) {
         fblock.getWorld().playSound(fblock.getLocation(), this.getSound("blockbreak"), this.getVolume("blockbreak"), this.getPitch("blockbreak"));
      }

   }

   private Sound getSound(String path) {
      NamespacedKey key = this.getKey(this.plugin.getConfig().getString("sounds." + path + ".sound"));
      return (Sound)Registry.SOUNDS.get(key);
   }

   private float getVolume(String path) {
      float volume = (float)this.plugin.getConfig().getDouble("sounds." + path + ".volume", 1.0D);
      return volume > 0.0F ? volume : 1.0F;
   }

   private float getPitch(String path) {
      float pitch = (float)this.plugin.getConfig().getDouble("sounds." + path + ".pitch", 1.0D);
      return (double)pitch > 0.5D && (double)pitch < 2.0D ? pitch : 1.0F;
   }

   private boolean isSoundEnabled(String path) {
      return this.plugin.getConfig().getBoolean("sounds." + path + ".enabled");
   }

   private NamespacedKey getKey(String keyString) {
      return NamespacedKey.minecraft(keyString.toLowerCase().replaceAll("_", "."));
   }
}
