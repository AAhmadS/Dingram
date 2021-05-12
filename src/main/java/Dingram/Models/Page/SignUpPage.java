package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.User;

import java.sql.Date;

public class SignUpPage extends LogPage {
    private User.UserBuilder userBuilder;
    private final String emailPattern="\\S{1,}"+"@"+"\\w{1,}"+"\\."+"com";
    private final String phoneNumberPattern="0"+"\\d{10}";
    private final String birthPattern="\\d{2}"+"/"+"\\d{2}"+"/"+"\\d{4}";
    private final String namePattern="[A-Z]"+"[a-z]{0,}";

    public SignUpPage(){}

    public SignUpPage(User user, Page previousPage , LogicalAgent logicalAgent) {
        super(user , previousPage ,logicalAgent);
        userBuilder=new User.UserBuilder();
        name="Sign Up Page"+
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "for returning to signInPage enter                    : 00-sign\n" +
                      "for entering username, write down                    : 01-your text\n" +
                      "for entering password, write down                    : 02-your text\n" +
                      "for entering name, write down                        : 03-your text\n" +
                      "for entering familyname, write down                  : 04-your text\n" +
                      "for entering IdName, write down                      : 05-your text\n" +
                      "for entering secret info, write down                 : 06-your text\n" +
                      "for entering birthday, write down                    : 07-day/month/year\n" +
                      "for entering bio, write down                         : 08-your text\n" +
                      "for creating account, write down                     : 09-account\n" +
                      "for getting a report write down                      : 10-report\n" +
                      "for entering email, write down                       : 11-your text\n" +
                      "for entering phone number, write down                : 12-your text\n" +
                      "and if you want to see instructions again write down : inst\n" +
                      "Attention:\n" +
                      "Username, IdName and password must only contain 0-9,a-z and A-Z characters, and at least 6 characters\n" +
                      "Also care about capital letters when entering your name and family name";
    }

    @Override

    public Page load(String in) {
        if (in.equals("inst")) {
            System.out.println(instructions);
            return this;
        }
        if (in.length()>3){
            String number=in.substring(0,2);
            String string=in.substring(3);
            if (number.matches(pattern)){
                switch (number) {
                    case "00":
                        if (!(string .equals("sign"))) System.out.println(INVALID);
                        else {
                            return previousPage;
                        }
                        break;
                    case "01":
                        setUsername(string);
                        break;
                    case "02":
                        setPassword(string);
                        break;
                    case "03":
                        setNameUser(string);
                        break;
                    case "04":
                        setFamilyName(string);
                        break;
                    case "05":
                        setIdName(string);
                        break;
                    case "06":
                        setVerification(string);
                        break;
                    case "07":
                        setBirthDay(string);
                        break;
                    case "08":
                        setBio(string);
                        break;
                    case "09":
                       return create(string);
                    case "10":
                        if (string.equals("report")) report();
                        else {
                            System.out.println(INVALID);
                        }
                        break;
                    case "11":
                        setEmail(string);
                        break;
                    case "12":
                        setPhoneNumber(string);
                        break;
                    default:
                        System.out.println(INVALID);
                }
            }
            else{
                System.out.println(INVALID);
            }
        }
        else{
            System.out.println(INVALID);
        }
        return this;
    }

    public void setUsername(String string){
        if (string.matches(secondaryPattern)){
            if (!User.usernames.contains(string)){
                userBuilder.setUsername(string);
                username=string;
                System.out.println("username has been successfully set to :"+string);
            }
            else{
                System.out.println("Looks like this username has already been picked, please try again");
            }
        }
        else{
            System.out.println("Invalid username, please try again");
        }
    }

    public void setPassword(String string){
        if (string.matches(secondaryPattern)){
            if (User.passwords.containsKey(string)){
                if (User.passwords.get(string)>Math.sqrt(User.usernames.size())) {
                    System.out.println("password is too weak, please try another one");
                }
                else{
                    userBuilder.setPassword(string);
                    password=string;
                    System.out.println("password has been successfully set to :"+string);
                }
            }
            else{
                userBuilder.setPassword(string);
                password=string;
                System.out.println("password has been successfully set to :"+string);
            }
        }
        else{
            System.out.println("Invalid password, please try again");
        }
    }

