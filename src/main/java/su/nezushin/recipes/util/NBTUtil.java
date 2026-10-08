package su.nezushin.recipes.util;

import com.google.common.collect.Sets;
import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NBTUtil {


    public static boolean equalsCommon(final ItemStack firstItem, ItemStack secondItem, Set<String> ignoreTags) {
        if(firstItem == null)
            return secondItem == null;

        ReadableNBT first = NBT.getComponents(firstItem, i -> (ReadableNBT) i);

        ReadableNBT second = NBT.getComponents(secondItem, i -> (ReadableNBT) i);
        return check(first, second, ignoreTags);
    }

    public static boolean check(ReadableNBT first, ReadableNBT second, Set<String> ignoreTags) {
        if (first == null) {
            return second == null;
        }

        var diff = first.extractDifference(second);

        diff.mergeCompound(second.extractDifference(first));
        System.out.println("123");
        System.out.println(diff);
        System.out.println("123");
        var res = checkTags(diff, ignoreTags, false, "");
        return res;
    }

    public static boolean checkTags(ReadWriteNBT nbt, Set<String> ignoreTags, boolean appendPath, String path) {
        System.out.println(path);
        for (var i : nbt.getKeys()) {
            var ipath = (appendPath ? path + "." : "") + i;
            if (ignoreTags.stream().noneMatch(j -> j.startsWith(ipath)))
                return false;
            if (nbt.getType(i) == NBTType.NBTTagCompound) {
                if (ignoreTags.stream().noneMatch(j -> j.equalsIgnoreCase(ipath)))
                    if (!checkTags(nbt.getCompound(i), ignoreTags, true, ipath))
                        return false;
            }
        }
        return true;
    }

}
