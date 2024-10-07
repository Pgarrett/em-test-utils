def classify(a, b, c):
    if a <= 0 or b <= 0 or c <= 0:
        return 0
    
    if a == b and b == c:
        return 3
    

    mmax = max(a, max(b, c))

    if (mmax == a and mmax - b - c >= 0) or (mmax == b and mmax - a - c >= 0) or (mmax == c and mmax - a - b >= 0):
        return 0
    

    if a == b or b == c or a == c:
        return 2
    else:
        return 1


print(f"t2: {classify(997, -33808568, 764)}") # t2: 0
print(f"t3: {classify(602, 861, -33551660)}") # t3: 0
print(f"t4: {classify(491, 93, 339)}") # t4: 0
print(f"t5: {classify(443, 685, 133)}") # t5: 0
print(f"t7: {classify(133, 443, 612)}") # t7: 0
print(f"t8: {classify(735, 464, 457)}") # t8: 1
print(f"t9: {classify(800, 49, 805)}") # t9: 1