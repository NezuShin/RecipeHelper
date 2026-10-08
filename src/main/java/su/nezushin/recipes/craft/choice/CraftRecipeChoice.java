package su.nezushin.recipes.craft.choice;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import su.nezushin.recipes.util.NBTUtil;

import java.util.*;

public class CraftRecipeChoice implements ConfigurationSerializable {

    private ItemStack item;
    private boolean useExactChoice, returnBuckets;
    private Set<String> ignoreTags;

    public CraftRecipeChoice(ItemStack item, boolean useExactChoice, boolean returnBuckets, Set<String> ignoreTags) {
        this.item = item;
        this.useExactChoice = useExactChoice;
        this.returnBuckets = returnBuckets;
        this.ignoreTags = ignoreTags;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public boolean isUseExactChoice() {
        return useExactChoice;
    }

    public void setUseExactChoice(boolean useExactChoice) {
        this.useExactChoice = useExactChoice;
    }

    public Set<String> getIgnoreTags() {
        return ignoreTags;
    }

    public void setIgnoreTags(Set<String> ignoreTags) {
        this.ignoreTags = ignoreTags;
    }

    public boolean isSimilar(ItemStack anotherItem) {
        return NBTUtil.equalsCommon(item, anotherItem, ignoreTags);
    }

    public RecipeChoice getRecipeChoice() {
        return useExactChoice ? new RecipeChoice.ExactChoice(item.clone()) :
                new RecipeChoice.MaterialChoice(item.getType());
    }

    public boolean isReturnBuckets() {
        return returnBuckets;
    }

    public void setReturnBuckets(boolean returnBuckets) {
        this.returnBuckets = returnBuckets;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("item", this.item);
        map.put("useExactChoice", this.useExactChoice);
        map.put("ignoreTags", this.ignoreTags);
        map.put("returnBuckets", this.returnBuckets);
        return map;
    }

    public static CraftRecipeChoice deserialize(Map<String, Object> map) {
        return new CraftRecipeChoice((ItemStack) map.get("item"), (Boolean) map.get("useExactChoice"),
                (Boolean) map.get("returnBuckets"),
                new HashSet<String>((Collection<String>) map.get("ignoreTags")));
    }
}
