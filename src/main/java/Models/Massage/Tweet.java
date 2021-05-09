package Models.Massage;


import Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tweet extends Massage{
    public static List<Long> tweets=new ArrayList<>();
    private List<Long> commentList;
    private List<Long> likeList;
    private List<Long> dislikeList;

    public Tweet(){}

    public Tweet(User user, String written, LocalDateTime localDateTime) {
        super(user, written, localDateTime);
        commentList=new ArrayList<>();
        tweets.add(this.getID());
    }

    public List<Long> getCommentList() {
        return commentList;
    }

    public List<Long> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Long> likeList) {
        this.likeList = likeList;
    }

    public List<Long> getDislikeList() {
        return dislikeList;
    }

    public void setDislikeList(List<Long> dislikeList) {
        this.dislikeList = dislikeList;
    }

    public void setCommentList(List<Long> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "Tweet" +
                "\nauthor : "+user.getIdentityName() +
                "\n{" +written+
                "}\nwritten in : "+localDateTime.toLocalDate().toString()+
                "\n------------------------------";
    }
}
