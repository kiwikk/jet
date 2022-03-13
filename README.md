# JET 
Jump Elimination Tool
## How for?
Many programs include some number of goto statements. This leads to an unstructured code, that is difficult
to understand and optimize. A method of elimination goto
statements is proposed.
### Description of the method
The first stage - the program receives as input the source
code containing the jump statements. The stage is implemented
using standard tools of the Java programming language.

The second stage is the transformation of the source code
into a state diagram.
Within the framework of this description, a list of correspondences, consisting of a transition operator and the label it
points to will be called as state diagram.
To do this, we need to collect all the transition operators in
the list in the order they appear. A separate step of the stage
should be the process of collecting all the labels pointed to by
goto statements. This is necessary to accurately determine the
exit point of the jump statement, which can be enclosed, for
example, in the depth of a method with a loop.

The final step is to map each jump operator to its label.
Thus the state diagram is obtained. The third stage is the
implementation of the transformation.

To perform the transformation, we need to create a boolean
variable for each label. The label variable denotes the transition state of this label and can be in two states: true (the
transition has been made) or false (the transition has not been
made). The initial value of each variable is false because no
labels have yet been navigated.

The next step of transformation is to search for jump
statements without labels, that is, without conditions. This is
necessary to create empty labels for such operators, with the
help of which they will be further linearly transformed.
Then we need to eliminate goto statements using instructions for each statement. The templates described in the
previous section can be an example of such instructions.

Another way to expand jump statements is to store the depth
at which they are located in the ancestors labels. The choice of
how operators are processed depends primarily on the depth
they are located.

The fourth stage is the construction of the equivalent code.
The implementation of the stage consists in substituting
the transformed code into the places of the jump operators
in accordance with the depth at which they were located.

Fifth stage - checking the code for equivalence.

Code verification is carried out with the help of Unit-testing
by checking the correspondence between the outputs of the
source code and the modified one.
