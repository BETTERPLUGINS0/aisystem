package com.lenis0012.bukkit.loginsecurity.storage;

public class PlayerInventory {
   private int id;
   private String helmet;
   private String chestplate;
   private String leggings;
   private String boots;
   private String offHand;
   private String contents;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getHelmet() {
      return this.helmet;
   }

   public void setHelmet(String helmet) {
      this.helmet = helmet;
   }

   public String getChestplate() {
      return this.chestplate;
   }

   public void setChestplate(String chestplate) {
      this.chestplate = chestplate;
   }

   public String getLeggings() {
      return this.leggings;
   }

   public void setLeggings(String leggings) {
      this.leggings = leggings;
   }

   public String getBoots() {
      return this.boots;
   }

   public void setBoots(String boots) {
      this.boots = boots;
   }

   public String getOffHand() {
      return this.offHand;
   }

   public void setOffHand(String offHand) {
      this.offHand = offHand;
   }

   public String getContents() {
      return this.contents;
   }

   public void setContents(String contents) {
      this.contents = contents;
   }
}
