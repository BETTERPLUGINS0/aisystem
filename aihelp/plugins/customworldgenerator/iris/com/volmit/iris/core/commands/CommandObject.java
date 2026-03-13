package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.WorldEditLink;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.service.ObjectSVC;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.core.service.WandSVC;
import com.volmit.iris.core.tools.IrisConverter;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisObjectPlacementScaleInterpolator;
import com.volmit.iris.engine.object.IrisObjectRotation;
import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.util.data.Cuboid;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.data.VectorMap;
import com.volmit.iris.util.data.registry.Materials;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.decree.specialhandlers.ObjectHandler;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.Direction;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@Decree(
   name = "object",
   aliases = {"o"},
   origin = DecreeOrigin.PLAYER,
   studio = true,
   description = "Iris object manipulation"
)
public class CommandObject implements DecreeExecutor {
   private static final Set<Material> skipBlocks;

   public static IObjectPlacer createPlacer(World world, Map<Block, BlockData> futureBlockChanges) {
      return new IObjectPlacer() {
         public int getHighest(int x, int z, IrisData data) {
            return var0.getHighestBlockYAt(var1x, var2);
         }

         public int getHighest(int x, int z, IrisData data, boolean ignoreFluid) {
            return var0.getHighestBlockYAt(var1x, var2, var4 ? HeightMap.OCEAN_FLOOR : HeightMap.MOTION_BLOCKING);
         }

         public void set(int x, int y, int z, BlockData d) {
            Block var5 = var0.getBlockAt(var1x, var2, var3);
            if (var2 > var0.getMinHeight() && var5.getType() != Material.BEDROCK) {
               var1.put(var5, var5.getBlockData());
               if (var4 instanceof IrisCustomData) {
                  IrisCustomData var6 = (IrisCustomData)var4;
                  var5.setBlockData(var6.getBase(), false);
                  Iris.warn("Tried to place custom block at " + var1x + ", " + var2 + ", " + var3 + " which is not supported!");
               } else {
                  var5.setBlockData(var4, false);
               }

            }
         }

         public BlockData get(int x, int y, int z) {
            return var0.getBlockAt(var1x, var2, var3).getBlockData();
         }

         public boolean isPreventingDecay() {
            return false;
         }

         public boolean isCarved(int x, int y, int z) {
            return false;
         }

         public boolean isSolid(int x, int y, int z) {
            return var0.getBlockAt(var1x, var2, var3).getType().isSolid();
         }

         public boolean isUnderwater(int x, int z) {
            return false;
         }

         public int getFluidHeight() {
            return 63;
         }

         public boolean isDebugSmartBore() {
            return false;
         }

         public void setTile(int xx, int yy, int zz, TileData tile) {
            var4.toBukkitTry(var0.getBlockAt(var1x, var2, var3));
         }

         public <T> void setData(int xx, int yy, int zz, T data) {
         }

         public <T> T getData(int xx, int yy, int zz, Class<T> t) {
            return null;
         }

         public Engine getEngine() {
            return null;
         }
      };
   }

