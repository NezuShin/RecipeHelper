package su.nezushin.recipes.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import su.nezushin.recipes.craft.impl.FurnaceCraft;

public class HelperFurnaceCraftUsedEvent extends Event implements Cancellable {
		
	private static HandlerList handlers = new HandlerList();
	private FurnaceCraft fc;
	private ItemStack result;
	private ItemStack source;
	private boolean cancelled = false;
	
	
	public HelperFurnaceCraftUsedEvent(FurnaceCraft fc, ItemStack result, ItemStack source) {
		this.fc = fc;
		this.result = result;
		this.source = source;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public FurnaceCraft getCraft() {
		return fc;
	}

	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

	public ItemStack getSource() {
		return source;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
