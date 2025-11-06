package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImNodeVariableContainer {

    protected final Map<Long, Object> values;

    public ImNodeVariableContainer() {
        this(new HashMap<>());
    }

    public ImNodeVariableContainer(Map<Long, Object> map) {
        this.values = map;
    }

    public <T> void setValue(long pinId, T value) {
        values.put(pinId, value);
    }

    public boolean hasValue(long pinId) {
        return values.containsKey(pinId);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueUnsafe(long pinId) {
        return (T) values.get(pinId);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getValue(long pinId) {
        if(hasValue(pinId)) return Optional.of((T) values.get(pinId));
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrCreate(long pinId, T value) {
        return (T) values.getOrDefault(pinId, value);
    }

    public Map<Long, Object> getValues() {
        return values;
    }

    public ImNodeVariableContainer copy() {
        return new ImNodeVariableContainer(new HashMap<>(values));
    }

}
