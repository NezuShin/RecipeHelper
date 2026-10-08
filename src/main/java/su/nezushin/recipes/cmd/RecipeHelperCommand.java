package su.nezushin.recipes.cmd;

import com.google.common.collect.Lists;
import su.nezushin.recipes.RecipeContainer;
import su.nezushin.recipes.RecipeHelper;
import su.nezushin.recipes.RecipeType;
import su.nezushin.recipes.gui.ChangeGUI;
import su.nezushin.recipes.gui.CreateGUI;
import su.nezushin.recipes.gui.ListGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeHelperCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nezu.recipe.admin") || !(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage("/" + label + " list");
            sender.sendMessage("/" + label + " create");
            sender.sendMessage("/" + label + " edit <id>");
            sender.sendMessage("/" + label + " save");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            new ListGUI(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            ItemStack item = p.getInventory().getItemInMainHand();

            new CreateGUI(p, item, args.length > 1 ? RecipeType.valueOf(args[1]) : RecipeType.workbench);
            return true;
        }

        if (args[0].equalsIgnoreCase("edit")) {
            if (args.length > 2) {
                p.sendMessage("/" + label + " edit <id>");
                return true;
            }
            try {
                int id = Integer.parseInt(args[1]);
                RecipeContainer rc = RecipeHelper.getInstance().recipes.
                        stream().filter(c -> c.getId() == id).findFirst().orElse(null);

                if (rc == null) {
                    p.sendMessage("recipe not found");
                    return true;
                }

                new ChangeGUI(p, rc);
            } catch (NumberFormatException e) {
                p.sendMessage("<id> must be int");
            }
        }

        if (args[0].equalsIgnoreCase("save")) {
            RecipeHelper.getInstance().saveAsync();
            p.sendMessage("saved!");
        }

        return true;
    }

    private List<String> fargs = Lists.newArrayList("list", "create", "edit", "save");
    private List<String> sargs = Lists.newArrayList("furnace", "workbench");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> r = new ArrayList<>();
            for (String s : fargs)
                if (s.startsWith(args[0].toLowerCase()))
                    r.add(s);
            return r;
        }
        if (args.length == 2) {
            List<String> r = new ArrayList<>();
            for (String s : sargs)
                if (s.startsWith(args[1].toLowerCase()))
                    r.add(s);
            return r;
        }
        return null;
    }
}
