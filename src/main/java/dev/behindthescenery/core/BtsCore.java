package dev.behindthescenery.core;

import dev.behindthescenery.core.multiengine.BtsEngineExecutor;
import dev.behindthescenery.core.system.profiler.remotery.RemoteryProfiler;
import dev.behindthescenery.core.system.rendering.RenderThreads;
import dev.behindthescenery.multiengine.ModulesBits;
import dev.behindthescenery.multiengine.MultiEngine;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.gen.structure.Structure;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(BtsCore.MOD_ID)
public class BtsCore {
    public static final String MOD_ID = "btscore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean LoadAssimp = false;
    public static boolean LoadProfiler = false;
    public static BtsEngineExecutor BTS_ENGINE_EXECUTOR = new BtsEngineExecutor(LOGGER);


    public BtsCore(IEventBus bus) {
        LOGGER.info("BtsCore is initializing...");

        if (FMLEnvironment.dist.isClient()) {
            Client.initialize();
        }

        MultiEngine.initialize(BTS_ENGINE_EXECUTOR);
    }

    public static Identifier location(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {

        private static final BlockPos position = new BlockPos(20, -40, 0);

        public static void initialize() {

            try {
//                if (LoadAssimp) AssimpResourcesImpl.initialize();
                if (LoadProfiler) RemoteryProfiler.start();
            } catch (Exception e) {
                e.printStackTrace();
            }


            if(!FMLLoader.isProduction()) {
//                 NeoForge.EVENT_BUS.addListener(Client::renderFrame);

            }

//            ANLib rustMath = new ANLib("rust_math");
//            var sin = ANFuncHelper.of(rustMath, "aros_sin", Double.class, Double.class);
//            var cos = ANFuncHelper.of(rustMath, "aros_cos", Double.class, Double.class);
//
//            LOGGER.info("Sin(pi) is {}", sin.invoke(Math.PI).orElse(-993d));
//            LOGGER.info("Cos(pi) is {}", cos.invoke(Math.PI).orElse(-993d));
        }

//         public static float size = 1f;

//         public static void testEvent(ItemTossEvent event) {

//             Material material = new Material(
//                 TextureType.DIFFUSE, location("textures/tank/t_west_tank_m1a1abrams_d_desert.png")
// //                TextureType.NORMAL, location("textures/tank/t_west_tank_m1a1abrams_n.png"),
// //                TextureType.ORM, location("textures/tank/t_west_tank_m1a1abrams_arm.png")
//             );
//             material.setMaterialName("MI_West_Tank_M1A1Abrams");

//             Model model = AssimpResources.setTexturesForModel(location("assimp/tank_optimized.fbx"), material);

//             WorldModel worldModel = new WorldModel(model,
//                     BtsRenderSystem.minecraftVectorToJOML(event.getPlayer().getPos())
//             );

//             LevelRenderManager.INSTANCE.addModel(worldModel);

//         }

//         @OnlyIn(Dist.CLIENT)
//         public static void renderFrame(RenderLevelStageEvent event) {
// //             if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
// //                 LevelRenderManager.INSTANCE.draw(event.getPoseStack());
// //             }
//         }
    }
}
