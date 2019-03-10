This example demonstrates 'chain of responsibility' pattern. Where a problem
is being solved in a chain until its solved. 

We use an example of trash sorting machine, which takes a piece of trash
and gives it to different utilizers, until one of them accepts it. 

[#PAPER, #MIXED, #GLASS, #UNKNOWN1, #GREEN_GLASS, #UNKNOWN1, #GLASS, #MIXED, #GLASS, #PAPER]
#PAPER.Utilizing as paper.
#MIXED......Utilizing as mixed trash.
#GLASS...Utilizing as glass.
#UNKNOWN1......Utilizing as mixed trash.
#GREEN_GLASS..Utilizing as bottle.
#UNKNOWN1......Utilizing as mixed trash.
#GLASS...Utilizing as glass.
#MIXED......Utilizing as mixed trash.
#GLASS...Utilizing as glass.
#PAPER.Utilizing as paper.

