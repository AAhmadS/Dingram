package Models.Massage;

import Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Comment extends Tweet {
    public static List<Long> commentsList=new ArrayList<>();
    private Long tweet;
    private String tweetWritten;
    private String tweetAuthor;

    public Comment(){}

    public Comment(User user, String written, LocalDateTime date,String tweetAuthor, String tweetWritten) {
        super(user, written, date);
        this.tweetAuthor=tweetAuthor;
        this.tweetWritten=tweetWritten;
        commentsList.add(this.getID());
    }

    public Long getTweet() {
        return tweet;
    }

    public void setTweet(Long tweet) {
        this.tweet = tweet;
    }

    @Override
    public String toString() {
        return "comment to : {" +tweetWritten+"}"+
                "\nfrom author : "+tweetAuthor+
                "\nauthor : "+user.getIdentityName() +
                "\n{" +written+
                "}\nwritten in : "+localDateTime.toLocalDate().toString()+
                "\n------------------------------";
    }
}
