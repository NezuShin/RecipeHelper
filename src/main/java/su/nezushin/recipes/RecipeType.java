package su.nezushin.recipes;

import su.nezushin.recipes.craft.AbstractCraft;
import su.nezushin.recipes.craft.impl.FurnaceCraft;
import su.nezushin.recipes.craft.impl.ShapedCraft;

public enum RecipeType {

	furnace, workbench;

	public static RecipeType get(AbstractCraft craft) {
		if (craft instanceof FurnaceCraft)
			return furnace;
		if (craft instanceof ShapedCraft) {
			return workbench;
		}
		return null;
	}
}
