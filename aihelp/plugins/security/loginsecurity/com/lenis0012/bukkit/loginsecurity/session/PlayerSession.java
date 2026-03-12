package com.lenis0012.bukkit.loginsecurity.session;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.events.AuthActionEvent;
import com.lenis0012.bukkit.loginsecurity.events.AuthModeChangedEvent;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionCallback;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionResponse;
import com.lenis0012.bukkit.loginsecurity.session.exceptions.ProfileRefreshException;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerSession {
   private PlayerProfile profile;
   private AuthMode mode;

   protected PlayerSession(PlayerProfile profile, AuthMode mode) {
      this.profile = profile;
      this.mode = mode;
   }

   public PlayerProfile getProfile() {
      return this.profile;
   }

   public void saveProfileAsync() {
      if (!this.isRegistered()) {
         throw new IllegalStateException("Can't save profile when not registered!");
      } else {
         LoginSecurity.getDatastore().getProfileRepository().update(this.profile, (result) -> {
            if (!result.isSuccess()) {
               LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to save user profile", result.getError());
            }

         });
      }
   }

   public void refreshProfile() throws ProfileRefreshException {
      PlayerProfile newProfile;
      try {
         newProfile = LoginSecurity.getDatastore().getProfileRepository().findByUniqueUserIdBlocking(UUID.fromString(this.profile.getUniqueUserId()));
      } catch (SQLException var3) {
         throw new ProfileRefreshException("Failed to load profile from database", var3);
      }

      if (newProfile != null && !this.isRegistered()) {
         throw new ProfileRefreshException("Profile was registered while in database!");
      } else if (newProfile == null && this.isRegistered()) {
         throw new ProfileRefreshException("Profile was not found, even though it should be there!");
      } else if (newProfile != null) {
         this.profile = newProfile;
      }
   }

   public void resetProfile() {
      String lastName = this.profile.getLastName();
      this.profile = LoginSecurity.getSessionManager().createBlankProfile(UUID.fromString(this.profile.getUniqueUserId()));
      this.profile.setLastName(lastName);
   }

   public boolean isLoggedIn() {
      return this.isAuthorized() && this.profile.getPassword() != null;
   }

   public boolean isAuthorized() {
      return this.mode == AuthMode.AUTHENTICATED;
   }

   public boolean isRegistered() {
      return this.profile.getPassword() != null;
   }

   public AuthMode getAuthMode() {
      return this.mode;
   }

   public Player getPlayer() {
      return Bukkit.getPlayer(this.profile.getLastName());
   }

   public void performActionAsync(AuthAction action, ActionCallback callback) {
      LoginSecurity.getExecutorService().execute(() -> {
         ActionResponse response = this.performAction(action);
         Bukkit.getScheduler().runTask(LoginSecurity.getInstance(), () -> {
            callback.call(response);
         });
      });
   }

   public ActionResponse performAction(AuthAction action) {
      boolean sync = Bukkit.isPrimaryThread();
      if (sync) {
         Bukkit.getScheduler().runTaskAsynchronously(LoginSecurity.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(new AuthActionEvent(this, action));
         });
      } else {
         AuthActionEvent event = new AuthActionEvent(this, action);
         Bukkit.getPluginManager().callEvent(event);
         if (event.isCancelled()) {
            return new ActionResponse(false, event.getCancelledMessage());
         }
      }

      ActionResponse response = new ActionResponse();
      AuthMode previous = this.mode;
      AuthMode current = action.run(this, response);
      if (current != null && response.isSuccess()) {
         this.mode = current;
         if (previous != this.mode) {
            AuthModeChangedEvent event = new AuthModeChangedEvent(this, previous, this.mode);
            if (sync) {
               Bukkit.getScheduler().runTaskAsynchronously(LoginSecurity.getInstance(), () -> {
                  Bukkit.getPluginManager().callEvent(event);
               });
            } else {
               Bukkit.getPluginManager().callEvent(event);
            }
         }

         return response;
      } else {
         return response;
      }
   }
}
