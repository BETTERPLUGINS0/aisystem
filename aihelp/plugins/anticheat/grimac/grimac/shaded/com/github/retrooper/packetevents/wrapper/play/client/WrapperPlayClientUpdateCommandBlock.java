package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUpdateCommandBlock extends PacketWrapper<WrapperPlayClientUpdateCommandBlock> {
   private static final int FLAG_TRACK_OUTPUT = 1;
   private static final int FLAG_CONDITIONAL = 2;
   private static final int FLAG_AUTOMATIC = 4;
   private Vector3i position;
   private String command;
   private WrapperPlayClientUpdateCommandBlock.CommandBlockMode mode;
   private boolean doesTrackOutput;
   private boolean conditional;
   private boolean automatic;
   private short flags;

   public WrapperPlayClientUpdateCommandBlock(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientUpdateCommandBlock(Vector3i position, String command, WrapperPlayClientUpdateCommandBlock.CommandBlockMode mode, boolean doesTrackOutput, boolean conditional, boolean automatic) {
      super((PacketTypeCommon)PacketType.Play.Client.UPDATE_COMMAND_BLOCK);
      this.position = position;
      this.command = command;
      this.mode = mode;
      this.doesTrackOutput = doesTrackOutput;
      this.conditional = conditional;
      this.automatic = automatic;
   }

   public void read() {
      this.position = new Vector3i(this.readLong(), this.serverVersion);
      this.command = this.readString();
      this.mode = WrapperPlayClientUpdateCommandBlock.CommandBlockMode.getById(this.readVarInt());
      this.flags = this.readUnsignedByte();
      this.doesTrackOutput = (this.flags & 1) != 0;
      this.conditional = (this.flags & 2) != 0;
      this.automatic = (this.flags & 4) != 0;
   }

   public void write() {
      this.writeLong(this.position.getSerializedPosition(this.serverVersion));
      this.writeString(this.command);
      this.writeVarInt(this.mode.ordinal());
      if (this.doesTrackOutput) {
         this.flags = (short)(this.flags | 1);
      }

      if (this.conditional) {
         this.flags = (short)(this.flags | 2);
      }

      if (this.automatic) {
         this.flags = (short)(this.flags | 4);
      }

      this.writeByte(this.flags);
   }

   public void copy(WrapperPlayClientUpdateCommandBlock wrapper) {
      this.position = wrapper.position;
      this.command = wrapper.command;
      this.mode = wrapper.mode;
      this.doesTrackOutput = wrapper.doesTrackOutput;
      this.conditional = wrapper.conditional;
      this.automatic = wrapper.automatic;
      this.flags = wrapper.flags;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public WrapperPlayClientUpdateCommandBlock.CommandBlockMode getMode() {
      return this.mode;
   }

   public void setMode(WrapperPlayClientUpdateCommandBlock.CommandBlockMode mode) {
      this.mode = mode;
   }

   public boolean isDoesTrackOutput() {
      return this.doesTrackOutput;
   }

   public void setDoesTrackOutput(boolean doesTrackOutput) {
      this.doesTrackOutput = doesTrackOutput;
   }

   public boolean isConditional() {
      return this.conditional;
   }

   public void setConditional(boolean conditional) {
      this.conditional = conditional;
   }

   public boolean isAutomatic() {
      return this.automatic;
   }

   public void setAutomatic(boolean automatic) {
      this.automatic = automatic;
   }

   public short getFlags() {
      return this.flags;
   }

   public void setFlags(short flags) {
      this.flags = flags;
   }

   public static enum CommandBlockMode {
      SEQUENCE,
      AUTO,
      REDSTONE;

      private static final WrapperPlayClientUpdateCommandBlock.CommandBlockMode[] VALUES = values();

      public static WrapperPlayClientUpdateCommandBlock.CommandBlockMode getById(int id) {
         return VALUES[id];
      }

      // $FF: synthetic method
      private static WrapperPlayClientUpdateCommandBlock.CommandBlockMode[] $values() {
         return new WrapperPlayClientUpdateCommandBlock.CommandBlockMode[]{SEQUENCE, AUTO, REDSTONE};
      }
   }
}