   @Decree(
      description = "Check the composition of an object"
   )
   public void analyze(@Param(description = "The object to analyze",customHandler = ObjectHandler.class) String object) {
      IrisObject var2 = IrisData.loadAnyObject(var1, this.data());
      VolmitSender var10000 = this.sender();
      int var10001 = var2.getW();
      var10000.sendMessage("Object Size: " + var10001 + " * " + var2.getH() + " * " + var2.getD());
      this.sender().sendMessage("Blocks Used: " + NumberFormat.getIntegerInstance().format((long)var2.getBlocks().size()));
      VectorMap.ValueIterator var3 = var2.getBlocks().values();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      HashMap var6 = new HashMap();

      while(var3.hasNext()) {
         BlockData var7 = (BlockData)var3.next();
         if (!var5.containsKey(var7)) {
            var5.put(var7, 1);
         } else {
            var5.put(var7, (Integer)var5.get(var7) + 1);
         }

         if (!var6.containsKey(var7.getMaterial())) {
            var6.put(var7.getMaterial(), 1);
            var4.put(var7.getMaterial(), new HashSet());
            ((Set)var4.get(var7.getMaterial())).add(var7);
         } else {
            var6.put(var7.getMaterial(), (Integer)var6.get(var7.getMaterial()) + 1);
            ((Set)var4.get(var7.getMaterial())).add(var7);
         }
      }

      List var17 = var5.keySet().stream().map(BlockData::getMaterial).sorted().toList();
      Objects.requireNonNull(var6);
      TreeSet var8 = new TreeSet(Comparator.comparingInt(var6::get).reversed());
      var8.addAll(var17);
      this.sender().sendMessage("== Blocks in object ==");
      int var9 = 0;
      Iterator var10 = var8.iterator();

      do {
         if (!var10.hasNext()) {
            return;
         }

         Material var11 = (Material)var10.next();
         int var12 = (Integer)var6.get(var11);
         ArrayList var13 = new ArrayList((Collection)var4.get(var11));
         Objects.requireNonNull(var5);
         var13.sort(Comparator.comparingInt(var5::get).reversed());
         BlockData var14 = (BlockData)var13.get(0);
         int var15 = (Integer)var5.get(var14);
         String var18 = var11.toString();
         String var16 = " - " + var18 + "*" + var12;
         if (var14.getAsString(true).contains("[")) {
            String var19 = var14.getAsString(true).split("\\[")[1];
            String var10003 = String.valueOf(ChatColor.GREEN);
            var19 = var19.replaceAll("true", var10003 + "true" + String.valueOf(ChatColor.GRAY));
            var10003 = String.valueOf(ChatColor.RED);
            var16 = var16 + " --> [" + var19.replaceAll("false", var10003 + "false" + String.valueOf(ChatColor.GRAY)) + "*" + var15;
         }

         this.sender().sendMessage(var16);
         ++var9;
      } while(var9 < 10);

      this.sender().sendMessage("  + " + (var8.size() - var9) + " other block types");
   }

   @Decree(
      description = "Shrink an object to its minimum size"
   )
   public void shrink(@Param(description = "The object to shrink",customHandler = ObjectHandler.class) String object) {
      IrisObject var2 = IrisData.loadAnyObject(var1, this.data());
      VolmitSender var10000 = this.sender();
      int var10001 = var2.getW();
      var10000.sendMessage("Current Object Size: " + var10001 + " * " + var2.getH() + " * " + var2.getD());
      var2.shrinkwrap();
      var10000 = this.sender();
      var10001 = var2.getW();
      var10000.sendMessage("New Object Size: " + var10001 + " * " + var2.getH() + " * " + var2.getD());

      try {
         var2.write(var2.getLoadFile());
      } catch (IOException var4) {
         var10000 = this.sender();
         String var5 = String.valueOf(var2.getLoadFile());
         var10000.sendMessage("Failed to save object " + var5 + ": " + var4.getMessage());
         var4.printStackTrace();
      }

   }

