package me.SuperRonanCraft.BetterRTP.references.messages;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileData;
import org.bukkit.command.CommandSender;

public class Message_RTP implements Message {
   public static Message_RTP msg = new Message_RTP();

   public static FileData getLang() {
      return BetterRTP.getInstance().getFiles().getLang();
   }

   public FileData lang() {
      return getLang();
   }

   public static void sms(CommandSender sendi, String msg) {
      Message.sms((Message)Message_RTP.msg, (CommandSender)sendi, (String)msg);
   }

   public static void sms(CommandSender sendi, String msg, Object placeholderInfo) {
      Message.sms(Message_RTP.msg, sendi, msg, (Object)placeholderInfo);
   }

   public static void sms(CommandSender sendi, String msg, List<Object> placeholderInfo) {
      Message.sms(Message_RTP.msg, sendi, msg, (List)placeholderInfo);
   }

   public static void sms(CommandSender sendi, List<String> msg, List<Object> placeholderInfo) {
      Message.sms((CommandSender)sendi, (List)msg, (Object)placeholderInfo);
   }

   public static String getPrefix() {
      return Message.getPrefix(msg);
   }
}
