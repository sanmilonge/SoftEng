# USE CASE: 31 Produce a Report Showing the Population of a City

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Researcher I want the system to produce a report showing the population of a city so that I can track urban population trends.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all cities.

### Success End Condition
A report is available showing the total population of the specified city.

### Failed End Condition
No report is produced.

### Primary Actor
Researcher.

### Trigger
A request for a report showing the population of a specific city is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Researcher specifies the city to include in the report.
2. System retrieves population data for the specified city.
3. System generates the report and provides it to the Researcher for review.

## EXTENSIONS
**City not found or population data unavailable:**
1. System informs the Researcher that the city cannot be found or data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
