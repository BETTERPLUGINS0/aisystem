package me.SuperRonanCraft.BetterRTP.references.messages;

import com.google.common.collect.ImmutableCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import me.SuperRonanCraft.BetterRTP.references.file.FileData;
import me.SuperRonanCraft.BetterRTP.references.messages.placeholder.PlaceholderAnalyzer;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public interface Message {
   FileData lang();

   static void sms(Message messenger, CommandSender sendi, String msg) {
      if (!msg.isEmpty()) {
         AsyncHandler.sync(() -> {
            sendi.sendMessage(placeholder(sendi, getPrefix(messenger) + msg));
         });
      }

   }

   static void sms(Message messenger, CommandSender sendi, String msg, Object placeholderInfo) {
      if (!msg.isEmpty()) {
         AsyncHandler.sync(() -> {
            sendi.sendMessage((String)Objects.requireNonNull(placeholder(sendi, getPrefix(messenger) + msg, placeholderInfo)));
         });
      }

   }

   static void sms(Message messenger, CommandSender sendi, String msg, List<Object> placeholderInfo) {
      if (!msg.isEmpty()) {
         AsyncHandler.sync(() -> {
            sendi.sendMessage(placeholder(sendi, getPrefix(messenger) + msg, placeholderInfo));
         });
      }

   }

   static void sms(CommandSender sendi, List<String> msg, Object placeholderInfo) {
      if (msg != null && !msg.isEmpty()) {
         AsyncHandler.sync(() -> {
            msg.forEach((str) -> {
               msg.set(msg.indexOf(str), placeholder(sendi, str, placeholderInfo));
            });
            sendi.sendMessage((String[])msg.toArray(new String[0]));
         });
      }

   }

   static String getPrefix(Message messenger) {
      return messenger.lang().getString("Messages.Prefix");
   }

   static List<String> placeholder(@Nullable CommandSender p, List<String> str, Object info) {
      if (str instanceof ImmutableCollection) {
         return str;
      } else {
         for(int i = 0; i < str.size(); ++i) {
            String s = placeholder(p, (String)str.get(i), info);
            if (s != null) {
               str.set(i, s);
            } else {
               str.remove(i);
               --i;
            }
         }

         return str;
      }
   }

   @Nullable
   static String placeholder(@Nullable CommandSender p, String str, @Nullable Object info) {
      if (info instanceof Collection) {
         str = placeholder(p, str, Collections.unmodifiableList((List)info));
      } else if (str != null) {
         str = PlaceholderAnalyzer.applyPlaceholders(p, str, info);
      }

      return str != null ? color(str) : null;
   }

   static String placeholder(@Nullable CommandSender p, String str, @NonNull List<Object> info) {
      if (info == null) {
         throw new NullPointerException("info is marked non-null but is null");
      } else {
         Object obj;
         for(Iterator var3 = info.iterator(); var3.hasNext(); str = placeholder(p, str, obj)) {
            obj = var3.next();
         }

         return str;
      }
   }

   static String placeholder(@Nullable CommandSender p, String str) {
      if (str != null) {
         str = PlaceholderAnalyzer.applyPlaceholders(p, str, (Object)null);
      }

      return str != null ? color(str) : null;
   }

   static String color(String str) {
      return translateHexColorCodes(str);
   }

   static String translateHexColorCodes(String message) {
      Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

      for(Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
         String hexCode = message.substring(matcher.start(), matcher.end());
         String replaceSharp = hexCode.replace('#', 'x');
         char[] ch = replaceSharp.toCharArray();
         StringBuilder builder = new StringBuilder("");
         char[] var7 = ch;
         int var8 = ch.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            char c = var7[var9];
            builder.append("&").append(c);
         }

         message = message.replace(hexCode, builder.toString());
      }

      return ChatColor.translateAlternateColorCodes('&', message);
   }
}
