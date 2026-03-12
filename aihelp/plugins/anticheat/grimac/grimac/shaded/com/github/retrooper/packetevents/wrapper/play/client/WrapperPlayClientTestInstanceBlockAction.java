package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.TestInstanceData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientTestInstanceBlockAction extends PacketWrapper<WrapperPlayClientTestInstanceBlockAction> {
   private Vector3i position;
   private WrapperPlayClientTestInstanceBlockAction.Action action;
   private TestInstanceData data;

   public WrapperPlayClientTestInstanceBlockAction(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientTestInstanceBlockAction(Vector3i position, WrapperPlayClientTestInstanceBlockAction.Action action, TestInstanceData data) {
      super((PacketTypeCommon)PacketType.Play.Client.TEST_INSTANCE_BLOCK_ACTION);
      this.position = position;
      this.action = action;
      this.data = data;
   }

   public void read() {
      this.position = this.readBlockPosition();
      this.action = (WrapperPlayClientTestInstanceBlockAction.Action)this.readEnum(WrapperPlayClientTestInstanceBlockAction.Action.class);
      this.data = TestInstanceData.read(this);
   }

   public void write() {
      this.writeBlockPosition(this.position);
      this.writeEnum(this.action);
      TestInstanceData.write(this, this.data);
   }

   public void copy(WrapperPlayClientTestInstanceBlockAction wrapper) {
      this.position = wrapper.position;
      this.action = wrapper.action;
      this.data = wrapper.data;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public WrapperPlayClientTestInstanceBlockAction.Action getAction() {
      return this.action;
   }

   public void setAction(WrapperPlayClientTestInstanceBlockAction.Action action) {
      this.action = action;
   }

   public TestInstanceData getData() {
      return this.data;
   }

   public void setData(TestInstanceData data) {
      this.data = data;
   }

   public static enum Action {
      INIT,
      QUERY,
      SET,
      RESET,
      SAVE,
      EXPORT,
      RUN;

      // $FF: synthetic method
      private static WrapperPlayClientTestInstanceBlockAction.Action[] $values() {
         return new WrapperPlayClientTestInstanceBlockAction.Action[]{INIT, QUERY, SET, RESET, SAVE, EXPORT, RUN};
      }
   }
}
