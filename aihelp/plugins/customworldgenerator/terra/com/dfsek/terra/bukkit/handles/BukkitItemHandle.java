package com.dfsek.terra.bukkit.handles;

import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.inventory.Item;
import com.dfsek.terra.api.inventory.item.Enchantment;
import com.dfsek.terra.bukkit.util.MinecraftUtils;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class BukkitItemHandle implements ItemHandle {
   public Item createItem(String data) {
      return BukkitAdapter.adapt(Material.matchMaterial(data));
   }

   public Enchantment getEnchantment(String id) {
      return BukkitAdapter.adapt(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(MinecraftUtils.stripMinecraftNamespace(id))));
   }

   public Set<Enchantment> getEnchantments() {
      return (Set)Arrays.stream(org.bukkit.enchantments.Enchantment.values()).map(BukkitAdapter::adapt).collect(Collectors.toSet());
   }
}
