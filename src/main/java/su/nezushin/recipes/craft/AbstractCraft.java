package su.nezushin.recipes.craft;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public abstract class AbstractCraft {

	protected ItemStack result;

	public AbstractCraft(ItemStack result) {
		this.result = result;
	}

	protected final void checkGlobal() {
		if (!Bukkit.isGlobalTickThread()) {
			throw new IllegalStateException("recipe registry must run on the global region thread");
		}
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
