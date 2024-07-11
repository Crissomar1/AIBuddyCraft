package com.crissomar;

import com.crissomar.aibuddycraft.utils.OpenAIChatCompletioner;
import com.crissomar.aibuddycraft.utils.ChatMessage;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * Unit test for simple Plugin.
 */
public class PluginTest 
{
    /**
     * Rigorous Test :-)
     */
    @SuppressWarnings("unused")
    @Test
    public void chatbotTest()
    {
        OpenAIChatCompletioner openAIChatCompletioner = new OpenAIChatCompletioner(
            "", 
            "claude-3-haiku-20240307",
            "50" );

        ChatMessage message = new ChatMessage("system","You are a good person bot.");
        ChatMessage message2 = new ChatMessage("user","How are you?");
        List<ChatMessage> conversation = List.of( message, message2);
        // String response = openAIChatCompletioner.chat(conversation);
        // System.out.println(response);
        assertTrue( true );
    }
}
