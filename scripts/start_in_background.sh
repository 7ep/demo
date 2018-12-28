#!/bin/sh

#this script will check to see if the application is running
# on port 8888.  If it finds that, it assumes it is running, 
# and then stops it, and starts it.

# if it doesn't find that, it assumes it's not running and 
# tries to start it.
if ss -nat|grep 8888 ; then
   echo "gretty running.  About to stop it.."
   ./stop.sh
   echo "now starting again..."
   ./start.sh &
else
   echo "gretty not running.  Starting it..."
   ./start.sh &
fi

