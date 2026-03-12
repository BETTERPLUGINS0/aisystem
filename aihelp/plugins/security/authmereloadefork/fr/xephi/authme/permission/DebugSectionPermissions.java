package fr.xephi.authme.permission;

public enum DebugSectionPermissions implements PermissionNode {
   DEBUG_COMMAND("authme.debug.command"),
   COUNTRY_LOOKUP("authme.debug.country"),
   DATA_STATISTICS("authme.debug.stats"),
   HAS_PERMISSION_CHECK("authme.debug.perm"),
   INPUT_VALIDATOR("authme.debug.valid"),
   LIMBO_PLAYER_VIEWER("authme.debug.limbo"),
   PERM_GROUPS("authme.debug.group"),
   PLAYER_AUTH_VIEWER("authme.debug.db"),
   MYSQL_DEFAULT_CHANGER("authme.debug.mysqldef"),
   SPAWN_LOCATION("authme.debug.spawn"),
   TEST_EMAIL("authme.debug.mail");

   private final String node;

   private DebugSectionPermissions(String param3) {
      this.node = node;
   }

   public String getNode() {
      return this.node;
   }

   public DefaultPermission getDefaultPermission() {
      return DefaultPermission.OP_ONLY;
   }

   // $FF: synthetic method
   private static DebugSectionPermissions[] $values() {
      return new DebugSectionPermissions[]{DEBUG_COMMAND, COUNTRY_LOOKUP, DATA_STATISTICS, HAS_PERMISSION_CHECK, INPUT_VALIDATOR, LIMBO_PLAYER_VIEWER, PERM_GROUPS, PLAYER_AUTH_VIEWER, MYSQL_DEFAULT_CHANGER, SPAWN_LOCATION, TEST_EMAIL};
   }
}
