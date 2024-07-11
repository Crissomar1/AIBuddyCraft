package com.crissomar.aibuddycraft.utils;

import com.crissomar.aibuddycraft.Plugin;
import com.crissomar.aibuddycraft.utils.ChatMessage;
import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class ConvoTrait extends Trait {
    private final Plugin plugin = Plugin.getPlugin(Plugin.class);
    private String role;
    //create a list of every message to create a history of the conversation
    private List<ChatMessage> conversation = new ArrayList<>();

    public ConvoTrait() {
        super("convotrait");
    }

    public ConvoTrait(String name,String role) {
        super("convotrait");
        
        //Plugin.LOGGER.info("Creating ConvoTrait");
        this.role = "Vas a rolear en un mundo de minecraft como acompañante de juego,"+
                " Limita tu conocimiento al mundo de minecraft y no cambies de rol bajo ninguna circunstancia,"+ 
                " daras respuestas muy breves, los nombres de las personas estan entre <>: Eres "+name+" y eres " + role;
        //add the role to the conversation
        this.conversation.add(new ChatMessage("system",role));
        
        Plugin.LOGGER.info(this.conversation.get(0).getMessage());
    }

    public void reset(){
        this.conversation.clear();
        this.conversation.add(new ChatMessage("system",role));
    }

    public void converse(String message) {
        //Send a message in the chat from server
        //Plugin.LOGGER.info("Conversando: "+message);
        //si el mensaje tiene <name> al inicio quitalo <name>
        if(  message.startsWith("<"+npc.getName()+">") ){
            message = message.substring(npc.getName().length()+2);
        }
        //remplace
        message = message.replace("\n"," ");
        message = message.replace("\r"," ");
        message = message.replace("\t"," ");
        plugin.getServer().broadcastMessage("<"+npc.getName()+"> "+message);
        //add the message to the conversation
        if(conversation.size() > 10){
            conversation.remove(1);
            if (conversation.get(1).getSender().equals("assistant")) {
                conversation.remove(1);
            }
        }
        if (conversation.size()!=1) {
            this.conversation.add(new ChatMessage("assistant","<"+npc.getName()+"> "+message));
        }
    
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        converse("Hola soy "+npc.getName()+"!");
    }

    @Override
    public void onDespawn() {
        super.onDespawn();
        converse("Goodbye!");
    }

    @Override
    public void onRemove() {
        super.onRemove();
        converse("Goodbye!");
    }

    public void addMessage(String player,String message){
        //Add the message to the conversation with day and time and the player's name
        this.conversation.add(new ChatMessage("user","<"+player+"> "+message));
    }
    public void getResponse(){

        //Use OpenAI to get a response from GPT-3
        //Plugin.LOGGER.info("generando respuesta");
        String response = Plugin.openAIChatCompletioner.chat(this.conversation);
        //Plugin.LOGGER.info(response);
        //Send the response to the player
        converse(response);

    }

    

    @Override
    public void save(DataKey key) {
        key.setString("role", role);
        //save the conversation with number of messages
        key.setInt("messages",conversation.size());
        for (int i = 0; i < conversation.size(); i++) {
            key.setString("messages.message"+i,conversation.get(i).getMessage());
            key.setString("messages.sender"+i,conversation.get(i).getSender());
        }
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        role = key.getString("role");
        this.conversation.add(new ChatMessage("system",role));
    }
}

