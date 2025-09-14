package dev.behindthescenery.core.utils.code_generator.api.operators.loop;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;

import java.util.List;

public interface ForLoopOperator extends LoopOperatorElement {

    String getVariableName();

    int getStartIndex();

    int getEndIndex();

    int getStep();

    List<CodeElement> getLoopBody();

    @Override
    default boolean useEndLineSymbol() {
        return false;
    }
}
