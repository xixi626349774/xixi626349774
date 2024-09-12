#include <iostream>
#include <cstdlib>
#include <vector>
#include <string>

void merge(int arr[], int l, int m, int r) {
    int n1 = m - l + 1;
    int n2 = r - m;

    int* L = (int*)std::malloc(n1 * sizeof(int));
    int* R = (int*)std::malloc(n2 * sizeof(int));
    for (int i = 0; i < n1; i++)//left half
        L[i] = arr[l + i];
    for (int j = 0; j < n2; j++)//right half
        R[j] = arr[m + 1+ j];

    int i = 0, j = 0, k = l;
    while (i < n1 && j < n2) {//merge the two halves
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        } else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }

    while (i < n1) {//copy the remaining elements of left half
        arr[k] = L[i];
        i++;
        k++;
    }

    while (j < n2) {//copy the remaining elements of right half
        arr[k] = R[j];
        j++;
        k++;
    }

    std::free(L);
    std::free(R);
}

void mergeSort(int arr[], int l, int r) {
    if (l < r) {
        int m = l+(r-l)/2;
        mergeSort(arr, l, m);//left
        mergeSort(arr, m+1, r);//right
        merge(arr, l, m, r);//merge the two halves
    }
}

void mergeD(double  arr[], int l, int m, int r) {
    int n1 = m - l + 1;
    int n2 = r - m;

    double* L = (double *)std::malloc(n1 * sizeof(double));
    double* R = (double*)std::malloc(n2 * sizeof(double));
    for (int i = 0; i < n1; i++)//left half
        L[i] = arr[l + i];
    for (int j = 0; j < n2; j++)//right half
        R[j] = arr[m + 1+ j];

    int i = 0, j = 0, k = l;
    while (i < n1 && j < n2) {//merge the two halves
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        } else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }

    while (i < n1) {//copy the remaining elements of left half
        arr[k] = L[i];
        i++;
        k++;
    }

    while (j < n2) {//copy the remaining elements of right half
        arr[k] = R[j];
        j++;
        k++;
    }

    std::free(L);
    std::free(R);
}

void mergeSortD(double arr[], int l, int r) {
    if (l < r) {
        int m = l+(r-l)/2;
        mergeSortD(arr, l, m);//left
        mergeSortD(arr, m+1, r);//right
        mergeD(arr, l, m, r);//merge the two halves
    }
}
 
void mergeS(std::vector<std::string>& arr, int l, int m, int r) {
    int n1 = m - l + 1;
    int n2 = r - m;

    std::vector<std::string> L(n1), R(n2);
    for (int i = 0; i < n1; i++)//left half
        L[i] = arr[l + i];
    for (int j = 0; j < n2; j++)//right half
        R[j] = arr[m + 1+ j];

    int i = 0, j = 0, k = l;
    while (i < n1 && j < n2) {//merge the two halves
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        } else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }

    while (i < n1) {//copy the remaining elements of left half
        arr[k] = L[i];
        i++;
        k++;
    }

    while (j < n2) {//copy the remaining elements of right half
        arr[k] = R[j];
        j++;
        k++;
    }  
}

void mergeSortS(std::vector<std::string>& arr, int l, int r) {
    if (l < r) {
        int m = l+(r-l)/2;
        mergeSortS(arr, l, m);//left
        mergeSortS(arr, m+1, r);//right
        mergeS(arr, l, m, r);//merge the two halves
    }
}

int main() {
    int arr[] = {64, 25, 12, 22, 11};
    int n = sizeof(arr) / sizeof(int);
    std::cout<< "Original array: \n";
    for (int i = 0; i < n; i++) {
        std::cout << arr[i] << " ";
    }
    mergeSort(arr, 0, n-1);
    std::cout << "\nSorted array: \n";
    for (int i = 0; i < n; i++) {
        std::cout << arr[i] << " ";
    }
    std::cout << "\n"; 

    double arrD[] = {64.5, 25.2, 12.3, 22.1, 11.9};
    int n1 = sizeof(arrD) / sizeof(double);
    std::cout<< "Original array: \n";
    for (int i = 0; i < n1; i++) {
        std::cout << arrD[i] << " ";
    }
    mergeSortD(arrD, 0, n1-1);
    std::cout << "\nSorted array: \n";
    for (int i = 0; i < n1; i++) {
        std::cout << arrD[i] << " ";
    }
    std::cout << "\n"; 

    std::vector<std::string> arrS = {"apple", "banana", "cherry", "date", "elderberry"};
    int n2 = arrS.size();
    std::cout<< "Original array: \n";
    for (int i = 0; i < n2; i++) {
        std::cout << arrS[i] << " ";
    }
    mergeSortS(arrS, 0, n2-1);
    std::cout << "\nSorted array: \n";
    for (int i = 0; i < n2; i++) {
        std::cout << arrS[i] << " ";
    }
    std::cout << "\n";

    return 0;
}
