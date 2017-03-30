# KnapSack
## AI assignment 3


The (standard) knapsack problem,
can be described in the following way. Assume that you have a set of items, each
with a (positive) weight and a (positive) value, and a knapsack (or bag) with a
limited weight capacity. The problem is to choose (to include in the knapsack)
items so that the weight capacity of the knapsack is not violated and the value
of the chosen items are as high as possible.
Mathematically, the problem can be formulated in the following way. We let
I = {1, . . . , n} be an index set over the n items, where item i ∈ I has a value
pi > 0 and a weight wi > 0, and we let W > 0 denote the weight capacity of the
knapsack. For item i ∈ I, the binary decision variable xi
is used to determine
whether to include item i in the knapsack: xi = 1 if the item is chosen, and
xi = 0 if it is not chosen.
The objective function of the problem is to maximize the utility of the chosen
item.
