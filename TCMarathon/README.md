TODO
=====
要將凌亂的矩形放置，並且目標函數為空洞數量平方與面積的積最大，而且還要矩形之間不重疊，如果可以重疊的話，這題有什麼 P 解嗎？

這題能用螞蟻嗎？假設容忍重疊，是否又能攀爬到不重疊的情況 ...

註記：大神推坑不眨眼

最後使用 Gene Algorithm 嘗試看看，但是效果並不好。

![](demo.png)

About
=====
## 2014 Topcoder Marathon Match Round 2 ##
[link](http://community.topcoder.com/longcontest/?module=ViewProblemStatement&compid=40847&rd=15982)

## Problem: RectanglesAndHoles ##

### Problem Statement ###

IMPORTANT: This problem is used for two simultaneous matches: TCO'14 Marathon Round 2 and Marathon Match 84. You can compete in TCO'14 Marathon Round 2 only if you are eligible for TCO'14 and haven't advanced to TCO'14 Marathon Championship Round. You can compete in Marathon Match 84 if you can't compete in TCO'14 Marathon Round 2 or would like to skip it by some reason. Competing in both matches is not allowed. Doing so will lead to disqualification. Note that registration does not count as competing. In order to be considered a competitor, you need to make at least one submit (example or full).

 
You have N rectangles. The dimensions of i-th rectangle are A[i]xB[i]. You also have a plane with Cartesian coordinate system. You need to place all these rectangles onto the plane. The sides of each rectangle must be parallel to coordinate axes. It is allowed to rotate rectangles, so the side with length A[i] can be parallel either to X or to Y axis. Two rectangles must never overlap (have an intersection of strictly positive area), but they can touch each other (have an intersection of zero area).

If we treat rectangles as obstacles, they separate the plane into several regions. Formally, let's call a point on the plane free if it does not lie strictly inside or on border of any of the rectangles. Two free points belong to the same region if and only if it's possible to draw a curve between these two points such that all points on the curve are free.

A region is called a hole if it has a finite area. Let Cnt be the number of holes that your placement of rectangles has formed and let Area be the total area of all these holes. Your goal in this problem is to maximize the value of Cnt^2 * Area.

### Implementation ###

You will need to implement the method place which takes int[]s A and B as input parameters. The return value must be a int[] ret containing 3*N elements. The placement of i-th rectangle is defined by values ret[3*i], ret[3*i+1] and ret[3*i+2]. They mean that the bottom left corner of i-th rectangle is at (ret[3*i], ret[3*i+1]). If ret[3*i+2] is 0, then the side with length A[i] is parallel to X axis, and if ret[3*i+2] is 1, then this side is parallel to Y axis.

In order for your return value to be considered valid, the following restrictions must be fulfilled:

-1,000,000 <= ret[3*i], ret[3*i+1] <= 1,000,000, for any i such that 0 <= i < N; ret[3*i+2] is either 0 or 1, for any i such that 0 <= i < N; rectangles defined by ret do not overlap.

### Scoring ###

For each test case we will calculate your raw and normalized scores. If you were not able to produce a valid return value, then your raw score is -1 and the normalized score is 0. Otherwise, the raw score is equal to Cnt^2 * Area. The normalized score for each test is 1,000,000.0 * YOUR / BEST, where BEST is the highest score currently obtained on this test case (considering only the last submission from each competitor). Finally, your total score is equal to the arithmetic average of normalized scores on all test cases.

You can see your raw scores on each example test case by making an example submit. You can also see total scores of all competitors on provisional test set in the match standings. No other information about scores is available during the match.

 

### Test case generation ###

Each test case is generated as follows:

    N is chosen uniformly, at random, between 100 and 1,000, inclusive.
    Each element of A and B is chosen uniformly and independently, at random, between 1 and 1,000, inclusive.

 

### Tools ###

An offline tester/visualizer is available. You can use it to test/debug your solution locally. You can also check its source code for exact implementation of test case generation and score calculation.
 
Definition
    	
Class:	RectanglesAndHoles
Method:	place
Parameters:	int[], int[]
Returns:	int[]
Method signature:	int[] place(int[] A, int[] B)
(be sure your method is public)
    
 
Notes
-	The time limit is 10 seconds per test case (this includes only the time spent in your code). The memory limit is 1024 megabytes.
-	There is no explicit code size limit. The implicit source code size limit is around 1 MB (it is not advisable to submit codes of size close to that or larger). Once your code is compiled, the binary size should not exceed 1 MB.
-	The compilation time limit is 60 seconds. You can find information about compilers that we use and compilation options here.
-	There are 10 example test cases and 100 full submission (provisional) test cases.