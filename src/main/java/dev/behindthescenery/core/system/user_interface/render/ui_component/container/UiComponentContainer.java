package dev.behindthescenery.core.system.user_interface.render.ui_component.container;

import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;

import java.util.Collection;

public interface UiComponentContainer {

    Collection<AbstractUiComponent> getUiComponents();

    Collection<AbstractUiComponent> getUiComponentsSortedByOrdinal();

    <T extends AbstractUiComponent> T addComponent(T component);

    <T extends AbstractUiComponent> boolean containsComponent(T component);

    void clearUiComponents();
}
