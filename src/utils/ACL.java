package utils;

public class ACL {

    private static int creatorPermission = 0;
    private static int ownerPermission = 1;
    private static int userPermission = 2;

    public static int getOwnerPermission() {
        return ownerPermission;
    }

    public static int getUserPermission() {
        return userPermission;
    }

    public static int getCreatorPermission(){
        return creatorPermission;
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
