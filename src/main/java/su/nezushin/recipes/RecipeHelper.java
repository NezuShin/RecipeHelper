package su.nezushin.recipes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import su.nezushin.recipes.cmd.RecipeHelperCommand;
import su.nezushin.recipes.craft.AbstractCraft;
import su.nezushin.recipes.craft.choice.CraftRecipeChoice;
import su.nezushin.recipes.craft.impl.BrewingStandCraft;
import su.nezushin.recipes.craft.impl.FurnaceCraft;
import su.nezushin.recipes.craft.impl.ShapedCraft;
import su.nezushin.recipes.craft.impl.SmokingCraft;
import su.nezushin.recipes.listener.RecipeHelperListener;

public class RecipeHelper extends JavaPlugin implements Listener {

    private List<FurnaceCraft> recipesFurnace = new ArrayList<>();
    private List<SmokingCraft> recipesSmoking = new ArrayList<>();
    private List<ShapedCraft> recipesShaped = new ArrayList<>();
    private List<BrewingStandCraft> recipesBrewingStand = new ArrayList<>();

    private FileConfiguration craftStorage;

    private static RecipeHelper instance;

    public List<RecipeContainer> recipes = new ArrayList<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(CraftRecipeChoice.class);
        ConfigurationSerialization.registerClass(FurnaceCraft.class);
        ConfigurationSerialization.registerClass(SmokingCraft.class);
        ConfigurationSerialization.registerClass(ShapedCraft.class);
        ConfigurationSerialization.registerClass(BrewingStandCraft.class);
        ConfigurationSerialization.registerClass(RecipeContainer.class);
        Bukkit.getPluginManager().registerEvents(new RecipeHelperListener(), getInstance());
        getDataFolder().mkdir();

        reloadConfig();
        getCommand("recipehelper").setExecutor(new RecipeHelperCommand());

        {
            File f = new File(getDataFolder() + File.separator + "storage.yml");
            if (!f.exists())
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            craftStorage = YamlConfiguration.loadConfiguration(f);
        }

        RecipeContainer.nextID = craftStorage.getInt("nextID", 0);


        recipes = (List<RecipeContainer>) craftStorage.getList("crafts", new ArrayList<RecipeContainer>());


        recipes.forEach(i -> i.getCraft().register());

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


    public void save() {
        File f = new File(getDataFolder() + File.separator + "storage.yml");
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        craftStorage.set("nextID", RecipeContainer.nextID);
        craftStorage.set("crafts", recipes);

        try {
            craftStorage.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveAsync() {
        //Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
        save();
        //});
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
