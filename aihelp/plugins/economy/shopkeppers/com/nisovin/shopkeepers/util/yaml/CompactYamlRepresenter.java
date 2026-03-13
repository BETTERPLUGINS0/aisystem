package com.nisovin.shopkeepers.util.yaml;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class CompactYamlRepresenter extends OldBukkitYamlRepresenter {
   protected Node representScalar(@Nullable Tag tag, @Nullable String value, @Nullable ScalarStyle style) {
      assert value != null;

      ScalarStyle effectiveStyle = style;
      if (style == null) {
         effectiveStyle = (ScalarStyle)Unsafe.assertNonNull(this.getDefaultScalarStyle());
      }

      if (effectiveStyle != ScalarStyle.DOUBLE_QUOTED && StringUtils.containsNewline(value)) {
         effectiveStyle = ScalarStyle.DOUBLE_QUOTED;
      }

      return (Node)Unsafe.assertNonNull(super.representScalar((Tag)Unsafe.nullableAsNonNull(tag), (String)Unsafe.nullableAsNonNull(value), effectiveStyle));
   }
}
