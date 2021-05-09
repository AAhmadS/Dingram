package Models.Page;

import Logic.LogicalAgent;
import Models.Massage.Tweet;
import Models.User;

import java.time.LocalDateTime;

public class PersonalPage extends Page{
    private transient String info;
    private transient String secondaryInstructions;
    private  String profileName;

    public PersonalPage(User user, Page previousPage, LogicalAgent logicalAgent){
        super(user, previousPage, logicalAgent);
        Pages.add(new Tweets(user,this,logicalAgent));
        Pages.add(new Edit(user,this, logicalAgent));
        Pages.add(new Lists(user,this,logicalAgent));
        Pages.add(new Notifications(user,this,logicalAgent));
        name="personal page" +
                "\n------------";
        instructions= "INSTRUCTIONS : \n" +
                      "back to main page                    : back\n" +
                      "tweets and comments                  : tweets\n" +
                      "change personal information          : edit\n" +
                      "write a tweet                        : write-your text    (if you need write in next line, write : /n)\n" +
                      "followings, followers and black list : lists\n" +
                      "notifications                        : notifications\n" +
                      "info                                 : info\n" +
                      "instructions                         : inst";
        setInfo("set");
        setSecondaryInstructions("");
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = user.getName()+" "+user.getFamilyName()+"\n"+user.getBio();
        if (logical.getCurrentUser().equals(user)){
            info+="\nonline";
        }
        else {
            this.info += "\nlast seen ";
            switch (user.getLastSeenType()) {
                case 1:
                    info += user.getLastSeenTime().toString();
                    break;
                case 2:
                    if (user.getFollowers().contains(logical.getCurrentUser().getID()))
                        info += user.getLastSeenTime().toString();
                    else {
                        info += "recently";
                    }
                    break;
                default:
                    info += "recently";
            }
            if (logical.getCurrentUser().getFollowers().contains(user.getID())) {
                info += "\nfollowed";
            } else {
                if (!user.equals(logical.getCurrentUser())) info += "\nnot followed yet";
            }
        }
    }

    public String getSecondaryInstructions() {
        return secondaryInstructions;
    }

    public void setSecondaryInstructions(String secondaryInstructions) {
        this.secondaryInstructions = "INSTRUCTIONS :\n" +
                info +
                "\nprevious page    : back" +
                "\ntweets           : tweets" +
                "\ninfo             : info" +
                "\nreport and block : report" +
                "\nfollow           : request" +
                "\nblock            : block" +
                "\nunblock          : unblock" +
                "\nunfollow         : unfollow" +
                "\ninstructions     : inst";
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = user.getIdentityName()+" personal page" +
                "\n------------";
    }

    @Override
    public Page preLoad() {
        if (user.equals(logical.getCurrentUser())) {
            System.out.println(name+"\n"+instructions);
        }
        else{
            System.out.println(profileName+"\n"+secondaryInstructions);
        }
       return logical.secondaryLoad();
    }

    @Override
    public Page load(String userInput) {
        if (!user.equals(logical.getCurrentUser())){
            return loadSecondary(userInput);
        }
        switch (userInput){
            case"back":
                return previousPage;
            case "tweets":
                return Pages.get(0);
            case"edit":
                return Pages.get(1);
            case"lists":
                return Pages.get(2);
            case"notifications":
                return Pages.get(3);
            case"info":
                System.out.println(info);
                break;
            case "inst":
                System.out.println(instructions);
                break;
            default:
                if (userInput.matches("write-"+".+")){
                    String string=userInput.substring(6);
                    String[] stringss=string.split("/n");
                    StringBuilder strings= new StringBuilder();
                    for (String s:
                         stringss) {
                        strings.append(s).append("\n");
                    }
                    Tweet tweet=new Tweet(user, strings.toString(), LocalDateTime.now());
                    logical.saveMessage(tweet);
                    user.getTweets().add(tweet.getID());
                    logical.saveUser(user);
                    System.out.println("tweeted");
                }
                else{
                    System.out.println(INVALID);
                }
        }
        return this;
    }

