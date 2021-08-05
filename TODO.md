#### Assorted Ideas (to be never completed)

Central controller to register ingame actions and execute based on priority (Eating, Attacking, etc.)

Inventory management (steal shit from open inventories thats needed, drop trash, keep some build blocks, xcarry?)

Make FSM.State.ACTIVE redirect to a configurable "mode" state the bot uses to generate behavior at runtime.

Implement modes:

- SPAWNFAG -> collect blocks, go to nether, chill at nhub, (do some funny shit with players nearby, public control via commands, clean blocks from nhub area)
- BASE_GUARD -> kill mobs, discord notifications for unknown players
- UTIL/FOLLOW -> follow player, carry shit around, do baritone area selection things (baritone command macros?)
- SCHEMATIC -> get shit from storages, load and print schematics on certain coords

Discord webhook notifications (on death, etc...)

Configurable admin and user whitelist/groups

Friend system

Command permissions (public private commands)

Modules:

- autoeat
- autoarmor
- velocity
- aura
- caura?
- jesus
- noslow
- step
- safewalk
- visualrange (say hi to players)
- blink (against newfags with crystals)

Queue for outgoing chat (drop on spam)

Commands:

- follow (follow player)
- clear (clear area)
- take (go to player, pick up dropped items and check containers in range)
- guard (patrol and kill mobs around anchor position)
- chatlog (upload latest chatlog as paste)
- nbt (dump current inventory nbt data as paste)
- names (get old names of player)
- search (get infos from ddg/google/etc.)
- yt (search videos)

Implement http api for dashboard and remote control
