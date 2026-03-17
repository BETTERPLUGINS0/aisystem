package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackParser extends DataParser {
   @NotNull
   public String getIdentifier() {
      return "item-stack";
   }

   @NotNull
   public Class<?> getType() {
      return ItemStack.class;
   }

   public Object parse(@NotNull ConfigurationSection var1) {
      HashMap var2 = new HashMap(var1.getValues(false));
      var2.put("v", (new ItemStack(Material.BEDROCK)).serialize().get("v"));
      return ItemStack.deserialize(var2);
   }

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      super.write(var1, var2);
      if (var1 instanceof ItemStack) {
         ItemStack var3 = (ItemStack)var1;
         Iterator var4 = var3.serialize().entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var2.set((String)var5.getKey(), var5.getValue());
         }
      }

   }
}
