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

public class ChangeGUI implements Listener {

	public void reg() {
		Bukkit.getPluginManager().registerEvents(this, RecipeHelper.getInstance());
	}

	public void unReg() {
		HandlerList.unregisterAll(this);
	}

	Inventory inv;
	RecipeContainer rc;
	Player p;
	RecipeType type;

	public ChangeGUI(Player p, RecipeContainer rc) {
		this.p = p;
		this.rc = rc;
		this.inv = Bukkit.createInventory(null, InventoryType.DISPENSER, "Close this menu to continue");

		this.type = RecipeType.get(rc.getCraft());

		if (rc.getCraft() instanceof ShapedCraft sc) {

			var items = sc.getMatrixAsItems();
			for(var x = 0; x < sc.getSizeX(); x++){
				for(var y = 0; y < sc.getSizeY(); y++){
					inv.setItem((x + y*3), items[x + y*sc.getSizeX()]);
				}
			}


			//inv.setContents(((ShapedCraft) rc.getCraft()).getMatrixAsItems());
		} else {
			inv.setItem(4, ((FurnaceCraft) rc.getCraft()).getSource().getItem());
		}

		p.openInventory(inv);
		reg();
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (!e.getPlayer().equals(p))
			return;
		unReg();
		if (type == RecipeType.workbench) {
			ShapedCraft sc = (ShapedCraft) rc.getCraft();

			sc.unregister();
			var sizeX = 0;
			var sizeY = 0;

			for (var y = 0; y < 3; y++)
				for (var x = 0; x < 3; x++)
					if (inv.getItem(x + y * 3) != null) {
						if (sizeX <= x) {
							sizeX = x+1;
						}
						if (sizeY <= y) {
							sizeY = y+1;
						}
					}


			var items = new CraftRecipeChoice[sizeX*sizeY];
			for(var y = 0; y < sizeY; y++)
				for(var x = 0; x < sizeX; x++)
					items[x + y*sizeX] = new CraftRecipeChoice(inv.getItem(x + y * 3), false, true, new HashSet<>());
			sc.setSizeX(sizeX);
			sc.setSizeY(sizeY);
			sc.setMatrix(items);

			rc.setCraft(sc);

			rc.getCraft().register();

			RecipeHelper.getInstance().saveAsync();

			p.sendMessage("craft saved! id: " + rc.getId());
		} else {
			FurnaceCraft sc = (FurnaceCraft) rc.getCraft();

			sc.unregister();

			ItemStack a = inv.getItem(4);

			if (a == null) {
				p.sendMessage("item cannot be null!");
				return;
			}

			rc.setCraft(sc);

			p.sendMessage("craft saved! id: " + rc.getId());

			sc.register();

			RecipeHelper.getInstance().saveAsync();
		}
		RecipeHelper.getInstance().saveAsync();
	}

}
