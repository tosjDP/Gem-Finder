import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Room
{
    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<Item> items = new ArrayList<>();

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        exits = new HashMap<>();
        this.description = description;
    }

    public void addItem(Item item) {
        if (item!=null) items.add(item);
    }

    public boolean hasItem(String name) {
        for(Item i : items) {
            if (i.getName().equals(name)) return true;
        }
        return false;
    }

    public Item getItem(String name) {
        Item foundItem = null;
        for(Item i : items) {
            if (i.getName().equals(name)) {
                foundItem = i;
                break;
            }
        }
        if (foundItem!=null && foundItem.isMoveable()) items.remove(foundItem);
        return foundItem;
    }

    public void setExit(String direction, Room room) {
        if (room!=null) exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

    public String getExitString() {
        String returnString = "Exits: ";
        for(String direction : exits.keySet()) {
            returnString += direction + " ";
        }
        return returnString;
    }

    public String getLongDescription() {
        String returnString = getDescription() + ". It contains:" + System.lineSeparator();
        for(Item i : items) {
            returnString += "  " + i.getLongDescription() + System.lineSeparator();
        }
        return returnString + getExitString();
    }

}
