package Models.Page;

import Logic.LogicalAgent;
import Models.Massage.Comment;
import Models.Massage.Tweet;
import Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeLine extends Page {
    private final transient List<Tweet> tweets;
    private final transient List<Tweet> queue;
    private final transient List<Integer> pointer;
    private transient boolean shown=false;
    public TimeLine(User user, Page previousPage, LogicalAgent logical){
        super(user,previousPage,logical);
        tweets=new ArrayList<>();
        name="Time line" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "back to main page or tweet from comments list : back\n" +
                      "show tweets                                   : show\n" +
                      "next or previous tweet                        : next, prev\n" +
                      "like or dislike tweet                         : like , dislike\n" +
                      "save to saved messages                        : save\n" +
                      "retweet                                       : retweet\n" +
                      "forward message to someone                    : forward-person name\n" +
                      "block author                                  : block\n" +
                      "mute author                                   : mute\n" +
                      "report author                                 : report\n" +
                      "visit author's personal page                  : personal\n" +
                      "comment on the tweet                          : comment-your text\n" +
                      "comments list                                 : commentslist\n" +
                      "instructions                                  : inst";
        queue = new ArrayList<>();
        pointer = new ArrayList<>();
        pointer.add(0,0);
    }

    @Override
    public Page preLoad() {
        for (long id: user.getFollowing()) {
            User sUser=logical.loadUser(id);
            if (sUser==null){
                continue;
            }
            for (long id2:
                 sUser.getTweets()) {
                Tweet tweet= logical.loadTweet(id2);
                if (tweet==null){
                    continue;
                }
                tweets.add(tweet);
            }
        }
        if (tweets.isEmpty()){
            System.out.println("no tweets available");
            return previousPage;
        }
        return super.preLoad();
    }

    @Override
    public Page load(String in) {
        switch (in){
            case"inst":
                System.out.println(instructions);
                break;
            case"report":
                report();
                break;
            case"block":
                block();
                break;
            case"personal":
                if (shown){
                    int point= pointer.get(pointer.size()-1);
                    Tweet tweet;
                    if (queue.size()==0){
                        tweet= tweets.get(point-1);
                    }else {
                        tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
                    }
                    logical.primaryLoad(new PersonalPage(tweet.getUser(),null,logical));
                }
                else {
                    System.out.println("you should see a tweet first");
                }
                break;
            case"retweet":
                retweet();
                break;
            case"save":
                save();
                break;
            case"like":
                like();
                break;
            case"dislike":
                dislike();
                break;
            case"show":
                show();
                break;
            case"prev":
                prevShow();
                break;
            case"next":
                nextShow();
                break;
            case"commentslist":
                showComments();
                break;
            case"back":
                if (queue.size()==0){
                    return previousPage;
                }
                else{
                    queue.remove(queue.size()-1);
                    pointer.remove(queue.size()+1);
                    prevShow();
                    return this;
                }
            default:
                if (in.matches("comment-"+"\\S{1,}")){
                    comment(in.substring(8));
                    break;
                }
                if (in.matches("forward-"+"\\w{1,}")){
                    forward(in.substring(8));
                    break;
                }
                System.out.println(INVALID);

        }
        return this;
    }

    public void show(){
        shown=true;
        if (tweets.isEmpty()){
            System.out.println("no tweets yet");
            shown=false;
            return;
        }
        int point= pointer.get(0);
        System.out.println(tweets.get(point));
        point++;
        pointer.set(0,point);
    }

    public void prevShow(){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            point-=2;
            if (point==-1){
                point=0;
                pointer.set(pointer.size()-1,point);
                System.out.println("you reached the bottom of the list, write back or next");
                return;
            }
            if (queue.size()==0){
                System.out.println(tweets.get(point).toString());
            }
            else{
                Tweet tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point));
                point++;
                pointer.set(pointer.size()-1,point);
                if (tweet!=null){
                    System.out.println(tweet.toString());
                }
                else{
                    prevShow();
                }
            }
        }
        else{
            show();
        }
    }

    public void nextShow(){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            if (queue.size()==0){
                if (point==tweets.size()){
                    System.out.println("end of the tweets list");
                }
                else {
                    System.out.println(tweets.get(point));
                    point++;
                    pointer.set(0,point);
                }
            }
            else{
                if (point==queue.get(queue.size()-1).getCommentList().size()){
                    System.out.println("end of the comments list");
                }
                else {
                    Tweet tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point));
                    point++;
                    pointer.set(queue.size(), point);
                    if (tweet!=null){
                        System.out.println(tweet.getCommentList().get(point-1).toString());
                    }
                    else{
                        nextShow();
                    }
                }
            }
        }
        else{
            show();
        }
    }

    public void like(){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            if (queue.size()==0){
                long id=logical.getCurrentUser().getID();
                if (tweets.get(point-1).getLikeList().contains(id)){
                    System.out.println("you've already liked this tweet");
                }
                else{
                    tweets.get(point-1).getLikeList().add(id);
                    tweets.get(point-1).getDislikeList().remove(id);
                    logical.saveMessage(tweets.get(point-1));
                    System.out.println("liked");
                }
            }
            else{
                Tweet tweet= logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
                if (tweet!=null){
                    long id=logical.getCurrentUser().getID();
                    if (tweet.getLikeList().contains(id)){
                        System.out.println("you've already liked this tweet");
                    }
                    else{
                        tweet.getLikeList().add(id);
                        tweet.getDislikeList().remove(id);
                        logical.saveMessage(tweet);
                        System.out.println("liked");
                    }
                }
            }
        }
        else{
            System.out.println("you should see a tweet first");
        }
    }

    public void dislike(){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            if (queue.size()==0){
                long id=logical.getCurrentUser().getID();
                if (tweets.get(point-1).getDislikeList().contains(id)){
                    System.out.println("you've already disliked this tweet");
                }
                else{
                    tweets.get(point-1).getDislikeList().add(id);
                    tweets.get(point-1).getLikeList().remove(id);
                    logical.saveMessage(tweets.get(point-1));
                    System.out.println("disliked");
                }
            }
            else{
                Tweet tweet= logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
                if (tweet!=null){
                    long id=logical.getCurrentUser().getID();
                    if (tweet.getDislikeList().contains(id)){
                        System.out.println("you've already disliked this tweet");
                    }
                    else{
                        tweet.getDislikeList().add(id);
                        tweet.getLikeList().remove(id);
                        logical.saveMessage(tweet);
                        System.out.println("disliked");
                    }
                }
            }
        }
        else{
            System.out.println("you should see a tweet first");
        }
    }

    public void retweet(){
        if (!shown){
            System.out.println("you should see a tweet first");
        }
        else{
            int point= pointer.get(pointer.size()-1);
            if (queue.size()==0) {
                logical.getCurrentUser().getTweets().add(tweets.get(point-1).getID());
            }
            else{
                logical.getCurrentUser().getTweets().add(queue.get(queue.size()-1).getCommentList().get(point-1));
            }
            logical.saveUser(logical.getCurrentUser());
            System.out.println("retweeted");
        }
    }

    public void forward(String string){
        if (!shown){
            System.out.println("you should see a tweet first");
        }
        else {
            int point = pointer.get(pointer.size() - 1);
            long tweetId;
            if (queue.size() == 0) {
                tweetId = tweets.get(point - 1).getID();
            } else {
                tweetId = queue.get(queue.size() - 1).getCommentList().get(point - 1);
            }
            int id = User.IdNames.indexOf(string);
            if (id == -1) {
                System.out.println("user not found");
            } else {

                if (user.getFollowing().contains((long)id)) {
                    int index = user.getChatsPagesByName().indexOf(string);
                    if (index!=-1) {
                        ChatPage chatPage = logical.loadPage(user.getChatsPages().get(index));
                        chatPage.getMessages().add(tweetId);
                        logical.notifyUser(chatPage.getUser2Id(), "user " + user.getIdentityName() + " sent you a message");
                        System.out.println("message sent");
                    } else {
                        System.out.println("first, you should start a chat with " + string);
                    }
                } else {
                    System.out.println("you should follow " + string + " first to message him");
                }
            }
        }
    }

    public void report(){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            Tweet tweet;
            if (queue.size()==0){
                tweet= tweets.get(point-1);
            }else {
                tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
            }
            User user=tweet.getUser();
            if (user!=null){
                Long id=logical.getCurrentUser().getID();
                user.getFollowers().remove(id);
                user.getFollowing().remove(id);
                logical.getCurrentUser().getBlackList().add(tweet.getUser().getID());
                logical.getCurrentUser().getFollowers().remove(tweet.getUser().getID());
                logical.getCurrentUser().getFollowing().remove(tweet.getUser().getID());
                logical.notifyUser(user.getID(),"user "+logical.getCurrentUser().getIdentityName()+" reported you");
                logical.saveUser(user);
                logical.saveUser(this.user);
            }
            System.out.println("reported");
        }
        else {
            System.out.println("you should see a tweet first");
        }
    }

    public void block(){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            Tweet tweet;
            if (queue.size()==0){
                tweet= tweets.get(point-1);
            }else {
                tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
            }
            User user=tweet.getUser();
            if (user!=null){
                Long id=logical.getCurrentUser().getID();
                user.getFollowers().remove(id);
                user.getFollowing().remove(id);
                logical.getCurrentUser().getBlackList().add(tweet.getUser().getID());
                logical.getCurrentUser().getFollowers().remove(tweet.getUser().getID());
                logical.getCurrentUser().getFollowing().remove(tweet.getUser().getID());
                logical.notifyUser(user.getID(),"user "+logical.getCurrentUser().getIdentityName()+" blocked you");
                logical.saveUser(user);
                logical.saveUser(this.user);
            }
            System.out.println("blocked");
        }
        else {
            System.out.println("you should see a tweet first");
        }
    }

    public void comment(String string){
        if (shown){
            int point= pointer.get(pointer.size()-1);
            Tweet tweet;
            if (queue.size()==0){
                tweet= tweets.get(point-1);
            }else {
                tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
            }
            Comment comment=new Comment(logical.getCurrentUser(),string, LocalDateTime.now(),tweet.getUser().getIdentityName(), tweet.getWritten());
            comment.setTweet(tweet.getID());
            tweet.getCommentList().add(comment.getID());
            logical.saveMessage(tweet);
            logical.saveMessage(comment);
            User user=tweet.getUser();
            if (user!=null){
                logical.notifyUser(user.getID(),"user : "+user.getIdentityName()+" commented on your tweet");
            }
            System.out.println("comment added");
        }else {
            System.out.println("you should see a tweet first");
        }
    }

    public void showComments(){
        if (!shown){
            System.out.println("you should see a tweet first");
        }
        else{
            int point= pointer.get(pointer.size()-1);
            Tweet tweet;
            if (queue.size()==0){
                tweet= tweets.get(point-1);
            }else {
                tweet=logical.loadTweet(queue.get(queue.size()-1).getCommentList().get(point-1));
            }
            if (tweet.getCommentList().size()==0){
                System.out.println("no comments for this tweet");
            }
            else {
                queue.add(tweet);
                pointer.add(1);
                System.out.println(logical.loadTweet(tweet.getCommentList().get(0)));
            }
        }
    }

    public void save(){
        if (!shown){
            System.out.println("you should see a tweet first");
        }
        else {
            int point = pointer.get(pointer.size() - 1);
            long tweetId;
            if (queue.size() == 0) {
                tweetId = tweets.get(point - 1).getID();
            } else {
                tweetId = queue.get(queue.size() - 1).getCommentList().get(point - 1);
            }
            user.getSavedMessages().add(tweetId);
            System.out.println("message saved");
        }
    }

}