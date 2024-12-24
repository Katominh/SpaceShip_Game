package space_game;
import java.util.*;

public class Map {
    private char[][][] map;
    private final int X, Y, Z;
    private int hullPt = 5, shieldPt = 3;
    private int[] coords = {2, 2, 2};
    private final int[] vancouver = new int[3];
    private boolean scanReadiness, viewTop = true;
    private Random rand = new Random();

    
    // constructor
    public Map(int x, int y, int z) {
        X = x;
        Y = y;
        Z = z;
        map = new char[x][y][z];
        
        // fill in all slots
        for (int i = 0, n = map.length; i < n; i++){
            for (char[] each : map[i])
                Arrays.fill(each, '?');
        }
        
        // select destination on map
        map[x - 3][y - 3][z - 3] = '✪';
        // current location
        map[2][2][2] = '*';

        // Vancouver ship coordinate
        vancouver[0] = x - 3;
        vancouver[1] = y - 3;
        vancouver[2] = z - 3;
    }
    
    
    // display hull & shield points with bars
    public void showStatus() {
    	System.out.print("Ship Hull Points: ");
    	for (int i = 0; i < hullPt; i++)
    		System.out.print("▮");
    	for (int i = 5 - hullPt; i > 0; i--)
    		System.out.print("▯");
    	
    	System.out.print("\nShip Shield Points: ");
    	for (int j = 0; j < shieldPt; j++)
    		System.out.print("▮");
    	for (int j = 3 - shieldPt; j > 0; j--)
    		System.out.print("▯");
    	
    	System.out.println();
    }
    
    
    // display keys for directions
    public void showInstruction() {
    	System.out.println("\ni for positive x direction \tk for negative x direction\n"
				+ "l for positive y direction \tj for negative y direction\n"
				+ "u for positive z direction \to for negative z direction\n");
    }
    
    
    // show current ship view
    public void view() {
    	if (viewTop)
    		displayTop();
    	else
    		displaySide();
    }
    
    
    // view setter 
    public void setView(boolean b) {
    	viewTop = b;
    }
    
    
    // swap ship view
    public void changeView() {
    	if (viewTop)
    		viewTop = false;
    	else
    		viewTop = true;
    }
    
    
    // scan setter
    public void setScan(boolean b) {
    	scanReadiness = b;
    }
    
    
    // scan getter
    public boolean scanReady() {
    	return scanReadiness;
    }
    
    
    // ship scan methods
    public void scan(String direction) {
    	if (direction.equals("i")) {
    		setView(true); // auto change to appropriate view
    		scanXPos();
    	} else if (direction.equals("k")) {
    		setView(true);
    		scanXNeg();
    	} else if (direction.equals("l")) {
    		setView(true);
    		scanYPos();
    	} else if (direction.equals("j")) {
    		setView(true);
    		scanYNeg();
    	} else if (direction.equals("u")) {
    		setView(false);
    		scanZPos();
    	} else { // effectively 'o'
    		setView(false);
    		scanZNeg();
    	}
    }

    
    // coordinate getter
    public int[] getCoords() {
        return coords;
    }
    
    
    // display coordinate
    public void showCoords() {
        System.out.printf(">> Your Current Coordinate: (%d, %d, %d)\n", coords[0], coords[1], coords[2]);
    }
    
    
    // Vancouver coordinate getter
    public int[] getDestination() {
        return vancouver;
    }
    
    
    // display Vancouver coordinate
    public void showDestination() {
        System.out.printf(">> Destination: (%d, %d, %d)\n\n", vancouver[0], vancouver[1], vancouver[2]);
    }

    
    // hull getter
    public int getHull() {
    	return hullPt;
    }
    
    
    // shield getter
	public int getShield() {
		return shieldPt;
	}


