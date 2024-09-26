#include <iostream>

using namespace std;

//add
void Matrix_Sum(int N, int** MatrixA, int** MatrixB, int** MatrixSum) {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MatrixSum[i][j] = MatrixA[i][j] + MatrixB[i][j];
}

//subtract
void Matrix_Sub(int N, int** MatrixA, int** MatrixB, int** MatrixSub) {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MatrixSub[i][j] = MatrixA[i][j] - MatrixB[i][j];
}

//ordinary multiplication
void Matrix_Mul(int N, int **MatrixA, int **MatrixB, int **MatrixMul)
{
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
        {
            MatrixMul[i][j] = 0;
            for (int k = 0; k < N; k++)
                MatrixMul[i][j] = MatrixMul[i][j] + MatrixA[i][k] * MatrixB[k][j];
        }
}

//Strassen multiplication
void Strassen(int MatrixSize, int** MatrixA, int** MatrixB, int** MatrixC ) {
	int n = MatrixSize / 2; //divide and conquer 
	//allocate memory for each small matrix
	int** MatrixA11 = new int*[n];
	int** MatrixA12 = new int*[n];
	int** MatrixA21 = new int*[n];
	int** MatrixA22 = new int*[n];
	int** MatrixB11 = new int*[n];
	int** MatrixB12 = new int*[n];
	int** MatrixB21 = new int*[n];
	int** MatrixB22 = new int*[n];
	int** MatrixC11 = new int*[n];
	int** MatrixC12 = new int*[n];
	int** MatrixC21 = new int*[n];
	int** MatrixC22 = new int*[n];

	for (int i = 0; i < n ; i++) {   
		MatrixA11[i] = new int[n];
		MatrixA12[i] = new int[n];
		MatrixA21[i] = new int[n];
		MatrixA22[i] = new int[n];
		MatrixB11[i] = new int[n];
		MatrixB12[i] = new int[n];
		MatrixB21[i] = new int[n];
		MatrixB22[i] = new int[n];
		MatrixC11[i] = new int[n];
		MatrixC12[i] = new int[n];
		MatrixC21[i] = new int[n];
		MatrixC22[i] = new int[n];
	}
	//divide the matrices into 4 sub-matrices
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++) {
			MatrixA11[i][j] = MatrixA[i][j];
			MatrixA12[i][j] = MatrixA[i][j + n];
			MatrixA21[i][j] = MatrixA[i + n][j];
			MatrixA22[i][j] = MatrixA[i + n][j + n];

			MatrixB11[i][j] = MatrixB[i][j];
			MatrixB12[i][j] = MatrixB[i][j + n];
			MatrixB21[i][j] = MatrixB[i + n][j];
			MatrixB22[i][j] = MatrixB[i + n][j + n];
		}		

	//calculate the 7 sub-matrices
	int** S1 = new int*[n];
	int** S2 = new int*[n];
	int** S3 = new int*[n];
	int** S4 = new int*[n];
	int** S5 = new int*[n];
	int** S6 = new int*[n];
	int** S7 = new int*[n];
	int** S8 = new int*[n];
	int** S9 = new int*[n];
	int** S10 = new int*[n];

	for (int i = 0; i < n; i++) {  
		S1[i] = new int[n];
		S2[i] = new int[n];
		S3[i] = new int[n];
		S4[i] = new int[n];
		S5[i] = new int[n];
		S6[i] = new int[n];
		S7[i] = new int[n];
		S8[i] = new int[n];
		S9[i] = new int[n];
		S10[i] = new int[n];
	}
	//add and subtract
	Matrix_Sub(n, MatrixA12, MatrixA22, S1);
	Matrix_Sum(n, MatrixB21, MatrixB22, S2);
	Matrix_Sum(n, MatrixA11, MatrixA22, S3);
	Matrix_Sum(n, MatrixB11, MatrixB22, S4);
	Matrix_Sub(n, MatrixA11, MatrixA21, S5);
	Matrix_Sum(n, MatrixB11, MatrixB12, S6);
	Matrix_Sum(n, MatrixA11, MatrixA12, S7);
	Matrix_Sub(n, MatrixB12, MatrixB22, S8);
	Matrix_Sub(n, MatrixB21, MatrixB11, S9);
	Matrix_Sum(n, MatrixA21, MatrixA22, S10);

	//multiplication
	int** M1 = new int*[n];
	int** M2 = new int*[n];
	int** M3 = new int*[n];
	int** M4 = new int*[n];
	int** M5 = new int*[n];
	int** M6 = new int*[n];
	int** M7 = new int*[n];

	for (int i = 0; i < n; i++) {  
		M1[i] = new int[n];
		M2[i] = new int[n];
		M3[i] = new int[n];
		M4[i] = new int[n];
		M5[i] = new int[n];
		M6[i] = new int[n];
		M7[i] = new int[n];
	}
	Matrix_Mul(n, S1, S2, M1);
	Matrix_Mul(n, S3, S4, M2);
	Matrix_Mul(n, S5, S6, M3);
	Matrix_Mul(n, S7, MatrixB22, M4);
	Matrix_Mul(n, MatrixA11, S8, M5);
	Matrix_Mul(n, MatrixA22, S9, M6);
	Matrix_Mul(n, S10, MatrixB11, M7);

	//Calculate the final result using Strassen's algorithm
	Matrix_Sum(n, M1, M2, MatrixC11);
	Matrix_Sub(n, MatrixC11, M4, MatrixC11);
	Matrix_Sum(n, MatrixC11, M6, MatrixC11);

	Matrix_Sum(n, M4, M5, MatrixC12);
	Matrix_Sum(n, M6, M7, MatrixC21);

	Matrix_Sub(n, M2, M3, MatrixC22);
	Matrix_Sum(n, MatrixC22, M5, MatrixC22);
	Matrix_Sub(n, MatrixC22, M7, MatrixC22);

	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++) {
			MatrixC[i][j] = MatrixC11[i][j];
			MatrixC[i][j + n] = MatrixC12[i][j];
			MatrixC[i + n][j] = MatrixC21[i][j];
			MatrixC[i + n][j + n] = MatrixC22[i][j];
		}
}

