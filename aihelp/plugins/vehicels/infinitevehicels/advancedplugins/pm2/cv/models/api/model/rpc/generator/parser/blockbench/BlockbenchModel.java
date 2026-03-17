package advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench;

import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockbenchModel {
   protected final transient Map<String, BlockbenchModel.Group> flatOutlinerByName = new ConcurrentHashMap();
   protected final transient Map<UUID, BlockbenchModel.Group> flatOutlinerByUUID = new ConcurrentHashMap();
   protected BlockbenchModel.Resolution resolution;
   protected Map<UUID, BlockbenchModel.Element> elements;
   protected Map<UUID, BlockbenchModel.Group> outliner;
   protected Map<Integer, BlockbenchModel.Texture> textures;
   protected Map<String, BlockbenchModel.Animation> animations;
   protected String animationVariablePlaceholders;

   public BlockbenchModel(BlockbenchModel.Resolution var1, Map<UUID, BlockbenchModel.Element> var2, Map<UUID, BlockbenchModel.Group> var3, Map<Integer, BlockbenchModel.Texture> var4, Map<String, BlockbenchModel.Animation> var5, String var6) {
      this.resolution = var1;
      this.elements = var2;
      this.outliner = var3;
      this.textures = var4;
      this.animations = var5;
      this.animationVariablePlaceholders = var6;
   }

   public BlockbenchModel preprocess() {
      LinkedList var1 = new LinkedList(this.outliner.values());

      while(!var1.isEmpty()) {
         BlockbenchModel.Group var2 = (BlockbenchModel.Group)var1.poll();
         this.flatOutlinerByName.put(var2.name, var2);
         this.flatOutlinerByUUID.put(var2.uuid, var2);
         var1.addAll(var2.getChildGroup().values());
      }

      return this;
   }

   public BlockbenchModel.Group getGroup(@NotNull String var1) {
      return (BlockbenchModel.Group)this.flatOutlinerByName.get(var1);
   }

   public BlockbenchModel.Group getGroup(@NotNull UUID var1) {
      return (BlockbenchModel.Group)this.flatOutlinerByUUID.get(var1);
   }

   public BlockbenchModel.Group removeGroup(@NotNull String var1) {
      return this.getGroup(var1);
   }

   public BlockbenchModel.Group removeGroup(@NotNull UUID var1) {
      return this.getGroup(var1);
   }

   public BlockbenchModel.Group removeGroup(@Nullable BlockbenchModel.Group var1) {
      if (var1 == null) {
         return null;
      } else {
         this.flatOutlinerByName.remove(var1.name);
         this.flatOutlinerByUUID.remove(var1.uuid);
         if (var1.parentGroup != null) {
            ((BlockbenchModel.Group)this.flatOutlinerByUUID.get(var1.parentGroup)).childGroup.remove(var1.uuid);
         } else {
            this.outliner.remove(var1.uuid);
         }

         return var1;
      }
   }

   @Generated
   public Map<String, BlockbenchModel.Group> getFlatOutlinerByName() {
      return this.flatOutlinerByName;
   }

   @Generated
   public Map<UUID, BlockbenchModel.Group> getFlatOutlinerByUUID() {
      return this.flatOutlinerByUUID;
   }

   @Generated
   public BlockbenchModel.Resolution getResolution() {
      return this.resolution;
   }

   @Generated
   public Map<UUID, BlockbenchModel.Element> getElements() {
      return this.elements;
   }

   @Generated
   public Map<UUID, BlockbenchModel.Group> getOutliner() {
      return this.outliner;
   }

   @Generated
   public Map<Integer, BlockbenchModel.Texture> getTextures() {
      return this.textures;
   }

   @Generated
   public Map<String, BlockbenchModel.Animation> getAnimations() {
      return this.animations;
   }

   @Generated
   public String getAnimationVariablePlaceholders() {
      return this.animationVariablePlaceholders;
   }

   public static class Resolution {
      protected Integer width;
      protected Integer height;

      @Generated
      public Integer getWidth() {
         return this.width;
      }

      @Generated
      public Integer getHeight() {
         return this.height;
      }
   }

   public static class Group {
      protected final Set<UUID> element = new HashSet();
      protected final Map<UUID, BlockbenchModel.Group> childGroup = new ConcurrentHashMap();
      protected String name;
      protected Float[] origin;
      protected Float[] rotation;
      protected UUID uuid;
      protected UUID parentGroup;
      protected boolean export;

      @Generated
      public Set<UUID> getElement() {
         return this.element;
      }

      @Generated
      public Map<UUID, BlockbenchModel.Group> getChildGroup() {
         return this.childGroup;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public Float[] getOrigin() {
         return this.origin;
      }

      @Generated
      public Float[] getRotation() {
         return this.rotation;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public UUID getParentGroup() {
         return this.parentGroup;
      }

      @Generated
      public boolean isExport() {
         return this.export;
      }
   }

   public static class Element {
      protected String name;
      protected UUID uuid;
      protected String type;

      public Element() {
      }

      public Element(String var1, UUID var2, String var3) {
         this.name = var1;
         this.uuid = var2;
         this.type = var3;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public String getType() {
         return this.type;
      }
   }

   public static class Cube extends BlockbenchModel.Element {
      protected String render_order;
      protected Float[] from;
      protected Float[] to;
      protected Float inflate;
      protected Float[] rotation;
      protected Float[] origin;
      protected Map<String, BlockbenchModel.Face> faces;

      public float width() {
         return Math.abs(this.to[0] - this.from[0]);
      }

      public float height() {
         return Math.abs(this.to[1] - this.from[1]);
      }

      public float depth() {
         return Math.abs(this.to[2] - this.from[2]);
      }

      public boolean isTranslucent() {
         return "in_front".equals(this.render_order);
      }

      @Generated
      public String getRender_order() {
         return this.render_order;
      }

      @Generated
      public Float[] getFrom() {
         return this.from;
      }

      @Generated
      public Float[] getTo() {
         return this.to;
      }

      @Generated
      public Float getInflate() {
         return this.inflate;
      }

      @Generated
      public Float[] getRotation() {
         return this.rotation;
      }

      @Generated
      public Float[] getOrigin() {
         return this.origin;
      }

      @Generated
      public Map<String, BlockbenchModel.Face> getFaces() {
         return this.faces;
      }
   }

   public static class NullObject extends BlockbenchModel.Element implements BlockbenchModel.AnimatableElement {
      protected Float[] position;
      protected String ik_target;
      protected String ik_source;
      protected Boolean lock_ik_target_rotation;

      public Float[] getOrigin() {
         return this.position;
      }

      @Nullable
      public Float[] getRotation() {
         return null;
      }

      @Generated
      public Float[] getPosition() {
         return this.position;
      }

      @Generated
      public String getIk_target() {
         return this.ik_target;
      }

      @Generated
      public String getIk_source() {
         return this.ik_source;
      }

      @Generated
      public Boolean getLock_ik_target_rotation() {
         return this.lock_ik_target_rotation;
      }
   }

   public static class Locator extends BlockbenchModel.Element {
      protected Float[] position;
      protected Float[] rotation;
      protected Boolean ignore_inherited_scale;

      @Generated
      public Float[] getPosition() {
         return this.position;
      }

      @Generated
      public Float[] getRotation() {
         return this.rotation;
      }

      @Generated
      public Boolean getIgnore_inherited_scale() {
         return this.ignore_inherited_scale;
      }
   }

   public static class Camera extends BlockbenchModel.Element implements BlockbenchModel.AnimatableElement {
      protected Float[] position;
      protected Float[] rotation;

      public Float[] getOrigin() {
         return this.position;
      }

      @Generated
      public Float[] getPosition() {
         return this.position;
      }

      @Generated
      public Float[] getRotation() {
         return this.rotation;
      }
   }

   public static class Unknown extends BlockbenchModel.Element {
   }

   public static class Face {
      protected Float[] uv;
      protected Integer rotation;
      protected Integer texture;

      public boolean isEmpty() {
         return this.texture == null || MathUtils.isSimilar(this.uv[0], this.uv[2]) || MathUtils.isSimilar(this.uv[1], this.uv[3]);
      }

      @Generated
      public Float[] getUv() {
         return this.uv;
      }

      @Generated
      public Integer getRotation() {
         return this.rotation;
      }

      @Generated
      public Integer getTexture() {
         return this.texture;
      }
   }

   public static class Texture {
      protected String name;
      protected String folder;
      protected String namespace;
      protected String id;
      protected Integer width;
      protected Integer height;
      protected Integer uv_width;
      protected Integer uv_height;
      protected Integer frame_time;
      protected String frame_order;
      protected Boolean frame_interpolate;
      protected UUID uuid;
      protected String raw_mcmeta;
      protected String source;

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public String getFolder() {
         return this.folder;
      }

      @Generated
      public String getNamespace() {
         return this.namespace;
      }

      @Generated
      public String getId() {
         return this.id;
      }

      @Generated
      public Integer getWidth() {
         return this.width;
      }

      @Generated
      public Integer getHeight() {
         return this.height;
      }

      @Generated
      public Integer getUv_width() {
         return this.uv_width;
      }

      @Generated
      public Integer getUv_height() {
         return this.uv_height;
      }

      @Generated
      public Integer getFrame_time() {
         return this.frame_time;
      }

      @Generated
      public String getFrame_order() {
         return this.frame_order;
      }

      @Generated
      public Boolean getFrame_interpolate() {
         return this.frame_interpolate;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public String getRaw_mcmeta() {
         return this.raw_mcmeta;
      }

      @Generated
      public String getSource() {
         return this.source;
      }
   }

   public static class Animation {
      protected UUID uuid;
      protected String name;
      protected String loop;
      protected Boolean override;
      protected Float length;
      protected BlockbenchModel.Animator effects;
      protected Map<UUID, BlockbenchModel.Animator> animators = new ConcurrentHashMap();
      protected Map<String, BlockbenchModel.Animator> unknownAnimators;

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public String getLoop() {
         return this.loop;
      }

      @Generated
      public Boolean getOverride() {
         return this.override;
      }

      @Generated
      public Float getLength() {
         return this.length;
      }

      @Generated
      public BlockbenchModel.Animator getEffects() {
         return this.effects;
      }

      @Generated
      public Map<UUID, BlockbenchModel.Animator> getAnimators() {
         return this.animators;
      }

      @Generated
      public Map<String, BlockbenchModel.Animator> getUnknownAnimators() {
         return this.unknownAnimators;
      }
   }

   public static class Animator {
      protected String name;
      @Nullable
      protected UUID uuid;
      protected Boolean rotationGlobal;
      protected Map<String, Map<Float, BlockbenchModel.Keyframe>> channels = new ConcurrentHashMap();

      @Nullable
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public Boolean getRotationGlobal() {
         return this.rotationGlobal;
      }

      @Generated
      public Map<String, Map<Float, BlockbenchModel.Keyframe>> getChannels() {
         return this.channels;
      }
   }

   public static class Keyframe {
      protected String channel;
      protected List<Map<String, String>> data_points = new ArrayList();
      protected Float time;
      protected String interpolation;
      protected Float[] bezier_left_time;
      protected Float[] bezier_left_value;
      protected Float[] bezier_right_time;
      protected Float[] bezier_right_value;

      @Generated
      public String getChannel() {
         return this.channel;
      }

      @Generated
      public List<Map<String, String>> getData_points() {
         return this.data_points;
      }

      @Generated
      public Float getTime() {
         return this.time;
      }

      @Generated
      public String getInterpolation() {
         return this.interpolation;
      }

      @Generated
      public Float[] getBezier_left_time() {
         return this.bezier_left_time;
      }

      @Generated
      public Float[] getBezier_left_value() {
         return this.bezier_left_value;
      }

      @Generated
      public Float[] getBezier_right_time() {
         return this.bezier_right_time;
      }

      @Generated
      public Float[] getBezier_right_value() {
         return this.bezier_right_value;
      }
   }

   public interface AnimatableElement {
      String getName();

      UUID getUuid();

      Float[] getOrigin();

      Float[] getPosition();

      @Nullable
      Float[] getRotation();
   }
}
