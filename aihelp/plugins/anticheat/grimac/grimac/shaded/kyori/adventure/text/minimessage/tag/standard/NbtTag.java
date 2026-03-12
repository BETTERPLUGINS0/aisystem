package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.EntityNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.StorageNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class NbtTag {
   private static final String NBT = "nbt";
   private static final String DATA = "data";
   private static final String BLOCK = "block";
   private static final String ENTITY = "entity";
   private static final String STORAGE = "storage";
   private static final String INTERPRET = "interpret";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("nbt", "data"), NbtTag::resolve, NbtTag::emit);

   private NbtTag() {
   }

   static Tag resolve(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String type = args.popOr("a type of block, entity, or storage is required").lowerValue();
      Object builder;
      String popped;
      if ("block".equals(type)) {
         popped = args.popOr("A position is required").value();

         try {
            builder = Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(popped));
         } catch (IllegalArgumentException var6) {
            throw ctx.newException(var6.getMessage(), args);
         }
      } else if ("entity".equals(type)) {
         builder = Component.entityNBT().selector(args.popOr("A selector is required").value());
      } else {
         if (!"storage".equals(type)) {
            throw ctx.newException("Unknown nbt tag type '" + type + "'", args);
         }

         builder = Component.storageNBT().storage(Key.key(args.popOr("A storage key is required").value()));
      }

      ((NBTComponentBuilder)builder).nbtPath(args.popOr("An NBT path is required").value());
      if (args.hasNext()) {
         popped = args.pop().value();
         if ("interpret".equalsIgnoreCase(popped)) {
            ((NBTComponentBuilder)builder).interpret(true);
         } else {
            ((NBTComponentBuilder)builder).separator(ctx.deserialize(popped));
            if (args.hasNext() && args.pop().value().equalsIgnoreCase("interpret")) {
               ((NBTComponentBuilder)builder).interpret(true);
            }
         }
      }

      return Tag.inserting((Component)((NBTComponentBuilder)builder).build());
   }

   @Nullable
   static Emitable emit(final Component comp) {
      String type;
      String id;
      if (comp instanceof BlockNBTComponent) {
         type = "block";
         id = ((BlockNBTComponent)comp).pos().asString();
      } else if (comp instanceof EntityNBTComponent) {
         type = "entity";
         id = ((EntityNBTComponent)comp).selector();
      } else {
         if (!(comp instanceof StorageNBTComponent)) {
            return null;
         }

         type = "storage";
         id = ((StorageNBTComponent)comp).storage().asString();
      }

      return (out) -> {
         NBTComponent<?, ?> nbt = (NBTComponent)comp;
         out.tag("nbt").argument(type).argument(id).argument(nbt.nbtPath());
         if (nbt.separator() != null) {
            out.argument(nbt.separator());
         }

         if (nbt.interpret()) {
            out.argument("interpret");
         }

      };
   }
}
