import java.awt.*;
import java.io.*;
import java.util.*;
import javax.naming.NameNotFoundException;
import javax.swing.*;

public class ParticleSimulator extends JPanel {
	private Heap<Event> _events;
	private java.util.List<Particle> _particles;
	private double _duration;
	private int _width;

	/**
	 * @param filename the name of the file to parse containing the particles
	 */
	public ParticleSimulator (String filename) throws IOException {
		_events = new HeapImpl<>();

		// Parse the specified file and load all the particles.
		Scanner s = new Scanner(new File(filename));
		_width = s.nextInt();
		_duration = s.nextDouble();
		s.nextLine();
		_particles = new ArrayList<>();
		while (s.hasNext()) {
			String line = s.nextLine();
			Particle particle = Particle.build(line);
			_particles.add(particle);
		}

		setPreferredSize(new Dimension(_width, _width));
	}

	@Override
	/**
	 * Draws all the particles on the screen at their current locations
	 * DO NOT MODIFY THIS METHOD
	 */
        public void paintComponent (Graphics g) {
		g.clearRect(0, 0, _width, _width);
		for (Particle p : _particles) {
			p.draw(g);
		}
	}

	// Helper class to signify the final event of the simulation.
	private class TerminationEvent extends Event {
		TerminationEvent (double timeOfEvent) {
			super(timeOfEvent, 0);
		}
	}

	/**
	 * Helper method to update the positions of all the particles based on their current velocities.
	 */
	private void updateAllParticles (double delta) {
		for (Particle p : _particles) {
			p.update(delta, _width);
		}
	}

	private boolean eventValid(double time){
		return true;
	}
	
	private int getParticleIndex(String name) throws NameNotFoundException{
		if (name==null)
		throw new NameNotFoundException();
			for (int i = 0; i < _particles.size();i++){
				if (name.equals(_particles.get(i).getName()))
					return i;
			}
		throw new NameNotFoundException();
	}

	private void addEvents(Particle p, double time){
		double collision;
			for (Particle p2 : _particles) {
				if (p!= p2){
					collision = p.getCollisionTime(p2);
					System.out.println("collide"+collision + " "+time+"time");
					if (collision != Double.POSITIVE_INFINITY) {
						_events.add(new Event(collision+time, time, p.getName(), p2.getName()));
						
					}
				}
			}
		System.out.println("wallX "+p.wallXCollisionTime(time, _width)+"  "+time+"time");
		
		//_events.add(new Event(p.wallCollisionTime(time, _width), time, p.getName()));
		double collideX = p.wallXCollisionTime(time, _width);
		double collideY = p.wallYCollisionTime(time, _width);
		if (collideX == collideY)
			_events.add(new Event(collideX, time, p.getName()));
		else if (collideX != Double.POSITIVE_INFINITY)
			_events.add(new Event(collideX, time, p.getName()));
		else if (collideY != Double.POSITIVE_INFINITY)
			_events.add(new Event(collideY, time, p.getName()));
	}

	/**
	 * Executes the actual simulation.
	 */
	private void simulate (boolean show) {
		double lastTime = 0;

		// Create initial events, i.e., all the possible
		// collisions between all the particles and each other,
		// and all the particles and the walls.

		for (Particle p : _particles){
			addEvents(p, lastTime);
		}
		_events.add(new TerminationEvent(_duration));
		while (_events.size() > 0) {
			Event event = _events.removeFirst();
			double delta = event._timeOfEvent - lastTime;
			Particle par1;
			Particle par2;
			double i = event._timeOfEvent;

			if (event instanceof TerminationEvent) { // when the simulation ends updates all particles
				updateAllParticles(delta);
				break;
			}

			try { // try to find the first particle. returns an error if particle name does not exist
				par1 = _particles.get(getParticleIndex(event._p1));
			} catch (NameNotFoundException e){ // catches when the event doesn't contain any particle. ie it is termination event. 
				par1=null;
			}
			try { // try to find the second particle. returns an error if particle name does not exist
				par2 = _particles.get(getParticleIndex(event._p2));
			} catch (NameNotFoundException e){ // catches if the event is a wall collision and the second particle doesn't exist
				par2=null;
			}

			// Check if event still valid; if not, then skip this event
			
			if (par1 != null && par1.getLastUpdate() != event._timeEventCreated) {
				continue;
			}

			// Since the event is valid, then pause the simulation for the right
			// amount of time, and then update the screen.
			if (show) {
				try {
					Thread.sleep((long) delta);
				} catch (RuntimeException ie) {
					System.out.println("ahhh delta = "+delta);
					System.out.println("ahhhh time = "+lastTime);
					System.out.println("ahhh eventTime = "+event._timeOfEvent);
					

				} catch (InterruptedException ie) {}
			}


			// Update positions of all particles
			updateAllParticles(delta);

			// Update the velocity of the particle(s) involved in the collision
			// (either for a particle-wall collision or a particle-particle collision).
			// You should call the Particle.updateAfterCollision method at some point.
			try {
				if (par2 == null){ // updates the  particle in a wall collision
					par1.updateWallCollision(lastTime, _width);
				}
				else { // if there is a second paricle we know that it was a collision between two particles
					par1.updateAfterCollision(lastTime, par2); // updates after a collision with two particles
				}
				// Enqueue new events for the particle(s) that were involved in this event.
				addEvents(par1, lastTime);
				
			} catch (NullPointerException e){} // catches the situation where there are no particles in the event


			// Update the time of our simulation
			//System.out.println(lastTime+" --> "+event._timeOfEvent);
			//System.out.println(delta);
			
			lastTime = event._timeOfEvent;
			
			// Redraw the screen
			if (show) {
				repaint();
				
			}
		}

		// Print out the final state of the simulation
		System.out.println(_width);
		System.out.println(_duration);
		for (Particle p : _particles) {
			System.out.println(p);
		}
	}

	public static void main (String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: java ParticalSimulator <filename>");
			System.exit(1);
		}
		
		ParticleSimulator simulator;

		simulator = new ParticleSimulator(args[0]);
		JFrame frame = new JFrame();
		frame.setTitle("Particle Simulator");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(simulator, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulator.simulate(true);
	}
}
