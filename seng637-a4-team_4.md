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
### Test suite access
* The **final (updated) test suite** of **Range class** can be found here: JFreeChart_Lab4/src/org/jfree/data/rangetest
* The **final (updated) test suite** of **DataUtilities class** can be found here: JFreeChart_Lab4/src/org/jfree/data/datautilitiestest
* The **original test suite** of **Range class** can be found here: JFreeChart_Lab4/src/org/jfree/data/rangetestbefore
* The **original test suite** of **DataUtilities class** can be found here: JFreeChart_Lab4/src/org/jfree/data/datautilitiestestbefore

Please note that, since we have commented out dead code identified by our test cases in the source code of Range and DataUtilities class, the original test suite's mutation score can be higher than the reported score (as it has reduced the no coverage code).

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
* Analysis of the PIT reports for both the Range and DataUtilities classes revealed that most surviving mutants were due to equivalent mutations, leaving little room for improving the mutation scores.
* Furthermore, we removed dead code that contains no coverage, which incread the coverage in the source code.
* We tried to create tests focusing on no coverage, but after careful examination, we noticed some dead code, we removed them, it increased the coverage.
* private methods also reported in the coverage
![alt text](./images/private_class_coverage.png)
* Constructor gets the coverage ![alt text](./images/constructor_coverage.png)
* Coverage for private methods, and constructor causes a drop in the mutation score.

# A discussion on the effect of equivalent mutants on mutation score accuracy
## Equivalent mutants in mutation score accuracy
Equivalent mutants are mutations that do not change the observable behavior of a program, meaning no test case can distinguish them from the original code. Their presence can reduce the accuracy of mutation scores because surviving equivalent mutants are counted as undetected faults, even though the test suite may be strong. This can give a misleading impression that the tests are weak or incomplete, highlighting the need for careful analysis of surviving mutants when interpreting mutation scores.

## An example from Range.shift method
For the **shift(Range base, double delta, boolean allowZeroCrossing) method**, most mutations such as negating the conditional on allowZeroCrossing or replacing arithmetic in base.getLowerBound() + delta—are killed by existing tests. However, some mutants like incrementing or decrementing the local double variables survived. These surviving mutants are likely equivalent because small changes to the local variables do not affect the returned Range in a way that is observable by the current tests. This example illustrates how equivalent mutants can survive without indicating actual test weaknesses, directly impacting mutation score interpretation.

## Automatically detect equivalent mutants
Automatically detecting equivalent mutants is challenging because it requires reasoning about which changes do not affect the behavior.

Consider this part of the shift method:
```
if (allowZeroCrossing) {
    return new Range(base.getLowerBound() + delta,
                     base.getUpperBound() + delta);
} else {
    return new Range(shiftWithNoZeroCrossing(base.getLowerBound(), delta),
                     shiftWithNoZeroCrossing(base.getUpperBound(), delta));
}
```
From the mutant logs, we have several mutants like:

* Mutant 10: Incremented (a++) double local variable number 3 - SURVIVED
* Mutant 11: Decremented (a--) double local variable number 3 - SURVIVED

These correspond to changes in local variables (doubles used in calculations) that do not affect the returned Range object for any input. They are essentially equivalent mutants, because no matter what value we give for base or delta, the final lower and upper bounds of the returned Range remain the same.

One potential approach we can consider is the use of **symbolic execution**, which is widely used in program verification and software security testing in both academia and industry.

We could try out this process with the core elements of the above mention approach:
1. Symbolically track variable usage:
* Treat inputs (base.lower, base.upper, delta, allowZeroCrossing) and local variables as symbolic values.
* Identify which local variables actually contribute to the returned Range (i.e., used in new Range(...) computations).
* If a mutation (like a++ or --a on a local double) changes a variable that does not affect the final lower or upper bounds, mark the mutant as equivalent.

2. Path-sensitive analysis:
* Explore both branches of the allowZeroCrossing conditional.
* Ensure that mutations are tested along all possible execution paths.
* Mutants that never influence the output in any path are considered equivalent.

Furthermore, we thought about this approach's pros, cons, and assumptions:
* Benefits: improves mutation score accuracy, and minimizes manual inspection.
* Disadvantages: Computationally expensive, path explosion in methods with multiple conditionals, and handling floating-point precision can be challenging.
* Assumptions: All relevant input ranges are considered.


# A discussion of what could have been done to improve the mutation score of the test suites
* We can add test cases for all the public methods in both classes.
* We could find a way to test constructors (if possible), and focus on private methods in the classes
* We could find out a good way to cover equivaluent mutants via tests, and ensure increase the test score.

# Why do we need mutation testing? Advantages and disadvantages of mutation testing

# Explain your SELENUIM test case design process

# Explain the use of assertions and checkpoints

# how did you test each functionaity with different test data

# How the team work/effort was divided and managed

# Difficulties encountered, challenges overcome, and lessons learned

# Comments/feedback on the assignment itself
