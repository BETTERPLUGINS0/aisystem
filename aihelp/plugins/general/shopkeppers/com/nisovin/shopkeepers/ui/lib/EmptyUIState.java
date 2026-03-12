package com.nisovin.shopkeepers.ui.lib;

public final class EmptyUIState implements UIState {
   public static final UIState INSTANCE = new EmptyUIState();

   private EmptyUIState() {
   }
}
