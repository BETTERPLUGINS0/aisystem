package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.Node;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class WrapperPlayServerDeclareCommands extends PacketWrapper<WrapperPlayServerDeclareCommands> {
   private List<Node> nodes;
   private int rootIndex;

   public WrapperPlayServerDeclareCommands(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDeclareCommands(List<Node> nodes, int rootIndex) {
      super((PacketTypeCommon)PacketType.Play.Server.DECLARE_COMMANDS);
      this.nodes = nodes;
      this.rootIndex = rootIndex;
   }

   public void read() {
      this.nodes = this.readList(PacketWrapper::readNode);
      this.rootIndex = this.readVarInt();
   }

   public void write() {
      this.writeList(this.nodes, PacketWrapper::writeNode);
      this.writeVarInt(this.rootIndex);
   }

   public List<Node> getNodes() {
      return this.nodes;
   }

   public void setNodes(List<Node> nodes) {
      this.nodes = nodes;
   }

   public int getRootIndex() {
      return this.rootIndex;
   }

   public void setRootIndex(int rootIndex) {
      this.rootIndex = rootIndex;
   }
}