    public Page loadSecondary(String in){
        switch (in){
            case"back":
                return null;
            case"tweets":
                return Pages.get(0);
            case"request":
                if (user.getBlackList().contains(logical.getCurrentUser().getID())){
                    System.out.println("you can't request following, you've been blocked");
                }
                else{
                    logical.notifyUser(user.getID(),"user :\n"+logical.getCurrentUser().getIdentityName()+"\nwants to follow you\nyou can either accept, reject or reject without notifying : ac, re, rew");
                    System.out.println("request successfully sent");
                }
                break;
            case"report":
                report();
                return null;
            case"block":
                block();
                break;
            case"unblock":
                unblock();
                break;
            case "unfollow":
                unfollow();
                break;
            case"inst":
                System.out.println(secondaryInstructions);
                break;
            case"info":
                System.out.println(info);
                break;
            default:
                System.out.println(INVALID);
        }
        return this;
    }

    public void report(){
        Long id=logical.getCurrentUser().getID();
        user.getFollowers().remove(id);
        user.getFollowing().remove(id);
        logical.getCurrentUser().getBlackList().add(user.getID());
        logical.getCurrentUser().getFollowers().remove(user.getID());
        logical.getCurrentUser().getFollowing().remove(user.getID());
        System.out.println("reported");
        logical.notifyUser(user.getID(),"user "+logical.getCurrentUser().getIdentityName()+" reported you");
    }
    public void block(){
        Long id=logical.getCurrentUser().getID();
        user.getFollowers().remove(id);
        logical.getCurrentUser().getBlackList().add(user.getID());
        logical.getCurrentUser().getFollowing().remove(user.getID());
        System.out.println("blocked");
        logical.notifyUser(user.getID(),"user "+logical.getCurrentUser().getIdentityName()+" blocked you");
    }
    public void unblock(){
        Long id=logical.getCurrentUser().getID();
        logical.getCurrentUser().getBlackList().remove(user.getID());
        System.out.println("unblocked");
        logical.notifyUser(user.getID(),"user "+logical.getCurrentUser().getIdentityName()+" unblocked you");

    }
    public void unfollow(){
        if (!user.getFollowers().contains(logical.getCurrentUser().getID())){
            System.out.println("you haven't followed yet");
        }
        else{
            user.getFollowers().remove(logical.getCurrentUser().getID());
            logical.getCurrentUser().getFollowing().remove(user.getID());
            String[] string=info.split("\n");
            string[string.length-1]="not followed yet";
            info="";
            for (String s: string) {
                info+=s+"\n";
            }
            System.out.println("unfollowed");
        }
    }
}
/**
 * pages we need
 * */
class Lists extends Page{
    private int pointer1=0;
    private boolean shown1=false;
    private int pointer2=0;
    private boolean shown2=false;
    private int pointer3=0;
    private boolean shown3=false;

    public Lists(User user, Page previousPage, LogicalAgent logicalAgent){
        super(user, previousPage, logicalAgent);
        name="Lists" +
                "\n------------";
        instructions= "INSTRUCTIONS : \n" +
                      "back to personal page                                      : back\n" +
                      "see followers                                              : followers\n" +
                      "see followings                                             : followings\n" +
                      "see black list                                             : black\n" +
                      "next or previous 5 on each list                            : next-lists name, prev-list name\n" +
                      "remove someone from each list                              : person name-list name\n" +
                      "see someone's personal page(must be in at least one least) : person name\n" +
                      "start following someone                                    : follow-person name\n" +
                      "instructions                                               : inst";
    }

