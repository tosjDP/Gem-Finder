import java.util.HashMap;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class CommandWords
{
    private HashMap<String, CommandWord> validCommands = new HashMap<>();
    // a constant array that holds all valid command words
    /*private static final String[] validCommands = {
        "go", "take", "drop", "look", "eat", "quit", "help"
    };*/

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        validCommands.put("go", CommandWord.GO);
        validCommands.put("take", CommandWord.TAKE);
        validCommands.put("drop", CommandWord.DROP);
        validCommands.put("look", CommandWord.LOOK);
        validCommands.put("eat", CommandWord.EAT);
        validCommands.put("exit", CommandWord.QUIT);
        validCommands.put("help", CommandWord.HELP);
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if a given string is a valid command,
     * false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        return validCommands.containsKey(aString);
    }

    public String getAllCommands() {
        String show = "";
        for (String command : validCommands.keySet()) {
            show += command + " ";
        }
        return show;
    }

    public CommandWord getCommand(String aString) {
        if (validCommands.containsKey(aString)) return validCommands.get(aString);
        return CommandWord.UNKNOWN;
    }
}
