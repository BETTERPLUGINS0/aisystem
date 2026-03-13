package com.ryandw11.structure.structure.properties;

import com.ryandw11.structure.exceptions.StructureConfigurationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BottomSpaceFill {
   private final Map<Biome, Material> blockMap;
   private Material defaultMaterial;
   private boolean enabled = false;

   public BottomSpaceFill(FileConfiguration configuration) {
      this.blockMap = new HashMap();
      if (configuration.contains("BottomSpaceFill")) {
         ConfigurationSection fillSection = (ConfigurationSection)Objects.requireNonNull(configuration.getConfigurationSection("BottomSpaceFill"));
         this.enabled = true;
         Iterator var3 = fillSection.getKeys(false).iterator();

         while(var3.hasNext()) {
            String keyGroup = (String)var3.next();
            String[] keys = keyGroup.split(",");

            Material fillMaterial;
            try {
               fillMaterial = Material.valueOf(((String)Objects.requireNonNull(fillSection.getString(keyGroup))).toUpperCase());
            } catch (IllegalArgumentException var14) {
               String var10002 = fillSection.getString(keyGroup);
               throw new StructureConfigurationException("Unknown fill material " + var10002 + " in BottomSpaceFill configuration section.");
            }

            String[] var7 = keys;
            int var8 = keys.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String key = var7[var9];
               if (key.equalsIgnoreCase("default")) {
                  this.defaultMaterial = fillMaterial;
               } else {
                  Biome biome;
                  try {
                     biome = Biome.valueOf(key.toUpperCase());
                  } catch (IllegalArgumentException var13) {
                     throw new StructureConfigurationException("Unknown biome " + key + " in BottomSpaceFill configuration section.");
                  }

                  this.blockMap.put(biome, fillMaterial);
               }
            }
         }

      }
   }

   public BottomSpaceFill() {
      this.enabled = false;
      this.blockMap = new HashMap();
   }

   public BottomSpaceFill(@Nullable Material defaultMaterial, @NotNull Map<Biome, Material> blockFillMap) {
      this.enabled = true;
      this.defaultMaterial = defaultMaterial;
      this.blockMap = (Map)Objects.requireNonNull(blockFillMap);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setDefaultFillMaterial(@Nullable Material material) {
      this.defaultMaterial = material;
   }

   public Optional<Material> getDefaultFillMaterial() {
      return Optional.ofNullable(this.defaultMaterial);
   }

   @NotNull
   public Map<Biome, Material> getFillBlockMap() {
      return this.blockMap;
   }

   public Optional<Material> getFillMaterial(Biome biome) {
      return this.blockMap.containsKey(biome) ? Optional.of((Material)this.blockMap.get(biome)) : Optional.ofNullable(this.defaultMaterial);
   }
}
