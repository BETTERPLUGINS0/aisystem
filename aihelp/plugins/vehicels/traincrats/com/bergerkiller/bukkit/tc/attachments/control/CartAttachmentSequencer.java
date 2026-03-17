package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.controller.Tickable;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentNameLookup;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelection;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelector;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.control.effect.DelayedEffectTask;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.effect.ScheduledEffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.sequencer.MapWidgetSequencerConfigurationMenu;
import com.bergerkiller.bukkit.tc.attachments.control.sequencer.SequencerMode;
import com.bergerkiller.bukkit.tc.attachments.control.sequencer.SequencerPlayStatus;
import com.bergerkiller.bukkit.tc.attachments.control.sequencer.SequencerType;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionBoolean;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionConstant;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionRegistry;
import com.bergerkiller.bukkit.tc.controller.functions.inputs.TransferFunctionInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.bukkit.entity.Player;

public class CartAttachmentSequencer extends CartAttachment implements Attachment.EffectAttachment {
   public static final AttachmentType TYPE = new AttachmentType() {
      public String getID() {
         return "SEQUENCER";
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/sequencer.png");
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentSequencer();
      }

      public void createAppearanceTab(Tab tab, final MapWidgetAttachmentNode attachment) {
         final TransferFunctionHost host = new TransferFunctionHost() {
            public TransferFunctionRegistry getRegistry() {
               return TransferFunction.getRegistry();
            }

            public TransferFunctionInput.ReferencedSource registerInputSource(TransferFunctionInput.ReferencedSource source) {
               return source;
            }

            public boolean isSequencer() {
               return true;
            }

            public boolean isAttachment() {
               return true;
            }

            public MinecartMember<?> getMember() {
               return null;
            }

            public Attachment getAttachment() {
               List<Attachment> attachments = attachment.getAttachments();
               return attachments.isEmpty() ? null : (Attachment)attachments.get(0);
            }

            public TrainCarts getTrainCarts() {
               return TrainCarts.plugin;
            }
         };
         ((<undefinedtype>)tab.addWidget(new MapWidgetSequencerConfigurationMenu() {
            public ConfigurationNode getConfig() {
               return attachment.getConfig();
            }

            public List<String> getEffectNames(AttachmentSelector<Attachment.EffectAttachment> allSelector) {
               return AttachmentNameLookup.Supplier.getSelection(allSelector, () -> {
                  return attachment.getAttachmentConfig().liveAttachmentsOfType(CartAttachmentSequencer.class);
               }).names();
            }

            public TransferFunctionHost getTransferFunctionHost() {
               return host;
            }

            public Attachment.EffectSink createEffectSink(AttachmentSelector<Attachment.EffectAttachment> effectSelector) {
               return Attachment.EffectSink.combineEffects((Iterable)AttachmentNameLookup.Supplier.getSelection(effectSelector, () -> {
                  return attachment.getAttachmentsOfType(CartAttachmentSequencer.class);
               }));
            }

            public SequencerPlayStatus getPlayStatus() {
               List<CartAttachmentSequencer> sequencers = attachment.getAttachmentsOfType(CartAttachmentSequencer.class);
               if (sequencers.isEmpty()) {
                  return SequencerPlayStatus.STOPPED_AUTOMATIC;
               } else if (sequencers.size() == 1) {
                  return ((CartAttachmentSequencer)sequencers.get(0)).getPlayStatus();
               } else {
                  Iterator var2 = sequencers.iterator();

                  SequencerPlayStatus playStatus;
                  do {
                     if (!var2.hasNext()) {
                        return ((CartAttachmentSequencer)sequencers.get(0)).getPlayStatus();
                     }

                     CartAttachmentSequencer sequencer = (CartAttachmentSequencer)var2.next();
                     playStatus = sequencer.getPlayStatus();
                  } while(!playStatus.isPlaying());

                  return playStatus;
               }
            }

            public void startPlaying() {
               attachment.getAttachmentsOfType(CartAttachmentSequencer.class).forEach((a) -> {
                  a.playEffect(Attachment.EffectAttachment.EffectOptions.DEFAULT);
               });
            }

            public void stopPlaying() {
               attachment.getAttachmentsOfType(CartAttachmentSequencer.class).forEach(CartAttachmentSequencer::stopEffect);
            }
         })).setBounds(-5, 1, 110, 81);
      }
   };
   private static final int STATE_NOT_PLAYING = 0;
   private static final int STATE_PLAYING = 1;
   private static final int STATE_STOP_REQUESTED = 2;
   private static final int STATE_IMMEDIATE_STOP_REQUESTED = 3;
   private final EffectLoop.Player player;
   private final CartAttachmentSequencer.SequencerTransferFunctionHost functionHost;
   private final EnumMap<SequencerMode, CartAttachmentSequencer.SequencerGroup> sequencerGroups;
   private CartAttachmentSequencer.SequencerGroup currentGroup;
   private EffectLoop.RunMode runMode;
   private final CartAttachmentSequencer.ConfigLoadedValue<TransferFunction> autoplayFunction;
   private final AtomicInteger playState;
   private Attachment.EffectAttachment.EffectOptions playOptions;
   private SequencerPlayStatus autoPlayStatus;
   private SequencerPlayStatus playStatus;

