package me.gsqfi.poketeams.poketeams.commands;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractTabExecutor implements TabExecutor {
    private final String name;
    private AbstractTabExecutor superExecutor;
    private final Map<String,AbstractTabExecutor> subCmdMap = new HashMap<>();

    public AbstractTabExecutor(String name) {
        this.name = name;
    }

    public AbstractTabExecutor(AbstractTabExecutor superExecutor, String name) {
        this(name);
        this.superExecutor = superExecutor;
        this.superExecutor.subCmdMap.put(this.name,this);
    }

    public ArrayList<String> getSubCmdName(){
        return Lists.newArrayList(this.subCmdMap.keySet());
    }

    public String[] removeArgsOneObject(String[] args){
        if (args.length == 0) return args;
        ArrayList<String> list = Lists.newArrayList(args);
        list.remove(0);
        return list.toArray(new String[0]);
    }
}
