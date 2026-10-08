package su.nezushin.recipes.util;

import org.bukkit.inventory.ItemStack;
import su.nezushin.recipes.craft.AbstractCraft;

import java.util.ArrayList;
import java.util.List;

public class RecipeUtil {

    public static <T extends AbstractCraft> List<T> getCrafts(ItemStack item, List<T> list) {
        List<T> r = new ArrayList<>();

        for (T t : list) {
            if (t.getResult().isSimilar(item))
                r.add(t);
        }

        return r;
    }
}