    @Override
    public Page load(String in) {
        SWITCH: switch (in){
            case"back":
                shown1=false;
                shown2=false;
                shown3=false;
                pointer1=0;
                pointer2=0;
                pointer3=0;
                logical.saveUser(user);
                return previousPage;
            case"followers":
                showFollowers();
                break;
            case"followings":
                showFollowings();
                break;
            case"black":
                showBlack();
                break;
            case"inst":
                System.out.println(instructions);
                break;
            default:
                if (in.matches("next-"+"\\w+")){
                    String string=in.substring(5);
                    switch (string){
                        case"followers":
                            if (shown1){
                                nextShowFollowers();
                            }
                            else{
                                showFollowers();
                            }
                            break;
                        case"followings":
                            if (shown2){
                                nextShowFollowings();
                            }
                            else {
                                showFollowings();
                            }
                            break;
                        case"black":
                            if (shown3){
                                nextShowBlack();
                            }
                            else{
                                showBlack();
                            }
                            break;
                        default:
                            System.out.println(INVALID);
                    }
                    break;
                }
                if (in.matches("prev-"+"\\w+")){
                    String string=in.substring(5);
                    switch (string){
                        case"followers":
                            if (shown1){
                                prevShowFollowers();
                            }
                            else {
                                showFollowers();
                            }
                            break;
                        case"followings":
                            if (shown2){
                                prevShowFollowings();
                            }
                            else{
                                showFollowings();
                            }
                            break;
                        case"black":
                            if (shown3){
                                prevShowBlack();
                            }
                            else{
                                showBlack();
                            }
                            break;
                        default:
                            System.out.println(INVALID);
                    }
                    break;
                }
                if (in.matches("\\w+")){
                    int index=User.IdNames.indexOf(in);
                    if (index!=-1){
                        logical.primaryLoad(new PersonalPage(logical.loadUser((long) index),null,logical));
                        break ;
                    }
                    else System.out.println("user not found");
                    break;
                }
                if (in.matches("follow-"+"\\w+")){
                    String string=in.substring(7);
                    long id=(long)User.IdNames.indexOf(string);
                    if (id==-1){
                        System.out.println("no such user found");
                        break;
                    }
                    if (user.getFollowing().contains(id)){
                        System.out.println("you have already followed "+string);
                        break;
                    }
                    if (logical.loadUser(id).getBlackList().contains(user.getID())){
                        System.out.println("you can't request following, you've been blocked");
                        break;
                    }
                    logical.notifyUser(id,"user :\n"+user.getIdentityName()+"\nwants to follow you\nyou can either accept, reject or reject without notifying : ac, re, rew");
                    System.out.println("request sent successfully");
                    break;
                }
                if (in.matches("\\w+"+"-"+"\\w+")){
                    String[] strings=in.split("-");
                    long id=(long)User.IdNames.indexOf(strings[0]);
                    if (id==-1){
                        System.out.println("no such user found");
                        break ;
                    }
                    switch (strings[1]){
                        case"followers":
                            user.getFollowers().remove(id);
                            break SWITCH;
                        case"followings":
                            user.getFollowing().remove(id);
                            break SWITCH;
                        case"Black":
                            user.getBlackList().remove(id);
                            break SWITCH;
                    }
                    break ;
                }
                System.out.println(INVALID);
        }
        return this;
    }

    public void showFollowers(){
        pointer1=user.getFollowers().size();
        shown1=true;
        if (pointer1==0){
            shown1=false;
            System.out.println("no followers yet");
        }
        else{
            for (int i=pointer1-1;i>Math.max(pointer1-5,-1);i--){
                System.out.println(User.IdNames.get((int)((long) user.getFollowers().get(i))));
            }
            pointer1=Math.max(pointer1-5,0);
        }
    }

    public void nextShowFollowers(){
        if (pointer1==0) System.out.println("you reached the end");
        else{
            for (int i=pointer1-1;i>Math.max(pointer1-5,-1);i--){
                System.out.println(User.IdNames.get((int)((long) user.getFollowers().get(i))));
            }
            pointer1=Math.max(pointer1-5,0);
        }
    }

    public void prevShowFollowers(){
        pointer1=Math.min(pointer1+10, user.getFollowers().size());
        if (pointer1==0) System.out.println("no followers yet");
        for (int i=pointer1-1;i>Math.max(pointer1-5,-1);i--){
            System.out.println(User.IdNames.get((int)((long) user.getFollowers().get(i))));
        }
        pointer1=Math.max(pointer1-5,0);
    }

    public void showFollowings(){
        pointer2=user.getFollowing().size();
        shown2=true;
        if (pointer2==0){
            shown2=false;
            System.out.println("you haven't followed anyone yet");
        }
        else{
            for (int i=pointer2-1;i>Math.max(pointer2-5,-1);i--){
                System.out.println(User.IdNames.get((int)((long) user.getFollowing().get(i))));
            }
            pointer2=Math.max(pointer2-5,0);
        }
    }

