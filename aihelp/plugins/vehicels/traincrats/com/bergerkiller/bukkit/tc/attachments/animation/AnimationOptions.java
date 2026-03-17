package com.bergerkiller.bukkit.tc.attachments.animation;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.google.common.base.Objects;
import java.util.Locale;

public class AnimationOptions implements Cloneable {
   private String _name;
   private String _sceneBegin;
   private String _sceneEnd;
   private boolean _hasSceneOption;
   private double _speed;
   private double _delay;
   private boolean _looped;
   private boolean _hasLoopOption;
   private boolean _reset;
   private boolean _queue;
   private boolean _hasMovementControlledOption;
   private boolean _movementControlled;
   private boolean _autoplay;

   protected AnimationOptions(AnimationOptions source) {
      this._name = source._name;
      this._sceneBegin = source._sceneBegin;
      this._sceneEnd = source._sceneEnd;
      this._hasSceneOption = source._hasSceneOption;
      this._speed = source._speed;
      this._delay = source._delay;
      this._looped = source._looped;
      this._hasLoopOption = source._hasLoopOption;
      this._reset = source._reset;
      this._queue = source._queue;
      this._hasMovementControlledOption = source._hasMovementControlledOption;
      this._movementControlled = source._movementControlled;
      this._autoplay = source._autoplay;
   }

   public AnimationOptions() {
      this("");
   }

   public AnimationOptions(String name) {
      this._name = name;
      this._sceneBegin = null;
      this._sceneEnd = null;
      this._hasSceneOption = false;
      this._speed = 1.0D;
      this._delay = 0.0D;
      this._looped = false;
      this._hasLoopOption = false;
      this._reset = false;
      this._queue = false;
      this._hasMovementControlledOption = false;
      this._movementControlled = false;
      this._autoplay = false;
   }

   public void setName(String name) {
      this._name = name;
   }

   public String getName() {
      return this._name;
   }

   public void setScene(String scene) {
      this._sceneBegin = scene;
      this._sceneEnd = scene;
      this._hasSceneOption = true;
   }

   public void setScene(String sceneBegin, String sceneEnd) {
      this._sceneBegin = sceneBegin;
      this._sceneEnd = sceneEnd;
      this._hasSceneOption = true;
   }

   public void resetScene() {
      this._sceneBegin = null;
      this._sceneEnd = null;
      this._hasSceneOption = false;
   }

   public String getSceneBegin() {
      return this._sceneBegin;
   }

   public String getSceneEnd() {
      return this._sceneEnd;
   }

   public boolean isSingleScene() {
      return this._sceneBegin != null && this._sceneBegin.equals(this._sceneEnd);
   }

   public boolean hasSceneOption() {
      return this._hasSceneOption;
   }

   public double getSpeed() {
      return this._speed;
   }

   public void setSpeed(double speed) {
      this._speed = speed;
   }

   public boolean isReversed() {
      return this._speed < 0.0D;
   }

   public double getDelay() {
      return this._delay;
   }

   public void setDelay(double delay) {
      this._delay = delay;
   }

   public void setLooped(boolean looped) {
      this._looped = looped;
      this._hasLoopOption = true;
   }

   public void resetLooped() {
      this._hasLoopOption = false;
   }

   public boolean hasLoopOption() {
      return this._hasLoopOption;
   }

   public boolean isLooped() {
      return this._looped;
   }

   public boolean isAutoPlay() {
      return this._autoplay;
   }

   public void setAutoPlay(boolean autoplay) {
      this._autoplay = autoplay;
   }

   public void setReset(boolean reset) {
      this._reset = reset;
   }

   public boolean getReset() {
      return this._reset;
   }

   public void setQueue(boolean queue) {
      this._queue = queue;
   }

   public boolean getQueue() {
      return this._queue;
   }

   public boolean hasMovementControlledOption() {
      return this._hasMovementControlledOption;
   }

   public boolean isMovementControlled() {
      return this._movementControlled;
   }

   public void setMovementControlled(boolean controlled) {
      this._movementControlled = controlled;
      this._hasMovementControlledOption = true;
   }

   public void clearMovementControlled() {
      this._movementControlled = false;
      this._hasMovementControlledOption = false;
   }

   public void apply(AnimationOptions options) {
      this.setDelay(this.getDelay() + this.getSpeed() * options.getSpeed() * options.getDelay());
      this.setSpeed(this.getSpeed() * options.getSpeed());
      if (options.hasLoopOption()) {
         this.setLooped(options.isLooped());
      }

      if (options.hasMovementControlledOption()) {
         this.setMovementControlled(options.isMovementControlled());
      }

      if (options.isAutoPlay()) {
         this.setAutoPlay(true);
      }

      if (options.hasSceneOption()) {
         this.setScene(options.getSceneBegin(), options.getSceneEnd());
      }

      this.setReset(options.getReset());
      this.setQueue(options.getQueue());
   }

