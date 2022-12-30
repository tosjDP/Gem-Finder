import enums.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Player {
    public static final int ITEM_GONE = 0;
    public static final int ITEM_NOTPRESENT = 1;
    public static final int ITEM_NOTMOVEABLE = 2;
    public static final int ITEM_TO_HEAVY = 3;
    public static final int ITEM_DROPPED = 4;
    public static final int DEAD = 0;
    public static final int ALIVE = 1;
    private String name;
    private Room currentRoom;
    private Room previousRoom;
    private ArrayList<Item> bag = new ArrayList<>();
    private boolean canGoNextFloor = false;
    private Item gem;
    private double maxWeight =20;
    private int hp;

    public Player(String name,int hp,Item gem,double maxWeight) {
        this.name = name;
        this.gem=gem;
        this.maxWeight=maxWeight;
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public int take(String name) {
        double currentweight=0;
        for (Item i : bag)
            currentweight += i.getWeight();
        Item item = currentRoom.getItem(name);

        if(currentweight+item.getWeight() > maxWeight) return ITEM_TO_HEAVY;
        if (item!=null && item.isMoveable()) {
            bag.add(item);
            return ITEM_GONE;
        }
        if (item==null) return ITEM_NOTPRESENT;
        return ITEM_NOTMOVEABLE;
    }

    public int drop(String name){
        for (Item item : bag){
            if (item.getName().equals(name)){
                currentRoom.addItem(item);
                bag.remove(item);
                return ITEM_DROPPED;
            }
        }
        return ITEM_NOTPRESENT;
    }
    public int takeDamage(){
        if(hp >0) {
            hp--;
            return ALIVE;
        }
        return DEAD;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public boolean isCanGoNextFloor() {
        return canGoNextFloor;
    }

    public void setCanGoNextFloor(boolean canGoNextFloor) {
        this.canGoNextFloor = canGoNextFloor;
    }

    public void setCurrentRoom(Room currentRoom) {
        int gemCount = Collections.frequency(bag,gem);
        this.previousRoom = this.currentRoom;
        this.currentRoom = currentRoom;

        if (this.currentRoom.type.equals(RoomType.FINISH)&&currentRoom.getFloor()==gemCount) {
            this.canGoNextFloor=true;
        }
    }
    public void goBack(){
        this.currentRoom = this.previousRoom;
        System.out.println();
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
