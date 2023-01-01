import enums.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

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
    private HashMap<RoomType,String> trapDescription;

    public Player(String name,int hp,Item gem,double maxWeight) {
        this.name = name;
        this.gem=gem;
        this.maxWeight=maxWeight;
        this.hp = hp;
        this.trapDescription.put(RoomType.PIT,"you trip over a small rock and fall into the infinte void");
        this.trapDescription.put(RoomType.FLOODED,"When you enter the room the gate closed behind you and the room started flooding");
        this.trapDescription.put(RoomType.TRAP,"you step on a hidden plate,oops");
        //TODO add description
    }

    public int getHp() {
        return hp;
    }

    /**
     *de take commande
     * dit chech of de player de item mag opnemen en het niet de buiten de regels valt
     */
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
    /**
     *de drop functie.
     */
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
        String description = trapDescription.get(currentRoom.getType());
        if(hp >0) {
            hp--;
            System.out.println(description);
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

        /**
         *checkt of de player de gem heeft die nodig is voor naar de volgende floor te gaan.
         */
        if (this.currentRoom.getType().equals(RoomType.FINISH)&&currentRoom.getFloor()==gemCount) {
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
