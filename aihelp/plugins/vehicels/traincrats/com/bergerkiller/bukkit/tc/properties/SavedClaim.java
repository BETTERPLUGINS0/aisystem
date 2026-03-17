package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.exception.command.InvalidClaimPlayerNameException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SavedClaim {
   public final UUID playerUUID;
   public final String playerName;

   public SavedClaim(OfflinePlayer player) {
      this.playerUUID = player.getUniqueId();
      this.playerName = player.getName();
   }

   public SavedClaim(UUID playerUUID) {
      this.playerUUID = playerUUID;
      this.playerName = null;
   }

   private SavedClaim(String config) throws IllegalArgumentException {
      config = config.trim();
      int name_end = config.lastIndexOf(32);
      if (name_end == -1) {
         this.playerName = null;
         this.playerUUID = UUID.fromString(config);
      } else {
         this.playerName = config.substring(0, name_end);
         this.playerUUID = UUID.fromString(config.substring(name_end + 1).trim());
      }

   }

   public String description() {
      return this.playerName == null ? "uuid=" + this.playerUUID.toString() : this.playerName;
   }

   public String toString() {
      return this.playerName == null ? this.playerUUID.toString() : this.playerName + " " + this.playerUUID.toString();
   }

   public int hashCode() {
      return this.playerUUID.hashCode();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (o instanceof SavedClaim) {
         SavedClaim other = (SavedClaim)o;
         return other.playerUUID.equals(this.playerUUID);
      } else {
         return false;
      }
   }

   public static boolean hasPermission(ConfigurationNode config, CommandSender sender) {
      if (!(sender instanceof Player)) {
         return true;
      } else {
         Set<SavedClaim> claims = loadClaims(config);
         if (claims.isEmpty()) {
            return true;
         } else {
            UUID playerUUID = ((Player)sender).getUniqueId();
            Iterator var4 = claims.iterator();

            SavedClaim claim;
            do {
               if (!var4.hasNext()) {
                  return false;
               }

               claim = (SavedClaim)var4.next();
            } while(!playerUUID.equals(claim.playerUUID));

            return true;
         }
      }
   }

   public static Set<SavedClaim> loadClaims(ConfigurationNode config) {
      if (!config.contains("claims")) {
         return Collections.emptySet();
      } else {
         List<String> claim_strings = config.getList("claims", String.class);
         if (claim_strings != null && !claim_strings.isEmpty()) {
            Set<SavedClaim> claims = new HashSet(claim_strings.size());
            Iterator var3 = claim_strings.iterator();

            while(var3.hasNext()) {
               String claim_str = (String)var3.next();

               try {
                  claims.add(new SavedClaim(claim_str));
               } catch (IllegalArgumentException var6) {
               }
            }

            return Collections.unmodifiableSet(claims);
         } else {
            return Collections.emptySet();
         }
      }
   }

   public static void saveClaims(ConfigurationNode config, Collection<SavedClaim> claims) {
      if (claims.isEmpty()) {
         config.remove("claims");
      } else {
         List<String> claim_strings = new ArrayList(claims.size());
         Iterator var3 = claims.iterator();

         while(var3.hasNext()) {
            SavedClaim claim = (SavedClaim)var3.next();
            claim_strings.add(claim.toString());
         }

         config.set("claims", claim_strings);
      }

   }

   public static Set<SavedClaim> parseClaims(Set<SavedClaim> oldClaims, String[] players) {
      Set<SavedClaim> result = new HashSet(players.length);
      String[] var3 = players;
      int var4 = players.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String playerArg = var3[var5];

         try {
            UUID queryUUID = UUID.fromString(playerArg);
            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(queryUUID);
            if (!(player instanceof Player) && (player.getName() == null || !player.hasPlayedBefore())) {
               player = null;
            }

            if (player != null) {
               result.add(new SavedClaim(player));
            } else {
               boolean uuidMatchesOldClaim = false;
               Iterator var16 = oldClaims.iterator();

               while(var16.hasNext()) {
                  SavedClaim oldClaim = (SavedClaim)var16.next();
                  if (oldClaim.playerUUID.equals(queryUUID)) {
                     result.add(oldClaim);
                     uuidMatchesOldClaim = true;
                     break;
                  }
               }

               if (!uuidMatchesOldClaim) {
                  result.add(new SavedClaim(queryUUID));
               }
            }
         } catch (IllegalArgumentException var12) {
            boolean nameMatchesOldClaim = false;
            Iterator var9 = oldClaims.iterator();

            while(var9.hasNext()) {
               SavedClaim oldClaim = (SavedClaim)var9.next();
               if (oldClaim.playerName != null && oldClaim.playerName.equals(playerArg)) {
                  result.add(oldClaim);
                  nameMatchesOldClaim = true;
                  break;
               }
            }

            if (!nameMatchesOldClaim) {
               OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(playerArg);
               if (!(player instanceof Player) && (player.getName() == null || !player.hasPlayedBefore())) {
                  throw new InvalidClaimPlayerNameException(playerArg);
               }

               result.add(new SavedClaim(player));
            }
         }
      }

      return result;
   }

   public static void buildClaimList(MessageBuilder builder, Set<SavedClaim> claims) {
      if (claims.isEmpty()) {
         builder.red(new Object[]{"Not Claimed"});
      } else {
         builder.setSeparator(ChatColor.WHITE, ", ");
         Iterator var2 = claims.iterator();

         while(var2.hasNext()) {
            SavedClaim claim = (SavedClaim)var2.next();
            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(claim.playerUUID);
            String name = player.getName();
            if (name == null) {
               name = claim.playerName;
               if (name == null) {
                  name = claim.playerUUID.toString();
               }

               builder.red(new Object[]{name});
            } else if (player.isOnline()) {
               builder.aqua(new Object[]{name});
            } else {
               builder.white(new Object[]{name});
            }
         }
      }

   }
}
