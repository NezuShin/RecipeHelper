package su.nezushin.recipes.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

public class ItemUtil {
	
	ItemStack item;
	ItemMeta meta;
	
	public ItemUtil(ItemStack item) {
		this.item = item;
		this.meta = item.getItemMeta();
	}
	
	public ItemStack item() {
		meta();
		return item;
	}
	
	public ItemUtil(Material m) {
		this.item = new ItemStack(m);
		this.meta = item.getItemMeta();
	}
	
	@SuppressWarnings("deprecation")
	public ItemUtil(Material m, short data) {
		this.item = new ItemStack(m, 1, data);
		this.meta = item.getItemMeta();
	}
	
	public ItemUtil setName(String name) {
		meta.setDisplayName(name.replace("&", "§"));
		meta();
		return this;
	}
	
	public ItemUtil flas(ItemFlag... f) {
		meta.addItemFlags(f);
		meta();
		return this;
	}
	
	public ItemUtil setUnbreakable(boolean u) {
		meta.setUnbreakable(u);
		meta();
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public ItemUtil setHeadOwner(String owner) {
		((SkullMeta)meta).setOwner(owner);
		meta();
		return this;
	}
	
	public ItemUtil addPotionEffect(PotionEffect effect, boolean ef) {
		((PotionMeta)meta).addCustomEffect(effect, ef);
		
		meta();
		return this;
	}
	
	public ItemUtil color(Color color) {
		try {
			((LeatherArmorMeta)meta).setColor(color);
		}catch (Exception e) {
			((PotionMeta)meta).setColor(color);
		}
		meta();
		return this;
	}
	
	public ItemUtil lore(String... lore) {
		List<String> list = new ArrayList<>();
		for(String s : lore) {
			if(s == null) continue;
			s.replace("&", "§");
			list.add(s);
		}
		meta.setLore(list);
		meta();
		return this;
	}
	
	public ItemUtil setline(int line,String text) {
		List<String> lore = meta.hasLore() ?  meta.getLore() : new ArrayList<>();
		if(lore.size() < line) {
			for(int i = lore.size(); i <= line; i++) {
				lore.add("");
			}
		}
		lore.set(line, text);
		meta.setLore(lore);
		meta();
		return this;
	}
	
	public ItemUtil lore(List<String> list) {
		meta.setLore(list);
		meta();
		return this;
	}
	
	private void meta() {
		item.setItemMeta(meta);
	}
	
}
