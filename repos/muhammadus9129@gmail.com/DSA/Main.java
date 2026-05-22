import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Postfix Expression: ");
        String postfix = sc.nextLine();

        int result = Evaluation.evaluate(postfix);

        System.out.println("Result: " + result);
    }
}