package itu.jgdiejuu.torcs;

import java.util.ArrayList;
import java.util.List;

import org.jgap.Chromosome;

import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;

public class BulkNEATController extends Controller {
	
	private static final int MAX_STEPS = 4000;
	
	private boolean finished = false;
	private ArrayList<Integer> fitnesses;
	
	private List<Chromosome> genotypes;
	private NEATController controller = null;
	private int curStep = 0;
	private int curGene = 0;

	private ActivatorTranscriber factory;

	public BulkNEATController(List<Chromosome> genotypes, ActivatorTranscriber activatorFactory){
		this.genotypes = genotypes;
		this.factory = activatorFactory;
		fitnesses = new ArrayList<Integer>(genotypes.size());
		
		createController();
	}

	@Override
	public Action control(SensorModel sensors) {
		if(curStep < MAX_STEPS && !disqualified()){
			if(curStep == 0)
				System.out.println(">> Controller's first action");
			
			curStep++;
			return controller.control(sensors);
		}else{
			// save fitness
			fitnesses.add(controller.getFitness());
			
			// change variables
			curGene++;
			curStep = 0;
			
			// go to next
			if(curGene < genotypes.size()){
				createController();
			}else{
				finished = true;
			}
			
			Action result = new Action();
			result.restartRace = true;
			
			//System.out.println(">> Reset sent");
			
			return result;
		}
	}
	
	private boolean disqualified() {
		return !(controller.getDeltaFive() > 1.0 || controller.getDeltaFive() < 0);
	}

	public boolean isFinished(){
		return finished;
	}
	
	public List<Integer> getFitnesses(){
		return fitnesses;
	}
	
	private void createController() {
		try {
			controller = new NEATController(factory.newActivator(genotypes.get(curGene)));
		} catch (TranscriberException e) { e.printStackTrace(); controller = null; }
		
	}

	@Override
	public void reset() {
		//System.out.println(">> Reset call received");
	}

	@Override
	public void shutdown() { }

}
