# USE CASE: 30 Produce a Report Showing the Population of a District

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Researcher I want the system to produce a report showing the population of a district so that I can perform localised studies.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all districts.

### Success End Condition
A report is available showing the total population of the specified district.

### Failed End Condition
No report is produced.

### Primary Actor
Researcher.

### Trigger
A request for a report showing the population of a specific district is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Researcher specifies the district to include in the report.
2. System retrieves population data for the specified district.
3. System generates the report and provides it to the Researcher for review.

## EXTENSIONS
**District not found or population data unavailable:**
1. System informs the Researcher that the district cannot be found or data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
