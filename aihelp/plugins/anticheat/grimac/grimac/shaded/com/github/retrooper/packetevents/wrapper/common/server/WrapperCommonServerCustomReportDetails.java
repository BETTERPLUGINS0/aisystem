package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Map;

public abstract class WrapperCommonServerCustomReportDetails<T extends WrapperCommonServerCustomReportDetails<T>> extends PacketWrapper<T> {
   private static final int MAX_KEY_LENGTH = 128;
   private static final int MAX_VALUE_LENGTH = 4096;
   private Map<String, String> details;

   public WrapperCommonServerCustomReportDetails(PacketSendEvent event) {
      super(event);
   }

   public WrapperCommonServerCustomReportDetails(PacketTypeCommon packetType, Map<String, String> details) {
      super(packetType);
      this.details = details;
   }

   public void read() {
      this.details = this.readMap((ew) -> {
         return ew.readString(128);
      }, (ew) -> {
         return ew.readString(4096);
      });
   }

   public void write() {
      this.writeMap(this.details, (ew, key) -> {
         ew.writeString(key, 128);
      }, (ew, val) -> {
         ew.writeString(val, 4096);
      });
   }

   public void copy(T wrapper) {
      this.details = wrapper.getDetails();
   }

   public Map<String, String> getDetails() {
      return this.details;
   }

   public void setDetails(Map<String, String> details) {
      this.details = details;
   }
}
