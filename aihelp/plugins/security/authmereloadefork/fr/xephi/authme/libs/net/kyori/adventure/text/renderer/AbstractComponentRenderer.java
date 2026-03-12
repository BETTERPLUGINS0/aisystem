package fr.xephi.authme.libs.net.kyori.adventure.text.renderer;

import fr.xephi.authme.libs.net.kyori.adventure.text.BlockNBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.EntityNBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.KeybindComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.NBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.ScoreComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.SelectorComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.StorageNBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractComponentRenderer<C> implements ComponentRenderer<C> {
   @NotNull
   public Component render(@NotNull final Component component, @NotNull final C context) {
      if (component instanceof TextComponent) {
         return this.renderText((TextComponent)component, context);
      } else if (component instanceof TranslatableComponent) {
         return this.renderTranslatable((TranslatableComponent)component, context);
      } else if (component instanceof KeybindComponent) {
         return this.renderKeybind((KeybindComponent)component, context);
      } else if (component instanceof ScoreComponent) {
         return this.renderScore((ScoreComponent)component, context);
      } else if (component instanceof SelectorComponent) {
         return this.renderSelector((SelectorComponent)component, context);
      } else {
         if (component instanceof NBTComponent) {
            if (component instanceof BlockNBTComponent) {
               return this.renderBlockNbt((BlockNBTComponent)component, context);
            }

            if (component instanceof EntityNBTComponent) {
               return this.renderEntityNbt((EntityNBTComponent)component, context);
            }

            if (component instanceof StorageNBTComponent) {
               return this.renderStorageNbt((StorageNBTComponent)component, context);
            }
         }

         return component;
      }
   }

   @NotNull
   protected abstract Component renderBlockNbt(@NotNull final BlockNBTComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderEntityNbt(@NotNull final EntityNBTComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderStorageNbt(@NotNull final StorageNBTComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderKeybind(@NotNull final KeybindComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderScore(@NotNull final ScoreComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderSelector(@NotNull final SelectorComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderText(@NotNull final TextComponent component, @NotNull final C context);

   @NotNull
   protected abstract Component renderTranslatable(@NotNull final TranslatableComponent component, @NotNull final C context);
}
