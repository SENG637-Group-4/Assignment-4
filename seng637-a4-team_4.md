**SENG 637 - Dependability and Reliability of Software Systems**

**Lab. Report \#4 – Mutation Testing and Web app testing**

| Group 4      |
|-----------------|
| Zohara Kamal            |   
| Thanoshan Vijayanandan          |   
| Minh Le                |   
| Shuvam Agarwala              |

# 1. Introduction

# 2. Analysis of 10 Mutants of the Range class 

# 3. Report all the statistics and the mutation score for each test class
**The final test suite of Range class can be found here: JFreeChart_Lab4/src/org/jfree/data/rangetest**

**The final test suite of DataUtilities class can be found here: JFreeChart_Lab4/src/org/jfree/data/datautilitiestest**

TODO: hyper-link above
TODO: remove before packages

## 3.1 Range class
### 3.1.1 Before
#### Mutation score
![alt text](./images/mutation_score_range_before_1.png)

#### Mutation statistics
![alt text](./images/mutation_stat_range_before_1.png)
![alt text](./images/mutation_stat_range_before_2.png)

### 3.1.2 After
#### Mutation score
![alt text](./images/mutation_score_range_after_1.png)

#### Mutation statistics
![alt text](./images/mutation_stat_range_after_1.png)
![alt text](./images/mutation_stat_range_after_2.png)

## 3.1 DataUtilities class
### 3.1.1 Before
#### Mutation score
![alt text](./images/mutation_score_datautilities_before_1.png)

#### Mutation statistics
![alt text](./images/mutation_stat_datautilities_before_1.png)

![alt text](./images/mutation_stat_datautilities_before_2.png)

### 3.1.2 After
#### Mutation score
![alt text](./images/mutation_score_datautilities_after_1.png)

#### Mutation statistics
![alt text](./images/mutation_stat_datautilities_after_1.png)

![alt text](./images/mutation_stat_datautilities_after_2.png)

# Analysis drawn on the effectiveness of each of the test classes
* For the DataUtilities class, we removed dead codes in the three places. We added some mutation test code.
* Identify one example to talk here in the Range class 
* When we analyzed the Pitest reports for both the Range class and DataUtilities class, we found that most of the surviving mutants were because of equivalent mutations. Therefore, very little could have been done to improve upon the mutation scores.
* Furthermore, we removed dead code that contains no coverage, which incread the coverage in the source code.
* We tried to create tests focusing on no coverage, but after careful examination, we noticed some dead code, we removed them, it increased the coverage.
* private classes also reported in the coverage
![alt text](./images/private_class_coverage.png)
* Constructor gets the coverage ![alt text](./images/constructor_coverage.png)


# A discussion on the effect of equivalent mutants on mutation score accuracy

# A discussion of what could have been done to improve the mutation score of the test suites

# Why do we need mutation testing? Advantages and disadvantages of mutation testing

# Explain your SELENUIM test case design process

# Explain the use of assertions and checkpoints

# how did you test each functionaity with different test data

# How the team work/effort was divided and managed

# Difficulties encountered, challenges overcome, and lessons learned

# Comments/feedback on the assignment itself
