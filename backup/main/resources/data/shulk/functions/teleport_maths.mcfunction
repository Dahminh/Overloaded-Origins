execute store result score @s yPos run data get entity @s Pos[1]

function shulk:teleport_functions/-60_320

execute store result score @s newYPos run data get entity @s Pos[1]
execute store result score @s yPosCheck run scoreboard players get @s yPos
scoreboard players operation @s yPosCheck -= @s newYPos
execute unless entity @s[scores={yPosCheck=-10..10}] run function shulk:teleport_maths_fix