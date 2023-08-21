package net.koeck.nutritionmod.events;

import net.koeck.nutritionmod.NutritionMod;
import net.koeck.nutritionmod.capabilities.PlayerDiet;
import net.koeck.nutritionmod.capabilities.PlayerDietProvider;
import net.koeck.nutritionmod.diet.DietEffect;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.koeck.nutritionmod.effect.ModEffects;
import net.koeck.nutritionmod.item.ModItems;
import net.koeck.nutritionmod.networking.ModMessages;
import net.koeck.nutritionmod.networking.NetworkingUtils;
import net.koeck.nutritionmod.networking.packet.CalorieDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.ExtrasDataSyncS2CPacket;
import net.koeck.nutritionmod.networking.packet.WeightClassDataSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public class ModEvents {

    private static final String  FIRST_JOIN_FLAG = NutritionMod.MOD_ID + ":first_join";

    @Mod.EventBusSubscriber(modid = NutritionMod.MOD_ID)
    public static class ForgeEvents {


        @SubscribeEvent
        public static void OnRightClickItem(PlayerInteractEvent.RightClickItem event) {
            ItemStack itemStack = event.getItemStack();

            if (itemStack.isEdible()) {
                for (ItemStack item : FoodGroupList.getAllAllowedFoods()) {
                    if (item.getDescriptionId().equals(itemStack.getDescriptionId())) {
                        Player player = event.getEntity();
                        if (player.getFoodData().getFoodLevel() == 20) {
                            player.getFoodData().setFoodLevel(19);
                            player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent((diet) -> {
                                diet.setOvereating(true);
                            });
                        }
                        return;
                    }
                }
            }
        }

        @SubscribeEvent
        public static void OnStopEatFood(LivingEntityUseItemEvent.Stop event) {
            LivingEntity entity = event.getEntity();
            if(entity instanceof Player player) {
                player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(diet -> {
                    if (diet.isOvereating()) {
                        player.getFoodData().setFoodLevel(20);
                        diet.setOvereating(false);
                    }
                });
            }
        }

        @SubscribeEvent
        public static void OnStartEatFood(LivingEntityUseItemEvent.Start event) {

            ItemStack itemStack = event.getEntity().getItemInHand(event.getEntity().getUsedItemHand());
            for (ItemStack item : FoodGroupList.getAllAllowedFoods()) {
                    if (event.getEntity() instanceof Player player && (item.getDescriptionId().equals(itemStack.getDescriptionId()) || itemStack.getDescriptionId().contains("item.minecraft.potion"))) {
                        if (player.getFoodData().getFoodLevel() == 20) {
                            player.getFoodData().setFoodLevel(19);
                        }
                        return;
                    }
                }

            if (itemStack.isEdible()) {
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
                if (!player.getPersistentData().contains(FIRST_JOIN_FLAG)) {
                    player.getPersistentData().putBoolean(FIRST_JOIN_FLAG, true);

                    if (!player.isCreative()) {
                        Inventory inventory = player.getInventory();
                        inventory.add(new ItemStack(Items.APPLE, 32));
                        inventory.add(new ItemStack(Items.COOKED_BEEF, 16));
                        inventory.add(new ItemStack(Items.BREAD, 16));
                        inventory.add(new ItemStack(Items.GLASS_BOTTLE, 16));
                        inventory.add(new ItemStack(Items.COOKIE, 8));
                        inventory.add(new ItemStack(Items.MILK_BUCKET));
                        inventory.add(new ItemStack(Items.MILK_BUCKET));
                        inventory.add(new ItemStack(Items.MILK_BUCKET));
                        inventory.add(new ItemStack(Items.MILK_BUCKET));
                        inventory.add(new ItemStack(Items.SHULKER_BOX));
                        inventory.add(new ItemStack(ModItems.PECAN.get(), 64));

                        player.sendSystemMessage(Component.literal("Open Diet Menu by pressing N"));
                    }

                    player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(diet -> {
                        Difficulty difficulty = event.getLevel().getDifficulty();
                        if (difficulty == Difficulty.NORMAL) {
                            diet.changeWeightClass(4);
                        } else if (difficulty == Difficulty.HARD) {
                            diet.changeWeightClass(8);
                            DietEffect.applyWeightClassEffect(diet.getWeightClass(), player);
                        }
                    });
                }

                player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(diet -> {
                    for (FoodGroup foodGroup : FoodGroupList.get()) {
                        NetworkingUtils.sendS2CPackage(foodGroup, diet.getFoodGroup(foodGroup), player);
                    }
                    ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(diet.getConsumedCalories()), player);
                    ModMessages.sendToPlayer(new WeightClassDataSyncS2CPacket(diet.getWeightClass()), player);
                });
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                if (!event.getObject().getCapability(PlayerDietProvider.PLAYER_DIET).isPresent()) {
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
                    playerDiet.addConsumedCalories(cakeCalories / 7);

                    ModMessages.sendToPlayer(new ExtrasDataSyncS2CPacket(playerDiet.getFoodGroup(foodGroup)), (ServerPlayer) player);
                    ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(playerDiet.getConsumedCalories()), (ServerPlayer) player);

                });
            }
        }

        @SubscribeEvent
        public static void OnDayEnd(TickEvent.PlayerTickEvent event) {

            if (event.side.isServer() && event.phase == TickEvent.Phase.END && event.player.getLevel().getDayTime() % 24010 == 0) {
                event.player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(playerDiet -> {
                    int weightClass = playerDiet.addWeightClassFromCalories();
                    DietEffect.applyDietEffects(playerDiet.getFoodGroup(), event.player, weightClass);
                    playerDiet.reset();
                    for (FoodGroup foodGroup : playerDiet.getConsumedFoodGroups().keySet()) {
                        NetworkingUtils.sendS2CPackage(foodGroup, 0, (ServerPlayer) event.player);
                    }
                    ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(0), (ServerPlayer) event.player);
                    ModMessages.sendToPlayer(new WeightClassDataSyncS2CPacket(weightClass), (ServerPlayer) event.player);
                });
            }
        }

        @SubscribeEvent
        public static void onToolTip(ItemTooltipEvent event) {
            for (ItemStack item : FoodGroupList.getAllAllowedFoods()) {
                if (item.getDescriptionId().equals(event.getItemStack().getDescriptionId())) {
                    Double[] info = FoodGroupList.getFoodInfo(item);
                    if (info != null) {
                        event.getToolTip().add(Component.literal(info[0] + "kcal"));
                        event.getToolTip().add(Component.literal(info[1] + (event.getItemStack().getDescriptionId().equals(Items.MILK_BUCKET.getDescriptionId()) || event.getItemStack().getDescriptionId().equals(Items.POTION.getDescriptionId()) ? "ml" : "g")));
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onRespawn(PlayerEvent.Clone event) {
            if(event.getOriginal().level.isClientSide)
                return;

            if(event.isWasDeath()) {
                Player originalPlayer = event.getOriginal();
                Player newPlayer = event.getEntity();
                newPlayer.getPersistentData().putBoolean(FIRST_JOIN_FLAG, originalPlayer.getPersistentData().getBoolean(FIRST_JOIN_FLAG));

                originalPlayer.reviveCaps();
                LazyOptional<PlayerDiet> loNewCap = newPlayer.getCapability(PlayerDietProvider.PLAYER_DIET);
                LazyOptional<PlayerDiet> loOldCap = originalPlayer.getCapability(PlayerDietProvider.PLAYER_DIET);
                loNewCap.ifPresent( newCap -> {
                    loOldCap.ifPresent(newCap::copyDiet);
                });
            }
        }

        @SubscribeEvent
        public static void OnFinishEatFood(LivingEntityUseItemEvent.Finish event) {
            if (!(event.getEntity() instanceof Player player) || event.getEntity().level.isClientSide()) {
                return;
            }

            player.getCapability(PlayerDietProvider.PLAYER_DIET).ifPresent(playerDiet -> {

                playerDiet.setOvereating(false);
                ItemStack item = event.getItem();
                List<FoodGroup> foodGroupList = FoodGroupList.getConsumedFoodGroups(item);

                double calories = FoodGroupList.getFoodCalories(item);

                playerDiet.addFoodGroup(foodGroupList, 1);
                playerDiet.addConsumedCalories(calories);


                for (FoodGroup foodGroup : foodGroupList) {
                    Map<FoodGroup, Integer> consumedFoodGroups = playerDiet.getConsumedFoodGroups();
                    if(foodGroup.name.equals("vegetables and fruit") && consumedFoodGroups.get(foodGroup) == foodGroup.dailyPortionAmt) {
                        player.addEffect(new MobEffectInstance(ModEffects.VEGGIE_AND_FRUIT_RUSH.get(), 6000));
                    }
                    NetworkingUtils.sendS2CPackage(foodGroup, playerDiet.getFoodGroup(foodGroup), (ServerPlayer) player);
                    ModMessages.sendToPlayer(new CalorieDataSyncS2CPacket(playerDiet.getConsumedCalories()), (ServerPlayer) player);
                }
            });
        }
    }
}
