package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.ui.lib.UIState;

public class EditorUIState implements UIState {
   private final int currentPage;

   public EditorUIState(int currentPage) {
      this.currentPage = currentPage;
   }

   public final int getCurrentPage() {
      return this.currentPage;
   }
}
