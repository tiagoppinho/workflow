---
layout: page
root: "../"
---

### Domain Model

The Workflow module comprehends a domain model that supports the [case-handling paradigm][Case-Handling Paradigm].
Case-Handling is a data-driven paradigm to support the execution of business processes. Instead of explicitely specifying the control flow of the activities, case-handling focus on what can be done, i.e. which data objects can be defined.

Although the workflow module is not a pure implementation of the case-handling paradigm, its domain model comprehends the most important primitives and business logic behind such approach.

FIGURE 1 GOES HERE

In Figure 1, you can see how a business process instance is composed by a set of activities and the data objects that are defined within those activities.

FIGURE 2 GOES HERE

Figure 2 depicts the concept of queues, which are objects that are used to handle work allocation: whenever a business process instance is in a queue, it means that all organizational members with access to that queue can view the process and execute their part.




[Case-Handling Paradigm]: http://wwwis.win.tue.nl/~wvdaalst/publications/p252.pdf