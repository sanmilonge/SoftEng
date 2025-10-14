# USE CASE: 26 Produce a Report Showing the Total Population of the World

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Researcher I want the system to produce a report showing the total population of the world so that I can use it as a reference for global population studies.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all countries in the world.

### Success End Condition
A report is available showing the total world population.

### Failed End Condition
No report is produced.

### Primary Actor
Researcher.

### Trigger
A request for a report showing the total population of the world is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Researcher requests a report showing the total population of the world.
2. System retrieves and sums population data from all countries.
3. System generates the report and provides it to the Researcher for review.

## EXTENSIONS
**Population data incomplete or unavailable:**
1. System informs the Researcher that population data is incomplete or unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