    public void nextShowFollowings(){
        if (pointer2==0) System.out.println("you reached the end");
        else{
            for (int i=pointer2-1;i>Math.max(pointer2-5,-1);i--){
                System.out.println(User.IdNames.get((int)((long) user.getFollowing().get(i))));
            }
            pointer2=Math.max(pointer2-5,0);
        }
    }

    public void prevShowFollowings(){
        pointer2=Math.min(pointer2+10, user.getFollowing().size());
        if (pointer2==0) System.out.println("you haven't followed anyone yet");
        for (int i=pointer2-1;i>Math.max(pointer2-5,-1);i--){
            System.out.println(User.IdNames.get((int)((long) user.getFollowing().get(i))));
        }
        pointer2=Math.max(pointer2-5,0);
    }

    public void showBlack(){
        pointer3=user.getBlackList().size();
        shown3=true;
        if (pointer3==0){
            shown3=false;
            System.out.println("list is empty");
        }
        else{
            for (int i=pointer3-1;i>Math.max(pointer3-5,-1);i--){
                System.out.println(User.IdNames.get((int)((long) user.getBlackList().get(i))));
            }
            pointer3=Math.max(pointer3-5,0);
        }
    }

    public void nextShowBlack(){
        if (pointer3==0) System.out.println("you reached the end");
        else{
            for (int i=pointer3-1;i>Math.max(pointer3-5,-1);i--){
                System.out.println(User.IdNames.get((int)((long) user.getBlackList().get(i))));
            }
            pointer3=Math.max(pointer3-5,0);
        }
    }

    public void prevShowBlack(){
        pointer3=Math.min(pointer3+10, user.getFollowers().size());
        if (pointer3==0) System.out.println("list is empty");
        for (int i=pointer3-1;i>Math.max(pointer3-5,-1);i--){
            System.out.println(User.IdNames.get((int)((long) user.getBlackList().get(i))));
        }
        pointer3=Math.max(pointer3-5,0);
    }
}

class Edit extends Page{
    public Edit(User user,Page previousPage, LogicalAgent logicalAgent){
        super(user, previousPage, logicalAgent);
        name="edit page" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "back to personal page : back\n" +
                      "edit email            : 0-new email\n" +
                      "edit phone number     : 1-new phone number\n" +
                      "edit identity name    : 2-new id name\n" +
                      "edit password         : 3-new password\n" +
                      "edit bio              : 4-new bio\n" +
                      "instructions          : inst ";
    }

    @Override
    public Page load(String in) {
        if (in.equals("back")){
            logical.saveUser(user);
            return previousPage;
        }
        if (in.equals("inst")){
            System.out.println(instructions);
            return this;
        }
        if(in.matches("[0-4]"+"-"+".+")){
            String number=in.substring(0,1);
            String string=in.substring(2);
            switch (number){
                case"0":
                    setEmail(string);
                    break;
                case"1":
                    setPhone(string);
                    break;
                case"2":
                    setIdName(string);
                    break;
                case"3":
                    setPassword(string);
                    break;
                case"4":
                    setBio(string);
                    break;
                default:
                    System.out.println(INVALID);
            }
        }
        else{
            System.out.println(INVALID);
        }
        return this;
    }

    public void setEmail(String string){
        if (string.matches("\\S+"+"@"+"\\w+"+"\\."+"com")){
            if (User.emails.contains(string)){
                System.out.println("this email has already been picked , try another email");
            }
            else{
                User.emails.remove(user.getEmail());
                User.emails.add(string);
                user.setEmail(string);
                System.out.println("email has been successfully set to : "+string);
            }
        }
        else{
            System.out.println("invalid email");
        }
    }

    public void setPhone(String string){
        if (string.matches("[0]"+"[0-9]{10}")){
            if (User.phoneNumbers.contains(string)){
                System.out.println("this phone number has already been picked, try another phone number");
            }
            else{
                User.phoneNumbers.remove(user.getPhoneNumber());
                user.setPhoneNumber(string);
                User.phoneNumbers.add(string);
            }
        }
        else{
            System.out.println("invalid phone number");
        }
    }

