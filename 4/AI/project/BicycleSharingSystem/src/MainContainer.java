import Classes.Variables;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

/**
 *
 */

public class MainContainer {

	Runtime rt;
	ContainerController container;

	public ContainerController initContainerInPlatform(String host, String port, String containerName) {
		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, containerName);
		profile.setParameter(Profile.MAIN_HOST, host);
		profile.setParameter(Profile.MAIN_PORT, port);
		// create a non-main agent container
		ContainerController container = rt.createAgentContainer(profile);
		return container;
	}

	public void initMainContainerInPlatform(String host, String port, String containerName) {

		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.CONTAINER_NAME, containerName);
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "true");
		prof.setParameter(Profile.GUI, "true");

		// create a main agent container
		this.container = rt.createMainContainer(prof);
		rt.setCloseVM(true);

	}

	public void startAgentInPlatform(String name, String classpath) {
		try {
			AgentController ac = container.createNewAgent(name, classpath, new Object[0]);
			ac.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MainContainer a = new MainContainer();

		try {

			a.initMainContainerInPlatform("localhost", "9888", "MainContainer");

			a.startAgentInPlatform("Interface", "Agents.Interface");

			Thread.sleep(2000);

			int stations = Variables.NUM_STATIONS; // Limit number of stations
			int clients = Variables.NUM_USERS; // Limit number of clients

			// Start agents Stations!
			for (int n = 0; n < stations; n++) {
				a.startAgentInPlatform("Station" + n, "Agents.Station");
			}

			// Provide some time for Agents to register in services
			Thread.sleep(1000);

			// Start agents Customers!

			for (int n = 0; n < clients; n++) {
				
				// Sleep 2 second for each x customer agents added
				if (n % Variables.NUM_USERS_PER_SPAWN == 0) {
					Thread.sleep(2000);
				}
				a.startAgentInPlatform("Client" + n, "Agents.Client");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}