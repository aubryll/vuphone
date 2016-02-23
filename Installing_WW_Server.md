## Overall Steps ##

  1. Download project into eclipse
  1. Modify the DB.properties file
  1. Export a runnable jar file
  1. Put jar file on server you want to run
  1. Start the server
  1. [Optional](Optional.md) Modify Map.html
  1. Live long and prosper


TODO - Put in all the screenshots!
[downloading](http://www.dre.vanderbilt.edu/~hturner/vandyupon-screenshots/step1-project-into-eclipse.zip)
[properties](http://www.dre.vanderbilt.edu/~hturner/vandyupon-screenshots/step2-modify-properties.zip)
[export](http://www.dre.vanderbilt.edu/~hturner/vandyupon-screenshots/step3-build-n-export.zip)
[moving](http://www.dre.vanderbilt.edu/~hturner/vandyupon-screenshots/step4-moving.zip)
[starting](http://www.dre.vanderbilt.edu/~hturner/vandyupon-screenshots/step5-starting.zip)

### More about Downloading into eclipse ###
You will need to install eclipse from http://www.eclipse.org/downloads/ - the Eclipse IDE For Java Developers is fine. Also install the Subclipse plugin.
New project->other->project from SVN
Add the svn repo
Locate the project in truck->projects->events-server
click finish

Screenshots of 1

### More about modifying the db.properties file ###

Screenshots

### More about exporting ###

run as java application, and choose the main class.

This is where the server might fail. Failure could mean something you previously defined is wrong (the DB username / password or the sql constructer class name, or the CREATE statements in the class you are using to create the sql) or that you are simply on the wrong machine. A "communications link failure" is generally the only "ok" error to get at this point

screenshots

### more about putting on server ###
here I use scp, you can easily use ftp or other means

screenshots

### more about starting the server ###

locate java on your machine with the command "which java"
create a script that will start the server. We name the example "server\_start"
fill in "server\_start" with the location of java, the name of the jar file, and the port you would like to start the server on
Note that starting on low ports (aka below 1000ish) requires elevated (aka root) privileges so I would recommend starting this on a port such as 8080 if you are new to this
add execution rights to server\_start with the chmod command
run the server (if the final line contains this then you are good - INFO:  Started SocketConnector@0.0.0.0:8080)
if you run into problems with errors like "PORT already in use" the try another port (use the command "lsof -i :port\_number" to see what is running on that port


screenshots

### More about [Optional](Optional.md) Modify Map.html ###
You only need to do this if you want the web interface (aka the map) to work
go into wherever you put map.html onto your web server
change the line that says var server = blah to var server = "http://www.myserver:myport"; Do NOT put an ending "/"

get your own google maps API key :)

screenshots


### more about living long and prospering ###