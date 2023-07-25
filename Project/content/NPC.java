package content;

public class NPC {

	String patronage;
	String allyType;
	String appearance;
	String ability;
	String talent;
	String knows;
	String interactionTrait;
	String mannerism;
	String ideal;
	String flawOrSecret;
	String bond;

	String villainType;
	String villainScheme;
	String villainMethod;
	String villainWeakness;

	public String getPatronage() {
		return patronage;
	}

	public String getAllyType() {
		return allyType;
	}

	public String getAppearance() {
		return appearance;
	}

	public String getAbility() {
		return ability;
	}

	public String getTalent() {
		return talent;
	}

	public String getKnows() {
		return knows;
	}

	public String getInteractionTrait() {
		return interactionTrait;
	}

	public String getMannerism() {
		return mannerism;
	}

	public String getIdeal() {
		return ideal;
	}

	public String getFlawOrSecret() {
		return flawOrSecret;
	}

	public String getBond() {
		return bond;
	}

	public String getVillainType() {
		return villainType;
	}

	public String getVillainScheme() {
		return villainScheme;
	}

	public String getVillainMethod() {
		return villainMethod;
	}

	public String getVillainWeakness() {
		return villainWeakness;
	}

	/**
	 * Constructor used for creating a new villain. Use this one if it has a second
	 * attack.
	 * 
	 * 
	 * @param ability
	 *            - What abilities does the NPC have?
	 * @param appearance
	 *            - What do they look like?
	 * @param talent
	 *            - What talents does the NPC have?
	 * @param knows
	 *            - What knowledge does the NPC have?
	 * @param interactionTrait
	 *            - How does the NPC behave when interacted with?
	 * @param mannerism
	 *            - What mannerism does the NPC have?
	 * @param ideal
	 *            - What ideals does the NPC aspire to?
	 * @param flawOrSecret
	 *            - what flaws or secrets does the NPC have?
	 * @param bond
	 *            - What bonds does the NPC have?
	 * @param villainType
	 *            - What type of villain is the NPC?
	 * @param villainScheme
	 *            - What type of scheme is the Villain plotting?
	 * @param villainMethod
	 *            - What type of methods does the Villain use to do their evil
	 *            bidding?
	 * @param villainWeakness
	 *            - The main weakness of the villain.
	 */

	public NPC(String patronage, String allyType, String ability, String appearance, String talent, String knows,
			String interactionTrait, String mannerism, String ideal, String flawOrSecret, String bond,
			String villainType, String villainScheme, String villainMethod, String villainWeakness) {

		this.patronage = null;
		this.allyType = null;

		this.ability = ability;
		this.appearance = appearance;
		this.talent = talent;
		this.knows = knows;
		this.interactionTrait = interactionTrait;
		this.mannerism = mannerism;
		this.ideal = ideal;
		this.flawOrSecret = flawOrSecret;
		this.bond = bond;
		this.villainType = villainType;
		this.villainScheme = villainScheme;
		this.villainMethod = villainMethod;
		this.villainWeakness = villainWeakness;
	}

	/**
	 * Constructor used for creating a new villain. Use this one if it has a second
	 * attack.
	 * 
	 * @param patronage
	 *            - What type of Patron is the NPC? Usually sends adventurers on
	 *            quest.
	 * @param allyType
	 *            - What type of ally is the NPC? Usually joins adventurers on
	 *            quest.
	 * @param appearance
	 *            - What do they look like?
	 * @param ability
	 *            - What abilities does the NPC have?
	 * @param talent
	 *            - What talents does the NPC have?
	 * @param knows
	 *            - What knowledge does the NPC have?
	 * @param interactionTrait
	 *            - How does the NPC behave when interacted with?
	 * @param mannerism
	 *            - What mannerism does the NPC have?
	 * @param ideal
	 *            - What ideals does the NPC aspire to?
	 * @param flawOrSecret
	 *            - what flaws or secrets does the NPC have?
	 * @param bond
	 *            - What bonds does the NPC have?
	 */

	public NPC(String patronage, String allyType, String ability, String appearance, String talent, String knows,
			String interactionTrait, String mannerism, String ideal, String flawOrSecret, String bond) {

		this.patronage = patronage;
		this.allyType = allyType;
		this.ability = ability;
		this.appearance = appearance;
		this.talent = talent;
		this.knows = knows;
		this.interactionTrait = interactionTrait;
		this.mannerism = mannerism;
		this.ideal = ideal;
		this.flawOrSecret = flawOrSecret;
		this.bond = bond;

		this.villainMethod = null;
		this.villainScheme = null;
		this.villainType = null;
		this.villainWeakness = null;
	}

}
