# Boids 

Boid simulation inspired by the version of The Coding Train and Sebastian Lague. This implementation is done in java with the lwjgl library for the 3D graphics. Contains an efficient implementation of an octree to speed up the boid interactions.

## Concept

This simulation simulates a high amount of agents called "boids" that have similar behaviours to birds. These agents either want to follow the same direction as their neighbours, get closer to them or distance themselves when too close to avoid collision. This gives place to emergent behaviours discovered by Craig Reynolds in 1986.
For more on the subject, go [here](https://en.wikipedia.org/wiki/Boids).

## Goal

The goal of this project was to make a more efficient version of The Coding Train's rendition. Having done it in python before, only a small amount of boids could reasonably coexist but by porting to java and OpenGL, the number of boids interacting with each other could tremendously increase.

### Visuals

![3D boids](https://github.com/tierChampion/Boids/blob/main/src/main/resources/images/3DBoidsSim.png)
