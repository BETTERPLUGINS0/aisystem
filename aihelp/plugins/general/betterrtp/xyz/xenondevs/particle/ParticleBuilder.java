package xyz.xenondevs.particle;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.data.ParticleData;
import xyz.xenondevs.particle.data.color.RegularColor;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public class ParticleBuilder {
   private final ParticleEffect particle;
   private Location location;
   private float offsetX = 0.0F;
   private float offsetY = 0.0F;
   private float offsetZ = 0.0F;
   private float speed = 1.0F;
   private int amount = 0;
   private ParticleData particleData = null;

   public ParticleBuilder(ParticleEffect particle, Location location) {
      this.particle = particle;
      this.location = location;
   }

   public ParticleBuilder(ParticleEffect particle) {
      this.particle = particle;
      this.location = null;
   }

   public ParticleEffect getParticle() {
      return this.particle;
   }

   public ParticleBuilder setLocation(Location location) {
      this.location = location;
      return this;
   }

   public Location getLocation() {
      return this.location;
   }

   public ParticleBuilder setOffsetX(float offsetX) {
      this.offsetX = offsetX;
      return this;
   }

   public float getOffsetX() {
      return this.offsetX;
   }

   public ParticleBuilder setOffsetY(float offsetY) {
      this.offsetY = offsetY;
      return this;
   }

   public ParticleBuilder setOffset(float offsetX, float offsetY, float offsetZ) {
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.offsetZ = offsetZ;
      return this;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public ParticleBuilder setOffset(Vector offset) {
      this.offsetX = (float)offset.getX();
      this.offsetY = (float)offset.getY();
      this.offsetZ = (float)offset.getZ();
      return this;
   }

   public ParticleBuilder setOffsetZ(float offsetZ) {
      this.offsetZ = offsetZ;
      return this;
   }

   public float getOffsetZ() {
      return this.offsetZ;
   }

   public ParticleBuilder setSpeed(float speed) {
      this.speed = speed;
      return this;
   }

   public float getSpeed() {
      return this.speed;
   }

   public ParticleBuilder setAmount(int amount) {
      this.amount = amount;
      return this;
   }

   public int getAmount() {
      return this.amount;
   }

   public ParticleBuilder setParticleData(ParticleData particleData) {
      this.particleData = particleData;
      return this;
   }

   public ParticleData getParticleData() {
      return this.particleData;
   }

   public ParticleBuilder setColor(Color color) {
      if (this.particle.hasProperty(PropertyType.COLORABLE)) {
         this.particleData = new RegularColor(color);
      }

      return this;
   }

   public Object toPacket() {
      if (this.location == null) {
         throw new IllegalStateException("Missing location of particle.");
      } else {
         if (this.particleData != null) {
            this.particleData.setEffect(this.particle);
         }

         ParticlePacket packet = new ParticlePacket(this.particle, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.amount, this.particleData);
         return packet.createPacket(this.location);
      }
   }

   public void display() {
      Object packet = this.toPacket();
      ((World)Objects.requireNonNull(this.location.getWorld())).getPlayers().forEach((p) -> {
         ReflectionUtils.sendPacket(p, packet);
      });
   }

   public void display(Player... players) {
      this.display((Collection)Arrays.asList(players));
   }

   public void display(Predicate<Player> filter) {
      Object packet = this.toPacket();
      Bukkit.getOnlinePlayers().stream().filter((p) -> {
         return filter.test(p) && p.getWorld().equals(this.location.getWorld());
      }).forEach((p) -> {
         ReflectionUtils.sendPacket(p, packet);
      });
   }

   public void display(Collection<? extends Player> players) {
      Object packet = this.toPacket();
      players.stream().filter((p) -> {
         return p.getWorld().equals(this.location.getWorld());
      }).forEach((p) -> {
         ReflectionUtils.sendPacket(p, packet);
      });
   }
}
