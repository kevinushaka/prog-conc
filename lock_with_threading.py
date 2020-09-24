# -*- coding: utf-8 -*-
"""
Created on Thu Sep 24 12:06:02 2020

@author: Kévin
"""
import threading
import time
               
class Counter(object):
    def __init__(self, start = 0):
        self.lock = threading.Lock()
        self.value = start
    def increment(self):
        print('Waiting for a lock')
        self.lock.acquire()
        try:
            print('Acquired a lock')
            self.value = self.value + 1
        finally:
            print('Released a lock')
            self.lock.release()

def worker(c):
    c.increment()
    print('Done')

if __name__ == '__main__':
    my_list=[]
    counter = Counter()
    for i in range(2):
        t = threading.Thread(target=worker, args=(counter,))
        my_list.append(t)
        t.start()

    print('Waiting for worker threads')
    main_thread = threading.currentThread()
    for t in my_list:
        t.join()
    print('Counter:'+ str(counter.value))