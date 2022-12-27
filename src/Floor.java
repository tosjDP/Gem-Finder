import enums.RoomType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Floor {
    private int currentFloor;
    private int floorSize;
    private int itemsPerFloor;
    private int maxItemsPerRoom;
    private Room[][] rooms;
    private ArrayList<Item> items;

    public Room[][] getRooms() {
        return rooms;
    }

    public Floor(int currentFloor, int itemsPerFloor, int floorSize,int maxItemsPerRoom, ArrayList<Item>items) {
        this.currentFloor = currentFloor;
        this.itemsPerFloor=itemsPerFloor;
        this.floorSize=floorSize;
        this.items=items;
        this.maxItemsPerRoom=maxItemsPerRoom;
        this.rooms=new Room[floorSize][floorSize];
        this.generateRooms();
        this.assignItemToRoom();
    }

    private void generateRooms(){
        for (int i = 0; i < floorSize; i++) {
            for (int j = 0; j < floorSize; j++) {
                RoomType type = RoomType.values()[new Random().nextInt(RoomType.values().length)];
                if(type == RoomType.FINISH || type == RoomType.START)
                    type = RoomType.values()[new Random().nextInt(RoomType.values().length)];

                this.rooms[i][j] = new Room("x: "+i+ " y: "+j,type,i,j,this);
            }
        }

        this.rooms[0][0] = new Room("You wake up in a dark room, and can't remember anything", RoomType.START,0,0,this);
        this.rooms[floorSize-1][floorSize-1] = new Room("", RoomType.FINISH,floorSize-1,floorSize-1,this);

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
    private void assignItemToRoom(){
        for (int i = 0; i < itemsPerFloor; i++) {
            while (items.size() > 0){
                int x = (int) Math.floor(Math.random()*(floorSize));
                int y = (int) Math.floor(Math.random()*(floorSize));
                while (this.rooms[x][y].getItemsCount() >= maxItemsPerRoom){
                    x = (int) Math.floor(Math.random()*(floorSize));
                    y = (int) Math.floor(Math.random()*(floorSize));
                }
                int index = (int)  Math.floor(Math.random()*(items.size()));
                this.rooms[x][y].addItem(items.get(index));
                items.remove(index);
            }

        }
    }

}