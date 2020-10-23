import multiprocessing
import time
import sys

def non_daemon():
    p = multiprocessing.current_process()
    print ('Starting:', p.name, p.pid)
    sys.stdout.flush()
    print ('Exiting :', p.name, p.pid)
    sys.stdout.flush()
