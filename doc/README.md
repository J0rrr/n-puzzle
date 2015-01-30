To launch the app, do as follows:

Open eclipse, and navigate to the 'Workspace' folder in this projects directory

In Workspace\src\nl\mprog\projects\npuzzle10875875, open any of the .java files.

Press 'run' in eclipse, choose what (virtual) device to run the app on, and pres ok.

The app should now run on your chosen (virtual) device.






While programming, sometimes the choices made in the design document were changed, as they didn't seem appropriate.
The options menu, for example, is in the final app not a seperate activity, but a little pop-up menu on top of the GamePlay activity.
Another difference between design doc and implementation is the choice for a gridview instead of a tablelayout. This seemed easier to implement.
The methods described in the design document also differ from the actual implementation.
The method 'Draw' didn't seem to be necessary anymore, since the gridview adapther has a 'notifyDataChanged' method.
Checking the movement and moving (swapping) the tiles happens within the 'handleClick' method, these aren't seperate methods anymore.
