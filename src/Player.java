import java.util.ArrayList;

public class Player {
    private String name;
    private Room currentRoom;
    private ArrayList<Item> bag = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public boolean take(String name) {

        return false;
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
}
