package dev.behindthescenery.core.arosnatives;

import dev.behindthescenery.core.BtsCore;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ANLib {
    private final Linker linker;
    private final SymbolLookup lookup;

    public ANLib(String library) {
        loadLibFromResources(library + "-amd64");

        this.linker = Linker.nativeLinker();
        this.lookup = SymbolLookup.loaderLookup();
    }

    public MethodHandle getFunction(@NotNull String name, @NotNull FunctionDescriptor desc) {
        MethodHandle handle = linker.downcallHandle(lookup.find(name).orElseThrow(), desc);
        if (handle == null) throw new NoSuchMethodError("Couldn't downcall handle " + name);

        return handle;
    }

    private static void loadLibFromResources(String name) {
        try {
            String libName = System.mapLibraryName(name);
            InputStream in = BtsCore.class.getResourceAsStream("/native/" + libName);
            if (in == null) throw new IOException();

            File tempFile = Files.createTempFile("lib", libName).toFile();
            Files.copy(in, tempFile.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();
            System.load(tempFile.getAbsolutePath());
            tempFile.deleteOnExit();
            BtsCore.LOGGER.info("Library {} loaded!", name);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load library", e);
        }
    }
}
