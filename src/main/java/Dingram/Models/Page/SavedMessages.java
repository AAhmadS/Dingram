package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.Massage.Massage;
import Dingram.Models.Massage.PVMassage;
import Dingram.Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SavedMessages extends Page{
    private final List<Long> messages;
    private Integer pointer;
    private boolean shown=false;

    public SavedMessages(){
        messages=new ArrayList<>();
    }
    public SavedMessages(User user, Page previousPage, LogicalAgent logical){
        super(user, previousPage, logical);
        messages=new ArrayList<>();
        name="saved messages" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "previous page                                    : back\n" +
                      "instructions                                     : inst\n" +
                      "show chats                                       : show\n" +
                      "when showing chats, next and previous 5 messages : next , prev\n" +
                      "write a new message                              : message-your text ";
        pointer=0;
        messages.addAll(user.getSavedMessages());
    }

    public List<Long> getMessages() {
        return messages;
    }

    @Override
    public Page load(String string) {

        switch (string){
            case "back":
                shown=false;
                pointer=messages.size();
                return previousPage;
            case"inst":
                System.out.println(instructions);
                break;
            case"show":
                show();
                break;
            case"next":
                if (shown){
                    nextShow();
                }
                else show();
                break;
            case"prev":
                if (shown){
                    prevShow();
                }
                else show();
                break;
            case "first":
                pointer=-5;
                prevShow();
                break;
            default:
                if (string.matches("message-"+".+")){
                    PVMassage pvMassage=new PVMassage(user,string.substring(8), LocalDateTime.now());
                    logical.saveMessage(pvMassage);
                    messages.add(pvMassage.getID());
                    user.getSavedMessages().add(pvMassage.getID());
                    logical.saveUser(user);
                    System.out.println("message added");
                }
                else{
                    if (string.matches("delete-"+"[1-5]")){
                        delete(string);
                    }
                    else{
                        System.out.println(INVALID);
                    }
                }
        }
        return this;
    }

    public void show(){
        pointer=messages.size();
        shown=true;
        if (pointer==0){
            shown=false;
            System.out.println("no messages yet, start messaging like shown in instructions");
        }
        else{
            for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
                Massage massage=logical.loadMessage(messages.get(i));
                if (massage!=null)System.out.println(massage.toString());
                else{
                    System.out.println("this message has been deleted");
                }
            }
            pointer=Math.max(pointer-5,0);
        }
    }

    public void nextShow(){
        if (pointer==0) System.out.println("you reached the first message, do as shown in instructions");
        else{
            for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
                Massage massage=logical.loadMessage(messages.get(i));
                if (massage!=null)System.out.println(massage.toString());
                else{
                    System.out.println("this message has been deleted");
                }
            }
            pointer=Math.max(pointer-5,0);
        }
    }

    public void prevShow(){
        pointer=Math.min(pointer+10, messages.size());
        if (pointer==0) System.out.println("no messages yet, start messaging like shown in instructions");
        for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
            Massage massage=logical.loadMessage(messages.get(i));
            if (massage!=null)System.out.println(massage.toString());
            else{
                System.out.println("this message has been deleted");
            }
        }
        pointer=Math.max(pointer-5,0);
    }

    public void delete(String string){
        int index=pointer+Integer.parseInt(string.substring(7,8))-1;
        if (index<=messages.size()){
            if (logical.loadMessage(messages.get(index)).getUser().equals(user)){
                logical.deleteMessage(messages.get(index));
            }
            messages.remove(index);
            user.getSavedMessages().remove(index);
            logical.saveUser(user);
            System.out.println("removed");
        }
        else System.out.println("Invalid index");
    }
}
