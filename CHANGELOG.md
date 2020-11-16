# Changelog

#0.2.1.2
 - fixed ct recipe expansions not working
 - decreased stack size of efficiency upgrades from 16 to 4
 - added endstone macerating recipe
 - added support for overclocker and other such modifying upgrades to multiblocks
 - fixed overridden items not showing up in jei
 - added ct methods for forge hammer

# 0.2.1.1
 - fixed crash

# 0.2.1
 - Moved tile texture into just a single large sprite sheet, should hopefully reduce ram usage a little
 - made recipes using ic2 pump also use bc pump if bc is loaded
 - renamed wrench interface, renamed method in scrwdriver interface. this means that gravisuit classic 1.0.8.3 will not load with this, you will need 1.0.8.4
 - made fluid caster have a new animated texture
 - changed wrench recipes to use plates and a hammer, they wil observe the plate and crafting tool config options
 - added iron bar recipe to assembling machine
 - fixed large turbines not outputting fluid
 - added stone dust to rare earth extractor
 - made fluid smelter active when powered with redstone
 - made my single use batteries able to be used in discharging slots
 - fixed my single use battery recipes give the full batteries
 - added some of my canning machine recipes to ic2c extras fluid canning machine
 - added burnable tooltips for diesel gen and gas turbine
 - added biomass distilling recipe
 - fixed bath dupe bug
 - changed required circuit mode for nichrome from 0 to 2
 - filtered digital tank input slot
 - added hopper assembling recipe
 - added new one probe info that tells you that wrenches are needed for machines
 - temp fix to allow wood into the coke oven
 - fixed auto output of hatches
 - fixed invar pipes not saving their material to world nbt on unload, causing them to become bronze fluid pipes
 - made electric screwdriver able to rotate blocks
 - fixed fusion reactor not saying the structure is valid when it is valid
 - fixed the fusion reactor continously increasing the max eu when a fusion energy injecor is removed and placed back down(hopefully)
 - hopefully fixed a rare crash I found when picking up a fusion energy injector
 - fixed second fusion material injector and fusion material extractor tank display slot being switched with each other, such that the output displays the second input and visa versa
 - fixed fusion reactor and large gas turbine not saving contents of tanks
 - added saw - currently just used to make vanilla amounts of planks and sticks, will add mod compat later
 - added steam machines
 - added gt2 config option
 - fixed fusion reactor jei info
 - fixed fusion reactor slot item setting
 - made energy output hatches able to output less then the specified output
 - fixed microwave explode issue
 - added crafting table recipe for drain cover
 - made alloy smelter have varying eu usage
 - added plate cable config
 - added crushed ore cauldron washing
 - fixed pump cover not pulling fluids from ic2c multitanks into quad pipes properly
 - fixed  opening guis of blocks when trying to place cover on pipe

