package su.nezushin.recipes.craft.impl;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import org.jetbrains.annotations.NotNull;
import su.nezushin.recipes.RecipeHelper;
import su.nezushin.recipes.craft.AbstractCraft;
import su.nezushin.recipes.craft.choice.CraftRecipeChoice;

public class FurnaceCraft extends AbstractCraft implements ConfigurationSerializable {

    private FurnaceRecipe recipe;

    private CraftRecipeChoice source;
    private int cookingTime;
    private float exp;

    public FurnaceCraft(ItemStack result, CraftRecipeChoice source, int cookingTime, float exp) {
        super(result);
        this.cookingTime = cookingTime;
        this.source = source;
        this.exp = exp;
    }

    @Override
    public void register() {
        checkGlobal();
        recipe = new FurnaceRecipe(RecipeHelper.getInstance()
                .createNamespacedKey(String.valueOf(source.hashCode() + result.hashCode())),
                result,
                source.getRecipeChoice(),
                exp, cookingTime);

        Bukkit.addRecipe(recipe);

        RecipeHelper.getInstance().getRecipesFurnace().add(this);
    }

    @Override
    public boolean recipe(Recipe r) {
        if (r == null || recipe == null)
            return false;

        if (!(r instanceof FurnaceRecipe rr))
            return false;

        return rr.getKey().getKey().equalsIgnoreCase(recipe.getKey().getKey());
    }

    @Override
    public void unregister() {
        checkGlobal();
        final Iterator<Recipe> recipeIterator = RecipeHelper.getInstance().getServer().recipeIterator();
        while (recipeIterator.hasNext()) {
            final Recipe recipe = recipeIterator.next();
            if (recipe != null && recipe != null && recipe instanceof FurnaceRecipe fr) {
                if (!recipe(fr)) {
                    continue;
                }
                recipeIterator.remove();
            }
        }
        RecipeHelper.getInstance().getRecipesFurnace().remove(this);
    }

    @Override
    public String toString() {
        return "FurnaceCraft [recipe=" + recipe + ", item=" + source + ", cookingTime=" + cookingTime + ", exp=" + exp
                + ", result=" + result + "]";
    }

    public CraftRecipeChoice getSource() {
        return source;
    }

    public void setSource(CraftRecipeChoice source) {
        this.source = source;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public FurnaceRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(FurnaceRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("result", result);
        map.put("source", source);
        map.put("cookingTime", cookingTime);
        map.put("exp", exp);
        return map;
    }

    public static FurnaceCraft deserialize(Map<String, Object> map) {
        return new FurnaceCraft((ItemStack) map.get("result"), (CraftRecipeChoice) map.get("source"),
                (Integer) map.get("cookingTime"), ((Double) map.get("exp")).floatValue());
    }
}
