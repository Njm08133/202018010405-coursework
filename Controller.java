import javax.swing.*;
import java.awt.event.*;

public class Controller {
    private Model model; // Model reference
    private View view; // View reference

    public Controller(Model model) {
        this.model = model; // Constructor initializing the model
    }

    // ActionListener for JButton
    public ActionListener GetJButton_Listener() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Action performed method
                JButton jButton = (JButton) e.getSource(); // Get the source of the action
                if ("Back".equals(jButton.getText())) { // Check if the button text is "Back"
                    model.back(); // Call back method in the model
                    model.notifyObserver(); // Notify the observer
                    Update(); // Call the update method
                } else if ("Enter".equals(jButton.getText())) { // Check if the button text is "Enter"
                    int[] verify; // Array to store validation result
                    try {
                        verify = model.validateExpression(); // Validate expression
                    } catch (Exception ex) {
                        throw new RuntimeException(ex); // Throw runtime exception if validation fails
                    }
                    switch (verify[0]) { // Switch case based on validation result
                        case -1:
                            getView().Show_Dialog(0, ""); // Show dialog for invalid expression
                            break;
                        case -2:
                        case -3:
                            getView().Show_Dialog(1, ""); // Show dialog for incorrect expression format
                            break;
                        case 6:
                            getView().Show_Dialog(3, ""); // Handle game over scenario
                            break;
                        case 7:
                            getView().Ok_Show(-1, model.getLine_num() * 7, "ok"); // Show OK dialog
                            getView().Show_Dialog(4, ""); // Show dialog
                            break;
                        default:
                            if (verify.length == 7) { // Check if verification array length is 7
                                for (int i = 0; i < verify.length; i++) {
                                    getView().Ok_Show(verify[i], i + (model.getLine_num() * 7), model.getUser_String()[i]); // Show OK dialog for each item
                                }
                            }
                            model.Clear_All(); // Clear all data
                            model.setLine_num(model.getLine_num() + 1); // Increment line number
                            break;
                    }
                } else {
                    model.Add_String(jButton.getText()); // Add string to model
                    model.notifyObserver(); // Notify observer
                    Update(); // Call update method
                }
                getView().setFocusable(true); // Set focusable to true
            }
        };
        return actionListener; // Return action listener
    }

    // Method to start the game
    public void Start_Game() {
        View page = new View(this, model); // Create view instance
        page.setVisible(true); // Set visibility of view
        page.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set default close operation
        this.view = page; // Assign view instance
    }

    // Get view instance
    public View getView() {
        return view; // Return view instance
    }

    // Update method
    public void Update() {
        for (int i = 0; i < model.getUser_String().length; i++) {
            getView().Add_String(model.getUser_String()[i] != null ? model.getUser_String()[i] : "", i + (model.getLine_num() * 7)); // Add string to view
        }
    }

    // Game over method
    public void Game_Over(int i) {
        if (i == 0) {
            model.Restart(); // Restart the game
            getView().dispose(); // Dispose the view
            Start_Game(); // Start the game again
        } else {
            System.exit(0); // Exit the program
        }
    }
}
