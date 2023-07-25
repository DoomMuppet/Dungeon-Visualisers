package content;

import java.util.Random;

public class Narrative {

	String dungeonGoal;
	String settlementGoal;
	String otherGoal;
	String adventureIntro;
	String adventureClimax;

	String eventBasedVillainAction;
	String eventBasedGoal;
	String framingEvent;
	String moralQuandry;
	String twist;
	String sideQuest;
	String characterObjective;
	
	String narInfo;
	private Random rng = new Random();
	
	public String getDungeonGoal() {
		return dungeonGoal;
	}


	public String getSettlementGoal() {
		return settlementGoal;
	}


	public String getOtherGoal() {
		return otherGoal;
	}


	public String getAdventureIntro() {
		return adventureIntro;
	}


	public String getAdventureClimax() {
		return adventureClimax;
	}


	public String getEventBasedVillainAction() {
		return eventBasedVillainAction;
	}


	public String getEventBasedGoal() {
		return eventBasedGoal;
	}


	public String getFramingEvent() {
		return framingEvent;
	}


	public String getMoralQuandry() {
		return moralQuandry;
	}


	public String getTwist() {
		return twist;
	}


	public String getSideQuest() {
		return sideQuest;
	}


	public String getCharacterObjective() {
		return characterObjective;
	}


	


	/**
	 * Constructor used for creating a location based narrative.
	 * 
	 * 
	 * @param dungeonGoal
	 *            - Dungeon goals to be achieved by the party and create a
	 *            narrative.
	 * @param otherGoal
	 *            - What side goals do characters have other than the main one?
	 * @param adventureIntro
	 *            - The introduction for how the party starts.
	 * @param adventureClimax
	 *            - How does the adventure end?
	 * 
	
	 * @param framingEvent
	 *            - What event/s are taking place when the story occurs?
	 * @param moralQuandry
	 *            - A moment of ethical or moral dilemma that can't be fixed by
	 *            magic or combat.
	 * @param twist
	 *            - a plot twist.
	 * @param sideQuest
	 *            - an optional side quest for the party.
	 * 
	 */

	public Narrative(String dungeonGoal, String settlementGoal, String otherGoal, String adventureIntro,
			String adventureClimax,	String framingEvent, String moralQuandry,
			String twist, String sideQuest) {

	
		this.dungeonGoal = dungeonGoal;
		this.otherGoal = otherGoal;
		this.adventureIntro = adventureIntro;
		this.adventureClimax = adventureClimax;
		
		this.framingEvent = framingEvent;
		this.moralQuandry = moralQuandry;
		this.twist = twist;
		this.sideQuest = sideQuest;
		
	}
	

	/**
	 * Constructor used for creating a location based narrative.
	 * 
	 * 
	 * @param settlementGoal
	 *            - Settlement goals to be achieved by the party and create a
	 *            narrative.
	 * @param otherGoal
	 *            - What side goals do characters have other than the main one?
	 * @param adventureIntro
	 *            - The introduction for how the party starts.
	 * @param adventureClimax
	 *            - How does the adventure end?
	 * 
	
	 * @param framingEvent
	 *            - What event/s are taking place when the story occurs?
	 * @param moralQuandry
	 *            - A moment of ethical or moral dilemma that can't be fixed by
	 *            magic or combat.
	 * @param twist
	 *            - a plot twist.
	 * @param sideQuest
	 *            - an optional side quest for the party.
	 * 
	 */

	public Narrative(String settlementGoal, String otherGoal, String adventureIntro,
			String adventureClimax,	String framingEvent, String moralQuandry,
			String twist, String sideQuest) {

	
		
		this.settlementGoal = settlementGoal;
		this.otherGoal = otherGoal;
		this.adventureIntro = adventureIntro;
		this.adventureClimax = adventureClimax;
		
		this.framingEvent = framingEvent;
		this.moralQuandry = moralQuandry;
		this.twist = twist;
		this.sideQuest = sideQuest;
		
	}
	
	
	/**
	 * Constructor used for creating an event based narrative.
	 * 
	 * 
	 @param eventBasedVillainAction
	 *            - Once you have a villain, it's time to determine what steps the
	 *            villain takes to achieve its goals. Create a time line showing
	 *            what the villain does and when, assuming no interference from the
	 *            adventurers.
	 * @param eventBasedGoal
	 *            - You can use the Event-Based Goals table to set the party's goal.
	 *            A goal can also suggest ways in which the adventurers become
	 *            caught up in the villain's plans, and what exactly they must do to
	 *            foil those plans.
	 * @param framingEvent
	 *            - What event/s are taking place when the story occurs?
	 * @param moralQuandry
	 *            - A moment of ethical or moral dilemma that can't be fixed by
	 *            magic or combat.
	 * @param twist
	 *            - a plot twist.
	 * @param sideQuest
	 *            - an optional side quest for the party.
	 * @param characterObjective
	 *            - An objective for one of the characters aside from main mission
	 *            or group objective.
	 * 
	 */

	public Narrative(String eventBasedVillainAction, String eventBasedGoal, String framingEvent, String moralQuandry,
			String twist, String sideQuest, String characterObjective) {

	
		
		this.eventBasedVillainAction = eventBasedVillainAction;
		this.eventBasedGoal = eventBasedGoal;
		this.framingEvent = framingEvent;
		this.moralQuandry = moralQuandry;
		this.twist = twist;
		this.sideQuest = sideQuest;
		this.characterObjective = characterObjective;
	}


