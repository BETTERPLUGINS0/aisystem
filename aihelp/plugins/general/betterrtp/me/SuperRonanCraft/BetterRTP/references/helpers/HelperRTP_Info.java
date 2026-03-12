package me.SuperRonanCraft.BetterRTP.references.helpers;

import java.util.ArrayList;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

public class HelperRTP_Info {
   public static List<String> getBiomes(String[] args, int start, CommandSender sendi) {
      List<String> biomes = new ArrayList();
      boolean error_sent = false;
      if (PermissionNode.BIOME.check(sendi)) {
         for(int i = start; i < args.length; ++i) {
            String str = args[i];

            try {
               biomes.add(Biome.valueOf(str.replaceAll(",", "").toUpperCase()).name());
            } catch (Exception var8) {
               if (!error_sent) {
                  MessagesCore.OTHER_BIOME.send(sendi, (Object)str);
                  error_sent = true;
               }
            }
         }
      }

      return biomes;
   }

   public static void addBiomes(List<String> list, String[] args) {
      try {
         Biome[] var2 = Biome.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Biome b = var2[var4];
            if (b.name().toUpperCase().replaceAll("minecraft:", "").startsWith(args[args.length - 1].toUpperCase())) {
               list.add(b.name().replaceAll("minecraft:", ""));
            }
         }
      } catch (NoSuchMethodError var6) {
      }

   }
}