# 0.2
 - Greatly reduced the ram requirements of gtcx, there still might be more improvements to do yet.
 - Improved server performance of a couple tiles
 - Added wood plate
 - Overrode centrifuge, you have to pick up existing ones and put it in the crafting grid to get the new one
 - Added certus quartz recipe to industrial grinder
 - Added new electric rail recipes for rc compat
 - Added bath
 - Added branch cutters - only available when forestry is loaded, basically version of the grafter but with a lot more durability
 - Added integrated circuit jei support
 - Added pure glass block
 - Added fluid pipes - may be unforseen bugs yet, but so far not many bugs. They are a bit like gt6 fluid pipes
 - Added covers
 - Added digital tank
 - Added screwdriver
 - Added new config for forcing pre electric machines, currently it just removes the sticky resin smelting recipe
 - Added coke oven, coal coke, and creosote
 - Added crushed ore config
 - Added small aluminium dust to dustSmallAluminum oredict
 - Added multiblock tooltips to fusion controller
 - Possibly fixed diesel gen generation issues
 - Reverted end game stuff like personal force field requiring osmium
 - Refactored hatch multiblocks, the haches now get the fluids and items from the multiblocks themselves, they are not just accessors and setters
 - Hatches now only get a casing texture if there are either 4 of the same casing adjacent or 3 of the same casing and one hatch adjacent - will make future casing support much easier
 - Made my wrenches no longer take massive amounts of damage when preventing loss
 - Changed regular reactor plating recipe to use lead plate instead of dense copper plate
 - Ruby grinder water recipe now makes 2 small chrome dust instead of 1
 - Diamond grinding recipe now makes 6 instead of 8 small diamond dusts as byproduct
 - Olivine now outputs pyrope instead of emerald as the secondary output, and emerald now does aluminium instead of olivine
 - Added diamond, olivine, and emerald acid recipes to grinder that give 3
 - Added nether quartz grinder recipe that outputs 4 nether quartz and 2 small netherrack dust
 - Nerfed all ore extracting recipes to 2 from 3
 - Fixed crop textures
 - Refactored a bunch of ct stuff
 - Changed tetrahedrite ore texture
 - Extruder no longer requires the mold in the mold slot
 - Updated chromite ore texture, it's now a bit lighter
 - Doubled default chromite weight - recommend you regenerate your config
 - Disabled precision wrench recipe if ic2c extras is loaded with the wrench override config true
 - Made my wrench always enabled even with the crafting tools disabled, it's just not used in recipes when said config is disabled
 - Removed macerator recipe that uses the grinder heads
 - Changed rotary macerator recipe to use stainless steel plates instead of grinder heads in the corners
 - Crushed ore processing is no longer dependent on ic2c extras
 - Fusion reactor now shows gain instead of total output, doesn't account for start eu though
 - Added a little more debug info for dynamo hatch
 - Fixed wrench duplication
 - Fixed wood recipes
 - Added railcraft electrode and coil recipes
 - Fixed sawmill lubricant issue
 - Removed a logger
 - Marble now macerates to marble dust
 - Fixed reactor chamber override with ic2c extras
 - Removed tiles from casings, means that the rotor textures will be broken for now till I figure out how to do it properly without using a tile
 - Nerfed glass fibre cables recipe
 - Removed silicon fluid
 - Converted all berylium fluid outputting recipes to outputting the item
 - Added assembling machine recipe for iron fence
 - Fixed electric sprayer recipe only taking full cf sprayer and made the recipe transfer foam from cf sprayer
 - Made iron scaffold recipe always use iron and made it use rods instead of fences
 - Made hardening spray work with turning foamed iron scaffold to reinforced stone
 - Made it possible to put hardening spray and matchbox in tool box
 - Made my wrenches have a faster mining speed
 - Thick reflector now takes beryllium plates instead of ingots
 - Changed casing to iron fence recipe in wiremill to always do iron
 - Made output hatch and dynamo hatch change modes with screwdriver
 - thermal boiler now has to have 2 output hatches
 - Fixed dynamo hatch mode info not being localized and running twice
 - Added iridium dust to ic2 iridium ore item compressing recipes
 - Made circuit crafting recipe take insulated cable - assembling recipe still uses uninsulated version
 - Reduced air electrolyzing power usage by a factor of ten
 - Made pbf able to output and input any items from the brick
 - Made ic2 crop scanner accept forestry bees, saplings, ect.
 - Boosted eu production from some fluids in the diesel gen, moved fuel and forestry ethanol from liquid fuel gen to diesel gen
 - Made digital tanks merge same fluid from a fluid orb into it's tank, even leaving any remaining fluid it can't accept in the orb
 - Added extra emerald ore gen to extreme hills
 - Made wrench able to harvest hoppers and furnaces
 - Removed unnecessary blockstate properties from pipes and cables, may improve ram usage a little more
 - Changed all air checks in multiblock checks to proper air checks instead of air state checks, should now work with other blocks that register as air
 - Lightened dark blue elements of the fusion reactor gui a little
 - Made dark blue text in fusion gui light blue instead
 - Added buttons to fusion reactor gui that show the layers of the fusion structure
 - Added new greeting panel when you first load the game, came about because of some drama between me and e the author of gtc


