package itu.jgdiejuu.torcs;

import java.util.Collections;
import java.util.List;

import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;

import com.anji.integration.ActivatorTranscriber;
import com.anji.util.Configurable;
import com.anji.util.Properties;

@SuppressWarnings("serial")
public class TorcsFitnessFunction implements BulkFitnessFunction, Configurable {

	//private static final int MAX_STEPS = 1500;
	
	private static final int MAX_FITNESS = 13337;
	private ActivatorTranscriber activatorFactory;
	
	public int getMaxFitnessValue() {
		return MAX_FITNESS;
	}

	@Override
	final public void evaluate( List<Chromosome> genotypes ) {
		
		BulkNEATController controller = new BulkNEATController(genotypes, activatorFactory);
		ClientProgram.main(new String[]{"-","maxEpisodes:1337"}, controller);
		List<Integer> fitnesses = controller.getFitnesses();
		
		// make sure none is negative (add the lowest negative values absolute to all)
		int minimum = Collections.min(fitnesses);
		if(minimum < 0){
			minimum = Math.abs(minimum);
			for(int i = 0; i < fitnesses.size(); i++){
				fitnesses.set(i, fitnesses.get(i) + minimum);
			}
		}
		
		// save fitnesses
		for(int i = 0; i < genotypes.size(); i++){
			genotypes.get(i).setFitnessValue(fitnesses.get(i));
		}
	}

	public void init( Properties props ){
		activatorFactory = (ActivatorTranscriber) props
				.singletonObjectProperty( ActivatorTranscriber.class );
	}
}
