import enums.RoomType;

import java.lang.reflect.Array;
import java.util.*;

public class Floor {
    private int currentFloor;
    private int floorSize;
    private int itemsPerFloor;
    private int maxItemsPerRoom;
    private Room[][] rooms;
    private ArrayList<Item> items;
    private boolean nextFloor = false;
    private Item gem;
    private int maxDangerRooms;
    private HashMap<RoomType,String> roomDescription = new HashMap<>();

    public Room[][] getRooms() {
        return rooms;
    }

    public Floor(int currentFloor, int itemsPerFloor, int floorSize,int maxItemsPerRoom, ArrayList<Item>items ,Item gem,int maxDangerRooms) {
        this.currentFloor = currentFloor;
        this.itemsPerFloor=itemsPerFloor;
        this.floorSize=floorSize;
        this.items = new ArrayList<>(items);
        this.maxItemsPerRoom=maxItemsPerRoom;
        this.rooms=new Room[floorSize][floorSize];
        this.gem=gem;
        this.maxDangerRooms=maxDangerRooms;
        this.roomDescription.put(RoomType.PIT,"A deep pit");
        this.roomDescription.put(RoomType.FLOODED,"a flooded room");
        this.roomDescription.put(RoomType.TRAP,"dont go in this one");
        //TODO room type beschtrijve
        this.generateRooms();
        this.assignItemToRoom();

    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getFloorSize() {
        return floorSize;
    }

    public boolean isNextFloor() {
        return nextFloor;
    }

    public void setNextFloor(boolean nextFloor) {
        this.nextFloor = nextFloor;
    }

    /**
     *deze functie genereet in een array al mijn RoomTypes op een random x en y.
     */
    private void generateRooms(){
        boolean hasTeleport = false;
        int dangerRoomcount = 0;
        for (int i = 0; i < floorSize; i++) {
            for (int j = 0; j < floorSize; j++) {
                RoomType type = RoomType.values()[new Random().nextInt(RoomType.values().length)];
                while(type.equals(RoomType.FINISH) || type.equals(RoomType.START)) {
                    type = RoomType.values()[new Random().nextInt(RoomType.values().length)];
                }
                if (dangerRoomcount >= maxDangerRooms)
                while(type.equals(RoomType.FLOODED)||type.equals(RoomType.PIT)||type.equals(RoomType.TRAP)) {
                    type = RoomType.values()[new Random().nextInt(RoomType.values().length)];
                }
                if(hasTeleport) {
                    while (type.equals(RoomType.TELEPORT)) {
                        type = RoomType.values()[new Random().nextInt(RoomType.values().length)];
                    }
                }
                if (type.equals(RoomType.TELEPORT))
                    hasTeleport= true;

                if (type.equals(RoomType.FLOODED)||type.equals(RoomType.PIT)||type.equals(RoomType.TRAP))
                    dangerRoomcount++;
                System.out.println(type);
                this.rooms[i][j] = new Room("x: "+i+ " y: "+j +" "+roomDescription.get(type),type,i,j,this);
            }
        }


        /**
         *zet de START en FINISH room van elke flor op een vaste plaats.
         */

        this.rooms[0][0] = new Room("You wake up in a dark room, and can't remember anything", RoomType.START,0,0,this);
        this.rooms[floorSize-1][floorSize-1] = new Room("a large emtpy room with a big door, looks like the gem fits te door", RoomType.FINISH,floorSize-1,floorSize-1,this);


        /**
         *deze functie zorgt ervoor dat er randen zijn aan mijn array door de hoeken en de kanten van elke floor in te stellen .
         */
        for (int i = 0; i < floorSize; i++) {
            for (int j = 0; j < floorSize; j++) {
                Room room = this.rooms[i][j];
                int x = room.getX();
                int y = room.getY();
                if(x == 0 && y == 0) {
                    room.setExit("east",this.rooms[x+1][y]);
                    room.setExit("south",this.rooms[x][y+1]);
                }
                else if(x == floorSize-1 && y == 0){
                    room.setExit("west",this.rooms[x-1][y]);
                    room.setExit("south",this.rooms[x][y+1]);
                }
                else if(x == 0 && y == floorSize-1){
                    room.setExit("north",this.rooms[x][y-1]);
                    room.setExit("east",this.rooms[x+1][y]);
                }
                else if(x == floorSize-1 && y == floorSize-1){
                    room.setExit("north",this.rooms[x][y-1]);
                    room.setExit("west",this.rooms[x-1][y]);
                } else if (x== 0 && y < floorSize-1 && y > 0) {
                    room.setExit("north",this.rooms[x][y-1]);
                    room.setExit("east",this.rooms[x+1][y]);
                    room.setExit("south",this.rooms[x][y+1]);
                } else if (y == 0 && x < floorSize-1 && x > 0 ) {
                    room.setExit("east",this.rooms[x+1][y]);
                    room.setExit("south",this.rooms[x][y+1]);
                    room.setExit("west",this.rooms[x-1][y]);
                }
                else if (x == floorSize-1 && y < floorSize-1 && y > 0 ) {
                    room.setExit("north",this.rooms[x][y-1]);
                    room.setExit("south",this.rooms[x][y+1]);
                    room.setExit("west",this.rooms[x-1][y]);
                }
                else if (y == floorSize-1 && x < floorSize-1 && x > 0 ) {
                    room.setExit("north",this.rooms[x][y-1]);
                    room.setExit("east",this.rooms[x+1][y]);
                    room.setExit("west",this.rooms[x-1][y]);
                }
                else {
                    room.setExit("north",this.rooms[x][y-1]);
                    room.setExit("south",this.rooms[x][y+1]);
                    room.setExit("east",this.rooms[x+1][y]);
                    room.setExit("west",this.rooms[x-1][y]);
                }
            }
        }
    }
    /**
     deze functie zet items in al mijn room sen kiest deze ook random en checkt of het geen traprooms zijn waar geen items in kunnen.
     */
    private void assignItemToRoom(){
        for (int i = 0; i < itemsPerFloor; i++) {
                int x = (int) Math.floor(Math.random()*(floorSize));
                int y = (int) Math.floor(Math.random()*(floorSize));
                while (this.rooms[x][y].getItemsCount() >= maxItemsPerRoom){
                    x = (int) Math.floor(Math.random()*(floorSize));
                    y = (int) Math.floor(Math.random()*(floorSize));
                }
                if (!this.rooms[x][y].getType().equals(RoomType.FLOODED)||!this.rooms[x][y].getType().equals(RoomType.TRAP)||!this.rooms[x][y].getType().equals(RoomType.PIT)) {
                    int index = (int)  Math.floor(Math.random()*(items.size()));
                    this.rooms[x][y].addItem(items.get(index));
                    items.remove(index);
                }
        }
        int x = (int) Math.floor(Math.random()*(floorSize));
        int y = (int) Math.floor(Math.random()*(floorSize));
        this.rooms[x][y].addItem(gem);
    }

}