package su.nezushin.recipes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import su.nezushin.recipes.craft.choice.CraftRecipeChoice;
import su.nezushin.recipes.craft.impl.BrewingStandCraft;
import su.nezushin.recipes.craft.impl.FurnaceCraft;
import su.nezushin.recipes.craft.impl.ShapedCraft;
import su.nezushin.recipes.craft.impl.SmokingCraft;
import su.nezushin.recipes.listener.RecipeHelperListener;

public class RecipeHelper extends JavaPlugin implements Listener {

    private final List<FurnaceCraft> recipesFurnace = new CopyOnWriteArrayList<>();
    private final List<SmokingCraft> recipesSmoking = new CopyOnWriteArrayList<>();
    private final List<ShapedCraft> recipesShaped = new CopyOnWriteArrayList<>();
    private final List<BrewingStandCraft> recipesBrewingStand = new CopyOnWriteArrayList<>();

    private static RecipeHelper instance;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(CraftRecipeChoice.class);
        ConfigurationSerialization.registerClass(FurnaceCraft.class);
        ConfigurationSerialization.registerClass(SmokingCraft.class);
        ConfigurationSerialization.registerClass(ShapedCraft.class);
        ConfigurationSerialization.registerClass(BrewingStandCraft.class);
        ConfigurationSerialization.registerClass(RecipeContainer.class);
        Bukkit.getPluginManager().registerEvents(new RecipeHelperListener(), getInstance());
    }

    @Override
    public void onDisable() {
        Bukkit.resetRecipes();
    }

    public NamespacedKey createNamespacedKey(String key) {
        return new NamespacedKey(this.getPluginMeta().getName().toLowerCase(), key);
    }

    public boolean isPluginKey(NamespacedKey key) {
        return key != null && key.getNamespace().equals(this.getPluginMeta().getName().toLowerCase());
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    public static RecipeHelper getInstance() {
        return instance;
    }

    public List<FurnaceCraft> getRecipesFurnace() {
        return recipesFurnace;
    }

    public List<ShapedCraft> getRecipesShaped() {
        return recipesShaped;
    }

    public List<SmokingCraft> getRecipesSmoking() {
        return recipesSmoking;
    }

    public List<BrewingStandCraft> getRecipesBrewingStand() {
        return recipesBrewingStand;
    }
}
