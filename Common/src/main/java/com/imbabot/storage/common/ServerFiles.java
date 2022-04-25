package com.imbabot.storage.common;

import java.util.List;

public class ServerFiles extends AbstractMessage {

    private List<String> list;

    public ServerFiles(List list){
        this.list = list;
    }

    public List<String> getList(){
        return list;
    }

}
