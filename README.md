# Beltway
A program that finds solutions for the Beltway Reconstruction problem. 
I am currently working on a paper describing the algorithm (hoping to publish by sometime early 2017) and looking for further optimizations. 
The program appears to outperform the O(n^n log(n)) backtracking algorithm given by Lemke, Skiena, Smith in 
the 1995 paper "Reconstructing Sets From Interpoint Distances". Although it does not find homometric sets, if the goal is to quickly find reconstructions of beltway distance sets the algorithm implemented in the program is the best known to me. 