   @Decree(
      description = "Convert .schem files in the 'convert' folder to .iob files."
   )
   public void convert() {
      try {
         IrisConverter.convertSchematics(this.sender());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   @Decree(
      description = "Get a powder that reveals objects",
      studio = true,
      aliases = {"d"}
   )
   public void dust() {
      this.player().getInventory().addItem(new ItemStack[]{WandSVC.createDust()});
      this.sender().playSound(Sound.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 1.0F, 1.5F);
   }

   @Decree(
      description = "Contract a selection based on your looking direction",
      aliases = {"-"}
   )
   public void contract(@Param(description = "The amount to inset by",defaultValue = "1") int amount) {
      if (!WandSVC.isHoldingWand(this.player())) {
         this.sender().sendMessage("Hold your wand.");
      } else {
         Location[] var2 = WandSVC.getCuboid(this.player());
         if (var2 != null && var2[0] != null && var2[1] != null) {
            Location var3 = var2[0].clone();
            Location var4 = var2[1].clone();
            Cuboid var5 = new Cuboid(var3, var4);
            Direction var6 = Direction.closest(this.player().getLocation().getDirection()).reverse();

            assert var6 != null;

            var5 = var5.expand(var6, -var1);
            var2[0] = var5.getLowerNE();
            var2[1] = var5.getUpperSW();
            this.player().getInventory().setItemInMainHand(WandSVC.createWand(var2[0], var2[1]));
            this.player().updateInventory();
            this.sender().playSound(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1.0F, 0.55F);
         } else {
            this.sender().sendMessage("No area selected.");
         }
      }
   }

   @Decree(
      description = "Set point 1 to look",
      aliases = {"p1"}
   )
   public void position1(@Param(description = "Whether to use your current position, or where you look",defaultValue = "true") boolean here) {
      if (!WandSVC.isHoldingWand(this.player())) {
         this.sender().sendMessage("Ready your Wand.");
      } else {
         if (WandSVC.isHoldingWand(this.player())) {
            Location[] var2 = WandSVC.getCuboid(this.player());
            if (var2 == null) {
               return;
            }

            if (!var1) {
               var2[1] = this.player().getTargetBlock((Set)null, 256).getLocation().clone();
            } else {
               var2[1] = this.player().getLocation().getBlock().getLocation().clone().add(0.0D, -1.0D, 0.0D);
            }

            this.player().setItemInHand(WandSVC.createWand(var2[0], var2[1]));
         }

      }
   }

   @Decree(
      description = "Set point 2 to look",
      aliases = {"p2"}
   )
   public void position2(@Param(description = "Whether to use your current position, or where you look",defaultValue = "true") boolean here) {
      if (!WandSVC.isHoldingWand(this.player())) {
         this.sender().sendMessage("Ready your Wand.");
      } else {
         if (WandSVC.isHoldingIrisWand(this.player())) {
            Location[] var2 = WandSVC.getCuboid(this.player());
            if (var2 == null) {
               return;
            }

            if (!var1) {
               var2[0] = this.player().getTargetBlock((Set)null, 256).getLocation().clone();
            } else {
               var2[0] = this.player().getLocation().getBlock().getLocation().clone().add(0.0D, -1.0D, 0.0D);
            }

            this.player().setItemInHand(WandSVC.createWand(var2[0], var2[1]));
         }

      }
   }

   @Decree(
      description = "Paste an object",
      sync = true
   )
   public void paste(@Param(description = "The object to paste",customHandler = ObjectHandler.class) String object, @Param(description = "Whether or not to edit the object (need to hold wand)",defaultValue = "false") boolean edit, @Param(description = "The amount of degrees to rotate by",defaultValue = "0") int rotate, @Param(description = "The factor by which to scale the object placement",defaultValue = "1") double scale) {
      IrisObject var6 = IrisData.loadAnyObject(var1, this.data());
      double var7 = Double.max(10.0D - (double)var6.getBlocks().size() / 10000.0D, 1.0D);
      VolmitSender var10000;
      String var10001;
      if (var4 > var7) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.YELLOW);
         var10000.sendMessage(var10001 + "Indicated scale exceeds maximum. Downscaled to maximum: " + var7);
         var4 = var7;
      }

      this.sender().playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.5F);
      IrisObjectPlacement var9 = new IrisObjectPlacement();
      var9.setRotation(IrisObjectRotation.of(0.0D, (double)var3, 0.0D));
      ItemStack var10 = this.player().getInventory().getItemInMainHand();
      Location var11 = this.player().getTargetBlock(skipBlocks, 256).getLocation().clone().add(0.0D, 1.0D, 0.0D);
      HashMap var12 = new HashMap();
      if (var4 != 1.0D) {
         var6 = var6.scaled(var4, IrisObjectPlacementScaleInterpolator.TRICUBIC);
      }

