package com.lenis0012.bukkit.loginsecurity.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PlayerLocation {
   private int id;
   private String world;
   private double x;
   private double y;
   private double z;
   private int yaw;
   private int pitch;

   public PlayerLocation() {
   }

   public PlayerLocation(Location location) {
      this.setWorld(location.getWorld().getName());
      this.setX(location.getX());
      this.setY(location.getY());
      this.setZ(location.getZ());
      this.setYaw((int)location.getYaw());
      this.setPitch((int)location.getPitch());
   }

   public Location asLocation() {
      if (this.getWorld() == null) {
         return null;
      } else {
         World bukkitWorld = Bukkit.getWorld(this.getWorld());
         return bukkitWorld == null ? null : new Location(bukkitWorld, this.getX(), this.getY(), this.getZ(), (float)this.getYaw(), (float)this.getPitch());
      }
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getWorld() {
      return this.world;
   }

   public void setWorld(String world) {
      this.world = world;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public int getYaw() {
      return this.yaw;
   }

   public void setYaw(int yaw) {
      this.yaw = yaw;
   }

   public int getPitch() {
      return this.pitch;
   }

   public void setPitch(int pitch) {
      this.pitch = pitch;
   }
}
