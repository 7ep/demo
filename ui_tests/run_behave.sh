#!/bin/sh

cd ~/demo_tests/ui_tests
PATH=/usr/lib64/qt-3.3/bin:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/home/brk/.local/bin:/home/brk/bin
DISPLAY=:0 pipenv run behave
