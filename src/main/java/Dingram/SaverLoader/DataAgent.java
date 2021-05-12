package Dingram.SaverLoader;

import Dingram.Models.Massage.Comment;
import Dingram.Models.Massage.Massage;
import Dingram.Models.Massage.PVMassage;
import Dingram.Models.Massage.Tweet;
import Dingram.Models.Page.ChatPage;
import Dingram.Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataAgent {
    private static final String DEFAULT_IN_ADDRESS="./src/Dingram.main/resources/Dingram/Initialize";
    private static final String DEFAULT_USER_ADDRESS = "./src/Dingram.main/resources/Dingram/User";
    private static final String DEFAULT_PAGE_ADDRESS = "./src/Dingram.main/resources/Dingram/Pages";
    private static final String DEFAULT_MESSAGE_ADDRESS = "./src/Dingram.main/resources/Dingram/Message";
    private final File passwords,usernames,IdNames,emails,phones,userIDs,tweetsId,messageId,pvId,CommentId,chatPageId;
    public static final Logger logger= LogManager.getLogger(DataAgent.class);

    private final Gson gson;

    public DataAgent() throws IOException {
        gson=new GsonBuilder().setPrettyPrinting().create();

        passwords=new File(DEFAULT_IN_ADDRESS+"/User\\passwords.txt");
        if (!passwords.exists())passwords.createNewFile();

        usernames=new File(DEFAULT_IN_ADDRESS+"/User\\usernames.txt");
        if (!usernames.exists())usernames.createNewFile();

        IdNames=new File(DEFAULT_IN_ADDRESS+"/User\\IdNames.txt");
        if (!IdNames.exists())IdNames.createNewFile();

        emails=new File(DEFAULT_IN_ADDRESS+"/User\\emails.txt");
        if (!emails.exists())emails.createNewFile();

        phones=new File(DEFAULT_IN_ADDRESS+"/User\\phones.txt");
        if (!phones.exists())phones.createNewFile();

        userIDs=new File(DEFAULT_IN_ADDRESS+"/User\\Idu.txt");
        if (!userIDs.exists())userIDs.createNewFile();

        tweetsId=new File(DEFAULT_IN_ADDRESS+"/Tweet\\Idt.txt");
        if (!tweetsId.exists())tweetsId.createNewFile();

        messageId=new File(DEFAULT_IN_ADDRESS+"/Message\\Idm.txt");
        if (!messageId.exists())messageId.createNewFile();

        pvId=new File(DEFAULT_IN_ADDRESS+"/PV\\Idp.txt");
        if (!pvId.exists())pvId.createNewFile();

        CommentId=new File(DEFAULT_IN_ADDRESS+"/Comment\\Idc.txt");
        if (!CommentId.exists())CommentId.createNewFile();

        chatPageId=new File(DEFAULT_IN_ADDRESS+"/Chat\\Idh.txt");
        if (!chatPageId.exists())chatPageId.createNewFile();
    }

    public ChatPage loadPage(Long id) {
       // File Dingram.main=new File(DEFAULT_PAGE_ADDRESS+"/"+id);
        File pageFile=new File(DEFAULT_PAGE_ADDRESS+"/"+id+"\\Dingram.main"+".txt");
        if (!pageFile.exists()){ return null; }
        ChatPage chatPage=null;
        try {
            chatPage = gson.fromJson(new FileReader(pageFile), ChatPage.class);
            logger.info("page loaded successfully");
        } catch (FileNotFoundException e) {
            logger.error("failed to load page : "+id);
            e.printStackTrace();
        }
        return chatPage;
    }
    public User loadUser(Long id){
        //File Dingram.main=new File(DEFAULT_USER_ADDRESS+"/"+id);
        File userFile=new File(DEFAULT_USER_ADDRESS+"/"+id+"\\Dingram.main"+".txt");
        File userNote=new File(DEFAULT_USER_ADDRESS+"/"+id+"\\note"+".txt");
        if (!userFile.exists()){ return null; }
        User user= null;
        try {
            user = gson.fromJson(new FileReader(userFile), User.class);
            ArrayList<String> notification=gson.fromJson(new FileReader(userNote), ArrayList.class);
            user.setNotifications(notification);
            logger.info("user loaded successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("failed to load user : "+id);
        }
        return user;
    }
    public Massage loadMessage(Long id){
        if (PVMassage.PvMassageList.contains(id)){
            return loadPVMassage(id);
        }
        if (Tweet.tweets.contains(id)){
            if(Comment.commentsList.contains(id)){
                return loadComment(id);
            }
            else{
                return loadTweet(id);
            }
        }
        return null;
    }
    public Tweet loadTweet(Long id) {
        //File Dingram.main=new File(DEFAULT_MESSAGE_ADDRESS+"/Tweets/"+id);
        if (Comment.commentsList.contains(id)){
            return loadComment(id);
        }
        File tweetFile=new File(DEFAULT_MESSAGE_ADDRESS+"/Tweets/"+id+"\\Dingram.main"+".txt");
        if (!tweetFile.exists()){ return null; }
        Tweet tweet=null;
        try {
            tweet = gson.fromJson(new FileReader(tweetFile), Tweet.class);
            logger.info("page loaded successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("failed to load user : "+id);
        }
        return tweet;
    }
    public Comment loadComment(Long id){
        //File Dingram.main=new File(DEFAULT_MESSAGE_ADDRESS+"/Comment/"+id);
        File commentFile=new File(DEFAULT_MESSAGE_ADDRESS+"/Comment/"+id+"\\Dingram.main"+".txt");
        if (!commentFile.exists()){ return null; }
        Comment comment=null;
        try {
            comment = gson.fromJson(new FileReader(commentFile), Comment.class);
            logger.info("comment loaded successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("failed to load comment : "+id);
        }
        return comment;
    }
    public PVMassage loadPVMassage(Long id){
        //File Dingram.main=new File(DEFAULT_MESSAGE_ADDRESS+"/PVMessage/"+id);
        File messageFile=new File(DEFAULT_MESSAGE_ADDRESS+"/PVMessage/"+id+"\\Dingram.main"+".txt");
        if (!messageFile.exists()){ return null; }
        PVMassage pvMassage=null;
        try {
            pvMassage = gson.fromJson(new FileReader(messageFile), PVMassage.class);
            logger.info("pv message loaded successfully");
        } catch (FileNotFoundException e) {
            logger.error("failed to load pv message : "+id);
            e.printStackTrace();
        }
        return pvMassage;
    }

    public void saveUser(User user){
        File main=new File(DEFAULT_USER_ADDRESS+"/"+user.getID());
        File userFile=new File(DEFAULT_USER_ADDRESS+"/"+user.getID()+"\\Dingram.main"+".txt");
        File userNote=new File(DEFAULT_USER_ADDRESS+"/"+user.getID()+"\\note"+".txt");
        String userS= gson.toJson(user);
        String userNotification=gson.toJson(user.getNotifications());
        if (!main.exists()){
            main.mkdirs();
            try {
                userFile.createNewFile();
                userNote.createNewFile();
                logger.info("user files created successfully");
            } catch (IOException e) {
                logger.error("failed to create user files");
                System.out.println(e);
            }
        }
        if (main.listFiles()!=null){
            PrintStream printStream;
            try {
                printStream = new PrintStream(new FileOutputStream(userFile,false));
                printStream.println(userS);
                printStream.flush();
                printStream.close();
                PrintStream printStream1=new PrintStream(new FileOutputStream(userNote,false));
                printStream1.println(userNotification);
                printStream1.flush();
                printStream1.close();
                logger.info("user"+user.getID()+" saved successfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error("failed to save :"+user.getID());
            }
        }
    }
    public void savePage(ChatPage page){
        File main=new File(DEFAULT_PAGE_ADDRESS+"/"+page.getID());
        File pageFile=new File(DEFAULT_PAGE_ADDRESS+"/"+page.getID()+"\\Dingram.main"+".txt");
        String pageS= gson.toJson(page);
        if (!main.exists()){
            main.mkdirs();
            try {
                pageFile.createNewFile();
                logger.info("chat page files created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("failed to create chat page files");
            }
        }
        PrintStream printStream;
        try {
            printStream = new PrintStream(new FileOutputStream(pageFile,false));
            printStream.println(pageS);
            printStream.flush();
            printStream.close();
            logger.info("user"+page.getID()+" saved successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("failed to save :"+page.getID());
        }
    }
    public void saveMessage(Massage message){
        Long id=message.getID();
        if (PVMassage.PvMassageList.contains(id)){
            savePV((PVMassage) message);
        }
        if (Tweet.tweets.contains(id)){
            if(Comment.commentsList.contains(id)){
                saveComment((Comment)message);
            }
            else{
                saveTweet((Tweet)message);
            }
        }
    }
    public void savePV(PVMassage pvMassage){
        File main=new File(DEFAULT_MESSAGE_ADDRESS+"/PVMessage/"+pvMassage.getID());
        File pvMessageFile=new File(DEFAULT_MESSAGE_ADDRESS+"/PVMessage/"+pvMassage.getID()+"\\Dingram.main"+".txt");
        String messageS= gson.toJson(pvMassage);
        if (!main.exists()){
            main.mkdirs();
            try {
                pvMessageFile.createNewFile();
                logger.info("Pv message files created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("failed to create user files");
            }
        }
        if (main.listFiles()!=null){
            try {
                PrintStream printStream = new PrintStream(new FileOutputStream(pvMessageFile,false));
                printStream.println(messageS);
                printStream.flush();
                printStream.close();
                logger.info("user"+pvMassage.getID()+" saved successfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error("failed to save :"+pvMassage.getID());
            }
        }
    }
    public void saveTweet(Tweet tweet){
        if (Comment.commentsList.contains(tweet.getID())){
            saveComment((Comment)tweet);
            return;
        }
        File main=new File(DEFAULT_MESSAGE_ADDRESS+"/Tweets/"+tweet.getID());
        File tweetFile=new File(DEFAULT_MESSAGE_ADDRESS+"/Tweets/"+tweet.getID()+"\\Dingram.main"+".txt");
        String tweetS= gson.toJson(tweet);
        if (!main.exists()){
            main.mkdirs();
            try {
                tweetFile.createNewFile();
                logger.info("tweet files created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("failed to create tweet files");
            }
        }
        if (main.listFiles()!=null){
            PrintStream printStream;
            try {
                printStream = new PrintStream(new FileOutputStream(tweetFile,false));
                printStream.println(tweetS);
                printStream.flush();
                printStream.close();
                logger.info("user"+tweet.getID()+" saved successfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error("failed to save :"+tweet.getID());
            }
        }
    }
    public void saveComment(Comment comment){
        File main=new File(DEFAULT_MESSAGE_ADDRESS+"/Comment/"+comment.getID());
        File commentFile=new File(DEFAULT_MESSAGE_ADDRESS+"/Comment/"+comment.getID()+"\\Dingram.main"+".txt");
        String tweetS= gson.toJson(comment);
        if (!main.exists()){
            main.mkdirs();
            try {
                commentFile.createNewFile();
                logger.info("comment files created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("failed to create comment files");
            }
        }
        if (main.listFiles()!=null){
            PrintStream printStream;
            try {
                printStream = new PrintStream(new FileOutputStream(commentFile,false));
                printStream.println(tweetS);
                printStream.flush();
                printStream.close();
                logger.info("user"+comment.getID()+" saved successfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error("failed to save :"+comment.getID());
            }
        }
    }

    public void deleteUser(User user){
        File userFile=new File(DEFAULT_USER_ADDRESS+"/"+user.getID()+"\\Dingram.main"+".txt");
        File userNote=new File(DEFAULT_USER_ADDRESS+"/"+user.getID()+"\\note"+".txt");
        userFile.delete();
        userNote.delete();
        logger.info("user :"+user.getID()+"deleted");
    }
    public void deletePage(Long id) {
        File pageFile=new File(DEFAULT_PAGE_ADDRESS+"/"+id+"\\Dingram.main"+".txt");
        pageFile.delete();
        logger.info("chat page :"+id+"deleted");
    }
    public void deleteMessage(Long id){
        if (PVMassage.PvMassageList.contains(id)){
            deletePV(id);
        }
        if (Tweet.tweets.contains(id)){
            if(Comment.commentsList.contains(id)){
                deleteComment(id);
            }
            else{
                deleteTweet(id);
            }
        }
    }
    public void deleteTweet(Long id){
        File tweetFile=new File(DEFAULT_MESSAGE_ADDRESS+"/Tweets/"+id+"\\Dingram.main"+".txt");
        tweetFile.delete();
        logger.info("tweet :"+id+"deleted");
    }
    public void deleteComment(Long id){
        File commentFile=new File(DEFAULT_MESSAGE_ADDRESS+"/Comment/"+id+"\\Dingram.main"+".txt");
        commentFile.delete();
        logger.info("comment :"+id+"deleted");
    }
    public void deletePV(Long id){
        File pvMessageFile=new File(DEFAULT_MESSAGE_ADDRESS+"/PVMessage/"+id+"\\Dingram.main"+".txt");
        pvMessageFile.delete();
        logger.info("pv message :"+id+"deleted");
    }

    public void notify(Long id, String string){
        File userNote=new File(DEFAULT_USER_ADDRESS+"/"+id+"\\note"+".txt");
        try{
            List<String> note=gson.fromJson(new FileReader(userNote),ArrayList.class);
            note.add(string);
            String noted=gson.toJson(note);
            PrintStream printStream=new PrintStream(new FileOutputStream(userNote,false));
            printStream.println(noted);
            printStream.flush();
            printStream.close();
            logger.info("uesr :"+id+"notified : \n"+string);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            logger.error("failed to notify user : "+id+" due to being deleted or so");
        }
    }

    public void update(){
        try {
            FileWriter fileWriter=new FileWriter(passwords,false);
            fileWriter.write(gson.toJson(User.passwords));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(userIDs,false);
            fileWriter.write(gson.toJson(User.id));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(IdNames,false);
            fileWriter.write(gson.toJson(User.IdNames));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(usernames,false);
            fileWriter.write(gson.toJson(User.usernames));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(emails,false);
            fileWriter.write(gson.toJson(User.emails));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(phones,false);
            fileWriter.write(gson.toJson(User.phoneNumbers));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(tweetsId,false);
            fileWriter.write(gson.toJson(Tweet.tweets));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(messageId,false);
            fileWriter.write(gson.toJson(Massage.getId()));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(CommentId,false);
            fileWriter.write(gson.toJson(Comment.commentsList));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(pvId,false);
            fileWriter.write(gson.toJson(PVMassage.PvMassageList));
            fileWriter.flush();fileWriter.close();

            fileWriter=new FileWriter(chatPageId,false);
            fileWriter.write(gson.toJson(ChatPage.id));
            fileWriter.flush();fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize(){
        try {
            User.usernames=gson.fromJson(new FileReader(usernames),ArrayList.class);
            User.passwords=gson.fromJson(new FileReader(passwords), HashMap.class);
            User.IdNames=gson.fromJson(new FileReader(IdNames),ArrayList.class);
            User.emails=gson.fromJson(new FileReader(emails),ArrayList.class);
            User.phoneNumbers=gson.fromJson(new FileReader(phones),ArrayList.class);
            User.id=gson.fromJson(new FileReader(userIDs),Long.class);
            Tweet.tweets=gson.fromJson(new FileReader(tweetsId),ArrayList.class);
            Massage.setID(gson.fromJson(new FileReader(messageId),Long.class));
            PVMassage.PvMassageList=gson.fromJson(new FileReader(pvId),ArrayList.class);
            Comment.commentsList=gson.fromJson(new FileReader(CommentId),ArrayList.class);
            ChatPage.id=gson.fromJson(new FileReader(chatPageId),Long.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}