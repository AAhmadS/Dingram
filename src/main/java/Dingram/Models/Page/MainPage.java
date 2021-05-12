package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.User;

public class MainPage extends Page{
    public MainPage(User user, Page previousPage, LogicalAgent logical){
        super(user,previousPage,logical);
        Pages.add(new Settings(user, this,logical));
        Pages.add(new PersonalPage(user, this,logical));
        Pages.add(new ExplorePage(user, this,logical));
        Pages.add(new ChatsPage(user, this, logical));
        Pages.add(new TimeLine(user, this,logical));
        name="Main Page" +
                "\n------------";
        instructions= "INSTRUCTIONS :\n" +
                      "exit          : exit\n" +
                      "Settings      : settings\n" +
                      "explore page  : explore\n" +
                      "Personal page : personal\n" +
                      "chat pages    : chats\n" +
                      "timeline      : timeline\n" +
                      "instructions  : inst";
    }
    @Override
    public Page load(String string) {
        switch (string){
                case"exit":
                    return previousPage;
                case"settings":
                    return getPages().get(0);
                case"explore":
                    return getPages().get(2);
                case"personal":
                    return getPages().get(1);
                case"chats":
                    return getPages().get(3);
                case"timeline":
                    return getPages().get(4);
                case"inst":
                    System.out.println(instructions);
                    break;
                default:
                    System.out.println(INVALID);
            }
        return this;
    }
}
