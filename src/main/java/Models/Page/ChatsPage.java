package Models.Page;

import Logic.LogicalAgent;
import Models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsPage extends Page{
    private final List<String> chats;
    private boolean shown=false;
    private Integer pointer;
    private transient final List<Long> unreadChats;
    private transient final List<Long> tempoList;

    public ChatsPage(){
        chats=new ArrayList<>();
        unreadChats=new ArrayList<>();
        tempoList=new ArrayList<>();
    }
    public ChatsPage(User user, Page previousPage, LogicalAgent logicalAgent) {
        super(user, previousPage, logicalAgent);
        this.chats = new ArrayList<>();
        chats.addAll(user.getChatsPagesByName());
        this.unreadChats=new ArrayList<>();
        unreadChats.addAll(user.getUnreadChats());
        this.tempoList=new ArrayList<>();
        tempoList.addAll(user.getChatsPages());
        tempoList.removeAll(unreadChats);
        tempoList.addAll(unreadChats);
        Pages.add(new SavedMessages(user, this, logicalAgent));
        for (Long id: tempoList) {
            Pages.add(logical.loadPage(id));
        }
        name="chats page" +
                "\n------------";
        instructions= "INSTRUCTIONS:\n" +
                      "back to mainPage              : back\n" +
                      "saved messages                : saved\n" +
                      "next or previous 5 chat pages : next, prev\n" +
                      "first or latest chat pages    : show , last\n" +
                      "enter a chat page             : enter-person name\n" +
                      "start a chat                  : start-person name\n" +
                      "instructions                  : inst";
    }

    public List<Long> getUnreadChats() {
        return unreadChats;
    }

    public List<String> getChats() {
        return chats;
    }

    @Override
    public Page preLoad() {
        if (!unreadChats.isEmpty()){
            instructions+="\n------------" +
                    "\nyou have "+unreadChats.size()+" chats with new messages";
        }
        return super.preLoad();
    }

    @Override
    public Page load(String string) {

        switch (string) {
            case "back":
                shown = false;
                pointer = tempoList.size();
                return previousPage;
            case "inst":
                System.out.println(instructions);
                break;
            case "saved":
                shown = false;
                pointer = tempoList.size();
                return Pages.get(0);
            case "show":
                show();
                break;
            case "next":
                if (shown) {
                    nextShow();
                } else show();
                break;
            case "prev":
                if (shown) {
                    prevShow();
                } else show();
                break;
            case "last":
                pointer = -5;
                prevShow();
                break;
            default:
                if (string.matches("enter-" + "\\w+")) {
                    int index = chats.indexOf(string.substring(6));
                    if (index == -1) {
                        System.out.println(INVALID +": you don't have a chat with this user");
                        return this;
                    } else {
                        ChatPage chatPage= (ChatPage) Pages.get(index + 1);
                        if (!chatPage.user.equals(user)){
                            chatPage.setUser2(chatPage.user);
                            chatPage.setUser(user);
                            chatPage.setUser2Id(chatPage.getUserId());
                            chatPage.setUserId(user.getID());
                            List<Long> unread=chatPage.getUnread();
                            chatPage.setUnread(chatPage.getUnread2());
                            chatPage.setUnread2(unread);
                        }
                        return chatPage;
                    }
                }
                if (string.matches("start-" + "\\w+")) {
                    int index = chats.indexOf(string.substring(6));
                    if (index == -1) {
                        User user2=logical.loadUser((long) User.IdNames.indexOf(string.substring(6)));
                        if (user2==null){
                            System.out.println("no such user found");
                            return this;
                        }
                       else {
                           if (user2.isActive()){
                               ChatPage chatPage=new ChatPage(user,user2,this,logical);
                               logical.savePage(chatPage);
                               Pages.add(chatPage);
                               return Pages.get(Pages.size()-1);
                           }
                           else{
                               System.out.println("currently, user in inactive , you cannot start a chat");
                           }
                        }

                    }
                    else {
                        System.out.println("chat already exists");
                    }
                }
                else {
                    System.out.println(INVALID);
                }
        }
        return this;
    }

    public void show(){
        pointer=tempoList.size();
        shown=true;
        if (pointer==0){
            shown=false;
            System.out.println("no chats yet, start chatting like shown in instructions");
        }
        else{
            for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
                int index= user.getChatsPages().indexOf(tempoList.get(i))+1;
                System.out.println(chats.get(index-1)+", new messages : "+((ChatPage)(Pages.get(i+1))).getUnread().size());
            }
            pointer=Math.max(pointer-5,0);
        }
    }

    public void nextShow(){
        if (pointer==0) System.out.println("you reached the last chat, do as shown in instructions");
        else{
            for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
                int index= user.getChatsPages().indexOf(tempoList.get(i))+1;
                System.out.println(chats.get(index-1)+", new messages : "+((ChatPage)(Pages.get(i))).getUnread().size());
            }
            pointer=Math.max(pointer-5,0);
        }
    }

    public void prevShow(){
        pointer=Math.min(pointer+10, tempoList.size());
        if (pointer==0) System.out.println("no chats yet, start messaging like shown in instructions");
        for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
            int index= user.getChatsPages().indexOf(tempoList.get(i))+1;
            System.out.println(chats.get(index-1)+", new messages : "+((ChatPage)(Pages.get(i))).getUnread().size());
        }
        pointer=Math.max(pointer-5,0);
    }

    public void delete(String string){
        int index=chats.indexOf(string);
        if(index==-1){
            System.out.println(INVALID);
        }
        else{
            tempoList.remove(index);
            chats.remove(string);
            user.getChatsPages().remove(index);
            user.getChatsPagesByName().remove(string);
            if (unreadChats.contains(index))unreadChats.remove(index);
            Pages.remove(index+1);
        }
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public Integer getPointer() {
        return pointer;
    }

    public void setPointer(Integer pointer) {
        this.pointer = pointer;
    }

    public List<Long> getTempoList() {
        return tempoList;
    }
}
