#include <iostream>
#include <cstdlib>
#include <cmath>
#include <vector>
#include <string>
#include <random>
#include <chrono>

void insertionSort(std::vector<int>& arr, int n) {
    for (int i = 1; i < n; ++i) {
        int key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {  // Move elements of arr[0..i-1], that are greater
            arr[j + 1] = arr[j];   //than key, to find the position to insert key
            j = j - 1;
        }
        arr[j + 1] = key;//insert key
    }
}

void merge(std::vector<int>& arr, int l, int m, int r) {
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

void improvedMergeSort(std::vector<int>& arr, int n, int k) {
    if (n <= k) {
        insertionSort(arr, n); // use insertion sort for subarrays
    } else {
        int m = n / 2;
        improvedMergeSort(arr, m, k);// recursively sort left half
        std::vector<int> rightHalf(arr.begin() + m, arr.end());
        improvedMergeSort(rightHalf, n - m, k); // recursively sort right half
        merge(arr, 0 , m - 1, n - 1);// merge the two halves
    }
}

void insertionSortD(std::vector<double>& arr, int n) {
    for (int i = 1; i < n; ++i) {
        double key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {  // Move elements of arr[0..i-1], that are greater
            arr[j + 1] = arr[j];   //than key, to find the position to insert key
            j = j - 1;
        }
        arr[j + 1] = key;//insert key
    }
}

void mergeD(std::vector<double>& arr, int l, int m, int r) {
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
 
void improvedMergeSortD(std::vector<double>& arr, int n, int k) {
    if (n <= k) {
        insertionSortD(arr, n); // use insertion sort for subarrays
    } else {
        int m = n / 2;
        improvedMergeSortD(arr, m, k);// recursively sort left half
        std::vector<int> rightHalf(arr.begin() + m, arr.end());
        improvedMergeSort(rightHalf, n - m, k); // recursively sort right half
        mergeD(arr, 0 , m - 1, n - 1);// merge the two halves
    }
}

void insertionSortS(std::vector<std::string>& arr) {
    int n = arr.size();
    for (int i = 1; i < n; ++i) {
        std::string key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {  // Move elements of arr[0..i-1], that are greater
            arr[j + 1] = arr[j];   //than key, to find the poesition to insert key
            j = j - 1;
        }
        arr[j + 1] = key;//insert key
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

void improvedMergeSortS(std::vector<std::string>& arr, int n, int k) {
    if (n <= k) {
        insertionSortS(arr); // use insertion sort for subarrays
    } else {
        int m = n / 2;
        improvedMergeSortS(arr, m, k);// recursively sort left half
        std::vector<std::string> rightHalf(arr.begin() + m, arr.end());
        improvedMergeSortS(rightHalf, n - m, k);// recursively sort right half
        mergeS(arr, 0 , m - 1, n - 1);// merge the two halves
    }
}
  
std::vector<int> generateRandomArray(int n) {  
    std::random_device rd;  
    std::mt19937 gen(rd());  
    std::uniform_int_distribution<> dis(1, 1000000); // The numbers in the dataset are between 1 and 1000000.
  
    std::vector<int> arr(n);  
    for (int i = 0; i < n; ++i) {  
        arr[i] = dis(gen);  
    }  
    return arr;  
}  
  
void measureSortTime(std::vector<int>& arr, int k) {  
    auto start = std::chrono::high_resolution_clock::now();  
    std::vector<int> copy = arr; // copy the original array to avoid the array be modified  
    int size = copy.size();
    improvedMergeSort(copy, size, k);  
    auto end = std::chrono::high_resolution_clock::now();  
      
    std::chrono::duration<double> elapsed = end - start;   
    std::cout << "Sorting time for k = " << k << ": " << elapsed.count() << " seconds\n";  
}

int main() {  
    int n = 100000; // 10w numbers in the dataset 
    std::vector<int> testData = generateRandomArray(n);  
  
    for (int k = 256; k <= 600; k += 20) { // test the time for different k values  
        measureSortTime(testData, k);  
    }  
    

    return 0;  
}