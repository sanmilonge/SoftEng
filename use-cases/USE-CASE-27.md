# USE CASE: 27 Produce a Report Showing the Population of a Specific Continent

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Researcher I want the system to produce a report showing the population of a specific continent so that I can analyse demographic distribution by continent.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all continents.

### Success End Condition
A report is available showing the total population of the specified continent.

### Failed End Condition
No report is produced.

### Primary Actor
Researcher.

### Trigger
A request for a report showing the population of a specific continent is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Researcher specifies the continent to include in the report.
2. System retrieves and sums population data for the specified continent.
3. System generates the report and provides it to the Researcher for review.

## EXTENSIONS
**Continent not found or population data unavailable:**
1. System informs the Researcher that the continent cannot be found or data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
