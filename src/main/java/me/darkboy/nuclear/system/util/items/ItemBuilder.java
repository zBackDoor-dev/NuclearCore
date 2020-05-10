package me.darkboy.nuclear.system.util.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta itemMeta;

    private ItemEnchantment[] enchantments;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.itemMeta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.itemMeta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.itemMeta = item.getItemMeta();
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder(Material material, int amount, byte damage) {
        this.item = new ItemStack(material, amount, damage);
        this.itemMeta = item.getItemMeta();
    }

    public ItemBuilder setDisplayName(String displayName) {
        itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchants(ItemEnchantment... enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer player) {
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwningPlayer(player);
        return this;
    }

    public ItemBuilder setSkullOwner(UUID uuid) {
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwningPlayer(Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid)));
        return this;
    }

    public ItemStack build() {
        if (enchantments != null) {
            for (ItemEnchantment enchant : enchantments) {
                itemMeta.addEnchant(enchant.getEnchantment(), enchant.getLevel(), true);
            }
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    public static class ItemEnchantment {

        @Getter
        private Enchantment enchantment;

        @Getter
        private int level = 0;

        public ItemEnchantment(Enchantment enchantment) {
            this.enchantment = enchantment;
        }

        public ItemEnchantment(Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }

        public ItemEnchantment setLevel(int level) {
            this.level = level;
            return this;
        }
    }
}
