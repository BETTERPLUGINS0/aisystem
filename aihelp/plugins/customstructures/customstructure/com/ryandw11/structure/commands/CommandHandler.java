package com.ryandw11.structure.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
   private final Map<List<String>, SubCommand> commandMap = new HashMap();

   public void registerCommand(String s, SubCommand subCommand) {
      if (this.commandMap.containsKey(Collections.singletonList(s.toLowerCase()))) {
         throw new IllegalArgumentException("Command already exists!");
      } else {
         this.commandMap.put(Collections.singletonList(s.toLowerCase()), subCommand);
      }
   }

   public void registerCommand(SubCommand subCommand, String... args) {
      List<String> list = new ArrayList(Arrays.asList(args));
      List<String> list = (List)list.stream().map(String::toLowerCase).collect(Collectors.toList());
      if (this.commandMap.containsKey(list)) {
         throw new IllegalArgumentException("Command already exists!");
      } else {
         this.commandMap.put(list, subCommand);
      }
   }

   public boolean handleCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length == 0) {
         return false;
      } else {
         Iterator var5 = this.commandMap.entrySet().iterator();

         Entry entry;
         do {
            if (!var5.hasNext()) {
               throw new IllegalArgumentException("Invalid command");
            }

            entry = (Entry)var5.next();
         } while(!((List)entry.getKey()).contains(args[0].toLowerCase()));

         String[] newArgs = new String[args.length - 1];
         if (newArgs.length > 0) {
            System.arraycopy(args, 1, newArgs, 0, newArgs.length);
         }

         return ((SubCommand)entry.getValue()).subCommand(sender, cmd, s, newArgs);
      }
   }
}
