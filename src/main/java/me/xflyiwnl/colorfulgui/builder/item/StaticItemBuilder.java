package me.xflyiwnl.colorfulgui.builder.item;

import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.ItemBuilder;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.action.GuiAction;
import me.xflyiwnl.colorfulgui.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class StaticItemBuilder extends ItemBuilder<StaticItem> {

    private Material material;

    private String name;
    private List<String> lore = Arrays.asList();

    private int amount = 1;
    private ItemFlag[] itemFlags;

    private boolean unbreakable = false;
    private int model = 0;

    private boolean isSkull = false;
    private Player player;

    private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    private GuiAction<InventoryClickEvent> action;

    public StaticItemBuilder() {
    }

    public StaticItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public StaticItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public StaticItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public StaticItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public StaticItemBuilder flags(ItemFlag... flags) {
        this.itemFlags = flags;
        return this;
    }

    public StaticItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public StaticItemBuilder model(int model) {
        this.model = model;
        return this;
    }

    public StaticItemBuilder enchant(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public StaticItemBuilder action(GuiAction<InventoryClickEvent> action) {
        this.action = action;
        return this;
    }

    public StaticItemBuilder skull(Player player) {
        this.player = player;
        this.isSkull = true;
        return this;
    }

    @Override
    public StaticItem build() {

        UUID uuid = UUID.randomUUID();

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(TextUtil.colorize(name));
        }

        itemMeta.setLore(TextUtil.colorize(lore));

        if (!enchantments.isEmpty()) {
            enchantments.forEach((enchantment, integer) -> {
                itemMeta.addEnchant(enchantment, integer, true);
            });
        }

        itemMeta.setUnbreakable(unbreakable);
        itemMeta.setCustomModelData(model);

        if (itemFlags != null) {
            itemMeta.addItemFlags(itemFlags);
        }

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ColorfulGUI.getInstance(), "colorfulgui"), PersistentDataType.STRING, uuid.toString());

        if (isSkull) {
            itemStack.setType(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setPlayerProfile(player.getPlayerProfile());
            itemStack.setItemMeta(skullMeta);
        }

        itemStack.setItemMeta(itemMeta);

        return new StaticItem(uuid, itemStack, action);
    }
}