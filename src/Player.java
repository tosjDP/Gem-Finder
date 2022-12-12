import java.util.ArrayList;

public class Player {
    public static final int ITEM_GONE = 0;
    public static final int ITEM_NOTPRESENT = 1;
    public static final int ITEM_NOTMOVEABLE = 2;
    private String name;
    private Room currentRoom;
    private ArrayList<Item> bag = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public int take(String name) {
        Item item = currentRoom.getItem(name);
        if (item!=null && item.isMoveable()) {
            bag.add(item);
            return ITEM_GONE;
        }
        if (item==null) return ITEM_NOTPRESENT;
        return ITEM_NOTMOVEABLE;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public String getBagDescription() {
        if (bag.isEmpty()) {
            return "The bag of " + name + " is empty.";
        }
        String returnString = "The bag of " + name + " contains:" + System.lineSeparator();
        for(Item i : bag) {
            returnString += "  " + i.getLongDescription() + System.lineSeparator();
        }
        return returnString;
    }
}
