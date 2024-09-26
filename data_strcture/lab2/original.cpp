#include <iostream>

using namespace std;

// Ordinary matrix multiplication
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

void print(int** MatrixA, int n) {
	for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++)
			cout << MatrixA[i][j] << " ";
	cout << endl;
	}
	cout << endl;
}

int main()
{
    int **MatrixA = (int **)malloc(2 * sizeof(int *));
    for (int i = 0; i < 2; i++)
    {
        MatrixA[i] = (int *)malloc(2 * sizeof(int));
    }
    MatrixA[0][0] = 1;
    MatrixA[0][1] = 2;
    MatrixA[1][0] = 3;
    MatrixA[1][1] = 4;

    int **MatrixB = (int **)malloc(2 * sizeof(int *));
    for (int i = 0; i < 2; i++)
    {
        MatrixB[i] = (int *)malloc(2 * sizeof(int));
    }
    MatrixB[0][0] = 5;
    MatrixB[0][1] = 6;
    MatrixB[1][0] = 7;
    MatrixB[1][1] = 8;
    int **MatrixC = (int **)malloc(2 * sizeof(int *));
    for (int i = 0; i < 2; i++)
    {
        MatrixC[i] = (int *)malloc(2 * sizeof(int));
    }
    Matrix_Mul(2, MatrixA, MatrixB,MatrixC);
    print(MatrixC, 2);
    free(MatrixA);
    free(MatrixB);
    free(MatrixC);
    return 0;
}
