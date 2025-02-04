package net.brnbrd.delightful.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.brnbrd.delightful.Delightful;
import net.brnbrd.delightful.Util;
import net.brnbrd.delightful.common.item.DelightfulItems;
import net.brnbrd.delightful.common.item.IConfigured;
import net.brnbrd.delightful.common.item.food.GreenTeaLeavesItem;
import net.brnbrd.delightful.common.item.knife.DelightfulKnifeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.utility.TextUtils;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
    private static final ResourceLocation ID = Util.rl(Delightful.MODID, "jei_plugin");

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Hide all disabled items from JEI
        List<ItemStack> hidden = new ArrayList<>(DelightfulItems.ITEMS.getEntries().stream()
            .filter(i -> (i.get() instanceof IConfigured c) ? !c.enabled() : !Util.enabled(i)) // Keep items not enabled
            .map(Util::gs).toList());

        // Delightful conflicts
        this.hide(hidden, Mods.VD, "pb_j");
        this.hide(hidden, Mods.UGD, "gloomgourd_pie_slice");

        // FD conflicts
        this.hide(hidden, Mods.IN, "fried_egg");
        this.hide(hidden, Mods.NA, "cooked_egg");
        this.hide(hidden, Mods.AA, "fried_egg");

        // Other
        this.hide(hidden, Mods.AA, "honeyed_apple", Mods.BB);
        this.hide(hidden, Mods.MOD, "bread_slice", Mods.SAS);
        this.hide(hidden, Mods.MOD, "toast", Mods.SAS);
        this.hide(hidden, "cratedelight", "berry_crate", "berry_good");
        this.hide(hidden, "cratedelight", "glowberry_crate", "berry_good");
        this.hide(hidden, "cratedelight", "cod_crate", "crabbersdelight");
        this.hide(hidden, "cratedelight", "salmon_crate", "crabbersdelight");
        this.hide(hidden, "cratedelight", "egg_crate", "incubation");
        this.hide(hidden, "cratedelight", "sugar_bag", "supplementaries");
        this.hide(hidden, "cratedelight", "apple_crate", "fruitsdelight");

        if (hidden.size() > 0) {
            registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden);
        }

        // Add Knife translations
        DelightfulItems.ITEMS.getEntries().stream()
            .map(RegistryObject::get)
            .filter(k -> k instanceof DelightfulKnifeItem dk && dk.enabled())
            .map(ItemStack::new)
            .forEach(i -> registration.addIngredientInfo(i, VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.knife")));

        // Add other descriptions
        if (Util.enabled(DelightfulItems.SALMONBERRIES)) {
            registration.addIngredientInfo(
                List.of(Util.gs(DelightfulItems.SALMONBERRIES), Util.gs(DelightfulItems.WILD_SALMONBERRIES)),
                VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".salmonberries.desc")
            );
        }
        if (((GreenTeaLeavesItem)DelightfulItems.GREEN_TEA_LEAF.get()).enabled()) {
            registration.addIngredientInfo(DelightfulItems.GREEN_TEA_LEAF.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".green_tea_leaf.desc"));
        }
        if (Util.enabled(DelightfulItems.ACORN)) {
            registration.addIngredientInfo(Util.gs(DelightfulItems.ACORN), VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".acorn.desc"));
        }
        if (Util.enabled(DelightfulItems.MINI_MELON)) {
            registration.addIngredientInfo(
                List.of(Items.MELON_SLICE.getDefaultInstance(), Util.gs(DelightfulItems.MINI_MELON)),
                VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".mini_melon.desc"));
        }
        if (Util.enabled(DelightfulItems.CANTALOUPE)) {
            registration.addIngredientInfo(
                List.of(Util.gs(DelightfulItems.CANTALOUPE_SLICE), Util.gs(DelightfulItems.CANTALOUPE)),
                VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".cantaloupe.desc").append(" ").append(Component.translatable(Delightful.MODID + ".sliceable.desc")));
        }
        if (Util.enabled(DelightfulItems.CANTALOUPE_SEEDS)) {
            registration.addIngredientInfo(
                Util.gs(DelightfulItems.CANTALOUPE_SEEDS),
                VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".cantaloupe_seeds.desc"));
        }
        if (Util.enabled(DelightfulItems.ANIMAL_FAT)) {
            registration.addIngredientInfo(Util.gs(DelightfulItems.ANIMAL_FAT), VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".animal_fat.desc"));
        }
        if (Util.enabled(DelightfulItems.ANIMAL_OIL_BOTTLE)) {
            registration.addIngredientInfo(Util.gs(DelightfulItems.ANIMAL_OIL_BOTTLE), VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".animal_oil_bottle.desc"));
        }
        registration.addIngredientInfo(Items.MELON.getDefaultInstance(), VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".sliceable.desc"));
        registration.addIngredientInfo(Items.PUMPKIN.getDefaultInstance(), VanillaTypes.ITEM_STACK, Component.translatable(Delightful.MODID + ".sliceable.desc"));
    }

    private void hide(List<ItemStack> hiddenList, String modid, String item, String... conflicts) {
        if (Mods.loaded(modid) && (conflicts.length < 1 || Mods.orLoaded(conflicts))) {
            Item found = Util.item(modid, item);
            if (found != null) {
                hiddenList.add(new ItemStack(found));
            }
        }
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }
}