    private void setNameUser(String string){
        if (string.matches(namePattern)){
            userBuilder.setName(string);
            System.out.println("name has been successfully set to :"+string);
        }
        else{
            System.out.println("Invalid name, please try again");
        }
    }

    private void setFamilyName(String string){
        if (string.matches(namePattern)){
            userBuilder.setFamilyName(string);
            System.out.println("family name has been successfully set to :"+string);
        }
        else{
            System.out.println("Invalid familyName, please try again");
        }
    }

    private void setBirthDay(String string){
        if (string.matches(birthPattern)){
            int day= Integer.parseInt(string.substring(0, 2));
            int month= Integer.parseInt(string.substring(3, 5));
            int year= Integer.parseInt(string.substring(6, 10));
            userBuilder.setBirthday(new Date(year,month,day));
            System.out.println("birthday successfully set to :" +string);
        }
        else{
            System.out.println("Invalid Date, please try again");
        }
    }

    private void setEmail(String string){
        if (string.matches(emailPattern)){
            if (!User.emails.contains(string)){
                userBuilder.setEmail(string);
                System.out.println("email has been successfully set to : "+string);
            }
            else{
                System.out.println("this email has been picked before");
            }
        }
        else{
            System.out.println("Invalid email address, please try again");
        }
    }

    private void setVerification(String string){
        userBuilder.setVerificationQuestion(string);
        secretInformation=string;
        System.out.println("Done, your secret info is :"+string);
    }

    private void setPhoneNumber(String string){
        if (string.matches(phoneNumberPattern)){
            if (!User.phoneNumbers.contains(string)){
                userBuilder.setPhoneNumber(string);
                System.out.println("phone number has been successfully set to : "+string);
            }
            else{
                System.out.println("this phone number has been picked before");
            }
        }
        else{
            System.out.println("Invalid Phone number, please try again");
        }
    }

    private void setBio(String string){
        userBuilder.setBio(string);
        System.out.println("bio has been successfully set to : "+string);
    }

    private void setIdName(String string){

        if (string.matches(secondaryPattern)){
            if (User.IdNames.contains(string)){
                System.out.println("this Id name has been already picked");
            }
            else{
                userBuilder.setIdentityName(string);
                System.out.println("Id name has been successfully set to :"+string);
            }
        }
        else{
            System.out.println("invalid id name");
        }
    }

    private Page create(String string){
        if (string.equals("account")){
            String validation = checkAccountValidation();
            if (validation.equals("valid")) {
                User user=userBuilder.build();
                logical.setCurrentUser(user);
                username = null;
                password = null;
                secretInformation = null;
                MainPage mainPage=new MainPage(user,previousPage,logical);
                logical.update();
                logical.saveUser(getLogical().getCurrentUser());
                return mainPage;
            }
            else {
                System.out.println(validation);
            }
        }
        else {
            System.out.println(INVALID);
        }
        return this;
    }

    public void report() {
        System.out.println("name :"+userBuilder.getName()+
                "\nfamilyName :"+userBuilder.getFamilyName()+
                "\nbirthday :"+userBuilder.getBirthday().toString()+
                "\nphoneNumber :"+userBuilder.getPhoneNumber()+
                "\nusername :"+username+
                "\npassword : "+password+
                "\nemail :"+userBuilder.getEmail()+
                "\nidentityName :"+userBuilder.getIdentityName()+
                "\nsecret information :"+secretInformation+
                "\nbio :"+userBuilder.getBio());
    }

    public String checkAccountValidation(){
        String s ="";
        if (username==null)s+="you should enter your username\n";
        if (userBuilder.getName()==null)s+="you should enter your name\n";
        if (userBuilder.getFamilyName()==null)s+="you should enter your familyName\n";
        if (userBuilder.getEmail()==null)s+="you should enter your email\n";
        if (password==null)s+="you should enter your password\n";
        if (userBuilder.getIdentityName()==null)s+="you should enter your IdName\n";
        if (secretInformation==null)s+="you should enter your secret info\n";
        if (userBuilder.getPhoneNumber()==null)s+="you should enter your phone number";
        if (s=="")return "valid";
        return s;
    }
}