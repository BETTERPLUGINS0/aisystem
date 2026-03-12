package fr.xephi.authme.permission;

public enum PlayerStatePermission implements PermissionNode {
   BYPASS_ANTIBOT("authme.bypassantibot", DefaultPermission.OP_ONLY),
   BYPASS_BUNGEE_SEND("authme.bypassbungeesend", DefaultPermission.NOT_ALLOWED),
   BYPASS_FORCE_SURVIVAL("authme.bypassforcesurvival", DefaultPermission.OP_ONLY),
   IS_VIP("authme.vip", DefaultPermission.NOT_ALLOWED),
   ALLOW_MULTIPLE_ACCOUNTS("authme.allowmultipleaccounts", DefaultPermission.OP_ONLY),
   BYPASS_PURGE("authme.bypasspurge", DefaultPermission.NOT_ALLOWED),
   BYPASS_COUNTRY_CHECK("authme.bypasscountrycheck", DefaultPermission.NOT_ALLOWED),
   ALLOW_CHAT_BEFORE_LOGIN("authme.allowchatbeforelogin", DefaultPermission.NOT_ALLOWED);

   private String node;
   private DefaultPermission defaultPermission;

   private PlayerStatePermission(String param3, DefaultPermission param4) {
      this.node = node;
      this.defaultPermission = defaultPermission;
   }

   public String getNode() {
      return this.node;
   }

   public DefaultPermission getDefaultPermission() {
      return this.defaultPermission;
   }

   // $FF: synthetic method
   private static PlayerStatePermission[] $values() {
      return new PlayerStatePermission[]{BYPASS_ANTIBOT, BYPASS_BUNGEE_SEND, BYPASS_FORCE_SURVIVAL, IS_VIP, ALLOW_MULTIPLE_ACCOUNTS, BYPASS_PURGE, BYPASS_COUNTRY_CHECK, ALLOW_CHAT_BEFORE_LOGIN};
   }
}
