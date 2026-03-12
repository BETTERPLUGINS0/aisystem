package me.SuperRonanCraft.BetterRTP.references.messages;

import me.SuperRonanCraft.BetterRTP.references.file.FileData;

public enum MessagesHelp implements MessageData {
   PREFIX("Prefix"),
   MAIN("Main"),
   BIOME("Biome"),
   EDIT("Edit"),
   HELP("Help"),
   INFO("Info"),
   PLAYER("Player"),
   RELOAD("Reload"),
   SETTINGS("Settings"),
   TEST("Test"),
   VERSION("Version"),
   WORLD("World"),
   LOCATION("Location");

   final String section;

   private MessagesHelp(String section) {
      this.section = section;
   }

   public String prefix() {
      return "Help.";
   }

   public FileData file() {
      return Message_RTP.getLang();
   }

   public String section() {
      return this.section;
   }

   // $FF: synthetic method
   private static MessagesHelp[] $values() {
      return new MessagesHelp[]{PREFIX, MAIN, BIOME, EDIT, HELP, INFO, PLAYER, RELOAD, SETTINGS, TEST, VERSION, WORLD, LOCATION};
   }
}
