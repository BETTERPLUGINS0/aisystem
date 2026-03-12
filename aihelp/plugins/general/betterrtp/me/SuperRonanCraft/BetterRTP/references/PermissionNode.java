package me.SuperRonanCraft.BetterRTP.references;

public enum PermissionNode implements PermissionCheck {
   ADMIN("admin"),
   USE("use"),
   BYPASS_ECONOMY("bypass.economy"),
   BYPASS_HUNGER("bypass.hunger"),
   BYPASS_COOLDOWN("bypass.cooldown"),
   BYPASS_DELAY("bypass.delay"),
   BYPASS_LOCATION("bypass.location"),
   RELOAD("reload"),
   SETTINGS("settings"),
   INFO("info"),
   UPDATER("updater"),
   RTP_OTHER("player"),
   BIOME("biome"),
   WORLD("world"),
   SIGN_CREATE("sign"),
   VERSION("version"),
   EDIT("edit"),
   LOCATION("location"),
   DEVELOPER("DEVELOPER_PERM");

   private final String node;

   private PermissionNode(String node) {
      this.node = PermissionCheck.getPrefix() + node;
   }

   public boolean isDev() {
      return this == DEVELOPER;
   }

   public String getNode() {
      return this.node;
   }

   // $FF: synthetic method
   private static PermissionNode[] $values() {
      return new PermissionNode[]{ADMIN, USE, BYPASS_ECONOMY, BYPASS_HUNGER, BYPASS_COOLDOWN, BYPASS_DELAY, BYPASS_LOCATION, RELOAD, SETTINGS, INFO, UPDATER, RTP_OTHER, BIOME, WORLD, SIGN_CREATE, VERSION, EDIT, LOCATION, DEVELOPER};
   }
}
