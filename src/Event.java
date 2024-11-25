/**
 * Represents a collision between a particle and another particle, or a particle and a wall.
 */
public class Event implements Comparable<Event> {
	double _timeOfEvent;
	double _timeEventCreated;
	String _p1;
	String _p2;

	/**
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 */
	public Event (double timeOfEvent, double timeEventCreated) {
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;
		_p1 = null;
		_p2=null;

	}

	/**
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 * @param p the particle that is colliding
	 */
	public Event (double timeOfEvent, double timeEventCreated, String p) {
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;
		_p1 = p;
		_p2=null;

	}

	/**
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 * @param p the first particle that is colliding
	 * @param p2 the second particle that is colliding
	 */
	public Event (double timeOfEvent, double timeEventCreated, String p, String p2) {
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;
		_p1 = p;
		_p2 = p2;
	}

	@Override
	/**
	 * Compares two Events based on their event times. Since you are implementing a maximum heap,
	 * this method assumes that the event with the smaller event time should receive higher priority.
	 */
	public int compareTo (Event e) {
		if (_timeOfEvent < e._timeOfEvent) {
			return +1;
		} else if (_timeOfEvent == e._timeOfEvent) {
			return 0;
		} else {
			return -1;
		}
	}
}
