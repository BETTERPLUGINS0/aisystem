package fr.xephi.authme.service.velocity;

public enum VMessageType {
   LOGIN,
   REGISTER,
   LOGOUT,
   FORCE_UNREGISTER,
   UNREGISTER;

   // $FF: synthetic method
   private static VMessageType[] $values() {
      return new VMessageType[]{LOGIN, REGISTER, LOGOUT, FORCE_UNREGISTER, UNREGISTER};
   }
}
