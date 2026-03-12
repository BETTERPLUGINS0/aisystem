package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.google.gson.JsonObject;

public class WrapperStatusServerResponse extends PacketWrapper<WrapperStatusServerResponse> {
   private String componentJson;

   public WrapperStatusServerResponse(PacketSendEvent event) {
      super(event);
   }

   public WrapperStatusServerResponse(JsonObject component) {
      this(component.toString());
   }

   public WrapperStatusServerResponse(String componentJson) {
      super((PacketTypeCommon)PacketType.Status.Server.RESPONSE);
      this.componentJson = componentJson;
   }

   public void read() {
      this.componentJson = this.readString();
   }

   public void write() {
      this.writeString(this.componentJson);
   }

   public void copy(WrapperStatusServerResponse wrapper) {
      this.componentJson = wrapper.componentJson;
   }

   public JsonObject getComponent() {
      return (JsonObject)this.getSerializers().gson().serializer().fromJson(this.componentJson, JsonObject.class);
   }

   public void setComponent(JsonObject component) {
      this.componentJson = this.getSerializers().gson().serializer().toJson(component);
   }

   public String getComponentJson() {
      return this.componentJson;
   }

   public void setComponentJson(String componentJson) {
      this.componentJson = componentJson;
   }
}
