package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.List;
import java.util.Objects;

public abstract class WrapperCommonServerServerLinks<T extends WrapperCommonServerServerLinks<T>> extends PacketWrapper<T> {
   private List<WrapperCommonServerServerLinks.ServerLink> links;

   public WrapperCommonServerServerLinks(PacketSendEvent event) {
      super(event);
   }

   public WrapperCommonServerServerLinks(PacketTypeCommon packetType, List<WrapperCommonServerServerLinks.ServerLink> links) {
      super(packetType);
      this.links = links;
   }

   public void read() {
      this.links = this.readList(WrapperCommonServerServerLinks.ServerLink::read);
   }

   public void write() {
      this.writeList(this.links, WrapperCommonServerServerLinks.ServerLink::write);
   }

   public void copy(T wrapper) {
      this.links = wrapper.getLinks();
   }

   public List<WrapperCommonServerServerLinks.ServerLink> getLinks() {
      return this.links;
   }

   public void setLinks(List<WrapperCommonServerServerLinks.ServerLink> links) {
      this.links = links;
   }

   public static final class ServerLink {
      @Nullable
      private final WrapperCommonServerServerLinks.KnownType knownType;
      @Nullable
      private final Component customType;
      private final String url;

      public ServerLink(@Nullable WrapperCommonServerServerLinks.KnownType knownType, @Nullable Component customType, String url) {
         if (knownType == null == (customType == null)) {
            throw new IllegalStateException("Illegal state of both known type and custom type combined: " + knownType + " / " + customType);
         } else {
            this.knownType = knownType;
            this.customType = customType;
            this.url = url;
         }
      }

      public static WrapperCommonServerServerLinks.ServerLink read(PacketWrapper<?> wrapper) {
         WrapperCommonServerServerLinks.KnownType knownType;
         Component customType;
         if (wrapper.readBoolean()) {
            knownType = (WrapperCommonServerServerLinks.KnownType)wrapper.readEnum((Enum[])WrapperCommonServerServerLinks.KnownType.values());
            customType = null;
         } else {
            knownType = null;
            customType = wrapper.readComponent();
         }

         String url = wrapper.readString();
         return new WrapperCommonServerServerLinks.ServerLink(knownType, customType, url);
      }

      public static void write(PacketWrapper<?> wrapper, WrapperCommonServerServerLinks.ServerLink link) {
         if (link.getKnownType() != null) {
            wrapper.writeBoolean(true);
            wrapper.writeEnum(link.getKnownType());
         } else {
            assert link.getCustomType() != null;

            wrapper.writeBoolean(false);
            wrapper.writeComponent(link.getCustomType());
         }

         wrapper.writeString(link.getUrl());
      }

      public ServerLink(WrapperCommonServerServerLinks.KnownType knownType, String url) {
         this(knownType, (Component)null, url);
      }

      public ServerLink(Component customType, String url) {
         this((WrapperCommonServerServerLinks.KnownType)null, customType, url);
      }

      @Nullable
      public WrapperCommonServerServerLinks.KnownType getKnownType() {
         return this.knownType;
      }

      @Nullable
      public Component getCustomType() {
         return this.customType;
      }

      public String getUrl() {
         return this.url;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof WrapperCommonServerServerLinks.ServerLink)) {
            return false;
         } else {
            WrapperCommonServerServerLinks.ServerLink that = (WrapperCommonServerServerLinks.ServerLink)obj;
            if (this.knownType != that.knownType) {
               return false;
            } else {
               return !Objects.equals(this.customType, that.customType) ? false : this.url.equals(that.url);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.knownType, this.customType, this.url});
      }

      public String toString() {
         return "ServerLink{knownType=" + this.knownType + ", customType=" + this.customType + ", url='" + this.url + '\'' + '}';
      }
   }

   public static enum KnownType {
      BUG_REPORT,
      COMMUNITY_GUIDELINES,
      SUPPORT,
      STATUS,
      FEEDBACK,
      COMMUNITY,
      WEBSITE,
      FORUMS,
      NEWS,
      ANNOUNCEMENTS;

      // $FF: synthetic method
      private static WrapperCommonServerServerLinks.KnownType[] $values() {
         return new WrapperCommonServerServerLinks.KnownType[]{BUG_REPORT, COMMUNITY_GUIDELINES, SUPPORT, STATUS, FEEDBACK, COMMUNITY, WEBSITE, FORUMS, NEWS, ANNOUNCEMENTS};
      }
   }
}
