//package dev.behindthescenery.core.system.user_interface.imgui.modules;
//
//import blue.endless.jankson.Comment;
//import dev.behindthescenery.config.api.AbstractConfig;
//import dev.behindthescenery.config.api.BtsConfigApi;
//import dev.behindthescenery.config.api.ConfigHolder;
//import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
//import dev.behindthescenery.core.system.user_interface.imgui.extern.BtsImGui;
//import imgui.ImGui;
//import imgui.ImVec2;
//import imgui.ImVec4;
//import imgui.type.ImInt;
//import net.minecraft.client.gui.DrawContext;
//import org.jetbrains.annotations.Nullable;
//
//import java.lang.reflect.Array;
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.util.*;
//
//public class BTSConfigEditor implements ImGuiDrawElement {
//
//
//    private static Map<String, ConfigHolder<AbstractConfig>> configListCache = new HashMap<>();
//
//    private @Nullable ConfigHolder<?> configHolder;
//    private @Nullable AbstractConfig config;
//    private List<Field> fields = new ArrayList<>();
//
//    public BTSConfigEditor() {
//        this(null);
//    }
//
//    public BTSConfigEditor(@Nullable ConfigHolder<?> config) {
//        this.setConfig(config);
//    }
//
//    public void setConfig(@Nullable ConfigHolder<?> config) {
//        this.fields.clear();
//        this.configHolder = config;
//
//        if(config == null) {
//            this.config = null;
//            return;
//        }
//
//        this.config = configHolder.getConfig();
//        fields.addAll(List.of(this.config.getClass().getFields()));
//    }
//
//    public void renderConfigFileSelector() {
//        ImInt index = new ImInt(config == null ? 0 : getConfigIndex(config.getClass().getSimpleName()));
//
//        ImGui.text("Config: ");
//        ImGui.sameLine();
//
//        ImGui.pushID("_config_selector");
//        if (ImGui.combo("", index, getConfigList())) {
//            Optional<ConfigHolder<AbstractConfig>> value = getConfig(getConfigList()[index.get()]);
//            if(value.isPresent()) setConfig(value.get());
//            else setConfig(null);
//        }
//        ImGui.popID();
//    }
//
//    public void renderImGui() {
//        render(null, 0,0,0);
//    }
//
//    @Override
//    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
//        renderConfigFileSelector();
//
//        ImGui.dummy(new ImVec2(0, ImGui.getTextLineHeight() / 2));
//        ImGui.separator();
//
//        if(this.config == null) {
//            ImGui.text("No config loaded!");
//            return;
//        }
//
////        ImGui.setWindowFontScale(1.3f);
////        ImGui.text("Loaded config: " + this.config.getClass().getName());
////        ImGui.setWindowFontScale(1.0f);
//
//        for (Field field : this.fields) {
//            try {
//                createInputFromFieldUnSafe(field);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    private void createInputFromFieldUnSafe(Field field) throws Exception {
//        Object value = field.get(this.config);
//
//        String comment = "";
//
//        try {
//            comment = field.getAnnotation(Comment.class).value();
//        } catch (NullPointerException e) {}
//
//        if (value instanceof Byte integer) {
//            BtsImGui.beginEditElementInt(field.getName(), integer, s -> setValue(field, s));
//            createTooltip(comment, "Byte");
//            return;
//        }
//        if (value instanceof Short integer) {
//            BtsImGui.beginEditElementInt(field.getName(), integer, s -> setValue(field, s));
//            createTooltip(comment, "Short");
//            return;
//        }
//        if (value instanceof Integer integer) {
//            BtsImGui.beginEditElementInt(field.getName(), integer, s -> setValue(field, s));
//            createTooltip(comment, "Int");
//            return;
//        }
//        if (value instanceof Long element) {
//            BtsImGui.beginEditElementLong(field.getName(), element, s -> setValue(field, s));
//            createTooltip(comment, "Long");
//            return;
//        }
//        if (value instanceof Float element) {
//            BtsImGui.beginEditElementFloat(field.getName(), element, s -> setValue(field, s));
//            createTooltip(comment, "Float");
//
//            return;
//        }
//        if (value instanceof Double element) {
//            BtsImGui.beginEditElementDouble(field.getName(), element, s -> setValue(field, s));
//            createTooltip(comment, "Double");
//            return;
//        }
//        if (value instanceof String element) {
//            BtsImGui.beginEditElementString(field.getName(), element, s -> setValue(field, s));
//            createTooltip(comment, "String");
//            return;
//        }
//        if (value instanceof Boolean element) {
//            BtsImGui.beginEditElementBoolean(field.getName(), element, s -> setValue(field, s));
//            createTooltip(comment, "Boolean");
//            return;
//        }
//
//        if (value.getClass().isArray()) {
//            int length = Array.getLength(value);
//            List<Object> elements = new ArrayList<>();
//            for (int i = 0; i < length; i++) {
//                elements.add(Array.get(value, i));
//            }
//
//            BtsImGui.beginArrayEdit(field.getName(), elements, (list) -> {
//                setValue(field, castArray(list.toArray(), field));
//            }, field.getType().getComponentType(), comment);
//            return;
//        }
//
//
//        if (value instanceof List<?> list) {
//            if(field.getGenericType() instanceof ParameterizedType parameterizedType) {
//                BtsImGui.beginArrayEdit(field.getName(), new ArrayList<>(list), (listNew) -> {
//                    setValue(field, listNew);
//                }, Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName()), comment);
//                return;
//            }
//        }
//
////        // **Обработка Map**
////        if (value instanceof Map<?, ?> map) {
////            BTSImGui.beginMapEdit(field.getName(), map, (mapNew) -> {
////                ((Map<Object, Object>) map).put(key, newValue);
////                setValue(field, map);
////            }, key -> {
////                ((Map<Object, Object>) map).remove(key);
////                setValue(field, map);
////            });
////            return;
////        }
//    }
//
//    private void createTooltip(String comment, String type) {
//        if(ImGui.isItemHovered()) {
//            ImGui.beginTooltip();
//            if(!comment.isEmpty()) {
//                ImGui.separator();
//                ImGui.text("Comment: " + comment);
//            }
//            ImGui.separator();
//            ImGui.textColored(new ImVec4(1, (float) 215 / 255,0, 1), "Type: " + type);
//            ImGui.separator();
//            ImGui.endTooltip();
//        }
//    }
//
//    private <T> T[] castArray(Object[] input, Field field) {
//        Class<?> arrayType = field.getType().getComponentType();
//
//        if (arrayType == null) {
//            throw new IllegalArgumentException("Field is not an array type");
//        }
//
//        T[] newArray = (T[]) Array.newInstance(arrayType, input.length);
//
//        for (int i = 0; i < input.length; i++) {
//            newArray[i] = (T) arrayType.cast(input[i]);
//        }
//
//        return newArray;
//    }
//
//
//    private void setValue(Field field, Object value) {
//        try {
//            field.setAccessible(true);
//            field.set(this.config, value);
//            this.configHolder.save();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static int getConfigIndex(String name) {
//        for (int i = 0; i < getConfigList().length; i++) {
//            if(getConfigList()[i].equalsIgnoreCase(name)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public static String[] getConfigList() {
//        if(configListCache.isEmpty()) {
//            Map<String, ConfigHolder<AbstractConfig>> configMap = new HashMap<>();
//
//            for (ConfigHolder<AbstractConfig> value : BtsConfigApi.getConfigManager().getAllConfigHolders().values()) {
//                configMap.put(value.getConfig().getClass().getSimpleName(), value);
//            }
//
//            configListCache = configMap;
//        }
//        return configListCache.keySet().toArray(new String[0]);
//    }
//
//    public static Optional<ConfigHolder<AbstractConfig>> getConfig(String name) {
//        return Optional.ofNullable(configListCache.get(name));
//    }
//}
