# Project Plan

**Author**: Team 59

Version: 1.1

| Version | Date | Description |
| --------| -----| ------------|
| 1.0 | 06/28/2019 | Initial Draft D2 Submission|
| 1.1 | 07/11/2019 | Minor formatting updates for D4 Submission|

## 1 Introduction

This project plan outlines the software development process for the construction and deployment of a multiplayer cryptogram game application on the Android platform.

## 2 Process Description

Team 59 will employ the Unified Software Process model to develop an Android app that implements the cryptogram game.

#### I. Inception
During the Inception phase, the team will perform **activities** to identify the requirements and tasks of the Cryptogram game from both the players' and the administrator's perspective. The goal to create an **initial use case document**, a **high level UML class diagram**, and a **initial project plan**.

The **inputs** needed for our initial use case document and UML class diagram were mainly obtained from the assignment 5 write up and from discussions on the Piazza forum. Additionally, after the individual UML class diagrams were submitted, team members discussed among themselves to create a singular team UML class diagram of the cryptogram app. The team also discussed the requirements for the initial use case using the content from the assignment 5 document. The concepts from the videos and notes from lecture P3L4 were used to formulate the initial project plan.

The **first output** is a completed UML diagram of the team's interpretation of the cryptogram app architecture. It incorporates the best features from the team member's individual design. The **second output** is a completed initial use case that captures the game requirements, pre-conditions, post-conditions, and scenarios. The content will include the game features and all of the stakeholder's tasks. The **third output** is the initial project plan that contains the phases and iterations of the app's development and the roles and responsibilities of the team members. 

#### II. Elaboration
During the elaboration phase the team will perform **activities** to analyze the user requirements and problem domain in detail and dive deeper into the critical use cases to ensure that they captured all of the requirements of the cryptogram game, user's task, and scenarios at the appropriate fidelity while eliminating the high risk elements of the application. Additionally, the team will go deeper into the architectural design of the application and its technical components. 

The **inputs** are the initial use case documents from the inception phase, the requirements from assignment 5, team UML diagram, Piazza discussions, team discussions over WebEx, and concepts and techniques from lectures P2L1, P2L2, P3L1, P3L2, P3L3, P3L4, and P4L1. The team also took into consideration the constraints of the Android operating system. 

The team's **outputs** are:

- Detailed and finalized Project plan
- Detail Use Case Model 
- Design document that contains:
	- Assumptions, background, and dependencies of cryptogram game
	- System constraints and hardware and software environment
	- High level design of the architecture
	- Component diagram that shows the logical and functional parts of the application
	- Final Team UML Class Diagram
	- User interface design in the form of Front End mockup screens
	- Deployment diagram to show how different components will be deployed on android devices.
- Test Plan that contains: 
	- Overall testing strategy for the cryptogram game from the perspective of players and the administrator
	- Techniques for test selection
	- Techniques to assess the quality of the test cases
	- Description of how bugs and enhancement requests are tracked
	- Description of technology used for testing
- Extra Requirements document that contains:
	- Requirements that do not fit the use case model such as non-functional aspects of the cryptogram game app

Members of the team will review the documents that they did not primarily draft and provide their feedback. The original author of the documents will review and discuss the feedback and make the appropriate changes and updates.The **satisfaction** criterion will be unanimous agreement on the details each section of the above documents by every member of the team. A document is considered **completed** if it has verbal sign off by every member of the team.

#### III. Construction
During the construction phase the team will perform **activities** to build the cryptogram game application. Specifically, the team will work together to develop component code for an initial version of the application. The team will also execute testing of the code and draft an initial manual.

The first **inputs** to the construction of the application are the use case model and the design document. The primary developers on the team will use these documents to begin to code the framework and the components and unit tests on Android Studio. They will also use the team's Github repository to share code.

The first **outputs** would be an alpha version of the cryptogram application where most of the critical requirements of the use case model realized and the core functionality implemented. If revisions to the use case model or design diagrams are required, then the appropriate documents will be updated.

