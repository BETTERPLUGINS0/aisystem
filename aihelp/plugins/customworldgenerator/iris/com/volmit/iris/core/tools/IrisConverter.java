package com.volmit.iris.core.tools;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.nbt.io.NBTUtil;
import com.volmit.iris.util.nbt.io.NamedTag;
import com.volmit.iris.util.nbt.tag.ByteArrayTag;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.IntTag;
import com.volmit.iris.util.nbt.tag.ShortTag;
import com.volmit.iris.util.nbt.tag.Tag;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

public class IrisConverter {
   public static void convertSchematics(VolmitSender sender) {
      File var1 = Iris.instance.getDataFolder(new String[]{"convert"});
      FilenameFilter var2 = (var0x, var1x) -> {
         return var1x.endsWith(".schem");
      };
      File[] var3 = var1.listFiles(var2);
      if (var3 == null) {
         var0.sendMessage("No schematic files to convert found in " + var1.getAbsolutePath());
      } else {
         AtomicInteger var4 = new AtomicInteger(0);
         PrecisionStopwatch var5 = PrecisionStopwatch.start();
         ExecutorService var6 = Executors.newFixedThreadPool(1);
         var6.submit(() -> {
            File[] var5x = var3;
            int var6 = var3.length;

            String var10001;
            for(int var7 = 0; var7 < var6; ++var7) {
               File var8 = var5x[var7];

               try {
                  PrecisionStopwatch var9 = PrecisionStopwatch.start();
                  boolean var11 = false;

                  NamedTag var12;
                  try {
                     var12 = NBTUtil.read(var8);
                  } catch (IOException var33) {
                     Iris.info(String.valueOf(C.RED) + "Failed to read: " + var8.getName());
                     throw new RuntimeException(var33);
                  }

                  CompoundTag var13 = (CompoundTag)var12.getTag();
                  int var14 = resolveVersion(var13);
                  if (var14 != 2 && var14 != 3) {
                     String var10002 = String.valueOf(C.RED);
                     throw new RuntimeException(var10002 + "Unsupported schematic version: " + var14);
                  }

                  var13 = var14 == 3 ? (CompoundTag)var13.get("Schematic") : var13;
                  short var15 = (Short)((ShortTag)var13.get("Width")).getValue();
                  short var16 = (Short)((ShortTag)var13.get("Height")).getValue();
                  short var17 = (Short)((ShortTag)var13.get("Length")).getValue();
                  int var18 = -1;
                  int var19 = var15 * var16 * var17;
                  AtomicInteger var20 = new AtomicInteger(0);
                  String var10000;
                  if (var19 > 2000000) {
                     var11 = true;
                     var10000 = String.valueOf(C.GRAY);
                     Iris.info(var10000 + "Converting.. " + var8.getName() + " -> " + var8.getName().replace(".schem", ".iob"));
                     Iris.info(String.valueOf(C.GRAY) + "- It may take a while");
                     if (var0.isPlayer()) {
                        var18 = J.ar(() -> {
                           var0.sendProgress((double)var20.get() / (double)var19, "Converting");
                        }, 0);
                     }
                  }

                  var13 = var14 == 3 ? (CompoundTag)var13.get("Blocks") : var13;
                  CompoundTag var21 = (CompoundTag)var13.get("Palette");
                  HashMap var22 = new HashMap(var21.size(), 0.9F);
                  Iterator var23 = ((Map)var21.getValue()).entrySet().iterator();

                  int var28;
                  while(var23.hasNext()) {
                     Entry var24 = (Entry)var23.next();
                     String var25 = (String)var24.getKey();
                     BlockData var26 = Bukkit.createBlockData(var25);
                     Tag var27 = (Tag)var24.getValue();
                     var28 = (Integer)((IntTag)var27).getValue();
                     var22.put(var28, var26);
                  }

                  boolean var35 = var14 == 3 ? var13.getByteArrayTag("Data").length() < 128 : (Integer)((IntTag)var13.get("PaletteMax")).getValue() < 128;
                  ByteArrayTag var36 = var14 == 3 ? (ByteArrayTag)var13.get("Data") : (ByteArrayTag)var13.get("BlockData");
                  byte[] var37 = (byte[])var36.getValue();
                  DataInputStream var38 = new DataInputStream(new ByteArrayInputStream(var37));
                  IrisObject var10 = new IrisObject(var15, var16, var17);

                  for(int var39 = 0; var39 < var16; ++var39) {
                     for(var28 = 0; var28 < var17; ++var28) {
                        for(int var29 = 0; var29 < var15; ++var29) {
                           int var30 = var35 ? var38.read() & 255 : Varint.readUnsignedVarInt((DataInput)var38);
                           BlockData var31 = (BlockData)var22.get(var30);
                           if (!var31.getMaterial().isAir()) {
                              var10.setUnsigned(var29, var39, var28, var31);
                           }

                           var20.getAndAdd(1);
                        }
                     }
                  }

                  if (var18 != -1) {
                     J.car(var18);
                  }

                  try {
                     var10.shrinkwrap();
                     var10.write(new File(var1, var8.getName().replace(".schem", ".iob")));
                     var4.incrementAndGet();
                     if (var0.isPlayer()) {
                        if (var11) {
                           var10001 = String.valueOf(C.IRIS);
                           var0.sendMessage(var10001 + "Converted " + var8.getName() + " -> " + var8.getName().replace(".schem", ".iob") + " in " + Form.duration(var9.getMillis()));
                        } else {
                           var10001 = String.valueOf(C.IRIS);
                           var0.sendMessage(var10001 + "Converted " + var8.getName() + " -> " + var8.getName().replace(".schem", ".iob"));
                        }
                     }

                     if (var11) {
                        var10000 = String.valueOf(C.GRAY);
                        Iris.info(var10000 + "Converted " + var8.getName() + " -> " + var8.getName().replace(".schem", ".iob") + " in " + Form.duration(var9.getMillis()));
                     } else {
                        var10000 = String.valueOf(C.GRAY);
                        Iris.info(var10000 + "Converted " + var8.getName() + " -> " + var8.getName().replace(".schem", ".iob"));
                     }

                     FileUtils.delete(var8);
                  } catch (IOException var32) {
                     var10001 = String.valueOf(C.RED);
                     var0.sendMessage(var10001 + "Failed to save: " + var8.getName());
                     throw new IOException(var32);
                  }
               } catch (Exception var34) {
                  var10001 = String.valueOf(C.RED);
                  var0.sendMessage(var10001 + "Failed to convert: " + var8.getName());
                  var34.printStackTrace();
               }
            }

            var5.end();
            if (var4.get() != 0) {
               var10001 = String.valueOf(C.GRAY);
               var0.sendMessage(var10001 + "Converted: " + var4.get() + " in " + Form.duration(var5.getMillis()));
            }

            if (var4.get() < var3.length) {
               var0.sendMessage(String.valueOf(C.RED) + "Some schematics failed to convert. Check the console for details.");
            }

         });
      }
   }

   private static int resolveVersion(CompoundTag compound) {
      try {
         IntTag var1 = var0.getIntTag("Version");
         if (var1 != null) {
            return (Integer)var1.getValue();
         } else {
            CompoundTag var2 = (CompoundTag)var0.get("Schematic");
            return (Integer)var2.getIntTag("Version").getValue();
         }
      } catch (NullPointerException var3) {
         throw new Exception("Cannot resolve schematic version", var3);
      }
   }
}
