package dev.behindthescenery.core.system.user_interface.imgui.modules;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiKey;
import imgui.type.ImString;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BTScriptalTerminal implements ImGuiDrawElement {

    private final ImString inputBuffer = new ImString();
    private final List<String> outputLines = new ArrayList<>();
    private static final List<String> commandHistory = new ArrayList<>(); // История команд
    private int historyIndex = -1; // Индекс текущей команды в истории


    public Function<String, String> commandExec;

    /**
     * @param commandExec Функция обработки команд
     */
    public BTScriptalTerminal(Function<String, String> commandExec) {
        this.commandExec = commandExec;

    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Область вывода (логи терминала)
        ImGui.beginChild("Output", 0, -ImGui.getFrameHeightWithSpacing(), true);
        for (String line : outputLines) {
            ImGui.textWrapped(line);
        }
        // Автоматическая прокрутка вниз
        if (ImGui.getScrollY() >= ImGui.getScrollMaxY()) {
            ImGui.setScrollHereY(1.0f);
        }
        ImGui.endChild();


        ImGui.pushItemWidth(-1);
        if (ImGui.inputText("##Input", inputBuffer, ImGuiInputTextFlags.EnterReturnsTrue | ImGuiInputTextFlags.CallbackAlways | ImGuiInputTextFlags.CallbackResize, new ImGuiInputTextCallback() {
            @Override
            public void accept(ImGuiInputTextCallbackData imGuiInputTextCallbackData) {
                inputTextCallback(imGuiInputTextCallbackData);
            }
        })) {
            // Обработка ввода при нажатии Enter
            String input = inputBuffer.get().trim();
            if (!input.isEmpty()) {
                processInput(input);
                inputBuffer.set('\0');
            }
        }
        ImGui.popItemWidth();

        handleArrowKeys();
    }

    private void processInput(String input) {
        outputLines.add("> " + input);

        commandHistory.add(input);
        historyIndex = commandHistory.size();

        // Обработка команды
        String output = executeCommand(input);
        if (output != null && !output.isEmpty()) {
            outputLines.add(output);
        }

        // Ограничиваем количество строк в выводе до 100
        if (outputLines.size() > 100) {
            outputLines.removeFirst();
        }
    }

    private String executeCommand(String command) {
        if (command.equalsIgnoreCase("clear")) {
            outputLines.clear();
            return null;
        }
        return commandExec.apply(command);
    }



    private int inputTextCallback(ImGuiInputTextCallbackData data) {
        // Обработка стрелок вверх/вниз
        if (ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.UpArrow))) {
            if (historyIndex > 0) {
                historyIndex--;
                String previousCommand = commandHistory.get(historyIndex);
                inputBuffer.set(previousCommand); // Обновляем ImString
                data.setCursorPos(previousCommand.length()); // Устанавливаем курсор в конец строки
                data.setSelectionStart(previousCommand.length()); // Убираем выделение текста
                data.setSelectionEnd(previousCommand.length());
                data.setBufDirty(true); // Сообщаем ImGui, что буфер был изменен
            }
        } else if (ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.DownArrow))) {
            if (historyIndex < commandHistory.size() - 1) {
                historyIndex++;
                String nextCommand = commandHistory.get(historyIndex);
                inputBuffer.set(nextCommand); // Обновляем ImString
                data.setCursorPos(nextCommand.length()); // Устанавливаем курсор в конец строки
                data.setSelectionStart(nextCommand.length()); // Убираем выделение текста
                data.setSelectionEnd(nextCommand.length());
                data.setBufDirty(true); // Сообщаем ImGui, что буфер был изменен
            } else if (historyIndex == commandHistory.size() - 1) {
                historyIndex++;
                inputBuffer.set(""); // Очищаем ImString
                data.setCursorPos(0); // Устанавливаем курсор в начало
                data.setSelectionStart(0); // Убираем выделение текста
                data.setSelectionEnd(0);
                data.setBufDirty(true); // Сообщаем ImGui, что буфер был изменен
            }
        }
        return 0; // Возвращаем 0, чтобы не изменять поведение по умолчанию
    }

    private void handleArrowKeys() {
        // Обработка стрелки вверх
        if (ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.UpArrow))) {
            if (historyIndex > 0) {
                historyIndex--;
                inputBuffer.set(commandHistory.get(historyIndex)); // Подставляем предыдущую команду
            }
        }

        // Обработка стрелки вниз
        if (ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.DownArrow))) {
            if (historyIndex < commandHistory.size() - 1) {
                historyIndex++;
                inputBuffer.set(commandHistory.get(historyIndex)); // Подставляем следующую команду
            } else if (historyIndex == commandHistory.size() - 1) {
                historyIndex++;
                inputBuffer.set(""); // Очищаем поле ввода
            }
        }
    }
}
