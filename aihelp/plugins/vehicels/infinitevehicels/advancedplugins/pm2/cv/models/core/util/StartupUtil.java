package advancedplugins.pm2.cv.models.core.util;

import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.nms.NMSHandler;
import advancedplugins.pm2.cv.models.core.util.exception.UnknownVersionException;
import org.jetbrains.annotations.ApiStatus.Internal;

public class StartupUtil {
   @Internal
   public static NMSHandler findNMSHandler() {
      String var0 = ServerInfo.NMS_VERSION;
      byte var3 = -1;
      switch(var0.hashCode()) {
      case 1505564:
         if (var0.equals("1.21")) {
            var3 = 4;
         }
         break;
      case 1446847518:
         if (var0.equals("1.20.1")) {
            var3 = 0;
         }
         break;
      case 1446847521:
         if (var0.equals("1.20.4")) {
            var3 = 1;
         }
         break;
      case 1446847522:
         if (var0.equals("1.20.5")) {
            var3 = 2;
         }
         break;
      case 1446847523:
         if (var0.equals("1.20.6")) {
            var3 = 3;
         }
         break;
      case 1446848479:
         if (var0.equals("1.21.1")) {
            var3 = 5;
         }
         break;
      case 1446848481:
         if (var0.equals("1.21.3")) {
            var3 = 6;
         }
         break;
      case 1446848482:
         if (var0.equals("1.21.4")) {
            var3 = 7;
         }
         break;
      case 1446848483:
         if (var0.equals("1.21.5")) {
            var3 = 8;
         }
         break;
      case 1446848484:
         if (var0.equals("1.21.6")) {
            var3 = 9;
         }
         break;
      case 1446848485:
         if (var0.equals("1.21.7")) {
            var3 = 10;
         }
         break;
      case 1446848486:
         if (var0.equals("1.21.8")) {
            var3 = 11;
         }
         break;
      case 1446848487:
         if (var0.equals("1.21.9")) {
            var3 = 12;
         }
         break;
      case 1902629937:
         if (var0.equals("1.21.10")) {
            var3 = 13;
         }
         break;
      case 1902629938:
         if (var0.equals("1.21.11")) {
            var3 = 14;
         }
      }

      NMSHandler var10000;
      switch(var3) {
      case 0:
         var10000 = find("v1_20_R1.NMSHandler_v1_20_R1");
         break;
      case 1:
         var10000 = find("v1_20_R4.NMSHandler_v1_20_R4");
         break;
      case 2:
      case 3:
         var10000 = find("v1_20_R6.NMSHandler_v1_20_R6");
         break;
      case 4:
      case 5:
         var10000 = find("v1_21_R1.NMSHandler_v1_21_R1");
         break;
      case 6:
         var10000 = find("v1_21_R2.MinecraftProtocolAdapter");
         break;
      case 7:
         var10000 = find("v1_21_R3.MinecraftProtocolAdapter");
         break;
      case 8:
         var10000 = find("v1_21_R4.NMSHandler_v1_21_R4");
         break;
      case 9:
         var10000 = find("v1_21_R5.NMSHandler_v1_21_R5");
         break;
      case 10:
      case 11:
         var10000 = find("v1_21_R5_spigot.MinecraftProtocolAdapter");
         break;
      case 12:
      case 13:
         var10000 = find("v1_21_R7.MinecraftProtocolAdapter");
         break;
      case 14:
         var10000 = ServerInfo.IS_PAPER ? find("v1_21_R10P.MinecraftProtocolAdapter") : find("v1_21_R10.MinecraftProtocolAdapter");
         break;
      default:
         var10000 = null;
      }

      NMSHandler var1 = var10000;
      if (var1 == null) {
         String var10002 = ServerInfo.IS_FOLIA ? "Folia" : (ServerInfo.IS_PAPER ? "Paper" : "Spigot");
         throw new UnknownVersionException("Sorry but your server version is not currently supported! (" + var10002 + "-" + var0 + ")");
      } else {
         return var1;
      }
   }

   private static NMSHandler find(String var0) {
      String var1 = "advancedplugins.pm2.cv.models." + var0;

      try {
         Class var2 = Class.forName(var1);
         return (NMSHandler)var2.getDeclaredConstructor().newInstance();
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
