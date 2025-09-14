package dev.behindthescenery.core.system.rendering.assimp.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.TextureType;
import dev.behindthescenery.core.system.rendering.assimp.resource.debug.DebugModelRender;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.*;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Material;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Model;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;
import dev.behindthescenery.core.system.rendering.assimp.shaders.AssimplShaders;
import dev.behindthescenery.core.system.rendering.model_render.buffer.BufferRenderType;
import dev.behindthescenery.core.system.rendering.model_render.model_data.Mesh;
import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;
import dev.behindthescenery.core.system.rendering.vertex.BtsVertexConsumer;
import dev.behindthescenery.core.utils.WorldRenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LEQUAL;

@OnlyIn(Dist.CLIENT)
public class BTSAssimpModelRenderer {

    @Deprecated
    public static void renderWorldModel(Model model, MatrixStack poseStack, int lightmapUV) {
        GlStateManager._enableDepthTest();
        GlStateManager._depthFunc(GL_LEQUAL);
        GlStateManager._depthMask(true);
        renderNode(model, model.getRootNode(), poseStack, lightmapUV, model.getMeshes());
        GlStateManager._disableDepthTest();
    }

    @Deprecated
    public static void renderNode(Model model, Node node, MatrixStack poseStack, int lightmapUV,
                                  List<Mesh> meshes) {
        if (node.isVisible()) {
            poseStack.push();
            poseStack.multiplyPositionMatrix(node.getGlobalTransform());

            for (Integer meshIndex : node.getMeshIndexes()) {
                Mesh mesh = meshes.get(meshIndex);
                renderMesh(model, null, mesh, node, poseStack, lightmapUV);
            }
            poseStack.pop();
        }
        node.getChildren().forEach(child -> renderNode(model, child, poseStack, lightmapUV, meshes));
    }

    public static void renderWorldModel(WorldModel worldModel, MatrixStack poseStack, int lightmapUV, int renderFlags) {

        final boolean useOcclusion = (renderFlags & WorldRenderParams.USE_OCCLUSION) != 0;

        poseStack.push();

        final Model model = worldModel.getModel();
        final Vector3f scale = worldModel.getScale();
        final Vector3f position = worldModel.getPosition();
        final Vector4f rotation = worldModel.getRotation();
        final WorldRenderParams renderParams = worldModel.getRenderParams();


        BtsRenderSystem.applyCameraOffset(poseStack, position);

        poseStack.multiply(new Quaternionf()
                .rotateXYZ(
                        (float) Math.toRadians(rotation.x),
                        (float) Math.toRadians(rotation.y),
                        (float) Math.toRadians(rotation.z)
                ));

        poseStack.scale(scale.x, scale.y, scale.z);

        if(useOcclusion) {
            BtsRenderSystem.setGlobalShader(AssimplShaders.getEmptyShader());
            GL11.glColorMask(false, false, false, false);
            GL11.glDepthMask(false);

            int queryId = worldModel.getOcclusionQueryId();
            GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, queryId);
            worldModel.getBoundBoxBuffer().draw(poseStack);
            GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);

            GL11.glColorMask(true, true, true, true);
            GL11.glDepthMask(true);
            BtsRenderSystem.clearGlobalShader();

