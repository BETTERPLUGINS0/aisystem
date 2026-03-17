package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.configuration.LeaderboardGuiConfiguration;
import advancedplugins.pm2.cv.api.enums.EnumPlaceholder;
import advancedplugins.pm2.cv.api.service.GuiBuilderService;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.util.LeaderboardUtil;
import es.outlook.adriansrj.spigui.buttons.SGButton;
import es.outlook.adriansrj.spigui.menu.SGMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LeaderboardMenu {
   private final Vehicle vehicle;
   private final int page;

   public void open(Player player) {
      SGMenu var2 = ((GuiBuilderService)Objects.requireNonNull((GuiBuilderService)InfiniteVehicles.getService(GuiBuilderService.class))).get().create(this.format((String)LeaderboardGuiConfiguration.TITLE, this.vehicle, -1), LeaderboardGuiConfiguration.ROWS);
      SGButton var3 = new SGButton(this.buildIcon(LeaderboardGuiConfiguration.EXIT_ITEM, this.vehicle, -1));
      var3.setListener((var1x) -> {
         var1.closeInventory();
      });
      ArrayList var4 = new ArrayList(LeaderboardGuiConfiguration.SLOTS);
      HashMap var5 = LeaderboardUtil.getKills(this.vehicle);
      int var6 = var4.size();
      int var7 = (int)Math.ceil((double)var5.size() / (double)var6);
      int var8 = (this.page - 1) * var6;
      int var9 = this.page * var6;
      if (var9 > var5.size()) {
         var9 = var5.size();
      }

      ArrayList var10 = new ArrayList(var5.keySet().stream().toList().reversed().subList(var8, var9));
      var10.reversed().forEach((var4x) -> {
         int var5x = (Integer)var5.get(var4x);
         if (!var4.isEmpty()) {
            int var6 = (Integer)var4.remove(0);
            SGButton var7 = new SGButton(this.buildIcon(LeaderboardGuiConfiguration.PLAYER_HEAD_ITEM, var4x, var5x));

            try {
               var2.setButton(slotCheck(var6), var7);
            } catch (InvalidConfigurationException var9) {
               var9.printStackTrace();
            }

         }
      });

      try {
         var2.setButton(slotCheck(LeaderboardGuiConfiguration.EXIT_ITEM.getSlot()), var3);
      } catch (InvalidConfigurationException var15) {
         var15.printStackTrace();
      }

      SGButton var11;
      if (this.page > 1) {
         var11 = new SGButton(this.buildIcon(LeaderboardGuiConfiguration.PREVIOUS_ITEM, this.vehicle, -1));
         var11.setListener((var2x) -> {
            (new LeaderboardMenu(this.vehicle, this.page - 1)).open(var1);
         });

         try {
            var2.setButton(slotCheck(LeaderboardGuiConfiguration.PREVIOUS_ITEM.getSlot()), var11);
         } catch (InvalidConfigurationException var14) {
            var14.printStackTrace();
         }
      }

      if (this.page < var7) {
         var11 = new SGButton(this.buildIcon(LeaderboardGuiConfiguration.NEXT_ITEM, this.vehicle, -1));
         var11.setListener((var2x) -> {
            (new LeaderboardMenu(this.vehicle, this.page + 1)).open(var1);
         });

         try {
            var2.setButton(slotCheck(LeaderboardGuiConfiguration.NEXT_ITEM.getSlot()), var11);
         } catch (InvalidConfigurationException var13) {
            var13.printStackTrace();
         }
      }

      var1.openInventory(var2.getInventory());
   }

   private static int slotCheck(int slot) {
      if (var0 >= 0 && var0 <= LeaderboardGuiConfiguration.ROWS * 9) {
         return var0;
      } else {
         throw new InvalidConfigurationException("invalid gui item slot: " + var0);
      }
   }

   @NotNull
   private ItemStack buildIcon(@NotNull GuiConfiguration.Item item, @NotNull Object object, int kills) {
      ItemStack var4 = ItemStackUtil.buildCustomItem(var1.getMaterial(), var1.getCustomModelData(), this.format(var1.getDisplayName(), var2, var3), this.format(var1.getDescription(), var2, var3), ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS);
      if (ItemStackUtil.isHead(var1.getMaterial()) && StringUtils.isNotBlank(var1.getHeadTexture())) {
         InfiniteVehicles.getTexturedHeadService().applyTexture(var4, var1.getHeadTexture());
      }

      if (ItemStackUtil.isHead(var1.getMaterial()) && StringUtils.isBlank(var1.getHeadTexture()) && var2 instanceof Player) {
         Player var5 = (Player)var2;
         InfiniteVehicles.getTexturedHeadService().applyTexture(var4, (OfflinePlayer)var5);
      }

      return var4;
   }

   @Nullable
   private String format(@Nullable String string, @NotNull Object object, int kills) {
      if (var1 == null) {
         return null;
      } else {
         EnumPlaceholder[] var4 = EnumPlaceholder.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumPlaceholder var7 = var4[var6];
            switch(var7) {
            case NAME:
               if (var2 instanceof VehicleImpl) {
                  var1 = var7.format(var1, ((VehicleImpl)var2).getConfiguration().getName());
               }
               break;
            case FUEL_LEVEL:
               if (var2 instanceof Vehicle) {
                  var1 = var7.format(var1, String.valueOf(((Vehicle)var2).getFuelLevel()));
               }
               break;
            case FUEL_LEVEL_PERCENTAGE:
               if (var2 instanceof Vehicle) {
                  Vehicle var8 = (Vehicle)var2;
                  double var9 = (double)(var8.getFuelLevel() / var8.getFuelCapacity()) * 100.0D;
                  var1 = var7.format(var1, String.valueOf(var9));
               }
               break;
            case FUEL_CAPACITY:
               if (var2 instanceof Vehicle) {
                  var1 = var7.format(var1, String.valueOf(((Vehicle)var2).getFuelCapacity()));
               }
               break;
            case PLAYER_NAME:
               if (var2 instanceof OfflinePlayer) {
                  var1 = var7.format(var1, (String)Objects.requireNonNull(((OfflinePlayer)var2).getName()));
               }
               break;
            case PLAYER_KILLS:
               var1 = var7.format(var1, String.valueOf(var3));
               break;
            case PAGE:
               var1 = var7.format(var1, String.valueOf(this.page));
            case NEXT_PAGE:
               var1 = var7.format(var1, String.valueOf(this.page + 1));
            case PREVIOUS_PAGE:
               var1 = var7.format(var1, String.valueOf(Math.min(this.page - 1, 1)));
            }
         }

         return var1;
      }
   }

   @Nullable
   private List<String> format(@Nullable List<String> stringList, @NotNull Object object, int kills) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var4 = new ArrayList();
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var4.add(this.format(var6, var2, var3));
         }

         return var4;
      }
   }

   public LeaderboardMenu(final Vehicle vehicle, final int page) {
      this.vehicle = var1;
      this.page = var2;
   }

   public Vehicle getVehicle() {
      return this.vehicle;
   }

   public int getPage() {
      return this.page;
   }
}
