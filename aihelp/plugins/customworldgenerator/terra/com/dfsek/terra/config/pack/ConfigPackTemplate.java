package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigPackTemplate implements ConfigTemplate {
   @Value("id")
   private String id;
   @Value("variables")
   @Default
   @Meta
   private Map<String, Double> variables = new HashMap();
   @Value("beta.carving")
   @Default
   @Meta
   private boolean betaCarvers = false;
   @Value("structures.locatable")
   @Default
   @Meta
   private Map<String, String> locatable = new HashMap();
   @Value("blend.terrain.elevation")
   @Default
   @Meta
   private int elevationBlend = 4;
   @Value("vanilla.mobs")
   @Default
   @Meta
   private boolean vanillaMobs = true;
   @Value("vanilla.caves")
   @Default
   @Meta
   private boolean vanillaCaves = false;
   @Value("vanilla.decorations")
   @Default
   @Meta
   private boolean vanillaDecorations = false;
   @Value("vanilla.structures")
   @Default
   @Meta
   private boolean vanillaStructures = false;
   @Value("author")
   @Default
   private String author = "Anon Y. Mous";
   @Value("disable.sapling")
   @Default
   @Meta
   private boolean disableSaplings = false;
   @Value("stages")
   @Default
   @Meta
   private List<GenerationStage> stages = Collections.emptyList();
   @Value("version")
   private Version version;
   @Value("disable.carvers")
   @Default
   @Meta
   private boolean disableCarvers = false;
   @Value("disable.structures")
   @Default
   @Meta
   private boolean disableStructures = false;
   @Value("disable.ores")
   @Default
   @Meta
   private boolean disableOres = false;
   @Value("disable.trees")
   @Default
   @Meta
   private boolean disableTrees = false;
   @Value("disable.flora")
   @Default
   @Meta
   private boolean disableFlora = false;
   @Value("generator")
   @Meta
   private ChunkGeneratorProvider generatorProvider;
   @Value("cache.biome.enable")
   @Default
   private boolean biomeCache = false;

   public boolean disableCarvers() {
      return this.disableCarvers;
   }

   public boolean disableFlora() {
      return this.disableFlora;
   }

   public boolean disableOres() {
      return this.disableOres;
   }

   public boolean disableStructures() {
      return this.disableStructures;
   }

   public boolean disableTrees() {
      return this.disableTrees;
   }

   public boolean vanillaMobs() {
      return this.vanillaMobs;
   }

   public boolean vanillaCaves() {
      return this.vanillaCaves;
   }

   public boolean vanillaDecorations() {
      return this.vanillaDecorations;
   }

   public boolean vanillaStructures() {
      return this.vanillaStructures;
   }

   public boolean doBetaCarvers() {
      return this.betaCarvers;
   }

   public ChunkGeneratorProvider getGeneratorProvider() {
      return this.generatorProvider;
   }

   public List<GenerationStage> getStages() {
      return this.stages;
   }

   public Version getVersion() {
      return this.version;
   }

   public boolean isDisableSaplings() {
      return this.disableSaplings;
   }

   public String getID() {
      return this.id;
   }

   public String getAuthor() {
      return this.author;
   }

   public Map<String, Double> getVariables() {
      return this.variables;
   }

   public int getElevationBlend() {
      return this.elevationBlend;
   }

   public Map<String, String> getLocatable() {
      return this.locatable;
   }

   public boolean getBiomeCache() {
      return this.biomeCache;
   }
}
