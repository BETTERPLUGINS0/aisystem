package me.SuperRonanCraft.BetterRTP.references.messages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;

public enum MessagesCore {
   SUCCESS_PAID("Success.Paid"),
   SUCCESS_BYPASS("Success.Bypass"),
   SUCCESS_LOADING("Success.Loading"),
   SUCCESS_TELEPORT("Success.Teleport"),
   FAILED_NOTSAFE("Failed.NotSafe"),
   FAILED_PRICE("Failed.Price"),
   FAILED_HUNGER("Failed.Hunger"),
   OTHER_NOTSAFE("Other.NotSafe"),
   OTHER_SUCCESS("Other.Success"),
   OTHER_BIOME("Other.Biome"),
   NOTEXIST("NotExist"),
   RELOAD("Reload"),
   NOPERMISSION("NoPermission.Basic"),
   NOPERMISSION_WORLD("NoPermission.World"),
   DISABLED_WORLD("DisabledWorld"),
   COOLDOWN("Cooldown"),
   INVALID("Invalid"),
   NOTONLINE("NotOnline"),
   DELAY("Delay"),
   SIGN("Sign"),
   MOVED("Moved"),
   ALREADY("Already"),
   EDIT_ERROR("Edit.Error"),
   EDIT_SET("Edit.Set"),
   EDIT_REMOVE("Edit.Remove");

   final String section;
   private static final String pre = "Messages.";

   private MessagesCore(String section) {
      this.section = section;
   }

   public void send(CommandSender sendi) {
      Message_RTP.sms(sendi, Message_RTP.getLang().getString("Messages." + this.section));
   }

   public void send(CommandSender sendi, Object placeholderInfo) {
      Message_RTP.sms(sendi, Message_RTP.getLang().getString("Messages." + this.section), placeholderInfo);
   }

   public void send(CommandSender sendi, List<Object> placeholderInfo) {
      Message_RTP.sms(sendi, Message_RTP.getLang().getString("Messages." + this.section), placeholderInfo);
   }

   public String get(CommandSender p, Object placeholderInfo) {
      return Message.placeholder(p, Message_RTP.getLang().getString("Messages." + this.section), placeholderInfo);
   }

   public void send(CommandSender sendi, HashMap<String, String> placeholder_values) {
      String msg = Message_RTP.getLang().getString("Messages." + this.section);

      String ph;
      for(Iterator var4 = placeholder_values.values().iterator(); var4.hasNext(); msg = msg.replace(ph, (CharSequence)placeholder_values.get(ph))) {
         ph = (String)var4.next();
      }

      Message_RTP.sms(sendi, msg);
   }

   // $FF: synthetic method
   private static MessagesCore[] $values() {
      return new MessagesCore[]{SUCCESS_PAID, SUCCESS_BYPASS, SUCCESS_LOADING, SUCCESS_TELEPORT, FAILED_NOTSAFE, FAILED_PRICE, FAILED_HUNGER, OTHER_NOTSAFE, OTHER_SUCCESS, OTHER_BIOME, NOTEXIST, RELOAD, NOPERMISSION, NOPERMISSION_WORLD, DISABLED_WORLD, COOLDOWN, INVALID, NOTONLINE, DELAY, SIGN, MOVED, ALREADY, EDIT_ERROR, EDIT_SET, EDIT_REMOVE};
   }
}