//initialize matrix with random numbers
void Init_Matrix(int MatrixSize, int** MatrixA) {
	for (int i = 0; i < MatrixSize; i++) {
		for (int j = 0; j < MatrixSize; j++) {
			MatrixA[i][j] = rand() % 10 + 1;//1~10
		}
	}
}

int main() {
	clock_t Original_start, Original_end, Strassen_start, Strassen_end;
    int MatrixSize;  
    for(MatrixSize = 32; MatrixSize <= 1024*32; MatrixSize *= 2){
        //allocate memory for matrices
	int** MatrixA =  new int*[MatrixSize];
	int** MatrixB = new int*[MatrixSize];
	int** MatrixC = new int*[MatrixSize];
	int** MatrixD = new int*[MatrixSize];  
	for (int i = 0; i < MatrixSize; i++) {
		MatrixA[i] = new int[MatrixSize];
		MatrixB[i] = new int[MatrixSize];
		MatrixC[i] = new int[MatrixSize];
		MatrixD[i] = new int[MatrixSize];
	}
	Init_Matrix(MatrixSize, MatrixA);
	Init_Matrix(MatrixSize, MatrixB);
    cout << "----------MatrixSize: " << MatrixSize << "----------" << endl;
    
	Original_start = clock();
	Matrix_Mul(MatrixSize, MatrixA,MatrixB, MatrixD);
    Original_end = clock();
	cout << "Time for original " << (Original_end - Original_start) << " Clocks" << endl;
	
	Strassen_start = clock();
	Strassen(MatrixSize, MatrixA, MatrixB, MatrixC);
	Strassen_end = clock(); 
	cout << "Time for Strassen " << (Strassen_end - Strassen_start) << " Clocks" << endl;
    }
    return 0;
}


                             

