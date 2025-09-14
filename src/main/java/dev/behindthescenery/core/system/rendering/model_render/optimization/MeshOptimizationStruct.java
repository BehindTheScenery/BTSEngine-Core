package dev.behindthescenery.core.system.rendering.model_render.optimization;

public class MeshOptimizationStruct {

    public boolean optimizeVertexCache = true;
    public boolean optimizeVertexCacheFifo = false;
    public int fifoCache = 16;
    public boolean optimizeOverdraw = false;
    public float optimizeOverdrawThreshold = 1.00f;
    public boolean optimizeVertexFetch = true;

    public MeshOptimizationStruct() {

    }

    public MeshOptimizationStruct optimizeVertexCache(boolean optimizeVertexCache, boolean useFifo) {
        return optimizeVertexCache(optimizeVertexCache, useFifo, 16);
    }

    public MeshOptimizationStruct optimizeVertexCache(boolean optimizeVertexCache, boolean useFifo, int fifoCache) {
        this.optimizeVertexCache = optimizeVertexCache;
        this.optimizeVertexCacheFifo = useFifo;
        this.fifoCache = fifoCache;
        return this;
    }

    public MeshOptimizationStruct optimizeOverdraw(boolean value) {
        return optimizeOverdraw(value, 1.00f);
    }

    public MeshOptimizationStruct optimizeOverdraw(boolean value, float threshold) {
        this.optimizeOverdraw = value;
        this.optimizeOverdrawThreshold = threshold;
        return this;
    }

    public MeshOptimizationStruct optimizeVertexFetch(boolean value) {
        this.optimizeVertexFetch = value;
        return this;
    }
}