            worldModel.updateOcclusionResult();
            if (worldModel.getLastSamplesPassed() == 0) {
                poseStack.pop();
                return;
            }
        }

        final boolean useDepthTest = renderParams.useDepthTest();

        if(useDepthTest) {
            GlStateManager._enableDepthTest();
            GlStateManager._depthFunc(renderParams.getDepthFunctions());
            GlStateManager._depthMask(true);
        }


        BtsRenderSystem.setGlobalShader(AssimplShaders.getColorShader());

        renderWorldModelNode(poseStack, worldModel, model, model.getRootNode(), lightmapUV, model.getMeshes(), useOcclusion);

        BtsRenderSystem.clearGlobalShader();

        if(useDepthTest) {
            GlStateManager._disableDepthTest();
        }

        poseStack.pop();
    }

    private static void renderWorldModelNode(MatrixStack poseStack, WorldModel worldModel, Model model, Node node, int lightmapUV,
                                             List<Mesh> meshes, boolean useOcclusion) {

        if(!node.isVisible()) {
            return;
        }

        poseStack.push();
        poseStack.multiplyPositionMatrix(node.getGlobalTransform());

        for (Integer meshIndex : node.getMeshIndexes()) {
            Mesh mesh = meshes.get(meshIndex);
            if (!DebugModelRender.disableRender) {
                renderMesh(model, worldModel, mesh, node, poseStack, lightmapUV);
            }
        }

        poseStack.pop();
        node.getChildren().forEach(child -> renderWorldModelNode(poseStack, worldModel, model, child, lightmapUV, meshes, useOcclusion));
    }

    public static void renderMesh(Model model, WorldModel worldModel, Mesh mesh, Node meshNode, MatrixStack poseStack, int lightmapUV) {

        poseStack.push();

        if(mesh.getMeshBuffer().getRenderType() == BufferRenderType.BTS) {

            ShaderProgram shader = BtsRenderSystem.getGlobalShader();

            Material material = mesh.material();

            if (shader != null) {
                material.putToShader(shader);
                var sunDirection = WorldRenderUtils.skyAngleToDirection();
                var lightColor = WorldRenderUtils.getSkyColor();

                shader.setUniformValue("lightDir", sunDirection);
                shader.setUniformValue("lightColor", lightColor);

//            if (mesh.getBoneIndices() != null && mesh.getBoneWeights() != null) {
//                Matrix4f[] boneTransforms = new Matrix4f[64];
//                for (int i = 0; i < model.getNodes().size() && i < 64; i++) {
//                    Node boneNode = model.getNodes().get(i);
//                    Matrix4f boneTransform = boneNode.getGlobalTransform();
//                    Matrix4f inverseInitial = new Matrix4f(boneNode.getInitialGlobalTransform()).invert();
//                    boneTransforms[i] = boneTransform.mul(inverseInitial, new Matrix4f());
//                }
//
//                shader.setUniformValue("boneTransforms", boneTransforms);
//            }

            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            mesh.getMeshBuffer().setupRenderWorldModel(poseStack, worldModel, null);
            mesh.getMeshBuffer().draw(poseStack);
            RenderSystem.disableBlend();
        } else {
            BufferBuilderStorage storage = MinecraftClient.getInstance().getBufferBuilders();
            var bufferSource = storage.getEffectVertexConsumers();
            mesh.getMeshBuffer().setupRenderWorldModel(poseStack, worldModel, bufferSource.getBuffer(BTSAssimpRenderType.getCutoutMeshEntity(mesh.material().getTexture(TextureType.DIFFUSE).getTextureID())));
            mesh.getMeshBuffer().draw(poseStack);
        }

        poseStack.pop();
    }

//    public static void renderInstancedMesh(Model model, Mesh mesh, MatrixStack poseStack, int instanceCount) {
//
//        poseStack.push();
//
//        ShaderProgram shader = BtsRenderSystem.getGlobalShader();
//        Material material = mesh.material();
//
//        if (shader != null) {
//            material.putToShader(shader);
//            var sunDirection = WorldRenderUtils.skyAngleToDirection();
//            var lightColor = WorldRenderUtils.getSkyColor();
//
//            shader.setUniformValue("lightDir", sunDirection);
//            shader.setUniformValue("lightColor", lightColor);
//
//            if (mesh.getBoneIndices() != null && mesh.getBoneWeights() != null) {
//                Matrix4f[] boneTransforms = new Matrix4f[64];
//                for (int i = 0; i < model.getNodes().size() && i < 64; i++) {
//                    Node node = model.getNodes().get(i);
//                    Matrix4f boneTransform = node.getGlobalTransform();
//                    Matrix4f inverseInitial = new Matrix4f(node.getInitialGlobalTransform()).invert();
//                    boneTransforms[i] = boneTransform.mul(inverseInitial, new Matrix4f());
//                }
//
//                shader.setUniformValue("boneTransforms", boneTransforms);
//            }
//
//        }
//
//        mesh.getMeshBuffer().draw(poseStack);
//
//        poseStack.pop();
//    }
}
