package su.nezushin.recipes.craft.impl;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import su.nezushin.recipes.craft.AbstractCraft;
import su.nezushin.recipes.craft.choice.CraftRecipeChoice;
import org.bukkit.Bukkit;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;


import su.nezushin.recipes.RecipeHelper;

public class ShapedCraft extends AbstractCraft implements ConfigurationSerializable {


    private ShapedRecipe recipe;
    private int sizeX;
    private int sizeY;

    private CraftRecipeChoice[] matrix;

    public ShapedCraft(ItemStack result, CraftRecipeChoice[] matrix, int sizeX, int sizeY) {
        super(result);
        this.matrix = matrix;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

    }


    @Override
    public void register() {
        checkGlobal();
        recipe = new ShapedRecipe(RecipeHelper.getInstance().createNamespacedKey(
                String.valueOf(result.hashCode() * matrix.hashCode())), result);

        String[] strings = new String[sizeY];

        Map<CraftRecipeChoice, Character> map = new HashMap<>();

        var j = 0;
        for (var y = 0; y < sizeY; y++) {
            String s = "";
            for (var x = 0; x < sizeX; x++) {
                var item = matrix[x + y*sizeX];
                if (item == null || item.getItem() == null) {
                    s += ' ';
                } else {
                    if (!map.containsKey(item)) {
                        char a = getChar(j++);

                        map.put(item, a);
                        s += a;
                    } else
                        s += map.get(item);
                }
            }
            strings[y] = s;
        }
        recipe.shape(strings);


        for (Entry<CraftRecipeChoice, Character> a : map.entrySet()) {
            //recipe.setIngredient(a.getValue(),  a.getKey());
            recipe.setIngredient(a.getValue(), a.getKey().getRecipeChoice());
        }

        Bukkit.addRecipe(recipe);
        RecipeHelper.getInstance().getRecipesShaped().add(this);
    }

    private char getChar(int i) {
        return "qwertyuiopasdfghjklzxcvbnm".charAt(i);
    }

    @Override
    public boolean recipe(Recipe r) {
        if (r == null)
            return false;
        if (!(r instanceof ShapedRecipe))
            return false;
        if (recipe == null)
            return false;
        return recipe.getKey().getKey().equalsIgnoreCase(((ShapedRecipe) r).getKey().getKey());
    }

    public boolean matrix(CraftingInventory inv) {
        for (var x = 0; x <= (3 - sizeX); x++) {
            for (var y = 0; y <= (3 - sizeY); y++) {
                if (matrix(inv, x, y))
                    return true;
            }
        }


        return false;
    }

    private boolean matrix(CraftingInventory inv, int xOffset, int yOffset) {
        for (var x = 0; x < sizeX; x++)
            for (var y = 0; y < sizeY; y++) {
                var i = x + y * sizeX;
                var item = inv.getItem(((x + xOffset) + ((y + yOffset) * 3)) + 1);
                if (item == null && (matrix[i] == null || matrix[i].getItem() == null)) {
                    continue;
                }
                if (item == null || (matrix[i] == null || matrix[i].getItem() == null))
                    return false;

                if (matrix[i].isSimilar(item)) continue;
                return false;
            }
        return true;
    }

    public ItemStack[] getMatrixAsItems() {
        var items = new ItemStack[matrix.length];
        for (var i = 0; i < matrix.length; i++) {
            items[i] = matrix[i].getItem();
        }
        return items;
    }

    public void setMatrixAsItems(ItemStack[] items) {
        for (var i = 0; i < items.length; i++) {
            matrix[i].setItem(items[i]);
        }
    }

    @Override
    public void unregister() {
        checkGlobal();
        final Iterator<Recipe> recipeIterator = RecipeHelper.getInstance().getServer().recipeIterator();
        while (recipeIterator.hasNext()) {
            final Recipe recipe = recipeIterator.next();
            if (recipe != null && recipe instanceof ShapedRecipe) {
                final ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                if (!recipe(shapedRecipe)) {
                    continue;
                }
                recipeIterator.remove();
            }
        }
        RecipeHelper.getInstance().getRecipesShaped().remove(this);
    }

    @Override
    public String toString() {
        return "ShapedCraft [recipe=" + recipe + ", matrix=" + Arrays.toString(matrix) + ", result=" + result + "]";
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(ShapedRecipe recipe) {
        this.recipe = recipe;
    }

    public CraftRecipeChoice[] getMatrix() {
        return matrix;
    }

    public void setMatrix(CraftRecipeChoice[] matrix) {
        this.matrix = matrix;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("result", result);
        map.put("matrix", List.of(matrix));
        map.put("sizeX", sizeX);
        map.put("sizeY", sizeY);
        return map;
    }

    public static ShapedCraft deserialize(Map<String, Object> map) {
        return new ShapedCraft((ItemStack) map.get("result"), (CraftRecipeChoice[]) new ArrayList<CraftRecipeChoice>((Collection<? extends CraftRecipeChoice>) map.get("matrix")).toArray(new CraftRecipeChoice[0]),
                (Integer) map.get("sizeX"), (Integer) map.get("sizeY"));
    }
}
