package com.aphidgpt.gpt;

import com.aphidgpt.AphidGPT;
import com.aphidgpt.json.Player;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class call {


    public static void main(String[] args) {
    }

    // TODO: IF YOU SEND A PROMPT SUCH AS: "Can you code a python script that says Hello Minecraft!" will cause it to send back "[TIMED OUT]" when there must be a different problem occuring as it has enough requests at its disposal to send without timing out.
    // TODO: Check above statement for if it is still relevant.
    public static Result chatGPT(List<Message> conversation, int maxNumOfRetryAttempts, long retryDelayMS) {
        AphidGPT.setTimeout(() -> {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Sending..."));
        }, 1000);

        String url = AphidGPT.END_POINT;
        String apiKey = AphidGPT.API_KEY; // Replace with your actual API key
        String model = AphidGPT.MODEL;
        
        boolean notRecieved = true;
        int numOfAttempts = 0;
        String inputMessage = conversation.get(conversation.size() - 1).getContent();
        
        Result res = new Result(inputMessage, "[NULL]", conversation);

        // TODO: Optimise requests
        while (notRecieved) {
            try {
                // Create the HTTP POST request
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "Bearer " + apiKey);
                con.setRequestProperty("Content-Type", "application/json");
                // Build the request body with the entire conversation history
                StringBuilder requestBody = new StringBuilder("{\"model\": \"" + model + "\", \"messages\": [");


                // Add MAIN PROMPT to requestBody
                requestBody.append(AphidGPT.MAIN_PROMPT_REQUEST);

                // Add CONVERSATION HISTORY to requestBody
                for (Message message : conversation) {
                    requestBody.append("{\"role\": \"" + message.getRole() + "\", \"content\": \"" + sanitizeString(message.getContent()) + "\"},");
                }
                // Remove extra end comma and close the array
                requestBody.deleteCharAt(requestBody.length() - 1).append("]}");
        
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(requestBody.toString());
                writer.flush();
                writer.close();

                
                
                // Get the response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

        
                // Extract the content from the response
                
                String generatedResponse = extractContentFromResponse(response.toString());
        

                // Create result that will be returned. It contains the inputed message, ai response, and the conversation history so far.
                res = new Result(inputMessage, generatedResponse, conversation);
            

                notRecieved = false;
                return res;
        
            } // If timed out we need to run it again 
            catch (IOException e) {
                numOfAttempts += 1;
                System.out.println("[Timed out] Attempt: "+numOfAttempts);
                // If over the max number of attempts we exit and just return a fake result.
                if (numOfAttempts >= maxNumOfRetryAttempts) {
                    System.out.println("[WARNING] Exceeded max number of attempts. Returning failed result.");
                    res = new Result(inputMessage, "[TIMED OUT / IOException]", conversation);
                    notRecieved = false;
                    return res;
                }
                if (retryDelayMS != 0) {
                    waitTime(retryDelayMS);
                }
//                AphidGPT.LOGGER.warn("API KEY: " + AphidGPT.API_KEY); // Possible privacy issue since it would be printed in plain text to terminal
                AphidGPT.LOGGER.warn(String.valueOf(e));
//                throw new RuntimeException(e); // Remove this
            }

        }
        return res;
    }

    // This method extracts the response expected from chatgpt and returns it.
    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content") + 11; // Marker for where the content starts.
        int endMarker = response.indexOf("\"", startMarker); // Marker for where the content ends.
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }

    // Message class to represent a message in the conversation
    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    public static class Result {
        private final String input;
        private final String output;
        private final List<Message> history;

        public Result(String input, String output, List<Message> history) {
            this.input = input;
            this.output = output;            
            this.history = history;
        }
        
        public String getUserInput() {
            return input;
        }

        public String getAIResponse() {
            return output;
        }
        public List<Message> getConversationHistory() {
            return history;
        }
    }


    public static void waitTime(long milliseconds) {
        try {
            System.out.println("Waiting...");
            Thread.sleep(milliseconds);
            System.out.println("Waited.");
        } catch (InterruptedException e) {
            System.out.println("Error while waiting!");
        }
    }

    // TODO: Optimise to use dictionaries instead of lists/arrays
    public static List<Message> getPlayerConversation(String uuid, ArrayList<Player> PLAYERS) {
        for (int i = 0; i < PLAYERS.size(); i++) {

            if (Objects.equals(PLAYERS.get(i).getUUID(), uuid)) {
                return PLAYERS.get(i).getConversation();
            }
        }

        return null;

    }

    public static String sanitizeString(String string) {
        String regex = "/^[a-zA-Z0-9_-]{1,64}$/";
        String sanitized = string.replaceAll(regex, "-");
        String finalSan = sanitized.replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\"", "'");
        AphidGPT.LOGGER.info("Sanitized String: " + finalSan);
        return finalSan;
    }

}