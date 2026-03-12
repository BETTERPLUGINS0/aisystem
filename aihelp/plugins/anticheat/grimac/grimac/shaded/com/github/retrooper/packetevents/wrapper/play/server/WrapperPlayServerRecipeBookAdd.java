package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.RecipeDisplayEntry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class WrapperPlayServerRecipeBookAdd extends PacketWrapper<WrapperPlayServerRecipeBookAdd> {
   private List<WrapperPlayServerRecipeBookAdd.AddEntry> entries;
   private boolean replace;

   public WrapperPlayServerRecipeBookAdd(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerRecipeBookAdd(List<WrapperPlayServerRecipeBookAdd.AddEntry> entries, boolean replace) {
      super((PacketTypeCommon)PacketType.Play.Server.RECIPE_BOOK_ADD);
      this.entries = entries;
      this.replace = replace;
   }

   public void read() {
      this.entries = this.readList(WrapperPlayServerRecipeBookAdd.AddEntry::read);
      this.replace = this.readBoolean();
   }

   public void write() {
      this.writeList(this.entries, WrapperPlayServerRecipeBookAdd.AddEntry::write);
      this.writeBoolean(this.replace);
   }

   public void copy(WrapperPlayServerRecipeBookAdd wrapper) {
      this.entries = wrapper.entries;
      this.replace = wrapper.replace;
   }

   public List<WrapperPlayServerRecipeBookAdd.AddEntry> getEntries() {
      return this.entries;
   }

   public void setEntries(List<WrapperPlayServerRecipeBookAdd.AddEntry> entries) {
      this.entries = entries;
   }

   public boolean isReplace() {
      return this.replace;
   }

   public void setReplace(boolean replace) {
      this.replace = replace;
   }

   public static final class AddEntry {
      private static final byte FLAG_NOTIFICATION = 1;
      private static final byte FLAG_HIGHLIGHT = 2;
      private RecipeDisplayEntry contents;
      private boolean notification;
      private boolean highlight;

      public AddEntry(RecipeDisplayEntry contents, byte flags) {
         this(contents, (flags & 1) != 0, (flags & 2) != 0);
      }

      public AddEntry(RecipeDisplayEntry contents, boolean notification, boolean highlight) {
         this.contents = contents;
         this.notification = notification;
         this.highlight = highlight;
      }

      public static WrapperPlayServerRecipeBookAdd.AddEntry read(PacketWrapper<?> wrapper) {
         RecipeDisplayEntry contents = RecipeDisplayEntry.read(wrapper);
         byte flags = wrapper.readByte();
         return new WrapperPlayServerRecipeBookAdd.AddEntry(contents, flags);
      }

      public static void write(PacketWrapper<?> wrapper, WrapperPlayServerRecipeBookAdd.AddEntry entry) {
         RecipeDisplayEntry.write(wrapper, entry.contents);
         wrapper.writeByte(entry.packFlags());
      }

      public RecipeDisplayEntry getContents() {
         return this.contents;
      }

      public void setContents(RecipeDisplayEntry contents) {
         this.contents = contents;
      }

      public boolean isNotification() {
         return this.notification;
      }

      public void setNotification(boolean notification) {
         this.notification = notification;
      }

      public boolean isHighlight() {
         return this.highlight;
      }

      public void setHighlight(boolean highlight) {
         this.highlight = highlight;
      }

      public byte packFlags() {
         return (byte)((this.notification ? 1 : 0) | (this.highlight ? 2 : 0));
      }
   }
}
