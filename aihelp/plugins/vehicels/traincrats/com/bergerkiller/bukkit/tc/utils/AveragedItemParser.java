package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.inventory.ItemParser;

public class AveragedItemParser extends ItemParser {
   public AveragedItemParser(ItemParser itemParser, int multiplier) {
      super(itemParser.getType(), itemParser.hasAmount() ? itemParser.getAmount() * multiplier : multiplier, itemParser.getData());
   }
}
