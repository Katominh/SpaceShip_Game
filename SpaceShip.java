package space_game;
import java.util.*;

public class SpaceShip {	
    public static void main(String[] agrs) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        int x, y, z, choice, count = 50;
        String[] choices = {"1", "2", "3", "4", "5"}, directions = {"i", "j", "k", "l", "u", "o"};
        String userDirection, choiceStr;
        boolean onSameTurn = false, moveDone = false;
        
        // game introductions
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("† KAM1130. This is mission control speaking to the HMCSS Kamloops.");
		System.out.println("† We have detected a distress signal from a spaceship near you.");
		System.out.println("† It seems like the HMCSS Vancouver has ran out of fuel and are stuck in deep space.");
		System.out.println("† You are the closest ship to this location.");
		System.out.println("† So, we have assigned you a new mission: To reach and rescue them (in 50 turns).");
		System.out.println("† May your trip be free of dangers, Captain. Mission control over and out.");
		
		// player instructions (with randomized tips)
		System.out.println("\n✦✦ This is a space ship game, where you can scan and move your ship in 3 dimension: x, y, and z");
		switch (rand.nextInt(5)) {
			case 0:
				System.out.println("✦ Game tip - don't charge up your shield while in debris or nova.");
				break;
				
			case 1:
				System.out.println("✦ Game tip - Your scan is available every turn and do not cost a turn.");
				break;
				
			case 2:
				System.out.println("✦ Game tip - Your ship can change view to move in 3 dimensions without costing a turn.");
				break;
				
			case 3:
				System.out.println("✦ Game tip - Your ship hull points is unreplenishable. Travel with care.");
				break;
				
			case 4:
				System.out.println("✦ Game tip - 'Sectør ø Interførence' is the limit of your border. Don't travel past this point.");
				break;
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		// setting map size (with input validations)
		System.out.println("      Set Map Size (Choose Difficulty)");
        do {
            System.out.print("- Set the size of x dimension (between 6 - 53): ");
            x = sc.nextInt();
        } while (x < 6 || x > 53);  // to prevent impossible maps 
        							// (the distance between the 2 ships must be at most 50 units)

        do {
            System.out.printf("- Set the size of y dimension (between 6 - %d): ", 59 - x);
            y = sc.nextInt();
        } while (y < 6 || y > (59 - x)); // same here

        do {
            System.out.printf("- Set the size of z dimension (between 6 - %d): ", 65 - x - y);
            z = sc.nextInt();
        } while (z < 6 || z > (65 - x - y)); // same here
        
		sc.nextLine();
        Map ship = new Map(x, y, z);
        
        do {
        	// in case the player perform a 0-turn action (change view, scan, ...)
        	if (!onSameTurn) {
        		ship.setScan(true); // replenish scan     		
        		ship.view();
        		ship.showCoords();
        		ship.showDestination();
        		ship.potentialDamage();
        		
        		// game over if ship has 0 hull point
        		if (ship.getHull() == 0) {
        			System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        			System.out.println("† GAME OVER! Your ship is now broken and cannot move!");
        			System.out.println("† Bad ending. Please restart to try again.");
        			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        			sc.close();
        			return; // effectively stopping the program
        		}
        		ship.showStatus();
            	System.out.printf("\n>>> Available actions - %d turns left (enter 'd' for more details)\n", count);
        	}
        	
        	// get player action (with input validation)
        	do {
    			System.out.print("> Enter your next action: ");
    			choiceStr = sc.next().toLowerCase();
    			if (choiceStr.equals("d")) { // show instruction
    				System.out.printf("\n- 1 to change view mode\n"
                			+ "- 2 to charge up 1 shield (cost 1 turn)\n"
                			+ "- 3 to scan - Scan Availability: %b\n"
                			+ "- 4 to move (cost 1 turn)\n"
                			+ "- 5 to pass a turn (cost 1 turn)\n\n", ship.scanReady());
    			}
        	} while (!Arrays.asList(choices).contains(choiceStr));
        	choice = Integer.parseInt(choiceStr); // this way program will not crash if player provide non-digit inputs
        	
        	// perform action
        	switch (choice) {
        	
        		// change view between vertical & horizontal
	        	case 1:
	        		ship.changeView();
	        		ship.view();
	        		System.out.println("\n>> View mode changed successfully.");
	        		onSameTurn = true;
	        		continue; 
	        	
	        	// recharge shield by 1 if not full shield
	        	case 2:
	        		if (ship.getShield() != 3) {
	        			ship.setShield(ship.getShield() + 1);
	        			System.out.println("** Charging up 1 shield...\n");
	        		} else {
	        			System.out.println("** Your ship is already charged to 3 shields.\n");
	        			onSameTurn = true;
	        			continue;
	        		} break;
	        		
	        	// scan a 3x3 in 1 of 6 directions (if scan available)
	        	case 3:
	        		sc.nextLine();
	        		if (ship.scanReady()) {
		        		System.out.println("\nChoose a direction for a 3x3 scan (Enter 'h' for instructions)");
		        		
		        		// get player input (with validation)
		        		do {
		        			System.out.print("> Your choice: ");
		        			userDirection = sc.nextLine().toLowerCase();
		        			if (userDirection.equals("h"))  // instructions
		        				ship.showInstruction();
		        		} while (!Arrays.asList(directions).contains(userDirection));
		        		
		        		// perform a scan
		        		ship.scan(userDirection);
		        		ship.setScan(false); // deplete scan for current turn
		        		ship.view();
		        		
		        		// continue same turn
		            	System.out.printf("\n>>> Available actions - %d turns left (enter 'd' for more details)\n", count);
		        		onSameTurn = true;
		        		continue;
	        		} else {
	        			System.out.println("** Already scanned. Currently recharging...\n");
	        			continue;
	        		}
	        		
	        	// move in 1 of 6 directions
	        	case 4:
	        		sc.nextLine();
	        		System.out.println("\nChoose a direction to move in (Enter 'h' for instructions)");
	        		
	        		do {
		        		// get player input (with validation)
		        		do {
		        			System.out.print("> Your choice: ");
		        			userDirection = sc.nextLine().toLowerCase();
		        			if (userDirection.equals("h"))  // instructions
		        				ship.showInstruction();
		        		} while (!Arrays.asList(directions).contains(userDirection));
		        		
		        		// prevent moving outside of border
		        		moveDone = ship.move(userDirection);
	        		} while (!moveDone);
	        		
	        		// win if the ship get to destination
	        		if (Arrays.equals(ship.getCoords(), ship.getDestination())) {
	        			System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	        			System.out.println("† CONGRATULATION! Your ship have found the HMCSS Vancouver thanks to your great leadership!");
	        			System.out.println("† All members have been rescued and are grateful to you! Good ending! :)");
	        			System.out.printf("† You have reach the HMCSS Vancouver in %d moves!\n", 50 - count);
	        			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	        			sc.close();
	        			return; // effectively stopping the program
	        		}
	        		break;
	        		
	        	// pass a turn (if player select 5)
	        	default:
	        		break;
        	}
        	
        	// decrement counter and signal new turn
        	count--;
        	onSameTurn = false;
        	
        	// separate lines for new turn (visuals)
        	if (count != 0)
        		System.out.println("\n≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈\n"
        						 + "≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈≈\n");
        } while (count != 0);
        
        // lose if run out of turns
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("† GAME OVER! Your ship ran out of fuel and cannot reach the HMCSS Vancouver!");
		System.out.println("† Bad ending. Please restart to try again.");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        sc.close();
    }
}