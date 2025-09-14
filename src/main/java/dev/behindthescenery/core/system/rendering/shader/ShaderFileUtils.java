package dev.behindthescenery.core.system.rendering.shader;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShaderFileUtils {

    public static String readShaderSource(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || !Files.isReadable(path)) {
            throw new RuntimeException("File not found!: " + filePath);
        }
        try {
            return IOUtils.toString(new FileInputStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error read Shader: " + filePath, e);
        }
    }

}
