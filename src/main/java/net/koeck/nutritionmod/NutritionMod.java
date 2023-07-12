package net.koeck.nutritionmod;

import com.mojang.logging.LogUtils;
import net.koeck.nutritionmod.networking.ModMessages;
import net.koeck.nutritionmod.utility.Config;
import net.koeck.nutritionmod.utility.DataImporter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import org.slf4j.Logger;

import java.io.File;

@Mod(NutritionMod.MOD_ID)
public class NutritionMod {
    public static final String MOD_ID = "nutritionmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NutritionMod() {

        Config.registerConfigs(new File(FMLConfig.defaultConfigPath()));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        ModMessages.register();

        DataImporter.reload();
    }

    @SubscribeEvent
    public void preInit(ModConfigEvent.Loading event) {


    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
