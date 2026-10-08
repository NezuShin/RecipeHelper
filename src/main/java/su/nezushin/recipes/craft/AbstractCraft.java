package su.nezushin.recipes.craft;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public abstract class AbstractCraft {

	protected ItemStack result;

	public AbstractCraft(ItemStack result) {
		this.result = result;
	}

	public abstract void unregister();
	
	public abstract void register();
	
	public abstract boolean recipe(Recipe r);


	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}
}
