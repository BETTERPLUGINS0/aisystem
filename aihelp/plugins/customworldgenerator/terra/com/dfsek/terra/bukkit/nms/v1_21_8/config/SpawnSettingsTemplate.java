package com.dfsek.terra.bukkit.nms.v1_21_8.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpawnSettingsTemplate implements ObjectTemplate<MobSpawnSettings> {
   private static final Logger logger = LoggerFactory.getLogger(SpawnTypeConfig.class);
   private static boolean used = false;
   @Value("spawns")
   @Default
   private List<SpawnTypeConfig> spawns = null;
   @Value("costs")
   @Default
   private List<SpawnCostConfig> costs = null;
   @Value("probability")
   @Default
   private Float probability = null;

   public MobSpawnSettings get() {
      Builder builder = new Builder();
      Iterator var2 = this.spawns.iterator();

      while(var2.hasNext()) {
         SpawnTypeConfig spawn = (SpawnTypeConfig)var2.next();
         MobCategory group = spawn.getGroup();
         Iterator var5 = spawn.getEntries().iterator();

         while(var5.hasNext()) {
            SpawnEntryConfig entry = (SpawnEntryConfig)var5.next();
            builder.addSpawn(group, entry.getWeight(), entry.getSpawnEntry());
         }
      }

      var2 = this.costs.iterator();

      while(var2.hasNext()) {
         SpawnCostConfig cost = (SpawnCostConfig)var2.next();
         builder.addMobCharge(cost.getType(), cost.getMass(), cost.getGravity());
      }

      if (this.probability != null) {
         builder.creatureGenerationProbability(this.probability);
      }

      return builder.build();
   }
}
