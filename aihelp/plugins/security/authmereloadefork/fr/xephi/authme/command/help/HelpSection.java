package fr.xephi.authme.command.help;

public enum HelpSection {
   COMMAND("command"),
   SHORT_DESCRIPTION("description"),
   DETAILED_DESCRIPTION("detailedDescription"),
   ARGUMENTS("arguments"),
   ALTERNATIVES("alternatives"),
   PERMISSIONS("permissions"),
   CHILDREN("children");

   private static final String PREFIX = "section.";
   private final String key;

   private HelpSection(String param3) {
      this.key = "section." + key;
   }

   public String getKey() {
      return this.key;
   }

   public String getEntryKey() {
      return this.key.substring("section.".length());
   }

   // $FF: synthetic method
   private static HelpSection[] $values() {
      return new HelpSection[]{COMMAND, SHORT_DESCRIPTION, DETAILED_DESCRIPTION, ARGUMENTS, ALTERNATIVES, PERMISSIONS, CHILDREN};
   }
}
