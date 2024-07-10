package com.crissomar.aibuddycraft.listeners;

import com.crissomar.aibuddycraft.Plugin;
import com.crissomar.aibuddycraft.utils.ConvoTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.CompletableFuture;

public class NPCListeners implements Listener {

    @SuppressWarnings("unused")
    private final Plugin plugin;
    public NPCListeners(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {

        //Get all the npcs near the player
        for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {

            if (npc.getEntity() == null || npc.hasTrait(ConvoTrait.class)){
                continue;
            }

            var trait = npc.getTraitNullable(ConvoTrait.class);

            //If the player is talking to the NPC but is not within 20 blocks, stop the conversation
            if (npc.getEntity().getLocation().distance(e.getPlayer().getLocation()) > 20){
                continue;
            }else{
                //get what the player typed in chat
                trait.addMessage(e.getPlayer().getName(),e.getMessage());

                CompletableFuture.runAsync(() -> {
                    //Use OpenAI to get a response
                    trait.getResponse();
                });
            }

        }

    }

}
