package com.crissomar.aibuddycraft.utils;

import java.util.List;

import com.crissomar.aibuddycraft.utils.ChatMessage;

import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SuppressWarnings("unused")
public class OpenAIChatCompletioner {
    //Create a class to send the messages to OpenAI and get the response
    private String apiKey;
    private String model;
    private String maxTokens;

    public OpenAIChatCompletioner(
        String apiKey, 
        String model, 
        String maxTokens) {
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
    }

    public String chat(List<ChatMessage> conversation) {
        //Create a JSON object with the conversation
        //first mesaage is the system message
        String system = conversation.get(0).getMessage();
        String conversationString = "";
        for (int i = 1; i < conversation.size(); i++) {
            conversationString += conversation.get(i).toOpenAIString() + ",";
        }
        conversationString = conversationString.substring(0, conversationString.length() - 1);
        //System.out.println(conversationString);
        String json = "{\"model\":\"" + model + "\"" +
               ",\"max_tokens\":" + maxTokens + 
                ",\"system\": \""+ system + "\""+
                ",\"messages\":[" + conversationString + "]}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.anthropic.com/v1/messages"))
            .header("x-api-key", apiKey)
            .header("anthropic-version", "2023-06-01")
            .header("Content-Type", "application/json")
            .POST(body)
            .build();
        //System.out.println(request.toString());

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String rBody = response.body();
                String message = rBody.substring(rBody.indexOf("content")+33, rBody.indexOf("stop_reason")-5);
                return message;
            } else {
                String rBody = response.body();
                String message = rBody.substring(rBody.indexOf("messages:")+10, rBody.indexOf("}"));
                throw new RuntimeException("Failed: HTTP error code : " + response.statusCode()+" M:"+message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