      var6.place(var11.getBlockX(), var11.getBlockY() + (int)var6.getCenter().getY(), var11.getBlockZ(), createPlacer(var11.getWorld(), var12), var9, new RNG(), (IrisData)null);
      ((ObjectSVC)Iris.service(ObjectSVC.class)).addChanges(var12);
      if (var2) {
         ItemStack var13 = WandSVC.createWand(var11.clone().subtract(var6.getCenter()).add((double)(var6.getW() - 1), (double)var6.getH() + var6.getCenter().clone().getY() - 1.0D, (double)(var6.getD() - 1)), var11.clone().subtract(var6.getCenter().clone().setY(0)));
         if (WandSVC.isWand(var10)) {
            this.player().getInventory().setItemInMainHand(var13);
            this.sender().sendMessage("Updated wand for objects/" + var6.getLoadKey() + ".iob ");
         } else {
            int var14 = WandSVC.findWand(this.player().getInventory());
            if (var14 == -1) {
               this.player().getInventory().addItem(new ItemStack[]{var13});
               this.sender().sendMessage("Given new wand for objects/" + var6.getLoadKey() + ".iob ");
            } else {
               this.player().getInventory().setItem(var14, var13);
               this.sender().sendMessage("Updated wand for objects/" + var6.getLoadKey() + ".iob ");
            }
         }
      } else {
         var10000 = this.sender();
         var10001 = String.valueOf(C.IRIS);
         var10000.sendMessage(var10001 + "Placed " + var1);
      }

   }

   @Decree(
      description = "Save an object"
   )
   public void save(@Param(description = "The dimension to store the object in",contextual = true) IrisDimension dimension, @Param(description = "The file to store it in, can use / for subfolders") String name, @Param(description = "Overwrite existing object files",defaultValue = "false",aliases = {"force"}) boolean overwrite, @Param(description = "Use legacy TileState serialization if possible",defaultValue = "true") boolean legacy) {
      IrisObject var5 = WandSVC.createSchematic(this.player(), var4);
      if (var5 == null) {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "You need to hold your wand!");
      } else {
         File var6 = ((StudioSVC)Iris.service(StudioSVC.class)).getWorkspaceFile(var1.getLoadKey(), "objects", var2 + ".iob");
         if (var6.exists() && !var3) {
            this.sender().sendMessage(String.valueOf(C.RED) + "File already exists. Set overwrite=true to overwrite it.");
         } else {
            VolmitSender var10000;
            String var10001;
            try {
               var5.write(var6, this.sender());
            } catch (IOException var8) {
               var10000 = this.sender();
               var10001 = String.valueOf(C.RED);
               var10000.sendMessage(var10001 + "Failed to save object because of an IOException: " + var8.getMessage());
               Iris.reportError(var8);
            }

            this.sender().playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.5F);
            var10000 = this.sender();
            var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Successfully object to saved: " + var1.getLoadKey() + "/objects/" + var2);
         }
      }
   }

   @Decree(
      description = "Shift a selection in your looking direction",
      aliases = {"-"}
   )
   public void shift(@Param(description = "The amount to shift by",defaultValue = "1") int amount) {
      if (!WandSVC.isHoldingWand(this.player())) {
         this.sender().sendMessage("Hold your wand.");
      } else {
         Location[] var2 = WandSVC.getCuboid(this.player());
         if (var2 != null && var2[0] != null && var2[1] != null) {
            Location var3 = var2[0].clone();
            Location var4 = var2[1].clone();
            Direction var5 = Direction.closest(this.player().getLocation().getDirection()).reverse();
            if (var5 != null) {
               var3.add(var5.toVector().multiply(var1));
               var4.add(var5.toVector().multiply(var1));
               Cuboid var6 = new Cuboid(var3, var4);
               var2[0] = var6.getLowerNE();
               var2[1] = var6.getUpperSW();
               this.player().getInventory().setItemInMainHand(WandSVC.createWand(var2[0], var2[1]));
               this.player().updateInventory();
               this.sender().playSound(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1.0F, 0.55F);
            }
         } else {
            this.sender().sendMessage("No area selected.");
         }
      }
   }

   @Decree(
      description = "Undo a number of pastes",
      aliases = {"-"}
   )
   public void undo(@Param(description = "The amount of pastes to undo",defaultValue = "1") int amount) {
      ObjectSVC var2 = (ObjectSVC)Iris.service(ObjectSVC.class);
      int var3 = Math.min(var2.getUndos().size(), var1);
      var2.revertChanges(var3);
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.BLUE);
      var10000.sendMessage(var10001 + "Reverted " + var3 + String.valueOf(C.BLUE) + " pastes!");
   }

   @Decree(
      description = "Gets an object wand and grabs the current WorldEdit selection.",
      aliases = {"we"},
      origin = DecreeOrigin.PLAYER,
      studio = true
   )
   public void we() {
      if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You can't get a WorldEdit selection without WorldEdit, you know.");
      } else {
         Cuboid var1 = WorldEditLink.getSelection(this.sender().player());
         if (var1 == null) {
            this.sender().sendMessage(String.valueOf(C.RED) + "You don't have a WorldEdit selection in this world.");
         } else {
            this.sender().player().getInventory().addItem(new ItemStack[]{WandSVC.createWand(var1.getLowerNE(), var1.getUpperSW())});
            this.sender().sendMessage(String.valueOf(C.GREEN) + "A fresh wand with your current WorldEdit selection on it!");
         }
      }
   }

   @Decree(
      description = "Get an object wand",
      sync = true
   )
   public void wand() {
      this.player().getInventory().addItem(new ItemStack[]{WandSVC.createWand()});
      this.sender().playSound(Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1.0F, 1.5F);
      this.sender().sendMessage(String.valueOf(C.GREEN) + "Poof! Good luck building!");
   }

   @Decree(
      name = "x&y",
      description = "Autoselect up, down & out",
      sync = true
   )
   public void xay() {
      if (!WandSVC.isHoldingWand(this.player())) {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "Hold your wand!");
      } else {
         Location[] var1 = WandSVC.getCuboid(this.player());
         if (var1 != null && var1[0] != null && var1[1] != null) {
            Location var2 = var1[0].clone();
            Location var3 = var1[1].clone();
            Location var4 = var1[0].clone();
            Location var5 = var1[1].clone();
            Cuboid var6 = new Cuboid(var2, var3);

            Cuboid var7;
            for(var7 = new Cuboid(var2, var3); !var6.containsOnly(Material.AIR); var6 = new Cuboid(var2, var3)) {
               var2.add(new Vector(0, 1, 0));
               var3.add(new Vector(0, 1, 0));
            }

            var2.add(new Vector(0, -1, 0));
            var3.add(new Vector(0, -1, 0));

            while(!var7.containsOnly(Material.AIR)) {
               var4.add(new Vector(0, -1, 0));
               var5.add(new Vector(0, -1, 0));
               var7 = new Cuboid(var4, var5);
            }

            var4.add(new Vector(0, 1, 0));
            var5.add(new Vector(0, 1, 0));
            var1[0] = var2;
            var1[1] = var5;
            var6 = new Cuboid(var1[0], var1[1]);
            var6 = var6.contract(Cuboid.CuboidDirection.North);
            var6 = var6.contract(Cuboid.CuboidDirection.South);
            var6 = var6.contract(Cuboid.CuboidDirection.East);
            var6 = var6.contract(Cuboid.CuboidDirection.West);
            var1[0] = var6.getLowerNE();
            var1[1] = var6.getUpperSW();
            this.player().getInventory().setItemInMainHand(WandSVC.createWand(var1[0], var1[1]));
            this.player().updateInventory();
            this.sender().playSound(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1.0F, 0.55F);
            this.sender().sendMessage(String.valueOf(C.GREEN) + "Auto-select complete!");
         } else {
            this.sender().sendMessage("No area selected.");
         }
      }
   }

   @Decree(
      name = "x+y",
      description = "Autoselect up & out",
      sync = true
   )
   public void xpy() {
      if (!WandSVC.isHoldingWand(this.player())) {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "Hold your wand!");
      } else {
         Location[] var1 = WandSVC.getCuboid(this.player());
         if (var1 != null && var1[0] != null && var1[1] != null) {
            var1[0].add(new Vector(0, 1, 0));
            var1[1].add(new Vector(0, 1, 0));
            Location var2 = var1[0].clone();
            Location var3 = var1[1].clone();

            Cuboid var4;
            for(var4 = new Cuboid(var2, var3); !var4.containsOnly(Material.AIR); var4 = new Cuboid(var2, var3)) {
               var2.add(new Vector(0, 1, 0));
               var3.add(new Vector(0, 1, 0));
            }

            var2.add(new Vector(0, -1, 0));
            var3.add(new Vector(0, -1, 0));
            var1[0] = var2;
            var3 = var1[1];
            var4 = new Cuboid(var2, var3);
            var4 = var4.contract(Cuboid.CuboidDirection.North);
            var4 = var4.contract(Cuboid.CuboidDirection.South);
            var4 = var4.contract(Cuboid.CuboidDirection.East);
            var4 = var4.contract(Cuboid.CuboidDirection.West);
            var1[0] = var4.getLowerNE();
            var1[1] = var4.getUpperSW();
            this.player().getInventory().setItemInMainHand(WandSVC.createWand(var1[0], var1[1]));
            this.player().updateInventory();
            this.sender().playSound(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM, 1.0F, 0.55F);
            this.sender().sendMessage(String.valueOf(C.GREEN) + "Auto-select complete!");
         } else {
            this.sender().sendMessage("No area selected.");
         }
      }
   }

   static {
      skipBlocks = Set.of(Materials.GRASS, Material.SNOW, Material.VINE, Material.TORCH, Material.DEAD_BUSH, Material.POPPY, Material.DANDELION);
   }
}
