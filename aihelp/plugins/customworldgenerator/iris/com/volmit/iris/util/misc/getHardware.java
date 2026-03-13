package com.volmit.iris.util.misc;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.Display;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

public class getHardware {
   public static String getServerOS() {
      SystemInfo var0 = new SystemInfo();
      OperatingSystem var1 = var0.getOperatingSystem();
      return var1.toString();
   }

   public static long getProcessMemory() {
      long var0 = Runtime.getRuntime().maxMemory() / 1048576L;
      return var0;
   }

   public static long getProcessUsedMemory() {
      Runtime var0 = Runtime.getRuntime();
      long var1 = var0.totalMemory();
      long var3 = var0.freeMemory();
      long var5 = var1 - var3;
      return var5 / 1048576L;
   }

   public static long getAvailableProcessMemory() {
      long var0 = getProcessMemory() - getProcessUsedMemory();
      return var0;
   }

   public static String getCPUModel() {
      try {
         SystemInfo var0 = new SystemInfo();
         CentralProcessor var1 = var0.getHardware().getProcessor();
         String var2 = var1.getProcessorIdentifier().getName();
         return var2.isEmpty() ? "Unknown CPU Model" : var2;
      } catch (Exception var3) {
         var3.printStackTrace();
         return "Unknown CPU Model";
      }
   }

   public static KList<String> getSensors() {
      try {
         KList var0 = new KList();
         SystemInfo var1 = new SystemInfo();
         var0.add((Object)("CPU Temperature: " + var1.getHardware().getSensors().getCpuTemperature()));
         var0.add((Object)("CPU Voltage: " + var1.getHardware().getSensors().getCpuTemperature()));
         var0.add((Object)("Fan Speeds: " + Arrays.toString(var1.getHardware().getSensors().getFanSpeeds())));
         return var0.copy();
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static KList<String> getGraphicsCards() {
      try {
         KList var0 = new KList();
         SystemInfo var1 = new SystemInfo();
         Iterator var2 = var1.getHardware().getGraphicsCards().iterator();

         while(var2.hasNext()) {
            GraphicsCard var3 = (GraphicsCard)var2.next();
            String var10001 = String.valueOf(C.BLUE);
            var0.add((Object)(var10001 + "Gpu Model: " + String.valueOf(C.GRAY) + var3.getName()));
            var10001 = String.valueOf(C.GRAY);
            var0.add((Object)("- vRam Size: " + var10001 + Form.memSize(var3.getVRam())));
            var10001 = String.valueOf(C.GRAY);
            var0.add((Object)("- Vendor: " + var10001 + var3.getVendor()));
         }

         return var0.copy();
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static KList<String> getDisk() {
      try {
         KList var0 = new KList();
         SystemInfo var1 = new SystemInfo();
         List var2 = var1.getHardware().getDiskStores();
         OperatingSystem var3 = var1.getOperatingSystem();
         List var4 = var3.getFileSystem().getFileStores();
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            HWDiskStore var6 = (HWDiskStore)var5.next();
            String var10001 = String.valueOf(C.BLUE);
            var0.add((Object)(var10001 + "Disk: " + var6.getName()));
            var0.add((Object)("- Model: " + var6.getModel()));
            var0.add((Object)("Partitions: " + String.valueOf(var6.getPartitions())));
            Iterator var7 = var4.iterator();

            while(var7.hasNext()) {
               OSFileStore var8 = (OSFileStore)var7.next();
               var10001 = String.valueOf(C.BLUE);
               var0.add((Object)(var10001 + "- Name: " + var8.getName()));
               var0.add((Object)(" - Description: " + var8.getDescription()));
               var0.add((Object)(" - Total Space: " + Form.memSize(var8.getTotalSpace())));
               var0.add((Object)(" - Free Space: " + Form.memSize(var8.getFreeSpace())));
               var0.add((Object)(" - Mount: " + var8.getMount()));
               var0.add((Object)(" - Label: " + var8.getLabel()));
            }

            var10001 = String.valueOf(C.DARK_GRAY);
            var0.add((Object)(var10001 + "-=" + String.valueOf(C.BLUE) + " Since Boot " + String.valueOf(C.DARK_GRAY) + "=- "));
            var0.add((Object)("- Total Reads: " + Form.memSize(var6.getReadBytes())));
            var0.add((Object)("- Total Writes: " + Form.memSize(var6.getWriteBytes())));
         }

         if (var0.isEmpty()) {
            var0.add((Object)"Failed to get disks.");
         }

         return var0.copy();
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   public static KList<String> getPowerSources() {
      try {
         KList var0 = new KList();
         SystemInfo var1 = new SystemInfo();
         List var2 = var1.getHardware().getPowerSources();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            PowerSource var4 = (PowerSource)var3.next();
            String var10001 = String.valueOf(C.BLUE);
            var0.add((Object)(var10001 + "- Name: " + var4.getName()));
            var0.add((Object)("- RemainingCapacityPercent: " + var4.getRemainingCapacityPercent()));
            var0.add((Object)("- Power Usage Rate: " + var4.getPowerUsageRate()));
            var0.add((Object)("- Power OnLine: " + var4.isPowerOnLine()));
            var0.add((Object)("- Capacity Units: " + String.valueOf(var4.getCapacityUnits())));
            var0.add((Object)("- Cycle Count: " + var4.getCycleCount()));
         }

         if (var0.isEmpty()) {
            var0.add((Object)"No PowerSources.");
         }

         return var0.copy();
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static KList<String> getEDID() {
      try {
         KList var0 = new KList();
         SystemInfo var1 = new SystemInfo();
         HardwareAbstractionLayer var2 = var1.getHardware();
         List var3 = var2.getDisplays();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Display var5 = (Display)var4.next();
            var0.add((Object)("Display: " + String.valueOf(var5.getEdid())));
         }

         if (!var0.isEmpty()) {
            var0.add((Object)"No displays");
         }

         return var0.copy();
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public static KList<String> getInterfaces() {
      try {
         KList var0 = new KList();
         Enumeration var1 = NetworkInterface.getNetworkInterfaces();
         Iterator var2 = Collections.list(var1).iterator();
         if (var2.hasNext()) {
            NetworkInterface var3 = (NetworkInterface)var2.next();
            var0.add((Object[])(String.valueOf(C.BLUE) + "Display Name: %s", var3.getDisplayName()));
            Enumeration var4 = var3.getInetAddresses();
            Iterator var5 = Collections.list(var4).iterator();

            while(var5.hasNext()) {
               InetAddress var6 = (InetAddress)var5.next();
               var0.add((Object[])("IP: %s", var6.getHostAddress()));
            }

            return var0.copy();
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return null;
   }
}
