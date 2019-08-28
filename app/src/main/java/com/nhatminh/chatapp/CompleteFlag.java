package com.nhatminh.chatapp;

import java.util.ArrayList;

public class CompleteFlag {
    ArrayList<Boolean> isComplete;


    public CompleteFlag(ArrayList<Boolean> isComplete) {
        this.isComplete = isComplete;
    }


    public CompleteFlag() {
        isComplete = new ArrayList<>();
    }


    public void addFlag(boolean isComplete){
        this.isComplete.add(isComplete);
    }

    public void setOneFlagComplete(){
        for(int i=0;i<isComplete.size();i++){
            if (!isComplete.get(i)){
                isComplete.set(i, true);
                return;
            }
        }
    }


    public boolean isAllFlagComplete(){
        for(int i=0;i<isComplete.size();i++){
            if (!isComplete.get(i)){
                return false;
            }
        }
        return true;

    }


}
