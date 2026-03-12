package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.player.HelperPlayer;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RTPEffect_Potions {
   private boolean potionEnabled;
   private final HashMap<PotionEffectType, Integer[]> potionEffects = new HashMap();
   private boolean invincibleEnabled;
   private int invincibleTime;

   void load() {
      this.potionEffects.clear();
      FileOther.FILETYPE config = FileOther.FILETYPE.EFFECTS;
      this.invincibleEnabled = config.getBoolean("Invincible.Enabled");
      if (this.invincibleEnabled) {
         this.invincibleTime = config.getInt("Invincible.Seconds");
      }

      this.potionEnabled = config.getBoolean("Potions.Enabled");
      if (this.potionEnabled) {
         List<String> list = config.getStringList("Potions.Types");
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            String p = (String)var3.next();
            String[] ary = p.replaceAll(" ", "").split(":");
            String type = ary[0].trim();
            PotionEffectType effect = PotionEffectType.getByName(type);
            if (effect != null) {
               try {
                  int duration = ary.length >= 2 ? Integer.parseInt(ary[1]) : 60;
                  int amplifier = ary.length >= 3 ? Integer.parseInt(ary[2]) : 1;
                  this.potionEffects.put(effect, new Integer[]{duration, amplifier});
               } catch (NumberFormatException var10) {
                  BetterRTP.getInstance().getLogger().info("The potion duration or amplifier `" + ary[1] + "` is not an integer. Effect was removed!");
               }
            } else {
               BetterRTP.getInstance().getLogger().info("The potion effect `" + type + "` does not exist! Please fix or remove this potion effect! Try '/rtp info potion_effects' to get a list of valid effects!");
            }
         }
      }

   }

   public void giveEffects(Player p) {
      AsyncHandler.syncAtEntity(p, () -> {
         if (this.invincibleEnabled) {
            HelperPlayer.getData(p).setInvincibleEndTime(System.currentTimeMillis() + (long)this.invincibleTime * 1000L);
         }

         if (this.potionEnabled) {
            List<PotionEffect> effects = new ArrayList();
            Iterator var3 = this.potionEffects.keySet().iterator();

            while(var3.hasNext()) {
               PotionEffectType e = (PotionEffectType)var3.next();
               Integer[] mods = (Integer[])this.potionEffects.get(e);
               int duration = mods[0];
               int amplifier = mods[1];
               effects.add(new PotionEffect(e, duration, amplifier, false, false));
            }

            p.addPotionEffects(effects);
         }

      });
   }
}
