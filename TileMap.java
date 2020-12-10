import java.util.*;
import java.io.*;

public class TileMap {
    // Terrain map colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE_BOLD = "\033[1;37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    private Map<Arguments, Path> arguments;

    // Instantiates a new TileMap
    public TileMap() {
        arguments = new HashMap<>();
    }

    // Takes a starting and an ending Tile and finds the lowest cost path from the start to the end
    public Path pathfind(Tile start, Tile end) {
        if (start == end) {
            return new Path(true);
        }
        return pathfind(start, end, new Stack<Tile>());
    }

    private Path pathfind(Tile start, Tile end, Stack<Tile> path) {
        if (start == end) {
            return new Path(start); // Edge case
        }
        Arguments argument = new Arguments(start, end);
        if (arguments.containsKey(argument)) {
            return arguments.get(argument); // Memoization
        }
        Path result = new Path(start);
        path.push(start);
        Path next = new Path(false);
        for (Tile t : start.adjacent) {
            if (!path.contains(t)) {
                Path p = pathfind(t, end, path); // Choose, explore, unchoose
                if (next.cost() > p.cost()) {
                    next = p;
                }
            }
        }
        path.pop();
        result.add(next);
        arguments.put(argument, result);
        return result;
    }

    // Converts an int-based representation of a map into a Tile-based representation
    public Tile[][] convert(int[][] map) {
        Tile[][] tileMap = new Tile[map[0].length][map.length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i == 0 || i == map.length - 1) {
                    if (j == 0 || j == map[i].length - 1) {
                        tileMap[j][i] = new Tile(map[j][i], 2, "(" + i + ", " + j + ")");
                    } else {
                        tileMap[j][i] = new Tile(map[j][i], 3, "(" + i + ", " + j + ")");
                    }
                } else {
                    if (j == 0 || j == map[i].length - 1) {
                        tileMap[j][i] = new Tile(map[j][i], 3, "(" + i + ", " + j + ")");
                    } else {
                        tileMap[j][i] = new Tile(map[j][i], 4, "(" + i + ", " + j + ")");
                    }
                }
            }
        }
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if (i != 0) {
                    tileMap[j][i].addAdjacent(tileMap[j][i - 1]);
                }
                if (i != tileMap.length) {
                    tileMap[j][i].addAdjacent(tileMap[j][i + 1]);
                }
                if (j != 0) {
                    tileMap[j][i].addAdjacent(tileMap[j - 1][i]);
                }
                if (i != tileMap[i].length) {
                    tileMap[j][i].addAdjacent(tileMap[j + 1][i]);
                }
            }
        }
        return tileMap;
    }

    // Generates a map of square tiles
    public Tile[][] generateSquares(int rows, int columns) {
        Tile[][] result = new Tile[rows][columns];
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int adjacents = 2;
                if (i != 0 && i < rows - 1) {
                    adjacents += 1;
                }
                if (j != 0 && j < columns - 1) {
                    adjacents += 1;
                }
                Tile current = new Tile(random.nextInt(10), adjacents, "(" + i + ", " + j + ")");
                if (i != 0) {
                    current.addAdjacent(result[i - 1][j]);
                    result[i - 1][j].addAdjacent(current);
                }
                if (j != 0) {
                    current.addAdjacent(result[i][j - 1]);
                    result[i][j - 1].addAdjacent(current);
                }
                result[i][j] = current;
            }
        }
        return result;
    }

    // Creates a String representation of a Tile based map
    public String printSquares(Tile[][] map) {
        String result = "";

        for (Tile[] row : map) {
            for (Tile t : row) {
                result += t.cost + " ";
            }
            result += "\n";
        }

        return result;
    }

    // Creates a String representation of a Tile based map
    public String printSquaresColor(Tile[][] map) {
        String result = "";

        for (Tile[] row : map) {
            for (Tile t : row) {
                result += ANSI_GREEN_BACKGROUND + ANSI_WHITE_BOLD + t.cost + " " + ANSI_RESET;
            }
            result += "\n";
        }

        return result;
    }

    private class Path {
        private int cost;
        private boolean valid;
        private PathNode front;

        public Path(boolean valid) {
            this.cost = 0;
            this.valid = valid;
        }

        public Path(Tile start) {
            PathNode p = new PathNode(start);
            this.front = p;
            this.cost = start.cost;
            this.valid = true;
        }

        public void add(Tile tile) {
            if (front != null) {
                PathNode current = front;
                while (current.next != null) {
                    current = current.next;
                }
                PathNode p = new PathNode(tile);
                current.add(p);
                cost += tile.cost;
            } else {
                front = new PathNode(tile);
            }
        }

        public void add(Path path) {
            PathNode current = front;
            while (current.next != null) {
                current = current.next;
            }
            current.next = path.front;
            cost += path.cost();
            if (cost < 0) {
                valid = false;
            }
        }

        public int cost() {
            if (!valid) {
                return Integer.MAX_VALUE;
            }
            return cost;
        }


        public String toString() {
            String result = "";
            PathNode current = front;
            while (current != null) {
                if (!result.isEmpty()) {
                    result += "->";
                }
                result += current.tile.name;
                current = current.next;
            }
            return result;
        }

    }

    private class PathNode {
        private Tile tile;
        private int cost;
        private PathNode next;

        public PathNode(Tile start) {
            this.tile = start;
            this.cost = start.cost;
        }

        public void add(PathNode next) {
            this.next = next;
        }

        public int cost() {
            return cost;
        }
    }

    private static class Tile {

        private int cost;
        private Tile[] adjacent;
        private int numAdjacents;
        private String name;

        public Tile(int cost, int adjacents) {
            this.cost = cost;
            this.adjacent = new Tile[adjacents];
            this.numAdjacents = 0;
            this.name = "";
        }

        public Tile(int cost, int adjacents, String name) {
            this.cost = cost;
            this.adjacent = new Tile[adjacents];
            this.numAdjacents = 0;
            this.name = name;
        }

        public Tile(int cost, Tile[] adjacent) {
            this.cost = cost;
            this.adjacent = adjacent;
        }

        public void addAdjacent(Tile tile) {
            adjacent[numAdjacents] = tile;
            numAdjacents += 1;
        }
    
        public String toString() {
            return name;
        }

    }

    private static class Arguments {
        public final Tile start;
        public final Tile end;

        public Arguments(Tile start, Tile end) {
            this.start = start;
            this.end = end;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Arguments)) {
                return false;
            }
            Arguments other = (Arguments) o;
            return this.start == other.start && this.end == other.end;
        }

        public int hashCode() {
            return Objects.hash(start, end);
        }

        public String toString() {
            return start + " " + end;
        }

    }

    // 
    public static void definePath(TileMap tileMap, String[] args, Scanner scanner) {
        Tile[][] map = tileMap.generateSquares(50, 50);
        System.out.println(tileMap.printSquares(map));
        System.out.println(tileMap.printSquaresColor(map));

        int startx;
        int starty;
        int endx;
        int endy;
        do {
            System.out.print("Start x (between 0 & 49): ");
            startx = scanner.nextInt();
            System.out.print("Start y (between 0 & 49): ");
            starty = scanner.nextInt();
            System.out.print("Start x (between 0 & 49): ");
            endx = scanner.nextInt();
            System.out.print("Start y (between 0 & 49): ");
            endy = scanner.nextInt();
            System.out.println();
        } while (startx >= 50 || endx >= 50 || starty >= 50 || endy >= 50 ||
            startx < 0 || endx < 0 || starty < 0 || endy < 0);

        Path p = tileMap.pathfind(map[startx][starty], map[endx][endy]);
        System.out.println(p);
        System.out.println("cost: " + p.cost());
        scanner.nextLine();

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
                    definePath(tileMap, args, scanner);
                case "E":
                    menuOptionSelected = true;
                    System.exit(0);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("TileMap");
        definePath(new TileMap(), args, scanner);
    }
}
