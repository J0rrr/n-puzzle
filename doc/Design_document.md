#### Design Document n-puzzle
## Describes the choices that were made for the creation of the android app 'n-puzzle'

Jordi van Ditmar
Studentnumber: 10875875


# Introduction
N-puzzle is a game where a picture is divided into tiles, which are then shuffled. One tile is left out, so that the tiles directly next to it can move to that empty space. It is up to the player to rearrange these tiles in such a manner that the original picture can be seen again.


# Description of features
For this specific project, a certain set of options have to be implemented: the player must be able to choose between some pictures, the player must be able to choose between difficulties (easy: 3x3, medium: 4x4 and hard: 5x5 board size). Finally, at any point during the game, the player should be able to restart by ‘reshuffling’ the board.

The following figure (see picture ‘design_doc pic 1’ in the same directory as the design doc) is a very basic visualization of what the app should look like. Upon starting the app, it should prompt the player to choose an image. After image selection, the app should display the solved puzzle for about 3 seconds, prior to going to the shuffled puzzle (to be solved). While puzzling, the ‘options’-button should lead the player to a little menu where the image and difficulty can be chosen, along with a button to reshuffle the current puzzle. Changing either the image or difficulty will also lead to a reshuffled puzzle (after the solution is shown for 3 seconds again).



# Inner working of the app

Each tile will be represented by a number. The first tile (upper left in the solved configuration of the board) will be numbered with a 1, the one (to the right of the first one) will be numbered with a 2, etc. etc. The board will be represented by a 2-dimensional array of numbers (the tiles).

The activity of the actual game will consist of a tablelayout with each entry consisting of a cropped part of the image. Upon clicking on a tile, an ‘OnClickListener’ will trigger the following methods to excecute:

Check_movement(board, tile)
Checks if the selected tile is next to the empty tile, e.g. checks if the player selected a valid move. Returns true if so, returns false (with an appropriate toast message) if not so.

Move(board, tile)
Executes the actual move. Returns the board where the move has been executed.

Draw(board, list_of_tile_images)
Draws the board to the display, returns true when successful.

Win(board)
Checks whether the game is won. Returns true if so, or false if not so.

Apart from these methods, the board also has to be initialized. To do so, the following extra methods are needed:

Generate_tiles(Image, board_size, screen_size)
Crops the image into appropriately sized images and assigns them with the right tile number. Returns this as list_of_tile_images.

Shuffle(board)
Shuffles the board to a new (unsolved) board in such a manner that the puzzle can still be solved. (Initially in the way described by the projects description, but later in a more random way). Returns the shuffled board (as board).