   public void loadFromConfig(ConfigurationNode config) {
      this._speed = config.contains("speed") ? (Double)config.get("speed", 1.0D) : 1.0D;
      this._delay = config.contains("delay") ? (Double)config.get("delay", 0.0D) : 0.0D;
      this._hasLoopOption = config.contains("looped");
      if (this._hasLoopOption) {
         this._looped = (Boolean)config.get("looped", false);
      } else {
         this._looped = false;
      }

      this._hasMovementControlledOption = config.contains("movementControlled");
      if (this._hasMovementControlledOption) {
         this._movementControlled = (Boolean)config.get("movementControlled", false);
      } else {
         this._movementControlled = false;
      }

      this._autoplay = config.contains("autoplay") && (Boolean)config.get("autoplay", false);
   }

   public void saveToConfig(ConfigurationNode config) {
      if (this._speed == 1.0D) {
         config.remove("speed");
      } else {
         config.set("speed", this._speed);
      }

      if (this._delay == 0.0D) {
         config.remove("delay");
      } else {
         config.set("delay", this._delay);
      }

      if (this._hasLoopOption) {
         config.set("looped", this._looped);
      } else {
         config.remove("looped");
      }

      if (this._hasMovementControlledOption) {
         config.set("movementControlled", this._movementControlled);
      } else {
         config.remove("movementControlled");
      }

      if (this._autoplay) {
         config.set("autoplay", true);
      } else {
         config.remove("autoplay");
      }

   }

   public void loadFromSign(SignActionEvent info) {
      String mode_line = info.getLine(1).toLowerCase(Locale.ENGLISH).trim();
      String[] var3 = mode_line.split(" ");
      int sceneStart = var3.length;

      int sceneSplitIdx;
      String part;
      for(sceneSplitIdx = 0; sceneSplitIdx < sceneStart; ++sceneSplitIdx) {
         part = var3[sceneSplitIdx];
         if (LogicUtil.contains(part, new String[]{"noloop", "unlooped", "ul", "nl"})) {
            this.setLooped(false);
         } else if (LogicUtil.contains(part, new String[]{"loop", "looped", "l"})) {
            this.setLooped(true);
         } else if (LogicUtil.contains(part, new String[]{"reset", "rst", "r"})) {
            this.setReset(true);
         } else if (LogicUtil.contains(part, new String[]{"queue", "que", "q"})) {
            this.setQueue(true);
         } else if (LogicUtil.contains(part, new String[]{"move", "mv", "m"})) {
            this.setMovementControlled(true);
         }
      }

      String nameAndScenes = info.getLine(2).trim();
      sceneStart = nameAndScenes.indexOf(91);
      if (sceneStart != -1 && nameAndScenes.endsWith("]")) {
         this.setName(nameAndScenes.substring(0, sceneStart).trim());
         sceneSplitIdx = nameAndScenes.indexOf(58, sceneStart + 1);
         if (sceneSplitIdx == -1) {
            this.setScene(nameAndScenes.substring(sceneStart + 1, nameAndScenes.length() - 1).trim());
         } else {
            part = nameAndScenes.substring(sceneStart + 1, sceneSplitIdx).trim();
            String end = nameAndScenes.substring(sceneSplitIdx + 1, nameAndScenes.length() - 1).trim();
            this.setScene(part, end);
         }
      } else {
         this.setName(nameAndScenes);
      }

      if (!info.getLine(3).isEmpty()) {
         String[] parts = info.getLine(3).split(" ");
         if (parts.length >= 1) {
            this.setSpeed(ParseUtil.parseDouble(parts[0], 1.0D));
         }

         if (parts.length >= 2) {
            this.setDelay(ParseUtil.parseDouble(parts[1], 0.0D));
         }
      }

   }

   public String getCommandSuccessMessage() {
      String name = this.getName();
      if (this.getSceneBegin() != null || this.getSceneEnd() != null) {
         name = name + " [";
         if (Objects.equal(this.getSceneBegin(), this.getSceneEnd())) {
            name = name + this.getSceneBegin();
         } else if (this.getSceneBegin() == null) {
            name = name + ".. > " + this.getSceneEnd();
         } else if (this.getSceneEnd() == null) {
            name = name + this.getSceneBegin() + " > ..";
         } else {
            name = name + this.getSceneBegin() + " > " + this.getSceneEnd();
         }

         name = name + "]";
      }

      if (this.hasLoopOption()) {
         if (this.isLooped()) {
            name = name + " (looped)";
         } else {
            name = name + " (not looped)";
         }
      }

      if (this.hasMovementControlledOption()) {
         if (this.isMovementControlled()) {
            name = name + " (movement controlled)";
         } else {
            name = name + " (not movement controlled)";
         }
      }

      if (this._reset) {
         name = name + " (reset)";
      } else if (this._queue) {
         name = name + " (queue)";
      }

      return Localization.COMMAND_ANIMATE_SUCCESS.get(name, Double.toString(this.getSpeed()), Double.toString(this.getDelay()));
   }

   public String getCommandFailureMessage() {
      return Localization.COMMAND_ANIMATE_FAILURE.get(this.getName());
   }

   public AnimationOptions clone() {
      return new AnimationOptions(this);
   }
}
