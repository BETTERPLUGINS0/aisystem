package com.volmit.iris.core.service;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisDirection;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawPieceConnector;
import com.volmit.iris.engine.object.IrisJigsawPool;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.function.Consumer2;
import com.volmit.iris.util.io.Converter;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.nbt.io.NBTUtil;
import com.volmit.iris.util.nbt.io.NamedTag;
import com.volmit.iris.util.nbt.mca.NBTWorld;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.IntTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Jigsaw;

public class ConversionSVC implements IrisService {
   private KList<Converter> converters;
   private File folder;

   public void onEnable() {
      this.folder = Iris.instance.getDataFolder(new String[]{"convert"});
      this.converters = new KList();
      J.s(() -> {
         J.attemptAsync(() -> {
         });
      }, 5);
   }

   public void onDisable() {
   }

   private String toPoolName(String poolReference) {
      return var1.split("\\Q:\\E")[1];
   }

   public void convertStructures(File in, File out, VolmitSender s) {
      KMap var4 = new KMap();
      KList var5 = new KList();
      AtomicInteger var6 = new AtomicInteger(0);
      AtomicInteger var7 = new AtomicInteger(0);
      File var8 = new File(var2.getAbsolutePath() + "/jigsaw-pools");
      var8.mkdirs();
      this.findAllNBT(var1, (var4x, var5x) -> {
         var6.getAndIncrement();
         if (var5.addIfMissing(var4x)) {
            String var6x = var1.toURI().relativize(var4x.toURI()).getPath();
            if (var6x.startsWith("/")) {
               var6x = var6x.substring(1);
            }

            if (var6x.endsWith("/")) {
               var6x = var6x.substring(0, var6x.length() - 1);
            }

            var4.put(var6x, new IrisJigsawPool());
         }

      });
      this.findAllNBT(var1, (var6x, var7x) -> {
         var7.getAndIncrement();
         String var8 = var1.toURI().relativize(var6x.toURI()).getPath();
         if (var8.startsWith("/")) {
            var8 = var8.substring(1);
         }

         if (var8.endsWith("/")) {
            var8 = var8.substring(0, var8.length() - 1);
         }

         IrisJigsawPool var9 = (IrisJigsawPool)var4.get(var8);
         String var10002 = var2.getAbsolutePath();
         File var10 = new File(var10002 + "/objects/" + var1.toURI().relativize(var6x.toURI()).getPath());
         var10002 = var2.getAbsolutePath();
         File var11 = new File(var10002 + "/jigsaw-pieces/" + var1.toURI().relativize(var6x.toURI()).getPath());
         var10.mkdirs();
         var11.mkdirs();

         try {
            NamedTag var12 = NBTUtil.read(var7x);
            CompoundTag var13 = (CompoundTag)var12.getTag();
            if (var13.containsKey("blocks") && var13.containsKey("palette") && var13.containsKey("size")) {
               String var10000 = var1.toURI().relativize(var6x.toURI()).getPath();
               String var14 = var10000 + var7x.getName().split("\\Q.\\E")[0];
               ListTag var15 = var13.getListTag("size");
               int var16 = ((IntTag)var15.get(0)).asInt();
               int var17 = ((IntTag)var15.get(1)).asInt();
               int var18 = ((IntTag)var15.get(2)).asInt();
               KList var19 = new KList();
               ListTag var20 = var13.getListTag("palette");

               for(int var21 = 0; var21 < var20.size(); ++var21) {
                  CompoundTag var22 = (CompoundTag)var20.get(var21);
                  var19.add((Object)NBTWorld.getBlockData(var22));
               }

               IrisJigsawPiece var42 = new IrisJigsawPiece();
               IrisObject var43 = new IrisObject(var16, var17, var18);
               ListTag var23 = var13.getListTag("blocks");

               for(int var24 = 0; var24 < var23.size(); ++var24) {
                  CompoundTag var25 = (CompoundTag)var23.get(var24);
                  ListTag var26 = var25.getListTag("pos");
                  int var27 = ((IntTag)var26.get(0)).asInt();
                  int var28 = ((IntTag)var26.get(1)).asInt();
                  int var29 = ((IntTag)var26.get(2)).asInt();
                  BlockData var30 = ((BlockData)var19.get(var25.getInt("state"))).clone();
                  if (var30.getMaterial().equals(Material.JIGSAW) && var25.containsKey("nbt")) {
                     String var10001 = var1.toURI().relativize(var6x.toURI()).getPath();
                     var42.setObject(var10001 + var7x.getName().split("\\Q.\\E")[0]);
                     IrisPosition var31 = new IrisPosition(var43.getSigned(var27, var28, var29));
                     CompoundTag var32 = var25.getCompoundTag("nbt");
                     CompoundTag var33 = new CompoundTag();
                     var33.putString("Name", var32.getString("final_state"));
                     BlockData var34 = var30.clone();
                     var30 = NBTWorld.getBlockData(var33);
                     String var35 = var32.getString("joint");
                     String var36 = var32.getString("pool");
                     String var37 = this.toPoolName(var36);
                     String var38 = var32.getString("name");
                     String var39 = var32.getString("target");
                     var4.computeIfAbsent(var37, (var0) -> {
                        return new IrisJigsawPool();
                     });
                     IrisJigsawPieceConnector var40 = new IrisJigsawPieceConnector();
                     var40.setName(var38);
                     var40.setTargetName(var39);
                     var40.setRotateConnector(false);
                     var40.setPosition(var31);
                     var40.getPools().add((Object)var37);
                     var40.setDirection(IrisDirection.getDirection(((Jigsaw)var34).getOrientation()));
                     if (var39.equals("minecraft:building_entrance")) {
                        var40.setInnerConnector(true);
                     }

                     var42.getConnectors().add((Object)var40);
                  }

                  if (!var30.getMaterial().equals(Material.STRUCTURE_VOID) && !var30.getMaterial().equals(Material.AIR)) {
                     var43.setUnsigned(var27, var28, var29, var30);
                  }
               }

               var9.getPieces().addIfMissing(var14);
               String[] var10004 = var7x.getName().split("\\Q.\\E");
               var43.write(new File(var10, var10004[0] + ".iob"));
               String[] var10003 = var7x.getName().split("\\Q.\\E");
               IO.writeAll(new File(var11, var10003[0] + ".json"), (Object)(new JSONObject((new Gson()).toJson(var42))).toString(4));
               var10000 = Form.pc((double)var7.get() / (double)var6.get(), 0);
               Iris.info("[Jigsaw]: (" + var10000 + ") Exported Piece: " + var14);
            }
         } catch (Throwable var41) {
            var41.printStackTrace();
            Iris.reportError(var41);
         }

      });
      Iterator var9 = var4.k().iterator();

      while(var9.hasNext()) {
         String var10 = (String)var9.next();

         try {
            IO.writeAll(new File(var8, var10 + ".json"), (Object)(new JSONObject((new Gson()).toJson(var4.get(var10)))).toString(4));
         } catch (IOException var12) {
            var12.printStackTrace();
            Iris.reportError(var12);
         }
      }

      Iris.info("Done! Exported " + Form.f(var6.get() * 2 + var4.size()) + " Files!");
   }

