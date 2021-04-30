import java.util.List;

public class DefenderArcade {
  public int countArcades(List<String> times) {
	  int defMachine=0;
	  int max=0;
	  if(times.size()>=50) {
		  max=50;
	  }else {
		  max=times.size();
	  }
	  for(int i=1;i<max;i++) {
		  String timeLst=times.get(i).toString();
		  String timeCompare=times.get(i-1).toString();
		  String[] timeCompareDiv=timeCompare.split(" ");
		  String[] timeLstDiv=timeLst.split(" ");
		  int start=Integer.parseInt(timeLstDiv[0]);
		  int end=Integer.parseInt(timeLstDiv[1]);
		  int startBefore=Integer.parseInt(timeCompareDiv[0]);
		  int endBefore=Integer.parseInt(timeCompareDiv[1]);
		  if(start>=endBefore) {
			  defMachine=defMachine;
		  }else {
			  defMachine=defMachine+1;
		  }
	  }
    return defMachine;
  }
}