    public void setPassword(String string){
        if (string.matches(secondaryPattern)){
            if (User.passwords.containsKey(string)){
                if (User.passwords.get(string)>Math.sqrt(User.getId())){
                    System.out.println("password is too weak, please try another password");
                }
                else{
                    User.passwords.replace(user.getPassword(),User.passwords.get(user.getPassword())-1);
                    User.passwords.replace(string,User.passwords.get(string)+1);
                    user.setPassword(string);
                    System.out.println("password has been successfully set to :"+string);
                }
            }
            else{
                User.passwords.replace(user.getPassword(),User.passwords.get(user.getPassword())-1);
                User.passwords.put(string,1);
                user.setPassword(string);
                System.out.println("password has been successfully set to :"+string);
            }
        }
        else{
            System.out.println("invalid password");
        }
    }

    public void setIdName(String string){
        if (string.matches("\\w+")){
            if (User.IdNames.contains(string)) System.out.println("this Id name has been already picked");
            else{
                User.IdNames.set(User.IdNames.indexOf(user.getIdentityName()),string);
                user.setIdentityName(string);
                System.out.println("Id name has been successfully set to : "+string);
            }
        }
        else{
            System.out.println("invalid Identity name");
        }
    }

    public void setBio(String string){
        user.setBio(string);
        System.out.println("bio has been successfully set to : "+string);
    }
}

class Notifications extends Page{
    private int pointer=0;
    private boolean shown=false;

    public Notifications(User user, Page previousPage, LogicalAgent logicalAgent){
        super(user, previousPage, logicalAgent);
        name="notification page" +
                "\n------------";
        instructions= "INSTRUCTIONS : \n" +
                      "back to personal page                                   : back\n" +
                      "show notifications                                      : show\n" +
                      "next or previous notification                           : next\n" +
                      "accept , reject or reject without informing the request : ac, re , rew\n" +
                      "if you go past a notification without reacting to the request(if being a request) we would reject it without informing automatically";
    }

    @Override
    public Page load(String in) {
        switch (in){
            case"back":
                shown=false;
                pointer=0;
                logical.saveUser(user);
                return previousPage;
            case"show":
                show();
                break;
            case"next":
                if (shown){
                    nextShow();
                }
                else{
                    show();
                }
                break;
            case"ac":
                accept();
                break;
            case"re":
                reject(true);
                break;
            case"rew":
                reject(false);
                break;
            default:
                System.out.println(INVALID);
        }
        return this;
    }

    public void show(){
        pointer=user.getNotifications().size()-1;
        shown=true;
        if (pointer==-1){
            shown=false;
            System.out.println("no notifications");
        }
        else{
            System.out.println(user.getNotifications().get(pointer));
            pointer--;
        }
    }

    public void nextShow(){

        if (pointer==-1) System.out.println("you reached the end");
        else{
            user.getNotifications().remove(pointer+1);
            System.out.println(user.getNotifications().get(pointer));
            pointer--;
        }
    }

    public void accept(){
        if (shown){
            String string=user.getNotifications().get(pointer+1);
            if (string.contains("ac")){
                String[] strings=string.split("\n");
                String idName=strings[1];
                long id=User.IdNames.indexOf(idName);
                user.getFollowers().add(id);
                logical.loadUser(id).getFollowing().add(user.getID());
                logical.notifyUser(id,"user "+user.getIdentityName()+" accepted your following request");
                System.out.println("request accepted");
            }else{
                System.out.println("this is just a notifying message , you cant accept anything");
            }
        }
        else{
            System.out.println("you should see a notification first");
        }
    }

    public void reject(boolean notify){
         if (shown){
             String string=user.getNotifications().get(pointer+1);
             if (string.contains("ac")){
                 String[] strings=string.split("\n");
                 String idName=strings[1];
                 long id=User.IdNames.indexOf(idName);
                 user.getFollowers().add(id);
                 if (notify==true) {
                     logical.notifyUser(id,"user "+user.getIdentityName()+" accepted your following request");
                     System.out.println("request rejected and notified");
                 }
                 System.out.println("rejected");
             }else{
                 System.out.println("this is just a notifying message , you cant reject anything");
             }
         }
         else{
             System.out.println("you should see a notification first");
         }
     }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
