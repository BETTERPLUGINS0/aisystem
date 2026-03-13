package com.ryandw11.structure.api.structaddon;

import com.ryandw11.structure.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public interface StructureSection {
   String getName();

   void setupSection(@Nullable ConfigurationSection var1);

   boolean checkStructureConditions(Structure var1, Block var2, Chunk var3);
}
