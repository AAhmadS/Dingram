package Dingram.Logic;

import Dingram.Models.Massage.Massage;
import Dingram.Models.Massage.Tweet;
import Dingram.Models.Page.ChatPage;
import Dingram.Models.Page.Page;
import Dingram.Models.Page.SignInPage;
import Dingram.Models.User;
import Dingram.SaverLoader.DataAgent;

import java.io.IOException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogicalAgent {
    private SignInPage signInPage;
    private Scanner scanner;
    private Page currentPage;
    private User currentUser;
    private final DataAgent dataAgent;
    public static final Logger logger= LogManager.getLogger(LogicalAgent.class);

    public LogicalAgent() throws IOException {
        scanner=new Scanner(System.in);
        dataAgent=new DataAgent();
        signInPage=new SignInPage(null,null,this);
        currentPage=signInPage;
    }
    public void start(){
        logger.info("Dingram started");
        initialize();
        primaryLoad(null);
    }
    public void primaryLoad(Page page){
        if (page==null){
            while (currentPage!=null){
                logger.info("user entered a new page");
                currentPage=currentPage.preLoad();
            }
            update();
            logger.info("program ends");
        }
        else{
            while (page!=null){
                logger.info("user entered a new page of other users");
                page=page.preLoad();
            }
            System.out.println("back to your pages, for instructions : inst");
        }
    }
    public Page secondaryLoad(){
        Page nextPage;
        nextPage=currentPage;
        while(currentPage.equals(nextPage)){
            String string=scanner.nextLine();
            logger.info("user input to be : "+string);
            nextPage=currentPage.load(string);
            if (nextPage==null)break;
        }
        return nextPage;
    }

    public Page getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User loadUser(Long id){
        return dataAgent.loadUser(id);
    }
    public Tweet loadTweet(Long id){ return dataAgent.loadTweet(id);}
    public Massage loadMessage(Long id){ return dataAgent.loadMessage(id); }
    public ChatPage loadPage(Long id) {
        return dataAgent.loadPage(id);
    }

    public void saveMessage (Massage massage){
        dataAgent.saveMessage(massage);
        update();
    }
    public void saveUser(User user){
        dataAgent.saveUser(user);
        update();
    }
    public  void savePage(ChatPage chatPage){
        dataAgent.savePage(chatPage);
        update();
    }

    public void deleteUser(){
        logger.info("user :"+currentUser.getIdentityName()+" deleted his account");
        dataAgent.deleteUser(currentUser);
        User.IdNames.set(Math.toIntExact(currentUser.getID()),"deleted user "+currentUser.getID());
        User.passwords.replace(currentUser.getPassword(),User.passwords.get(currentUser.getPassword())-1);
        if (currentUser.getPhoneNumber()!=null)User.phoneNumbers.remove(currentUser.getPhoneNumber());
        User.usernames.remove(currentUser.getUsername());
        User.emails.remove(currentUser.getEmail());
        update();
        currentUser=null;
    }
    public void deleteMessage(Long id){
        dataAgent.deleteMessage(id);
        update();
    }
    public void deletePage(Long id) {
        dataAgent.deletePage(id);
        update();
    }

    public void update(){
        dataAgent.update();
        logger.info("static fields updated");
    }
    public void initialize(){
        logger.info("static lists initialized");
        dataAgent.initialize();
    }
    public void notifyUser(Long id, String s) {
        dataAgent.notify(id,s);
        update();
    }
}
