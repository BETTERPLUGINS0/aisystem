package org.terraform.schematic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.RedstoneWire.Connection;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.FilenameArgument;
import org.terraform.coregen.BlockDataFixerAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.version.Version;

public class TerraSchematic {
   public static final String SCHEMATIC_FOLDER;
   @NotNull
   public static final HashMap<String, HashMap<Vector, BlockData>> cache;
   final SimpleBlock refPoint;
   @NotNull
   private final File schematicFolder;
   @NotNull
   public SchematicParser parser = new SchematicParser();
   HashMap<Vector, BlockData> data = new HashMap();
   BlockFace face;
   private double VERSION_VALUE;

   public TerraSchematic(SimpleBlock vector) {
      this.face = BlockFace.NORTH;
      this.schematicFolder = new File(TerraformGeneratorPlugin.get().getDataFolder(), SCHEMATIC_FOLDER);
      this.refPoint = vector;
   }

   public TerraSchematic(@NotNull Location loc) {
      this.face = BlockFace.NORTH;
      this.schematicFolder = new File(TerraformGeneratorPlugin.get().getDataFolder(), SCHEMATIC_FOLDER);
      this.refPoint = new SimpleBlock(loc);
   }

   @NotNull
   public static TerraSchematic load(String internalPath, SimpleBlock refPoint) throws FileNotFoundException {
      TerraSchematic schem = new TerraSchematic(refPoint);
      if (cache.containsKey(internalPath)) {
         schem.data = (HashMap)cache.get(internalPath);
         return schem.clone(refPoint);
      } else {
         boolean wasInDataFolder = false;
         InputStream is = TerraformGeneratorPlugin.get().getClass().getResourceAsStream("/" + internalPath + ".terra");
         if (is == null) {
            File schematicFolder = new File(TerraformGeneratorPlugin.get().getDataFolder(), SCHEMATIC_FOLDER);
            File schematicFile = new File(schematicFolder, internalPath + ".terra");

            try {
               if (!schematicFile.getCanonicalPath().startsWith(schematicFolder.getCanonicalPath())) {
                  throw new IllegalArgumentException("Schematic name contained illegal characters (i.e. periods)");
               }
            } catch (Exception var14) {
               throw new IllegalArgumentException("Schematic name contained illegal characters (i.e. periods)");
            }

            is = new FileInputStream(schematicFile);
            wasInDataFolder = true;
         }

         Scanner sc = new Scanner((InputStream)is, StandardCharsets.UTF_8);
         String line = sc.nextLine();
         schem.VERSION_VALUE = Double.parseDouble(line);

         while(true) {
            do {
               if (!sc.hasNextLine()) {
                  sc.close();
                  if (schem.data.size() < 100 && !wasInDataFolder) {
                     cache.put(internalPath, schem.data);
                  }

                  return schem;
               }

               line = sc.nextLine();
            } while(line.isEmpty());

            String[] cont = line.split(":@:", -1);
            String[] v = cont[0].split(",", -1);
            Vector key = new Vector(Integer.parseInt(v[0]), Integer.parseInt(v[1]), Integer.parseInt(v[2]));

            BlockData value;
            try {
               value = Bukkit.createBlockData(cont[1]);
            } catch (IllegalArgumentException var13) {
               BlockDataFixerAbstract fixer = TerraformGeneratorPlugin.injector.getBlockDataFixer();
               if (fixer == null) {
                  throw var13;
               }

               value = Bukkit.createBlockData(fixer.updateSchematic(schem.getVersionValue(), cont[1]));
            }

            schem.data.put(key, value);
         }
      }
   }

   @NotNull
   public TerraSchematic clone(SimpleBlock refPoint) {
      TerraSchematic clone = new TerraSchematic(refPoint);
      clone.data = new HashMap();
      clone.data.putAll(this.data);
      clone.VERSION_VALUE = this.VERSION_VALUE;
      return clone;
   }

   public void registerBlock(@NotNull Block b) {
      Vector rel = b.getLocation().toVector().subtract(this.refPoint.toVector());
      this.data.put(rel, b.getBlockData());
   }

