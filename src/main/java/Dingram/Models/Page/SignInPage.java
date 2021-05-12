package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.User;

public class SignInPage extends LogPage {
    private User potentialUser;
    public SignInPage(User user , Page previousPage , LogicalAgent logicalAgent){
        super(user, previousPage, logicalAgent);
        this.previousPage=new SignUpPage(user,this,logicalAgent);
        instructions= "Instructions :\n" +
                      "for going to signUpPage enter                  : 00-sign\n" +
                      "for entering username, write down              : 01-your text\n" +
                      "for entering password, write down              : 02-your text\n" +
                      "if you've forgotten your password, write down  : 03-your secret info\n" +
                      "and if you want to see instructions write down : inst";
        name="Sign In Page" +
                "\n------------";
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
                switch (number){
                    case "00":
                        if (string.equals("sign"))return previousPage;
                        else { System.out.println(INVALID+" sign"); }
                        break;
                    case "01":
                        checkUsername(string);
                        break;
                    case "02":
                        return checkPassword(string);
                    case "03":
                        return checkSecret(string);
                    default:
                        System.out.println(INVALID);
                }
            }
            else{
                System.out.println(INVALID);
            }
        }
        return this;
    }

    private void checkUsername(String string){
        if (User.usernames.contains(string)){
            Long id=(long) User.usernames.indexOf(string);
            potentialUser=logical.loadUser(id);
            System.out.println("username set, enter password");
        }
        else{
            System.out.println(INVALID+" username");
        }
    }

    public Page checkPassword(String string){
        if (potentialUser==null){
            System.out.println("you should enter username first");
            return this;
        }
        else {
            if (string.equals(potentialUser.getPassword())){
                logical.setCurrentUser(potentialUser);
                Page page=new MainPage(potentialUser,this,logical);
                System.out.println("welcome "+potentialUser.getIdentityName());
                potentialUser=null;
                return page;
            }
            else{
                System.out.println("wrong password");
                return this;
            }
        }
    }

    public Page checkSecret(String string){
        if (potentialUser==null){
            System.out.println("you should enter username first");
            return this;
        }
        else {
            if (string.equals(potentialUser.getVerificationQuestion())){
                logical.setCurrentUser(potentialUser);
                Page page=new MainPage(potentialUser,this,logical);
                System.out.println("welcome "+potentialUser.getIdentityName());
                potentialUser=null;
                return page;
            }
            else{
                System.out.println("wrong info");
                return this;
            }
        }
    }
}