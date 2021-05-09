package Models.Massage;

import Models.User;

import java.time.LocalDateTime;

public abstract class Massage {
    protected static long id=0;
    protected long Id;
    protected User user;
    protected long userId;
    protected String written;
    protected LocalDateTime localDateTime;

    public Massage(){}

    public Massage(User user, String written, LocalDateTime localDateTime) {
        Id=id+1;
        id++;
        this.user = user;
        this.userId= user.getID();
        this.written = written;
        this.localDateTime = localDateTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static long getId() { return id; }

    public static void setID(long iD){id=iD;}

    public long getID(){ return Id; }

    public User getUser() {
        return user;
    }

    public String getWritten() {
        return written;
    }

    public void setWritten(String written) {
        this.written = written;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "\nauthor = " + user.getIdentityName()+
                "\n" + written +
                "\nwritten in =" + localDateTime.toLocalDate().toString() +
                "\n}";
    }
}
