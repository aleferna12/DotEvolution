# Dot Evolution

A simple simulation of evolution mediated by natural selection in organisms represented by colored dots.

The dot species has traits such as:

- Radius
- Speed
- Parental investment
- Color

which evolve due to mutations and are selected in the population due to intra-specific competition.

The simulation can also be sped up or slowed down and generates statistics and graphics to visualize the evolution of the different traits.

## How it works

> There are many options available to experiment with in Dot Evolution. To access them, click the "Settings" button in the lower-left corner of the simulation.

When the user clicks the screen, a bunch of dot organisms are spawned. They are quite similar to each other, but vary slightly in their traits (around a mean defined under "Evolving paramaters" in the settings menu).

The dots then rapidly mature, and start accumulating the food they come across. How well they can gather food depends on characteristics such as their size, speed and how well they can turn left and right. However, a large size or speed also mean higher metabolic needs, meaning that a big dot consumes its food faster than a small one. Because of this, the success of a particular dot depends on a thin balance between efficiency and cost.

When a dot has accumulated enough food and comes across another member of the dot species they may copulate, thereby generating a child. This process consumes food from both parents, which is then given to the child dot as an investment to start its life. How much food a dot is willing to invest in its child is, in itself, a trait that evolves, which gives the opportunity to both precocial and altricial life forms to arise.

The simulation algorithm assumes that the traits of the child are determined by many genes interacting with each other, resulting in quantitative inheritance. This means that the child will not directly inherit the characteristics of either parent, but rather acquire a new phenotype which is somewhere between those of its parents[^1].

Lastly, the child's traits are mutated. Mutation is important since it gives the new organism a chance to be even better fit to the environment than its parents. In reality, mutations may occur at any time in an organism's life, but for simplicity the algorithm of Dot Evolution only mutates each trait once right before the dot is born.

![Dot Evolution](https://i.imgur.com/1qOepSD.png)

[^1]: Dot Evolution's model assumes that all alleles contribute equally to the phenotype as an approximation.

## Installation

1. Download [Java 1.8](https://www.java.com/download/ie_manual.jsp?)
2. Download the [latest jar](https://github.com/aleferna12/DotEvolution/releases/download/v1.0/DotEvolution.jar) release of Dot Evolution
3. Double-click the downloaded file to run it
