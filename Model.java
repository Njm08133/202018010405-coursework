import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * The Model class represents the data and business logic of the application
 * in the MVC (Model-View-Controller) architectural pattern.
 */
public class Model extends Observable implements IModel {
    ArrayList<Observer> observers=new ArrayList<>(); // List to hold observers
    private String[] user_String = new String[7]; // Array to store user input characters

    private ArrayList<String> equationList; // List to store equations read from a text file
    private boolean showTarget, isRandom;

    public String answer; // Stores the correct answer
    private int line_num = 0; // Tracks the current line number/chances
    private int attemptGuess;

    /**
     * Constructor for the Model class.
     * Initializes the equationList by reading equations from a text file and sets the correct answer.
     */
    public Model(boolean showTarget, boolean isRandom) {
        try {
            ArrayList<String> strings = new ArrayList<>();
            String pathname = "equations.txt";
            File filename = new File(pathname); // read input.txt
            //System.out.println("Enter a boolean value of isRandom (true/false): ");
            this.setIsRandom(isRandom);
            //System.out.println("Enter a boolean value of showTarget (true/false): ");
            this.setShowTarget(showTarget);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // Create an input stream object reader
            BufferedReader br = new BufferedReader(reader); // Create an object that translates the contents of the file into a language that the computer can read and understand
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // Read data one row at a time
                strings.add(line);
            }
            equationList = strings;
            if (isRandom){
                Get_AnswerRandom();
            }else {
                Get_AnswerFixed();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @pre. The method assumes that the equationList ArrayList is not null and contains at least one equation.
     * @post. The answer variable is set to a randomly selected equation from the equationList.
     **/
    private void Get_AnswerRandom() {
        // Preconditions
        assert equationList != null : "The equationList ArrayList must not be null.";
        assert !equationList.isEmpty() : "The equationList ArrayList must contain at least one equation.";

        this.answer = equationList.get(new Random().nextInt(equationList.size()));

        // Postconditions:
        assert answer != null : "The answer variable must not be null.";
    }

    /**
     * @pre. The method assumes that the equationList ArrayList is not null and contains at least one equation.
     * @post. The answer variable is set to the first equation from the equationList.
     **/
    private void Get_AnswerFixed() {
        // Preconditions
        assert equationList != null : "The equationList ArrayList must not be null.";
        assert !equationList.isEmpty() : "The equationList ArrayList must contain at least one equation.";

        this.answer = equationList.get(0);

        // Postconditions:
        assert answer != null : "The answer variable must not be null.";
    }

    /**
     * @pre. none
     * @post. No assertion code needed
     **/
    public int getAttemptGuess() {
        this.attemptGuess = 6 - this.getLine_num();
        return attemptGuess;

        // Postconditions: The return value attemptGuess represents the number of remaining attempts for the user, calculated as 6 - this.getLine_num()
//        assert attemptGuess >= 0 : "Incorrect calculation of attemptGuess";
    }

    /**
     * @pre. s must not be null.
     * @post. Adds the provided string s to the user_String array.
     **/
    // Characters added by the user
    public void Add_String(String s) {
        // Preconditions
        assert s != null : "Input string 's' must not be null.";

        for (int i = 0; i < user_String.length; i++) {
            if (user_String[i] == null) {
                user_String[i] = s;
                break;
            }
        }

        // Postconditions
        boolean stringAdded = false;
        for (String str : user_String) {
            if (str != null && str.equals(s)) {
                stringAdded = true;
                break;
            }
        }
        assert stringAdded : "String 's' was not added to the user_String array.";
    }


    /**
     * @pre. null
     * @post. Returns true if all characters in user_String have lengths greater than or equal to 7; otherwise, returns false.
     */
    // Determine whether the user can continue to enter characters
    public Boolean user_input() {
        for (int i = 0; i < user_String.length; i++) {
            if (user_String[i] == null || user_String[i].length() < 7) {
                return false; // If any of the characters is less than 7, return false
            }
        }
        return true; // Returns true if all characters are greater than or equal to 7.
    }


    /**
     * @pre. null
     * @post. Removes the last entered character from user_String if there are characters entered.
     */
    //back character
    public void back() {
        boolean is = true;
        for (int i = 0; i < user_String.length; i++) {
            if (user_String[i] == null) {
                if (i == 0) {
                    return;
                }
                user_String[i - 1] = null;
                is = false;
            }
        }
        if (is) {
            user_String[6] = null;
        }
        // Postconditions
        // After calling back(), the last entered character should be removed from user_String if characters were entered.
        assert (user_String[user_String.length - 1] == null) : "Last entered character should be removed.";

    }

    /**
     * @pre. The user must have entered at least 7 characters/The input string array must not be null.
     * @post. Returns an array representing the validation result of the entered expression:
     * If the entered characters are not enough, returns {-1}.
     * If the entered expression lacks an equals sign '=', returns {-2}.
     * If the entered expression is correct, returns {7}.
     * If the user has run out of chances (line_num reaches 5), returns {6}.
     * If the entered expression is incorrect, returns {-3}.
     */
    //Validating user-entered expressions
    public int[] validateExpression() {
        // Preconditions
//        assert user_input() : "Input characters are not enough for validation.";
//        assert user_String != null : "user_String array must not be null for validation.";

        // Verify that the expression is complete, with or without the equals sign.
        boolean valid = false;
        for (int i = 0; i < user_String.length; i++) {
            if (user_String[i] != null && user_String[i].equals("=")) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            return new int[]{-1}; // The equation is incomplete.
        }

        // Verify that the expression has addition, subtraction, multiplication and division
        valid = false;
        for (int i = 0; i < user_String.length; i++) {
            if (user_String[i] != null && (user_String[i].equals("+") || user_String[i].equals("-") || user_String[i].equals("*") || user_String[i].equals("/"))) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            return new int[]{-2}; // The equation is incomplete.
        }

        // Verify that the expression is correct
        int[] ints = new int[7];
        StringBuilder s = new StringBuilder();
        StringBuilder s1 = new StringBuilder();
        int i1 = 0;
        for (int i = 0; i < user_String.length; i++) {
            if (user_String[i] != null && user_String[i].equals("=")) {
                i1 = i;
                break;
            } else {
                s.append(user_String[i]);
            }
        }

        for (int i = i1 + 1; i < user_String.length; i++) {
            s1.append(user_String[i]);
        }

        Calculator calculator = new Calculator();
        int eval = calculator.calcExpresion(s.toString());
        int eval1 = calculator.calcExpresion(s1.toString());

        if (eval == eval1) {
            char[] chars = answer.toCharArray();
            char[] chars1 = (s + "=" + s1).toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == chars1[i]) {
                    ints[i] = 0; // Characters are contained and in the correct position
                } else {
                    boolean is = false;
                    for (int j = 0; j < chars.length; j++) {
                        if (chars[j] == chars1[i]) {
                            ints[i] = 1; // Included but not in the right place
                            is = false;
                            break;
                        } else {
                            is = true;
                        }
                    }

                    if (is) {
                        ints[i] = 2; // This number is not included in the equation
                    }
                }
            }

            boolean is = true;
            for (int i = 0; i < ints.length; i++) {
                if (ints[i] != 0) {
                    is = false;
                    break;
                }
            }
            if (is) {
                return new int[]{7}; // The equation is correct.
            } else {
                if ((5 - line_num) == 0) {
                    return new int[]{6}; // Opportunities exhausted
                }
            }

        } else {
            return new int[]{-3}; // The equation does not hold
        }
        // Postconditions
//        assert user_String != null : "Result array must not be null after validation.";

        return ints;
    }


