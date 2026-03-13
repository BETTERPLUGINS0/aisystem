package org.terraform.structure.pyramid;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable.AttachedFace;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.RedstoneWire.Connection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class CryptRoom extends RoomPopulatorAbstract {
   public CryptRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      BlockFace face = BlockUtils.getDirectBlockFace(this.rand);
      Iterator var4 = room.getFourWalls(data, 3).entrySet().iterator();

      while(var4.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var4.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            w.Pillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
            if (w.getDirection() == face) {
               w.getFront().Pillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
               RedstoneWire wire;
               if (i == 1) {
                  wire = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
                  wire.setFace(face, Connection.SIDE);
                  wire.setFace(face.getOppositeFace(), Connection.SIDE);
                  w.getFront().setBlockData(wire);
                  wire = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
                  wire.setFace(BlockUtils.getAdjacentFaces(face)[0], Connection.SIDE);
                  wire.setFace(face.getOppositeFace(), Connection.UP);
                  w.getFront(2).getDown().setBlockData(wire);
                  wire = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
                  wire.setFace(BlockUtils.getAdjacentFaces(face)[0], Connection.SIDE);
                  wire.setFace(BlockUtils.getAdjacentFaces(face)[1].getOppositeFace(), Connection.SIDE);
                  w.getFront().getUp(3).setBlockData(wire);
                  RedstoneWallTorch rTorch = (RedstoneWallTorch)Bukkit.createBlockData(Material.REDSTONE_WALL_TORCH);
                  rTorch.setFacing(face);
                  w.getFront().getUp().setBlockData(rTorch);
                  Switch lever = (Switch)Bukkit.createBlockData(Material.LEVER);
                  lever.setAttachedFace(AttachedFace.WALL);
                  lever.setFacing(face.getOppositeFace());
                  lever.setPowered(false);
                  w.getRear().getUp().setBlockData(lever);
                  w.getFront(3).Pillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
               } else if (i == 2) {
                  wire = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
                  wire.setFace(BlockUtils.getAdjacentFaces(face)[1], Connection.SIDE);
                  wire.setFace(face.getOppositeFace(), Connection.SIDE);
                  w.getFront(2).getDown().setBlockData(wire);
                  wire = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
                  wire.setFace(face, Connection.SIDE);
                  wire.setFace(face.getOppositeFace(), Connection.SIDE);
                  w.getFront().getDown().setBlockData(wire);
                  wire = (RedstoneWire)Bukkit.createBlockData(Material.REDSTONE_WIRE);
                  wire.setFace(BlockUtils.getAdjacentFaces(face)[0], Connection.SIDE);
                  wire.setFace(BlockUtils.getAdjacentFaces(face)[1].getOppositeFace(), Connection.SIDE);
                  w.getFront().getUp(3).setBlockData(wire);
                  w.getFront(3).Pillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
                  w.getFront(2).Pillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
               } else if (i == 3) {
                  w.Pillar(2, this.rand, new Material[]{Material.AIR});
                  w.getFront().Pillar(2, this.rand, new Material[]{Material.AIR});
                  Piston faceDown = (Piston)Bukkit.createBlockData(Material.STICKY_PISTON);
                  faceDown.setFacing(BlockFace.DOWN);
                  w.getFront().getUp(3).setBlockData(faceDown);
                  Piston faceUp = (Piston)Bukkit.createBlockData(Material.STICKY_PISTON);
                  faceUp.setFacing(BlockFace.UP);
                  w.getFront().getDown(2).setBlockData(faceUp);
                  w.getUp(2).getFront(2).Pillar(room.getHeight() - 2, this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
               } else {
                  w.getFront(2).Pillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
               }
            } else if (w.getDirection() == face.getOppositeFace() && GenUtils.chance(this.rand, 1, 10)) {
               SimpleBlock pos = w.getFront().get();
               Directional chest = (Directional)Bukkit.createBlockData(Material.CHEST);
               chest.setFacing(face.getOppositeFace());
               pos.setBlockData(chest);
               data.lootTableChest(pos.getX(), pos.getY(), pos.getZ(), TerraLootTable.DESERT_PYRAMID);
            }

            w = w.getLeft();
         }
      }

      for(int i = 0; i < GenUtils.randInt(this.rand, 1, 4); ++i) {
         data.addEntity(room.getX(), room.getY(), room.getZ(), EntityType.HUSK);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 11 && room.getWidthZ() >= 11;
   }
}
