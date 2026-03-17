package com.bergerkiller.bukkit.tc.attachments.ui.animation;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetBlinkyButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.AnimationMenu;
import java.util.function.Consumer;

public class ConfigureAnimationDialog extends MapWidgetMenu {
   private final AnimationMenu menu;

   public ConfigureAnimationDialog(AnimationMenu menu) {
      this.setBackgroundColor((byte)62);
      this.setBounds(14, 18, 88, 80);
      this.menu = menu;
   }

   public void onAttached() {
      super.onAttached();
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            this.updateIcon();
         }

         public void onClick() {
            ConfigureAnimationDialog.this.updateOptions((opt) -> {
               opt.setLooped(!opt.isLooped());
            });
            this.updateIcon();
         }

         private void updateIcon() {
            this.setIcon(ConfigureAnimationDialog.this.getOptions().isLooped() ? "attachments/anim_config_loop_on.png" : "attachments/anim_config_loop_off.png");
            this.setTooltip(ConfigureAnimationDialog.this.getOptions().isLooped() ? "Looped: YES" : "Looped: NO");
         }
      })).setClickSound(SoundEffect.CLICK_WOOD).setPosition(11, 7);
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            this.updateIcon();
         }

         public void onClick() {
            ConfigureAnimationDialog.this.updateOptions((opt) -> {
               opt.setAutoPlay(!opt.isAutoPlay());
            });
            this.updateIcon();
         }

         private void updateIcon() {
            this.setIcon(ConfigureAnimationDialog.this.getOptions().isAutoPlay() ? "attachments/anim_config_autoplay_on.png" : "attachments/anim_config_autoplay_off.png");
            this.setTooltip(ConfigureAnimationDialog.this.getOptions().isAutoPlay() ? "Autoplay: YES" : "Autoplay: NO");
         }
      })).setClickSound(SoundEffect.CLICK_WOOD).setPosition(36, 7);
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onAttached() {
            super.onAttached();
            this.updateIcon();
         }

         public void onClick() {
            ConfigureAnimationDialog.this.updateOptions((opt) -> {
               opt.setMovementControlled(!opt.isMovementControlled());
            });
            this.updateIcon();
         }

         private void updateIcon() {
            this.setIcon(ConfigureAnimationDialog.this.getOptions().isMovementControlled() ? "attachments/anim_config_movecontrol_on.png" : "attachments/anim_config_movecontrol_off.png");
            this.setTooltip(ConfigureAnimationDialog.this.getOptions().isMovementControlled() ? "Movement-\nControlled: YES" : "Movement-\nControlled: NO");
         }
      })).setClickSound(SoundEffect.CLICK_WOOD).setPosition(61, 7);
      byte lblColor = MapColorPalette.getColor(152, 89, 36);
      ((MapWidgetText)this.addWidget(new MapWidgetText())).setColor(lblColor).setText("Speed").setPosition(13, 29);
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setValue(ConfigureAnimationDialog.this.getOptions().getSpeed());
         }

         public String getAcceptedPropertyName() {
            return "Animation Speed";
         }

         public void onActivate() {
            this.setValue(1.0D);
         }

         public void onValueChanged() {
            if (ConfigureAnimationDialog.this.getOptions().getSpeed() != this.getValue()) {
               ConfigureAnimationDialog.this.updateOptions((opt) -> {
                  opt.setSpeed(this.getValue());
               });
            }

         }
      })).setBounds(4, 38, 80, 11);
      ((MapWidgetText)this.addWidget(new MapWidgetText())).setColor(lblColor).setText("Delay").setPosition(13, 54);
      ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            super.onAttached();
            this.setValue(ConfigureAnimationDialog.this.getOptions().getDelay());
         }

         public String getAcceptedPropertyName() {
            return "Animation Delay";
         }

         public void onValueChanged() {
            if (ConfigureAnimationDialog.this.getOptions().getDelay() != this.getValue()) {
               ConfigureAnimationDialog.this.updateOptions((opt) -> {
                  opt.setDelay(this.getValue());
               });
            }

         }
      })).setBounds(4, 63, 80, 11);
   }

   private AnimationOptions getOptions() {
      return this.menu.getAnimation().getOptions();
   }

   private void updateOptions(Consumer<AnimationOptions> func) {
      Animation anim = this.menu.getAnimation().clone();
      func.accept(anim.getOptions());
      this.menu.setAnimation(anim);
      this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
      this.menu.playAnimation((opt) -> {
         boolean looped = anim.getOptions().isLooped();
         opt.setSpeed(1.0D);
         opt.setLooped(looped);
         opt.setReset(!looped);
         opt.setMovementControlled(anim.getOptions().isMovementControlled());
      });
   }
}