	public String determineSetNar() {		
		NarrativeContent narContent = new NarrativeContent();
		String point1 = String.valueOf(rng.nextInt(20));
		String point2 = String.valueOf(rng.nextInt(20));
		if (point1.equals(point2) || point1.equals("0") || point2.equals("0")) {
			point1 = "Villain Location";
			point2 = "DM's Choice";
		}
		else {}
		
		narInfo = "\n <b> Narrative Information </b> \n <br /> Note: These are only ideas and may contradict each other.<br /><br /> \n \n";
		narInfo = (narInfo + "<b> Settlement Goal: </b>" + narContent.settlementGoals.get(rng.nextInt(narContent.settlementGoals.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Adventure Introduction: </b>" + narContent.adventureIntro.get(rng.nextInt(narContent.adventureIntro.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Adventure Climax: </b>" + narContent.adventureClimax.get(rng.nextInt(narContent.adventureClimax.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Character Objective: </b>" + narContent.characterObjectives.get(rng.nextInt(narContent.characterObjectives.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Framing Event: </b>" + narContent.framingEvents.get(rng.nextInt(narContent.framingEvents.size())).toString() + "<br /> \n");
		//narInfo = (narInfo + "<b> Moral Quandry: </b>" + narContent.moralQuandries.get(rng.nextInt(narContent.moralQuandries.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Side goals (perhaps for one character): </b>" + narContent.otherGoals.get(rng.nextInt(narContent.otherGoals.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Side Quest: </b>" + narContent.sideQuests.get(rng.nextInt(narContent.sideQuests.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Plot Twist: </b>" + narContent.twists.get(rng.nextInt(narContent.twists.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "\n <br /><b> Secret Passage: </b>" + narContent.secretPassages.get(rng.nextInt(narContent.secretPassages.size())).toString()+ " From room:  "+ point1 +" To room: " + point2 + "<br /> \n");
		
		//System.out.println(npcInfo);
		return narInfo;
	}
	
	public String determineDunNar() {		
		NarrativeContent narContent = new NarrativeContent();
		String point1 = String.valueOf(rng.nextInt(20));
		String point2 = String.valueOf(rng.nextInt(20));
		if (point1.equals(point2) || point1.equals("0") || point2.equals("0")) {
			point1 = "Villain Location";
			point2 = "DM's Choice";
		}
		else {}
		
		narInfo = "\n<b> Narrative Information</b> <br />\n Note: These are only ideas and may contradict each other. <br /><br />\n \n";
		narInfo = (narInfo + "<b> Dungeon Goal: </b>" + narContent.dungeonGoals.get(rng.nextInt(narContent.dungeonGoals.size())).toString() + " <br />\n");
		narInfo = (narInfo + "<b> Adventure Introduction: </b>" + narContent.adventureIntro.get(rng.nextInt(narContent.adventureIntro.size())).toString() + " <br />\n");
		narInfo = (narInfo + "<b> Framing Event: </b>" + narContent.framingEvents.get(rng.nextInt(narContent.framingEvents.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Adventure Climax: </b>" + narContent.adventureClimax.get(rng.nextInt(narContent.adventureClimax.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> A Character's side objective:</b> " + narContent.characterObjectives.get(rng.nextInt(narContent.characterObjectives.size())).toString() + "<br /> \n");
		//narInfo = (narInfo + "<b> Moral or ethical dilemma: </b>" + narContent.moralQuandries.get(rng.nextInt(narContent.moralQuandries.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Side Quest: </b>" + narContent.sideQuests.get(rng.nextInt(narContent.sideQuests.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Plot Twist: </b>" + narContent.twists.get(rng.nextInt(narContent.twists.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<br /><b> Secret Passage: </b>" + narContent.secretPassages.get(rng.nextInt(narContent.secretPassages.size())).toString()+ " From:  "+ point1 +" To: " + point2 + "<br /> \n");
		
		//System.out.println(npcInfo);
		return narInfo;
	}
	
	public String determineEventNar() {		
		NarrativeContent narContent = new NarrativeContent();
		String point1 = String.valueOf(rng.nextInt(20));
		String point2 = String.valueOf(rng.nextInt(20));
		if (point1.equals(point2) || point1.equals("0") || point2.equals("0")) {
			point1 = "Villain Location";
			point2 = "DM's Choice";
		}
		else {}
		
		narInfo = "\n <b> Narrative Information </b> \n <br /> Note: These are only ideas and may contradict each other.<br /><br /> \n \n";
		narInfo = (narInfo + "<b> Character Objective: </b>" + narContent.characterObjectives.get(rng.nextInt(narContent.characterObjectives.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Event Based Goal: </b>" + narContent.eventBasedGoals.get(rng.nextInt(narContent.eventBasedGoals.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Event based Villain Action: </b>" + narContent.eventBasedVillainActions.get(rng.nextInt(narContent.eventBasedVillainActions.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Framing Event: </b>" + narContent.framingEvents.get(rng.nextInt(narContent.framingEvents.size())).toString() + "<br /> \n");
		//narInfo = (narInfo + "<b> Moral Quandry: </b>" + narContent.moralQuandries.get(rng.nextInt(narContent.moralQuandries.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Side goals (perhaps for one character): </b>" + narContent.otherGoals.get(rng.nextInt(narContent.otherGoals.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Side Quest: </b>" + narContent.sideQuests.get(rng.nextInt(narContent.sideQuests.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<b> Plot Twist: </b>" + narContent.twists.get(rng.nextInt(narContent.twists.size())).toString() + "<br /> \n");
		narInfo = (narInfo + "<br /><b>Secret Passage: </b>" + narContent.secretPassages.get(rng.nextInt(narContent.secretPassages.size())).toString() + " From:  "+ point1 +" To: " + point2 + "<br />< \n");
		
		return narInfo;
	}
}

