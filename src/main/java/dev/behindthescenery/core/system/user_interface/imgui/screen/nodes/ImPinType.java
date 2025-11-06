package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes;

public enum ImPinType {
    Input(0),
    Output(1);

    final int id;

    ImPinType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isInput() {
        return this == Input;
    }

    public boolean isOutput() {
        return this == Output;
    }

    public boolean canConnect(ImPinType pinType) {
        return this != pinType;
    }
}
