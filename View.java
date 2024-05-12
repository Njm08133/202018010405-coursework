import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class View extends JFrame implements Observer {
    private Controller control; // Controller reference
    private Model model; // Model reference
    private HashMap<Integer, JTextField> jTextFields; // HashMap to store JTextField objects, mapped by index
    private HashMap<String, JButton> jButtons; // HashMap to store JButton objects, mapped by button text
    private JTextField correctEquationField; // Text field to display the correct equation
    private boolean guessedCorrectly = false; // Variable to track if user has guessed correctly at least once

    // Constructor
    public View(Controller control, Model model) {
        this.control = control; // Initialize Controller reference
        this.model = model; // Initialize Model reference
        model.addObserver(this); // Add View as an observer to the Model
        initialize(); // Initialize the view
    }

    // Initialize the interface
    public void initialize() {
        JPanel jPanel = new JPanel(new BorderLayout()); // Change layout to BorderLayout

        jTextFields = new HashMap<>(); // Initialize HashMap to store JTextField objects
        jButtons = new HashMap<>(); // Initialize HashMap to store JButton objects

        JPanel inputPanel = new JPanel(new GridLayout(6, 7, 20, 20)); // Create a panel for input fields
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                JTextField jTextField = new JTextField(); // Create a new JTextField
                jTextField.setPreferredSize(new Dimension(100, 100)); // Set preferred size
                jTextField.setHorizontalAlignment(JTextField.CENTER); // Center align text
                jTextField.setFont(new Font("12", 23, 33)); // Set font
                jTextField.setFocusable(false); // Disable focus
                jTextField.setEditable(false); // Make it non-editable
                jTextFields.put(i * 7 + j, jTextField); // Add JTextField to HashMap
                inputPanel.add(jTextField); // Add JTextField to inputPanel
            }
        }

        jPanel.add(inputPanel, BorderLayout.CENTER); // Add inputPanel to the center of jPanel

        // Add correct equation field to the bottom of jPanel
        if (model.getShowTarget()) {
            correctEquationField = new JTextField(model.answer); // Create a new JTextField with the correct equation
            correctEquationField.setPreferredSize(new Dimension(100, 40)); // Set preferred size
            correctEquationField.setHorizontalAlignment(JTextField.CENTER); // Center align text
            correctEquationField.setFont(new Font("12", 23, 33)); // Set font
            correctEquationField.setFocusable(false); // Disable focus
            correctEquationField.setEditable(false); // Make it non-editable
            jPanel.add(correctEquationField, BorderLayout.SOUTH); // Add correct equation field to the bottom of jPanel
        }

        // Rest of your code...

        // Create virtual keyboard buttons
        String[] buttonLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", "=", "Back", "Enter"};
        for (String label : buttonLabels) {
            JButton jButton = new JButton(label); // Create a new JButton
            jButton.setPreferredSize(new Dimension(80, 40)); // Set preferred size
            jButtons.put(label, jButton); // Add JButton to HashMap
        }

        // Add virtual keyboard buttons to the panel
        JPanel jPanel1 = new JPanel();
        for (String label : buttonLabels) {
            jPanel1.add(jButtons.get(label));
        }

        // Add action listeners to virtual keyboard buttons
        for (JButton button : jButtons.values()) {
            button.addActionListener(control.GetJButton_Listener());
        }

        UIManager.put("OptionPane.okButtonText", "Confirm");
        // Add the "New Game" button
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (guessedCorrectly) {
                    UIManager.put("OptionPane.okButtonText", "Confirm");
                    UIManager.put("OptionPane.cancelButtonText", "Cancel");
                    int choice = JOptionPane.showConfirmDialog(View.this, "You will start a new game, Confirm or Cancel the", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.OK_OPTION) {
                        control.Game_Over(0); // Restart the game
                        guessedCorrectly = false; // Reset guessedCorrectly variable
                    }
                } else {
                    // Show a message indicating that user needs to guess correctly at least once
                    JOptionPane.showMessageDialog(View.this, "You need to guess correctly at least once before starting a new game.", "Invalid Action", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        jPanel1.add(newGameButton);

        jPanel1.setLayout(new FlowLayout(FlowLayout.LEADING, 15, 15)); // Set layout and spacing for the virtual keyboard
        inputPanel.setPreferredSize(new Dimension(880, 600)); // Set size for the input field panel
        jPanel1.setPreferredSize(new Dimension(880, 180)); // Set size for the virtual keyboard

        setFocusable(true); // Set focusable to true
        add(BorderLayout.CENTER, jPanel); // Add input field panel to the window
        add(BorderLayout.SOUTH, jPanel1); // Add virtual keyboard panel to the window
        setBounds(400, 20, 880, 800); // Set size and position for the window
    }

    // Display the string input by the player in the specified input field
    public void Add_String(String str, int index) {
        jTextFields.get(index).setText(str);
    }

    // Display validation results
    public void Ok_Show(int i, int index, String s) {
        JTextField textField = jTextFields.get(index); // Get JTextField from HashMap
        JButton button = jButtons.get(s); // Get JButton from HashMap

        switch (i) {
            case 2:
                textField.setBackground(Color.gray); // Set background color to gray
                button.setBackground(Color.gray); // Set background color to gray
                break;
            case 1:
                textField.setBackground(Color.orange); // Set background color to orange
                button.setBackground(Color.orange); // Set background color to orange
                break;
            case 0:
                textField.setBackground(Color.GREEN); // Set background color to green
                button.setBackground(Color.GREEN); // Set background color to green
                guessedCorrectly = true; // Set guessedCorrectly to true when user guesses correctly
                break;
            case -1:
                for (int j = index; j < index + 7; j++) {
                    jTextFields.get(j).setBackground(Color.GREEN); // Set background color to green for a range of JTextFields
                }
                repaint(); // Repaint the view
                break;
        }
    }

    // Show dialog based on the given scenario
    public void Show_Dialog(int i, String s) {
        UIManager.put("OptionPane.okButtonText", "Confirm"); // Set text for OK button

        if (i == 0 || i == 1) {
            JOptionPane.showMessageDialog(this, "Equational error", "Title", JOptionPane.ERROR_MESSAGE); // Show error dialog
        } else if (i == 3) {
            Object[] options = {"Restart", "Exit game"}; // Options for game over dialog
            int choice = JOptionPane.showOptionDialog(null, "You lose! The correct answer is:" + model.answer, "Game over.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.YES_OPTION) {
                control.Game_Over(0); // Restart the game
            } else {
                // Exit game logic
                System.exit(0); // Exit the program directly
            }
        } else if (i == 4) {
            Object[] options = {"Restart", "Exit game"}; // Options for game over dialog
            int choice = JOptionPane.showOptionDialog(null, "You win!", "Game over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.YES_OPTION) {
                control.Game_Over(0); // Restart the game
            } else {
                // Exit game logic
                System.exit(0); // Exit the program directly
            }
        }
    }

    @Override
    public void Update() {
        control.Update(); // Call update method in Controller
    }
}
