package dev.behindthescenery.core.system.user_interface.imgui.extern;

import imgui.ImGui;
import imgui.type.*;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TagEditor<T extends NbtElement> {
    private static final int BUFFER_SIZE = 512;

    private final String name;
    private final T tag;
    private final Consumer<T> consumer;
    private boolean valueChanged = false;
    private NbtList currentListTag = null;

    public TagEditor(String name, T tag, Consumer<T> consumer) {
        this.name = name;
        this.tag = tag;
        this.consumer = consumer;
    }

    public boolean edit() {
        return switch (tag.getType()) {
            case 1 -> editByteTag();
            case 2 -> editShortTag();
            case 3 -> editIntTag();
            case 4 -> editLongTag();
            case 5 -> editFloatTag();
            case 6 -> editDoubleTag();
            case 7 -> editByteArrayTag();
            case 8 -> editStringTag();
            case 9 -> editListTag();
            case 10 -> editCompoundTag();
            case 11 -> editIntArrayTag();
            case 12 -> editLongArrayTag();
            default -> false;
        };
    }

    private boolean editByteTag() {
        NbtByte byteTag = (NbtByte) tag;
        ImInt value = new ImInt(byteTag.byteValue());

        if (handleIntInput("Byte", value)) {
            int clampedValue = Math.min(Math.max(value.get(), Byte.MIN_VALUE), Byte.MAX_VALUE);
            consumer.accept((T) NbtByte.of((byte) clampedValue));
            return true;
        }
        return false;
    }

    private boolean editShortTag() {
        NbtShort shortTag = (NbtShort) tag;
        ImInt value = new ImInt(shortTag.shortValue());

        if (handleIntInput("Short", value)) {
            int clampedValue = Math.min(Math.max(value.get(), Short.MIN_VALUE), Short.MAX_VALUE);
            consumer.accept((T) NbtShort.of((short) clampedValue));
            return true;
        }
        return false;
    }

    private boolean editIntTag() {
        NbtInt intTag = (NbtInt) tag;
        ImInt value = new ImInt(intTag.intValue());

        if (handleIntInput("Int", value)) {
            consumer.accept((T) NbtInt.of(value.get()));
            return true;
        }
        return false;
    }

    private boolean editLongTag() {
        NbtLong longTag = (NbtLong) tag;
        ImLong value = new ImLong(longTag.longValue());

        if (ImGui.inputScalar(name, value)) {
            consumer.accept((T) NbtLong.of(value.get()));
            return true;
        }
        setTooltipIfHovered("Long");
        return false;
    }

    private boolean editFloatTag() {
        NbtFloat floatTag = (NbtFloat) tag;
        ImFloat value = new ImFloat(floatTag.floatValue());

        if (ImGui.inputFloat(name, value)) {
            consumer.accept((T) NbtFloat.of(value.get()));
            return true;
        }
        setTooltipIfHovered("Float");
        return false;
    }

    private boolean editDoubleTag() {
        NbtDouble doubleTag = (NbtDouble) tag;
        ImDouble value = new ImDouble(doubleTag.doubleValue());

        if (ImGui.inputDouble(name, value)) {
            consumer.accept((T) NbtDouble.of(value.get()));
            return true;
        }
        setTooltipIfHovered("Double");
        return false;
    }

    private boolean editStringTag() {
        NbtString stringTag = (NbtString) tag;
        ImString value = new ImString(stringTag.asString(), BUFFER_SIZE);

        if (ImGui.inputText(name, value)) {
            consumer.accept((T) NbtString.of(value.get()));
            return true;
        }
        setTooltipIfHovered("String");
        return false;
    }

    private boolean editByteArrayTag() {
        return editArrayTag((NbtByteArray) tag, "Byte Array");
    }

    private boolean editIntArrayTag() {
        return editArrayTag((NbtIntArray) tag, "Int Array");
    }

    private boolean editLongArrayTag() {
        return editArrayTag((NbtLongArray) tag, "Long Array");
    }

    private boolean editListTag() {
        currentListTag = (NbtList) tag;
        boolean result = editCollectionTag(currentListTag, "List");
        currentListTag = null;
        return result;
    }

    private boolean editCompoundTag() {
        NbtCompound compoundTag = (NbtCompound) tag;
        ImGui.pushID(name + "_compoundTag");

        boolean value = ImGui.treeNode(name);
        setTooltipIfHovered("CompoundTag");
        if (value) {
            for (String key : compoundTag.getKeys()) {
                if (BtsImGui.beginEditElementTag(key, compoundTag.get(key), s -> {
                    compoundTag.put(key, s);
                    consumer.accept((T) compoundTag);
                })) {
                    valueChanged = true;
                }
            }
            ImGui.treePop();
        }
        ImGui.popID();

        return valueChanged;
    }

    private <S extends NbtElement> boolean editArrayTag(AbstractNbtList<S> arrayTag, String tooltip) {
        ImGui.pushID(name + "_" + tooltip.replace(" ", ""));
        List<Integer> indicesToRemove = new ArrayList<>();

        boolean value = ImGui.treeNode(name);
        setTooltipIfHovered(tooltip);

        if (value) {
            for (int i = 0; i < arrayTag.size(); i++) {
                int finalI = i;
                if (BtsImGui.beginEditElementTag(String.valueOf(i), arrayTag.get(i), s -> {
                    arrayTag.set(finalI, s);
                    consumer.accept((T) arrayTag);
                })) {
                    valueChanged = true;
                }

                handleItemContextMenu(arrayTag, i, indicesToRemove);
            }

            ImGui.treePop();
        }

        ImGui.popID();

        for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
            arrayTag.remove(indicesToRemove.get(i).intValue());
            valueChanged = true;
        }

        if (valueChanged) {
            consumer.accept((T) arrayTag);
        }

        return valueChanged;
    }

    private <S extends NbtElement> boolean editCollectionTag(AbstractNbtList<S> collectionTag, String tooltip) {
        ImGui.pushID(name + "_" + tooltip.replace(" ", ""));
        List<Integer> indicesToRemove = new ArrayList<>();

        boolean value = ImGui.treeNode(name);
        setTooltipIfHovered(tooltip);

        if (value) {
            for (int i = 0; i < collectionTag.size(); i++) {
                int finalI = i;
                if (BtsImGui.beginEditElementTag(String.valueOf(i), collectionTag.get(i), s -> {
                    collectionTag.set(finalI, s);
                    consumer.accept((T) collectionTag);
                })) {
                    valueChanged = true;
                }

                handleItemContextMenu(collectionTag, i, indicesToRemove);
            }

            ImGui.treePop();
        }

        ImGui.popID();

        for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
            collectionTag.remove(indicesToRemove.get(i).intValue());
            valueChanged = true;
        }

        if (valueChanged) {
            consumer.accept((T) collectionTag);
        }

        return valueChanged;
    }

    private <S extends NbtElement> void handleItemContextMenu(AbstractNbtList<S> collectionTag, int index, List<Integer> indicesToRemove) {
        if (ImGui.beginPopupContextItem("context_" + index + "_" + name)) {
            if (ImGui.beginMenu("Добавить")) {
                if (collectionTag instanceof NbtByteArray) {
                    if (ImGui.menuItem("Byte")) {
                        collectionTag.add((S) NbtByte.of((byte) 0));
                        valueChanged = true;
                    }
                }
                else if (collectionTag instanceof NbtIntArray) {
                    if (ImGui.menuItem("Int")) {
                        collectionTag.add((S) NbtInt.of(0));
                        valueChanged = true;
                    }
                }
                else if (collectionTag instanceof NbtLongArray) {
                    if (ImGui.menuItem("Long")) {
                        collectionTag.add((S) NbtLong.of(0L));
                        valueChanged = true;
                    }
                }
                else if (collectionTag instanceof NbtList listTag) {
                    if(listTag.getHeldType() == 0) {
                        if (ImGui.menuItem("Byte")) {
                            addElementToCollection(collectionTag, NbtByte.of((byte) 0));
                        }
                        if (ImGui.menuItem("Short")) {
                            addElementToCollection(collectionTag, NbtShort.of((short) 0));
                        }
                        if (ImGui.menuItem("Int")) {
                            addElementToCollection(collectionTag, NbtInt.of(0));
                        }
                        if (ImGui.menuItem("Long")) {
                            addElementToCollection(collectionTag, NbtLong.of(0));
                        }
                        if (ImGui.menuItem("Float")) {
                            addElementToCollection(collectionTag, NbtFloat.of(0));
                        }
                        if (ImGui.menuItem("Double")) {
                            addElementToCollection(collectionTag, NbtDouble.of(0));
                        }
                        if (ImGui.menuItem("String")) {
                            addElementToCollection(collectionTag, NbtString.of(""));
                        }
                        if (ImGui.menuItem("CompoundTag")) {
                            addElementToCollection(collectionTag, new NbtCompound());
                        }
                        if (ImGui.menuItem("List")) {
                            addElementToCollection(collectionTag, new NbtList());
                        }
                        if (ImGui.menuItem("Byte Array")) {
                            addElementToCollection(collectionTag, new NbtByteArray(new ArrayList<>()));
                        }
                        if (ImGui.menuItem("Int Array")) {
                            addElementToCollection(collectionTag, new NbtIntArray(new ArrayList<>()));
                        }
                        if (ImGui.menuItem("Long Array")) {
                            addElementToCollection(collectionTag, new NbtLongArray(new ArrayList<>()));
                        }
                    } else {
                        switch (listTag.getHeldType()) {
                            case 1 -> {
                                if (ImGui.menuItem("Byte")) {
                                    addElementToCollection(collectionTag, NbtByte.of((byte) 0));
                                }
                            }
                            case 2 -> {
                                if (ImGui.menuItem("Short")) {
                                    addElementToCollection(collectionTag, NbtShort.of((short) 0));
                                }
                            }
                            case 3 -> {
                                if (ImGui.menuItem("Int")) {
                                    addElementToCollection(collectionTag, NbtInt.of(0));
                                }
                            }
                            case 4 -> {
                                if (ImGui.menuItem("Long")) {
                                    addElementToCollection(collectionTag, NbtLong.of(0));
                                }
                            }
                            case 5 -> {
                                if (ImGui.menuItem("Float")) {
                                    addElementToCollection(collectionTag, NbtFloat.of(0));
                                }
                            }
                            case 6 -> {
                                if (ImGui.menuItem("Double")) {
                                    addElementToCollection(collectionTag, NbtDouble.of(0));
                                }
                            }
                            case 7 -> {
                                if (ImGui.menuItem("Byte Array")) {
                                    addElementToCollection(collectionTag, new NbtByteArray(new ArrayList<>()));
                                }
                            }
                            case 8 -> {
                                if (ImGui.menuItem("String")) {
                                    addElementToCollection(collectionTag, NbtString.of(""));
                                }
                            }
                            case 9 -> {
                                if (ImGui.menuItem("List")) {
                                    addElementToCollection(collectionTag, new NbtList());
                                }
                            }
                            case 10 -> {
                                if (ImGui.menuItem("CompoundTag")) {
                                    addElementToCollection(collectionTag, new NbtCompound());
                                }
                            }
                            case 11 -> {
                                if (ImGui.menuItem("Int Array")) {
                                    addElementToCollection(collectionTag, new NbtIntArray(new ArrayList<>()));
                                }
                            }
                            case 12 -> {
                                if (ImGui.menuItem("Long Array")) {
                                    addElementToCollection(collectionTag, new NbtLongArray(new ArrayList<>()));
                                }
                            }
                            default -> {}
                        }
                    }
                }
                ImGui.endMenu();
            }

            if (ImGui.menuItem("Удалить")) {
                indicesToRemove.add(index);
            }

            ImGui.endPopup();
        }
    }

    private <S extends NbtElement> void addElementToCollection(AbstractNbtList<S> collectionTag, NbtElement newTag) {
        if (collectionTag instanceof NbtList) {
            collectionTag.add((S) newTag);
            valueChanged = true;
            consumer.accept((T) collectionTag);
        }
    }

    private boolean handleIntInput(String tooltip, ImInt value) {
        if (ImGui.inputInt(name, value)) {
            return true;
        }
        setTooltipIfHovered(tooltip);
        return false;
    }

    private void setTooltipIfHovered(String tooltip) {
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(tooltip);
        }
    }
}
