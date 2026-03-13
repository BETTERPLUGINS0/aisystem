package me.casperge.realisticseasons.dump;

import java.io.File;
import java.util.Scanner;

public class LogGrabber {
   public static String grabLatestLog() {
      String var0 = "";

      try {
         File var1 = new File("logs/latest.log");
         if (var1.exists()) {
            Scanner var2 = new Scanner(var1);

            String var4;
            try {
               for(int var3 = 0; var2.hasNextLine() && var3 < 30000; var0 = var0 + var4 + "\n") {
                  ++var3;
                  var4 = var2.nextLine();
               }
            } finally {
               var2.close();
            }

            return var0;
         } else {
            return "Error occured loading log on client. File did not exist.";
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         return "Error occured loading log on client";
      }
   }
}
