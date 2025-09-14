package dev.behindthescenery.core.utils.code_generator.basic;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.ImportCodeElement;

public class ImportCodeElementImpl implements ImportCodeElement {

    protected String prefix;
    protected String name;
    protected boolean endSymbol;

    public ImportCodeElementImpl(String prefix, String  name, boolean endSymbol) {
        this.prefix = prefix;
        this.name = name;
        this.endSymbol = endSymbol;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean useEndSymbol() {
        return endSymbol;
    }

    @Override
    public CodeElement copy() {
        return new ImportCodeElementImpl(prefix, name, endSymbol);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }

}
