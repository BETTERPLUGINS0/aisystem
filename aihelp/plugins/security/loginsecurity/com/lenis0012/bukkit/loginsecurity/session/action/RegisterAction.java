package com.lenis0012.bukkit.loginsecurity.session.action;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.hashing.Algorithm;
import com.lenis0012.bukkit.loginsecurity.session.AuthAction;
import com.lenis0012.bukkit.loginsecurity.session.AuthActionType;
import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.exceptions.ProfileRefreshException;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import java.sql.SQLException;
import java.util.logging.Level;

public class RegisterAction extends AuthAction {
   private final String password;

   public <T> RegisterAction(AuthService<T> service, T serviceProvider, String password) {
      super(AuthActionType.REGISTER, service, serviceProvider);
      this.password = password;
   }

   public AuthMode run(PlayerSession session, ActionResponse response) {
      try {
         session.refreshProfile();
      } catch (ProfileRefreshException var8) {
         response.setSuccess(false);
         response.setErrorMessage("Your account was modified by a third party, please rejoin!");
         return null;
      }

      LoginSecurity plugin = (LoginSecurity)LoginSecurity.getInstance();
      PlayerProfile profile = session.getProfile();
      String hash = Algorithm.BCRYPT.hash(this.password);
      profile.setPassword(hash);
      profile.setHashingAlgorithm(Algorithm.BCRYPT.getId());

      try {
         plugin.datastore().getProfileRepository().insertBlocking(profile);
      } catch (SQLException var7) {
         plugin.getLogger().log(Level.SEVERE, "Failed to register user", var7);
         response.setSuccess(false);
         response.setErrorMessage("Failed to register your account, try again later.");
         return null;
      }

      return AuthMode.AUTHENTICATED;
   }
}
