package net.koeck.nutritionmod.events;

import net.koeck.nutritionmod.NutritionMod;
import net.koeck.nutritionmod.capabilities.PlayerDiet;
import net.koeck.nutritionmod.capabilities.PlayerDietProvider;
import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.DietEffect;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.koeck.nutritionmod.networking.ModMessages;
import net.koeck.nutritionmod.networking.NetworkingUtils;
import net.koeck.nutritionmod.networking.packet.CalorieDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.DairyDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.DrinksDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.ExtrasDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.FatOilDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.GrainDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.ProteinDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.VegetablesFruitDataSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = NutritionMod.MOD_ID)
    public static class ClientForgeEvents {


        @SubscribeEvent
        public static void OnRightClickItem(PlayerInteractEvent.RightClickItem event) {

            if(!event.getItemStack().isEdible()) {
                return;
            }

            for (ItemStack item :FoodGroupList.getAllAllowedFoods()) {
                if (item.getDescriptionId().equals(event.getItemStack().getDescriptionId())){
                    return;
                }
            }

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setResult(Event.Result.DENY);
            event.setCanceled(true);
        }



        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(diet -> {
                        for (FoodGroup foodGroup : FoodGroupList.get()) {
                            NetworkingUtils.sendS2CPackage(foodGroup, diet.getFoodGroup(foodGroup), (ServerPlayer) event.getEntity());
                        }
                        ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(diet.getConsumedCalories()), (ServerPlayer) event.getEntity());
                    });
                }
            }
        }


        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player) {
                if(!event.getObject().getCapability(PlayerDietProvider.PLAYER_DIET).isPresent()) {
                    event.addCapability(new ResourceLocation(NutritionMod.MOD_ID, "properties"), new PlayerDietProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerDiet.class);
        }


        @SubscribeEvent
        public static void OnEatCake(PlayerInteractEvent event) {
            if (event.getEntity().level.isClientSide()) {
                return;
            }

            BlockPos blockPos = event.getPos();
            Player player = event.getEntity();

            if (event instanceof PlayerInteractEvent.RightClickBlock
                && event.getLevel().getBlockState(blockPos).getBlock().equals(Blocks.CAKE)
                && player.canEat(false)) {

                player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(playerDiet -> {
                    FoodGroup foodGroup = FoodGroupList.getByName("extras");
                    ItemStack cakeItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:cake")), 1);
                    double cakeCalories = FoodGroupList.getFoodCalories(cakeItem);

                    playerDiet.addFoodGroup(foodGroup, 1);
                    playerDiet.addConsumedCalories(cakeCalories);
                    System.out.println(playerDiet);

                    ModMessages.sendToPlayer(new ExtrasDataSyncS2CPacket(playerDiet.getFoodGroup(foodGroup)), (ServerPlayer) player);
                    ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(playerDiet.getConsumedCalories()), (ServerPlayer) player);

                });
            }
        }

        @SubscribeEvent
        public static void OnDayEnd(TickEvent.PlayerTickEvent event) {
            if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
                int gameTime = (int) event.player.getLevel().getDayTime();
                if(gameTime % 24000 == 0) {
                    event.player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(playerDiet -> {
                        double bmi = playerDiet.addBodyWeightFromCalories();
                        DietEffect.applyDietEffects(playerDiet.getFoodGroup(), event.player, bmi);
                        playerDiet.reset();
                        for (FoodGroup foodGroup: playerDiet.getConsumedFoodGroups().keySet()) {
                            NetworkingUtils.sendS2CPackage(foodGroup, 0, (ServerPlayer) event.player);
                        }
                        ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(0), (ServerPlayer) event.player);
                    });
                }
            }
        }

        //TODO: milk shouldnt remove effects
        @SubscribeEvent
        public static void OnEndEatFood(LivingEntityUseItemEvent.Finish event) {
            if (!(event.getEntity() instanceof Player) || event.getEntity().level.isClientSide()) {
                return;
            }



            Player player = (Player) event.getEntity();

            player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(playerDiet -> {

                ItemStack item = event.getItem();
                List<FoodGroup> foodGroupList = FoodGroupList.getConsumedFoodGroups(item);
                double calories = FoodGroupList.getFoodCalories(item);
                playerDiet.addFoodGroup(foodGroupList, 1);
                playerDiet.addConsumedCalories(calories);

                for (FoodGroup foodGroup : foodGroupList) {
                    NetworkingUtils.sendS2CPackage(foodGroup, playerDiet.getFoodGroup(foodGroup), (ServerPlayer) player);
                    ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(playerDiet.getConsumedCalories()), (ServerPlayer) player);
                }
            });
        }


    }
}