   public CartAttachmentSequencer() {
      this.player = TrainCarts.plugin.getEffectLoopPlayerController().createPlayer(20);
      this.functionHost = new CartAttachmentSequencer.SequencerTransferFunctionHost();
      this.runMode = EffectLoop.RunMode.ASYNCHRONOUS;
      this.autoplayFunction = new CartAttachmentSequencer.ConfigLoadedValue(TransferFunctionBoolean.FALSE);
      this.playState = new AtomicInteger(0);
      this.playOptions = Attachment.EffectAttachment.EffectOptions.DEFAULT;
      this.autoPlayStatus = SequencerPlayStatus.STOPPED_AUTOMATIC;
      this.playStatus = SequencerPlayStatus.STOPPED_AUTOMATIC;
      this.sequencerGroups = new EnumMap(SequencerMode.class);
      SequencerMode[] var1 = SequencerMode.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SequencerMode mode = var1[var3];
         this.sequencerGroups.put(mode, new CartAttachmentSequencer.SequencerGroup(this, mode));
      }

      this.currentGroup = (CartAttachmentSequencer.SequencerGroup)this.sequencerGroups.get(SequencerMode.START);
   }

   public void onLoad(ConfigurationNode config) {
      this.runMode = (EffectLoop.RunMode)config.getOrDefault("runMode", EffectLoop.RunMode.ASYNCHRONOUS);
      CartAttachmentSequencer.ConfigLoadedValue var10000 = this.autoplayFunction;
      ConfigurationNode var10001 = config.getNodeIfExists("autoplay");
      CartAttachmentSequencer.SequencerTransferFunctionHost var10002 = this.functionHost;
      Objects.requireNonNull(var10002);
      var10000.load(var10001, var10002::loadFunction);
      SequencerMode[] var2 = SequencerMode.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SequencerMode mode = var2[var4];
         ((CartAttachmentSequencer.SequencerGroup)this.sequencerGroups.get(mode)).load(config.getNodeIfExists(mode.configKey()));
      }

   }

   public void onDetached() {
      this.immediateStop();
      Iterator var1 = this.sequencerGroups.values().iterator();

      while(var1.hasNext()) {
         CartAttachmentSequencer.SequencerGroup group = (CartAttachmentSequencer.SequencerGroup)var1.next();
         group.onDetached();
      }

   }

   public Attachment.EffectAttachment.EffectOptions getCurrentPlayOptions() {
      return this.playOptions;
   }

   public double getProgression() {
      return Math.min(1.0D, (double)this.currentGroup.nanosElapsed / (double)this.currentGroup.duration.nanos);
   }

   public SequencerPlayStatus getPlayStatus() {
      return this.playStatus;
   }

   public void playEffect(Attachment.EffectAttachment.EffectOptions options) {
      this.playOptions = options;
      this.updatePlayStatus(SequencerPlayStatus.PLAYING_MANUAL);
   }

   public void stopEffect() {
      this.updatePlayStatus(SequencerPlayStatus.STOPPED_MANUAL);
   }

   private void updatePlayStatus(SequencerPlayStatus status) {
      this.playStatus = status;
      if (status.isPlaying()) {
         int prevState = this.playState.getAndSet(1);
         if (prevState == 0) {
            (new CartAttachmentSequencer.ActiveEffectLoop(this.player, this.runMode)).play();
         }
      } else {
         this.playState.compareAndSet(1, 2);
      }

   }

   private void immediateStop() {
      this.autoPlayStatus = SequencerPlayStatus.STOPPED_AUTOMATIC;
      this.playStatus = SequencerPlayStatus.STOPPED_AUTOMATIC;
      this.playState.compareAndSet(1, 3);
      this.playState.compareAndSet(2, 3);
   }

   public void makeVisible(Player viewer) {
   }

   public void makeHidden(Player viewer) {
   }

   public void onTick() {
      MinecartMember<?> member = this.getMember();
      if (member != null && !member.isUnloaded()) {
         SequencerPlayStatus currAutoPlayStatus = ((TransferFunction)this.autoplayFunction.get()).map(0.0D) != 0.0D ? SequencerPlayStatus.PLAYING_AUTOMATIC : SequencerPlayStatus.STOPPED_AUTOMATIC;
         if (this.autoPlayStatus != currAutoPlayStatus) {
            this.autoPlayStatus = currAutoPlayStatus;
            this.updatePlayStatus(currAutoPlayStatus);
         }

         this.sequencerGroups.values().forEach(CartAttachmentSequencer.SequencerGroup::onTick);
         this.functionHost.sources.removeIf((s) -> {
            if (s.hasRecipients()) {
               if (!s.isTickedDuringPlay()) {
                  s.onTick();
               }

               return false;
            } else {
               this.functionHost.onSourceRemoved(s);
               return true;
            }
         });
      } else {
         if (this.playStatus.isPlaying()) {
            this.immediateStop();
         }

      }
   }

   public void onTransformChanged(Matrix4x4 transform) {
      this.functionHost.sources.forEach((s) -> {
         s.onTransform(transform);
      });
   }

   public void onMove(boolean absolute) {
   }

   public class SequencerTransferFunctionHost implements TransferFunctionHost {
      private final List<TransferFunctionInput.ReferencedSource> sources = new ArrayList();
      private List<TransferFunctionInput.ReferencedSource> sourcesTickedDuringPlay = Collections.emptyList();

      public TrainCarts getTrainCarts() {
         return TrainCarts.plugin;
      }

      public TransferFunctionRegistry getRegistry() {
         return TransferFunction.getRegistry();
      }

      public void tickPlaySources() {
         this.sourcesTickedDuringPlay.forEach(TransferFunctionInput.ReferencedSource::onTick);
      }

      public void onSourceRemoved(TransferFunctionInput.ReferencedSource source) {
         int idx = this.sourcesTickedDuringPlay.indexOf(source);
         if (idx != -1) {
            List<TransferFunctionInput.ReferencedSource> newList = new ArrayList(this.sourcesTickedDuringPlay);
            newList.remove(idx);
            this.sourcesTickedDuringPlay = newList;
         }

      }

      public TransferFunctionInput.ReferencedSource registerInputSource(TransferFunctionInput.ReferencedSource source) {
         int index = this.sources.indexOf(source);
         if (index == -1) {
            this.sources.add(source);
            if (source.isTickedDuringPlay()) {
               List<TransferFunctionInput.ReferencedSource> newList = new ArrayList(this.sourcesTickedDuringPlay);
               newList.add(source);
               this.sourcesTickedDuringPlay = newList;
            }

            return source;
         } else {
            return (TransferFunctionInput.ReferencedSource)this.sources.get(index);
         }
      }

      public boolean isSequencer() {
         return true;
      }

      public boolean isAttachment() {
         return true;
      }

      public Attachment getAttachment() {
         return CartAttachmentSequencer.this;
      }

      public MinecartMember<?> getMember() {
         return CartAttachmentSequencer.this.getMember();
      }
   }

   public static class SequencerGroup implements Tickable {
      private final CartAttachmentSequencer.ConfigLoadedValue<TransferFunction> speedFunction = new CartAttachmentSequencer.ConfigLoadedValue(TransferFunctionConstant.of(1.0D));
      private final CartAttachmentSequencer sequencer;
      private final SequencerMode mode;
      private EffectLoop.Time duration;
      private long nanosElapsed;
      private boolean interruptPlay;
      private final Map<ConfigurationNode, CartAttachmentSequencer.SequencerEffect> effectsByConfig;
      private List<CartAttachmentSequencer.SequencerEffect> effects;

      public SequencerGroup(CartAttachmentSequencer sequencer, SequencerMode mode) {
         this.duration = EffectLoop.Time.ZERO;
         this.nanosElapsed = 0L;
         this.interruptPlay = false;
         this.effectsByConfig = new IdentityHashMap();
         this.effects = Collections.emptyList();
         this.sequencer = sequencer;
         this.mode = mode;
      }

      public SequencerMode mode() {
         return this.mode;
      }

      public void onDetached() {
         this.effects.forEach(CartAttachmentSequencer.SequencerEffect::onRemoved);
      }

      public void load(ConfigurationNode config) {
         if (config != null && !config.isEmpty()) {
            CartAttachmentSequencer.ConfigLoadedValue var10000 = this.speedFunction;
            ConfigurationNode var10001 = config.getNodeIfExists("speed");
            CartAttachmentSequencer.SequencerTransferFunctionHost var10002 = this.sequencer.functionHost;
            Objects.requireNonNull(var10002);
            var10000.load(var10001, var10002::loadFunction);
            this.duration = EffectLoop.Time.seconds(Math.max(0.0D, (Double)config.getOrDefault("duration", 0.0D)));
            this.interruptPlay = (Boolean)config.getOrDefault("interrupt", false);
            List effectConfigs;
            if (!this.duration.isZero() && !(effectConfigs = config.getNodeList("effects")).isEmpty()) {
               List<CartAttachmentSequencer.SequencerEffect> newEffects = new ArrayList(effectConfigs.size());
               Iterator var4 = effectConfigs.iterator();

               while(var4.hasNext()) {
                  ConfigurationNode effectConfig = (ConfigurationNode)var4.next();
                  CartAttachmentSequencer.SequencerEffect effect = (CartAttachmentSequencer.SequencerEffect)this.effectsByConfig.remove(effectConfig);
                  if (effect == null) {
                     effect = new CartAttachmentSequencer.SequencerEffect(this);
                  }

                  effect.load(this.sequencer, effectConfig);
                  newEffects.add(effect);
               }

               this.effectsByConfig.values().forEach(CartAttachmentSequencer.SequencerEffect::onRemoved);
               this.effectsByConfig.clear();

               for(int i = 0; i < newEffects.size(); ++i) {
                  this.effectsByConfig.put((ConfigurationNode)effectConfigs.get(i), (CartAttachmentSequencer.SequencerEffect)newEffects.get(i));
               }

               this.effects = newEffects;
            } else {
               this.effects = Collections.emptyList();
            }

         } else {
            this.speedFunction.reset();
            this.duration = EffectLoop.Time.ZERO;
            this.interruptPlay = false;
            this.nanosElapsed = 0L;
            this.effects.forEach(CartAttachmentSequencer.SequencerEffect::onRemoved);
            this.effects = Collections.emptyList();
            this.effectsByConfig.clear();
         }
      }

      public void onTick() {
         this.effects.forEach(CartAttachmentSequencer.SequencerEffect::onTick);
      }

      public EffectLoop.Time advance(EffectLoop.Time dt, boolean stopRequested) {
         long durationNanos = this.duration.nanos;
         if (stopRequested && this.interruptPlay) {
            return dt;
         } else if (durationNanos != 0L) {
            this.sequencer.functionHost.tickPlaySources();
            double speed = ((TransferFunction)this.speedFunction.get()).map(0.0D);
            this.effects.forEach(CartAttachmentSequencer.SequencerEffect::updateEffectLoop);
            if (speed <= 1.0E-6D) {
               return EffectLoop.Time.ZERO;
            } else {
               EffectLoop.Time dt_adjusted = speed == 1.0D ? dt : dt.multiply(speed);
               long prev_time_nanos = this.nanosElapsed;
               long curr_time_nanos = prev_time_nanos + dt_adjusted.nanos;
               if (curr_time_nanos <= durationNanos) {
                  this.advanceAllEffects(prev_time_nanos, curr_time_nanos);
                  return EffectLoop.Time.ZERO;
               } else if (this.mode == SequencerMode.LOOP && !stopRequested) {
                  long remainder = curr_time_nanos - durationNanos;
                  if (remainder >= durationNanos) {
                     remainder %= durationNanos;
                  }

                  this.advanceAllEffects(prev_time_nanos, durationNanos);
                  this.advanceAllEffects(0L, remainder);
                  return EffectLoop.Time.ZERO;
               } else {
                  this.advanceAllEffects(prev_time_nanos, durationNanos);
                  return EffectLoop.Time.nanos(Math.max(1L, (long)((double)(curr_time_nanos - durationNanos) / speed)));
               }
            }
         } else {
            return !stopRequested && this.mode == SequencerMode.LOOP ? EffectLoop.Time.ZERO : dt;
         }
      }

      private void advanceAllEffects(long prevNanos, long currNanos) {
         this.effects.forEach((e) -> {
            ((ScheduledEffectLoop)e.effectLoop.get()).advance(prevNanos, currNanos);
         });
         this.nanosElapsed = currNanos;
      }

      public void resetToBeginning() {
         this.nanosElapsed = 0L;
      }
   }

   private static class ConfigLoadedValue<T> {
      private final T defaultValue;
      private ConfigurationNode previousConfig = null;
      private T value;

      public ConfigLoadedValue(T defaultValue) {
         this.defaultValue = defaultValue;
         this.value = defaultValue;
      }

      public T get() {
         return this.value;
      }

      public void reset() {
         this.previousConfig = null;
         this.value = this.defaultValue;
      }

      public void forceLoad(ConfigurationNode config, Function<ConfigurationNode, T> loader) {
         if (config != null) {
            this.previousConfig = config.clone();
            this.value = loader.apply(config);
         } else {
            this.reset();
         }

      }

      public void load(ConfigurationNode config, Function<ConfigurationNode, T> loader) {
         if (this.previousConfig == null) {
            if (config != null) {
               this.forceLoad(config, loader);
            }
         } else if (config == null || !this.previousConfig.equals(config)) {
            this.forceLoad(config, loader);
         }

      }
   }

   private class ActiveEffectLoop implements EffectLoop {
      private final EffectLoop.Player player;
      private final EffectLoop.RunMode runMode;
      private boolean stopped;

      public ActiveEffectLoop(EffectLoop.Player player, EffectLoop.RunMode runMode) {
         this.player = player;
         this.runMode = runMode;
         this.stopped = false;
      }

      public void play() {
         this.player.play(this, this.runMode);
      }

      public boolean advance(EffectLoop.Time dt, EffectLoop.Time duration, boolean loop) {
         if (this.stopped) {
            return false;
         } else if (!this.advanceGroups(dt)) {
            this.stopped = true;
            return false;
         } else {
            EffectLoop.RunMode currentMode = CartAttachmentSequencer.this.runMode;
            if (currentMode != this.runMode) {
               this.stopped = true;
               (CartAttachmentSequencer.this.new ActiveEffectLoop(this.player, currentMode)).play();
               return false;
            } else {
               return true;
            }
         }
      }

      public boolean advanceGroups(EffectLoop.Time dt) {
         int currState = CartAttachmentSequencer.this.playState.get();
         if (currState == 0) {
            return false;
         } else if (currState == 3) {
            CartAttachmentSequencer.this.currentGroup = (CartAttachmentSequencer.SequencerGroup)CartAttachmentSequencer.this.sequencerGroups.get(SequencerMode.START);
            CartAttachmentSequencer.this.currentGroup.resetToBeginning();
            return !CartAttachmentSequencer.this.playState.compareAndSet(3, 0) && !CartAttachmentSequencer.this.playState.compareAndSet(2, 0);
         } else if (currState != 2) {
            while(true) {
               dt = CartAttachmentSequencer.this.currentGroup.advance(dt, CartAttachmentSequencer.this.currentGroup.mode() == SequencerMode.STOP);
               if (dt.isZero()) {
                  return true;
               }

               if (CartAttachmentSequencer.this.currentGroup.mode() == SequencerMode.STOP) {
                  CartAttachmentSequencer.this.currentGroup = (CartAttachmentSequencer.SequencerGroup)CartAttachmentSequencer.this.sequencerGroups.get(SequencerMode.START);
               } else {
                  CartAttachmentSequencer.this.currentGroup = (CartAttachmentSequencer.SequencerGroup)CartAttachmentSequencer.this.sequencerGroups.get(SequencerMode.LOOP);
               }

               CartAttachmentSequencer.this.currentGroup.resetToBeginning();
            }
         } else {
            while(true) {
               dt = CartAttachmentSequencer.this.currentGroup.advance(dt, CartAttachmentSequencer.this.currentGroup.mode() != SequencerMode.STOP);
               if (dt.isZero()) {
                  return true;
               }

               if (CartAttachmentSequencer.this.currentGroup.mode() == SequencerMode.STOP) {
                  return !CartAttachmentSequencer.this.playState.compareAndSet(2, 0) && !CartAttachmentSequencer.this.playState.compareAndSet(3, 0);
               }

               CartAttachmentSequencer.this.currentGroup = (CartAttachmentSequencer.SequencerGroup)CartAttachmentSequencer.this.sequencerGroups.get(SequencerMode.STOP);
               CartAttachmentSequencer.this.currentGroup.resetToBeginning();
            }
         }
      }
   }

   public static class SequencerEffect implements Attachment.EffectSink, Tickable {
      private final CartAttachmentSequencer.SequencerGroup group;
      private AttachmentSelection<Attachment.EffectAttachment> effectAttachments = AttachmentSelection.none(Attachment.EffectAttachment.class);
      private final CartAttachmentSequencer.ConfigLoadedValue<TransferFunction> activeFunction;
      private final CartAttachmentSequencer.ConfigLoadedValue<TransferFunction> volumeFunction;
      private final CartAttachmentSequencer.ConfigLoadedValue<TransferFunction> pitchFunction;
      private final CartAttachmentSequencer.ConfigLoadedValue<ScheduledEffectLoop> effectLoop;
      private SequencerType sequencerType;
      private boolean active;
      private double volume;
      private double pitch;
      private EffectLoop.Time stopAfterTime;
      private final AtomicReference<DelayedEffectTask> pendingStop;

      public SequencerEffect(CartAttachmentSequencer.SequencerGroup group) {
         this.activeFunction = new CartAttachmentSequencer.ConfigLoadedValue(TransferFunctionBoolean.TRUE);
         this.volumeFunction = new CartAttachmentSequencer.ConfigLoadedValue(TransferFunctionConstant.of(1.0D));
         this.pitchFunction = new CartAttachmentSequencer.ConfigLoadedValue(TransferFunctionConstant.of(1.0D));
         this.effectLoop = new CartAttachmentSequencer.ConfigLoadedValue(ScheduledEffectLoop.NONE);
         this.sequencerType = null;
         this.stopAfterTime = EffectLoop.Time.NEVER;
         this.pendingStop = new AtomicReference((Object)null);
         this.group = group;
      }

      public void onRemoved() {
         DelayedEffectTask task = (DelayedEffectTask)this.pendingStop.getAndSet((Object)null);
         if (task != null) {
            task.runNow();
         }

      }

      public void onTick() {
         this.effectAttachments.sync();
      }

      public void updateEffectLoop() {
         this.active = ((TransferFunction)this.activeFunction.get()).map(0.0D) != 0.0D;
         this.volume = ((TransferFunction)this.volumeFunction.get()).map(0.0D);
         this.pitch = ((TransferFunction)this.pitchFunction.get()).map(0.0D);
      }

      public void playEffect(Attachment.EffectAttachment.EffectOptions options) {
         if (this.active) {
            EffectLoop.Time stopAfterTime = this.stopAfterTime;
            if (stopAfterTime.isZero()) {
               this.effectAttachments.forEach(Attachment.EffectAttachment::stopEffect);
            } else {
               Attachment.EffectAttachment.EffectOptions adjusted = this.volume == 1.0D && this.pitch == 1.0D ? options : options.multiply(this.volume, this.pitch);
               this.effectAttachments.forEach((e) -> {
                  e.playEffect(adjusted);
               });
               if (!stopAfterTime.isNever()) {
                  DelayedEffectTask newTask = this.group.sequencer.player.scheduleTask(stopAfterTime, () -> {
                     this.effectAttachments.forEach(Attachment.EffectAttachment::stopEffect);
                  }, this.group.sequencer.runMode);
                  DelayedEffectTask prevTask = (DelayedEffectTask)this.pendingStop.getAndSet(newTask);
                  if (prevTask != null) {
                     prevTask.cancel();
                  }
               }
            }
         }

      }

      public void stopEffect() {
         if (this.active) {
            this.effectAttachments.forEach(Attachment.EffectAttachment::stopEffect);
         }

      }

      public void load(CartAttachmentSequencer sequencer, ConfigurationNode config) {
         this.effectAttachments = sequencer.getSelection(AttachmentSelector.readFromConfig(config, "effect").withType(Attachment.EffectAttachment.class).excludingSelf());
         CartAttachmentSequencer.ConfigLoadedValue var10000 = this.activeFunction;
         ConfigurationNode var10001 = config.getNodeIfExists("active");
         CartAttachmentSequencer.SequencerTransferFunctionHost var10002 = sequencer.functionHost;
         Objects.requireNonNull(var10002);
         var10000.load(var10001, var10002::loadFunction);
         var10000 = this.volumeFunction;
         var10001 = config.getNodeIfExists("volume");
         var10002 = sequencer.functionHost;
         Objects.requireNonNull(var10002);
         var10000.load(var10001, var10002::loadFunction);
         var10000 = this.pitchFunction;
         var10001 = config.getNodeIfExists("pitch");
         var10002 = sequencer.functionHost;
         Objects.requireNonNull(var10002);
         var10000.load(var10001, var10002::loadFunction);
         Double stopAfterTimeSeconds = (Double)config.getOrDefault("stopAfter", Double.class, (Object)null);
         this.stopAfterTime = stopAfterTimeSeconds != null ? EffectLoop.Time.seconds(stopAfterTimeSeconds) : EffectLoop.Time.NEVER;
         SequencerType newType = SequencerType.byName((String)config.getOrDefault("type", ""));
         if (this.sequencerType != newType) {
            this.sequencerType = newType;
            this.effectLoop.forceLoad(config.getNodeIfExists("config"), (c) -> {
               return this.sequencerType.createEffectLoop(c, this);
            });
         } else {
            this.effectLoop.load(config.getNodeIfExists("config"), (c) -> {
               return this.sequencerType.createEffectLoop(c, this);
            });
         }

      }
   }
}
