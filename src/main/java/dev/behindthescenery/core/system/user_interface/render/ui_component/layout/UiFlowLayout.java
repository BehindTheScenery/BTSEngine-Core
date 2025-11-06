package dev.behindthescenery.core.system.user_interface.render.ui_component.layout;

import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.container.AbstractUiElementsContainer;

public class UiFlowLayout implements UiLayout{

    protected final Direction direction;

    public UiFlowLayout(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void apply(AbstractUiElementsContainer container) {
        int offset = 0;

        for (AbstractUiComponent component : container.getUiComponents()) {
            switch (direction) {
                case VERTICAL -> {
                    component.setPosition(component.getPosX(), component.getPosY() + offset);
                    offset += component.getHeight();
                }
                case HORIZONTAL ->  {
                    component.setPosition(container.getPosX() + offset, component.getPosY());
                    offset += component.getWidth();
                }
            }
        }
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}
