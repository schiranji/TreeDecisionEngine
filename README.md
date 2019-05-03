# TreeDecisionEngine
Rule based Decision engine where Rules are organized in Tree model. Goal of this project is to keep adding intelligence by adding single concept/tree branch/intelligence at a time. As a start added processors examples for decisioning loan/credit card/home/lineof credit applications.
* Processors can be added dynamically using yml tree structure. Decision Engine reads all processors and executes rules in each processor based on the tree structure defined in processors.yml.
* Decisions are generated only by leaf nodes or stop condition in inside node.
* Once stop condition is reached, rest of the sub tree is ignored and the decision is added to the list.
* Engine can run in either single threaded or multi threaded mode.
* Engine will return list of Results based on processor rules.

#How to Use
1.Build jar and add dependency to your project.
2. Define Request (extends com.techsavy.de.domain.DecisionEngineRequest) Class with all the data required for to make deicsion.
3. Define Response (extends com.techsavy.de.domain.ProcessorResponse) Class with all the data that will be populated by Rules while processing request.
4. Define Processors (extends com.techsavy.de.processor.BaseProcessor) with required rules.
5. Arrange processors in yml file (see test/resources/processors.yml, processors2.yml) in the order of processing.
6. Sample invocation to decision engine can be found in (TestDecisionEngine2.java)


##Sample implementation (Sample2) can be found in test directory.

#Tasks
- [ ] Use InheritableThreadLocal for DecisionEngineRequest.
- [X] See if same level process can be launched in parallel.
- [X] Auditing at Each level of processor and rule
- [X] Throttling for avoiding crashes / out of memory (Using CachedThreadPool for now)
- [X] Enable auditing based on flags.
- [ ] Use static for processor level map?
- [ ] Call method is not taking arguments. See if processors can be pooled for avoiding instantiation overhead. 
- [X] Custom exception and better exception handling at processor failure for timeout.
- [ ] UI for designing rule tree
- [ ] UI for editing and adding processors 
- [ ] UI for adding rules
- [ ] Use Interfaces in method signature

