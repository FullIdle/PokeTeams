package me.gsqfi.poketeams.poketeams.gui.v16;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import me.gsqfi.poketeams.poketeams.PlayerData;
import me.gsqfi.poketeams.poketeams.helper.StringHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TeamsListGui extends ListenerInvHolder {
    private final Inventory inventory;
    private final Player player;
    private final Map<Integer,String> team_map = new HashMap<>();
    private final int page;

    public TeamsListGui(Player player,int page){
        this.page = page;
        this.inventory = Bukkit.createInventory(this,54,StringHelper.configLang("teams_list_gui_title"));
        this.player = player;
        initInv();
        initTeams();
        initEventHandler();
    }

    private void initEventHandler(){
        this.onClick(e->{
            e.setCancelled(true);
            int slot = e.getSlot();
            if (slot == 47){
                if (this.page == 0) {
                    return;
                }
                TeamsListGui gui = new TeamsListGui(this.player, this.page-1);
                Inventory inv = gui.getInventory();
                this.player.closeInventory();
                this.player.openInventory(inv);
                return;
            }
            if (slot == 51){
                Inventory inv = new TeamsListGui(this.player, this.page + 1).getInventory();
                this.player.closeInventory();
                this.player.openInventory(inv);
                return;
            }
            ItemStack currentItem = e.getCurrentItem();
            if (currentItem != null && currentItem.getType().equals(Material.CHEST)) {
                String name = team_map.get(slot);
                HumanEntity whoClicked = e.getWhoClicked();
                whoClicked.closeInventory();
                Inventory inv = new TeamsGui(player, name).getInventory();
                whoClicked.openInventory(inv);
            }
        });
    }

    private void initInv(){
        /*47 51*/
        {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringHelper.configLang("teams_list_gui_previous_page_button"));
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(47,itemStack);
        }
        {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringHelper.configLang("teams_list_gui_next_page_button"));
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(51,itemStack);
        }
    }
    private void initTeams(){
        FileConfiguration config = PlayerData.getConfig();
        if (config.contains(player.getName())) {
            List<String> keys = Lists.newArrayList(config.getConfigurationSection(player.getName()).getKeys(false));
            int i = keys.size() / 45;
            if (this.page > i) return;
            ItemStack itemStack = new ItemStack(Material.CHEST);
            ItemMeta itemMeta = itemStack.getItemMeta();
            int x = 0;
            for (int j = i*45; j < Math.min(keys.size(),(i+1)*45); j++) {
                String name = keys.get(j);
                itemMeta.setDisplayName(StringHelper.configLang("teams_list_gui_team_name").replace("{team_name}",name));
                itemStack.setItemMeta(itemMeta);
                this.inventory.addItem(itemStack);
                this.team_map.put(x,name);
                x++;
            }
        }
    }
}
