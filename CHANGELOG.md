# Changelog

# 0.0.8
- Added tooltips for lockers and advanced worktable
- Added dustbin
- Increased required temperature of nichrome and kanthal
- Added hardened clay processing to i. grinder
- Changed ruby processing
- Changed rotary macerator recipe
- Added chromite ore, spawns at same rate as sheldonite.
- Fixed blast furnace heat getting reset when any slot gets changed

# 0.0.7
- Added fluid caster and molds
- Added fluid smelter
- Note: the fluid caster and fluid smelter currently have the alloy smelter texture, I'll fix that eventually.
- Added plate bender
- Added wiremill
- Added lathe
- Added microwave oven
- Fixed alloy smelter needing too much eu/tick
- Replaced the g in guis with a flat yellow g instead of a gradient yellow g
- Made aluminium and silicon require blast furnace to smelt, however the heat value is the same as steel.
- Added config to remove all furnace recipes for charcoal, defaults to false.
- Changed iron fence recipe
- Changed iron bar and hopper recipes
- Made oilberries a food, though I would not recommend eating them. the results would be... unpleasant.
- Fixed some of my new tiles not displaying their max eu input.
- Made my electric items have a cyan durability bar.
- Added config for recipes using crafting tools.
- Added config for harder circuit recipes.
- Removed the harder progression config in favor of more modular individual configs.
- Added iridium reinforced tungstensteel block
- Reduced redstone amount from 10 to 8 in the macerator
- Blacklisted iridium alloy ingot oredict in auto plate recipes
- Removed duplicate obsidian to silicon recipe in centrifuge
- Made small dust to dust recipe shapeless
- Added ingotHot oredicts to the hot ingots
- Fixed some ic2 dusts to gtc small dust recipes
- Added better uranium and sideproducts out of uranium ore using mercury and sodium persulfate- may change bit
- Fixed position of invalid multiblock overlay of industrial grinder
- Added oreTungsten oredict to tuungstate ore
- Added my own bedrock ores to gtc's new bedrock ore list
- Fixed jei border on the ibf cutting off at the bottom.
- Fixed data storage circuit recipe not using proper amount of emeralds or olivine
- Added diesel generator
- Added gas turbine
- Added coil slot to industrial blast furnace
- Changed plutonium byproduct from grinding uranium ore to thorium
- Added biofuel and coalfuel fluids so they can be burned in the diesel gen. the cells empty into the fluid when placed into  the diesel gen.
- Added pump module
- Added 2 more tanks to the distillation tower and added lube as an output of oil and fuel as an output of naptha
- Added nickel ore processing to the industrial grinder
- Added thermal hardened glass recipes to the alloy smelter
- Fixed steel jack hammer being able to mine obsidian and other diamond level blocks
- Lowered steel jackhammer speed to 7x as fast as the diamond one is at 3x3
- Fixed ruby ore dropping red garnets when silk touched
- Added ct support for all machines added in this update except for the microwave.
- Changed recipe of vacuum freezer controller and advanced machine casingsLowered default gen of galena- needs a config reset to take effect.
- Added advanced worktable- advanced version of the basic worktable, can charge items in the worktable's new tool slots. advanced worktable can accept tier 2 power.
- Added regular and charging lockers- right click to swap your armor. charging can charge up to tier 3 items and can accept tier 3 power. The locker's gui can be opened if in creative and not right clicking the front.
- Quartered itnt amounts needed for compressing gem dusts in the implosion compressor
- Moved coal chunk to industrial diamond recipe to the implosion compressor
- Added explosion sound to compressor; occurs every 2 seconds while processing.
- Fixed some centrifuge recipes being unintentionally removed.
- Added radiation to plutonium and uranium- ads to the ic2c extras list instead of gtcx's system when ic2c extras is loaded.
- Added ct support for the radiation system, does nothing if ic2c extras is loaded.

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
