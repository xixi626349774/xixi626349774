#include <iostream>

using namespace std;

//add
void Matrix_Sum(int n, int** MatrixA, int** MatrixB, int** MatrixSum) {
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++)
			MatrixSum[i][j] = MatrixA[i][j] + MatrixB[i][j];
}

//subtract
void Matrix_Sub(int n, int** MatrixA, int** MatrixB, int** MatrixSub) {
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++)
			MatrixSub[i][j] = MatrixA[i][j] - MatrixB[i][j];
}

//ordinary multiplication
void Matrix_Mul(int n, int **MatrixA, int **MatrixB, int **MatrixMul)
{
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
        {
            MatrixMul[i][j] = 0;
            for (int k = 0; k < n; k++)
                MatrixMul[i][j] = MatrixMul[i][j] + MatrixA[i][k] * MatrixB[k][j];
        }
}

void Strassen(int N, int** MatrixA, int** MatrixB, int** MatrixC ) {
	int n = N / 2; //divide and conquer 
	//allocate memory for each small matrix
	int** MatrixA11 = (int **)malloc(n * sizeof(int *));
	int** MatrixA12 = (int **)malloc(n * sizeof(int *));
	int** MatrixA21 = (int **)malloc(n * sizeof(int *));
	int** MatrixA22 = (int **)malloc(n * sizeof(int *));
	int** MatrixB11 = (int **)malloc(n * sizeof(int *));
	int** MatrixB12 = (int **)malloc(n * sizeof(int *));
	int** MatrixB21 = (int **)malloc(n * sizeof(int *));
	int** MatrixB22 = (int **)malloc(n * sizeof(int *));
	int** MatrixC11 = (int **)malloc(n * sizeof(int *));
	int** MatrixC12 = (int **)malloc(n * sizeof(int *));
	int** MatrixC21 = (int **)malloc(n * sizeof(int *));
	int** MatrixC22 = (int **)malloc(n * sizeof(int *));

	for (int i = 0; i < n ; i++) {   
		MatrixA11[i] = (int *)malloc(n * sizeof(int));
		MatrixA12[i] = (int *)malloc(n * sizeof(int));
		MatrixA21[i] = (int *)malloc(n * sizeof(int));
		MatrixA22[i] = (int *)malloc(n * sizeof(int));
		MatrixB11[i] = (int *)malloc(n * sizeof(int));
		MatrixB12[i] = (int *)malloc(n * sizeof(int));
		MatrixB21[i] = (int *)malloc(n * sizeof(int));
		MatrixB22[i] = (int *)malloc(n * sizeof(int));
		MatrixC11[i] = (int *)malloc(n * sizeof(int));
		MatrixC12[i] = (int *)malloc(n * sizeof(int));
		MatrixC21[i] = (int *)malloc(n * sizeof(int));
		MatrixC22[i] = (int *)malloc(n * sizeof(int));
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
	int** S1 = (int **)malloc(n * sizeof(int *));
	int** S2 = (int **)malloc(n * sizeof(int *));
	int** S3 = (int **)malloc(n * sizeof(int *));
	int** S4 = (int **)malloc(n * sizeof(int *));
	int** S5 = (int **)malloc(n * sizeof(int *));
	int** S6 = (int **)malloc(n * sizeof(int *));
	int** S7 = (int **)malloc(n * sizeof(int *));
	int** S8 = (int **)malloc(n * sizeof(int *));
	int** S9 = (int **)malloc(n * sizeof(int *));
	int** S10 = (int **)malloc(n * sizeof(int *));

	for (int i = 0; i < n; i++) {  
		S1[i] = (int *)malloc(n * sizeof(int));
		S2[i] = (int *)malloc(n * sizeof(int));
		S3[i] = (int *)malloc(n * sizeof(int));
		S4[i] = (int *)malloc(n * sizeof(int));
		S5[i] = (int *)malloc(n * sizeof(int));
		S6[i] = (int *)malloc(n * sizeof(int));
		S7[i] = (int *)malloc(n * sizeof(int));
		S8[i] = (int *)malloc(n * sizeof(int));
		S9[i] = (int *)malloc(n * sizeof(int));
		S10[i] = (int *)malloc(n * sizeof(int));
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
	int** M1 = (int **)malloc(n * sizeof(int *));
	int** M2 = (int **)malloc(n * sizeof(int *));
	int** M3 = (int **)malloc(n * sizeof(int *));
	int** M4 = (int **)malloc(n * sizeof(int *));
	int** M5 = (int **)malloc(n * sizeof(int *));
	int** M6 = (int **)malloc(n * sizeof(int *));
	int** M7 = (int **)malloc(n * sizeof(int *));

	for (int i = 0; i < n; i++) {  
		M1[i] = (int *)malloc(n * sizeof(int));
		M2[i] = (int *)malloc(n * sizeof(int));
		M3[i] = (int *)malloc(n * sizeof(int));
		M4[i] = (int *)malloc(n * sizeof(int));
		M5[i] = (int *)malloc(n * sizeof(int));
		M6[i] = (int *)malloc(n * sizeof(int));
		M7[i] = (int *)malloc(n * sizeof(int));
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
    //free memory
	free(MatrixA11);free(MatrixA12);free(MatrixA21);free(MatrixA22);
	free(MatrixB11);free(MatrixB12);free(MatrixB21);free(MatrixB22);
	free(MatrixC11);free(MatrixC12);free(MatrixC21);free(MatrixC22);
    free(S1);free(S2);free(S3);free(S4);free(S5);free(S6);free(S7);free(S8);free(S9);free(S10);
    free(M1);free(M2);free(M3);free(M4);free(M5);free(M6);free(M7);
}

void print(int** MatrixA, int n) {
	for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++)
			cout << MatrixA[i][j] << " ";
	cout << endl;
	}
	cout << endl;
}

int main() {
	int N = 4;
    int** MatrixA = (int **)malloc(N * sizeof(int *));
    int** MatrixB = (int **)malloc(N * sizeof(int *));
    int** MatrixC = (int **)malloc(N * sizeof(int *));
    for (int i = 0; i < N; i++) {
        MatrixA[i] = (int *)malloc(N * sizeof(int));
        MatrixB[i] = (int *)malloc(N * sizeof(int));
        MatrixC[i] = (int *)malloc(N * sizeof(int));
    }
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            MatrixA[i][j] = 1;
            MatrixB[i][j] = 2;
        }
    }
    Strassen(N, MatrixA, MatrixB, MatrixC);
    print(MatrixC, N);
    free(MatrixA);//free memory
    free(MatrixB);
    free(MatrixC);
    return 0;
    }

