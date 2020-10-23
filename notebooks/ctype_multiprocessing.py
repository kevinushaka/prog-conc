# -*- coding: utf-8 -*-
"""
Created on Thu Sep 24 13:27:45 2020

@author: Kévin
"""
import multiprocessing 

# function to deposit to account 
def increment(variable):     
    variable.value = variable.value + 1

if __name__ == "__main__": 
    counter = multiprocessing.Value('i', 0)

    p1 = multiprocessing.Process(target=increment, args=(counter,)) 
    p2 = multiprocessing.Process(target=increment, args=(counter,)) 
  
    p1.start() 
    p2.start() 

    p1.join();
    p2.join();

    print("Final balance = "+str(counter.value)) 