import math
def generateSearch(min,max):
    mid = math.floor((min + max) / 2)
    mid10 = mid * 10
    min10 = min * 10
    max10 = max * 10
    output = ""
    filename = str(min10)+"_"+str(max10)+".mcfunction"
    print(filename)
    if min != mid:
        output = "execute if score @s yPos matches "+str(min10 - 9)+".."+str(mid10)+" run function shulk:teleport_functions/"+str(min10)+"_"+str(mid10)
        generateSearch(min,mid)
    else:
        output = "execute if score @s yPos matches "+str(min10 - 9)+".."+str(min10)+" run spreadplayers ~ ~ 20 40 under "+str(min10)+" false @s"

    if mid+1 != max:
        output += "\nexecute if score @s yPos matches "+str(mid10 + 1)+".."+str(max10)+" run function shulk:teleport_functions/"+str(mid10+10)+"_"+str(max10)
        generateSearch(mid+1,max)
    else:
        output += "\nexecute if score @s yPos matches "+str(max10 - 9)+".."+str(max10)+" run spreadplayers ~ ~ 20 40 under "+str(max10)+" false @s"

    print(output + "\n")
    f = open(filename, "w")
    f.write(output)
    f.close()


generateSearch(-6,32)