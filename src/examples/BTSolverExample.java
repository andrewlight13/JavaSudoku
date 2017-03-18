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
		
		//THIS PROGRAM WILL ONLY RUN EASY PROBLEMS BY DEFAULT
		//to run hard programs, change the number after "sudokuInputFiles" and "sudokuInputFilestoprint" to the corresponding array seen below
		
		String[] sudokuInputFiles1 = {"ExampleSudokuFiles/PE1.txt","ExampleSudokuFiles/PE2.txt","ExampleSudokuFiles/PE3.txt","ExampleSudokuFiles/PE4.txt","ExampleSudokuFiles/PE5.txt"};
		String[] sudokuInputFilestoprint1 = {"PE1.txt","PE2.txt","PE3.txt","PE4.txt","PE5.txt"};
		String[] sudokuInputFiles = {"ExampleSudokuFiles/PH1.txt","ExampleSudokuFiles/PH2.txt","ExampleSudokuFiles/PH3.txt","ExampleSudokuFiles/PH4.txt","ExampleSudokuFiles/PH5.txt"};
		String[] sudokuInputFilestoprint = {"PH1.txt","PH2.txt","PH3.txt","PH4.txt","PH5.txt"};
		String[] sudokuInputFiles2 = {"ExampleSudokuFiles/PH1.txt","ExampleSudokuFiles/PH2.txt","ExampleSudokuFiles/PH3.txt","ExampleSudokuFiles/PH4.txt","ExampleSudokuFiles/PH5.txt"}; //"ExampleSudokuFiles/PM1.txt","ExampleSudokuFiles/PM2.txt","ExampleSudokuFiles/PM3.txt","ExampleSudokuFiles/PM4.txt","ExampleSudokuFiles/PM5.txt",
		String[] sudokuInputFilestoprint2 = {"PH1.txt","PH2.txt","PH3.txt","PH4.txt","PH5.txt"}; 
		boolean setArc = true;						// set this if you want to preprocess the problem using arc consistency
		
		ConsistencyCheck[] consistencyChecks = {ConsistencyCheck.ArcConsistency, ConsistencyCheck.ForwardChecking, ConsistencyCheck.None};
		ValueSelectionHeuristic[] ValueSelection = {ValueSelectionHeuristic.LeastConstrainingValue, ValueSelectionHeuristic.None};
		VariableSelectionHeuristic[] VariableSelection = {VariableSelectionHeuristic.MinimumRemainingValue, VariableSelectionHeuristic.None};
		boolean[] Nakedp = {true, false};
		boolean[] Nakedt = {true, false};
		for(int c=0; c < consistencyChecks.length; ++c) {
			for(int val=0; val < ValueSelection.length; ++val){
				for (int var=0;var < VariableSelection.length; ++var){
					for(int np=0;np < Nakedp.length; ++np){
						for(int nt=0;nt < Nakedt.length; ++nt){
							if (consistencyChecks[c] == ConsistencyCheck.None && (Nakedp[np] || Nakedt[nt]))
								continue;
							System.out.println("\n\n");
							if(setArc) System.out.print("Using AC-3 Preprocessing: ");
							System.out.println("Consistency Check: "+ consistencyChecks[c]+"\nValueSelectionHeuristic: "+ ValueSelection[val] + "\nVariableSelectionHeuristic: " + VariableSelection[var]+ "\nNaked Pair: " + Nakedp[np] + "\nNaked Triple: "+ Nakedt[nt]);
							if (VariableSelection[var] == VariableSelectionHeuristic.Degree)
								if (consistencyChecks[c] == ConsistencyCheck.None && ValueSelection[val] == ValueSelectionHeuristic.None)
									continue;
							double timesum = 0.0;
							double assignmentsum = 0.0;
							double backtrackssum = 0.0;
							for(int i=0; i < sudokuInputFiles1.length; ++i) {
								SudokuFile sf = SudokuBoardReader.readFile(sudokuInputFiles1[i]);
								//System.out.println(sf.toString());
								BTSolver solver = new BTSolver(sf);
								System.out.print(sudokuInputFilestoprint1[i]);
								solver.setConsistencyChecks(consistencyChecks[c]);
								solver.startWithArc = setArc;
								solver.setValueSelectionHeuristic(ValueSelection[val]);
								solver.setVariableSelectionHeuristic(VariableSelection[var]);
								solver.setNakedPair(Nakedp[np]);
								solver.setNakedTriple(Nakedt[nt]);
								
								Thread t1 = new Thread(solver);
								try
								{
									t1.start();
									t1.join(300000);
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
									timesum += solver.getTimeTaken();
									assignmentsum +=  solver.getNumAssignments();
									backtrackssum +=  solver.getNumBacktracks();
									//System.out.println(solver.getSolution());	
									//System.out.println("Solved");

								}

								else
								{
									System.out.println("Failed to find a solution");
								}
							}
							System.out.println("Average Solution Results: ");
							System.out.println("Average runtime: "+ timesum/5 + " ms");
							System.out.println("Average number of assignments per puzzle: " + assignmentsum/5);
							System.out.println("Average number of backtracks per puzzle: " + backtrackssum/5);
							
							
						}
					}
				}
			}
		}
		
		System.out.println("DONE");
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

AC + LCV + MRV
Time taken:20662 ms
Number of assignments: 176
Number of backtracks: 5

naked pair + AC + LCV + MRV
Time taken:23372 ms
Number of assignments: 176
Number of backtracks: 5

both + AC +LCV +MRV
Time taken:19013 ms
Number of assignments: 182
Number of backtracks: 3

both tuples + LCV
Failed to get result

both tuples +LCV + MRV


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
