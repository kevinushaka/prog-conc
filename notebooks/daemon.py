import multiprocessing
import time
import sys

def daemon():
    p = multiprocessing.current_process()
    print ('Starting:', p.name, p.pid)
    sys.stdout.flush()
    time.sleep(2)
    print ('Exiting :', p.name, p.pid)
    sys.stdout.flush()
