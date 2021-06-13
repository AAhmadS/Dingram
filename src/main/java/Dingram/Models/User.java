package Dingram.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class User {
    public static List<String> usernames=new ArrayList<>();
    public static HashMap<String,Integer> passwords=new HashMap<>();
    public static List<String> IdNames=new ArrayList<>();
    public static List<String> emails=new ArrayList<>();
    public static List<String> phoneNumbers=new ArrayList<>();
    public static long id=0;
    private Long Id;
    private String username;
    private String name;
    private String familyName;
    private String phoneNumber;
    private Date birthDay;
    private String password;
    private String email;
    private String identityName;
    private String bio;
    private int lastSeenType;
    private String verificationQuestion;
    private boolean privacy;
    private boolean active;
    private LocalDate localDate;
    private List<Long> tweets;
    private List<Long> comments;
    private List<Long> following;
    private List<Long> followers;
    private List<Long> blackList;
    private List<String> notifications;
    private List<Long> savedMessages;
    private List<Long> chatsPages;
    private List<Long> unreadChats;
    private List<String> chatsPagesByName;
    private LocalDateTime lastSeenTime;

    public User(){}

    public User(String username, String password, String email, String identityName, String verificationQuestion, String name, String familyName, Date birthDay) {
        this.username=username;
        usernames.add(username);
        if (passwords.containsKey(password)){
            passwords.replace(password,passwords.get(password)+1);
        }
        else {
            passwords.put(password,1);
        }
        this.password=password;
        this.email=email;
        emails.add(email);
        this.identityName=identityName;
        IdNames.add(identityName);
        this.verificationQuestion = verificationQuestion;
        this.name=name;
        this.familyName=familyName;
        this.birthDay=birthDay;
        this.savedMessages = new ArrayList<>();
        this.chatsPages=new ArrayList<>();
        this.chatsPagesByName=new ArrayList<>();
        this.unreadChats=new ArrayList<>();
        this.following=new ArrayList<>();
        this.followers=new ArrayList<>();
        this.blackList=new ArrayList<>();
        this.notifications=new ArrayList<>();
        this.tweets=new ArrayList<>();
        this.comments=new ArrayList<>();
        bio="";
        Id = id;
        id++;
        tweets=new ArrayList<>();
        comments=new ArrayList<>();
        blackList=new ArrayList<>();
        notifications = new ArrayList<>();
        following = new ArrayList<>();
        followers=new ArrayList<>();
        localDate=LocalDate.now();
        lastSeenType=1;
        active=true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Long> getSavedMessages() {
        return savedMessages;
    }

    public static class UserBuilder{
        String username;
        String password;
        String email;
        String identityName;
        String verificationQuestion;
        String name;
        String familyName;
        String phoneNumber;
        String bio;
        Date birthday;


        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setIdentityName(String identityName) {
            this.identityName = identityName;
            return this;
        }

        public UserBuilder setVerificationQuestion(String verificationQuestion) {
            this.verificationQuestion = verificationQuestion;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getIdentityName() {
            return identityName;
        }

        public String getVerificationQuestion() {
            return verificationQuestion;
        }

        public String getName() {
            return name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public String getFamilyName() {
            return familyName;
        }

        public UserBuilder setFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public UserBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public String getBio() {
            return bio;
        }

        public UserBuilder setBio(String bio) {
            this.bio = bio;
            return this;
        }

        public User build(){
            User user=new User(username,password,email,identityName, verificationQuestion, name, familyName, birthday);
            if (bio!=null)user.setBio(bio);
            if (phoneNumber!=null){
                user.setPhoneNumber(phoneNumber);
                phoneNumbers.add(phoneNumber);
            }
            username=null;
            password=null;
            email=null;
            verificationQuestion=null;
            phoneNumber=null;
            name=null;
            familyName=null;
            bio=null;
            identityName=null;
            birthday=null;
            return user;
        }
    }

    public static long getId() {
        return id;
    }

    public Long getID(){return  Id;}

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getVerificationQuestion() {
        return verificationQuestion;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public List<Long> getTweets() {
        return tweets;
    }

    public List<Long> getComments() {
        return comments;
    }

    public List<Long> getFollowing() {
        return following;
    }

    public List<Long> getFollowers() {
        return followers;
    }

    public List<Long> getBlackList() {
        return blackList;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public int getLastSeenType() {
        return lastSeenType;
    }

    public void setLastSeenType(int lastSeenType) {
        this.lastSeenType = lastSeenType;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public static void setId(long id) {
        User.id = id;
    }

    public LocalDateTime getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(LocalDateTime lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public void setVerificationQuestion(String verificationQuestion) { this.verificationQuestion = verificationQuestion; }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setTweets(List<Long> tweets) {
        this.tweets = tweets;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public void setFollowing(List<Long> following) {
        this.following = following;
    }
    public void setFollowers(List<Long> followers) {
        this.followers = followers;
    }

    public void setBlackList(List<Long> blackList) {
        this.blackList = blackList;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public void setSavedMessages(List<Long> savedMessages) {
        this.savedMessages = savedMessages;
    }

    public List<Long> getChatsPages() {
        return chatsPages;
    }
    public void setChatsPages(List<Long> chatsPages) {
        this.chatsPages = chatsPages;
    }

    public List<String> getChatsPagesByName() {
        return chatsPagesByName;
    }
    public void setChatsPagesByName(List<String> chatsPagesByName) {
        this.chatsPagesByName = chatsPagesByName;
    }

    public List<Long> getUnreadChats() {
        return unreadChats;
    }
    public void setUnreadChats(List<Long> unreadChats) {
        this.unreadChats = unreadChats;
    }

}