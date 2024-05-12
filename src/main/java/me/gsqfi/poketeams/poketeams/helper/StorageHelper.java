package me.gsqfi.poketeams.poketeams.helper;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StorageHelper {
    /**
     * 查找玩家指定uuid的宝可梦
     */
    public static Pokemon find(UUID uuid, Player player){
        UUID pUid = player.getUniqueId();
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(pUid);
        PCStorage pc = Pixelmon.storageManager.getPCForPlayer(pUid);
        Pokemon pokemon = party.find(uuid);
        if (pokemon == null){
            pokemon = pc.find(uuid);
        }
        return pokemon;
    }
}