	// shield setter
    public void setShield(int s) {
    	shieldPt = s;
    }
    
    
    // move methods
    public boolean move(String direct) {
    	// prevent out-of-bound movements
    	if (coords[0] == 0 && direct.equals("k") ||
			coords[0] == X - 1 && direct.equals("i") ||
			coords[1] == 0 && direct.equals("j") ||
			coords[1] == Y - 1 && direct.equals("l") ||
			coords[2] == 0 && direct.equals("o") ||
			coords[2] == Z - 1 && direct.equals("u")) {
    		System.out.println("** Help signal is not in this direction. Try again.\n");
    		return false; // to signal ship haven't moved
    	}
    	
    	// move the ship
    	if (direct.equals("i")) 
    		coords[0] += 1;
    	else if (direct.equals("k")) 
    		coords[0] -= 1;
    	else if (direct.equals("l")) 
    		coords[1] += 1;
    	else if (direct.equals("j")) 
    		coords[1] -= 1;
    	else if (direct.equals("u")) 
    		coords[2] += 1;
    	else // effectively 'o'
    		coords[2] -= 1;
    	return true; // to signal ship have moved
    }
    
    
    // check for damage
    public void potentialDamage() {
    	char object;
    	int damage;
    	
    	// if ship move blindly without scanning
    	if (map[coords[0]][coords[1]][coords[2]] == '?') {
    		object = randomRoll();
    		map[coords[0]][coords[1]][coords[2]] = object;
    	}
    	else
    		object = map[coords[0]][coords[1]][coords[2]];
    	
    	switch (object) {
			case 'N': // nova - deals 0 to 2 damage
				damage = rand.nextInt(3);
				if (damage == 0)
		    		System.out.println("◈ Phew! The nova is dormant for now: 0 damage taken");
				else if (damage == 1)
		    		System.out.println("◈◈ Minor damage! The nova's somewhat powerful wave have hit the ship: 1 damage taken");
				else
		    		System.out.println("◈◈◈ Critical damage! The nova is very powerful at this moment: 2 damage taken");
				break;
				
			case 'D': // debris - deals 1 damage
				System.out.println("◈◈ Ouch! The debris have hit the ship: 1 damage taken");
				damage = 1;
				break;
			
			default: // '*' empty space OR '✪' Vancouver ship - deals 0 damage
				System.out.println("◈ Everything is clear in your region");
				return;
    	}
    	
    	// reduce shield 1st and hull 2nd
    	if (shieldPt != 0)
    		shieldPt -= damage;
    	else
    		hullPt -= damage;
    }

    
    // display a 7x7 grid of top view to user (x and y dimension)
	private void displayTop() {
		// header
	    char symbol = '↑';
	    System.out.println("\n\t      TOP VIEW");
	    
	    // x is row; y is column
	    for (int x = coords[0] + 3; x >= coords[0] - 3; x--){
	    	
	    	// display the x arrow (vertical)
	    	if (x == coords[0] + 3)
	    		System.out.print(" x  ");
	    	else {
	    		System.out.print(" |  ");
	    		symbol = '|';
	    	}
	    		
	    	// if x is outside the map --> show 'Sector Interference'
	    	if (x < 0 || x >= X) 
				System.out.print(" ø Sectør ø Interførence ø ");
	    	
	    	else {
		        for (int y = coords[1] - 3; y <= coords[1] + 3; y++){
		        	// if y is outside the map --> show 'ø'
		        	if (y < 0 || y >= Y)
		            	System.out.print(" ø ");

		        	else if (x == coords[0] && y == coords[1])
		                System.out.print(" Δ "); // icon for current ship position
		        	
		            else // display map contents
		                System.out.printf(" %s ", map[x][y][coords[2]]);
		                
	                if (y != coords[1] + 3) // separating columns
	                    System.out.print("|"); 
		        }
	    	}
	    	
	        if (x != coords[0] - 3)
	            System.out.println("\n " + symbol + "  ---------------------------"); // separate rows
	        else
	            System.out.println("\n |\n ⋅---------------------------> y"); // display the y arrow (horizontal)
	    }
	}


