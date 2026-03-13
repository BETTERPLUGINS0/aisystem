package com.nisovin.shopkeepers.commands.util;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.commands.arguments.AmbiguousUserNameHandler;
import com.nisovin.shopkeepers.commands.lib.util.ObjectMatcher;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.user.SKUser;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class UserArgumentUtils {
   private static final int DEFAULT_AMBIGUOUS_USER_NAME_MAX_ENTRIES = 5;

   public static Stream<User> getKnownUsers() {
      return Stream.concat(EntityUtils.getOnlinePlayersStream().map(SKUser::of), SKShopkeepersPlugin.getInstance().getShopkeeperRegistry().getAllPlayerShopkeepers().stream().map((x) -> {
         return x.getOwnerUser();
      }).filter((x) -> {
         return !x.isOnline();
      }));
   }

   @Nullable
   public static User findUser(UUID uniqueId) {
      Optional<User> userOpt = getKnownUsers().filter((x) -> {
         return x.getUniqueId().equals(uniqueId);
      }).findFirst();
      if (userOpt.isPresent()) {
         return (User)userOpt.get();
      } else {
         OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
         String name = offlinePlayer.getName();
         return name == null ? null : SKUser.of(uniqueId, name);
      }
   }

   public static boolean handleAmbiguousUserName(CommandSender sender, String name, Iterable<? extends User> matches) {
      return handleAmbiguousUserName(sender, name, matches, 5);
   }

   public static boolean handleAmbiguousUserName(CommandSender sender, String name, Iterable<? extends User> matches, int maxEntries) {
      AmbiguousUserNameHandler ambiguousUserNameHandler = new AmbiguousUserNameHandler(name, matches, maxEntries);
      if (ambiguousUserNameHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousUserNameHandler.getErrorMsg();

         assert errorMsg != null;

         TextUtils.sendMessage(sender, errorMsg);
         return true;
      } else {
         return false;
      }
   }

   private UserArgumentUtils() {
   }

   private abstract static class AbstractUserNameMatcher implements UserArgumentUtils.UserNameMatcher {
      public Stream<User> match(String input, boolean lookupOfflinePlayer) {
         if (StringUtils.isEmpty(input)) {
            return Stream.empty();
         } else {
            if (this.checkExactMatchesFirst()) {
               Stream<User> exactMatches = NAME_EXACT.match(input);
               Iterator<User> iterator = exactMatches.iterator();
               if (iterator.hasNext()) {
                  return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
               }
            }

            String normalizedInput = StringUtils.normalize(input);
            List<User> matchingUsers = new ArrayList();
            boolean[] onlyPerfectMatches = new boolean[]{false};
            Stream var10000 = UserArgumentUtils.getKnownUsers();
            Objects.requireNonNull(var10000);
            Iterable<User> users = var10000::iterator;
            Iterator var7 = users.iterator();

            while(var7.hasNext()) {
               User user = (User)var7.next();

               assert user != null;

               String name = user.getName();
               String normalizedName = StringUtils.normalize(name);
               boolean matched = this.match(normalizedInput, user, normalizedName, matchingUsers, onlyPerfectMatches);
               if (!matched) {
                  String displayName = user.getDisplayName();
                  String normalizedDisplayName = StringUtils.normalize(TextUtils.stripColor(displayName));
                  this.match(normalizedInput, user, normalizedDisplayName, matchingUsers, onlyPerfectMatches);
               }
            }

            if (!onlyPerfectMatches[0] && lookupOfflinePlayer) {
               OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
               if (offlinePlayer.hasPlayedBefore()) {
                  String name = offlinePlayer.getName();
                  if (name == null || name.isEmpty()) {
                     name = input;
                  }

                  User user = SKUser.of(offlinePlayer.getUniqueId(), name);
                  return Stream.of(user);
               }
            }

            return matchingUsers.stream();
         }
      }

      public boolean matchesDisplayNames() {
         return true;
      }

      protected boolean checkExactMatchesFirst() {
         return true;
      }

      protected boolean match(String normalizedInput, User user, String normalizedName, List<User> matchingUsers, boolean[] onlyPerfectMatches) {
         if (this.matches(normalizedInput, normalizedName)) {
            if (normalizedName.length() == normalizedInput.length()) {
               if (!onlyPerfectMatches[0]) {
                  matchingUsers.clear();
               }

               onlyPerfectMatches[0] = true;
               matchingUsers.add(user);
               return true;
            }

            if (!onlyPerfectMatches[0]) {
               matchingUsers.add(user);
               return true;
            }
         }

         return false;
      }

      protected abstract boolean matches(String var1, String var2);
   }

   public interface UserNameMatcher extends ObjectMatcher<User> {
      UserArgumentUtils.UserNameMatcher NAME_EXACT = new UserArgumentUtils.UserNameMatcher() {
         public Stream<User> match(String input, boolean lookupOfflinePlayer) {
            if (StringUtils.isEmpty(input)) {
               return Stream.empty();
            } else {
               String normalizedInput = StringUtils.normalize(input);
               Stream<User> matchingUsers = UserArgumentUtils.getKnownUsers().filter((x) -> {
                  return StringUtils.normalize(x.getName()).equals(normalizedInput);
               });
               Iterator<User> iterator = matchingUsers.iterator();
               if (iterator.hasNext()) {
                  return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
               } else {
                  if (lookupOfflinePlayer) {
                     OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
                     if (offlinePlayer.hasPlayedBefore()) {
                        String name = offlinePlayer.getName();
                        if (name == null || name.isEmpty()) {
                           name = input;
                        }

                        User user = SKUser.of(offlinePlayer.getUniqueId(), name);
                        return Stream.of(user);
                     }
                  }

                  return Stream.empty();
               }
            }
         }

         public boolean matchesDisplayNames() {
            return false;
         }

         // $FF: synthetic method
         private static boolean lambda$match$0(String normalizedInput, User x) {
            return StringUtils.normalize(x.getName()).equals(normalizedInput);
         }
      };
      UserArgumentUtils.UserNameMatcher EXACT = new UserArgumentUtils.AbstractUserNameMatcher() {
         protected boolean checkExactMatchesFirst() {
            return false;
         }

         protected boolean matches(String normalizedInputName, String normalizedName) {
            return normalizedName.equals(normalizedInputName);
         }
      };
      UserArgumentUtils.UserNameMatcher STARTS_WITH = new UserArgumentUtils.AbstractUserNameMatcher() {
         protected boolean matches(String normalizedInputName, String normalizedName) {
            return normalizedName.startsWith(normalizedInputName);
         }
      };
      UserArgumentUtils.UserNameMatcher CONTAINS = new UserArgumentUtils.AbstractUserNameMatcher() {
         protected boolean matches(String normalizedInputName, String normalizedName) {
            return normalizedName.contains(normalizedInputName);
         }
      };

      default Stream<User> match(String input) {
         return this.match(input, false);
      }

      Stream<User> match(String var1, boolean var2);

      boolean matchesDisplayNames();
   }
}
