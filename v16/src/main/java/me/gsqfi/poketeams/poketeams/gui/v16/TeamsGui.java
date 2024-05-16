package me.gsqfi.poketeams.poketeams.gui.v16;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import lombok.Getter;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import me.gsqfi.poketeams.poketeams.Data;
import me.gsqfi.poketeams.poketeams.PlayerData;
import me.gsqfi.poketeams.poketeams.helper.v16.StorageHelper;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class TeamsGui extends ListenerInvHolder {
    private final Inventory inventory;
    private final Player player;
    private final String team_name;
    private final ArrayList<Pokemon> pokemons = new ArrayList<>();

    public TeamsGui(Player player,String team_name) {
        this.team_name = team_name;
        this.player = player;
        this.inventory = Bukkit.createInventory(this,45,StringHelper.configLang("teams_gui_title").replace("{team_name}",team_name));

        initInv();
        initEventHolder();
    }

    @SneakyThrows
    private void initInv() {
        {
            ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1, (short) 0);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(" ");
            itemStack.setItemMeta(itemMeta);
            for (int i = 0; i < 9; i++) {
                this.inventory.setItem(i, itemStack);
            }
            for (int i = 36; i < 45; i++) {
                this.inventory.setItem(i, itemStack);
            }
        }
        {//宝可梦
            List<String> list = PlayerData.getConfig().getStringList(player.getName() + "." + team_name);
            int x = 10;
            for (String uuid : list) {
                ItemStack itemStack = this.getBarrierItemStack();
                Pokemon pokemon = null;
                if (!uuid.isEmpty()) {
                    pokemon = StorageHelper.find(UUID.fromString(uuid), player);
                    if (pokemon != null) {
                        itemStack = this.getPokemonItemStack(pokemon);
                    } else {
                        player.sendMessage(StringHelper.configLang("missing_poke"));
                    }
                }
                this.pokemons.add(pokemon);
                this.inventory.setItem(x, itemStack);
                if (x == 16) {
                    x = 28;
                    continue;
                }
                x += 3;
            }
        }
        {
            //replace team
            ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringHelper.configLang("teams_gui_substitute_team_button"));
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(38,itemStack);
        }
        {
            //remove button
            ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringHelper.configLang("teams_gui_remove_team_button"));
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(42,itemStack);
        }
    }

    private ItemStack getBarrierItemStack(){
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringHelper.configLang("teams_gui_poke_empty_tank"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private void initEventHolder(){
        this.onClick(e->{
            e.setCancelled(true);
            int slot = e.getSlot();
            if (slot == 38){
                //替换队伍
                PlayerPartyStorage party = StorageProxy.getParty(player.getUniqueId());
                PCStorage pc = StorageProxy.getPCForPlayer(player.getUniqueId());
/*
                boolean b = false;
*/
                for (Pokemon pokemon : party.getAll()) {
                    if (pokemon != null) {
                        party.set(pokemon.getPosition(),null);
                        pc.add(pokemon);
                    }
                }

                for (int i = 0; i < this.pokemons.size(); i++) {
                    Pokemon pokemon = this.pokemons.get(i);
                    if (pokemon != null) {
/*                        if (pokemon.isInRanch()){
                            if (!b){
                                b = true;
                            }
                            continue;
                        }*/
                        pc.set(pokemon.getPosition(),null);
                        party.set(i, pokemon);
                    }
                }
                player.closeInventory();
/*
                if (b) player.sendMessage(StringHelper.configMsg("team_in_ranch"));
*/
                player.sendMessage(StringHelper.configLang("team_replaced"));
                return;
            }
            if (slot == 42){
                PlayerData.getConfig().set(player.getName()+"."+team_name,null);
                PlayerData.save();

                player.closeInventory();
                player.sendMessage(StringHelper.configLang("team_removed"));
            }
        });
    }

    /**
     * 获取格式化后的物品
     */
    private ItemStack getPokemonItemStack(Pokemon pokemon){
        StoragePosition position = pokemon.getPosition();
        FileConfiguration config = Data.plugin.getConfig();
        ArrayList<String> lore = new ArrayList<>();
        for (String lore_info : config.getStringList("pokemon_item_format.lore")) {
            lore.add(formatString(lore_info.replace("{pokemon_name}",pokemon.getLocalizedName()),position));
        }
        ItemStack itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_16_R3.ItemStack) ((Object) SpriteItemHelper.getPhoto(pokemon)));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(formatString(config.getString("pokemon_item_format.name").replace("{pokemon_name}",pokemon.getLocalizedName()),position));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 格式化宝可梦字符串
     */
    private String formatString(String str,StoragePosition position){
        return StringHelper.papi(player, StringHelper.colorCodeReplace(str).replace("{box}",
                String.valueOf(position.box)).replace("{order}",
                String.valueOf(position.order)));
    }
}
