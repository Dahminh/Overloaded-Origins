execute as @e[tag=preSB] run data modify entity @s Target set from entity @e[sort=random,limit=1,distance=..5] UUID
tag @e[tag=preSB] remove preSB
effect give @e[distance=..5] glowing