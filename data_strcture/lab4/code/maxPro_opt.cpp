#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <ctime>
#include <chrono>
#include <cstring>

using namespace std;
using namespace std::chrono;

const int N = 50;
const int MAX_TIME = 100;   
const int MAX_PROFIT = 10000;  
const int MAX_DEADLINE = 1000;  

struct Job {
    int id;
    int time;
    int profit;
    int deadline;
};

int job_num;
vector<Job> jobs;
vector<int> jobList;
int f[MAX_DEADLINE + 10];//To avoid segmentation fault

bool compareByDeadline(const Job &a, const Job &b) {
    return a.deadline < b.deadline;
}

int dp__opt(){
    //f[i][j]:All the jobs are [0:n - 1],it's the max profit for the job[0:i] within time j
    memset(f, 0, sizeof(f));
    int n = jobs.size();
    int max_ddl = jobs[n - 1].deadline;
    for(int j = 0; j <= max_ddl; j++){
        if (j >= jobs[0].time && j <= jobs[0].deadline)
            f[j] = jobs[0].profit;
        else
            f[j] = 0;
    }
    for(int i = 1; i < n; i++)
        for(int j = max_ddl; j >= 0; j--){
            if (j >= jobs[i].time && j <= jobs[i].deadline)
                f[j] = max(f[j], f[j - jobs[i].time] + jobs[i].profit);
        }
    
    return f[max_ddl];
}

void testBench(){
    srand(time(0));   

    job_num = rand() % (N) + 1;
    for (int i = 0; i < job_num; i++) {
        Job job;
        job.time = rand() % (MAX_TIME) + 1;
        job.profit = rand() % (MAX_PROFIT) + 1;
        job.deadline = rand() % (MAX_DEADLINE) + 1;
        jobs.push_back(job);
    }

    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for test bench: " << dp__opt() << endl;
}

int main() {
    //To check the correctness  
    jobs = {{0,1, 10, 2}, {1,2, 5, 4}, {2,1, 8, 3}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 1: " << dp__opt() << endl;
    
    jobs = {{0,2, 15, 3}, {1,1, 20, 1}, {2,3, 25, 2}, {3,2, 5, 5}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 2: " << dp__opt() << endl; 
    
    jobs = {{0,1, 10, 3}, {1,2, 15, 5}, {2,1, 5, 2}, {3,3, 12, 6}, {4,2, 8, 7}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 3: " << dp__opt() << endl;

    jobs = {{0,1, 50, 2}, {1,2, 5, 4}, {2,3, 100, 3}, {3,1, 15, 5}, {4,2, 10, 6}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 4: " << dp__opt() << endl;

    jobs = {{0,1, 20, 2}, {1,2, 15, 2}, {2,1, 25, 1}, {3,2, 10, 3}, {4,1, 5, 3}, {5,3, 30, 4}};
    sort(jobs.begin(), jobs.end(), compareByDeadline);
    cout << "Maximum profit for case 5: " << dp__opt() << endl;

    //To test the performance of dp__opt algorithm
    auto start = high_resolution_clock::now();
    testBench();
    auto end = high_resolution_clock::now();
    duration<double> elapsed = end - start;
 
    cout << "Elapsed time for dp__opt: " << elapsed.count() << " seconds" << endl;
 
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