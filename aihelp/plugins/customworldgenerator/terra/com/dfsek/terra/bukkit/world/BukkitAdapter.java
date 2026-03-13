package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.inventory.ItemStack;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.BukkitPlayer;
import com.dfsek.terra.bukkit.world.block.BukkitBlockTypeAndItem;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemStack;
import com.dfsek.terra.bukkit.world.inventory.meta.BukkitEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.block.data.type.RedstoneWire.Connection;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;

public final class BukkitAdapter {
   public static BlockState adapt(BlockData data) {
      return BukkitBlockState.newInstance(data);
   }

   public static BlockData adapt(BlockState data) {
      return ((BukkitBlockState)data).getHandle();
   }

   public static Axis adapt(org.bukkit.Axis axis) {
      Axis var10000;
      switch(axis) {
      case X:
         var10000 = Axis.X;
         break;
      case Y:
         var10000 = Axis.Y;
         break;
      case Z:
         var10000 = Axis.Z;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static WorldProperties adapt(WorldInfo worldInfo) {
      return new BukkitWorldProperties(worldInfo);
   }

   public static WorldInfo adapt(WorldProperties properties) {
      return (WorldInfo)properties.getHandle();
   }

   public static Half adapt(org.bukkit.block.data.Bisected.Half half) {
      Half var10000;
      switch(half) {
      case BOTTOM:
         var10000 = Half.BOTTOM;
         break;
      case TOP:
         var10000 = Half.TOP;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static RedstoneConnection adapt(Connection connection) {
      RedstoneConnection var10000;
      switch(connection) {
      case NONE:
         var10000 = RedstoneConnection.NONE;
         break;
      case UP:
         var10000 = RedstoneConnection.UP;
         break;
      case SIDE:
         var10000 = RedstoneConnection.SIDE;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static Connection adapt(RedstoneConnection connection) {
      Connection var10000;
      switch(connection) {
      case SIDE:
         var10000 = Connection.SIDE;
         break;
      case UP:
         var10000 = Connection.UP;
         break;
      case NONE:
         var10000 = Connection.NONE;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static RailShape adapt(Shape shape) {
      RailShape var10000;
      switch(shape) {
      case SOUTH_WEST:
         var10000 = RailShape.SOUTH_WEST;
         break;
      case SOUTH_EAST:
         var10000 = RailShape.SOUTH_EAST;
         break;
      case NORTH_EAST:
         var10000 = RailShape.NORTH_EAST;
         break;
      case NORTH_WEST:
         var10000 = RailShape.NORTH_WEST;
         break;
      case ASCENDING_EAST:
         var10000 = RailShape.ASCENDING_EAST;
         break;
      case ASCENDING_WEST:
         var10000 = RailShape.ASCENDING_WEST;
         break;
      case ASCENDING_SOUTH:
         var10000 = RailShape.ASCENDING_SOUTH;
         break;
      case ASCENDING_NORTH:
         var10000 = RailShape.ASCENDING_NORTH;
         break;
      case NORTH_SOUTH:
         var10000 = RailShape.NORTH_SOUTH;
         break;
      case EAST_WEST:
         var10000 = RailShape.EAST_WEST;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static Shape adapt(RailShape shape) {
      Shape var10000;
      switch(shape) {
      case EAST_WEST:
         var10000 = Shape.EAST_WEST;
         break;
      case NORTH_SOUTH:
         var10000 = Shape.NORTH_SOUTH;
         break;
      case ASCENDING_NORTH:
         var10000 = Shape.ASCENDING_NORTH;
         break;
      case ASCENDING_SOUTH:
         var10000 = Shape.ASCENDING_SOUTH;
         break;
      case ASCENDING_WEST:
         var10000 = Shape.ASCENDING_WEST;
         break;
      case ASCENDING_EAST:
         var10000 = Shape.ASCENDING_EAST;
         break;
      case NORTH_WEST:
         var10000 = Shape.NORTH_WEST;
         break;
      case NORTH_EAST:
         var10000 = Shape.NORTH_EAST;
         break;
      case SOUTH_EAST:
         var10000 = Shape.SOUTH_EAST;
         break;
      case SOUTH_WEST:
         var10000 = Shape.SOUTH_WEST;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static org.bukkit.block.data.Bisected.Half adapt(Half half) {
      org.bukkit.block.data.Bisected.Half var10000;
      switch(half) {
      case TOP:
         var10000 = org.bukkit.block.data.Bisected.Half.TOP;
         break;
      case BOTTOM:
         var10000 = org.bukkit.block.data.Bisected.Half.BOTTOM;
         break;
      default:
         throw new IllegalStateException();
      }

      return var10000;
   }

   public static org.bukkit.Axis adapt(Axis axis) {
      org.bukkit.Axis var10000;
      switch(axis) {
      case Z:
         var10000 = org.bukkit.Axis.Z;
         break;
      case Y:
         var10000 = org.bukkit.Axis.Y;
         break;
      case X:
         var10000 = org.bukkit.Axis.X;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static Vector3 adapt(Location location) {
      return Vector3.of(location.getX(), location.getY(), location.getZ());
   }

   public static Vector adapt(Vector3 vector3) {
      return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
   }

   public static Vector3 adapt(Vector vector) {
      return Vector3.of(vector.getX(), vector.getY(), vector.getZ());
   }

   public static CommandSender adapt(org.bukkit.command.CommandSender sender) {
      return new BukkitCommandSender(sender);
   }

   public static Entity adapt(org.bukkit.entity.Entity entity) {
      return new BukkitEntity(entity);
   }

   public static org.bukkit.command.CommandSender adapt(CommandSender sender) {
      return ((BukkitCommandSender)sender).getHandle();
   }

   public static ServerWorld adapt(World world) {
      return new BukkitServerWorld(world);
   }

   public static World adapt(ServerWorld world) {
      return (World)world.getHandle();
   }

   public static Chunk adapt(org.bukkit.Chunk chunk) {
      return new BukkitChunk(chunk);
   }

   public static org.bukkit.Chunk adapt(Chunk chunk) {
      return (org.bukkit.Chunk)chunk.getHandle();
   }

   public static Enchantment adapt(org.bukkit.enchantments.Enchantment enchantment) {
      return new BukkitEnchantment(enchantment);
   }

   public static org.bukkit.enchantments.Enchantment adapt(Enchantment enchantment) {
      return ((BukkitEnchantment)enchantment).getHandle();
   }

   public static Player adapt(com.dfsek.terra.api.entity.Player player) {
      return ((BukkitPlayer)player).getHandle();
   }

   public static com.dfsek.terra.api.entity.Player adapt(Player player) {
      return new BukkitPlayer(player);
   }

   public static BukkitBlockTypeAndItem adapt(Material material) {
      return new BukkitBlockTypeAndItem(material);
   }

   public static Material adapt(BlockType type) {
      return ((BukkitBlockTypeAndItem)type).getHandle();
   }

   public static ItemStack adapt(org.bukkit.inventory.ItemStack in) {
      return new BukkitItemStack(in);
   }

   public static org.bukkit.inventory.ItemStack adapt(ItemStack in) {
      return ((BukkitItemStack)in).getHandle();
   }
}
