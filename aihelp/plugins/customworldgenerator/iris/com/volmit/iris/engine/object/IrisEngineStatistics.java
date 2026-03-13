package com.volmit.iris.engine.object;

import lombok.Generated;

public class IrisEngineStatistics {
   private int totalHotloads = 0;
   private int chunksGenerated = 0;
   private int IrisToUpgradedVersion = 0;
   private int IrisCreationVersion = 0;
   private int MinecraftVersion = 0;

   public void generatedChunk() {
      ++this.chunksGenerated;
   }

   public void setUpgradedVersion(int i) {
      this.IrisToUpgradedVersion = var1;
   }

   public int getUpgradedVersion() {
      return this.IrisToUpgradedVersion;
   }

   public void setVersion(int i) {
      this.IrisCreationVersion = var1;
   }

   public int getVersion() {
      return this.IrisCreationVersion;
   }

   public void setMCVersion(int i) {
      this.MinecraftVersion = var1;
   }

   public int getMCVersion() {
      return this.MinecraftVersion;
   }

   public void hotloaded() {
      ++this.totalHotloads;
   }

   @Generated
   public int getTotalHotloads() {
      return this.totalHotloads;
   }

   @Generated
   public int getChunksGenerated() {
      return this.chunksGenerated;
   }

   @Generated
   public int getIrisToUpgradedVersion() {
      return this.IrisToUpgradedVersion;
   }

   @Generated
   public int getIrisCreationVersion() {
      return this.IrisCreationVersion;
   }

   @Generated
   public int getMinecraftVersion() {
      return this.MinecraftVersion;
   }

   @Generated
   public void setTotalHotloads(final int totalHotloads) {
      this.totalHotloads = var1;
   }

   @Generated
   public void setChunksGenerated(final int chunksGenerated) {
      this.chunksGenerated = var1;
   }

   @Generated
   public void setIrisToUpgradedVersion(final int IrisToUpgradedVersion) {
      this.IrisToUpgradedVersion = var1;
   }

   @Generated
   public void setIrisCreationVersion(final int IrisCreationVersion) {
      this.IrisCreationVersion = var1;
   }

   @Generated
   public void setMinecraftVersion(final int MinecraftVersion) {
      this.MinecraftVersion = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEngineStatistics)) {
         return false;
      } else {
         IrisEngineStatistics var2 = (IrisEngineStatistics)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getTotalHotloads() != var2.getTotalHotloads()) {
            return false;
         } else if (this.getChunksGenerated() != var2.getChunksGenerated()) {
            return false;
         } else if (this.getIrisToUpgradedVersion() != var2.getIrisToUpgradedVersion()) {
            return false;
         } else if (this.getIrisCreationVersion() != var2.getIrisCreationVersion()) {
            return false;
         } else {
            return this.getMinecraftVersion() == var2.getMinecraftVersion();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEngineStatistics;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getTotalHotloads();
      var3 = var3 * 59 + this.getChunksGenerated();
      var3 = var3 * 59 + this.getIrisToUpgradedVersion();
      var3 = var3 * 59 + this.getIrisCreationVersion();
      var3 = var3 * 59 + this.getMinecraftVersion();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getTotalHotloads();
      return "IrisEngineStatistics(totalHotloads=" + var10000 + ", chunksGenerated=" + this.getChunksGenerated() + ", IrisToUpgradedVersion=" + this.getIrisToUpgradedVersion() + ", IrisCreationVersion=" + this.getIrisCreationVersion() + ", MinecraftVersion=" + this.getMinecraftVersion() + ")";
   }
}
