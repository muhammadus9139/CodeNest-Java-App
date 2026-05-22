class Evaluation {

    static int evaluate(String postfix) {
        IntStack stack = new IntStack();

        for (int i = 0; i < postfix.length(); i++) {
            char ch = postfix.charAt(i);

            // agar number ho
            if (Character.isDigit(ch)) {
                stack.push(ch - '0');
            }
            // agar operator ho
            else {
                if (stack.isEmpty()) return -1;

                int val2 = stack.pop(); // val2 is liye pehly likha ha q k stack ma LIFO hota ha
                // or is liye q k postfix me right operand pehle stack se nikalta hai

                if (stack.isEmpty()) return -1;
                int val1 = stack.pop();

                switch (ch) {
                    case '+': stack.push(val1 + val2);
                    break;
                    case '-': stack.push(val1 - val2);
                    break;
                    case '*': stack.push(val1 * val2);
                    break;
                    case '/': stack.push(val1 / val2);
                    break;
                }
            }
        }

        return stack.pop();
    }
}