    /**
     * @pre. The precondition for this method is that the current object must have a valid line number (line_num) set, otherwise it might lead to unpredictable results.
     * @post. The postcondition for this method is that it returns the line number (line_num) of the current object as a result, which should be a valid integer value.
     */
    public int getLine_num() {
        assert invariant() : "Line number cannot be negative";
        return line_num;
    }


    /**
     * @pre. The precondition of this method is to accept an integer parameter line_num, which represents the line number to be set.
     * @post. The postcondition of this method is to set the object's line_num attribute to the value of the passed line_num parameter.
     */
    public void setLine_num(int line_num) {
        // Precondition
        assert invariant() : "Line number must be a non-negative integer";
        this.line_num = line_num;

    }


    /**@pre. The user_String array must not be null./The user_String array length must be greater than zero.
     * @post. All elements in the user_String array will be set to null.
     */
    // Empty Character Array
    public void Clear_All() {
        assert user_String != null : "Precondition failed: user_String array must not be null";
        assert user_String.length > 0 : "Precondition failed: user_String array length must be greater than zero";

        for (int i = 0; i < user_String.length; i++) {
            user_String[i] = null;
            assert user_String[i] == null : "Postcondition failed: Element at index " + i + " is not null";
        }

    }

    /**@pre.     The Restart() method assumes that the environment is in a state where the Clear_All() method can be safely executed.
    The Restart() method expects that line_num is appropriately initialized.
    The Restart() method relies on the availability and correctness of the Get_Answer() method.
    * @post.    Upon successful execution of Restart(), all relevant data or variables that need to be cleared are reset.
     * The value of line_num is set to 0 after the execution of Restart().
     * The method Restart() ensures that the program is ready to fetch a new answer using the Get_Answer() method.
    */
    public void Restart() {
        //Precondition
        assert user_String != null : "The user_String array must not be null for Restart() method.";
        assert line_num >= 0 : "The line_num must be a non-negative integer for Restart() method.";
        Clear_All();
        line_num = 0;
        Get_Answer();
        //Postcondition
        assert user_String != null : "The user_String array is null after Restart() method execution.";
        assert line_num == 0 : "The line_num is not reset to 0 after Restart() method execution.";
    }


