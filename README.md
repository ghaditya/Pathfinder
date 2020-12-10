# User Guide
The goal of the project is to implement two different methods of finding the shortest path on a 
given generated map, accomplished with two different branch programs - TileMap and ArrayMap.


## Project Description
ArrayMap and TileMap differences:

ArrayMap features tiles in which there is no "cost" to move to each tile, meaning all tiles can 
be moved onto the same way. This is with the exception of obstacle tiles, which cannot be moved 
onto at all. As a result, this part of the program will generate all possible shortest paths in 
terms of the direction moved (i.e. R representing moving the tile to the right). ArrayMap also 
randomizes the starting and ending locations, giving the user a bit less flexibility in designing
the terrain.

On the other hand, TileMap features tiles (custom Tile object) in which each has a random cost
attributed to it. The goal of this part of the project is to generate the shortest route from a
user inputted start tile and end tile based on on the cost of the route. The resulting route
shows the traversal as a series of x and y coordinate pairs. In TileMap, the user has the ability
to choose both the starting and ending tile after viewing the cost-numbered terrain.


## Running the project
1. Open new terminal window
2. """ java Main.java """
3. Follow the program instructions
   A. Entering "A" opens the ArrayMap
      1. Enter the desired dimensions of your ArrayMap
         M. Entering "M" returns you to the main menu
         T. Entering "T" resets the ArrayMap
         E. Entering "E" exist the program
   T. Entering "T" opens the TileMap
      1. Enter the desired starting location coordinates
      2. Enter the desired ending location coordinates
         M. Entering "M" returns you to the main menu
         T. Entering "T" resets the TileMap
         E. Entering "E" exist the program
   E. Entering "E" exits the program


## How to Read Results
-> Reading the ArrayMap: Upon entering in coordinates for the generated ArrayMap, the first
image one will be greated with is an image of a square represented with integer values,
either 0, 1, 2, or 9:
   0s represent a traversable tile
   1 represents the randomized start tile
   2 represents the randomized end tile
   9s represent an untraversable obstacle
The second image is represented with colored symbols:
   The white "A" is the starting location
   The white "B" is the ending locaiton
   Green "U"s are traversable terrain
   Blue "="s are obstacles
The final image is the bird's-eye view of the simulated terrain, showing green land with
white mountains and blue water manipulating the path between the user (the white smiling
emoji) and the desination (the white house). The shortest possible routes will follow,
represented by a series of Ls, Us, Rs, and Ds for the tile directions (Left, Up, Right, Down)
one must travel to in order to traverse the shortest possible routes.

-> Reading the TileMap: Upon running the TileMap program a 50x50 map is generated with
numbers 0-9 that correspond to each specfic randomized tile. The numbers for each tile
represent to a "cost" value, or the value it would take to traverse that tile (for
real-world purposes we could call this cost a unit of time). As an example, a tile with
a designated value of 9 would cost 9 units to traverse. After entering the desired starting
and ending coordinate pairs, the path of least cost will be illustrated as a series of
coordinate pairs, with arrows in between each pair illuminating the most cost-efficient
route of travel.


### Instructional Video
See the video [here](http://youtube.com) or visit http://youtube.com.
