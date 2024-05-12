import java.util.Scanner;

public class GUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a boolean value of isRandom (true/false): ");
        boolean isRandom = scanner.nextBoolean();
        System.out.println("Enter a boolean value of showTarget (true/false): ");
        boolean showTarget = scanner.nextBoolean();
        Model model = new Model(showTarget, isRandom);
        Controller page_control=new Controller(model);
        page_control.Start_Game();
    }
}
