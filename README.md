# Swissborg application

Hello everyone, thanks for reviewing my application to swissborg. I am very excited for this opportunity.
I found very rewarding working on this code challenge, it really hits the points I like: functional programming to model algorithms, using test driven development so be sane,and new tools like `scala-cli`.
I made effort on trying to explan all my tough process through the git history, this work is all from scratch.

I started the repo from the ground up, and made tons of comments and commits trying to explain my though process along the way.

## An algorithmic complexity analysis

Bellman-Ford Algorithm Analysis

1. Initialization
Distances: Initialize the distance to all vertices as infinity (Double.PositiveInfinity), except for the source vertex, which is set to 0.
Predecessors: Initialize the predecessors of all vertices as None.
2. Relaxation of Edges
Iterations: Perform the relaxation step for all edges |V| - 1 times, where |V| is the number of vertices.
Relaxation: For each edge (u, v) with weight w, if distances(u) + w < distances(v), update distances(v) to distances(u) + w and set the predecessor of v to u.
3. Check for Negative Weight Cycles
After the relaxation steps, iterate over all edges to check if a shorter path can still be found. If so, a negative weight cycle exists.
Time Complexity
Initialization: O(V), where V is the number of vertices.
Relaxation: O(V * E), where E is the number of edges. This is because we relax all edges |V| - 1 times.
Negative Cycle Check: O(E), as we need to check all edges once more.
Space Complexity
Distances: O(V), as we store the distance for each vertex.
Predecessors: O(V), as we store the predecessor for each vertex.

## A note about BORG and what are its key features

SwissBorg is a blockchain-based platform focused on accessible and secure cryptocurrency wealth management. It offers a variety of services including a multi-crypto exchange, wealth management tools, and an AI-based investment strategy. Key features include its SwissBorg Earn program for staking and yield farming, the Smart Engine for optimized trading across exchanges, and transparency with real-time proof of funds.

I find exciting finding a swiss company that does crypto but underlying is a wealth management solution. I love mathematics and alls its derivatives, like finance (YES pun intented). I am looking forward to move my career finance and using Functional programming as a tool to do maths. Looking forward to hearing from you.

## Commands

Compile

```bash
scala-cli compile .
```

Run

```bash
scala-cli run .
```

test

```bash
scala-cli test .
```

format

```bash
scala-cli fmt .
```
