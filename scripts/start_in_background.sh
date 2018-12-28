#!/bin/sh

#this script will check to see if the application is running
# on port 8888.  If it finds that, it assumes it is running, 
# and then stops it, and starts it.

# if it doesn't find that, it assumes it's not running and 
# tries to start it.

# get the directory of where this script is located
# we will use it to call further scripts.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

if ss -nat|grep 8888 ; then
   echo "gretty running.  About to stop it.."
   $DIR/stop.sh
   echo "now starting again..."
   $DIR/start.sh &
else
   echo "gretty not running.  Starting it..."
   $DIR/start.sh &
fi

