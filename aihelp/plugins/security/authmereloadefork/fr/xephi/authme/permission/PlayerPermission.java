package fr.xephi.authme.permission;

public enum PlayerPermission implements PermissionNode {
   LOGIN("authme.player.login"),
   LOGOUT("authme.player.logout"),
   REGISTER("authme.player.register"),
   UNREGISTER("authme.player.unregister"),
   CHANGE_PASSWORD("authme.player.changepassword"),
   SEE_EMAIL("authme.player.email.see"),
   ADD_EMAIL("authme.player.email.add"),
   CHANGE_EMAIL("authme.player.email.change"),
   RECOVER_EMAIL("authme.player.email.recover"),
   CAPTCHA("authme.player.captcha"),
   CAN_LOGIN_BE_FORCED("authme.player.canbeforced"),
   SEE_OWN_ACCOUNTS("authme.player.seeownaccounts"),
   VERIFICATION_CODE("authme.player.security.verificationcode"),
   QUICK_COMMANDS_PROTECTION("authme.player.protection.quickcommandsprotection"),
   ENABLE_TWO_FACTOR_AUTH("authme.player.totpadd"),
   DISABLE_TWO_FACTOR_AUTH("authme.player.totpremove");

   private String node;

   private PlayerPermission(String param3) {
      this.node = node;
   }

   public String getNode() {
      return this.node;
   }

   public DefaultPermission getDefaultPermission() {
      return DefaultPermission.ALLOWED;
   }

   // $FF: synthetic method
   private static PlayerPermission[] $values() {
      return new PlayerPermission[]{LOGIN, LOGOUT, REGISTER, UNREGISTER, CHANGE_PASSWORD, SEE_EMAIL, ADD_EMAIL, CHANGE_EMAIL, RECOVER_EMAIL, CAPTCHA, CAN_LOGIN_BE_FORCED, SEE_OWN_ACCOUNTS, VERIFICATION_CODE, QUICK_COMMANDS_PROTECTION, ENABLE_TWO_FACTOR_AUTH, DISABLE_TWO_FACTOR_AUTH};
   }
}
