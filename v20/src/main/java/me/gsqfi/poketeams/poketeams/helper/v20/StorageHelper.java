package me.gsqfi.poketeams.poketeams.helper.v20;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StorageHelper {
    /**
     * 查找玩家指定uuid的宝可梦
     */
    public static Pokemon find(UUID uuid, Player player){
        UUID pUid = player.getUniqueId();
        PlayerPartyStorage party = StorageProxy.getPartyNow(pUid);
        PCStorage pc = StorageProxy.getPCForPlayerNow(pUid);
        Pokemon pokemon = party.find(uuid);
        if (pokemon == null){
            pokemon = pc.find(uuid);
        }
        return pokemon;
    }
}
