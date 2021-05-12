package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.Massage.Tweet;
import Dingram.Models.User;

class Tweets extends Page{
    private boolean shown=false;
    private int pointer=0;

    public Tweets(User user , Page previousPage , LogicalAgent logicalAgent){
        super(user , previousPage ,logicalAgent);
        name="tweets list" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "back to previous page                     : back\n" +
                      "like or dislike a tweet (index represents the tweet index from bottom to the top in the last 5 shown) : like-index or dislike-index\n" +
                      "show tweets                               : show\n" +
                      "next or previous 5 tweets                 : next , prev\n" +
                      "instructions                              : inst";
    }
    @Override
    public Page load(String in) {
        switch (in){
            case"back":
                shown=false;
                pointer=0;
                logical.saveUser(logical.getCurrentUser());
                if (!logical.getCurrentUser().equals(user)) logical.saveUser(user);
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
            case"prev":
                if (shown){
                    prevShow();
                }
                else{
                    show();
                }
                break;
            case"inst":
                System.out.println(instructions);
                break;
            default:
                if (in.matches("like-"+"[1-5]")){
                    if (logical.getCurrentUser().equals(user)){
                        System.out.println("you cant like you own tweet");
                    }
                    else{
                        like(in);
                    }
                    break;
                }
                if (in.matches("dislike-"+"[1-5]")){
                    if (logical.getCurrentUser().equals(user)){
                        System.out.println("you cant dislike you own tweet");
                    }
                    else{
                        disLike(in);
                    }
                    break;
                }

        }
        return this;
    }

    public void show(){
        pointer=user.getTweets().size();
        shown=true;
        if (pointer==0){
            shown=false;
            System.out.println("no tweets yet");
        }
        else{
            for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
                Tweet tweet=logical.loadTweet(user.getTweets().get(i));
                if (tweet!=null){
                    System.out.println(tweet.toString());
                }
                else{
                    System.out.println("this tweet was deleted");
                }
            }
            pointer=Math.max(pointer-5,0);
        }
    }

    public void nextShow(){
        if (pointer==0) System.out.println("you reached the end");
        else{
            for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
                Tweet tweet=logical.loadTweet(user.getTweets().get(i));
                if (tweet!=null){
                    System.out.println(tweet.toString());
                }
                else{
                    System.out.println("this tweet was deleted");
                }
            }
            pointer=Math.max(pointer-5,0);
        }
    }

    public void prevShow(){
        pointer=Math.min(pointer+10, user.getTweets().size());
        if (pointer==0) System.out.println("no tweets yet");
        for (int i=pointer-1;i>Math.max(pointer-5,-1);i--){
            Tweet tweet=logical.loadTweet(user.getTweets().get(i));
            if (tweet!=null){
                System.out.println(tweet.toString());
            }
            else{
                System.out.println("this tweet was deleted");
            }
        }
        pointer=Math.max(pointer-5,0);
    }

    public void like(String string){
        int index=pointer+Integer.valueOf(string.substring(5,6))-1;
        if (index<=user.getTweets().size()){
            Tweet tweet=logical.loadTweet(user.getTweets().get(pointer));
            if (!tweet.getLikeList().contains(logical.getCurrentUser().getID())){
                tweet.getLikeList().add(logical.getCurrentUser().getID());
                tweet.getDislikeList().remove(logical.getCurrentUser().getID());
                logical.saveMessage(tweet);
                logical.notifyUser(user.getID(), "user :\n"+logical.getCurrentUser().getIdentityName()+"\nliked your tweet");
                System.out.println("liked");
            }
            else System.out.println("you've already liked this tweet");
        }
        else System.out.println("Invalid index");
    }

    public void disLike(String string){
        int index=pointer+Integer.valueOf(string.substring(8,9))-1;
        if (index<=user.getTweets().size()){
            Tweet tweet=logical.loadTweet(user.getTweets().get(pointer));
            if (!tweet.getDislikeList().contains(logical.getCurrentUser().getID())){
                tweet.getLikeList().remove(logical.getCurrentUser().getID());
                tweet.getDislikeList().add(logical.getCurrentUser().getID());
                logical.saveMessage(tweet);
                logical.notifyUser(user.getID(),"user :\n"+logical.getCurrentUser().getIdentityName()+"\ndisliked your tweet");
                System.out.println("disliked");
            }
            else System.out.println("you've already disliked this tweet");
        }
        else System.out.println("Invalid index");
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }
}