   public void apply() {
      BlockDataFixerAbstract bdfa = TerraformGeneratorPlugin.injector.getBlockDataFixer();
      ArrayList<Vector> multiFace = new ArrayList();

      Iterator var3;
      Vector pos;
      Object bd;
      for(var3 = this.data.entrySet().iterator(); var3.hasNext(); this.parser.applyData(this.refPoint.getRelative(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), (BlockData)bd)) {
         Entry<Vector, BlockData> entry = (Entry)var3.next();
         pos = ((Vector)entry.getKey()).clone();
         bd = ((BlockData)entry.getValue()).clone();
         int x;
         if (this.face == BlockFace.WEST) {
            x = pos.getBlockX();
            pos.setX(pos.getZ());
            pos.setZ(x * -1);
         } else if (this.face == BlockFace.SOUTH) {
            pos.setX(pos.getX() * -1.0D);
            pos.setZ(pos.getZ() * -1.0D);
         } else if (this.face == BlockFace.EAST) {
            x = pos.getBlockX();
            pos.setX(pos.getZ() * -1.0D);
            pos.setZ(x);
         }

         if (this.face != BlockFace.NORTH) {
            if (bd instanceof Orientable) {
               Orientable o = (Orientable)bd;
               if (this.face == BlockFace.EAST || this.face == BlockFace.WEST) {
                  if (o.getAxis() == Axis.X) {
                     o.setAxis(Axis.Z);
                  } else if (o.getAxis() == Axis.Z) {
                     o.setAxis(Axis.X);
                  }
               }
            } else if (bd instanceof Rotatable) {
               Rotatable r = (Rotatable)bd;
               if (this.face == BlockFace.SOUTH) {
                  r.setRotation(r.getRotation().getOppositeFace());
               } else if (this.face == BlockFace.EAST) {
                  r.setRotation(BlockUtils.getAdjacentFaces(r.getRotation())[0]);
               } else if (this.face == BlockFace.WEST) {
                  r.setRotation(BlockUtils.getAdjacentFaces(r.getRotation())[1]);
               }
            } else if (bd instanceof Directional) {
               Directional r = (Directional)bd;
               if (BlockUtils.isDirectBlockFace(r.getFacing())) {
                  if (this.face == BlockFace.SOUTH) {
                     r.setFacing(r.getFacing().getOppositeFace());
                  } else if (this.face == BlockFace.WEST) {
                     r.setFacing(BlockUtils.getAdjacentFaces(r.getFacing())[1]);
                  } else if (this.face == BlockFace.EAST) {
                     r.setFacing(BlockUtils.getAdjacentFaces(r.getFacing())[0]);
                  }
               }
            } else if (bd instanceof MultipleFacing) {
               multiFace.add(pos);
            } else if (bd instanceof RedstoneWire) {
               RedstoneWire w = (RedstoneWire)bd;
               RedstoneWire newData = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
               newData.setPower(w.getPower());
               Iterator var12 = w.getAllowedFaces().iterator();

               while(var12.hasNext()) {
                  BlockFace wireFace = (BlockFace)var12.next();
                  if (w.getFace(wireFace) != Connection.NONE) {
                     if (this.face == BlockFace.SOUTH) {
                        newData.setFace(wireFace.getOppositeFace(), w.getFace(wireFace));
                     } else if (this.face == BlockFace.WEST) {
                        newData.setFace(BlockUtils.getAdjacentFaces(wireFace)[1], w.getFace(wireFace));
                     } else if (this.face == BlockFace.EAST) {
                        newData.setFace(BlockUtils.getAdjacentFaces(wireFace)[0], w.getFace(wireFace));
                     } else {
                        newData.setFace(wireFace, w.getFace(wireFace));
                     }
                  }
               }

               bd = newData;
            }

            if (bdfa != null) {
               bdfa.correctFacing(pos, (SimpleBlock)null, (BlockData)bd, this.face);
            }
         }
      }

      this.parser.applyDelayedData();
      var3 = multiFace.iterator();

      while(var3.hasNext()) {
         Vector pos = (Vector)var3.next();
         SimpleBlock b = this.refPoint.getRelative(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
         if (b.getBlockData() instanceof MultipleFacing) {
            BlockUtils.correctSurroundingMultifacingData(b);
         }
      }

      if (bdfa != null && this.face != BlockFace.NORTH) {
         Vector[] var14 = bdfa.flush();
         int var16 = var14.length;

         for(int var18 = 0; var18 < var16; ++var18) {
            Vector pos = var14[var18];
            SimpleBlock b = this.refPoint.getRelative(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
            bdfa.correctFacing(pos, b, (BlockData)null, this.face);
         }
      }

   }

   public void export(@NotNull String path) throws IOException {
      String validation = (new FilenameArgument("schem-name", false)).validate((CommandSender)null, path);
      if (!validation.isEmpty()) {
         throw new IOException(validation);
      } else {
         File outputFile = new File(this.schematicFolder, path);
         if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
         }

         if (!outputFile.exists()) {
            outputFile.createNewFile();
         }

         FileOutputStream outputStream = new FileOutputStream(outputFile);
         BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
         bufferedWriter.write(Version.VERSION.getSchematicHeader());
         bufferedWriter.newLine();
         Iterator var6 = this.data.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Vector, BlockData> entry = (Entry)var6.next();
            int var10000 = ((Vector)entry.getKey()).getBlockX();
            String vector = var10000 + "," + ((Vector)entry.getKey()).getBlockY() + "," + ((Vector)entry.getKey()).getBlockZ();
            bufferedWriter.write(vector + ":@:" + ((BlockData)entry.getValue()).getAsString());
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
      }
   }

   public BlockFace getFace() {
      return this.face;
   }

   public void setFace(BlockFace face) {
      this.face = face;
   }

   public double getVersionValue() {
      return this.VERSION_VALUE;
   }

   static {
      SCHEMATIC_FOLDER = File.separator + "schematic";
      cache = new HashMap();
   }
}
