/**
 * The IModel interface defines the methods that must be implemented by any class
 * that serves as the Model in the MVC (Model-View-Controller) architectural pattern.
 */
public interface IModel {
    // Get the number of remaining attempts for the user
    int getAttemptGuess();

    // Add a string to the user input array
    void Add_String(String s);

    // Check if user input is sufficient for validation
    Boolean user_input();

    // Remove the last entered character from user input
    void back();

    // Validate the user-entered expression and return the validation result
    int[] validateExpression();

    // Get the current line number
    int getLine_num();

    // Set the current line number
    void setLine_num(int line_num);

    // Clear all elements in the user input array
    void Clear_All();

    // Restart the game by resetting relevant data
    void Restart();

    // Get the user input array
    String[] getUser_String();

    // Add an observer to the list of observers
    void addObserver(Observer observer);

    // Notify all observers of a state change
    void notifyObserver();

    // Trigger a change in the state of the object and notify observers
    void setChange();

    // Set whether to show the target number
    void setShowTarget(boolean showTarget);

    // Get whether to show the target number
    boolean getShowTarget();

    // Set whether to use random target number
    void setIsRandom(boolean isRandom);

    // Get whether to use random target number
    boolean getIsRandom();
}
