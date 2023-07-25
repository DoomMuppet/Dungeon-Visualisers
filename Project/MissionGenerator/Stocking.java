package MissionGenerator;


//this is the class that stocks the dungeon, it is based on the previous stocking class but has the following changes:
/*1. It no longer determines what type a room is at random, in all cases except encounter
 *2. Certain room types have been removed 
 *3. Lock and key support has been added 
 *4. At the end of each room description it writes which rooms this room is connected too
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import content.Bestiary;
import content.Monster;
import content.NpcContent;


public class Stocking {
	
	private Random rng = new Random();
	private ArrayList<Monster> selectedTheme;
	private String combatRating;
	private int roomCount = 1;
	private String npcInfo = "";
	private String villInfo = "";
	int npcCount = 0;
	int ranEnc = 0;
	String hOrI = "i";
	
	
	public void stock(Map<String,Room> rooms, double lvlCR) {
		File file = new File("RoomDescription.txt");
		themeSelection();
		int rnd;
		try {
			PrintWriter write = new PrintWriter(file);
			write.println();
			for(Room room: rooms.values()) {
				
				write.println("Room: "+ room.getName());
				switch(room.getType()) {
				case "start":
					write.println("This is the Entrance to the Dungeon");
					break;
				case "goal":
					write.println("This is the Player's goal within the dungeon");
					VillainPlacement(write);
					break;
				case "lock":
					lockPlacement(write,room);
					break;
				case "key":
					keyPlacement(write,room);
					break;
				case "item":
					treasurePlacement(write, lvlCR);
					break;
				case "encounter": //this randomly selects a type of encounter
					rnd = rng.nextInt(10);
					if(rnd <= 6) {
						monsterPlacement(write, lvlCR);
					} else if(rnd ==7){
						trapPlacement(write);
					} else if(rnd == 8) {
						specialPlacement(write);
					}  else if(rnd>= 9){
						NpcPlacement(write);
						npcCount +=1;
					}
					else {
						monsterPlacement(write,lvlCR);
					}
				}
				write.println("This room connects to: " + room.getConnections().toString());
			}
			write.close();
			
		}
		catch (IOException e) {
			
		}
		
	}
	
	private void themeSelection() {
		Bestiary bestiary = new Bestiary();
		bestiary.addMonsterThemes();

		int themeSelect = rng.nextInt(5);

		switch (themeSelect) {
		case 0:
			selectedTheme = new ArrayList<Monster>(bestiary.aquatic);
			break;
		case 1:
			selectedTheme = new ArrayList<Monster>(bestiary.humanoid);
			break;
		case 2:
			selectedTheme = new ArrayList<Monster>(bestiary.forestry);
			break;
		case 3:
			selectedTheme = new ArrayList<Monster>(bestiary.dragon);
			break;
		case 4:
			selectedTheme = new ArrayList<Monster>(bestiary.undead);
			break;
		case 5:
			selectedTheme = new ArrayList<Monster>(bestiary.random);
			break;
		}
	}
	
	//used to place the keys required throughout the dungeon
	private void keyPlacement(PrintWriter write, Room r) {
		write.println("This room contains a key!");
		write.println("The key is for room:" +r.key);
		write.println();
	}
	private void lockPlacement(PrintWriter write, Room r) {
		write.println("This is locked!");
		write.println("The key for this room:" +r.lock);
		write.println();
	}
	//The villain is always placed at the goal of the dungeon
	private void VillainPlacement(PrintWriter write) {
		NpcContent npcContent = new NpcContent();
		villInfo = "VILLAIN INFORMATION \n";
		villInfo = (villInfo + " Villain Type: " + npcContent.villainage.get(rng.nextInt(npcContent.villainage.size()))
				+ " \n");
		villInfo = (villInfo + " Villain Scheme (the overall plan): "
				+ npcContent.villainSchemes.get(rng.nextInt(npcContent.villainSchemes.size())) + " \n");
		villInfo = (villInfo + " Villain Method (the methods do they typically use to achieve the plan): "
				+ npcContent.villainMethods.get(rng.nextInt(npcContent.villainMethods.size())) + " \n");
		villInfo = (villInfo + " Villain Weakness: "
				+ npcContent.villainWeaknesses.get(rng.nextInt(npcContent.villainWeaknesses.size())) + " \n \n");
		villInfo = (villInfo + " OPTIONAL VILLAIN INFO \n");
		villInfo = (villInfo + " Ability: " + npcContent.abilities.get(rng.nextInt(npcContent.abilities.size()))
				+ " \n");
		villInfo = (villInfo + " Appearance: " + npcContent.appearances.get(rng.nextInt(npcContent.appearances.size()))
				+ " \n");
		villInfo = (villInfo + " Interaction Trait: "
				+ npcContent.interactionTraits.get(rng.nextInt(npcContent.interactionTraits.size())) + " \n");
		villInfo = (villInfo + " Ideals: " + npcContent.ideals.get(rng.nextInt(npcContent.ideals.size())) + " \n");
		villInfo = (villInfo + " Bonds: " + npcContent.bonds.get(rng.nextInt(npcContent.bonds.size())) + " \n");
		villInfo = (villInfo + " Knows: " + npcContent.knowledge.get(rng.nextInt(npcContent.knowledge.size())) + " \n");
		villInfo = (villInfo + " Mannerism: " + npcContent.mannerisms.get(rng.nextInt(npcContent.mannerisms.size()))
				+ " \n");
		villInfo = (villInfo + " Flaw or Secret: "
				+ npcContent.flawsAndSecrets.get(rng.nextInt(npcContent.flawsAndSecrets.size())) + " \n");
		villInfo = (villInfo + " Talent: " + npcContent.talents.get(rng.nextInt(npcContent.talents.size())) + " \n");

		write.print(villInfo);

	}
	//The following methods are used for "Encounter" rooms
	private void NpcPlacement(PrintWriter write) {
		NpcContent npcContent = new NpcContent();

		npcInfo = "\n NPC INFORMATION \n Note: Choose either Ally Type or Patronage if they are counterintuitive/contradictory. \n \n";
		npcInfo = (npcInfo + " Patronage: " + npcContent.patrons.get(rng.nextInt(npcContent.patrons.size())).toString()
				+ " \n");
		npcInfo = (npcInfo + " Ally type: "
				+ npcContent.adventureAllies.get(rng.nextInt(npcContent.adventureAllies.size())).toString() + " \n");
		npcInfo = (npcInfo + " Ability: "
				+ npcContent.abilities.get(rng.nextInt(npcContent.abilities.size())).toString() + " \n");
		npcInfo = (npcInfo + " Appearance: "
				+ npcContent.appearances.get(rng.nextInt(npcContent.appearances.size())).toString() + " \n");
		npcInfo = (npcInfo + " Interaction Trait: "
				+ npcContent.interactionTraits.get(rng.nextInt(npcContent.interactionTraits.size())).toString()
				+ " \n");
		npcInfo = (npcInfo + " Ideals: " + npcContent.ideals.get(rng.nextInt(npcContent.ideals.size())).toString()
				+ " \n");
		npcInfo = (npcInfo + " Bonds: " + npcContent.bonds.get(rng.nextInt(npcContent.bonds.size())).toString()
				+ " \n");
		npcInfo = (npcInfo + " Knows: " + npcContent.knowledge.get(rng.nextInt(npcContent.knowledge.size())).toString()
				+ " \n");
		npcInfo = (npcInfo + " Mannerism: "
				+ npcContent.mannerisms.get(rng.nextInt(npcContent.mannerisms.size())).toString() + " \n");
		npcInfo = (npcInfo + " Flaw or Secret: "
				+ npcContent.flawsAndSecrets.get(rng.nextInt(npcContent.flawsAndSecrets.size())).toString() + " \n");
		npcInfo = (npcInfo + " Talent: " + npcContent.talents.get(rng.nextInt(npcContent.talents.size())).toString()
				+ " \n");
		// System.out.println(npcInfo);
		write.print(npcInfo);
	}


	private void monsterPlacement(PrintWriter write, double whatCR) {

		// Generates random number between 0 and the total size of the list of monsters
		// in the theme list.
		// 1/ combat rating is for a party rating level of 1, needs adapting so that
		// some more challenging monsters
		// can be added into the dungeon at certain points. Perhaps offer the level of
		// the overall monsters to be added
		// and used as a variable value here.

		int selectMonster = rng.nextInt(selectedTheme.size());

		Monster chosenMonster = selectedTheme.get(selectMonster);

		int numberOfMonsters = (int) (whatCR / chosenMonster.getCombatRating());

		convertCR(chosenMonster.getCombatRating());


		write.println("MONSTER: " + numberOfMonsters + "x " + chosenMonster.getName() + "; ");
		write.print("STATS: " + "CR: " + combatRating + "; ");
		write.print("HP: " + chosenMonster.getHealth() + "; ");
		write.print("AC: " + chosenMonster.getArmour() + "; ");
		write.println();
		write.print("COMBAT: " + "ATTACK BONUS: " + chosenMonster.getAttackBonus() + "; ");
		write.print(chosenMonster.getFirstAttackName() + ": " + chosenMonster.getFirstAttackDamage() + "; ");
		if (chosenMonster.getSecondAttackName() != null) {
			write.print(chosenMonster.getSecondAttackName() + ": " + chosenMonster.getSecondAttackDamage() + "; ");
		}
		write.println();
		write.print("More information can be found about the monster on page " + chosenMonster.getPageNum()
				+ " in the Monster Manual (5th Edition)");
		write.println();
	}

	private void convertCR(double decimal) {
		int denominator = (int) (1 / decimal);

		if (denominator == 1) {
			combatRating = "1";
		} else {
			combatRating = "1/" + denominator;
		}
	}

	private void trapPlacement(PrintWriter write) {
		write.println("This room is trapped!");
		write.print(
				"More information can be found about the possible traps on pages 120-123 in the Dungeon Masters Guide (5th Edition)");
		write.println();
	}

	private void specialPlacement(PrintWriter write) {
		write.println("There is something special about this room.");
		write.print("The room could have a puzzle, riddle, interesting features or an unusual effect");
		write.println();
	}


	private void treasurePlacement(PrintWriter write, double lvlCR) {
		write.println();
		write.print("<b>Suggested Loot: This room contains treasure!</b>");
		write.print(
				"This should be adapted for your campaign and game session to avoid too much or too little treasure/items. \n Treasure tables can be found in chapter 7 (p.137) of the Dungeon Master's Guide (5th Edition)");
		write.println();
	}
}
