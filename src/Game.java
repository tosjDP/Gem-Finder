import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game
{
    private Parser parser;
    private Player player;
    private ArrayList<Floor> floors =new ArrayList<>();
    private Floor currentFloor;
    public static ArrayList<Item> items=new ArrayList<>();
    private Item gem = new Item("Gem","yeey",0.1);

    /**++
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        player = new Player("Jos",gem);
        initializeData();
        floors.add(new Floor(1,10,3,2,items,gem));
        floors.add(new Floor(2,10,4,2,items,gem));
        floors.add(new Floor(3,10,5,2,items,gem));
        floors.add(new Floor(4,10,6,2,items,gem));
        //floors.add(new Floor(5,10,1,2,items));
        currentFloor = floors.get(0);
        player.setCurrentRoom(currentFloor.getRooms()[0][0]);

    }
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    private void printLocationInfo() {
        System.out.println("currentfloor: " + floors.indexOf(currentFloor));
        System.out.println(player.getName() + " is " + player.getCurrentRoom().getLongDescription());
        System.out.println(player.getBagDescription());
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        CommandWord commandWord = command.getCommandWord();
        switch (commandWord) {
            case HELP:
                printHelp();
                break;
            case GO:
                goRoom(command);
                break;
            case TAKE:
                take(command);
                break;
            case DROP:
                break;
            case LOOK:
                look();
                break;
            case EAT:
                eat();
                break;
            case QUIT:
                wantToQuit = quit(command);
                break;
            default:
        }

        return wantToQuit;
    }

    private void take(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }

        int result = player.take(command.getSecondWord().toLowerCase());
        if (result==Player.ITEM_GONE) {
            printLocationInfo();
        } else {
            if (result==Player.ITEM_NOTMOVEABLE) {
                System.out.println("Item " + command.getSecondWord() + " is not moveable");
            } else {
                System.out.println("Can't find " + command.getSecondWord());
            }

        }
    }
    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println(player.getName() + " is lost. " + player.getName() + " is alone. He wanders");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("His command words are:");
        System.out.println("   " + parser.showCommands());
    }

    private void look() {
        System.out.println(player.getName() + " is " + player.getCurrentRoom().getLongDescription());
    }

    private void eat() {
        System.out.println(player.getName() + " has eaten and is not hungry anymore");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (direction.equals("back")) {
            player.goBack();
            printLocationInfo();
        }
        else if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            player.setCurrentRoom(nextRoom);
            if (player.isCanGoNextFloor()){
                int index = floors.indexOf(currentFloor);
                if (index == floors.size()-1){
                    //TODO add win statement
                }
                else{
                    currentFloor = floors.get(index+1);

                    player.setCurrentRoom(currentFloor.getRooms()[0][0]);
                    player.setCanGoNextFloor(false);
                }
            }
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
    private void initializeData(){
        items.add(new Item("cake","hmm jummy cake",0.5));
        items.add(new Item("Fedora","youre a real chad if you wear one of these",0.2));
        items.add(new Item("candle","a non lit candle..to bad",0.1));
        items.add(new Item("Bones","Bones of a human that has tried to escape but failed",3.2));
        items.add(new Item("bucket","Bucket with some holes in it",0.75));
        items.add(new Item("Torch","giving a easing glow",1.5));
        items.add(new Item("Rock","Sturdy and heavy",9.3));
        items.add(new Item("pocketwatch","a beautifull golden pocketwatch, broken sadly engouh",0.2));
        items.add(new Item("book of spells","an old book covered in dust, what secrets does it hold?",0.5));
        items.add(new Item("book of prayer","a gold plated book with holy transcripst",0.5));
        items.add(new Item("giants sword","a sword that must have been from giant race, youre not carringy this one",50));
        items.add(new Item("chest","a dirty old chest",50));
        items.add(new Item("mushroomhat","a hat made from a mushroom or a mushroom made from a hat ?",0.25));
        items.add(new Item("wooden stake","i hope i wont need this...",0.15));
        items.add(new Item("pile of gold ","its free real estate",4));
        items.add(new Item("skeleton","a deceased adventurer looks like he took an arrow to the knee...",25));
        items.add(new Item("amazon package","somehow this seems vagely familiar",2));
        items.add(new Item("bag of doritos","ooo cheese flavour the best",0.2));
        items.add(new Item("shield","a wooden shield with metal pieces",3));
        items.add(new Item("crimson flask of tears","a haunting red liquid is inside this flask, should you drink it?",0.25));
        items.add(new Item("stick of C4","this doesnt seem out of place at all",2.5));
    }
}
