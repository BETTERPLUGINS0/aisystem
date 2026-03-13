package me.casperge.realisticseasons.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.seasonevent.buildin.ChristmasEvent;
import me.casperge.realisticseasons.seasonevent.buildin.DefaultEventType;
import me.casperge.realisticseasons.seasonevent.buildin.EasterEvent;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons1_21_R4.NmsCode_21_R4;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent.ChangeReason;

public class BlockEvents implements Listener {
   private RealisticSeasons main;
   boolean testBoolean = true;
   private List<Material> logPlaceAndBreak;

   public BlockEvents(RealisticSeasons var1) {
      var1.getServer().getPluginManager().registerEvents(this, var1);
      this.main = var1;
      this.logPlaceAndBreak = new ArrayList(Arrays.asList(Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.SWEET_BERRY_BUSH));
      if (Version.is_1_21_5_or_up()) {
         this.logPlaceAndBreak.add(NmsCode_21_R4.getLeafLitter());
      }

   }

   @EventHandler
   public void moistureChange(MoistureChangeEvent var1) {
      if (this.main.getSeasonManager().getSeason(var1.getBlock().getWorld()) == Season.WINTER) {
         if (!this.main.getSettings().modifyBlocks || !this.main.getSettings().spawnIceInWinter) {
            return;
         }

         if (var1.getBlock().getWorld().getHighestBlockAt(var1.getBlock().getLocation()).getLocation().equals(var1.getBlock().getLocation())) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onGrow(CauldronLevelChangeEvent var1) {
      if (var1.getReason() == ChangeReason.NATURAL_FILL && var1.getBlock().getLocation().equals(var1.getBlock().getWorld().getHighestBlockAt(var1.getBlock().getLocation()).getLocation()) && this.main.getSeasonManager().getSeason(var1.getBlock().getWorld()) == Season.WINTER) {
         String var2 = this.main.getNMSUtils().getBiomeName(var1.getBlock().getLocation());
         if (!ChunkUtils.checkBiome(ChunkUtils.BiomeType.AFFECTINWINTER, var2) && var1.getNewState().getType() == Material.WATER_CAULDRON) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void naturalFreezeEvent(BlockFormEvent var1) {
      if ((var1.getNewState().getType() == Material.ICE || var1.getNewState().getType() == Material.SNOW) && this.main.getBlockUtils().affectBlockInWinter(var1.getBlock())) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void blockPlace(BlockPlaceEvent var1) {
      if (this.main.getSettings().keepPlayerPlacedPlants) {
         if (this.main.getSeasonManager().worldData.containsKey(var1.getBlock().getWorld()) && this.main.getSeasonManager().worldData.get(var1.getBlock().getWorld()) != Season.DISABLED && this.logPlaceAndBreak.contains(var1.getBlockPlaced().getType())) {
            this.main.getBlockStorage().logPlacement(var1.getBlock().getLocation(), StoredBlockType.FLOWER);
         }

      }
   }

   @EventHandler
   public void grassDestroy(BlockFadeEvent var1) {
      if (var1.getBlock().getType().equals(Material.GRASS_BLOCK) && (var1.getBlock().getRelative(BlockFace.UP).getType().equals(Material.SNOW) || var1.getBlock().getRelative(BlockFace.UP).getType().equals(Material.SNOW_BLOCK))) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void blockBreak(BlockBreakEvent var1) {
      if (this.logPlaceAndBreak.contains(var1.getBlock().getType())) {
         this.main.getBlockStorage().logBreak(var1.getBlock().getLocation(), StoredBlockType.FLOWER);
      } else if (var1.getBlock().getType() == Material.PLAYER_HEAD) {
         if (this.main.getBlockStorage().isStored(var1.getBlock().getLocation(), StoredBlockType.PRESENT)) {
            if (!var1.isCancelled()) {
               var1.setDropItems(false);
               ChristmasEvent var2 = (ChristmasEvent)this.main.getEventManager().getDefaultEvent(DefaultEventType.CHRISTMAS);
               if (var2 != null) {
                  var2.presentOpened(var1.getPlayer(), var1.getBlock().getLocation());
               }
            }
         } else if (this.main.getBlockStorage().isStored(var1.getBlock().getLocation(), StoredBlockType.EGG) && !var1.isCancelled()) {
            var1.setDropItems(false);
            EasterEvent var3 = (EasterEvent)this.main.getEventManager().getDefaultEvent(DefaultEventType.EASTER);
            if (var3 != null) {
               var3.eggOpened(var1.getPlayer(), var1.getBlock().getLocation());
            }
         }
      }

   }
}
