package su.nezushin.recipes.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import su.nezushin.recipes.RecipeContainer;
import su.nezushin.recipes.RecipeHelper;
import su.nezushin.recipes.RecipeType;
import su.nezushin.recipes.craft.choice.CraftRecipeChoice;
import su.nezushin.recipes.craft.impl.FurnaceCraft;
import su.nezushin.recipes.craft.impl.ShapedCraft;

import java.util.HashSet;

public class CreateGUI implements Listener {

    Inventory inv;
    Player p;
    ItemStack item;
    RecipeType type;

    /**
     * @param p
     * @param item
     * @param type - if true - workbench else furnace
     */
    public CreateGUI(Player p, ItemStack item, RecipeType type) {
        this.p = p;
        this.item = item;
        this.type = type;

        inv = Bukkit.createInventory(null, InventoryType.DISPENSER, "Close this menu to continue");
        p.openInventory(inv);
        reg();
    }

    public void reg() {
        Bukkit.getPluginManager().registerEvents(this, RecipeHelper.getInstance());
    }

    public void unReg() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (!e.getPlayer().equals(p))
            return;
        unReg();
        if (type == RecipeType.workbench) {
            var sizeX = 0;
            var sizeY = 0;

            for (var y = 0; y < 3; y++)
                for (var x = 0; x < 3; x++)
                    if (inv.getItem(x + y * 3) != null) {
                        if (sizeX <= x) {
                            sizeX = x + 1;
                        }
                        if (sizeY <= y) {
                            sizeY = y + 1;
                        }
                    }

            var items = new CraftRecipeChoice[sizeX * sizeY];
            for (var y = 0; y < sizeY; y++)
                for (var x = 0; x < sizeX; x++)
                    items[x + y * sizeX] = new CraftRecipeChoice(inv.getItem(x + y * 3), false, true, new HashSet<>());

            RecipeContainer rc = new RecipeContainer(new ShapedCraft(item, items, sizeX, sizeY));


            rc.getCraft().register();

            RecipeHelper.getInstance().recipes.add(rc);


            p.sendMessage("craft saved! id: " + rc.getId());

        } else {

            ItemStack a = inv.getItem(4);

            if (a == null) {
                p.sendMessage("item cannot be null!");
                return;
            }

            RecipeContainer rc = new RecipeContainer(new FurnaceCraft(item, new CraftRecipeChoice(a, false, true, new HashSet<>()), 200, 0.0f));

            rc.getCraft().register();

            RecipeHelper.getInstance().recipes.add(rc);

            p.sendMessage("craft saved! id: " + rc.getId());
        }
        RecipeHelper.getInstance().saveAsync();

    }

}