# 0.1.2
 - Added thermal boiler
 - Added input, output, machine control, and dynamo hatches
 - Adeed lava filter, turbine rotors, turbine blades, and broken turbine rotors 
 - Added large steam turbine 
 - Added large gas turbine 
 - Added plate cutting machine
 - Added fusion reactor
 - Added extruder
 - Added diamond sawblade
 - Added hardening spray
 - Added industrial sawmill - by default it overrides the ic2c sawmill, but that can be changed in the config if so desired
 - Added trash bin
 - Added stone compressor and stone extractor
 - Fuxed greg icon in alloy furnace gui not being a simple yellow
 - Added diamond to diamond dust recipe to macerator
 - Added nugget to ingot recipes 
 - Added tungstensteel reinforced stone
 - Added connected textures for casings
 - Made electrolyzer output fluids instead of fluid tubes, will be pr’d into the centrifuge soon.
 - Fixed itnt recipe in assembling machine 
 - Fixed possible lang crash
 - Fluid smelter and caster now auto output and input fluids in the right and left sides respectively 
 - Rebalanced damage done by gtcx tools
 - Made tungstensteel tools use steel rods and steel tools use iron rods
 - Assembling machine no longer accepts aluminium instead of steel in its crafting recipe
 - Magic energy converter now uses plates
 - Added tungstensteel dust to hot tungstensteel ingot recipe to ibf
 - Fixed magnalium recipe only making 1 instead of 3
 - Added 3 tetrahedrite 1 zinc to 3 brass and 3 tetrahedrite 1 tin to 3 bronze recipes to alloy smelter
 - Made cinnabar electrolyzing recipes take a bit longer 
 - Did same with redrock dust, endstone dyst, and tetrahedrite dust
 - Added various centrifuging and electrolyzing recipes for alloy dusts
 - added fertilizer recipe to chemical reactor 
 - Added config option to make my tools not have the enchantment tool glean, client side only
 - Made integrated circuit get it’s mode based off the metadata
 - Fixed iron bars recipe and put the wrench on the top instead of bottom for the recipe
 - Made hull recycling recipes only output 6 ingots instead of 8 cause of an exploit found
 - Fixed refined iron + ash to iron recipe
 - Fixed chromite not generating
 - Removed extra pesu recipe
 - Changed heat values for materials in the fluid smelter to match gt6
 - Added torch placement to ic2 drills
 - Fixed windmill recipes using aluminium instead of magnalium
 - Made machine block permanently a cheap machine block, steel hull is now used instead in basic machine block recipes
 - Made sheldonite processing output tiny iridium dust instead of iridium nuggets
 - Added ic2c extras iridium shard to iridium tiny dust recipe in macerator
 - Made electrum circuit board recipe not use silver
 - Multiblocks now only support certain types of ic2 upgrades: ImportExport, RedstoneControl, and Sounds
 - Changed recipes of some reactor heat vents and such, and cells
 - Added some tooltips for blocks
 - Added fluid support to chemical reactor
 - Fixed fluid smelter not working with upgrades
 - Added refined iron plate, rod, and gear to wrought iron oredict
 - Added aluminium and electrum cables
 - Added saphire acid i. grinder recipe
 - Added invar as a viable alternative to steel/refined iron in mixed metal ingot recipe
 - Fixed nichrome conflict with stainless steel
 - Added coil recycling recipes
 - Removed stainless steel hull from the advanced machine block oredict
 - Added wood nerf config, defaults to false
 - Changed reinforced casing recipe to use basic machine blocks instead of advanced
 - Fixed a rare crash
 - Made sodalite not usable in circuit recipes
 - Added alternative hv cable using recipes that use aluminium cables instead
 - Added thermal glowstone and ender clathrate recipe to vacuum freezer
 - Added proper marble and redrock support
 - Fixed colors of my color blocks not getting set on the server side
 - Added crowbar - only useful as railcraft crowbar right now, but will be used to remove covers from pipes once those are finished
 - Ic2c extras thermal centrifuge now uses steel plates instead of refined iron/steel ingots based off ic2c steel config
 - A couple other machines also now only use steel even when the steel mode is off
 - Added osmiridium material
 - Made cloaking device and forcefield require osmium, and made tesla staff require osmiridium
 - Added "OsmiumGT" oredict to all osmium items to prevent usage of mekanism osmium in gtcx recipes
 - Added 9 iridium dust to 1 osmium dust centrifuging recipe
 - Added ae2 compat - pure crystals can now be made in indusrial electrolyzer, and charged certus can now be made in ic2c electrolyzer
 - Moved thorium to regular furnace from ibf
 - Fixed sodium sulfide using carbon instead of sulfur
 - Iron fence now uses iron rods instead of refined iron rods, excep in the instance that crafting tools are disabled
 - Made wave generator and ocean generator more expensive - wave generator now require magnalium turbine rotors, ocean generator require osmium rotors
 - Thermometer now requires mercury
 - Nuclear rods now use regular copper plates instead of dense copper plates in dual and quad recipes
 - Fixed chemical reactor texture having slot textures where slots don't exist
 - Added match and match box
 - Added ability to make thermal fluids by smelting certain items in the fluid smelter
 - Fixed the no power icon overlaying over the slots in the chemical reactor, it now overlays at the bottom
 - Made distillation tower use the multitank that the machine tank uses, you won't lose your fluids, it'll just add any new fluids to the new tank. next release after this curse one those old tanks will be gone, so take out your fluids now.
 - Added fluid filters to all input tanks of machines that didn't have ones, fixed i sawmill only accepting water in the process
 - Made soul sand in centrifuge give saltpeter

# 0.1.1
- Added intergrated circuit
- Added more rc recipes
- Added railcraft and gravisuit compat config options
- Fixed distillation tower consuming fluids and processing even when it shouldn't
- Added fluid support to electolyzer
- Added recipes to plate ender and wiremill from ic2c extras roller and extruder
- Made steel jackhammer able to mine obsidian again
- Possibly fixed diamond chainsaw not activating fast leaf decay's fast leaf decaying
- Increased attack of diamond chainsaw from 10 to 15

# 0.1.0
- Fixed a few recipes - see commit history for more details of what was changed
- Added better mod support for other stones to steel jackhammer.
- Added cassiterite material
- Made Dustbin autocompress stacks
- Added some missing recipes

# 0.0.9
- Changed fluid caster gui recipe use info
- New fluid smelter and caster textures thanks to Globalista Maoísta, also changed chainsaw texture thanks to same person
- added recipes for super fuel binders
- added support for railcraft, not finished yet.
- made distillation tower only accept valid fluids
- fixed base heat getting reset on chunk, world, or server reload
- added recipe for saltpeter
- fixed unsmeltable ic2 dusts in fluid smelter
- made fluid smelter heat go up with redstone signal
- removed recipes for data stuff as e removed the data items
- new forcefield projector and tesseract recipes

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
