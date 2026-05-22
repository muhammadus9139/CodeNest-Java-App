class IntStack {
    int top = -1;
    int arr[] = new int[100];

    void push(int x) {
        arr[++top] = x;
    }

    int pop() {
        return arr[top--];
    }

    boolean isEmpty() {
        return top == -1;
    }
}