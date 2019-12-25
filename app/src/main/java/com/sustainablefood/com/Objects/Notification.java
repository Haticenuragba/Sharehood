package com.sustainablefood.com.Objects;

public class Notification {
    public String id;
    public String name;
    public String phone;
    public String image;
    public String guestId;
    public String locationId;
    public int status;

    public boolean isReceived(){
        if(this.status == 0){
            return true;
        }
        else{
            return false;
        }
    }
}
