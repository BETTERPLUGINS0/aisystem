package emanondev.itemedit;

import emanondev.itemedit.utility.VersionUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class CooldownAPI {
   private final YMLConfig conf;
   private final Map<UUID, Map<String, Long>> cooldowns = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();

   CooldownAPI(@NotNull APlugin plugin) {
      long now = System.currentTimeMillis();
      this.conf = plugin.getConfig("cooldownData.yml");
      Iterator var4 = this.conf.getKeys(false).iterator();

      while(var4.hasNext()) {
         String id = (String)var4.next();
         Map<String, Long> map = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();
         this.cooldowns.put(UUID.fromString(id), map);
         Iterator var7 = this.conf.getKeys(id).iterator();

         while(var7.hasNext()) {
            String cooldownId = (String)var7.next();

            try {
               long value = this.conf.getLong(id + "." + cooldownId, 0L);
               if (value > now) {
                  ((Map)map).put(cooldownId, value);
               }
            } catch (Exception var11) {
               plugin.log("Corrupted path value for cooldown data on ");
               var11.printStackTrace();
            }
         }
      }

   }

   void save() {
      long now = System.currentTimeMillis();
      this.conf.getKeys(false).forEach((path) -> {
         this.conf.set(path, (Object)null);
      });
      Iterator var3 = this.cooldowns.keySet().iterator();

      while(var3.hasNext()) {
         UUID uuid = (UUID)var3.next();
         Map<String, Long> values = (Map)this.cooldowns.get(uuid);
         Iterator var6 = values.keySet().iterator();

         while(var6.hasNext()) {
            String id = (String)var6.next();
            if ((Long)values.get(id) > now) {
               this.conf.getLong(uuid.toString() + "." + id, (Long)values.get(id));
            }
         }
      }

      this.conf.save();
   }

   public void setCooldown(@NotNull Block block, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      this.setCooldown(new UUID((long)block.getX() << 32 | (long)block.getY(), (long)block.getZ() << 32 | (long)block.getWorld().getName().hashCode()), cooldownId, duration, timeUnit);
   }

   public void addCooldown(@NotNull Block block, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      this.addCooldown(new UUID((long)block.getX() << 32 | (long)block.getY(), (long)block.getZ() << 32 | (long)block.getWorld().getName().hashCode()), cooldownId, duration, timeUnit);
   }

   public void reduceCooldown(@NotNull Block block, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      this.reduceCooldown(new UUID((long)block.getX() << 32 | (long)block.getY(), (long)block.getZ() << 32 | (long)block.getWorld().getName().hashCode()), cooldownId, duration, timeUnit);
   }

   public void removeCooldown(@NotNull Block block, @NotNull String cooldownId) {
      this.removeCooldown(new UUID((long)block.getX() << 32 | (long)block.getY(), (long)block.getZ() << 32 | (long)block.getWorld().getName().hashCode()), cooldownId);
   }

   public boolean hasCooldown(@NotNull Block block, @NotNull String cooldownId) {
      return this.hasCooldown(new UUID((long)block.getX() << 32 | (long)block.getY(), (long)block.getZ() << 32 | (long)block.getWorld().getName().hashCode()), cooldownId);
   }

   public long getCooldown(@NotNull Block block, @NotNull String cooldownId, @NotNull TimeUnit timeUnit) {
      return this.getCooldown(new UUID((long)block.getX() << 32 | (long)block.getY(), (long)block.getZ() << 32 | (long)block.getWorld().getName().hashCode()), cooldownId, timeUnit);
   }

   public void setCooldown(@NotNull OfflinePlayer player, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      this.setCooldown(player.getUniqueId(), cooldownId, duration, timeUnit);
   }

   public void addCooldown(@NotNull OfflinePlayer player, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      this.addCooldown(player.getUniqueId(), cooldownId, duration, timeUnit);
   }

   public void reduceCooldown(@NotNull OfflinePlayer player, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      this.reduceCooldown(player.getUniqueId(), cooldownId, duration, timeUnit);
   }

   public void removeCooldown(@NotNull OfflinePlayer player, @NotNull String cooldownId) {
      this.removeCooldown(player.getUniqueId(), cooldownId);
   }

   public boolean hasCooldown(@NotNull OfflinePlayer player, @NotNull String cooldownId) {
      return this.hasCooldown(player.getUniqueId(), cooldownId);
   }

   public long getCooldown(@NotNull OfflinePlayer player, @NotNull String cooldownId, @NotNull TimeUnit timeUnit) {
      return this.getCooldown(player.getUniqueId(), cooldownId, timeUnit);
   }

   public void setCooldown(@NotNull UUID uuid, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      if (timeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
         throw new UnsupportedOperationException("Time unit must be at least MILLISECONDS.");
      } else {
         if (duration <= 0L && this.cooldowns.containsKey(uuid)) {
            ((Map)this.cooldowns.get(uuid)).remove(cooldownId);
         } else {
            this.cooldowns.computeIfAbsent(uuid, (k) -> {
               return (Map)(VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap());
            });
            ((Map)this.cooldowns.get(uuid)).put(cooldownId, System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(duration, timeUnit));
         }

      }
   }

   public void addCooldown(@NotNull UUID uuid, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      if (timeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
         throw new UnsupportedOperationException("Time unit must be at least MILLISECONDS.");
      } else {
         this.setCooldown(uuid, cooldownId, this.getCooldown(uuid, cooldownId, TimeUnit.MILLISECONDS) + TimeUnit.MILLISECONDS.convert(duration, timeUnit), TimeUnit.MILLISECONDS);
      }
   }

   public void reduceCooldown(@NotNull UUID uuid, @NotNull String cooldownId, @Range(from = 0L,to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
      if (timeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
         throw new UnsupportedOperationException();
      } else {
         this.setCooldown(uuid, cooldownId, this.getCooldown(uuid, cooldownId, TimeUnit.MILLISECONDS) - TimeUnit.MILLISECONDS.convert(duration, timeUnit), TimeUnit.MILLISECONDS);
      }
   }

   public void removeCooldown(@NotNull UUID uuid, @NotNull String cooldownId) {
      if (this.cooldowns.get(uuid) != null) {
         ((Map)this.cooldowns.get(uuid)).remove(cooldownId);
      }

   }

   public boolean hasCooldown(@NotNull UUID player, @NotNull String cooldownId) {
      return this.getCooldown(player, cooldownId, TimeUnit.MILLISECONDS) > 0L;
   }

   public long getCooldown(@NotNull UUID uuid, @NotNull String cooldownId, @NotNull TimeUnit timeUnit) {
      if (timeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
         throw new UnsupportedOperationException("Time unit must be at least MILLISECONDS.");
      } else {
         long cooldownMS = this.cooldowns.containsKey(uuid) ? Math.max(0L, (Long)((Map)this.cooldowns.get(uuid)).getOrDefault(cooldownId, 0L) - System.currentTimeMillis()) : 0L;
         return timeUnit.convert(cooldownMS, TimeUnit.MILLISECONDS);
      }
   }
}
