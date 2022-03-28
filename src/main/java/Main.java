import helpers.InputOutput;

public class Main {
    public static void main(String[] args) {
        /**
         * example of using kotlin in java on object
         * (i.e. singleton)
         * (i.e. I'm using it like a class with static functions only)
         * my inglish is bed is bed i ogorcheniy
         * */
       var list = InputOutput.INSTANCE.readFileAsLines("C:\\Users\\Mi\\Desktop\\Test.kt");
       InputOutput.INSTANCE.printList(list);
    }
}
