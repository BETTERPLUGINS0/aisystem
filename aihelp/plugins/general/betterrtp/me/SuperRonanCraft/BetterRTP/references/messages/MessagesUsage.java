package me.SuperRonanCraft.BetterRTP.references.messages;

import me.SuperRonanCraft.BetterRTP.references.file.FileData;
import org.bukkit.command.CommandSender;

public enum MessagesUsage implements MessageData {
   RTP_OTHER("Player"),
   WORLD("World"),
   BIOME("Biome"),
   LOCATION("Location"),
   EDIT_LOCATION("Edit.Location"),
   EDIT_BASE("Edit.Base"),
   EDIT_DEFAULT("Edit.Default"),
   EDIT_WORLD("Edit.World"),
   EDIT_WORLDTYPE("Edit.Worldtype"),
   EDIT_OVERRIDE("Edit.Override"),
   EDIT_BLACKLISTEDBLLOCKS("Edit.BlacklistedBlocks"),
   EDIT_PERMISSIONGROUP("Edit.PermissionGroup");

   final String section;

   private MessagesUsage(String section) {
      this.section = section;
   }

   public void send(CommandSender sendi, Object placeholderInfo) {
      Message_RTP.sms(sendi, Message_RTP.getLang().getString(this.prefix() + this.section), placeholderInfo);
   }

   public String prefix() {
      return "Usage.";
   }

   public String section() {
      return this.section;
   }

   public FileData file() {
      return Message_RTP.getLang();
   }

   // $FF: synthetic method
   private static MessagesUsage[] $values() {
      return new MessagesUsage[]{RTP_OTHER, WORLD, BIOME, LOCATION, EDIT_LOCATION, EDIT_BASE, EDIT_DEFAULT, EDIT_WORLD, EDIT_WORLDTYPE, EDIT_OVERRIDE, EDIT_BLACKLISTEDBLLOCKS, EDIT_PERMISSIONGROUP};
   }
}
