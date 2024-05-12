import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input
        System.out.println("Enter a boolean value of isRandom (true/false): ");
        boolean isRandom = scanner.nextBoolean(); // Read user input for isRandom
        System.out.println("Enter a boolean value of showTarget (true/false): ");
        boolean showTarget = scanner.nextBoolean(); // Read user input for showTarget

        Model model = new Model(showTarget, isRandom); // Create an instance of the Model class with user inputs

        if (showTarget) {
            // If showTarget is true, print the correct equation
            System.out.println("Equation: " + model.answer);
        }

        scanner.nextLine(); // Clear line breaks

        System.out.println("Enter an equation of length 7 (including symbols)");
        System.out.println("Start the game");

        String[] array = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/"}; // Initialize an array with possible values
        // Create a Scanner object to receive user input.
        int attempts = 0;
        boolean gameOver = false;

        while (!gameOver && attempts < 6) {
            String s = scanner.nextLine(); // Read the equation entered by the user
            // Convert the equation to a character array
            char[] chars = s.toCharArray();

            if (chars.length != 7) {
                // Check if the equation length is not 7
                System.out.println("Equation formatting error");
            } else {
                String[] enteredValues = new String[7]; // Array to store user-entered values
                for (int i = 0; i < chars.length; i++) {
                    // Add characters in the equation to the model one by one
                    model.Add_String(String.valueOf(chars[i]));
                    enteredValues[i] = String.valueOf(chars[i]); // Store user-entered values
                }
                // Update the array of remaining values
                for (int i = 0; i < enteredValues.length; i++) {
                    for (int j = 0; j < array.length; j++) {
                        if (enteredValues[i] != null && enteredValues[i].equals(array[j])) {
                            array[j] = ""; // Mark the input value as null
                            break;
                        }
                    }
                }
                int[] verify = model.validateExpression(); // Validate the equation

                if (verify[0] == -1 || verify[0] == -2 || verify[0] == -3) {
                    // Check the validation results
                    System.out.println("Equality error");
                    model.Clear_All();
                    System.out.println("Please re-enter the equation:");
                } else if (verify[0] == 6) {
                    // Game over if the user runs out of attempts
                    System.out.println("Game over. You failed");
                    gameOver = true;
                } else if (verify[0] == 7) {
                    // Game over if the user wins
                    System.out.println("Game over. You won");
                    gameOver = true;
                } else {
                    // Classify characters in the equation
                    StringBuilder notExit = new StringBuilder();
                    StringBuilder incorrectPosition = new StringBuilder();
                    StringBuilder correctPosition = new StringBuilder();

                    boolean validPosition = true;
                    for (int i = 0; i < verify.length; i++) {
                        if (verify[i] == 2) {
                            notExit.append(model.getUser_String()[i]).append(" ");
                            System.out.println(model.getUser_String()[i] + " - This character does not exist in the equation");
                        } else if (verify[i] == 1) {
                            incorrectPosition.append(model.getUser_String()[i]).append(" ");
                            System.out.println(model.getUser_String()[i] + " - This character exists in the equation but position is incorrect");
                            validPosition = false;
                        } else if (verify[i] == 0) {
                            correctPosition.append(model.getUser_String()[i]).append(" ");
                            System.out.println(model.getUser_String()[i] + " - This character exists in the equation and the position is correct");
                        }
                    }
                    System.out.println("List:");
                    System.out.println("Not exit in the equation: " + notExit);
                    System.out.println("In the equation but in incorrect positions: " + incorrectPosition);
                    System.out.println("In the equation with correct positions: " + correctPosition);

                    // Print Remaining Values
                    System.out.println("Remaining values:");
                    for (String value : array) {
                        if (!value.equals("")) {
                            System.out.print(value + " ");
                        }
                    }
                    System.out.println();

                    if (!validPosition) {
                        model.Clear_All();
                        model.setLine_num(model.getLine_num() + 1);
                        System.out.println("You can guess " + model.getAttemptGuess() + " times");
                        attempts++;
                    }
                }
            }
        }
        if (!gameOver) {
            // Print game over message if the game is lost
            System.out.println("Game over. You lost");
        }
    }
}
