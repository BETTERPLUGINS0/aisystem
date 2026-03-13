package me.casperge.realisticseasons.seasonevent;

import java.util.List;

public interface SeasonCustomEvent {
   List<String> getCommands(boolean var1);

   boolean doDisplay();

   String getName();
}
