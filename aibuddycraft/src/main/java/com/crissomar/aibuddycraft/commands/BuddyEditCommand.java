package com.crissomar.aibuddycraft.commands;


import com.crissomar.aibuddycraft.utils.ConvoTrait;
import com.crissomar.aibuddycraft.Plugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class BuddyEditCommand implements CommandExecutor, TabCompleter{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p){
            //usage: /buddy <edit|skin|remove> <nombre> [argumentos]
            if (args.length < 2){
                p.sendMessage("Please specify an action and NPC name.");
                return true;
            }

            String action = args[0];
            String name = args[1];
            // Find the NPC with the specified name
            NPC npc = null;
            for (NPC n : CitizensAPI.getNPCRegistry()){
                if (n.getName().equals(name)){
                    npc = n;
                    break;
                }
            }

            if (npc == null){
                p.sendMessage("NPC not found.");
                return true;
            }

            if (action.equals("remove")){
                npc.destroy();
                p.sendMessage("NPC removed.");
                return true;
            }

            if (action.equals("edit")){
                if (args.length < 3){
                    p.sendMessage("Please specify a role.");
                    return true;
                }
                Plugin.LOGGER.info("setting role to " + args[2]);
                // role is text from the third argument onwards
                //String role = args[2];
                String role = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if(npc.hasTrait(ConvoTrait.class)){
                    Plugin.LOGGER.info("npc has convo trait");
                    npc.removeTrait( ConvoTrait.class);
                    npc.addTrait(new ConvoTrait(npc.getName(),role));
                }else{
                    Plugin.LOGGER.info("npc does not have convo trait");
                    npc.addTrait(new ConvoTrait(npc.getName(),role));
                }
                p.sendMessage("Role set to " + role);
                return true;
            }

            if (action.equals("skin")){
                if (args.length < 3){
                    p.sendMessage("Please specify a skin name.");
                    return true;
                }else if (args.length == 3){
                    p.performCommand("npc select " + npc.getName());
                    p.performCommand("npc skin " + args[2]);
                    p.sendMessage("Skin set");
                    return true;
                }else if (args[2] == "url"){
                    p.performCommand("npc select " + npc.getName());
                    p.performCommand("npc skin url " + args[3]);
                    p.sendMessage("Skin set");
                    return true;
                }
                return true;
            }
        }else{
            sender.sendMessage("You must be a player to use this command.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            return Arrays.asList("edit", "skin", "remove");
        }

        if (args.length == 2){
            Iterable<NPC> npcs = CitizensAPI.getNPCRegistry().sorted();
            return StreamSupport.stream(npcs.spliterator(), false)
                    .map(NPC::getName)
                    .collect(Collectors.toList());
        }

        if(args.length == 3 && args[0].equals("skin")){
            return Arrays.asList("url");
        }

        if(args.length == 3 && args[0].equals("edit")){
            return Arrays.asList("role");
        }

        return null;
    }
}
