package github.nighter.smartspawner.language;

import org.bukkit.configuration.file.YamlConfiguration;

public record LocaleData(YamlConfiguration messages, YamlConfiguration gui, YamlConfiguration formatting, YamlConfiguration items) {
   public LocaleData(YamlConfiguration messages, YamlConfiguration gui, YamlConfiguration formatting, YamlConfiguration items) {
      this.messages = messages;
      this.gui = gui;
      this.formatting = formatting;
      this.items = items;
   }

   public YamlConfiguration messages() {
      return this.messages;
   }

   public YamlConfiguration gui() {
      return this.gui;
   }

   public YamlConfiguration formatting() {
      return this.formatting;
   }

   public YamlConfiguration items() {
      return this.items;
   }
}
