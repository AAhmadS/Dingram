package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.Massage.Massage;
import Dingram.Models.Massage.PVMassage;
import Dingram.Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatPage extends Page{
    public static long id=0;
    private Long ID;
    private User user2;
    private Long user2Id;
    private Long userId;
    private List<Long> messages;
    private List<Long> unread;
    private List<Long> unread2;
    private Integer pointer=0;
    private boolean shown=false;

    public ChatPage(){}
    public ChatPage(User user, User user2,Page previousPage, LogicalAgent logicalAgent){
        super(user , previousPage , logicalAgent);
        this.user2=user2;
        this.user2Id=user2.getID();
        this.userId=user.getID();
        messages=new ArrayList<>();
        unread=new ArrayList<>();
        name=user.getIdentityName()+" and "+user2.getIdentityName()+" chat" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "previous page                                    : back\n" +
                      "instructions                                     : inst\n" +
                      "show chats                                       : show\n" +
                      "when showing chats, next and previous 5 messages : next , prev\n" +
                      "write a new message                              : message-your text\n" +
                      "delete a message                                 : delete-index of the message in the last 5 shown(from bottom to the top)";
        ID=id;
        id++;
        user.getChatsPages().add(this.ID);
        user2.getChatsPages().add(this.ID);
        logical.saveUser(user);
        logical.saveUser(user2);
    }

    public User getUser2() {
        return user2;
    }

    public List<Long> getMessages() {
        return messages;
    }

    public Integer getPointer() {
        return pointer;
    }

    public void setPointer(Integer pointer) {
        this.pointer = pointer;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public void addMessage(Massage massage){
        messages.add(massage.getID());
    }

    @Override
    public Page load(String string) {

        switch (string){
            case "back":
                shown=false;
                pointer=messages.size();
                logical.savePage(this);
                logical.saveUser(user);
                logical.saveUser(user2);
                pointer=0;
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
                   unread2.add(pvMassage.getID());
                   System.out.println("sent");
                   logical.notifyUser(user2Id,"user "+user.getIdentityName()+" sent you a message");
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

    public List<Long> getUnread() {
        return unread;
    }

    public void addUnread(Long id){
        unread.add(id);
        messages.add(id);
    }

    public void delete(String string){
        int index=pointer+Integer.valueOf(string.substring(7,8))-1;
        if (index<=messages.size()){
            if (logical.loadMessage(messages.get(index)).getUser().equals(user)){
                logical.deleteMessage(messages.get(index));
                messages.remove(index);
                logical.savePage(this);
                System.out.println("deleted successfully");
            }
            else System.out.println("you can't delete this message ,for not being yours");
        }
        else System.out.println("Invalid index");
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMessages(List<Long> messages) {
        this.messages = messages;
    }

    public void setUnread(List<Long> unread) {
        this.unread = unread;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public List<Long> getUnread2() {
        return unread2;
    }

    public void setUnread2(List<Long> unread2) {
        this.unread2 = unread2;
    }


}