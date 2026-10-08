package su.nezushin.recipes.craft.impl;

import io.papermc.paper.potion.PotionMix;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import su.nezushin.recipes.RecipeHelper;
import su.nezushin.recipes.craft.AbstractCraft;
import su.nezushin.recipes.craft.choice.CraftRecipeChoice;

import java.util.*;
import java.util.Map.Entry;

public class BrewingStandCraft extends AbstractCraft implements ConfigurationSerializable {


    private PotionMix potionMix;
    private CraftRecipeChoice source;
    private CraftRecipeChoice potion;

    public BrewingStandCraft(ItemStack result, CraftRecipeChoice source, CraftRecipeChoice potion) {
        super(result);
        this.source = source;
        this.potion = potion;
    }


    @Override
    public void register() {
        potionMix = new PotionMix(getKey(), result, potion.getRecipeChoice(), source.getRecipeChoice());
        Bukkit.getPotionBrewer().addPotionMix(potionMix);
        RecipeHelper.getInstance().getRecipesBrewingStand().add(this);
    }

    @Override
    public boolean recipe(Recipe r) {
        return false;
    }

    @Override
    public void unregister() {
        Bukkit.getPotionBrewer().removePotionMix(getKey());
        RecipeHelper.getInstance().getRecipesBrewingStand().remove(this);
    }

    private NamespacedKey getKey() {
        return RecipeHelper.getInstance().createNamespacedKey(
                String.valueOf(result.hashCode() * source.hashCode() * source.hashCode()));
    }

    public CraftRecipeChoice getSource() {
        return source;
    }

    public void setSource(CraftRecipeChoice source) {
        this.source = source;
    }

    public CraftRecipeChoice getPotion() {
        return potion;
    }

    public void setPotion(CraftRecipeChoice potion) {
        this.potion = potion;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("result", result);
        map.put("source", source);
        map.put("potion", potion);
        return map;
    }

    public static BrewingStandCraft deserialize(Map<String, Object> map) {
        return new BrewingStandCraft((ItemStack) map.get("result"), (CraftRecipeChoice) map.get("source"), (CraftRecipeChoice) map.get("potion"));
    }
}
