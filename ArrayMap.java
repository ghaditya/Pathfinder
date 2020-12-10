import java.util.*;

public class ArrayMap {
    // Terrain map colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE_BOLD = "\033[1;37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    // Length of row of terrain map
    private int rowSize;

    // Length of column of terrain map
    private int columnSize;

    // Store Random object
    private Random random;

    // Stores generated terrain map
    private int[][] terrain;

    // Stores the list of shortest routes
    private Map<String, Integer> routes;
    private int smallestCounter;

    // Represents starting tile
    public static final int START_VALUE = 1;

    // Represents ending tile
    public static final int END_VALUE = 2;

    // Represents obstacle tile
    public static final int OBSTACLE_VALUE = 9;

    // Instantiates a new ArrayMap
    public ArrayMap() {
        this.rowSize = 5;
        this.columnSize = 5;
        this.random = new Random(100);
        this.terrain = generate(rowSize, columnSize, random);
        this.routes = new HashMap<>();
        this.smallestCounter += terrain.length * terrain[0].length;
    }

    // Instantiates a new ArrayMap. Takes in parameters int "rowSize", int "columnSize" in order
    // to generate the map to be used for pathfinding.
    public ArrayMap(int rowSize, int columnSize, Random random) {
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.random = random;
        this.terrain = generate(rowSize, columnSize, random);
        this.routes = new HashMap<>();
        this.smallestCounter += terrain.length * terrain[0].length;
    }

