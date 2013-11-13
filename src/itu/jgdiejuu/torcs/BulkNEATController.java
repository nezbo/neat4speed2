package itu.jgdiejuu.torcs;

import java.util.List;

import org.jgap.Chromosome;

import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;

public class BulkNEATController extends Controller {
	
	private static final int MAX_STEPS = 1500;
	
	private boolean finished = false;
	private List<Chromosome> genotypes;
	private NEATController controller = null;
	private int curStep = 0;
	private int curGene = 0;

	private ActivatorTranscriber factory;

	public BulkNEATController(List<Chromosome> genotypes, ActivatorTranscriber activatorFactory){
		this.genotypes = genotypes;
		this.factory = activatorFactory;
		createController();
	}

	@Override
	public Action control(SensorModel sensors) {
		if(curStep < MAX_STEPS){
			curStep++;
			return controller.control(sensors);
		}else{
			// save fitness
			genotypes.get(curGene).setFitnessValue(controller.getFitness());
			
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
			return result;
		}
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	
	private void createController() {
		try {
			controller = new NEATController(factory.newActivator(genotypes.get(curGene)));
		} catch (TranscriberException e) { e.printStackTrace(); controller = null; }
		
	}

	@Override
	public void reset() {
		System.out.println("Reset call received");
	}

	@Override
	public void shutdown() { }

}
