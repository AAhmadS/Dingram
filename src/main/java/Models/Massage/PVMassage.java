package Models.Massage;

import Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PVMassage extends Massage{
    public static List<Long> PvMassageList=new ArrayList<>();

    private boolean seen;
    public PVMassage(){}

    public PVMassage(User user, String written, LocalDateTime date) {
        super(user, written, date);
        seen=false;
        PvMassageList.add(this.getID());
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
