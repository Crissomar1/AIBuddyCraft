package com.crissomar.aibuddycraft.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SummonNPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p){

            if (args.length < 1){
                p.sendMessage("Please specify an NPC name and role.");
                return true;
            }

            String name = args[0];
            // Create a new player NPC with the specified name and role
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
            npc.spawn(p.getLocation());

            p.sendMessage("NPC created with name " + name);
        }

        return true;
    }
}
