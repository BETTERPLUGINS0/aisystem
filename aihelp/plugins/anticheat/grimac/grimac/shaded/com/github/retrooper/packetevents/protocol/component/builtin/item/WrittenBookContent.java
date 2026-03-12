package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Filterable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.List;
import java.util.Objects;

public class WrittenBookContent {
   private Filterable<String> title;
   private String author;
   private int generation;
   private List<Filterable<Component>> pages;
   private boolean resolved;

   public WrittenBookContent(Filterable<String> title, String author, int generation, List<Filterable<Component>> pages, boolean resolved) {
      this.title = title;
      this.author = author;
      this.generation = generation;
      this.pages = pages;
      this.resolved = resolved;
   }

   public static WrittenBookContent read(PacketWrapper<?> wrapper) {
      Filterable<String> title = Filterable.read(wrapper, (ew) -> {
         return ew.readString(32);
      });
      String author = wrapper.readString();
      int generation = wrapper.readVarInt();
      List<Filterable<Component>> pages = wrapper.readList((ew) -> {
         return Filterable.read(ew, PacketWrapper::readComponent);
      });
      boolean resolved = wrapper.readBoolean();
      return new WrittenBookContent(title, author, generation, pages, resolved);
   }

   public static void write(PacketWrapper<?> wrapper, WrittenBookContent content) {
      Filterable.write(wrapper, content.title, (ew, text) -> {
         ew.writeString(text, 32);
      });
      wrapper.writeString(content.author);
      wrapper.writeVarInt(content.generation);
      wrapper.writeList(content.pages, (ew, page) -> {
         Filterable.write(ew, page, PacketWrapper::writeComponent);
      });
      wrapper.writeBoolean(content.resolved);
   }

   public Filterable<String> getTitle() {
      return this.title;
   }

   public void setTitle(Filterable<String> title) {
      this.title = title;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public int getGeneration() {
      return this.generation;
   }

   public void setGeneration(int generation) {
      this.generation = generation;
   }

   @Nullable
   public Filterable<Component> getPage(int index) {
      return index >= 0 && index < this.pages.size() ? (Filterable)this.pages.get(index) : null;
   }

   public void addPage(Filterable<Component> page) {
      this.pages.add(page);
   }

   public List<Filterable<Component>> getPages() {
      return this.pages;
   }

   public void setPages(List<Filterable<Component>> pages) {
      this.pages = pages;
   }

   public boolean isResolved() {
      return this.resolved;
   }

   public void setResolved(boolean resolved) {
      this.resolved = resolved;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof WrittenBookContent)) {
         return false;
      } else {
         WrittenBookContent that = (WrittenBookContent)obj;
         if (this.generation != that.generation) {
            return false;
         } else if (this.resolved != that.resolved) {
            return false;
         } else if (!this.title.equals(that.title)) {
            return false;
         } else {
            return !this.author.equals(that.author) ? false : this.pages.equals(that.pages);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.title, this.author, this.generation, this.pages, this.resolved});
   }
}
