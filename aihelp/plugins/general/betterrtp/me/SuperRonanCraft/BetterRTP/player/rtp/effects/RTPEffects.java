package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

public class RTPEffects {
   final RTPEffect_Particles particles = new RTPEffect_Particles();
   final RTPEffect_Potions potions = new RTPEffect_Potions();
   final RTPEffect_Sounds sounds = new RTPEffect_Sounds();
   final RTPEffect_Titles titles = new RTPEffect_Titles();

   public void load() {
      this.particles.load();
      this.potions.load();
      this.sounds.load();
      this.titles.load();
   }

   public RTPEffect_Particles getParticles() {
      return this.particles;
   }

   public RTPEffect_Potions getPotions() {
      return this.potions;
   }

   public RTPEffect_Sounds getSounds() {
      return this.sounds;
   }

   public RTPEffect_Titles getTitles() {
      return this.titles;
   }
}
