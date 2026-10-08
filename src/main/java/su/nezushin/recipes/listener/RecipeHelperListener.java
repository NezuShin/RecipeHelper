package su.nezushin.recipes.listener;

import com.google.common.collect.Sets;
import org.bukkit.Keyed;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.*;
import su.nezushin.recipes.RecipeHelper;
import su.nezushin.recipes.api.HelperFurnaceCraftUsedEvent;
import su.nezushin.recipes.api.HelperShapedCraftUsedEvent;
import su.nezushin.recipes.craft.impl.FurnaceCraft;
import su.nezushin.recipes.craft.impl.ShapedCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import su.nezushin.recipes.util.RecipeUtil;

import java.util.Set;

public class RecipeHelperListener implements Listener {

    private final Set<Material> buckets = Sets.newHashSet(Material.LAVA_BUCKET, Material.COD_BUCKET, Material.MILK_BUCKET,
            Material.WATER_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET, Material.TROPICAL_FISH_BUCKET);

    @EventHandler
    public void onBrewingStart(BrewingStartEvent e) {

        if (e.getBlock().getState() instanceof BrewingStand bs) {
            var inv = bs.getInventory();
            for (var i = 0; i < 3; i++) {
                inv.getItem(i);
            }
        }
    }

    @EventHandler
    public void onBrewingStart(BrewEvent e) {
        var rh = RecipeHelper.getInstance();
        var inv = e.getContents();

        var results = e.getResults();

        var source = inv.getItem(3);

        boolean returnBucket = false;
        for (var i = 0; i < results.size(); i++) {
            var result = results.get(i);
            var potion = inv.getItem(i);
            boolean found = false;
            boolean hasCrafts = false;
            Label_CraftSearch:
            for (var craft : RecipeUtil.getCrafts(result, rh.getRecipesBrewingStand())) {
                hasCrafts = true;
                if (craft.getPotion().isSimilar(potion) && craft.getSource().isSimilar(source)) {
                    found = true;
                    if(source != null && buckets.contains(source.getType()) && craft.getSource().isReturnBuckets())
                        returnBucket = true;
                    break Label_CraftSearch;
                }
            }
            if (!found && hasCrafts) {
                results.set(i, potion);
                continue;
            }
        }

        if (returnBucket) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(RecipeHelper.getInstance(),
                    () -> inv.setItem(3, new ItemStack(Material.BUCKET)));
        }


    }

    @EventHandler
    public void onFurnaceCraft(FurnaceSmeltEvent e) {
        var rh = RecipeHelper.getInstance();
        var returnBuckets = true;

        boolean c = false;


        if (e.getRecipe() instanceof Keyed recipe && !RecipeHelper.getInstance().isPluginKey(recipe.getKey()))
            return;


        FurnaceCraft fc = null;
        for (FurnaceCraft f : RecipeUtil.getCrafts(e.getResult(), rh.getRecipesFurnace())) {
            c = true;

            if (f.getSource().isSimilar(e.getSource())) {
                fc = f;
                break;
            }
        }

        if (c) {
            if (fc == null) {
                e.setCancelled(true);
                return;
            }
            HelperFurnaceCraftUsedEvent event = new HelperFurnaceCraftUsedEvent(fc, e.getResult(), e.getSource());

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            if (buckets.contains(fc.getSource().getItem().getType()) && returnBuckets) {
                Furnace furn = (Furnace) e.getBlock().getState();
                FurnaceInventory inv = furn.getInventory();

                inv.setSmelting(new ItemStack(Material.BUCKET));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RecipeHelper.getInstance(),
                        () -> inv.setSmelting(new ItemStack(Material.BUCKET)));
            }

            e.setResult(event.getResult());
        }
    }


    @EventHandler
    public void onCraft(CraftItemEvent e) {
        boolean c = false;
        var returnBuckets = true;

        CraftingInventory inv = e.getInventory();

        ItemStack result = inv.getResult();

        if (result == null)
            return;

        if (e.getRecipe() instanceof Keyed recipe && !RecipeHelper.getInstance().isPluginKey(recipe.getKey()))
            return;

        ShapedCraft sc = null;
        for (ShapedCraft f : RecipeUtil.getCrafts(result, RecipeHelper.getInstance().getRecipesShaped())) {
            c = true;
            if (f.matrix(inv)) {
                sc = f;
                break;
            }
        }

        if (c) {
            if (sc == null) {
                e.setCancelled(true);
                return;
            }
            HelperShapedCraftUsedEvent event = new HelperShapedCraftUsedEvent(sc, inv, e.getCurrentItem(), e);

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            if (returnBuckets) {
                ItemStack[] matrix = sc.getMatrixAsItems();
                for (int i = 0; i < matrix.length; i++) {
                    ItemStack item = matrix[i];

                    if (item != null && buckets.contains(item.getType())) {
                        final int ii = i;
                        inv.setItem(ii + 1, new ItemStack(Material.BUCKET));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RecipeHelper.getInstance(),
                                () -> inv.setItem(ii + 1, new ItemStack(Material.BUCKET)));
                    }

                }

            }

            e.setCurrentItem(event.getResult());

        }

    }


    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        CraftingInventory inv = e.getInventory();

        if (e.getRecipe() instanceof Keyed recipe && !RecipeHelper.getInstance().isPluginKey(recipe.getKey()))
            return;

        ItemStack result = inv.getResult();
        if (result == null)
            return;

        for (ShapedCraft f : RecipeUtil.getCrafts(result, RecipeHelper.getInstance().getRecipesShaped())) {
            if (f.matrix(inv)) {
                return;
            }
        }

        inv.setResult(null);

    }

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent e) {
        if (!RecipeHelper.getInstance().isPluginKey(e.getRecipe().getKey()))
            return;

        e.setCancelled(true);
    }
}
