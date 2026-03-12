package me.gypopo.economyshopgui.objects;

import com.mojang.authlib.GameProfile;
import java.util.Collection;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.providers.requirements.RequirementType;
import me.gypopo.economyshopgui.providers.requirements.Requirements;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DisplayItem {
   String section();

   String itemLoc();

   String getItemPath();

   String getDisplayname();

   ItemStack getShopItem();

   Requirements getRequirements();

   boolean meetsRequirements(Player var1, boolean var2);

   boolean hasItemRequirement(RequirementType var1);

   void addItemRequirement(ItemRequirement var1);

   void removeItemRequirement(ItemRequirement var1);

   void addItemRequirements(Collection<? extends ItemRequirement> var1);

   void removeItemRequirements(Collection<? extends ItemRequirement> var1);

   boolean hasItemError();

   void updateSkullTexture(GameProfile var1, boolean var2);

   DisplayItem.DynamicLore getLore();

   public interface DynamicLore {
      String[] get(boolean var1, boolean var2);
   }
}
