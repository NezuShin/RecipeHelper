package su.nezushin.recipes.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import su.nezushin.recipes.util.ItemUtil;
import su.nezushin.recipes.RecipeContainer;
import su.nezushin.recipes.RecipeHelper;

public class ListGUI implements Listener {

    Inventory inv;
    Player p;

    Map<Integer, List<RecipeContainer>> pages = new HashMap<Integer, List<RecipeContainer>>();
    int currentPage = 0;

    public void reg() {
        Bukkit.getPluginManager().registerEvents(this, RecipeHelper.getInstance());
    }

    public void unReg() {
        HandlerList.unregisterAll(this);
    }

    public ListGUI(Player p) {
        this.p = p;

        inv = Bukkit.createInventory(null, 4 * 9, "RMB - change; Shift+LMB - delete");

        {
            ItemStack item = new ItemUtil(Material.IRON_BARS).setName(" ").item();
            for (int i = 27; i < 36; i++) {
                inv.setItem(i, item);
            }
        }

        {
            inv.setItem(32, new ItemUtil(Material.SPECTRAL_ARROW).setName("&rNext page").item());
            inv.setItem(30, new ItemUtil(Material.SPECTRAL_ARROW).setName("&rPrevious page").item());
        }

        {
            List<RecipeContainer> rcl = RecipeHelper.getInstance().recipes;

            List<RecipeContainer> current = new ArrayList<>();
            int pn = 0;
            for (int i = 0, j = 0; i < rcl.size(); i++, j++) {
                current.add(rcl.get(i));
                if (j == 27) {
                    pages.put(pn, current);
                    j = 0;
                    pn++;
                    current = new ArrayList<>();
                }
            }
            pages.put(pn, current);

        }
        display();
        reg();
        p.openInventory(inv);

    }

    public void display() {
        List<RecipeContainer> list = pages.get(currentPage);

        ItemStack AIR = new ItemStack(Material.AIR);

        for (int i = 0; i < 27; i++) {
            if (list.size() > i) {
                RecipeContainer iii = list.get(i);
                inv.setItem(i, new ItemUtil(iii.getCraft().getResult()).item());
            } else {
                inv.setItem(i, AIR);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getWhoClicked().equals(p))
            return;
        e.setCancelled(true);
        if(!e.getInventory().equals(e.getClickedInventory()))
            return;

        int slot = e.getSlot();

        if (slot == -999) {
            return;
        } else if (slot == 30) {
            currentPage = currentPage == 0 ? pages.size() - 1 : currentPage - 1;
            display();
        } else if (slot == 32) {
            currentPage = currentPage == pages.size() - 1 ? 0 : currentPage + 1;
            display();
        } else if (slot < 27) {
            List<RecipeContainer> page = pages.get(currentPage);

            RecipeContainer item = page.get(slot);

            if (item == null || item.getCraft() == null)
                return;


            if (e.getClick().equals(ClickType.RIGHT)) {
                unReg();
                new ChangeGUI(p, item);
            } else if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                item.getCraft().unregister();
                RecipeHelper.getInstance().recipes.remove(item);
                RecipeHelper.getInstance().saveAsync();
                unReg();
                p.closeInventory();
            }
        }

    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (!e.getPlayer().equals(p))
            return;
        unReg();
    }

}
