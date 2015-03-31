# NBodies
stuff
# # Possible errors
1) Bodies traveling through one another
2) Collisions occur between timesteps, causing change in all bodies globally
3) Collisions with more than 2 bodies
# # Possible optimizations
1) Consider only a small area around the body
2) Compute accelerations only for pairs, rather than all bodies twice
3) Change in data structure