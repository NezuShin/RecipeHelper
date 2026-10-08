package su.nezushin.recipes;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import su.nezushin.recipes.craft.AbstractCraft;

import java.util.HashMap;
import java.util.Map;

public class RecipeContainer implements ConfigurationSerializable {

	private AbstractCraft craft;
	private int id;
	public static int nextID;
	
	public RecipeContainer(AbstractCraft craft) {
		id = nextID;
		this.craft = craft;
		
		nextID++;
	}
	
	public RecipeContainer(AbstractCraft craft, int id) {
		this.craft = craft;
		this.id = id;
	}

	public AbstractCraft getCraft() {
		return craft;
	}

	public void setCraft(AbstractCraft craft) {
		this.craft = craft;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public @NotNull Map<String, Object> serialize() {
		var map = new HashMap<String, Object>();
		map.put("craft", this.craft);
		map.put("id", id);
		return map;
	}

	public static RecipeContainer deserialize(Map<String, Object> map) {
		return new RecipeContainer((AbstractCraft) map.get("craft"), (Integer) map.get("id"));
	}
}
