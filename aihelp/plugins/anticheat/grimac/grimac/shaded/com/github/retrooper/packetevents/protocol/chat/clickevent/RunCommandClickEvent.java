package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RunCommandClickEvent implements ClickEvent {
   private final String command;

   public RunCommandClickEvent(String command) {
      this.command = command;
   }

   public static RunCommandClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      boolean v1215 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5);
      String command = compound.getStringTagValueOrThrow(v1215 ? "command" : "value");
      return new RunCommandClickEvent(command);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, RunCommandClickEvent clickEvent) {
      boolean v1215 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5);
      compound.setTag(v1215 ? "command" : "value", new NBTString(clickEvent.command));
   }

   public ClickEventAction<?> getAction() {
      return ClickEventActions.RUN_COMMAND;
   }

   public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
      return ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.runCommand(this.command);
   }

   public String getCommand() {
      return this.command;
   }
}
