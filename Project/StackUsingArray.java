package Project;
public class StackUsingArray<T extends Comparable<T>> {
    private static int stackSize = 0;
    private static BTNode[] stackArray = new BTNode[0];
    private static int stackTop;
    public StackUsingArray(int cap) {
        stackSize = cap;
        stackArray = new BTNode[stackSize];
        stackTop = -1;
    }
    
    public BTNode pop() {
        return stackArray[stackTop--];
    }

    public static BTNode peek() {
        return stackArray[stackTop];
    }

    public static boolean isEmpty() {
        return (stackTop == -1);
    }

    public static boolean isFull() {
        return (stackTop == stackSize - 1);
    }

    public void push(BTNode j) {
        stackArray[++stackTop] = j;
    }

    public static int size() {
        return stackTop + 1;
    }
}