# MATH2604 Summative Group Project

The aim of this project was to implement methods for working with diagonal and tridiagonal matrices and then use these to solve a boundary value problem numerically.

The code is organised into three main classes.

Diagonals.java handles diagonal matrices, which are stored as one dimensional arrays where each element represents a diagonal entry. The class includes methods to return an example matrix, compute the inverse by taking reciprocals and perform element wise sum and product. Basic checks are included for null inputs and mismatched array lengths.

Tridiagonals.java works with tridiagonal matrices stored in a 3 x n format. The three rows represent the upper diagonal, main diagonal and lower diagonal. This class includes a method to check whether a matrix is valid, as well as methods for adding two tridiagonal matrices and multiplying a tridiagonal matrix by a diagonal one. It also implements the Thomas algorithm to solve linear systems efficiently without converting to a full matrix.

ODE.java applies these matrix methods to solve the differential equation

f''(x) = cos(x) * f(x) + a x^2

with boundary conditions f(0) = 0 and f(1) = 0.

The interval [0, 1] is divided into n interior points and finite difference approximations are used to form a tridiagonal system. This system is then solved using the solver from the Tridiagonals class. The method returns an approximation to f(0.5). If 0.5 is exactly a grid point, that value is used, otherwise the average of the two nearest values is returned.

The provided DeclarationTest.java file was used to verify that all methods have the correct structure and declarations. All tests pass successfully.

No external libraries are required for the main implementation. JUnit is only needed to run the provided test file. The code does not use packages, in line with the assignment requirements.

Overall, the project shows how using specialised matrix structures simplifies computations and improves efficiency, particularly when solving differential equations numerically.