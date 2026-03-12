package fr.xephi.authme.command.help;

public enum HelpMessage {
   HEADER("header"),
   OPTIONAL("optional"),
   HAS_PERMISSION("hasPermission"),
   NO_PERMISSION("noPermission"),
   DEFAULT("default"),
   RESULT("result");

   private static final String PREFIX = "common.";
   private final String key;

   private HelpMessage(String param3) {
      this.key = "common." + key;
   }

   public String getKey() {
      return this.key;
   }

   public String getEntryKey() {
      return this.key.substring("common.".length());
   }

   // $FF: synthetic method
   private static HelpMessage[] $values() {
      return new HelpMessage[]{HEADER, OPTIONAL, HAS_PERMISSION, NO_PERMISSION, DEFAULT, RESULT};
   }
}
