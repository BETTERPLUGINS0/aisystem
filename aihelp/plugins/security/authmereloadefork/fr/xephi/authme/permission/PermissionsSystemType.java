package fr.xephi.authme.permission;

public enum PermissionsSystemType {
   LUCK_PERMS("LuckPerms", "LuckPerms"),
   PERMISSIONS_EX("PermissionsEx", "PermissionsEx"),
   Z_PERMISSIONS("zPermissions", "zPermissions"),
   VAULT("Vault", "Vault");

   private String displayName;
   private String pluginName;

   private PermissionsSystemType(String param3, String param4) {
      this.displayName = displayName;
      this.pluginName = pluginName;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getPluginName() {
      return this.pluginName;
   }

   public String toString() {
      return this.getDisplayName();
   }

   public static boolean isPermissionSystem(String name) {
      PermissionsSystemType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PermissionsSystemType permissionsSystemType = var1[var3];
         if (permissionsSystemType.pluginName.equals(name)) {
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   private static PermissionsSystemType[] $values() {
      return new PermissionsSystemType[]{LUCK_PERMS, PERMISSIONS_EX, Z_PERMISSIONS, VAULT};
   }
}
