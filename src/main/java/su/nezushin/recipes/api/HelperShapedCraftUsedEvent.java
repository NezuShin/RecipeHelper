package su.nezushin.recipes.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import su.nezushin.recipes.craft.impl.ShapedCraft;

public class HelperShapedCraftUsedEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	private ShapedCraft craft;
	private CraftingInventory inv;
	private ItemStack crafted;
	private boolean cancelled = false;
	private CraftItemEvent event;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public HelperShapedCraftUsedEvent(ShapedCraft craft, CraftingInventory inv, ItemStack crafted, CraftItemEvent event) {
		this.craft = craft;
		this.inv = inv;
		this.crafted = crafted;
		this.event = event;
	}
	
	public ShapedCraft getCraft() {
		return craft;
	}


	public CraftingInventory getInv() {
		return inv;
	}

	public ItemStack getResult() {
		return crafted;
	}

	public void setResult(ItemStack crafted) {
		this.crafted = crafted;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public CraftItemEvent getEvent() {
		return event;
	}
	
}
