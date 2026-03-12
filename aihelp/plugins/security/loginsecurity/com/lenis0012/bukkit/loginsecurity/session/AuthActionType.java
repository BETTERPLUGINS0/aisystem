package com.lenis0012.bukkit.loginsecurity.session;

public enum AuthActionType {
   LOGIN,
   REGISTER,
   REMOVE_PASSWORD,
   CHANGE_PASSWORD,
   LOGOUT,
   BYPASS;

   // $FF: synthetic method
   private static AuthActionType[] $values() {
      return new AuthActionType[]{LOGIN, REGISTER, REMOVE_PASSWORD, CHANGE_PASSWORD, LOGOUT, BYPASS};
   }
}
