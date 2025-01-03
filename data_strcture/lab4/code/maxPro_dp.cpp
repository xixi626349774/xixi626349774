#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <ctime>
#include <chrono>
#include <cstring>

using namespace std;
using namespace std::chrono;

const int N = 260000;
const int MAX_TIME = 100;   
const int MAX_PROFIT = 10000;  
const int MAX_DEADLINE = 1000;  

struct Job {
    int id;
    int time;
    int profit;
    int deadline;
};

vector<Job> jobs;
vector<int> jobList;
int f[N + 10][MAX_DEADLINE + 10];//To avoid segmentation fault

bool compareByDeadline(const Job &a, const Job &b) {
    return a.deadline < b.deadline;
}

int dp_invert(){
    //f[i][j]:All the jobs are [0:n - 1],it's the max profit for the job[i:n - 1] with start time j
    memset(f, 0, sizeof(f));
    jobList.clear();
    int n = jobs.size();
    int max_ddl = jobs[n - 1].deadline;
    for(int i = n - 1; i >= 0; i--)
        for(int j = 0; j <= max_ddl; j++){
            if(j + jobs[i].time > jobs[i].deadline)//Can't benefit from this job,don't consider it
                f[i][j] = f[i + 1][j];
            else{
                f[i][j] = max(f[i + 1][j], f[i + 1][j + jobs[i].time] + jobs[i].profit);
            }      
        }
    
    int j = 0;
    for(int i = 0;i < n;i++){
        if((j + jobs[i].time <= jobs[i].deadline) && (f[i][j] == f[i + 1][j + jobs[i].time] + jobs[i].profit)){
            jobList.push_back(jobs[i].id);
            j += jobs[i].time;
        }
    }
    return f[0][0];
}

void testBench(){
    srand(time(0));   

    for (int i = 0; i < N; i++) {
        Job job;
        job.id = i;
        job.time = rand() % (MAX_TIME) + 1;
        job.profit = rand() % (MAX_PROFIT) + 1;
        job.deadline = rand() % (MAX_DEADLINE) + 1;
        jobs.push_back(job);
    }

    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for test bench: " << dp_invert() << endl;
}

int main() {
    //To check the correctness  
    jobs = {{0,1, 10, 2}, {1,2, 5, 4}, {2,1, 8, 3}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 1: " << dp_invert() << endl;
    cout << "Job order: ( ";
    for (int i = 0; i < jobList.size(); ++i) 
        cout << jobList[i] << " ";
    cout << ")" << endl;
    
    jobs = {{0,2, 15, 3}, {1,1, 20, 1}, {2,3, 25, 2}, {3,2, 5, 5}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 2: " << dp_invert() << endl;
    cout << "Job order: ( ";
    for (int i = 0; i < jobList.size(); ++i) 
        cout << jobList[i] << " ";
    cout << ")" << endl;
    
    jobs = {{0,1, 10, 3}, {1,2, 15, 5}, {2,1, 5, 2}, {3,3, 12, 6}, {4,2, 8, 7}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 3: " << dp_invert() << endl;
    cout << "Job order: ( ";
    for (int i = 0; i < jobList.size(); ++i) 
        cout << jobList[i] << " ";
    cout << ")" << endl;
    
    jobs = {{0,1, 50, 2}, {1,2, 5, 4}, {2,3, 100, 3}, {3,1, 15, 5}, {4,2, 10, 6}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 4: " << dp_invert() << endl;
    cout << "Job order: ( ";
    for (int i = 0; i < jobList.size(); ++i) 
        cout << jobList[i] << " ";
    cout << ")" << endl;
    
    jobs = {{0,1, 20, 2}, {1,2, 15, 2}, {2,1, 25, 1}, {3,2, 10, 3}, {4,1, 5, 3}, {5,3, 30, 4}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 5: " << dp_invert() << endl;
    cout << "Job order: ( ";
    for (int i = 0; i < jobList.size(); ++i) 
        cout << jobList[i] << " ";
    cout << ")" << endl;

    //To test the performance of dp_invert algorithm
    auto start = high_resolution_clock::now();
    testBench();
    auto end = high_resolution_clock::now();
    duration<double> elapsed = end - start;
 
    cout << "Elapsed time for dp_invert: " << elapsed.count() << " seconds" << endl;
 
    return 0;
}

/*input: (t,p,d), output: (job order) max profit
[(1, 10, 2), (2, 5, 4), (1, 8, 3)]
(0, 2, 1) 23
[(2, 15, 3), (1, 20, 1), (3, 25, 2), (2, 5, 5)]	
(1, 0, 3) 40
[(1, 10, 3), (2, 15, 5), (1, 5, 2), (3, 12, 6), (2, 8, 7)]
(2, 0, 1, 4) 38
[(1, 50, 2), (2, 5, 4), (3, 100, 3), (1, 15, 5), (2, 10, 6)]
(2, 3, 4) 125
[(1, 20, 2), (2, 15, 2), (1, 25, 1), (2, 10, 3), (1, 5, 3), (3, 30, 4)]
(2, 5) 55
*/