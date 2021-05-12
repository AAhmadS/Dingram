package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.User;

import java.time.LocalDateTime;

public class Settings extends Page{
    /**
     * updates needed in this page
     * */
    public Settings(User user, Page previousPage, LogicalAgent logical){
        super(user,previousPage,logical);
        Pages.add(new PrivacyPage(user,previousPage,logical));
        name="Settings" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "for going back to previous page write down : previous\n" +
                      "privacy settings                           : privacy\n" +
                      "log out                                    : out\n" +
                      "delete account                             : delete";
    }
    @Override
    public Page load(String userInput) {
        switch (userInput){
            case "previous":
                return previousPage;
            case "out":
                user.setLastSeenTime(LocalDateTime.now());
                logical.saveUser(user);
                return null;
            case"delete":
                delete();
                return null;
            case"privacy":
                return Pages.get(0);
            default:
                System.out.println(INVALID);
                return this;
        }
    }
    public void delete(){
        logical.deleteUser();
    }


    private class PrivacyPage extends Page{
        public PrivacyPage(User user,Page previousPage, LogicalAgent logical){
            super(user,previousPage,logical);
            name="privacy settings" +
                    "\n------------";
            instructions= "back to settings               : back\n" +
                          "change password                : 00-new password\n" +
                          "change public-private settings : 01-public or 01-private\n" +
                          "change last seen settings      : 02-anyone or 02-none or 02-justfollowings\n" +
                          "active inactive settings       : 03-active or 03- inactive ";
        }
        @Override
        public Page load(String userInput) {
            if (userInput.equals("inst")){
                System.out.println(instructions);
                return this;
            }
            if (userInput.equals("back")){
                logical.saveUser(user);
                return previousPage;
            }
            if (userInput.length()>3){
                String number=userInput.substring(0,2);
                String string = userInput.substring(3);
                switch (number){
                    case"00":
                        setPassword(string);
                        break;
                    case"01":
                        setPrivacy(string);
                        break;
                    case"02":
                        setLastSeen(string);
                        break;
                    case "03":
                        setActive(string);
                        break;
                    default:
                        System.out.println(INVALID);
                }
            }
            else{
                System.out.println(INVALID);
            }
            return this;
        }
        public void setPassword(String string){
            if (string.matches(secondaryPattern)){
                User.passwords.replace(user.getPassword(), User.passwords.get(user.getPassword())-1);
                user.setPassword(string);
                if (User.passwords.containsKey(user.getPassword())){
                    User.passwords.replace(user.getPassword(), User.passwords.get(user.getPassword())+1);
                }
                else {
                    User.passwords.put(user.getPassword(),1);
                }
                System.out.println("password successfully set to : "+string);
                logical.update();
            }
            else {
                System.out.println("Invalid password, please try again");
            }
        }
        public void setPrivacy(String string){
            if (string.equals("private")){
                user.setPrivacy(false);
                System.out.println("set successfully");
            }
            else{
                if (string.equals("public")){
                    user.setPrivacy(true);
                    System.out.println("set successfully");
                }
                else{
                    System.out.println(INVALID);
                }
            }
        }
        public void setLastSeen(String string){
            switch (string){
                case"anyone":
                    user.setLastSeenType(1);
                    System.out.println("set successfully");
                    break;
                case"none":
                    user.setLastSeenType(2);
                    System.out.println("set successfully");
                    break;
                case"justfollowings":
                    user.setLastSeenType(3);
                    System.out.println("set successfully");
                    break;
                default:
                    System.out.println(INVALID);
            }
        }
        public void setActive(String string){
            if (string.equals("active"))user.setActive(true);
            else{
                if(string.equals("inactive"))user.setActive(false);
                else{
                    System.out.println(INVALID);
                }
            }
        }
    }
}