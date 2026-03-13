package me.casperge.interfaces;

import me.casperge.enums.GrassType;

public interface CustomBiome {
   void setFogColor(String var1);

   void setWaterColor(String var1);

   void setWaterFogColor(String var1);

   void setSkyColor(String var1);

   void setFoliageColor(String var1);

   void setGrassColor(String var1);

   void setFrozen(Boolean var1);

   void setDepth(Float var1);

   void setScale(Float var1);

   void setTemperature(Float var1);

   void setDownfall(Float var1);

   void register();

   String getName();

   String getPreName();

   String getFullName();

   boolean isRegistered();

   String getFogColor();

   String getWaterColor();

   String getWaterFogColor();

   String getSkyColor();

   String getFoliageColor();

   String getGrassColor();

   boolean isFrozen();

   float getDepth();

   float getScale();

   float getTemperature();

   float getDownfall();

   int getBiomeID();

   GrassType getGrassType();
}
