import java.util.Scanner;

/**
 * Animates a backtracking maze solver. If no command line arguments are given, the
 * maze description is read from standard input. If a single command line argument
 * is given, it is assumed to be a file name and program will attempt to read the
 * maze description from that file.
 */
public class ConsoleBacktrackingMazeSolver {

    private static String CLEAR_SCREEN = "\u001B[2J";
    private static String CURSOR_HOME = "\u001B[1;1H";

    private static Maze.MazeListener listener = new Maze.MazeListener() {
        public void mazeChanged(Maze maze) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            System.out.println(CLEAR_SCREEN + CURSOR_HOME + maze);
            System.out.println();
        }
    };

    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                throw new IllegalArgumentException("Program requires 0 or 1 arguments");
            }
            var maze = (args.length == 1) ? Maze.fromFile(args[0])
                    : Maze.fromScanner(new Scanner(System.in));

            var success = new BacktrackingMazeSolver().solve(maze, listener);
            System.out.println(success ? "Made it! " : "Poor ratty!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

