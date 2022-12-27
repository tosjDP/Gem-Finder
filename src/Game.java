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
    private Floor floor;
        
    /**++
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        player = new Player("Jos");
        //createRooms();
        parser = new Parser();
        floor = new Floor(1);
        player.setCurrentRoom(floor.getRooms()[0][0]);

    }

    /**
     * Create all the rooms and link their exits together.
     */
//    private void createRooms()
//    {
//        Room outside, theater, pub, lab, office, cellar;
//        Item ashtray, promoboard;
//
//        // create the rooms
//        outside = new Room("outside the main entrance of the university");
//        promoboard = new Item("promoboard", "university promoboard",  3.2);
//        ashtray = new Item("ashtray", "big yellow ashtray", 4.6);
//        outside.addItem(promoboard);
//        promoboard.setMoveable(false);
//        outside.addItem(ashtray);
//        theater = new Room("in a lecture theater");
//        pub = new Room("in the campus pub");
//        lab = new Room("in a computing lab");
//        office = new Room("in the computing admin office");
//        cellar = new Room("the cellar with stock for the pub");
//
//        // initialise room exits
//        outside.setExit("east", theater);
//        outside.setExit("south", lab);
//        outside.setExit("west", pub);
//        theater.setExit("west", outside);
//        pub.setExit("east", outside);
//        pub.setExit("down", cellar);
//        cellar.setExit("up", pub);
//        lab.setExit("north", outside);
//        lab.setExit("east", office);
//        office.setExit("west", lab);
//
//        cellar.addItem(new Item("rum", "a barrel of rum", 10.7));
//        cellar.addItem(new Item("water", "a crate bottles of water", 12.2));
//
//        player.setCurrentRoom(outside);  // start game outside
//    }

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

        int result = player.take(command.getSecondWord());
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
        String back = "back";
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
}
