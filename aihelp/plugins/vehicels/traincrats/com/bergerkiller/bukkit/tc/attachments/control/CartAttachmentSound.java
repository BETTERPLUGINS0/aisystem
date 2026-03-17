package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.resources.ResourceKey;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.sound.MapWidgetSoundAutoResumeToggle;
import com.bergerkiller.bukkit.tc.attachments.control.sound.MapWidgetSoundPerspectiveMode;
import com.bergerkiller.bukkit.tc.attachments.control.sound.MapWidgetSoundPlayStop;
import com.bergerkiller.bukkit.tc.attachments.control.sound.MapWidgetSoundPositionMode;
import com.bergerkiller.bukkit.tc.attachments.control.sound.MapWidgetSoundSelector;
import com.bergerkiller.bukkit.tc.attachments.control.sound.MapWidgetSoundVolumePitch;
import com.bergerkiller.bukkit.tc.attachments.control.sound.SoundPerspectiveMode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutCustomSoundEffectHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutStopSoundHandle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CartAttachmentSound extends CartAttachment implements Attachment.EffectAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "SOUND";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/sound.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentSound();
      }

      public void createAppearanceTab(final Tab tab, final MapWidgetAttachmentNode attachment) {
         final MapWidgetSoundSelector soundSelector = (MapWidgetSoundSelector)tab.addWidget((new MapWidgetSoundSelector() {
            public void onAttached() {
               this.setMode(((SoundPerspectiveMode)attachment.getConfig().getOrDefault("perspectiveMode", SoundPerspectiveMode.SAME)).getSoundMode());
               this.setSoundPath((String)attachment.getConfig().getOrDefault("sound.key", String.class, (Object)null));
               this.setCategory((String)attachment.getConfig().getOrDefault("sound.category", "master"));
               super.onAttached();
            }

            public void onSoundChanged(ResourceKey<SoundEffect> sound) {
               attachment.getConfig().set("sound.key", sound == null ? null : sound.getPath());
            }

            public void onCategoryChanged(String categoryName) {
               attachment.getConfig().set("sound.category", categoryName);
            }
         }).setMode(MapWidgetSoundSelector.Mode.FIRST_PERSPECTIVE));
         soundSelector.setBounds(2, 18, 102, 11);
         final MapWidgetSoundSelector soundSelectorAlt = (MapWidgetSoundSelector)tab.addWidget((new MapWidgetSoundSelector() {
            public void onAttached() {
               this.setMode(((SoundPerspectiveMode)attachment.getConfig().getOrDefault("perspectiveMode", SoundPerspectiveMode.SAME)).getSoundAltMode());
               this.setSoundPath((String)attachment.getConfig().getOrDefault("soundAlt.key", String.class, (Object)null));
               this.setCategory((String)attachment.getConfig().getOrDefault("soundAlt.category", "master"));
               super.onAttached();
            }

            public void onSoundChanged(ResourceKey<SoundEffect> sound) {
               attachment.getConfig().set("soundAlt.key", sound == null ? null : sound.getPath());
            }

            public void onCategoryChanged(String categoryName) {
               attachment.getConfig().set("soundAlt.category", categoryName);
            }
         }).setMode(MapWidgetSoundSelector.Mode.THIRD_PERSPECTIVE));
         soundSelectorAlt.setBounds(2, 32, 102, 11);
         tab.addWidget((new MapWidgetSoundPerspectiveMode() {
            public void onAttached() {
               this.setMode((SoundPerspectiveMode)attachment.getConfig().getOrDefault("perspectiveMode", SoundPerspectiveMode.SAME));
               super.onAttached();
            }

            public void onModeChanged(SoundPerspectiveMode newMode) {
               attachment.getConfig().set("perspectiveMode", newMode);
               soundSelector.setMode(newMode.getSoundMode());
               soundSelectorAlt.setMode(newMode.getSoundAltMode());
               Iterator var2 = tab.getWidgets().iterator();

               while(var2.hasNext()) {
                  MapWidget widget = (MapWidget)var2.next();
                  if (widget instanceof MapWidgetSoundPositionMode) {
                     ((MapWidgetSoundPositionMode)widget).setIsSamePerspective(newMode == SoundPerspectiveMode.SAME);
                  }
               }

            }
         }).setPosition(9, 3));
         tab.addWidget((new MapWidgetSoundAutoResumeToggle() {
            public void onAttached() {
               this.setAutoResume((Boolean)attachment.getConfig().getOrDefault("autoResume", false));
               super.onAttached();
            }

            public void onAutoResumeChanged(boolean autoResume) {
               attachment.getConfig().set("autoResume", autoResume);
            }
         }).setPosition(21, 3));
         tab.addWidget((new MapWidgetSoundPositionMode() {
            public void onAttached() {
               SoundPerspectiveMode perspective = (SoundPerspectiveMode)attachment.getConfig().getOrDefault("perspectiveMode", SoundPerspectiveMode.SAME);
               this.setIsSamePerspective(perspective == SoundPerspectiveMode.SAME);
               this.setMode((Boolean)attachment.getConfig().getOrDefault("sound.atPlayer", false), (Boolean)attachment.getConfig().getOrDefault("soundAlt.atPlayer", false));
               super.onAttached();
            }

            public void onModeChanged(MapWidgetSoundPositionMode.SoundPositionMode newMode) {
               attachment.getConfig().set("sound.atPlayer", newMode.isAtPlayer1P());
               attachment.getConfig().set("soundAlt.atPlayer", newMode.isAtPlayer3P());
            }
         }).setPosition(33, 3));
         tab.addWidget((new MapWidgetSoundPlayStop() {
            public void onPlay() {
               attachment.getAttachmentsOfType(CartAttachmentSound.class).forEach((a) -> {
                  a.playEffect(Attachment.EffectAttachment.EffectOptions.DEFAULT);
               });
            }

            public void onStop() {
               attachment.getAttachmentsOfType(CartAttachmentSound.class).forEach(CartAttachmentSound::stopEffect);
            }
         }).setBounds(81, 3, 24, 11));
         ((<undefinedtype>)tab.addWidget(new MapWidgetSoundVolumePitch() {
            public void onAttached() {
               this.setInitialBaseVolume((double)(Float)attachment.getConfig().getOrDefault("volume.base", 1.0F));
               this.setInitialRandomVolume((double)(Float)attachment.getConfig().getOrDefault("volume.random", 0.0F));
               this.setInitialBaseSpeed((double)(Float)attachment.getConfig().getOrDefault("pitch.base", 1.0F));
               this.setInitialRandomSpeed((double)(Float)attachment.getConfig().getOrDefault("pitch.random", 0.0F));
               super.onAttached();
            }

            public void onChanged() {
               if (this.getBaseVolume() == 1.0D && this.getRandomVolume() == 0.0D) {
                  attachment.getConfig().remove("volume");
               } else {
                  attachment.getConfig().set("volume.base", this.getBaseVolume());
                  attachment.getConfig().set("volume.random", this.getRandomVolume());
               }

               if (this.getBaseSpeed() == 1.0D && this.getRandomSpeed() == 0.0D) {
                  attachment.getConfig().remove("pitch");
               } else {
                  attachment.getConfig().set("pitch.base", this.getBaseSpeed());
                  attachment.getConfig().set("pitch.random", this.getRandomSpeed());
               }

            }
         })).setBounds(-3, 47, 107, 30);
      }
   };
   private final CartAttachmentSound.SoundListeners listeners = new CartAttachmentSound.SoundListeners();
   private CartAttachmentSound.SoundConfiguration sound;

   public CartAttachmentSound() {
      this.sound = CartAttachmentSound.SoundConfiguration.NO_CONFIG;
   }

   public void onLoad(ConfigurationNode config) {
      CartAttachmentSound.SoundConfiguration newSound = new CartAttachmentSound.SoundConfiguration(config);
      boolean refreshSounds = !CartAttachmentSound.SoundConfiguration.isSameSounds(this.sound, newSound);
      this.sound = newSound;
      this.listeners.updateListeners(this, this.sound, refreshSounds);
   }

   public void makeVisible(Player viewer) {
      this.makeVisible(this.getManager().asAttachmentViewer(viewer));
   }

   public void makeHidden(Player viewer) {
      this.makeHidden(this.getManager().asAttachmentViewer(viewer));
   }

   public void makeVisible(AttachmentViewer viewer) {
      this.listeners.addListener(this, this.sound, viewer);
   }

   public void makeHidden(AttachmentViewer viewer) {
      this.listeners.removeListener(this, this.sound, viewer);
   }

   public void playEffect(Attachment.EffectAttachment.EffectOptions effectOptions) {
      this.listeners.play(this.sound.createVolumePitch(effectOptions));
   }

   public void stopEffect() {
      this.listeners.stop();
   }

   public void onTick() {
      this.listeners.updateListeners(this, this.sound, false);
   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.listeners.updateLoc(transform, this.getManager().getWorld());
   }

   public void onMove(boolean absolute) {
   }

   private static class SoundListeners {
      private final List<CartAttachmentSound.SoundListener> listeners;
      private CartAttachmentSound.VolumePitch lastVolumePitch;
      private Location loc;

      private SoundListeners() {
         this.listeners = new ArrayList();
         this.lastVolumePitch = CartAttachmentSound.VolumePitch.SILENT;
         this.loc = null;
      }

      public synchronized void play(CartAttachmentSound.VolumePitch volumePitch) {
         this.lastVolumePitch = volumePitch;
         if (!volumePitch.silent) {
            Location loc = this.loc;
            if (loc != null) {
               Iterator var3 = this.listeners.iterator();

               while(var3.hasNext()) {
                  CartAttachmentSound.SoundListener listener = (CartAttachmentSound.SoundListener)var3.next();
                  listener.play(loc, volumePitch);
               }
            }

         }
      }

      public synchronized void stop() {
         this.lastVolumePitch = CartAttachmentSound.VolumePitch.SILENT;
         this.listeners.forEach(CartAttachmentSound.SoundListener::stop);
      }

      public synchronized void addListener(CartAttachmentSound sound, CartAttachmentSound.SoundConfiguration config, AttachmentViewer viewer) {
         Iterator var4 = this.listeners.iterator();

         CartAttachmentSound.SoundListener listener;
         do {
            if (!var4.hasNext()) {
               boolean isAlt = this.detectIsAlt(sound, config, viewer);
               this.listeners.add(new CartAttachmentSound.SoundListener(viewer, isAlt, config.sound(isAlt)));
               return;
            }

            listener = (CartAttachmentSound.SoundListener)var4.next();
         } while(!listener.viewer.equals(viewer));

      }

      public synchronized void removeListener(CartAttachmentSound sound, CartAttachmentSound.SoundConfiguration config, AttachmentViewer viewer) {
         Iterator iter = this.listeners.iterator();

         CartAttachmentSound.SoundListener listener;
         do {
            if (!iter.hasNext()) {
               return;
            }

            listener = (CartAttachmentSound.SoundListener)iter.next();
         } while(!viewer.equals(listener.viewer));

         iter.remove();
         if (config.autoResume) {
            listener.stop();
         }

      }

      public void updateLoc(Matrix4x4 transform, World world) {
         this.loc = transform.toLocation(world);
      }

      public void updateListeners(CartAttachmentSound sound, CartAttachmentSound.SoundConfiguration config, boolean forceRefreshSounds) {
         Iterator var4 = this.listeners.iterator();

         while(true) {
            CartAttachmentSound.SoundListener listener;
            boolean isAlt;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               listener = (CartAttachmentSound.SoundListener)var4.next();
               isAlt = this.detectIsAlt(sound, config, listener.viewer);
            } while(!forceRefreshSounds && isAlt == listener.isAlt);

            synchronized(this) {
               if (config.autoResume) {
                  listener.stop();
               }

               listener.isAlt = isAlt;
               listener.sound = config.sound(isAlt);
               Location loc;
               if (config.autoResume && !this.lastVolumePitch.silent && (loc = this.loc) != null) {
                  listener.playResume(loc, this.lastVolumePitch);
               }
            }
         }
      }

      private boolean detectIsAlt(CartAttachmentSound sound, CartAttachmentSound.SoundConfiguration config, AttachmentViewer viewer) {
         if (config.perspectiveMode == SoundPerspectiveMode.SAME) {
            return false;
         } else {
            MinecartMember<?> member = MinecartMemberStore.getFromEntity(viewer.getPlayer().getVehicle());
            if (member != null && !member.isUnloaded()) {
               MinecartMember<?> soundMember = sound.getMember();
               if (soundMember != null && !soundMember.isUnloaded()) {
                  switch(config.perspectiveMode) {
                  case CART:
                     return member != soundMember;
                  case TRAIN:
                     return member.getGroup() != soundMember.getGroup();
                  case SEAT:
                     if (member == soundMember) {
                        for(Attachment a = sound.getParent(); a != null; a = a.getParent()) {
                           if (a instanceof CartAttachmentSeat && ((CartAttachmentSeat)a).getEntity() == viewer.getPlayer()) {
                              return false;
                           }
                        }
                     }

                     return true;
                  default:
                     return true;
                  }
               } else {
                  return true;
               }
            } else {
               return true;
            }
         }
      }

      // $FF: synthetic method
      SoundListeners(Object x0) {
         this();
      }
   }

   private static class SoundConfiguration {
      public static final CartAttachmentSound.SoundConfiguration NO_CONFIG = new CartAttachmentSound.SoundConfiguration(new ConfigurationNode());
      public final CartAttachmentSound.SoundType sound;
      public final CartAttachmentSound.SoundType soundAlt;
      public final SoundPerspectiveMode perspectiveMode;
      public final boolean autoResume;
      public final CartAttachmentSound.VariableFloatRange volume;
      public final CartAttachmentSound.VariableFloatRange pitch;

      public SoundConfiguration(ConfigurationNode config) {
         this.sound = new CartAttachmentSound.SoundType(config.getNodeIfExists("sound"));
         this.soundAlt = new CartAttachmentSound.SoundType(config.getNodeIfExists("soundAlt"));
         if (CartAttachmentSound.SoundType.isSameSound(this.sound, this.soundAlt)) {
            this.perspectiveMode = SoundPerspectiveMode.SAME;
         } else {
            this.perspectiveMode = (SoundPerspectiveMode)config.getOrDefault("perspectiveMode", SoundPerspectiveMode.SAME);
         }

         this.autoResume = (Boolean)config.getOrDefault("autoResume", false);
         this.volume = CartAttachmentSound.VariableFloatRange.decode(config.getNodeIfExists("volume"));
         this.pitch = CartAttachmentSound.VariableFloatRange.decode(config.getNodeIfExists("pitch"));
      }

      public CartAttachmentSound.SoundType sound(boolean isAlt) {
         return isAlt ? this.soundAlt : this.sound;
      }

      public CartAttachmentSound.VolumePitch createVolumePitch(Attachment.EffectAttachment.EffectOptions effectOptions) {
         return new CartAttachmentSound.VolumePitch((float)(effectOptions.volume() * (double)this.volume.next()), (float)(effectOptions.speed() * (double)this.pitch.next()));
      }

      public static boolean isSameSounds(CartAttachmentSound.SoundConfiguration a, CartAttachmentSound.SoundConfiguration b) {
         return CartAttachmentSound.SoundType.isSameSound(a.sound, b.sound) && CartAttachmentSound.SoundType.isSameSound(a.soundAlt, b.soundAlt);
      }
   }

   private static class VolumePitch {
      public static final CartAttachmentSound.VolumePitch SILENT = new CartAttachmentSound.VolumePitch(0.0F, 1.0F);
      public final float volume;
      public final float pitch;
      public final boolean silent;

      public VolumePitch(float volume, float pitch) {
         this.volume = volume;
         this.silent = volume < 1.0E-4F;
         this.pitch = pitch;
      }
   }

   private static class RandomFloat implements CartAttachmentSound.VariableFloatRange {
      private final Random random = new Random();
      private final float base;
      private final float mult;

      public RandomFloat(float base, float mult) {
         this.base = base - mult;
         this.mult = 2.0F * mult;
      }

      public float next() {
         return this.base + this.random.nextFloat(this.mult);
      }
   }

   @FunctionalInterface
   private interface VariableFloatRange {
      CartAttachmentSound.VariableFloatRange DEFAULT = () -> {
         return 1.0F;
      };

      float next();

      static CartAttachmentSound.VariableFloatRange decode(ConfigurationNode node) {
         return node == null ? DEFAULT : get((Float)node.getOrDefault("base", 1.0F), (Float)node.getOrDefault("random", 0.0F));
      }

      static CartAttachmentSound.VariableFloatRange get(float base, float random) {
         if (random < 1.0E-4F) {
            return Math.abs(base - 1.0F) < 1.0E-4F ? DEFAULT : () -> {
               return base;
            };
         } else {
            return new CartAttachmentSound.RandomFloat(base, random);
         }
      }
   }

   private static class SoundListener {
      public final AttachmentViewer viewer;
      public final AtomicBoolean isResumingPlay;
      public boolean isAlt;
      public CartAttachmentSound.SoundType sound;

      public SoundListener(AttachmentViewer viewer, boolean isAlt, CartAttachmentSound.SoundType sound) {
         this.viewer = viewer;
         this.isResumingPlay = new AtomicBoolean(false);
         this.isAlt = isAlt;
         this.sound = sound;
      }

      public void stop() {
         this.isResumingPlay.set(false);
         this.sound.stop(this.viewer);
      }

      public void play(Location location, CartAttachmentSound.VolumePitch volumePitch) {
         CartAttachmentSound.SoundType sound = this.sound;
         if (this.isResumingPlay.compareAndSet(true, false)) {
            sound.stop(this.viewer);
         }

         sound.play(this.viewer, location, volumePitch);
      }

      public void playResume(Location location, CartAttachmentSound.VolumePitch volumePitch) {
         this.isResumingPlay.set(true);
         this.sound.play(this.viewer, location, volumePitch);
      }
   }

   private static class SoundType {
      private static final Random RANDOM_SEED_SOURCE = new Random();
      private static final boolean CAN_STOP_SOUND = Common.hasCapability("Common:Sound:StopSoundPacket");
      public final ResourceKey<SoundEffect> key;
      public final String category;
      public final boolean atPlayer;

      public SoundType(ConfigurationNode config) {
         if (config != null) {
            String keyPath = (String)config.getOrDefault("key", String.class, (Object)null);
            this.key = keyPath == null ? null : SoundEffect.fromName(keyPath);
            this.category = (String)config.getOrDefault("category", "master");
            this.atPlayer = (Boolean)config.getOrDefault("atPlayer", false);
         } else {
            this.key = null;
            this.category = "master";
            this.atPlayer = false;
         }

      }

      public boolean exists() {
         return this.key != null;
      }

      public void play(AttachmentViewer viewer, Location location, CartAttachmentSound.VolumePitch volumePitch) {
         if (this.key != null) {
            Location at = this.atPlayer ? viewer.getPlayer().getLocation() : location;
            viewer.send((PacketHandle)PacketPlayOutCustomSoundEffectHandle.createNew(this.key, this.category, at.getX(), at.getY(), at.getZ(), volumePitch.volume, volumePitch.pitch, RANDOM_SEED_SOURCE.nextLong()));
         }

      }

      public void stop(AttachmentViewer viewer) {
         if (this.key != null && CAN_STOP_SOUND) {
            this.stopImpl(viewer);
         }

      }

      private void stopImpl(AttachmentViewer viewer) {
         viewer.send((PacketHandle)PacketPlayOutStopSoundHandle.createNew(this.key, this.category));
      }

      public static boolean isSameSound(CartAttachmentSound.SoundType a, CartAttachmentSound.SoundType b) {
         return LogicUtil.bothNullOrEqual(a.key, b.key) && a.category.equals(b.category) && a.atPlayer == b.atPlayer;
      }
   }
}
