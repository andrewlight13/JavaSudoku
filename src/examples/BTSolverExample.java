package examples;

import cspSolver.BTSolver;
import cspSolver.BTSolver.ConsistencyCheck;
import cspSolver.BTSolver.ValueSelectionHeuristic;
import cspSolver.BTSolver.VariableSelectionHeuristic;
import sudoku.SudokuBoardGenerator;
import sudoku.SudokuBoardReader;
import sudoku.SudokuFile;

public class BTSolverExample {

	public static void main(String[] args)
	{
		//SudokuFile sf = SudokuBoardGenerator.generateBoard(9, 3, 3, 12);
		SudokuFile sf = SudokuBoardReader.readFile("ExampleSudokuFiles/PH1.txt");
		BTSolver solver = new BTSolver(sf);
		
		solver.setConsistencyChecks(ConsistencyCheck.ForwardChecking);
		solver.setValueSelectionHeuristic(ValueSelectionHeuristic.LeastConstrainingValue);
		solver.setVariableSelectionHeuristic(VariableSelectionHeuristic.MinimumRemainingValue);
		solver.setNakedPair(false);
		solver.setNakedTriple(false);
		
		Thread t1 = new Thread(solver);
		try
		{
			t1.start();
			t1.join(60000);
			if(t1.isAlive())
			{
				t1.interrupt();
			}
		}catch(InterruptedException e)
		{
		}


		if(solver.hasSolution())
		{
			solver.printSolverStats();
			System.out.println(solver.getSolution());	
		}

		else
		{
			System.out.println("Failed to find a solution");
		}

	}
}
/*

----------------------------------------------
				Sample Data
----------------------------------------------

PH1

no tuples, FC, LCV, MRV
Time taken:1755 ms
Number of assignments: 554
Number of backtracks: 375

JUST pairs, all other heuristics the same
Time taken:700 ms
Number of assignments: 178
Number of backtracks: 2

JUST triples
Time taken:813 ms
Number of assignments: 213
Number of backtracks: 36

Both
Time taken:1384 ms
Number of assignments: 185
Number of backtracks: 2


_________________________________
PH2

no tuples, FC, LCV, MRV
Time taken:11021 ms
Number of assignments: 13221
Number of backtracks: 13190

naked pairs
Time taken:1517 ms
Number of assignments: 1451
Number of backtracks: 1433

naked triples
Time taken:778 ms
Number of assignments: 587
Number of backtracks: 569

naked all
Time taken:242 ms
Number of assignments: 134
Number of backtracks: 123
*/