nom = int(input("Enter the number of machines: "))
noj = int(input("Enter the number of jobs: "))

PT = [[0] * nom for _ in range(noj)]

for i in range(noj):
    for j in range(nom):
        PT[i][j] = int(input(f"Enter processing time for Job {i + 1} on Machine {j + 1}: "))

#min PT
mptFM = float('inf')
mptLM = float('inf')

for i in range(noj):
    ptFM = PT[i][0]
    ptLM = PT[i][nom - 1]
    mptFM = min(mptFM, ptFM)
    mptLM = min(mptLM, ptLM)

#maxPT
maxPT = [max(PT[i][1:nom-1]) for i in range(noj)]

#maxofmax PT
maxmaxPT = max(maxPT)

print("\nMinimum processing time on the first machine:", mptFM)
print("Minimum processing time on the last machine:", mptLM)
print("\nMaximum processing time for each machine (except first and last):")

for j in range(1, nom - 1):
    print(f"Machine {j + 1}: {maxPT[j - 1]}")

print("Maximum of the maximum processing times:", maxmaxPT)

#condition
if mptFM < maxmaxPT and mptLM < maxmaxPT:
    print("\nCannot continue with this method. Change the inputs.")

#machine G and I claculations
machineG = [sum(PT[i][:-1]) for i in range(noj)]
machineI = [sum(PT[i][1:]) for i in range(noj)]

print("\nData stored in Machine G:")
for i in range(noj):
    print(f"Job {i + 1}: {machineG[i]}")

print("\nData stored in Machine I:")
for i in range(noj):
    print(f"Job {i + 1}: {machineI[i]}")

#optimal sequence
allocated = [False] * noj
OS = [0] * noj
count = 0

for slot in range(noj):
    mdI = float('inf')
    mdG = float('inf')
    iJI = -1
    gJI = -1

    for i in range(noj):
        if not allocated[i] and machineG[i] < mdG:
            mdG = machineG[i]
            gJI = i
    
    for i in range(noj):
        if not allocated[i] and machineI[i] < mdI:
            mdI = machineI[i]
            iJI = i

    if mdG <= mdI:
        OS[count] = gJI + 1
        allocated[gJI] = True
        count += 1

    if mdI < mdG:
        OS[noj - slot - 1] = iJI + 1
        allocated[iJI] = True

print("\nOptimal Sequence:")
for i in range(noj):
    print(f"  {OS[i]}", end="")


#outTimes
outTimes = [[0] * noj for _ in range(nom)]

for i in range(noj):
    job = OS[i] - 1
    for j in range(nom):
        if i == 0:
            if j == 0:
                outTimes[j][i] = PT[job][j]
            else:
                outTimes[j][i] = outTimes[j - 1][i] + PT[job][j]
        else:
            if j == 0:
                outTimes[j][i] = outTimes[j][i - 1] + PT[job][j]
            else:
                outTimes[j][i] = max(outTimes[j - 1][i], outTimes[j][i - 1]) + PT[job][j]

print()
print()
print("Values in outTimes:")
for i in range(nom):
    for j in range(noj):
        print(outTimes[i][j], end=" ")
    print()

# inTimes
inTimes = [[outTimes[j][i] - PT[OS[i] - 1][j] for i in range(noj)] for j in range(nom)]

print("\nValues in inTimes array:")
for i in range(nom):
    for j in range(noj):
        print(inTimes[i][j], end=" ")
    print()


# minimum elapsed time
minElapsed = outTimes[nom - 1][noj - 1]
print("\nMinimum Elapsed Time:", minElapsed)

# idle time
idleTimes = [0] * nom
for i in range(nom):
    lastOutTime = outTimes[i][noj - 1]
    firstInTime = inTimes[i][0]
    idleTimes[i] = (minElapsed - lastOutTime) + firstInTime
    
    for j in range(1, noj):
        inTime = inTimes[i][j]
        outTime = outTimes[i][j - 1]
        idle = inTime - outTime
        
        if idle > 0:
            idleTimes[i] += idle

print("\nIdle Time for Each Machine:")
for i in range(nom):
    print(f"Machine {i + 1}: {idleTimes[i]}")



'''Enter the number of machines: 5
Enter the number of jobs: 4
Enter processing time for Job 1 on Machine 1: 14 
Enter processing time for Job 1 on Machine 2: 10
Enter processing time for Job 1 on Machine 3: 4
Enter processing time for Job 1 on Machine 4: 6
Enter processing time for Job 1 on Machine 5: 18
Enter processing time for Job 2 on Machine 1: 12
Enter processing time for Job 2 on Machine 2: 12
Enter processing time for Job 2 on Machine 3: 8
Enter processing time for Job 2 on Machine 4: 10
Enter processing time for Job 2 on Machine 5: 20
Enter processing time for Job 3 on Machine 1: 10
Enter processing time for Job 3 on Machine 2: 8
Enter processing time for Job 3 on Machine 3: 10
Enter processing time for Job 3 on Machine 4: 12
Enter processing time for Job 3 on Machine 5: 16
Enter processing time for Job 4 on Machine 1: 16
Enter processing time for Job 4 on Machine 2: 6
Enter processing time for Job 4 on Machine 3: 6
Enter processing time for Job 4 on Machine 4: 4
Enter processing time for Job 4 on Machine 5: 12

Minimum processing time on the first machine: 10
Minimum processing time on the last machine: 12

Maximum procesing time for each machine (except first and last):
Machine 2: 12
Machine 3: 10
Machine 4: 12
Maximum of the maximum processing times: 12

Data stored in Machine G:
Job 1: 34
Job 2: 42
Job 3: 40
Job 4: 32

Data stored in Machine I:
Job 1: 38
Job 2: 50
Job 3: 46
Job 4: 28


Optimal Sequence:
  1  3  2  4

Values in outTimes
14 24 36 52
24 32 48 58
28 42 56 64
34 54 66 70
52 70 90 102

Values in inTimes array:
0 14 24 36 
14 24 36 52
24 32 48 58
28 42 56 66
34 54 70 90

Minimum Elapsed Time: 102

Idle Time for Each Machine:
Machine 1: 50
Machine 2: 66
Machine 3: 74
Machine 4: 70
Machine 5: 36 '''