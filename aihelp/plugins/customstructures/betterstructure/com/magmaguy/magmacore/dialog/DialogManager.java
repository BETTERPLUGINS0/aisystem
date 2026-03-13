/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.magmaguy.magmacore.dialog;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DialogManager {
    private DialogManager() {
    }

    public static JsonObject serializeItemComponents(ItemStack itemStack) {
        try {
            JsonObject components = new JsonObject();
            if (!itemStack.hasItemMeta()) {
                return components;
            }
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName()) {
                JsonObject customName = new JsonObject();
                customName.addProperty("text", meta.getDisplayName());
                components.add("minecraft:custom_name", (JsonElement)customName);
            }
            if (meta.hasLore() && meta.getLore() != null) {
                JsonArray loreArray = new JsonArray();
                for (String loreLine : meta.getLore()) {
                    JsonObject loreComponent = new JsonObject();
                    loreComponent.addProperty("text", loreLine);
                    loreArray.add((JsonElement)loreComponent);
                }
                components.add("minecraft:lore", (JsonElement)loreArray);
            }
            if (meta.hasCustomModelData()) {
                components.addProperty("minecraft:custom_model_data", (Number)meta.getCustomModelData());
            }
            if (meta.isUnbreakable()) {
                JsonObject unbreakable = new JsonObject();
                unbreakable.addProperty("show_in_tooltip", Boolean.valueOf(false));
                components.add("minecraft:unbreakable", (JsonElement)unbreakable);
            }
            if (itemStack.getType().getMaxDurability() > 0) {
                components.addProperty("minecraft:max_stack_size", (Number)itemStack.getType().getMaxStackSize());
            }
            return components;
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to serialize item components: " + e.getMessage());
            e.printStackTrace();
            return new JsonObject();
        }
    }

    public static void sendDialog(Player player, DialogBuilder builder) {
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("minecraft:dialog show " + player.getName() + " " + builder.build().toString()));
    }

    public static abstract class DialogBuilder<T extends DialogBuilder<T>> {
        protected final String type;
        protected final List<BodyElement> bodyElements = new ArrayList<BodyElement>();
        protected final List<InputControl> inputControls = new ArrayList<InputControl>();
        protected JsonElement title;
        protected JsonElement externalTitle;
        protected Boolean canCloseWithEscape;
        protected Boolean pause;
        protected AfterAction afterAction;

        protected DialogBuilder(String type) {
            this.type = type;
        }

        public T title(JsonElement title) {
            this.title = title;
            return (T)this;
        }

        public T title(String text) {
            return this.title(TextComponent.of(text));
        }

        public T externalTitle(JsonElement externalTitle) {
            this.externalTitle = externalTitle;
            return (T)this;
        }

        public T externalTitle(String text) {
            return this.externalTitle(TextComponent.of(text));
        }

        public T addBody(BodyElement element) {
            this.bodyElements.add(element);
            return (T)this;
        }

        public T addInput(InputControl control) {
            this.inputControls.add(control);
            return (T)this;
        }

        public T canCloseWithEscape(boolean canCloseWithEscape) {
            this.canCloseWithEscape = canCloseWithEscape;
            return (T)this;
        }

        public T pause(boolean pause) {
            this.pause = pause;
            return (T)this;
        }

        public T afterAction(AfterAction afterAction) {
            this.afterAction = afterAction;
            return (T)this;
        }

        protected JsonObject buildBase() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", this.type);
            if (this.title != null) {
                obj.add("title", this.title);
            }
            if (this.externalTitle != null) {
                obj.add("external_title", this.externalTitle);
            }
            if (!this.bodyElements.isEmpty()) {
                if (this.bodyElements.size() == 1) {
                    obj.add("body", (JsonElement)this.bodyElements.get(0).toJson());
                } else {
                    JsonArray arr = new JsonArray();
                    for (BodyElement element : this.bodyElements) {
                        arr.add((JsonElement)element.toJson());
                    }
                    obj.add("body", (JsonElement)arr);
                }
            }
            if (!this.inputControls.isEmpty()) {
                JsonArray inputsArray = new JsonArray();
                for (InputControl control : this.inputControls) {
                    inputsArray.add((JsonElement)control.toJson());
                }
                obj.add("inputs", (JsonElement)inputsArray);
            }
            if (this.canCloseWithEscape != null) {
                obj.addProperty("can_close_with_escape", this.canCloseWithEscape);
            }
            if (this.pause != null) {
                obj.addProperty("pause", this.pause);
            }
            if (this.afterAction != null) {
                obj.addProperty("after_action", this.afterAction.value);
            }
            return obj;
        }

        public abstract JsonObject build();

        public String toJson() {
            return this.build().toString();
        }
    }

    public static final class TextComponent {
        private TextComponent() {
        }

        public static JsonElement of(String text) {
            JsonObject obj = new JsonObject();
            obj.addProperty("text", text);
            return obj;
        }
    }

    public static class DynamicCustomAction
    implements Action {
        private final String id;
        private final Map<String, JsonElement> additions = new HashMap<String, JsonElement>();

        public DynamicCustomAction(String id) {
            this.id = Objects.requireNonNull(id, "id");
        }

        public DynamicCustomAction add(String key, JsonElement value) {
            this.additions.put(key, value);
            return this;
        }

        public DynamicCustomAction add(String key, String value) {
            this.additions.put(key, (JsonElement)new JsonPrimitive(value));
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "dynamic/custom");
            obj.addProperty("id", this.id);
            if (!this.additions.isEmpty()) {
                JsonObject addObj = new JsonObject();
                for (Map.Entry<String, JsonElement> entry : this.additions.entrySet()) {
                    addObj.add(entry.getKey(), entry.getValue());
                }
                obj.add("additions", (JsonElement)addObj);
            }
            return obj;
        }
    }

    public static class DynamicRunCommandAction
    implements Action {
        private final String template;

        public DynamicRunCommandAction(String template) {
            this.template = Objects.requireNonNull(template, "template");
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "dynamic/run_command");
            obj.addProperty("template", this.template);
            return obj;
        }
    }

    public static class CustomAction
    implements Action {
        private final String id;
        private String payload;

        public CustomAction(String id) {
            this.id = Objects.requireNonNull(id, "id");
        }

        public CustomAction payload(String payload) {
            this.payload = payload;
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "custom");
            obj.addProperty("id", this.id);
            if (this.payload != null) {
                obj.addProperty("payload", this.payload);
            }
            return obj;
        }
    }

    public static class ShowDialogAction
    implements Action {
        private final DialogReference dialog;

        public ShowDialogAction(DialogReference dialog) {
            this.dialog = Objects.requireNonNull(dialog, "dialog");
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "show_dialog");
            obj.add("dialog", this.dialog.toJsonElement());
            return obj;
        }
    }

    public static class CopyToClipboardAction
    implements Action {
        private final String value;

        public CopyToClipboardAction(String value) {
            this.value = Objects.requireNonNull(value, "value");
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "copy_to_clipboard");
            obj.addProperty("value", this.value);
            return obj;
        }
    }

    public static class ChangePageAction
    implements Action {
        private final int page;

        public ChangePageAction(int page) {
            this.page = page;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "change_page");
            obj.addProperty("page", (Number)this.page);
            return obj;
        }
    }

    public static class SuggestCommandAction
    implements Action {
        private final String command;

        public SuggestCommandAction(String command) {
            this.command = Objects.requireNonNull(command, "command");
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "suggest_command");
            obj.addProperty("command", this.command);
            return obj;
        }
    }

    public static class RunCommandAction
    implements Action {
        private final String command;

        public RunCommandAction(String command) {
            this.command = Objects.requireNonNull(command, "command");
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "run_command");
            obj.addProperty("command", this.command);
            return obj;
        }
    }

    public static class OpenUrlAction
    implements Action {
        private final String url;

        public OpenUrlAction(String url) {
            this.url = Objects.requireNonNull(url, "url");
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "open_url");
            obj.addProperty("url", this.url);
            return obj;
        }
    }

    public static class ActionButton {
        private final JsonElement label;
        private final Action action;
        private JsonElement tooltip;
        private Integer width;

        private ActionButton(JsonElement label, Action action) {
            this.label = Objects.requireNonNull(label, "label");
            this.action = Objects.requireNonNull(action, "action");
        }

        public static ActionButton of(JsonElement label, Action action) {
            return new ActionButton(label, action);
        }

        public static ActionButton of(String label, Action action) {
            return new ActionButton(TextComponent.of(label), action);
        }

        public ActionButton tooltip(JsonElement tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public ActionButton tooltip(String text) {
            return this.tooltip(TextComponent.of(text));
        }

        public ActionButton width(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.width = width;
            return this;
        }

        private JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.add("label", this.label);
            if (this.tooltip != null) {
                obj.add("tooltip", this.tooltip);
            }
            if (this.width != null) {
                obj.addProperty("width", (Number)this.width);
            }
            obj.add("action", (JsonElement)this.action.toJson());
            return obj;
        }
    }

    public static class NumberRangeInput
    implements InputControl {
        private final String key;
        private final JsonElement label;
        private String labelFormat;
        private Integer width;
        private Float start;
        private Float end;
        private Float step;
        private Float initial;

        private NumberRangeInput(String key, JsonElement label) {
            this.key = Objects.requireNonNull(key, "key");
            this.label = Objects.requireNonNull(label, "label");
        }

        public static NumberRangeInput of(String key, JsonElement label) {
            return new NumberRangeInput(key, label);
        }

        public static NumberRangeInput of(String key, String label) {
            return new NumberRangeInput(key, TextComponent.of(label));
        }

        public NumberRangeInput labelFormat(String format) {
            this.labelFormat = format;
            return this;
        }

        public NumberRangeInput width(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.width = width;
            return this;
        }

        public NumberRangeInput start(float start) {
            this.start = Float.valueOf(start);
            return this;
        }

        public NumberRangeInput end(float end) {
            this.end = Float.valueOf(end);
            return this;
        }

        public NumberRangeInput step(float step) {
            this.step = Float.valueOf(step);
            return this;
        }

        public NumberRangeInput initial(float initial) {
            this.initial = Float.valueOf(initial);
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "minecraft:number_range");
            obj.addProperty("key", this.key);
            obj.add("label", this.label);
            if (this.labelFormat != null) {
                obj.addProperty("label_format", this.labelFormat);
            }
            if (this.width != null) {
                obj.addProperty("width", (Number)this.width);
            }
            if (this.start != null) {
                obj.addProperty("start", (Number)this.start);
            }
            if (this.end != null) {
                obj.addProperty("end", (Number)this.end);
            }
            if (this.step != null) {
                obj.addProperty("step", (Number)this.step);
            }
            if (this.initial != null) {
                obj.addProperty("initial", (Number)this.initial);
            }
            return obj;
        }
    }

    public static class SingleOptionInput
    implements InputControl {
        private final String key;
        private final JsonElement label;
        private final List<Option> options = new ArrayList<Option>();
        private Boolean labelVisible;
        private Integer width;

        private SingleOptionInput(String key, JsonElement label) {
            this.key = Objects.requireNonNull(key, "key");
            this.label = Objects.requireNonNull(label, "label");
        }

        public static SingleOptionInput of(String key, JsonElement label) {
            return new SingleOptionInput(key, label);
        }

        public static SingleOptionInput of(String key, String label) {
            return new SingleOptionInput(key, TextComponent.of(label));
        }

        public SingleOptionInput labelVisible(boolean visible) {
            this.labelVisible = visible;
            return this;
        }

        public SingleOptionInput width(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.width = width;
            return this;
        }

        public SingleOptionInput addOption(String id, JsonElement display, boolean initial) {
            this.options.add(new Option(id, display, initial));
            return this;
        }

        public SingleOptionInput addOption(String id, String display, boolean initial) {
            this.options.add(new Option(id, TextComponent.of(display), initial));
            return this;
        }

        @Override
        public JsonObject toJson() {
            if (this.options.isEmpty()) {
                throw new IllegalStateException("single_option input requires at least one option");
            }
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "minecraft:single_option");
            obj.addProperty("key", this.key);
            obj.add("label", this.label);
            if (this.labelVisible != null) {
                obj.addProperty("label_visible", this.labelVisible);
            }
            if (this.width != null) {
                obj.addProperty("width", (Number)this.width);
            }
            JsonArray opts = new JsonArray();
            for (Option opt : this.options) {
                opts.add((JsonElement)opt.toJson());
            }
            obj.add("options", (JsonElement)opts);
            return obj;
        }

        private static class Option {
            private final String id;
            private final JsonElement display;
            private final boolean initial;

            Option(String id, JsonElement display, boolean initial) {
                this.id = id;
                this.display = display;
                this.initial = initial;
            }

            JsonObject toJson() {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", this.id);
                obj.add("display", this.display);
                if (this.initial) {
                    obj.addProperty("initial", Boolean.valueOf(true));
                }
                return obj;
            }
        }
    }

    public static class BooleanInput
    implements InputControl {
        private final String key;
        private final JsonElement label;
        private Boolean initial;
        private String onTrue;
        private String onFalse;

        private BooleanInput(String key, JsonElement label) {
            this.key = Objects.requireNonNull(key, "key");
            this.label = Objects.requireNonNull(label, "label");
        }

        public static BooleanInput of(String key, JsonElement label) {
            return new BooleanInput(key, label);
        }

        public static BooleanInput of(String key, String label) {
            return new BooleanInput(key, TextComponent.of(label));
        }

        public BooleanInput initial(boolean initial) {
            this.initial = initial;
            return this;
        }

        public BooleanInput onTrue(String value) {
            this.onTrue = value;
            return this;
        }

        public BooleanInput onFalse(String value) {
            this.onFalse = value;
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "minecraft:boolean");
            obj.addProperty("key", this.key);
            obj.add("label", this.label);
            if (this.initial != null) {
                obj.addProperty("initial", this.initial);
            }
            if (this.onTrue != null) {
                obj.addProperty("on_true", this.onTrue);
            }
            if (this.onFalse != null) {
                obj.addProperty("on_false", this.onFalse);
            }
            return obj;
        }
    }

    public static class TextInput
    implements InputControl {
        private final String key;
        private final JsonElement label;
        private Integer width;
        private Boolean labelVisible;
        private String initial;
        private Integer maxLength;
        private Integer maxLines;
        private Integer height;

        private TextInput(String key, JsonElement label) {
            this.key = Objects.requireNonNull(key, "key");
            this.label = Objects.requireNonNull(label, "label");
        }

        public static TextInput of(String key, JsonElement label) {
            return new TextInput(key, label);
        }

        public static TextInput of(String key, String label) {
            return new TextInput(key, TextComponent.of(label));
        }

        public TextInput width(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.width = width;
            return this;
        }

        public TextInput labelVisible(boolean visible) {
            this.labelVisible = visible;
            return this;
        }

        public TextInput initial(String initial) {
            this.initial = initial;
            return this;
        }

        public TextInput maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public TextInput multiline(Integer maxLines, Integer height) {
            this.maxLines = maxLines;
            this.height = height;
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "minecraft:text");
            obj.addProperty("key", this.key);
            obj.add("label", this.label);
            if (this.width != null) {
                obj.addProperty("width", (Number)this.width);
            }
            if (this.labelVisible != null) {
                obj.addProperty("label_visible", this.labelVisible);
            }
            if (this.initial != null) {
                obj.addProperty("initial", this.initial);
            }
            if (this.maxLength != null) {
                obj.addProperty("max_length", (Number)this.maxLength);
            }
            if (this.maxLines != null || this.height != null) {
                JsonObject multi = new JsonObject();
                if (this.maxLines != null) {
                    multi.addProperty("max_lines", (Number)this.maxLines);
                }
                if (this.height != null) {
                    multi.addProperty("height", (Number)this.height);
                }
                obj.add("multiline", (JsonElement)multi);
            }
            return obj;
        }
    }

    public static class ItemBody
    implements BodyElement {
        private final String itemId;
        private final int count;
        private JsonObject components;
        private JsonElement description;
        private Boolean showDecoration;
        private Boolean showTooltip;
        private Integer width;
        private Integer height;

        private ItemBody(String itemId, int count) {
            this.itemId = Objects.requireNonNull(itemId, "itemId");
            this.count = count;
        }

        public static ItemBody of(String itemId, int count) {
            return new ItemBody(itemId, count);
        }

        public ItemBody components(JsonObject components) {
            this.components = components;
            return this;
        }

        public ItemBody description(JsonElement description) {
            this.description = description;
            return this;
        }

        public ItemBody description(String text) {
            return this.description(TextComponent.of(text));
        }

        public ItemBody showDecoration(boolean show) {
            this.showDecoration = show;
            return this;
        }

        public ItemBody showTooltip(boolean show) {
            this.showTooltip = show;
            return this;
        }

        public ItemBody width(int width) {
            if (width < 1 || width > 256) {
                throw new IllegalArgumentException("width must be between 1 and 256");
            }
            this.width = width;
            return this;
        }

        public ItemBody height(int height) {
            if (height < 1 || height > 256) {
                throw new IllegalArgumentException("height must be between 1 and 256");
            }
            this.height = height;
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "minecraft:item");
            JsonObject itemObj = new JsonObject();
            itemObj.addProperty("id", this.itemId);
            if (this.count > 0) {
                itemObj.addProperty("count", (Number)this.count);
            }
            if (this.components != null) {
                itemObj.add("components", (JsonElement)this.components);
            }
            obj.add("item", (JsonElement)itemObj);
            if (this.description != null) {
                JsonObject desc = new JsonObject();
                desc.add("contents", this.description);
                obj.add("description", (JsonElement)desc);
            }
            if (this.showDecoration != null) {
                obj.addProperty("show_decoration", this.showDecoration);
            }
            if (this.showTooltip != null) {
                obj.addProperty("show_tooltip", this.showTooltip);
            }
            if (this.width != null) {
                obj.addProperty("width", (Number)this.width);
            }
            if (this.height != null) {
                obj.addProperty("height", (Number)this.height);
            }
            return obj;
        }
    }

    public static class PlainMessageBody
    implements BodyElement {
        private final JsonElement contents;
        private Integer width;

        private PlainMessageBody(JsonElement contents) {
            this.contents = contents;
        }

        public static PlainMessageBody of(JsonElement contents) {
            return new PlainMessageBody(contents);
        }

        public static PlainMessageBody of(String text) {
            return new PlainMessageBody(TextComponent.of(text));
        }

        public PlainMessageBody width(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.width = width;
            return this;
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "minecraft:plain_message");
            obj.add("contents", this.contents);
            if (this.width != null) {
                obj.addProperty("width", (Number)this.width);
            }
            return obj;
        }
    }

    public static class DialogReference {
        private final String id;
        private final DialogBuilder<?> inline;

        private DialogReference(String id, DialogBuilder<?> inline) {
            this.id = id;
            this.inline = inline;
        }

        public static DialogReference id(String id) {
            return new DialogReference(Objects.requireNonNull(id, "id"), null);
        }

        public static DialogReference inline(DialogBuilder<?> builder) {
            return new DialogReference(null, Objects.requireNonNull(builder, "builder"));
        }

        private JsonElement toJsonElement() {
            if (this.id != null) {
                return new JsonPrimitive(this.id);
            }
            return this.inline.build();
        }
    }

    public static class DialogListDialogBuilder
    extends DialogBuilder<DialogListDialogBuilder> {
        private final List<DialogReference> dialogs = new ArrayList<DialogReference>();
        private ActionButton exitAction;
        private Integer columns;
        private Integer buttonWidth;

        public DialogListDialogBuilder() {
            super("minecraft:dialog_list");
        }

        public DialogListDialogBuilder addDialog(DialogReference ref) {
            Objects.requireNonNull(ref, "dialog reference");
            this.dialogs.add(ref);
            return this;
        }

        public DialogListDialogBuilder exitAction(ActionButton button) {
            this.exitAction = button;
            return this;
        }

        public DialogListDialogBuilder columns(int columns) {
            if (columns < 1) {
                throw new IllegalArgumentException("columns must be >= 1");
            }
            this.columns = columns;
            return this;
        }

        public DialogListDialogBuilder buttonWidth(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.buttonWidth = width;
            return this;
        }

        @Override
        public JsonObject build() {
            JsonObject obj = this.buildBase();
            if (!this.dialogs.isEmpty()) {
                JsonArray arr = new JsonArray();
                for (DialogReference ref : this.dialogs) {
                    arr.add(ref.toJsonElement());
                }
                obj.add("dialogs", (JsonElement)arr);
            }
            if (this.exitAction != null) {
                obj.add("exit_action", (JsonElement)this.exitAction.toJson());
            }
            if (this.columns != null) {
                obj.addProperty("columns", (Number)this.columns);
            }
            if (this.buttonWidth != null) {
                obj.addProperty("button_width", (Number)this.buttonWidth);
            }
            return obj;
        }
    }

    public static class ServerLinksDialogBuilder
    extends DialogBuilder<ServerLinksDialogBuilder> {
        private ActionButton exitAction;
        private Integer columns;
        private Integer buttonWidth;

        public ServerLinksDialogBuilder() {
            super("minecraft:server_links");
        }

        public ServerLinksDialogBuilder exitAction(ActionButton button) {
            this.exitAction = button;
            return this;
        }

        public ServerLinksDialogBuilder columns(int columns) {
            if (columns < 1) {
                throw new IllegalArgumentException("columns must be >= 1");
            }
            this.columns = columns;
            return this;
        }

        public ServerLinksDialogBuilder buttonWidth(int width) {
            if (width < 1 || width > 1024) {
                throw new IllegalArgumentException("width must be between 1 and 1024");
            }
            this.buttonWidth = width;
            return this;
        }

        @Override
        public JsonObject build() {
            JsonObject obj = this.buildBase();
            if (this.exitAction != null) {
                obj.add("exit_action", (JsonElement)this.exitAction.toJson());
            }
            if (this.columns != null) {
                obj.addProperty("columns", (Number)this.columns);
            }
            if (this.buttonWidth != null) {
                obj.addProperty("button_width", (Number)this.buttonWidth);
            }
            return obj;
        }
    }

    public static class MultiActionDialogBuilder
    extends DialogBuilder<MultiActionDialogBuilder> {
        private final List<ActionButton> actions = new ArrayList<ActionButton>();
        private Integer columns;
        private ActionButton exitAction;

        public MultiActionDialogBuilder() {
            super("minecraft:multi_action");
        }

        public MultiActionDialogBuilder addAction(ActionButton button) {
            Objects.requireNonNull(button, "button");
            this.actions.add(button);
            return this;
        }

        public MultiActionDialogBuilder columns(int columns) {
            if (columns < 1) {
                throw new IllegalArgumentException("columns must be >= 1");
            }
            this.columns = columns;
            return this;
        }

        public MultiActionDialogBuilder exitAction(ActionButton button) {
            this.exitAction = button;
            return this;
        }

        @Override
        public JsonObject build() {
            if (this.actions.isEmpty()) {
                throw new IllegalStateException("multi_action dialog requires at least one action");
            }
            JsonObject obj = this.buildBase();
            JsonArray arr = new JsonArray();
            for (ActionButton btn : this.actions) {
                arr.add((JsonElement)btn.toJson());
            }
            obj.add("actions", (JsonElement)arr);
            if (this.columns != null) {
                obj.addProperty("columns", (Number)this.columns);
            }
            if (this.exitAction != null) {
                obj.add("exit_action", (JsonElement)this.exitAction.toJson());
            }
            return obj;
        }
    }

    public static class ConfirmationDialogBuilder
    extends DialogBuilder<ConfirmationDialogBuilder> {
        private ActionButton yesButton;
        private ActionButton noButton;

        public ConfirmationDialogBuilder() {
            super("minecraft:confirmation");
        }

        public ConfirmationDialogBuilder yes(ActionButton button) {
            this.yesButton = button;
            return this;
        }

        public ConfirmationDialogBuilder no(ActionButton button) {
            this.noButton = button;
            return this;
        }

        @Override
        public JsonObject build() {
            JsonObject obj = this.buildBase();
            if (this.yesButton != null) {
                obj.add("yes", (JsonElement)this.yesButton.toJson());
            }
            if (this.noButton != null) {
                obj.add("no", (JsonElement)this.noButton.toJson());
            }
            return obj;
        }
    }

    public static class NoticeDialogBuilder
    extends DialogBuilder<NoticeDialogBuilder> {
        private ActionButton action;

        public NoticeDialogBuilder() {
            super("minecraft:notice");
        }

        public NoticeDialogBuilder action(ActionButton action) {
            this.action = action;
            return this;
        }

        @Override
        public JsonObject build() {
            JsonObject obj = this.buildBase();
            if (this.action != null) {
                obj.add("action", (JsonElement)this.action.toJson());
            }
            return obj;
        }
    }

    public static interface Action {
        public JsonObject toJson();
    }

    public static interface InputControl {
        public JsonObject toJson();
    }

    public static interface BodyElement {
        public JsonObject toJson();
    }

    public static enum AfterAction {
        CLOSE("close"),
        NONE("none"),
        WAIT_FOR_RESPONSE("wait_for_response");

        private final String value;

        private AfterAction(String value) {
            this.value = value;
        }
    }
}

