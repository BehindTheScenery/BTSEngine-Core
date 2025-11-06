package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.utils;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.ImNodeVariableContainer;

/**
 * Интерфейс для рендеринга поля редактирования в ImGui.
 */
public interface EditFieldRenderer {
    /**
     * Создает Runnable для рендеринга поля редактирования.
     *
     * @param variableContainer Список конструкторов или значений узла.
     * @param elementWidth Ширина элемента (0 для значения по умолчанию).
     * @param elementFlags Флаги ImGui для настройки поведения.
     * @return Runnable, выполняющий рендеринг.
     */
    Runnable createEditField(ImNodeVariableContainer variableContainer, float elementWidth, int elementFlags);
}
