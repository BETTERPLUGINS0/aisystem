package com.bergerkiller.bukkit.tc.attachments.control.sequencer;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelector;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.effect.MidiChartDialog;
import com.bergerkiller.bukkit.tc.attachments.control.effect.MidiScheduledEffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.effect.ScheduledEffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.effect.SimpleScheduledEffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.effect.SimpleScheduledEffectLoopDialog;
import com.bergerkiller.bukkit.tc.attachments.control.effect.midi.MidiChart;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SequencerType {
   private static Map<String, SequencerType> types = new HashMap();
   private final String name;
   private final MapTexture iconDefault;
   private final MapTexture iconFocus;
   public static final SequencerType SIMPLE;
   public static final SequencerType MIDI;

   public SequencerType(String name, MapWidgetSequencerEffect.Icon icon) {
      this(name, icon.image(false), icon.image(true));
   }

   public SequencerType(String name, MapTexture iconDefault, MapTexture iconFocus) {
      this.name = name;
      this.iconDefault = iconDefault;
      this.iconFocus = iconFocus;
   }

   public abstract void openConfigurationDialog(SequencerType.OpenDialogArguments var1);

   public abstract ScheduledEffectLoop createEffectLoop(ConfigurationNode var1, Attachment.EffectSink var2);

   public String name() {
      return this.name;
   }

   public MapTexture icon(boolean focused) {
      return focused ? this.iconFocus : this.iconDefault;
   }

   public ConfigurationNode createConfig(AttachmentSelector<Attachment.EffectAttachment> effectSelector) {
      ConfigurationNode config = new ConfigurationNode();
      config.set("type", this.name());
      effectSelector.writeToConfig(config, "effect");
      config.getNode("config");
      return config;
   }

   public static List<SequencerType> all() {
      return (List)types.values().stream().sorted(Comparator.comparing(SequencerType::name)).collect(Collectors.toList());
   }

   public static SequencerType fromConfig(ConfigurationNode config) {
      String typeName = (String)config.getOrDefault("type", String.class, (Object)null);
      if (typeName != null) {
         SequencerType type = byName(typeName);
         if (type != null) {
            return type;
         }
      }

      return SIMPLE;
   }

   public static SequencerType byName(String name) {
      SequencerType type = (SequencerType)types.get(name);
      if (type == null) {
         type = (SequencerType)types.get(name.toUpperCase(Locale.ENGLISH));
      }

      return type;
   }

   public static <T extends SequencerType> T register(T type) {
      types.put(type.name().toUpperCase(Locale.ENGLISH), type);
      return type;
   }

   public static void unregister(SequencerType type) {
      types.remove(type.name().toUpperCase(Locale.ENGLISH), type);
   }

   static {
      SIMPLE = register(new SequencerType("Simple", MapWidgetSequencerEffect.Icon.SIMPLE) {
         public void openConfigurationDialog(SequencerType.OpenDialogArguments args) {
            args.parent.addWidget(new SimpleScheduledEffectLoopDialog(args.config));
         }

         public ScheduledEffectLoop createEffectLoop(ConfigurationNode config, Attachment.EffectSink effectSink) {
            SimpleScheduledEffectLoop effectLoop = new SimpleScheduledEffectLoop();
            effectLoop.setEffectSink(effectSink);
            effectLoop.setDelay(EffectLoop.Time.seconds((Double)config.getOrDefault("delay", 0.0D)));
            return effectLoop;
         }
      });
      MIDI = register(new SequencerType("MIDI", MapWidgetSequencerEffect.Icon.MIDI) {
         public void openConfigurationDialog(final SequencerType.OpenDialogArguments args) {
            MidiChartDialog dialog = new MidiChartDialog() {
               public void onChartChanged(MidiChart chart) {
                  args.config.setTo(chart.toYaml());
               }

               public Attachment.EffectSink getEffectSink() {
                  return args.effectSink;
               }
            };
            dialog.setChart(MidiChart.fromYaml(args.config));
            dialog.setDuration(args.duration);
            args.parent.addWidget(dialog);
         }

         public ScheduledEffectLoop createEffectLoop(ConfigurationNode config, Attachment.EffectSink effectSink) {
            MidiScheduledEffectLoop effectLoop = new MidiScheduledEffectLoop();
            effectLoop.setEffectSink(effectSink);
            effectLoop.setChart(MidiChart.fromYaml(config));
            return effectLoop;
         }
      });
   }

   public static final class OpenDialogArguments {
      public final MapWidget parent;
      public final ConfigurationNode config;
      public final EffectLoop.Time duration;
      public final Attachment.EffectSink effectSink;

      public OpenDialogArguments(MapWidget parent, ConfigurationNode config, EffectLoop.Time duration, Attachment.EffectSink effectSink) {
         this.parent = parent;
         this.config = config;
         this.duration = duration;
         this.effectSink = effectSink;
      }
   }
}
