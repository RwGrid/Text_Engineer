package com.example.mymlapp.MessagingClasses;

public class CustomMessageEvent {
    private String customMessage;
    // step 1 in event bus is to implement library in gradle
    // step 2 is to create this class, which is the event we want to pass
    // now In our Listening class , where we want to listen to any event(with data) sent to the class we want
    //in my case it is the summarize fragment where i want to send the text i scraped from the desired website
    // and get it from the Main activity, so i must implement a listener in the
    // step 3 is to put this ' EventBus.getDefault().register(this);' in the place u want to send the  data to(summarize fragment)
    //step 4 is to put in summarize fragment , what type of event are u subscribing to ' @Subscribe onEvent(CustomMessageEvent event)
   //step 5 is to send data from Main activity to the summarize fragment
    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