    // Generates a tile map based on the inputted parameters int "rowSize", int "columnSize".
    // Randomizes the start and end tile as well as obstacle tiles.
    private int[][] generate(int rows, int columns, Random rand) {
        // All tiles are automatically designated as 0
        int[][] array = new int[rows][columns];

        // Initializes the random starting point
        array[rand.nextInt(rows)][rand.nextInt(columns)] = START_VALUE;

        // Initializes the random ending point
        boolean flag = true;
        while (flag) {
            int row = rand.nextInt(rows);
            int column = rand.nextInt(columns);
            if (array[row][column] != START_VALUE) {
                array[row][column] = END_VALUE;
                flag = false;
            }
        }

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                int generate = rand.nextInt(3);
                if (generate == 0 && array[i][j] == 0) {
                    array[i][j] = OBSTACLE_VALUE;
                    this.smallestCounter--;
                }
            }
        }

        return array;
    }

    // Traverses a given tile map parameter and prints out the shortest routes.
    public void traverse(int[][] terrain) {
        // Initialize start and end
        int xStart = 0;
        int yStart = 0;
        int xEnd = 0;
        int yEnd = 0;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (terrain[i][j] == START_VALUE) {
                    xStart = j;
                    yStart = i;
                } else if (terrain[i][j] == END_VALUE) {
                    xEnd = j;
                    yEnd = i;
                }
            }
        }
        // Traverse via private helper method
        traverse(terrain, xStart, yStart, xEnd, yEnd, "", 0);

        // Remove all routes that are longer than the shortest route
        for(Iterator<Map.Entry<String, Integer>> it = routes.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            if(entry.getValue() > this.smallestCounter) {
                it.remove();
            }
        }

        System.out.println("Shortest possible routes:"); // Add icons here from blank to blank
        if (routes.isEmpty()) {
            System.out.println("There are no possible routes");
        } else for (String route : this.routes.keySet()) {
            System.out.println(route);
        }
    }

    // Origin 0, 0 at top left. X and Y increase as we go down or the right, respectively.
    private void traverse(int[][] terrain, int x, int y, int endX, int endY, String soFar, int counter) {
        if (counter <= this.smallestCounter) { // Ensure that routes too long are not considered
            if (x == endX && y == endY) {
                // Update map containing routes and update length of shortest route
                this.routes.put(soFar, counter);
                this.smallestCounter = counter;
            } else if (x >= 0 & x < terrain[0].length & y >= 0 & y < terrain.length & counter < this.smallestCounter) {
                terrain[y][x] = -1; // -1 indicating already visited, recall that 9 indicates impassable object
                
                // If tile to move onto is valid,recurse to continue testing the route
                // Ensure that if on the edge, do not check for a tile past the bounds of the terrain map
                if (x != terrain[0].length - 1) {
                    if (terrain[y][x + 1] != OBSTACLE_VALUE & terrain[y][x + 1] != -1) { 
                        traverse(terrain, x + 1, y, endX, endY, soFar + "R", counter + 1); 
                        // Need to do more testing but without the next two statements, not all valid routes are tested. Weird...
                        terrain[y][x + 1] = -1; // Need to "unchoose" this route
                        terrain[y][x + 1] = 0;
                    }
                }
                if (x != 0) { 
                    if (terrain[y][x - 1] != 9 & terrain[y][x - 1] != -1) {
                        traverse(terrain, x - 1, y, endX, endY, soFar + "L", counter + 1);
                        terrain[y][x - 1] = -1;
                        terrain[y][x - 1] = 0;
                    }
                }
                if (y != terrain.length - 1) {
                    if (terrain[y + 1][x] != 9 & terrain[y + 1][x] != -1) {
                        traverse(terrain, x, y + 1, endX, endY, soFar + "D", counter + 1);
                        terrain[y + 1][x] = -1;
                        terrain[y + 1][x] = 0;
                    }
                }
                if (y != 0) {
                    if (terrain[y - 1][x] != 9 & terrain[y - 1][x] != -1) {
                        traverse(terrain, x, y - 1, endX, endY, soFar + "U", counter + 1);
                        terrain[y - 1][x] = -1;
                        terrain[y - 1][x] = 0;
                    }
                }
            }
        }
    }

    // Returns the terrain of this map
    public int[][] terrain() {
        return this.terrain;
    }

    // Initializes and prints the terrain map via input
    public static void showPath(int columns, int rows) {
        Random rand = new Random();
        ArrayMap arrayMap = new ArrayMap(columns, rows, rand);

        // Print out terrain map as numerical representation
        for (int[] x : arrayMap.terrain) {
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        System.out.println();

        /* Print out terrain map as color and symbol representation: 
         * A - Start tile
         * B - End tile
         * U - Movable tile
         * = - Obstacle tile (represents water)
         */

        for (int[] x : arrayMap.terrain) {
            for (int y : x) {
                System.out.print(ANSI_BLACK_BACKGROUND);
                if (y == OBSTACLE_VALUE) {
                    System.out.print(ANSI_BLUE + "=");
                } else if (y == START_VALUE) {
                    System.out.print(ANSI_WHITE + "A");
                } else if (y == END_VALUE) {
                    System.out.print(ANSI_WHITE + "B");
                } else {
                    System.out.print(ANSI_GREEN + "U");
                }
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println();

        /* Print out terrain map as visual birds-eye view: 
         * Help the character get home!
         * ☺ - Start tile
         * ⌂ - End tile
         * green tile - Movable tile
         * ▲ or blue tile - Obstacle tile (represents water)
         */

        // Sample Texture: ◙ ▓ ▒ ░ █ ■ ≡
        // Reference: https://en.wikipedia.org/wiki/Code_page_437

        for (int[] x : arrayMap.terrain) {
            for (int y : x) {
                if (y == OBSTACLE_VALUE) {
                    int generate = rand.nextInt(3);
                    if (generate == 0) {
                        System.out.print(ANSI_GREEN_BACKGROUND + ANSI_WHITE_BOLD + "▲");
                    } else {
                        System.out.print(ANSI_BLUE_BACKGROUND + " "); // ≈
                    }
                } else if (y == START_VALUE) {
                    System.out.print(ANSI_GREEN_BACKGROUND + ANSI_WHITE_BOLD + "☺"); // ○ ☺ A
                } else if (y == END_VALUE) {
                    System.out.print(ANSI_GREEN_BACKGROUND + ANSI_WHITE_BOLD + "⌂"); // ⌂ B
                } else {
                    System.out.print(ANSI_GREEN_BACKGROUND + " "); // . ∙ * '
                }
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println();
        // Traverse the terrain
        arrayMap.traverse(arrayMap.terrain());
        System.out.println();
    }

    // Allows user to access this part of the project via a menu system as well as obtain user
    // input for the terrain map, repeating until a valid combination of row and column numbers are 
    // inputted. 
    public static void definePath(String[] args, Scanner scanner) {
        // Internal testing recommends max of roughly 170 tiles total.
        final int maxUnits = 170;
        int columns = maxUnits;
        int rows = maxUnits;
        // Provide recommendations and get user input for number of rows and columns.
        while (columns * rows > maxUnits) {
            System.out.println("Total number of tiles should be 170 or less (columns times rows should be 170 or less).");
            System.out.println("Sample combinations to try: 12X12, 6X28, 10X17");
            System.out.print("Enter number of columns: ");
            columns = scanner.nextInt();
            System.out.print("Enter number of rows: ");
            rows = scanner.nextInt();
        }
        showPath(columns, rows);

        boolean menuOptionSelected = false;
        while (!menuOptionSelected) {
            System.out.println("Options: Main Menu (M), Try Again (T), Exit (E)");
            String input = scanner.next();
            switch (input.substring(0, 1).toUpperCase()) {
                case "M":
                    menuOptionSelected = true;
                    Main.main(args);
                case "T":
                    menuOptionSelected = true;
                    definePath(args, scanner);
                case "E":
                    menuOptionSelected = true;
                    System.exit(0);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("ArrayMap");
        Scanner scanner = new Scanner(System.in);
        definePath(args, scanner);
    }
}
