import java.util.Stack;

public class BacktrackingMazeSolver {

    /**
     * Moves a rat from (x1,y1) to (x2,y2), filling in the cells as it goes, and
     * notifying a listener at each step.
     */
    public boolean solve(Maze maze, Maze.MazeListener listener) {

        // TODO - if listener is null, throw an IllegalArgumentException
        // saying "Listener cannot be null"
        if(listener == null){
            throw new IllegalArgumentException("Listener cannot be null");
        }

        var path = new Stack<Maze.Location>();

        // TODO: initialize the current location to the initial rat location
        Maze.Location current = maze.getInitialRatPosition();

        // Solution loop. At each step, place the rat and notify listener.
        while (true) {

            /*
            there are two ways to see if the rat reached the cheeze
            first: you test the current cell content for cheeze, if yes you return true
            (it's like ahead of placing checking)
            second: after you place the rat you check the whole maze for no cheeze, if yes 
            you return true, and since we have no way to access the private cells multi dim array
            we are stuck with the first way 
            */

            if(current.contents() == Maze.Cell.CHEESE){
                return true;
            }

            // TODO: Place the rat in the current cell
            current.place(Maze.Cell.RAT);
            // TODO: Notify the listener
            listener.mazeChanged(maze);
            // TODO: Did we reach the desired end cell? If so, return true
            // see above note
                  

            if (current.above().canBeMovedTo()) {
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.above();
            } else if (current.toTheRight().canBeMovedTo()) {
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.toTheRight();
            } else if (current.below().canBeMovedTo()) {
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.below();
            } else if (current.toTheLeft().canBeMovedTo()) {
                path.push(current);
                current.place(Maze.Cell.PATH);
                current = current.toTheLeft();
            } else {
                current.place(Maze.Cell.TRIED);
                if(path.empty()){
                    return false;
                }else{
                    current = path.pop();
                }
            }
        }
    }
}
