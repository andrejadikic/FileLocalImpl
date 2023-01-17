# LocalFileStorage
This project presents implementation of API specification which
can be found [here](https://github.com/Kovelja009/FileManager).

LocalFileStorage has concrete implementation of each function
from API spec with error handling. Whole concept is based on
prompting user to enter file storage destination. User can choose
whether he wants to create new Local Storage or to use existing one.
Then he enters the path to the Local file storage and <b>createStorage</b>
method takes care of the rest (error handling if path is bad or there is
an existing file storage). User can also provide JSON file which represents
custom configuration for created File storage. 

JSON config can contain: 
* Storage name 
* Max storage size
* Directories with custom capacity
* Forbidden extensions

Other supported options are:
* Directory creation
  * createDirectory - with provided path, folder name and capacity
  * createDirectories - with provided path and folder creation pattern
  * createDir - with provided path and folder name

* File creation
  * createFile - with provided path and file name including extension

* File moving
  * moveFile - with provided path to file to be moved, destination path
  * moveFiles - with provided path to directory which files should be moved to provided location

* File renaming
* File copying
* Directory copying
* File and Directory download
