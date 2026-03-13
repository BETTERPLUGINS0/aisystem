package com.nisovin.shopkeepers.commands.lib.util;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.arguments.AmbiguousPlayerEntryNameHandler;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerArgumentUtils {
   private static final int DEFAULT_AMBIGUOUS_PLAYER_NAME_MAX_ENTRIES = 5;

   public static boolean handleAmbiguousPlayerName(CommandSender sender, String name, Iterable<? extends Entry<? extends UUID, ? extends String>> matches) {
      return handleAmbiguousPlayerName(sender, name, matches, 5);
   }

   public static boolean handleAmbiguousPlayerName(CommandSender sender, String name, Iterable<? extends Entry<? extends UUID, ? extends String>> matches, int maxEntries) {
      AmbiguousPlayerEntryNameHandler ambiguousPlayerNameHandler = new AmbiguousPlayerEntryNameHandler(name, matches, maxEntries);
      if (ambiguousPlayerNameHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousPlayerNameHandler.getErrorMsg();

         assert errorMsg != null;

         TextUtils.sendMessage(sender, errorMsg);
         return true;
      } else {
         return false;
      }
   }

   private PlayerArgumentUtils() {
   }

   private abstract static class AbstractPlayerNameMatcher implements PlayerArgumentUtils.PlayerNameMatcher {
      public Stream<Player> match(String input) {
         if (StringUtils.isEmpty(input)) {
            return Stream.empty();
         } else {
            if (this.checkExactMatchFirst()) {
               Player exactMatch = Bukkit.getPlayerExact(input);
               if (exactMatch != null) {
                  return Stream.of(exactMatch);
               }
            }

            String normalizedInput = StringUtils.normalize(input);
            List<Player> matchingPlayers = new ArrayList();
            boolean[] onlyPerfectMatches = new boolean[]{false};
            Iterator var5 = Bukkit.getOnlinePlayers().iterator();

            while(var5.hasNext()) {
               Player player = (Player)var5.next();

               assert player != null;

               String playerName = (String)Unsafe.assertNonNull(player.getName());
               String normalizedPlayerName = StringUtils.normalize(playerName);
               boolean matched = this.match(normalizedInput, player, normalizedPlayerName, matchingPlayers, onlyPerfectMatches);
               if (matched) {
                  if (onlyPerfectMatches[0]) {
                     return Stream.of(player);
                  }
               } else {
                  String displayName = player.getDisplayName();
                  String normalizedDisplayName = StringUtils.normalize(TextUtils.stripColor(displayName));
                  this.match(normalizedInput, player, normalizedDisplayName, matchingPlayers, onlyPerfectMatches);
               }
            }

            return matchingPlayers.stream();
         }
      }

      public boolean matchesDisplayNames() {
         return true;
      }

      protected boolean checkExactMatchFirst() {
         return true;
      }

      protected boolean match(String normalizedInput, Player player, String normalizedName, List<Player> matchingPlayers, boolean[] onlyPerfectMatches) {
         if (this.matches(normalizedInput, normalizedName)) {
            if (normalizedName.length() == normalizedInput.length()) {
               if (!onlyPerfectMatches[0]) {
                  matchingPlayers.clear();
               }

               onlyPerfectMatches[0] = true;
               matchingPlayers.add(player);
               return true;
            }

            if (!onlyPerfectMatches[0]) {
               matchingPlayers.add(player);
               return true;
            }
         }

         return false;
      }

      protected abstract boolean matches(String var1, String var2);
   }

   public interface PlayerNameMatcher extends ObjectMatcher<Player> {
      PlayerArgumentUtils.PlayerNameMatcher NAME_EXACT = new PlayerArgumentUtils.PlayerNameMatcher() {
         public Stream<Player> match(String input) {
            if (StringUtils.isEmpty(input)) {
               return Stream.empty();
            } else {
               Player exactMatch = Bukkit.getPlayerExact(input);
               return exactMatch != null ? Stream.of(exactMatch) : Stream.empty();
            }
         }

         public boolean matchesDisplayNames() {
            return false;
         }
      };
      PlayerArgumentUtils.PlayerNameMatcher EXACT = new PlayerArgumentUtils.AbstractPlayerNameMatcher() {
         protected boolean checkExactMatchFirst() {
            return false;
         }

         protected boolean matches(String normalizedInputName, String normalizedName) {
            return normalizedName.equals(normalizedInputName);
         }
      };
      PlayerArgumentUtils.PlayerNameMatcher STARTS_WITH = new PlayerArgumentUtils.AbstractPlayerNameMatcher() {
         protected boolean matches(String normalizedInputName, String normalizedName) {
            return normalizedName.startsWith(normalizedInputName);
         }
      };
      PlayerArgumentUtils.PlayerNameMatcher CONTAINS = new PlayerArgumentUtils.AbstractPlayerNameMatcher() {
         protected boolean matches(String normalizedInputName, String normalizedName) {
            return normalizedName.contains(normalizedInputName);
         }
      };

      Stream<Player> match(String var1);

      boolean matchesDisplayNames();
   }
}
