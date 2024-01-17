package com.aphidgpt.json;

import com.aphidgpt.gpt.call;

import java.util.List;

public class Player {
    private String uuid;
    private List<call.Message> conversation;

    public Player(String uuid, List<call.Message> conversation) {
        this.uuid = uuid;
        this.conversation = conversation;
    }

    public String getUUID () {
        return uuid;
    }

    public List<call.Message> getConversation () {
        return conversation;
    }

}
