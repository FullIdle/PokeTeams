package me.gsqfi.poketeams.poketeams.helper;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class ComponentHelper {
    private final ComponentBuilder builder = new ComponentBuilder("");
    private ComponentBuilder temp;

    public ComponentHelper(ComponentBuilder builder) {
        this.temp = builder;
    }

    public ComponentHelper(String text){
        this.temp = new ComponentBuilder(text);
    }

    public ComponentHelper apply(String text){
        this.builder.append(temp.create());
        this.temp = new ComponentBuilder(text);
        return this;
    }

    public ComponentHelper event(ClickEvent event){
        this.temp.event(event);
        return this;
    }

    public ComponentHelper event(HoverEvent event){
        this.temp.event(event);
        return this;
    }

    public BaseComponent[] create(){
        return this.getBuilder().create();
    }

    public ComponentBuilder getBuilder(){
        if (this.temp != null) {
            this.builder.append(this.temp.create());
        }
        return this.builder;
    }
}
