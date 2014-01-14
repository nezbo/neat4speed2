package itu.jgdiejuu.torcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgap.Chromosome;

import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;

public class BulkNEATController extends Controller {
	
	private static final int MAX_STEPS = 8000; // about 2½ minute
	private static int HIST_SIZE = 5; // number of historical fitnesses for each chromosome if enabled
	private static HashMap<Long,ArrayList<Integer>> chromoFitnesses;
	
	private boolean finished = false;
	
	private List<Chromosome> genotypes;
	private NEATController controller = null;
	private int curStep = 0;
	private int curGene = 0;

	private ActivatorTranscriber factory;
	private boolean manualGear;
	

	public BulkNEATController(List<Chromosome> genotypes, ActivatorTranscriber activatorFactory, boolean manualGear, boolean fitnessHistory){
		this.manualGear = manualGear;
		this.genotypes = genotypes;
		this.factory = activatorFactory;
		
		chromoFitnesses = new HashMap<Long,ArrayList<Integer>>();
		
		if(!fitnessHistory) HIST_SIZE = 1;
		
		createController();
	}

	@Override
	public Action control(SensorModel sensors) {
		if(curStep < MAX_STEPS && !disqualified()){
			
			curStep++;
			return controller.control(sensors);
		}else{
			Action result = new Action();
			
			// print reason
			if(disqualified()){
				System.out.println(">>\tController Stuck: d="+controller.getDeltaFive()+" step "+curStep);
			}else{
				System.out.println(">>\tController Out of Time: "+curStep+" = "+MAX_STEPS);
			}
			System.out.println(">>\t"+controller.toString());
			
			// save fitness
			saveFitness(controller.getMaxDistance());
			
			// change variables
			curGene++;
			curStep = 0;
			
			// go to next
			if(curGene < genotypes.size()){
				createController();
				result.restartRace = true;
			}else{
				//System.out.println("BULK out of genes");
				finished = true;
			}
			
			return result;
		}
	}
	
	private boolean disqualified() {
		return !(controller.getDeltaFive() >= 1.0 /*|| controller.getDeltaFive() < 0*/);
	}

	public boolean isFinished(){
		return finished;
	}
	
	private void saveFitness(int fitness){
		long id = genotypes.get(curGene).getId();
		ArrayList<Integer> arr = chromoFitnesses.get(id);
		
		if(arr == null) arr = new ArrayList<Integer>(HIST_SIZE);
		if(arr.size() == HIST_SIZE){ 
			// remove oldest
			arr.remove(0);
		}
		arr.add(fitness);
		
		System.out.println(">> Get fitness: "+fitness+" (avg: "+calcAvg(arr)+" of "+arr.size()+")");
	}
	
	public List<Integer> getFitnesses(){
		ArrayList<Integer> result = new ArrayList<Integer>(chromoFitnesses.size());
		for(Chromosome c : genotypes){
			long id = c.getId();
			ArrayList<Integer> arr = chromoFitnesses.get(id);
			
			result.add(calcAvg(arr));
		}
		return result;
	}
	
	private int calcAvg(ArrayList<Integer> arr){
		int result = 0;
		for(int i : arr) result += i;
		return result / arr.size();
	}
	
	private void createController() {
		try {
			System.out.println(">> Controller #"+(curGene+1) + " - id: "+genotypes.get(curGene).getId());
			controller = new NEATController(factory.newActivator(genotypes.get(curGene)),manualGear);
		} catch (TranscriberException e) { e.printStackTrace(); controller = null; }
		
	}

	@Override
	public void reset() {
		//System.out.println(">> Reset call received");
	}

	@Override
	public void shutdown() { }

}
