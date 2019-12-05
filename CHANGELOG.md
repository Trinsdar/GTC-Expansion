# Changelog

# 0.0.6
 - made pbf display particles when active
 - fixed eu display amount in pbf recipes in jei
 - removed dust alloy recipes and moved nichrome alloy recipe to blast furnace
 - tweaked material textures
 - added galena recipe to pbf
 - added dark ash as a second output of steel recipes in pbf
 - changed a couple material colors
 - removed generator recipe that uses an iron furnace
 - changed end sheldonite texture
 - changed alloy furnace to brick instead of stone based
 - made it possible to get carbon out of regular ash
 - fixed centrifuge not being able to accept aluminum in place of steel
 - made my wrenches 100% lossless since I override ic2c bronze one anyways. haven't decided whether I want to override the electric one yet.
 - made industrial grinder multiblock controller go on the side center like in gt4 instead of the bottom center
 - tweaked tile guis and added 2 more upgrade slots to he assembling machine and alloy smelter
 - made mobs unable to spawn on fire bricks
 - removed a couple loggers I accidently left in
 - added steel jack hammer and diamond chainsaw.
 - added gravisuit classic compat such that the advanced chainsaw and drill recipes are changed

# 0.0.5.1

 - fixed tinker's crash
 - fixed some recipe not overriding ic2 or gtclassic recipes when ic2 classic's steel config is enabled

# 0.0.5

 - fixed primitive blast furnace and allow furnace not dropping with pickaxe
 - refactored material registeration. **MEANS ALL YOUR MATERIALS THAT ARE NOT INGOTS OR DUSTS OR BLOCKS OR GEMS WILL BE DELETED. SO TURN EVERYTHING BACK INTO ONE OF THOSE 4.**
 - made recipes using aluminium plates use both aluminiums
 - added assembling machine recipes for furnace and crafting table
 - fixed circuit recipes not overriding ic2 ones when steel mode is enabled.
# 0.0.4

 - Fixed overlapping slots in a couple machines.
 - Added missing energy buffers to implosion compressor, industrial grinder, and vacuum freezer. may fix compatibility with mekanism energy pipes.
 - Fixed molten fluid textures sometimes not working.
 - Added ct support for all current machines. haven't made any docs yet though.
 - Fixed structures of vacuum freezer and distillation tower.
 - Fixed lang tooltips of both blast furnaces.
 - Made industrial grinder now use straight fluids instead of fluid containers.
 - Added click fluid support for industrial grinder and distillation tower.
 - Refactored config system in the background, may need to regenerate the config.
 - Made flint tools no longer have an enchantment glow.
 - Fixed axe attack speeds and damages 
 
# 0.0.3 

 - Ported over blast furnace from gtclasic, but with the addition of heat requirements for recipes. different casings give different amounts of heat, and using lava in the center instead of air will increase the heat 250 per lava source block, so up to 500.
 - Added Wrenches and removed gem crafting tools
 - Added assembling machine
 - Added chemical reactor
 - Added distillation tower
 - ported over ic2c extras roller ingot blacklist additions from ic2c extras to this.
 - Added gt2 mode config, not implemented for machine recipes yet.
 - Added gear recipes
 - Changed some gtc and ic2c recipes.
 - Added some fluids, such as diesel and gasoline
 - Added oilberry crop
 - Added acid, mercury, lithium(large only), and sodium standard and large batteries
 - Added a few of my materials to the element list.
 - Added config to remove crafting uu recipes in order to force the uu assembler.
 - Removed green sapphire and made the industrial grinder recipe for sapphire ore output small aluminium in it's place.
 
# 0.0.2.2 (Not released on curse)

 - Added back nickel, olivine, and zinc crops
 - Removed the iridiumAlloyIngot oredict from iridium alloy ingots so mods don't try to add recipes for ingot to plate in their respective plate making machines.
 - Changed circuit recipes to use uninsulated copper cables, red alloy plates on the side, and refined iron or other plates in the center.
 - Added ic2c extras compat for dense plate recipes.
 - Made tiles display their max input.
 - Fixed tool dupes with hammer and file.
 - Made hot ingots burn you if you don't have fire resistence effect, full hazmat suit, or full quantum suit.

# 0.0.2.1 (Not released on curse)

 - Removed crops for now till gtclassic 1.0.4 is released.
 - Added olivine, galena, tetrahedrite, and cassiterite to twilight hollow hill worldgen.
 - Fixed some gtclassic and ic2 recipes that I override, though the effect may not be seen till gtclassic 1.0.4 comes out

# 0.0.2 (Not released on curse)

 - Added Cassiterite, Tetrahedrite, and Galena ores.
 - Added crushed ores, purified crushed ore, and tiny dusts that are only available when ic2c extras is loaded.
 - Added nickel, olivine, and zinc crops
 - Modified the total damage values of flint, ruby, and sapphire tools to be 2x, 4x, and 4x respectively.
 - Made platinum smeltable in the furnace and removed it's blast furnace recipe.
