# Processing N number of Jobs using M number of Machines

## Note
This repository contains 4 codes in total - 2 codes in Java (1 with GUI and 1 without GUI) , and 2 in Python (1 with GUI and 1 without GUI).  

## Overview

This project is a Java-based GUI application for job scheduling with a focus on minimizing idle time across multiple machines. The application allows users to input processing times for jobs and machines, then calculates and displays the optimal sequence of jobs, minimum elapsed time, and idle times for each machine.

## Usage

1. Start the Application:
Click the "Get Started" button to proceed to the job scheduling interface.

2. Input Data:
a. Enter the number of machines and jobs.
b. Provide the processing times in the specified format.

3. View Results:
After clicking the "Calculate" button, the application will display the optimal job sequence, minimum elapsed time, and idle times for each machine.

### Example Input
For a setup with 5 machines and 4 jobs, you might enter the following processing times:

14 10 4 6 18 12 12 8 10 20 10 8 10 12 16 16 6 6 4 12

### Expected Output

Optimal Sequence: 1  3  2  4  

Minimum Elapsed Time: 102

Idle Time for Each Machine:
Machine 1: 50
Machine 2: 66
Machine 3: 74
Machine 4: 70
Machine 5: 36
