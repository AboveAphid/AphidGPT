package com.aphidgpt.json;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class manage {

    public static void main(String[] args){
    }

    // Write Json to file
    public static void WriteJson(Object object, String filename) {
        Gson gson = new Gson();

        try {
            FileWriter fileWriter = new FileWriter(filename); // Create filewriter obj
            gson.toJson(object, fileWriter); // Use fileWriter object in gson to write object in file
            fileWriter.close(); // Close fileWriter otherwise it won't save.
        }catch (IOException e) {
            System.out.println("[AphidGPT-JsonManagement] Failed to write file: " + filename);
        }
    }


    public static ArrayList<Player> ReadPlayersJson(String filepath) {
        Gson gson = new Gson(); // Create Gson object so we can interact with json
        Type playersTypeToken = new TypeToken<ArrayList<Player>>(){}.getType(); // Get Player class type so that we can use it as a ArrayList of all players so that we can then loop for each value.

        try {
            FileReader fileReader = new FileReader(filepath); // Setup fileReader
            ArrayList<Player> returnVal = gson.fromJson(fileReader, playersTypeToken); // Read file and return a list of players
            fileReader.close(); // Close file so we don't lose data
            return returnVal; // Return list of players

        } catch (FileNotFoundException e) {
            System.out.println("[AphidGPT-JsonManagement] Failed to find file: " + filepath);
        } catch (IOException e) {
            System.out.println("[AphidGPT-JsonManagement] Failed to read file: " + filepath);
        }
        return new ArrayList<>();
    }


    public static ArrayList<Player> addPlayer (Player p, ArrayList<Player> players, String filepath) {
        if (players == null) {
            players = new ArrayList<>();
        }

        players.add(p);

        WriteJson(players, filepath);

        return players;
    }


}