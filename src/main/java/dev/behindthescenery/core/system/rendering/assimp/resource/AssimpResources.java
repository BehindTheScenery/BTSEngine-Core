package dev.behindthescenery.core.system.rendering.assimp.resource;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.assimp.resource.animation.Animation;
import dev.behindthescenery.core.system.rendering.assimp.resource.loader.AnimationLoader;
import dev.behindthescenery.core.system.rendering.assimp.resource.loader.ModelLoader;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Material;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Model;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Чтобы ресурсы загружались их нужно поместить в папку {@code assimp}
 */
public class AssimpResources implements ResourceReloader {
    private static Map<Identifier, Model> MODELS = new Object2ObjectOpenHashMap<>();
    private static Map<Identifier, Animation> ANIMATIONS = new Object2ObjectOpenHashMap<>();

    /**
     * Извлекает объект модели, связанный с заданным расположением ресурса.
     *
     * @param resourceLocation Местоположение ресурса в моде.
     * @return Объект модели, связанный с расположением ресурса.
     * Модель может быть пустым, если она никогда не загружалась.
     */
    public static Model getModel(Identifier resourceLocation) {
        Model model = MODELS.get(resourceLocation);
        if (model == null) MODELS.put(resourceLocation, model = new Model());
        return model;
    }

    /**
     * Извлекает анимационный объект, связанный с заданным местоположением ресурса.
     *
     * @param resourceLocation Местоположение ресурса для анимации.
     * @return Анимационный объект, связанный с расположением ресурса.
     * Анимация может быть пустым, если она никогда не загружалась.
     */
    public static Animation getAnimation(Identifier resourceLocation) {
        Animation animation = ANIMATIONS.get(resourceLocation);
        if (animation == null) ANIMATIONS.put(resourceLocation, animation = new Animation());
        return animation;
    }

    public static Model setTexturesForModel(Identifier resourceLocation, Material material) {
        return setTexturesForModel(getModel(resourceLocation), material);
    }

    public static Model setTexturesForModel(Model model, Material material) {
        model.setMaterial(material);
        return model;
    }

    public static Model setTexturesForModel(Identifier resourceLocation, Material... material) {
        return setTexturesForModel(getModel(resourceLocation), material);
    }

    public static Model setTexturesForModel(Model model, Material... material) {
        for (Material material1 : material) {
            setTexturesForModel(model, material1);
        }
        return model;
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer preparationBarrier, ResourceManager resourceManager, Profiler prepareProfiler,
                                          Profiler applyProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        List<AIScene> scenes = new ObjectArrayList<>();

        Map<Identifier, Model> models = new Object2ObjectOpenHashMap<>();
        Map<Identifier, Animation> animations = new Object2ObjectOpenHashMap<>();

        return CompletableFuture.allOf(loadAssimpScene(backgroundExecutor, resourceManager, scenes::add, models::put, animations::put))
                .thenCompose(preparationBarrier::whenPrepared)
                .thenAcceptAsync(unused -> {
                    MODELS = models;
                    ANIMATIONS = animations;
                    scenes.forEach(Assimp::aiReleaseImport);

                    BtsCore.LOGGER.info("Loaded {} models", MODELS.size());
                    BtsCore.LOGGER.info("Loaded {} animations", ANIMATIONS.size());
                }, gameExecutor);
    }

    private CompletableFuture<Void> loadAssimpScene(Executor executor, ResourceManager resourceManager,
                                                    Consumer<AIScene> addScene,
                                                    BiConsumer<Identifier, Model> putModel,
                                                    BiConsumer<Identifier, Animation> putAnimation
    ) {
        return CompletableFuture.supplyAsync(() -> resourceManager.findResources("assimp", resource -> true), executor)
                .thenApplyAsync(resources -> {
                    Map<Identifier, CompletableFuture<AIScene>> sceneTasks = new HashMap<>();

                    for (Identifier resourceLocation : resources.keySet()) {
                        AIScene scene = loadAssimpScene(resourceManager, resourceLocation);
                        addScene.accept(scene);

                        if (scene != null) sceneTasks.put(resourceLocation, CompletableFuture.supplyAsync(() -> scene, executor));
                    }
                    return sceneTasks;
                }, executor)
                .thenAcceptAsync(resource -> {
                    for (Map.Entry<Identifier, CompletableFuture<AIScene>> entry : resource.entrySet()) {
                        Identifier resourceLocation = entry.getKey();
                        AIScene scene = entry.getValue().join();

                        Model model = ModelLoader.loadScene(AssimpResources.getModel(resourceLocation), scene).setId(resourceLocation);
                        putModel.accept(resourceLocation, model);

                        for (int i = 0; i < scene.mNumAnimations(); i++) {
                            AIAnimation aiAnimation = AIAnimation.create(scene.mAnimations().get(i));
                            Identifier animationLocation = resourceLocation;
                            if (scene.mNumAnimations() > 1) {
                                animationLocation = animationLocation.withSuffixedPath("/" + aiAnimation.mName().dataString());
                            }

                            Animation animation = AnimationLoader.loadAnimation(AssimpResources.getAnimation(animationLocation), aiAnimation);
                            putAnimation.accept(animationLocation, animation);
                        }
                    }
                }, executor);
    }

    private AIScene loadAssimpScene(ResourceManager resourceManager, Identifier resourceLocation) {
        try (InputStream inputStream = resourceManager.getResourceOrThrow(resourceLocation).getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return Assimp.aiImportFileFromMemory(buffer,
                    Assimp.aiProcess_Triangulate | Assimp.aiProcess_PopulateArmatureData | Assimp.aiProcess_LimitBoneWeights, "");
        }
        catch (Exception e) {
            throw new RuntimeException(new FileNotFoundException(resourceLocation.toString()));
        }
    }
}

