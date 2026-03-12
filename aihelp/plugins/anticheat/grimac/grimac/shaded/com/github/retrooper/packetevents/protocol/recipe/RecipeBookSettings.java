package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class RecipeBookSettings {
   private final Map<RecipeBookType, RecipeBookSettings.TypeState> states;

   public RecipeBookSettings(Map<RecipeBookType, RecipeBookSettings.TypeState> states) {
      this.states = states;
   }

   public static RecipeBookSettings read(PacketWrapper<?> wrapper) {
      Map<RecipeBookType, RecipeBookSettings.TypeState> state = new EnumMap(RecipeBookType.class);
      RecipeBookType[] var2 = RecipeBookType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RecipeBookType bookType = var2[var4];
         state.put(bookType, RecipeBookSettings.TypeState.read(wrapper));
      }

      return new RecipeBookSettings(state);
   }

   public static void write(PacketWrapper<?> wrapper, RecipeBookSettings settings) {
      RecipeBookType[] var2 = RecipeBookType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RecipeBookType bookType = var2[var4];
         RecipeBookSettings.TypeState.write(wrapper, settings.getState(bookType));
      }

   }

   public RecipeBookSettings.TypeState getState(RecipeBookType type) {
      return (RecipeBookSettings.TypeState)this.states.computeIfAbsent(type, ($) -> {
         return new RecipeBookSettings.TypeState();
      });
   }

   public void setState(RecipeBookType type, RecipeBookSettings.TypeState state) {
      this.states.put(type, state);
   }

   public Map<RecipeBookType, RecipeBookSettings.TypeState> getStates() {
      return this.states;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof RecipeBookSettings)) {
         return false;
      } else {
         RecipeBookSettings that = (RecipeBookSettings)obj;
         return this.states.equals(that.states);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.states);
   }

   public String toString() {
      return "RecipeBookSettings{states=" + this.states + '}';
   }

   public static final class TypeState {
      private static final boolean DEFAULT_OPEN = false;
      private static final boolean DEFAULT_FILTERING = false;
      private boolean open;
      private boolean filtering;

      public TypeState() {
         this(false, false);
      }

      public TypeState(boolean open, boolean filtering) {
         this.open = open;
         this.filtering = filtering;
      }

      public static RecipeBookSettings.TypeState read(PacketWrapper<?> wrapper) {
         boolean open = wrapper.readBoolean();
         boolean filtering = wrapper.readBoolean();
         return new RecipeBookSettings.TypeState(open, filtering);
      }

      public static void write(PacketWrapper<?> wrapper, RecipeBookSettings.TypeState state) {
         wrapper.writeBoolean(state.open);
         wrapper.writeBoolean(state.filtering);
      }

      public boolean isOpen() {
         return this.open;
      }

      public void setOpen(boolean open) {
         this.open = open;
      }

      public boolean isFiltering() {
         return this.filtering;
      }

      public void setFiltering(boolean filtering) {
         this.filtering = filtering;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof RecipeBookSettings.TypeState)) {
            return false;
         } else {
            RecipeBookSettings.TypeState typeState = (RecipeBookSettings.TypeState)obj;
            if (this.open != typeState.open) {
               return false;
            } else {
               return this.filtering == typeState.filtering;
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.open, this.filtering});
      }

      public String toString() {
         return "TypeState{open=" + this.open + ", filtering=" + this.filtering + '}';
      }
   }
}
