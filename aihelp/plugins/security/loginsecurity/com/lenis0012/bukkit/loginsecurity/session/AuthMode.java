package com.lenis0012.bukkit.loginsecurity.session;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import org.jetbrains.annotations.Nullable;

public enum AuthMode {
   AUTHENTICATED,
   UNAUTHENTICATED,
   UNREGISTERED;

   @Nullable
   public LanguageKeys getAuthMessage() {
      switch(this) {
      case UNAUTHENTICATED:
         return LanguageKeys.MESSAGE_LOGIN;
      case UNREGISTERED:
         return LoginSecurity.getConfiguration().isRegisterConfirmPassword() ? LanguageKeys.MESSAGE_REGISTER2 : LanguageKeys.MESSAGE_REGISTER;
      default:
         return null;
      }
   }

   // $FF: synthetic method
   private static AuthMode[] $values() {
      return new AuthMode[]{AUTHENTICATED, UNAUTHENTICATED, UNREGISTERED};
   }
}
