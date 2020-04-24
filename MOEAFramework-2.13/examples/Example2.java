/* Copyright 2009-2019 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.IOException;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.PRNG;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.DTLZ.DTLZ3;
import org.moeaframework.problem.DTLZ.DTLZ4;


/**
 * Demonstrates how only a few lines of code are necessary to setup, run
 * and statistically compare multiple algorithms.
 */
public class Example2 {
	
	
//my DTLZ3 class with 3 objectives
	public static class MyDTLZ3 extends DTLZ3{
		

		public MyDTLZ3() {
			super (3);
			
		}
		
		@Override
		public void evaluate(Solution solution) {
			double[] x = EncodingUtils.getReal(solution);
			double[] f = new double[numberOfObjectives];

			int k = numberOfVariables - numberOfObjectives + 1;

			double g = 0.0;
			for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
				g += Math.pow(x[i] - 0.5, 2.0)
						- Math.cos(20.0 * Math.PI * (x[i] - 0.5));
			}
			g = 100.0 * (k + g);

			for (int i = 0; i < numberOfObjectives; i++) {
				f[i] = 1.0 + g;

				for (int j = 0; j < numberOfObjectives - i - 1; j++) {
					f[i] *= Math.cos(0.5 * Math.PI * x[j]);
				}

				if (i != 0) {
					f[i] *= Math.sin(0.5 * Math.PI * x[numberOfObjectives - i - 1]);
				}
			}

			solution.setObjectives(f);
		}

		@Override
		public Solution generate() {
			Solution solution = newSolution();

			for (int i = 0; i < numberOfObjectives - 1; i++) {
				((RealVariable)solution.getVariable(i)).setValue(PRNG.nextDouble());
			}

			for (int i = numberOfObjectives - 1; i < numberOfVariables; i++) {
				((RealVariable)solution.getVariable(i)).setValue(0.5);
			}

			evaluate(solution);

			return solution;
		}

		

	
		
	}
	
//my DTLZ4 class with 3 objectives
public static class MyDTLZ4 extends DTLZ4{
		  
	
	private static final double alpha = 100.0;

		public MyDTLZ4() {
			super (3);
			
		}
		
		public void evaluate(Solution solution) {
			double[] x = EncodingUtils.getReal(solution);
			double[] f = new double[numberOfObjectives];

			int k = numberOfVariables - numberOfObjectives + 1;

			double g = 0.0;
			for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
				g += Math.pow(x[i] - 0.5, 2.0);
			}

			for (int i = 0; i < numberOfObjectives; i++) {
				f[i] = 1.0 + g;

				for (int j = 0; j < numberOfObjectives - i - 1; j++) {
					f[i] *= Math.cos(0.5 * Math.PI * Math.pow(x[j], alpha));
				}

				if (i != 0) {
					f[i] *= Math.sin(0.5 * Math.PI
							* Math.pow(x[numberOfObjectives - i - 1], alpha));
				}
			}

			solution.setObjectives(f);
		}

		@Override
		public Solution generate() {
			Solution solution = newSolution();

			for (int i = 0; i < numberOfObjectives - 1; i++) {
				((RealVariable)solution.getVariable(i)).setValue(PRNG.nextDouble());
			}

			for (int i = numberOfObjectives - 1; i < numberOfVariables; i++) {
				((RealVariable)solution.getVariable(i)).setValue(0.5);
			}

			evaluate(solution);

			return solution;
		}


		

	
		
	}


	public static void main(String[] args) throws IOException {

   
   	  NondominatedPopulation nsgaResults = new Executor()
   			 // .withProblem("ZDT2")           // put your ZDT1,ZDT2 problem here 
              .withProblemClass(MyDTLZ4.class) // put your modified problem here
              .withAlgorithm("NSGA-II")
              .withMaxEvaluations(10000)
              .withProperty("populationSize", 100)
              .run();
   	  
	  NondominatedPopulation moeadResults = new Executor()
			  //.withProblem("ZDT2")            //put your ZDT1, ZDT2 problem here 
              .withProblemClass(MyDTLZ4.class) // put your modified problem here
              
              .withAlgorithm("MOEA/D")
              .withMaxEvaluations(10000)
              .withProperty("populationSize", 100)
              .run();
   	  
	  //analyzer 
	  Analyzer analyzer = new Analyzer()
			  	//.withProblem("ZDT2")              //put your ZDT1, ZDT2 problem here 
				.withProblemClass(MyDTLZ4.class)    //put your modified problem here 
				.showStatisticalSignificance()
				.includeHypervolume()
				.showAggregate();
	  
	 //add the results to analyser
	 analyzer.add("NSGA-II", nsgaResults);
	 analyzer.add("MOEA/D", moeadResults);
	 
	 
	 //print hypervolume metrics
	 analyzer.printAnalysis();
	  
	 
	  //scatter plot
   	  new Plot()
   	      .add("NSGA-II", nsgaResults)
   	      .add("MOEA/D", moeadResults)
   	      .setXLabel("NSGA-II")
   	      .setYLabel("MOEA/D")
   	      .show();
   	  
   	//check for myDTLZ3 and myDTLZ4 class with 3 objectives 
 	 
 	 System.out.format("Objective1  Objective2 Objective3%n");
 		
 		for (Solution solution : nsgaResults) {
 			System.out.format("%.4f      %.4f  %.4f%n", 
 					solution.getObjective(0),
 					solution.getObjective(1),
 					solution.getObjective(2));
 		}
 	 
 
   	        

		
	}
	
}