The second **inputs** to the construction phase is the alpha version of the cryptogram application and the test plan and use case model.

The second **output** is an executed test plan where the results of the test are added to the test plan document with detail output and explanations.

The third **input** is the alpha version of the cryptogram game application.

The third **output** is a draft of the manual with detailed instructions and screen shots of the application.

#### IV. Transition

During the transition phase the developers will review the test plan results and perform **activities** to fix obvious bugs and to resolve errors found during the execution of the test plan. Unit, integration, and regression testing are also performed at this stage. The developers will also polish the application's performance and the UI's aesthetics. Additionally, any revisions to the aforementioned documents due to updating the application code base will be finalized.

The first **inputs** to the transition phase is the alpha version of the application, use case models, test plan, and design documents.

The first **outputs** produced are the updated cryptogram application that is considered beta quality, and a final version of the use case models and design documents reflecting any revisions performed during the transition stage

The second **inputs** are the updated cryptogram application and the most recent test plan.

The **output** is an updated test plan, where tests that failed in the previous stage are executed again and the results logged. Also regression testing is performed where the existing tests that passed are executed again to make sure nothing is broken. Any failures will need to be immediately addressed and the code updated and the tests re-executed. 

The third **inputs** is the final version of the application and the current version of the manual.

The third **output** is the final version of the manual that reflects the latest application. If necessary, screenshots of the UI and tasks descriptions are updated.

The last activity is to commit and push the final versions of the cryptogram game application, use case model, design document, test plan, project plan, manual, extra requirements document to the Git repository. The team will also conduct a final meeting on the lessons learnt, and discuss what worked, what didn't work, and what future enhancements can performed in a hypothetical next cycle.

## 3 Team

#### Team Members
| Name | GTID | Email |
| :---------------- | :--------------- |:--------------- |
|Deepti Venkatesh| dvenkatesh7| dvenkatesh7@gatech.edu|
|Vaneet Verma|vverma41|vaneetverma@yahoo.com|
|Chun Wong|cwong65|chun.wong@gatech.edu|
|Yurong Fan|yfan329|yurongfan@gmail.com|
  
#### Roles Description
- **Project Manager**: The Project Manager is responsible for coordinating the project requirements and communicating tasks and deadlines with every member of the team. Additional responsibilities of the PM is to ensure that the activities performed by the team adheres to the project plan and that all the documents and code base are updated and committed to the Github repository. The PM is also responsible for updating and revising the use case model, project plan, manual, and the extra requirements document. 
- **Development Lead**: The Development Lead is responsible for defining the design of both the front end and the back end architecture and setting guidelines for the development and the integration of components. The Development Lead is responsible for ensuring that the design realizes the requirements from the use case model and the implementation conforms to the design document. The development lead is also responsible for reviewing and finalizing the code base and updating and revising the UML class model and the design document. 
- **Test Lead**: The Test lead is responsible for designing the test plan and coordinating the execution of the plan within the context of functional testing, unit testing, regression testing, and integration testing. The Test Lead will also collect and summarize the test results. The Test Lead is also responsible for updating and revising the testing plan document.
- **Front End Lead**: The Front End Lead is responsible for creating the design of the UI/UX and coordinating with the Front End engineers to implement the visual user interface.
- **Developer**: The developer will code the components of the application & unit tests and integrate it with the application. They will take direction from the Development Lead.
- **Tester**: The tester will execute the test cases and record the results. They will take direction from the Test Lead.
- **Front End Engineer**: The Front End Engineer will implement the visual elements and the user interface. They will take direction from the Front End Lead.

#### Team members and roles

| Member | Roles |
| :---------------- | :--------------- |
| Chun Wong | Project Manager, Developer, Tester|
| Deepti Venkatesh | Development Lead, Front End Engineer, Tester |
| Vaneet Verma | Front End Lead, Developer, Tester | 
| Yurong Fan | Test Lead, Front End Engineer, Developer |