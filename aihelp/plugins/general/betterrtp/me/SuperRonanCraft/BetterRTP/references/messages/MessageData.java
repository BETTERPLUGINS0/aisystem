package me.SuperRonanCraft.BetterRTP.references.messages;

import me.SuperRonanCraft.BetterRTP.references.file.FileData;

public interface MessageData {
   String section();

   String prefix();

   FileData file();

   default String get() {
      return this.file().getString(this.prefix() + this.section());
   }
}
