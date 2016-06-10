# Beltway
A program that finds solutions for the Beltway Reconstruction problem. 
I am currently working on a paper describing the algorithm (hoping to publish by sometime early 2017) and looking for further optimizations. 
The algorithm appears to outperform the O(n^n log(n)) backtracking algorithm given by Lemke, Skiena, Smith in 
the paper "Reconstructing Sets From Interpoint Distances". Although it does not find all homometric sets, if the goal is to quickly find reconstructions of beltway distance sets, then the algorithm implemented in the program is the best known to me (It should be pretty straightforward to modify it to find all homometric sets). 
