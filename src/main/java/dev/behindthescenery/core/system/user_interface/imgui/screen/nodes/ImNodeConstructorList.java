package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNode;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePin;
import imgui.ImGui;

import java.util.*;
import java.util.function.Supplier;

public record ImNodeConstructorList<T extends ImNode<B>, B extends ImNodePin>(
        ImGuiNodeEditorScreen<T, B> editorScreen,
        Map<String, Map<String, Supplier<T>>> groupedConstructors
) {

    private static final String DEFAULT_GROUP = "Other"; // Группа по умолчанию для конструкторов без группы

    /**
     * Создаёт новый список конструкторов с пустой структурой групп.
     *
     * @param editorScreen Экран редактора узлов.
     */
    public ImNodeConstructorList(ImGuiNodeEditorScreen<T, B> editorScreen) {
        this(editorScreen, new HashMap<>());
    }

    /**
     * Добавляет конструктор в указанную группу.
     *
     * @param group       Название группы (не null).
     * @param id          Уникальный идентификатор конструктора (не null).
     * @param constructor Поставщик экземпляра узла (не null).
     * @return Этот объект для цепочки вызовов.
     * @throws IllegalArgumentException Если group, id или constructor равны null.
     */
    public ImNodeConstructorList<T, B> addConstructor(String group, String id, Supplier<T> constructor) {
        Objects.requireNonNull(group, "Group cannot be null");
        Objects.requireNonNull(id, "Constructor ID cannot be null");
        Objects.requireNonNull(constructor, "Constructor cannot be null");

        // Получаем или создаём под-карту для группы
        Map<String, Supplier<T>> constructorsInGroup = groupedConstructors.computeIfAbsent(group, k -> new HashMap<>());
        constructorsInGroup.put(id, constructor);
        return this;
    }

    /**
     * Добавляет конструктор без группы (помещается в группу по умолчанию "Other").
     *
     * @param id          Уникальный идентификатор конструктора (не null).
     * @param constructor Поставщик экземпляра узла (не null).
     * @return Этот объект для цепочки вызовов.
     * @throws IllegalArgumentException Если id или constructor равны null.
     */
    public ImNodeConstructorList<T, B> addConstructor(String id, Supplier<T> constructor) {
        return addConstructor(DEFAULT_GROUP, id, constructor);
    }

    /**
     * Добавляет несколько конструкторов в указанную группу.
     *
     * @param group       Название группы (не null).
     * @param constructors Карта идентификаторов и поставщиков узлов (не null).
     * @return Этот объект для цепочки вызовов.
     * @throws IllegalArgumentException Если group или constructors равны null.
     */
    public ImNodeConstructorList<T, B> addConstructors(String group, Map<String, Supplier<T>> constructors) {
        Objects.requireNonNull(group, "Group cannot be null");
        Objects.requireNonNull(constructors, "Constructors map cannot be null");

        Map<String, Supplier<T>> constructorsInGroup = groupedConstructors.computeIfAbsent(group, k -> new HashMap<>());
        constructorsInGroup.putAll(constructors);
        return this;
    }

    /**
     * Получает конструктор по группе и идентификатору.
     *
     * @param group Название группы (не null).
     * @param id    Идентификатор конструктора (не null).
     * @return Optional с поставщиком узла, если найден, иначе пустой Optional.
     * @throws IllegalArgumentException Если group или id равны null.
     */
    public Optional<Supplier<T>> getConstructor(String group, String id) {
        Objects.requireNonNull(group, "Group cannot be null");
        Objects.requireNonNull(id, "Constructor ID cannot be null");

        Map<String, Supplier<T>> constructorsInGroup = groupedConstructors.get(group);
        return Optional.ofNullable(constructorsInGroup != null ? constructorsInGroup.get(id) : null);
    }

    /**
     * Получает конструктор по идентификатору, предполагая, что он в группе по умолчанию.
     *
     * @param id Идентификатор конструктора (не null).
     * @return Optional с поставщиком узла, если найден, иначе пустой Optional.
     * @throws IllegalArgumentException Если id равен null.
     */
    public Optional<Supplier<T>> getConstructor(String id) {
        return getConstructor(DEFAULT_GROUP, id);
    }

    /**
     * Рендерит список конструкторов в виде подменю ImGui, сгруппированных по категориям.
     *
     * @return true, если был создан узел (пользователь выбрал конструктор), иначе false.
     */
    public boolean renderConstructors() {
        ImGui.separatorText("Nodes");

        boolean nodeCreated = false;

        // Сортируем группы для предсказуемого порядка отображения
        List<String> sortedGroups = new ArrayList<>(groupedConstructors.keySet());
        Collections.sort(sortedGroups);

        for (String group : sortedGroups) {
            Map<String, Supplier<T>> constructorsInGroup = groupedConstructors.get(group);

            // Пропускаем пустые группы
            if (constructorsInGroup.isEmpty()) {
                continue;
            }

            // Для группы по умолчанию просто рендерим кнопки без подменю
            if (group.equals(DEFAULT_GROUP)) {
                for (Map.Entry<String, Supplier<T>> entry : constructorsInGroup.entrySet()) {
                    if (ImGui.button(entry.getKey())) {
                        editorScreen.getNodesData().createNode(entry.getValue().get(), true);
                        nodeCreated = true;
                    }
                }
            } else {
                // Для остальных групп создаём подменю
                if (ImGui.beginMenu(group)) {
                    for (Map.Entry<String, Supplier<T>> entry : constructorsInGroup.entrySet()) {
                        if (ImGui.menuItem(entry.getKey())) {
                            editorScreen.getNodesData().createNode(entry.getValue().get(), true);
                            nodeCreated = true;
                        }
                    }
                    ImGui.endMenu();
                }
            }
        }

        return nodeCreated;
    }
}
