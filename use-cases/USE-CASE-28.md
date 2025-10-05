# USE CASE: 28 Produce a Report Showing the Population of a Specific Region

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Researcher I want the system to produce a report showing the population of a specific region so that I can compare regional population variations.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all regions.

### Success End Condition
A report is available showing the total population of the specified region.

### Failed End Condition
No report is produced.

### Primary Actor
Researcher.

### Trigger
A request for a report showing the population of a specific region is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Researcher specifies the region to include in the report.
2. System retrieves and sums population data for the specified region.
3. System generates the report and provides it to the Researcher for review.

## EXTENSIONS
**Region not found or population data unavailable:**
1. System informs the Researcher that the region cannot be found or data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
