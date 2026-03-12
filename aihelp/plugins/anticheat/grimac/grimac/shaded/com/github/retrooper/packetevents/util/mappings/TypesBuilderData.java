package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.VersionRange;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TypesBuilderData {
   protected final ResourceLocation name;
   protected final int[] ids;
   protected final TypesBuilder typesBuilder;
   protected final VersionRange versions;

   /** @deprecated */
   @Deprecated
   public TypesBuilderData(ResourceLocation name, int[] ids) {
      this(name, ids, new TypesBuilder("", true), VersionRange.ALL_VERSIONS);
   }

   @ApiStatus.Internal
   public TypesBuilderData(ResourceLocation name, int[] ids, TypesBuilder typesBuilder, VersionRange versions) {
      this.name = name;
      this.ids = ids;
      this.typesBuilder = typesBuilder;
      this.versions = versions;
   }

   public int getId(ClientVersion version) {
      return this.ids[this.typesBuilder.getDataIndex(version)];
   }

   public ResourceLocation getName() {
      return this.name;
   }

   /** @deprecated */
   @Deprecated
   public int[] getData() {
      return this.ids;
   }

   public VersionRange getVersions() {
      return this.versions;
   }
}
