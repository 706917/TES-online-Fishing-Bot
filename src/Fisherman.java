import java.awt.AWTException;
import java.io.IOException;

/** Java class to provide automated performance in TES Online game.
 * Automated actions - fishing
 * 
 * Current version workseer only with following conditions:
 * 1 - The monitor has resolution 1920 x 1080
 * 1 - The game runs on full screen mode
 * 2 - VOTAN FISHERMAN ADDON installed and activated in the game
 * to provide specified color changers of monitored pixels.
 * 
 * Future releases will handle these constrains
 */
 
public class Fisherman {
	public static void main(String[] args) throws IOException, AWTException {
		
		EsoFisherman rob = new EsoFisherman();
		rob.fish();
		
		
	}

}
