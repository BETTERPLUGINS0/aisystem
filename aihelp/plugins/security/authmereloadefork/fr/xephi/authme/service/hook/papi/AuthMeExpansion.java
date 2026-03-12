package fr.xephi.authme.service.hook.papi;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.HooksSettings;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AuthMeExpansion extends PlaceholderExpansion {
   private final Settings settings;

   public AuthMeExpansion() {
      this.settings = AuthMe.settings;
   }

   @NotNull
   public String getIdentifier() {
      return "authme";
   }

   @NotNull
   public String getAuthor() {
      return "HaHaWTH";
   }

   @NotNull
   public String getVersion() {
      return AuthMe.getPluginVersion();
   }

   public boolean persist() {
      return true;
   }

   public String onRequest(OfflinePlayer player, @NotNull String params) {
      if (!(Boolean)this.settings.getProperty(HooksSettings.PLACEHOLDER_API)) {
         return null;
      } else {
         AuthMeApi authMeApi = AuthMeApi.getInstance();
         if (authMeApi == null) {
            return null;
         } else if (params.equalsIgnoreCase("version")) {
            return this.getVersion();
         } else {
            Player onlinePlayer;
            if (params.equalsIgnoreCase("is_registered") && player != null) {
               onlinePlayer = player.getPlayer();
               if (onlinePlayer != null) {
                  return String.valueOf(authMeApi.isRegistered(onlinePlayer.getName()));
               }
            }

            if (params.equalsIgnoreCase("is_authenticated") && player != null) {
               onlinePlayer = player.getPlayer();
               if (onlinePlayer != null) {
                  return String.valueOf(authMeApi.isAuthenticated(onlinePlayer));
               }
            }

            if (params.equalsIgnoreCase("last_login_time") && player != null) {
               onlinePlayer = player.getPlayer();
               if (onlinePlayer != null) {
                  return authMeApi.getLastLoginTime(onlinePlayer.getName()).toString();
               }
            }

            return null;
         }
      }
   }
}
