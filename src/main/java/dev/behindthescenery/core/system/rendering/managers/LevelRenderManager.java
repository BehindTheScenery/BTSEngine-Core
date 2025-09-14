package dev.behindthescenery.core.system.rendering.managers;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.WorldModel;
import dev.behindthescenery.core.system.rendering.assimp.shaders.AssimplShaders;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LevelRenderManager {

    public static LevelRenderManager INSTANCE = new LevelRenderManager();

    protected CopyOnWriteArrayList<WorldModel> models = new CopyOnWriteArrayList<>();
    protected List<Listener> listeners = new ArrayList<>();

    public void addModel(WorldModel model) {
        models.add(model);
        for (Listener listener : listeners) {
            listener.onModelsChange();
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void clear() {
        models.clear();
    }

    public int modelCounts() {
        return models.size();
    }

    public List<WorldModel> getModels() {
        return models;
    }

    public List<VoxelShape> getCollisions(Box box) {
        if(box == null) {
            return models.stream().map(WorldModel::getCollision).toList();
        }

        return models.stream().map(WorldModel::getCollision)
                .filter(shape -> !shape.isEmpty() && shape.getBoundingBox().intersects(box))
                .toList();
    }

    public void draw(MatrixStack matrixStack) {
        BtsRenderSystem.setGlobalShader(AssimplShaders.getColorShader());
        for (WorldModel model : models) {
            model.draw(matrixStack, 0);
        }
    }

    public static interface Listener {

        void onModelsChange();
    }
}
