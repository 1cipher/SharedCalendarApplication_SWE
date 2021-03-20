package utils;

public class ACL {

    public static int getOwnerPermission() {
        return 1;
    }

    public static int getUserPermission() {
        return 2;
    }

    public static int getCreatorPermission(){
        return 0;
    }

    public static boolean canDeleteCalendar(int p){
        if (p==0)
            return true;
        return false;
    }

    public static boolean canDeleteEvent(int p){
        if (p<2)
            return true;
        return false;
    }

    public static boolean canCreateEvent(int p){
        if (p<2)
            return true;
        return false;
    }

    public static boolean canEditEvent(int p) {
        if (p < 2)
            return true;
        return false;
    }
}