    // display a 7x3 grid of side view to user (x and z dimension)
	private void displaySide() {
		// header
	    char symbol = '↑';
	    System.out.println("\n     SIDE VIEW");
	    
	    // z is row; x is column
	    for (int z = coords[2] + 3; z >= coords[2] - 3; z--) {
	    	
	    	// display the z arrow (vertical)
	    	if (z == coords[2] + 3)
	    		System.out.print(" z  ");
	    	else {
	    		System.out.print(" |  ");
	    		symbol = '|';
	    	}
	    	
	    	// if z is outside the map --> show ' ø | ø | ø '
	    	if (z < 0 || z >= Z)
	    		System.out.print(" ø | ø | ø ");
	    	
	    	else if (z == coords[2])
                System.out.print("==== ‣ ===="); // icon for current ship position
	    	
    		else {
		        for (int x = coords[0] - 1; x <= coords[0] + 1; x++){
			    	// if x is outside the map --> show 'ø'
		        	if (x < 0 || x >= X)
		        		System.out.print(" ø ");
		        	
		        	else { // display map contents
		                System.out.printf(" %s ", map[x][coords[1]][z]);
		                
	                if (x != coords[0] + 1) // separate columns
	                    System.out.print("|");
		            }
		        }
    		}
	        
	        if (z == coords[2] - 3)
	            System.out.println("\n |\n ⋅-----------> x"); // display the x arrow (horizontal)
	        else
	            System.out.println("\n " + symbol + "  -----------"); // separating rows
	    }
	}

	
	// scan a 3x3 in positive x direction
	private void scanXPos() {
	    for (int x = coords[0] + 3; x >= coords[0] + 1; x--){
	    	// prevent x out of bounds
	    	if (x >= 0 && x < X) {
				for (int y = coords[1] - 1; y <= coords[1] + 1; y++) {
					// prevent y out of bounds AND change only '?' to nova, debris, or empty space
					if ((y >= 0 && y < Y) && map[x][y][coords[2]] == '?')
						map[x][y][coords[2]] = randomRoll();
				}
	    	}	    
    	}
	}
	
	
	// scan negative x direction (same concept, difference numbers)
	private void scanXNeg() {
	    for (int x = coords[0] - 3; x <= coords[0] - 1; x++){
	    	if (x >= 0 && x < X) {
				for (int y = coords[1] - 1; y <= coords[1] + 1; y++) {
					if ((y >= 0 && y < Y) && map[x][y][coords[2]] == '?')
						map[x][y][coords[2]] = randomRoll();
				}
	    	}	    
    	}
	}
	
	// scan positive y direction (same concept, difference numbers)
	private void scanYPos() {
	    for (int x = coords[0] + 1; x >= coords[0] - 1; x--){
	    	if (x >= 0 && x < X) {
				for (int y = coords[1] + 1; y <= coords[1] + 3; y++) {
					if ((y >= 0 && y < Y) && map[x][y][coords[2]] == '?')
						map[x][y][coords[2]] = randomRoll();
				}
	    	}	    
    	}
	}


	// scan negative y direction (same concept, difference numbers)
	private void scanYNeg() {
	    for (int x = coords[0] + 1; x >= coords[0] - 1; x--){
	    	if (x >= 0 && x < X) {
				for (int y = coords[1] - 3; y <= coords[1] - 1; y++) {
					if ((y >= 0 && y < Y) && map[x][y][coords[2]] == '?')
						map[x][y][coords[2]] = randomRoll();
				}
	    	}	    
		}
	}

	// scan positive z direction (same concept, difference numbers)
	private void scanZPos() {
	    for (int z = coords[2] + 3; z >= coords[2] + 1; z--){
	    	if (z >= 0 && z < Z) {
				for (int x = coords[0] - 1; x <= coords[0] + 1; x++) {
					if ((x >= 0 && x < Y) && map[x][coords[1]][z] == '?')
						map[x][coords[1]][z] = randomRoll();
				}
	    	}	    
    	}
	}
	
	// scan negative z direction (same concept, difference numbers)
	private void scanZNeg() {
	    for (int z = coords[2] - 1; z >= coords[2] - 3; z--){
	    	if (z >= 0 && z < Z) {
				for (int x = coords[0] - 1; x <= coords[0] + 1; x++) {
					if ((x >= 0 && x < Y) && map[x][coords[1]][z] == '?')
						map[x][coords[1]][z] = randomRoll();
				}
	    	}	    
    	}
	}
	
	// choose random space object
	private char randomRoll() {
		int roll = rand.nextInt(100) + 1;
		
		if (roll <= 5) // 5% nova
			return 'N';
		else if (roll <= 25) // 20% debris
			return 'D';
		else // 75% empty space
			return '*';
	}
}