import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testValidateExpression() {
        // Preparing test data
        Model model = new Model(false, true);
        model.Add_String("2");
        model.Add_String("+");
        model.Add_String("3");
        model.Add_String("*");
        model.Add_String("1");
        model.Add_String("/");
        model.Add_String("5");

        // execute a test
        int[] result = model.validateExpression();

        // assert
        assertArrayEquals(new int[]{-1}, result); // Without the equal sign, the expected result should return {-1}
    }


    @Test
    public void inputFieldTest() {
        Model model = new Model(false, true);

        // Scenario 2: Input Field Limit Reached
        model.Clear_All();
        model.Add_String("1");
        model.Add_String("2");
        model.Add_String("3");
        model.Add_String("4");
        model.Add_String("=");
        model.Add_String("5");
        model.Add_String("6");
        assertFalse(model.user_input()); // Expecting input field limit reached
    }


    @Test
    public void testNewGame() {
        Model model = new Model(false, true);

        // Simulate a game with progress
        model.Add_String("1");
        model.Add_String("+");
        model.Add_String("2");
        model.Add_String("=");
        model.Add_String("3");
        model.validateExpression();
        model.setLine_num(2);

        // Restart the game
        model.Restart();

        // Verify game is reset
        assertNull(model.getUser_String()[0]); // Assertion: expecting user string at index 0 to be null
        assertEquals(0, model.getLine_num());  // Assertion: expecting line number to be 0
    }
}