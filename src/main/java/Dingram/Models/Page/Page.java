package Dingram.Models.Page;

import Dingram.Logic.LogicalAgent;
import Dingram.Models.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {
    protected User user;
    protected transient List<Page> Pages;
    protected transient Page previousPage;
    protected String instructions;
    protected String name;
    protected transient LogicalAgent logical;
    protected final transient String INVALID="Invalid input, pleas try again";
    protected final transient String secondaryPattern="\\w{6,}";
    protected final transient String pattern="[0-1][0-9]";

    protected Page() {
        Pages=new ArrayList<>();
    }

    protected Page(User user,Page previousPage,LogicalAgent logical){
        this.user=user;
        this.previousPage=previousPage;
        this.logical=logical;
        Pages=new ArrayList<>();
    }

    public List<Page> getPages() { return Pages; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getINVALID() {
        return INVALID;
    }

    public String getSecondaryPattern() {
        return secondaryPattern;
    }

    public String getPattern() {
        return pattern;
    }

    public LogicalAgent getLogical() {
        return logical;
    }

    public void setLogical(LogicalAgent logical) {
        this.logical = logical;
    }

    public Page getPreviousPage() {
        return previousPage;
    }

    public void setPages(List<Page> pages) {
        Pages = pages;
    }

    public Page preLoad(){
        System.out.println(name+"\n"+instructions);
        return logical.secondaryLoad();
    }

    public abstract Page load(String userInput);

    public void showInstruction(){
        System.out.println(instructions);
    }
}
