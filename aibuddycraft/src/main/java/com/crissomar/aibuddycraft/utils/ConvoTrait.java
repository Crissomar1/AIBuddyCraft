package com.crissomar.aibuddycraft.utils;

import com.crissomar.aibuddycraft.Plugin;
import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.trait.Trait;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;


public class ConvoTrait extends Trait {
    private String role;
    //create a list of every message to create a history of the conversation
    private List<ChatMessage> conversation = new ArrayList<>();

    public ConvoTrait(String role) {
        super("convotrait");
        
        Plugin.LOGGER.info("Creating ConvoTrait");
        this.role = role;
        //add the role to the conversation
        this.conversation.add(new ChatMessage("Admin",role));
        
        Plugin.LOGGER.info(this.conversation.toString());
    }

    public void reset(){
        this.conversation.clear();
        this.conversation.add(new ChatMessage("Admin",role));
    }

    public void converse(String message) {
        npc.getDefaultSpeechController().speak(new SpeechContext( message));
        //add the message to the conversation
        this.conversation.add(new ChatMessage("AI",message));
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        converse("Hello! I'm "+npc.getName()+"!");
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
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().toString();
        this.conversation.add(new ChatMessage("player","["+date+time+"]["+player+"] "+message+"\n"));
    }
    public void getResponse(){

        //Use OpenAI to get a response from GPT-3
        String apikey = Plugin.OPENAI_API_KEY;
        OpenAiService service = new OpenAiService(apikey, Duration.ofSeconds(0));
        ChatCompletionRequest request = ChatCompletionRequest
            .builder()
            .model(Plugin.OPENAI_MODEL)
            .messages(conversation)
            .maxTokens(Integer.parseInt(Plugin.OPENAI_MAX_TOKENS))
            .temperature(Double.parseDouble(Plugin.OPENAI_TEMPERATURE))
            .topP(Double.parseDouble(Plugin.OPENAI_TOP_P))
            .frequencyPenalty(Double.parseDouble(Plugin.OPENAI_FREQUENCY_PENALTY))
            .presencePenalty(Double.parseDouble(Plugin.OPENAI_PRESENCE_PENALTY))
            .build();

        var choices = service.createChatCompletion(request).getChoices();
        var response = choices.get(0).toString(); //what the AI responds with
        this.converse(response.toString());
    }
}

