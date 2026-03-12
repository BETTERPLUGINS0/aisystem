package fr.xephi.authme.permission;

public enum AdminPermission implements PermissionNode {
   REGISTER("authme.admin.register"),
   UNREGISTER("authme.admin.unregister"),
   FORCE_LOGIN("authme.admin.forcelogin"),
   CHANGE_PASSWORD("authme.admin.changepassword"),
   LAST_LOGIN("authme.admin.lastlogin"),
   ACCOUNTS("authme.admin.accounts"),
   GET_EMAIL("authme.admin.getemail"),
   CHANGE_EMAIL("authme.admin.changemail"),
   VIEW_TOTP_STATUS("authme.admin.totpviewstatus"),
   DISABLE_TOTP("authme.admin.totpdisable"),
   GET_IP("authme.admin.getip"),
   SEE_RECENT_PLAYERS("authme.admin.seerecent"),
   SPAWN("authme.admin.spawn"),
   SET_SPAWN("authme.admin.setspawn"),
   FIRST_SPAWN("authme.admin.firstspawn"),
   SET_FIRST_SPAWN("authme.admin.setfirstspawn"),
   PURGE("authme.admin.purge"),
   PURGE_LAST_POSITION("authme.admin.purgelastpos"),
   PURGE_BANNED_PLAYERS("authme.admin.purgebannedplayers"),
   PURGE_PLAYER("authme.admin.purgeplayer"),
   SWITCH_ANTIBOT("authme.admin.switchantibot"),
   CONVERTER("authme.admin.converter"),
   RELOAD("authme.admin.reload"),
   ANTIBOT_MESSAGES("authme.admin.antibotmessages"),
   UPDATE_MESSAGES("authme.admin.updatemessages"),
   SEE_OTHER_ACCOUNTS("authme.admin.seeotheraccounts"),
   BACKUP("authme.admin.backup");

   private String node;

   private AdminPermission(String param3) {
      this.node = node;
   }

   public String getNode() {
      return this.node;
   }

   public DefaultPermission getDefaultPermission() {
      return DefaultPermission.OP_ONLY;
   }

   // $FF: synthetic method
   private static AdminPermission[] $values() {
      return new AdminPermission[]{REGISTER, UNREGISTER, FORCE_LOGIN, CHANGE_PASSWORD, LAST_LOGIN, ACCOUNTS, GET_EMAIL, CHANGE_EMAIL, VIEW_TOTP_STATUS, DISABLE_TOTP, GET_IP, SEE_RECENT_PLAYERS, SPAWN, SET_SPAWN, FIRST_SPAWN, SET_FIRST_SPAWN, PURGE, PURGE_LAST_POSITION, PURGE_BANNED_PLAYERS, PURGE_PLAYER, SWITCH_ANTIBOT, CONVERTER, RELOAD, ANTIBOT_MESSAGES, UPDATE_MESSAGES, SEE_OTHER_ACCOUNTS, BACKUP};
   }
}
