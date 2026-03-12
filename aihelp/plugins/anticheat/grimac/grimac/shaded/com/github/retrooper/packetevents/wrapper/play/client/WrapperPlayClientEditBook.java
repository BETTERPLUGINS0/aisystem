package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WrapperPlayClientEditBook extends PacketWrapper<WrapperPlayClientEditBook> {
   /** @deprecated */
   @Deprecated
   public static final int MAX_BYTES_PER_CHAR = 4;
   private static final int TITLE_MAX_CHARS_LEGACY = 128;
   private static final int TITLE_MAX_CHARS = 32;
   private static final int PAGE_MAX_CHARS_LEGACY = 8192;
   private static final int PAGE_MAX_CHARS = 1024;
   private static final int MAX_PAGES_LEGACY = 200;
   private static final int MAX_PAGES = 100;
   private int slot;
   @Nullable
   private List<String> pages;
   @Nullable
   private String title;
   @Nullable
   private ItemStack itemStack;
   @Nullable
   private Boolean signing;

   public WrapperPlayClientEditBook(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientEditBook(int slot, List<String> pages, @Nullable String title) {
      super((PacketTypeCommon)PacketType.Play.Client.EDIT_BOOK);
      this.slot = slot;
      this.pages = pages;
      this.title = title;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
         boolean modernLimits = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2);
         int pageLimit = modernLimits ? 100 : 200;
         int pageCharLimit = modernLimits ? 1024 : 8192;
         this.slot = this.readVarInt();
         int pageCount = this.readVarInt();
         if (pageCount > pageLimit) {
            throw new IllegalStateException("Page count " + pageCount + " is larger than limit of " + pageLimit);
         }

         this.pages = new ArrayList(pageCount);

         for(int i = 0; i < pageCount; ++i) {
            this.pages.add(this.readString(pageCharLimit));
         }

         this.title = (String)this.readOptional((reader) -> {
            int titleLimit = modernLimits ? 32 : 128;
            return reader.readString(titleLimit);
         });
      } else {
         this.itemStack = this.readItemStack();
         this.signing = this.readBoolean();
         this.slot = this.readVarInt();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17_1)) {
         boolean modernLimits = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2);
         int pageCharLimit = modernLimits ? 1024 : 8192;
         this.writeVarInt(this.slot);
         this.writeVarInt(this.pages.size());
         Iterator var3 = this.pages.iterator();

         while(var3.hasNext()) {
            String page = (String)var3.next();
            this.writeString(page, pageCharLimit);
         }

         this.writeOptional(this.title, (writer, innerTitle) -> {
            int titleLimit = modernLimits ? 32 : 128;
            writer.writeString(innerTitle, titleLimit);
         });
      } else {
         this.writeItemStack(this.itemStack);
         this.writeBoolean(this.signing);
         this.writeVarInt(this.slot);
      }

   }

   public void copy(WrapperPlayClientEditBook wrapper) {
      this.slot = wrapper.slot;
      this.pages = wrapper.pages;
      this.title = wrapper.title;
      this.itemStack = wrapper.itemStack;
      this.signing = wrapper.signing;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   @Nullable
   public List<String> getPages() {
      return this.pages;
   }

   public void setPages(List<String> pages) {
      this.pages = pages;
   }

   @Nullable
   public String getTitle() {
      return this.title;
   }

   public void setTitle(@Nullable String title) {
      this.title = title;
   }

   @Nullable
   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public void setItemStack(@Nullable ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   @Nullable
   public Boolean getSigning() {
      return this.signing;
   }

   public void setSigning(@Nullable Boolean signing) {
      this.signing = signing;
   }
}