   public void findAllNBT(File path, Consumer2<File, File> inFile) {
      if (var1 != null) {
         if (var1.isFile() && var1.getName().endsWith(".nbt")) {
            var2.accept(var1.getParentFile(), var1);
         } else {
            File[] var3 = var1.listFiles();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               File var6 = var3[var5];
               if (var6.isDirectory()) {
                  this.findAllNBT(var6, var2);
               } else if (var6.isFile() && var6.getName().endsWith(".nbt")) {
                  var2.accept(var1, var6);
               }
            }

         }
      }
   }

   public void check(VolmitSender s) {
      int var2 = 0;
      Iris.instance.getDataFolder(new String[]{"convert"});
      File[] var3 = this.folder.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];
         Iterator var7 = this.converters.iterator();

         while(var7.hasNext()) {
            Converter var8 = (Converter)var7.next();
            if (var6.getName().endsWith("." + var8.getInExtension())) {
               File var9 = new File(this.folder, var6.getName().replaceAll("\\Q." + var8.getInExtension() + "\\E", "." + var8.getOutExtension()));
               ++var2;
               var8.convert(var6, var9);
               String var10001 = var6.getName();
               var1.sendMessage("Converted " + var10001 + " -> " + var9.getName());
            }
         }

         if (var6.isDirectory() && var6.getName().equals("structures")) {
            File var10 = new File(this.folder, "jigsaw");
            if (!var10.exists()) {
               var1.sendMessage("Converting NBT Structures into Iris Jigsaw Structures...");
               var10.mkdirs();
               J.a(() -> {
                  this.convertStructures(var6, var10, var1);
               });
            }
         }
      }

      var1.sendMessage("Converted " + var2 + " File" + (var2 == 1 ? "" : "s"));
   }
}
