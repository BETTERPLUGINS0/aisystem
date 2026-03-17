package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.collections.StringMap;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Effect {
   private static final StringMap<Integer> DIR_NAMES = new StringMap();
   private static final StringMap<Integer> DISK_NAMES = new StringMap();
   public final List<String> effects = new ArrayList();
   public float pitch = 1.0F;
   public float volume = 1.0F;
   public int range;

   public void parseEffect(String text) {
      text = text.toUpperCase(Locale.ENGLISH).replace(' ', '_');
      text = text.replace("MUSIC", "RECORD");
      if (text.equals("LINK")) {
         this.effects.add("SMOKE");
         this.effects.add("EXTINGUISH");
      } else {
         this.effects.add(text);
      }
   }

   private String trimSpace(String text) {
      return text.substring(StringUtil.getSuccessiveCharCount(text, '_'));
   }

   public void play(Player player) {
      this.play(player.getEyeLocation(), player);
   }

   public void play(Location location) {
      this.play(location, (Player)null);
   }

   public void play(Location location, Player player) {
      Iterator var3 = this.effects.iterator();

      while(true) {
         while(var3.hasNext()) {
            String name = (String)var3.next();
            Integer data;
            if (name.startsWith("SMOKE")) {
               name = this.trimSpace(name.substring(5));
               data = null;
               if (name.length() >= 2) {
                  data = (Integer)DIR_NAMES.get(name.substring(0, 2));
               }

               if (data == null && name.length() >= 1) {
                  data = (Integer)DIR_NAMES.get(name.substring(0, 1));
               }

               if (data == null) {
                  try {
                     data = ParseUtil.parseInt(name, (Integer)null);
                  } catch (NumberFormatException var7) {
                  }
               }

               if (data == null) {
                  data = 0;
               }

               if (data == 4) {
                  playEffect(location.clone().add(0.0D, 0.5D, 0.0D), player, org.bukkit.Effect.SMOKE, data);
               } else {
                  playEffect(location, player, org.bukkit.Effect.SMOKE, data);
               }
            } else if (name.startsWith("RECORD")) {
               name = this.trimSpace(name.substring(6));
               if (name.startsWith("PLAY")) {
                  name = this.trimSpace(name.substring(4));
               }

               data = (Integer)DISK_NAMES.get(name);
               if (data == null) {
                  try {
                     data = ParseUtil.parseInt(name, (Integer)null);
                  } catch (NumberFormatException var8) {
                  }
               }

               if (data == null) {
                  data = 2257;
               }

               playEffect(location, player, org.bukkit.Effect.RECORD_PLAY, data);
            } else {
               org.bukkit.Effect effect = (org.bukkit.Effect)ParseUtil.parseEnum(org.bukkit.Effect.class, name, (Object)null);
               if (effect != null && !effect.toString().toUpperCase(Locale.ENGLISH).endsWith("_BREAK")) {
                  playEffect(location, player, effect, 0);
               } else {
                  Sound sound = (Sound)ParseUtil.parseEnum(Sound.class, name, (Object)null);
                  if (sound != null) {
                     playSound(location, player, sound, this.volume, this.pitch);
                  } else {
                     playSound(location, player, name, this.volume, this.pitch);
                  }
               }
            }
         }

         return;
      }
   }

   private static void playEffect(Location location, Player player, org.bukkit.Effect effect, int data) {
      try {
         if (player != null) {
            player.playEffect(location, effect, data);
         } else {
            location.getWorld().playEffect(location, effect, data);
         }

         location.getWorld().playEffect(location, effect, data);
      } catch (Throwable var5) {
      }

   }

   private static void playSound(Location location, Player player, Sound sound, float volume, float pitch) {
      try {
         if (player != null) {
            player.playSound(location, sound, volume, pitch);
         } else {
            location.getWorld().playSound(location, sound, volume, pitch);
         }
      } catch (Throwable var6) {
      }

   }

   private static void playSound(Location location, Player player, String name, float volume, float pitch) {
      try {
         if (player != null) {
            PlayerUtil.playSound(player, location, SoundEffect.fromName(name), volume, pitch);
         } else {
            WorldUtil.playSound(location, SoundEffect.fromName(name), volume, pitch);
         }
      } catch (Throwable var6) {
      }

   }

   static {
      DIR_NAMES.putUpper("U", 4);
      DIR_NAMES.putUpper("M", 4);
      DIR_NAMES.putUpper("N", 7);
      DIR_NAMES.putUpper("E", 3);
      DIR_NAMES.putUpper("S", 1);
      DIR_NAMES.putUpper("W", 5);
      DIR_NAMES.putUpper("NE", 6);
      DIR_NAMES.putUpper("SE", 0);
      DIR_NAMES.putUpper("NW", 8);
      DIR_NAMES.putUpper("SW", 2);
      DISK_NAMES.putUpper("NONE", 0);
      DISK_NAMES.putUpper("13", 2256);
      DISK_NAMES.putUpper("CAT", 2257);
      DISK_NAMES.putUpper("BLOCKS", 2258);
      DISK_NAMES.putUpper("CHIRP", 2259);
      DISK_NAMES.putUpper("FAR", 2260);
      DISK_NAMES.putUpper("MALL", 2261);
      DISK_NAMES.putUpper("MELLOHI", 2262);
      DISK_NAMES.putUpper("STAL", 2263);
      DISK_NAMES.putUpper("STRAD", 2264);
      DISK_NAMES.putUpper("WARD", 2265);
      DISK_NAMES.putUpper("11", 2266);
      DISK_NAMES.putUpper("WAIT", 2267);
   }
}
