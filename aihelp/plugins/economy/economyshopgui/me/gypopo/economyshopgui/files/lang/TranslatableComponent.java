package me.gypopo.economyshopgui.files.lang;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.util.AdventureUtil;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.meta.PaperMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class TranslatableComponent implements Translatable {
   private final String preTranslated;

   public TranslatableComponent(String s) {
      this.preTranslated = ChatUtil.getAdventureUtils().formatLegacyToMini(s);
   }

   public TranslatableComponent(String preTranslated, boolean original) {
      this.preTranslated = preTranslated;
   }

   public TranslatableComponent(Component component) {
      this.preTranslated = AdventureUtil.serialize(component);
   }

   public Translatable replace(String ph, String value) {
      return this.preTranslated.indexOf(ph) == -1 ? this : new TranslatableComponent(this.preTranslated.replace(ph, ChatUtil.getAdventureUtils().formatLegacyToMini(value)), false);
   }

   public Translatable replace(String ph, Translatable value) {
      return this.preTranslated.indexOf(ph) == -1 ? this : new TranslatableComponent(this.preTranslated.replace(ph, ChatUtil.getAdventureUtils().formatLegacyToMini(value.toString())), false);
   }

   public String getLegacy() {
      return AdventureUtil.toLegacy((Component)this.get());
   }

   public String toString() {
      return this.preTranslated;
   }

   public Object get() {
      return ChatUtil.getAdventureUtils().formatMini(this.preTranslated);
   }

   public List<Translatable> getLines() {
      return (List)Arrays.asList(this.toString().split("\n")).stream().map(TranslatableComponent::new).collect(Collectors.toList());
   }

   public Translatable.Builder builder() {
      return new TranslatableComponent.ComponentBuilder(this);
   }

   public static class ComponentBuilder implements Translatable.Builder {
      private Component component;

      public ComponentBuilder(TranslatableComponent base) {
         this.component = (Component)base.get();
      }

      public Translatable.Builder append(Translatable s) {
         this.component = this.component.append((Component)s.get());
         return this;
      }

      public Translatable.Builder append(String s) {
         this.component = this.component.append(Component.text(s));
         return this;
      }

      public Translatable.Builder color(char c) {
         this.component = this.component.append(Component.text("", (TextColor)PaperMeta.COLOR_MAP.get(c)));
         return this;
      }

      public Translatable build() {
         return new TranslatableComponent(this.component);
      }
   }
}
