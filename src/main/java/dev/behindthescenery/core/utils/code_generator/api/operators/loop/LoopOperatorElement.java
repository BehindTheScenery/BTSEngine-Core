package dev.behindthescenery.core.utils.code_generator.api.operators.loop;

import dev.behindthescenery.core.utils.code_generator.api.CodeOperatorElement;

public interface LoopOperatorElement extends CodeOperatorElement {

    @Override
    default boolean useEndLineSymbol() {
        return false;
    }

}
