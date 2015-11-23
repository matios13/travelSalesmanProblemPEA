package travelSalesman;

public class TimeInterval {
	long startTime=0;
	long endTime=0;
	public void startTiming(){
		startTime=System.currentTimeMillis();
	}
	public void endTiming(){
		endTime=System.currentTimeMillis();
	}
	
	public long getElapsedTime(){
		return (endTime-startTime);
	}
}
