execute store result score @s dmg run attribute @s minecraft:generic.attack_damage get 1
execute store result score @s critDmg run attribute @s additionalentityattributes:critical_bonus_damage get 100
scoreboard players add @s critDmg 100
execute store result score @s sharpness run data get entity @s SelectedItem.tag.Enchantments[{id:"minecraft:sharpness"}].lvl 50
scoreboard players add @s[scores={sharpness=1..}] sharpness 50
scoreboard players operation @s dmg *= @s critDmg
scoreboard players operation @s dmg += @s sharpness
resource operation @s assassin:damage/assassinate_resource = @s dmg