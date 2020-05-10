import java.util.stream.Stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {

    // A maze is a rectangular array of cells. The reason we use arrays is that
    // the maze has a fixed size, and arrays are the fastest when indexing by
    // position, which is exactly what we do when we search a maze.
    private final Cell[][] cells;

    private Location initialRatLocation;
    private Location initialCheeseLocation;

    /**
     * Builds and returns a new maze given a description in the form of an array
     * of strings, one for each row of the maze, with each string containing o's
     * and w's and r's and c's. o=Open space, w=Wall, r=Rat, c=Cheese.
     *
     * The maze must be rectangular and contain nothing but legal characters. There
     * must be exactly one 'r' and exactly one 'c'.
     *
     * The constructor is private to force users to only construct mazes through one
     * of the factory methods fromString, fromFile, or fromScanner.
     */
    private Maze(String[] lines) {
        // TODO: Fill this in. There is a lot to check for! The maze must be
        // perfectly rectanglar, not contain any illegal characters, have exactly
        // one rat (not less, not more), and have exactly one cheese (not less,
        // not more).

        int len = lines[0].length();


        this.cells = new Cell[lines.length][len];
        this.initialCheeseLocation = null;
        this.initialRatLocation = null;


        for (int i = 0; i < lines.length; i++) {
            if(lines[i].length() != len){
                throw new IllegalArgumentException("Non-rectangular maze");
            }
            var Chars = lines[i].toCharArray(); 
            for (int j = 0; j < Chars.length; j++) {
                switch  (Chars[j]){
                    case 'o':
                        this.cells[i][j] = Maze.Cell.OPEN;
                        break;
                    case 'w':
                        this.cells[i][j] = Maze.Cell.WALL;
                        break;
                    case 'r':
                        this.cells[i][j] = Maze.Cell.RAT;
                        if(this.initialRatLocation == null){
                            this.initialRatLocation = new Location(i, j);
                        }else{
                            throw new IllegalArgumentException("Maze can only have one rat");
                        }  
                        break;
                    case 'c':
                        this.cells[i][j] = Maze.Cell.CHEESE;
                        if(this.initialCheeseLocation == null){
                            this.initialCheeseLocation = new Location(i, j);
                        }else{
                            throw new IllegalArgumentException("Maze can only have one cheese");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unknowen cell kind!!!!");
                                       
                }
            }
        }

        if(this.initialRatLocation == null){
            throw new IllegalArgumentException("Maze has no rat");
        }
        
        if(this.initialCheeseLocation == null){
            throw new IllegalArgumentException("Maze has no cheese");
        }
    }

    public static Maze fromString(final String description) {
        return new Maze(description.trim().split("\\s+"));
    }

    public static Maze fromFile(final String filename) throws FileNotFoundException {
        return Maze.fromScanner(new Scanner(new File(filename)));
    }

    public static Maze fromScanner(final Scanner scanner) {

        var listStrings = new ArrayList<String>(); 

        while(scanner.hasNext()){
            listStrings.add(scanner.nextLine());
        }

        return new Maze((String[])listStrings.toArray());
    }

    /**
     * A nice representation of a Location, so we don't have to litter our code
     * with separate row and column variables! A location object bundles these
     * two values together. It also includes a whole bunch of nice little methods
     * so that our code reads nicely.
     */
    public class Location {
        private final int row;
        private final int column;

        Location(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        boolean isInMaze() {
            int errors = 0;
            if((this.row < 0) || (this.row >= getHeight())){
                errors++;
            }
            if((this.column < 0) || (this.column >= getWidth())){
                errors++;
            }
            return errors == 0; 
        }

        boolean canBeMovedTo() {
            return isInMaze() && (hasCheese() || (contents() == Maze.Cell.OPEN));
        }

        boolean hasCheese() {
            return contents() == Maze.Cell.CHEESE;
        }

        Location above() {
            return new Location(this.row - 1, this.column);
        }

        Location below() {
            return new Location(this.row + 1, this.column);
        }

        Location toTheLeft() {
            return new Location(this.row, this.column - 1);
        }

        Location toTheRight() {
            return new Location(this.row, this.column + 1);
        }

        void place(Cell cell) {
            cells[row][column] = cell;
        }

        Cell contents() {
            return cells[row][column];
        }

        boolean isAt(final Location other) {
            return (this.row == other.row) && (this.column == other.column);
        }
    }

    /**
     * A simple cell value. A cell can be open (meaning a rat has never visited it),
     * a wall, part of the rat's current path, or "tried" (meaning the rat found it
     * to be part of a dead end.
     */
    public static enum Cell {
        OPEN(' '), WALL('\u2588'), TRIED('x'), PATH('.'), RAT('r'), CHEESE('c');

        private char kind;
 
        public String toString(){
            return "" + this.kind;
        }
    
        // enum constructor - can not be public or protected
        Cell(char kind){
            this.kind = kind;
        }
    }

    public interface MazeListener {
        void mazeChanged(Maze maze);
    }

    public int getWidth() {
        return cells[0].length;
    }

    public int getHeight() {
        return cells.length;
    }

    public Location getInitialRatPosition() {
        return this.initialRatLocation;
    }

    public Location getInitialCheesePosition() {
        return this.initialCheeseLocation;
    }

    /**
     * Returns a textual description of the maze, separating each row with a newline.
     */
    public String toString() {
        return Stream.of(cells)
            .map(row -> Stream.of(row).map(Cell::toString).collect(joining()))
            .collect(joining("\n"));
    }
}
