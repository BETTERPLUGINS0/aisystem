package github.nighter.smartspawner.logging;

public enum SpawnerEventType {
   SPAWNER_PLACE("Spawner placed"),
   SPAWNER_BREAK("Spawner broken"),
   SPAWNER_EXPLODE("Spawner destroyed by explosion"),
   SPAWNER_STACK_HAND("Spawner stacked by hand"),
   SPAWNER_STACK_GUI("Spawner stacked via GUI"),
   SPAWNER_DESTACK_GUI("Spawner destacked via GUI"),
   SPAWNER_GUI_OPEN("Spawner GUI opened"),
   SPAWNER_STORAGE_OPEN("Storage GUI opened"),
   SPAWNER_STACKER_OPEN("Stacker GUI opened"),
   SPAWNER_EXP_CLAIM("Experience claimed"),
   SPAWNER_SELL_ALL("Items sold"),
   SPAWNER_ITEM_TAKE_ALL("All items taken from storage"),
   SPAWNER_ITEM_DROP("Item dropped from storage"),
   SPAWNER_ITEMS_SORT("Items sorted in storage"),
   SPAWNER_ITEM_FILTER("Item filter toggled"),
   SPAWNER_DROP_PAGE_ITEMS("Page items dropped"),
   COMMAND_EXECUTE_PLAYER("Command executed by player"),
   COMMAND_EXECUTE_CONSOLE("Command executed by console"),
   COMMAND_EXECUTE_RCON("Command executed by RCON"),
   SPAWNER_EGG_CHANGE("Spawner entity type changed");

   private final String description;

   private SpawnerEventType(String param3) {
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }

   // $FF: synthetic method
   private static SpawnerEventType[] $values() {
      return new SpawnerEventType[]{SPAWNER_PLACE, SPAWNER_BREAK, SPAWNER_EXPLODE, SPAWNER_STACK_HAND, SPAWNER_STACK_GUI, SPAWNER_DESTACK_GUI, SPAWNER_GUI_OPEN, SPAWNER_STORAGE_OPEN, SPAWNER_STACKER_OPEN, SPAWNER_EXP_CLAIM, SPAWNER_SELL_ALL, SPAWNER_ITEM_TAKE_ALL, SPAWNER_ITEM_DROP, SPAWNER_ITEMS_SORT, SPAWNER_ITEM_FILTER, SPAWNER_DROP_PAGE_ITEMS, COMMAND_EXECUTE_PLAYER, COMMAND_EXECUTE_CONSOLE, COMMAND_EXECUTE_RCON, SPAWNER_EGG_CHANGE};
   }
}