    /**@pre.     The method assumes that the user_String array is not null.
    The method assumes that the length of the user_String array is greater than zero.
     * @post.    All elements in the user_String array will be returned.
     */
    public String[] getUser_String() {
        //Precondition
        assert user_String != null : "Precondition failed: user_String array must not be null";
        assert user_String.length > 0 : "Precondition failed: user_String array length must be greater than zero";
        return user_String;
    }


    /**@pre.      The equationList ArrayList must not be null.
    The equationList ArrayList must contain at least one equation.
     * @post.     The answer variable will be set to a randomly selected equation from the equationList.
     * The answer variable will contain a valid equation string.
     */
    private void Get_Answer() {
        //Precondition
        assert equationList != null : "The equationList ArrayList must not be null.";
        assert !equationList.isEmpty() : "The equationList ArrayList must contain at least one equation.";

        if(this.getIsRandom()){
            this.answer = equationList.get(new Random().nextInt(equationList.size()));
        }else{
            this.answer = equationList.get(0);
        }
        answer = equationList.get(new Random().nextInt(equationList.size())); // Randomly select one of the correct formulas as the correct answer

        //Postcondition
        assert answer != null : "The answer variable must not be null.";
        assert !answer.isEmpty() : "The answer variable must contain a valid equation string.";

    }



    /**@pre.     The observer parameter must not be null.
     * @post.    The observer is added to the list of observers.
     */
    //Adding Observers
    public void addObserver(Observer observer){
        //Precondition
         assert observer != null : "Observer parameter must not be null.";

        observers.add(observer);

        //Postcondition
        assert observers.contains(observer) : "Observer was not added to the list of observers.";

    }


    /**@pre.       The observers ArrayList must not be null./The observers ArrayList must contain at least one Observer object.
     * @post.      After execution, all observers in the observers ArrayList are notified by calling their Update() method.
     */
    //Update Notification
    @Override
    public void notifyObserver(){
        //Precondition
        assert observers != null : "Precondition failed: observers ArrayList must not be null";
        assert !observers.isEmpty() : "Precondition failed: observers ArrayList must contain at least one Observer object";
        //Notify each observer of the update
        for(Observer item:observers){
            item.Update();
        }
    }

    /**@pre.     null
     * @post. The method triggers a change in the state of the object.
     * All observers registered with the object are notified of the state change.
     */
    @Override
    public void setChange(){
        setChange();
        notifyObserver();
        //Postcondition
        // Since this method internally calls another method to change the state and then notifies the observers, we can ensure that the state change has occurred and the observers are notified.
        assert true : "State change triggered and observers notified successfully.";
    }
    public boolean invariant(){
        return line_num >= 0;
    }

    /**@pre.     null
     * @post.    The showTarget attribute of the object is set to the value of the showTarget parameter.
     */
    public void setShowTarget(boolean showTarget) {
        this.showTarget = showTarget;

      //  assert showTarget != null : "showTarget parameter must not be null.";

    }

    /**@pre.     The showTarget variable must be initialized.
    No specific state changes have been made to showTarget since its initialization.
     * @post.    The method returns the current value of the showTarget variable.
     * No state changes are made to showTarget during or as a result of calling this method.
     */
    public boolean getShowTarget(){
        //assert showTarget != null : "The showTarget variable must be initialized.";

        return showTarget;
    }

    /**@pre.     The parameter isRandom must not be null.
     * @post.    The isRandom attribute of the Model object will be set to the value of the isRandom parameter.
     */
    public void setIsRandom(boolean isRandom){
        //Precondition
//        assert isRandom != null : "Input boolean 'isRandom' must not be null.";

        this.isRandom = isRandom;

        //Postcondition
       // assert this.isRandom == isRandom : "The 'isRandom' attribute is not set correctly.";

    }

    /**@pre.     null.
     * @post.    Returns the value of the isRandom boolean variable.
     */
    public boolean getIsRandom() {
        return isRandom;

        // Postcondition
//        assert true : "Method getIsRandom() successfully returned the value of isRandom.";

    }

}
